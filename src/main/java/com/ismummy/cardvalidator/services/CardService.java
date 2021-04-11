package com.ismummy.cardvalidator.services;

import com.ismummy.cardvalidator.exception.InvalidInputException;
import com.ismummy.cardvalidator.exception.InvalidPageException;
import com.ismummy.cardvalidator.helpers.CardCountResponse;
import com.ismummy.cardvalidator.helpers.CardPayload;
import com.ismummy.cardvalidator.helpers.binlistApiResponse.BinListApiResponse;
import com.ismummy.cardvalidator.models.Card;
import com.ismummy.cardvalidator.repositories.CardRepository;
import com.ismummy.cardvalidator.helpers.CardVerificationResponse;
import com.ismummy.cardvalidator.utils.CardType;
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
import java.util.Optional;
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
        String validCardNumber = validateCard(cardNumber);


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
        return prepareResponse(card);
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
     * This function validates the length of the card
     *
     * @param cardNumber:String
     * @return the card number (truncated to 6 digits) if the input falls within the range of 6 to 19 characters
     * @throws InvalidInputException if the input is invalid
     */
    private String validateCard(String cardNumber) throws InvalidInputException {

        if (!cardNumber.matches("\\d{6,19}")) {
            throw new InvalidInputException("Invalid Card Number Input");
        }

        if (cardNumber.length() > 6) {
            return cardNumber.substring(0, 6);
        }

        return cardNumber;
    }

    /**
     * This function saves the valid card into the temp database
     *
     * @param cardNumber:String
     */
    private Card saveValidCard(String cardNumber, BinListApiResponse binListApiResponse) {
        Card cardDetail = mapToCard(cardNumber, binListApiResponse);
        return cardRepository.save(cardDetail);
    }

    /**
     * This function maps the response from the BinList API to the structure of the database
     *
     * @param cardNumber:String
     * @param binListApiResponse: an object instantiated with the BinListApiResponse class
     * @return the new generated object
     */
    private Card mapToCard(String cardNumber, BinListApiResponse binListApiResponse) {

        Card cardDetail = new Card();
        cardDetail.setCardNumber(cardNumber);
        cardDetail.setType(binListApiResponse.getType().equals("debit") ? CardType.DEBIT : CardType.CREDIT);
        cardDetail.setBank(binListApiResponse.getBank() == null ? "" : binListApiResponse.getBank().getName());
        cardDetail.setScheme(binListApiResponse.getScheme() == null ? "" : binListApiResponse.getScheme());

        return cardDetail;
    }


    private CardVerificationResponse prepareResponse(Card card) {
        CardVerificationResponse cardVerificationResponse = new CardVerificationResponse();
        CardPayload payload = new CardPayload(card);

        cardVerificationResponse.setPayload(payload);
        cardVerificationResponse.setSuccess(true);

        return cardVerificationResponse;
    }


}
