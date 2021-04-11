package com.ismummy.cardvalidator.helpers;

import com.ismummy.cardvalidator.models.Card;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardPayload {
    private String scheme;
    private String type;
    private String bank;

    public CardPayload(Card card) {
        this.scheme = card.getScheme();
        this.type = card.getType().getType();
        this.bank = card.getBank();
    }
}
