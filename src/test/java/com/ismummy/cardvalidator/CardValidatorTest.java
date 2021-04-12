package com.ismummy.cardvalidator;


import com.ismummy.cardvalidator.exception.InvalidInputException;
import com.ismummy.cardvalidator.helpers.CardPayload;
import com.ismummy.cardvalidator.helpers.CardVerificationResponse;
import com.ismummy.cardvalidator.services.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CardValidatorApplication.class})
public class CardValidatorTest {
    @Resource
    private CardService cardService;

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
        expectedResponse.getPayload().setBank("ZENITH BANK");


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
