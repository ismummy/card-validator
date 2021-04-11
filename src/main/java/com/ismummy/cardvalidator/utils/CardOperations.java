package com.ismummy.cardvalidator.utils;

import com.ismummy.cardvalidator.exception.InvalidInputException;
import com.ismummy.cardvalidator.helpers.CardPayload;
import com.ismummy.cardvalidator.helpers.CardVerificationResponse;
import com.ismummy.cardvalidator.helpers.binlistApiResponse.BinListApiResponse;
import com.ismummy.cardvalidator.models.Card;

public class CardOperations {
    /**
     * This function validates the length of the card
     *
     * @param cardNumber:String
     * @return the card number (truncated to 6 digits) if the input falls within the range of 6 to 19 characters
     * @throws InvalidInputException if the input is invalid
     */
    public static String validateCard(String cardNumber) throws InvalidInputException {

        if (!cardNumber.matches("\\d{6,19}")) {
            throw new InvalidInputException("Invalid Card Number Input");
        }

        if (cardNumber.length() > 6) {
            return cardNumber.substring(0, 6);
        }

        return cardNumber;
    }

    /**
     * This function maps the response from the BinList API to the structure of the database
     *
     * @param cardNumber:String
     * @param binListApiResponse: an object instantiated with the BinListApiResponse class
     * @return the new generated object
     */
    public static Card mapToCard(String cardNumber, BinListApiResponse binListApiResponse) {

        Card cardDetail = new Card();
        cardDetail.setCardNumber(cardNumber);
        cardDetail.setType(binListApiResponse.getType().equals("debit") ? CardType.DEBIT : CardType.CREDIT);
        cardDetail.setBank(binListApiResponse.getBank() == null ? "" : binListApiResponse.getBank().getName());
        cardDetail.setScheme(binListApiResponse.getScheme() == null ? "" : binListApiResponse.getScheme());

        return cardDetail;
    }


    /**
     * This function prepare the Card verification service response
     *
     * @param card:Card
     * @return the new generated object
     */
    public static CardVerificationResponse prepareResponse(Card card) {
        CardVerificationResponse cardVerificationResponse = new CardVerificationResponse();
        CardPayload payload = new CardPayload(card);

        cardVerificationResponse.setPayload(payload);
        cardVerificationResponse.setSuccess(true);

        return cardVerificationResponse;
    }
}
