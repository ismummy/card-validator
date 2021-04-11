package com.ismummy.cardvalidator.services;

import com.ismummy.cardvalidator.exception.InvalidInputException;
import com.ismummy.cardvalidator.exception.InvalidPageException;
import com.ismummy.cardvalidator.helpers.CardCountResponse;
import com.ismummy.cardvalidator.helpers.CardVerificationResponse;
import com.ismummy.cardvalidator.helpers.binlistApiResponse.BinListApiResponse;
import com.ismummy.cardvalidator.models.Card;
import com.ismummy.cardvalidator.repositories.CardRepository;
import com.ismummy.cardvalidator.utils.CardOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CardService {

    @Value("${binlist.url}")
    String binlistURL;

    //Auto wire the card repository
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    RestTemplate restTemplate;


    public CardVerificationResponse verifyCard(String cardNumber, HttpEntity<?> httpEntity) throws HttpStatusCodeException, InvalidInputException {
        String validCardNumber = CardOperations.validateCard(cardNumber);


        BinListApiResponse binListApiResponse = null;
        ResponseEntity<BinListApiResponse> response = null;

        try {
            response = restTemplate.exchange(binlistURL + "/{validCard}",
                    HttpMethod.GET, httpEntity, BinListApiResponse.class, validCardNumber);
            binListApiResponse = response.getBody();
        } catch (HttpStatusCodeException ex) {
            throw ex;
        }

        Card card = saveValidCard(validCardNumber, binListApiResponse);
        return CardOperations.prepareResponse(card);
    }


    public CardCountResponse getCardsCount(Pageable pageable) throws RuntimeException {
        CardCountResponse cardResponse = new CardCountResponse();

        Page<Map<String, Object>> page = cardRepository.getValidCardCount(pageable);

        if (page == null) {
            throw new InvalidPageException();
        }

        if (page.getSize() < 1L) {
            throw new RuntimeException();
        }

        cardResponse.setStart(page.getNumber() + 1);
        cardResponse.setLimit(page.getSize());
        cardResponse.setSize(page.getTotalElements());
        if (page.hasContent()) {
            cardResponse.setSuccess(true);
            Map<String, Object> payload = new ConcurrentHashMap<>();
            for (Map<String, Object> item : page) {
                payload.put(String.valueOf(item.get("cardNumber")), Integer.parseInt(String.valueOf(item.get("count"))));
            }
            cardResponse.setPayload(payload);
        }

        return cardResponse;
    }

    /**
     * This function saves the valid card into the temp database
     *
     * @param cardNumber:String
     */
    private Card saveValidCard(String cardNumber, BinListApiResponse binListApiResponse) {
        Card cardDetail = CardOperations.mapToCard(cardNumber, binListApiResponse);
        return cardRepository.save(cardDetail);
    }


}
