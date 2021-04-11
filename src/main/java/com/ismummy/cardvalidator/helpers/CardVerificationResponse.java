package com.ismummy.cardvalidator.helpers;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardVerificationResponse {
    private boolean success;
    private CardPayload payload;
}
