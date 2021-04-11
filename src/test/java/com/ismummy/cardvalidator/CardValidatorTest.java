package com.ismummy.cardvalidator;


import com.ismummy.cardvalidator.exception.InvalidInputException;
import com.ismummy.cardvalidator.helpers.CardPayload;
import com.ismummy.cardvalidator.helpers.CardVerificationResponse;
import com.ismummy.cardvalidator.helpers.binlistApiResponse.BinListApiResponse;
import com.ismummy.cardvalidator.repositories.CardRepository;
import com.ismummy.cardvalidator.services.CardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardValidatorTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    RestTemplate restTemplate;


    @InjectMocks
    CardService cardService;

    @Test
    public void throws_error_on_wrong_input() {
        String wrongCardNumber = "1234";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        assertThrows(InvalidInputException.class, () -> {
            cardService.verifyCard(wrongCardNumber, entity);
        });
    }


    @Test
    public void return_correct_response_with_valid_card() {
        String cardNumber = "5399419107596587";

        CardVerificationResponse expectedResponse = new CardVerificationResponse();
        expectedResponse.setSuccess(true);
        expectedResponse.setPayload(new CardPayload());
        expectedResponse.getPayload().setType("debit");
        expectedResponse.getPayload().setScheme("mastercard");
        expectedResponse.getPayload().setBank("GTBANK");

        BinListApiResponse response = new BinListApiResponse();
        response.setBrand("MasterCard");
        response.setScheme("MasterCard");
        response.setType("debit");


        ResponseEntity<BinListApiResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.ACCEPTED);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<BinListApiResponse>>any(),
                ArgumentMatchers.<Class<BinListApiResponse>>any(),
                ArgumentMatchers.anyString()))
                .thenReturn(responseEntity);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        CardVerificationResponse actualResponse = cardService.verifyCard(cardNumber, entity);

        assertTrue(actualResponse.isSuccess());
        assertEquals(expectedResponse.getPayload().getBank(), actualResponse.getPayload().getBank());
        assertEquals(expectedResponse.getPayload().getScheme(), actualResponse.getPayload().getScheme());
        assertEquals(expectedResponse.getPayload().getType().toLowerCase(), actualResponse.getPayload().getType().toLowerCase());
    }

}
