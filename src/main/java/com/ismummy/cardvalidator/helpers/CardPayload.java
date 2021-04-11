package com.ismummy.cardvalidator.helpers;

import com.ismummy.cardvalidator.exception.GlobalExceptionHandler;
import com.ismummy.cardvalidator.models.Card;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
public class CardPayload {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String scheme;
    private String type;
    private String bank;

    public CardPayload(Card card) {
        logger.error(card.toString());
        this.scheme = card.getScheme();
        this.type = card.getType().getType();
        this.bank = card.getBank();
    }
}
