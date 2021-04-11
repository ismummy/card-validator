package com.ismummy.cardvalidator.helpers.binlistApiResponse;

import lombok.Data;

@Data
public class Number {
    private int length;
    private boolean luhn;

    public Number() {
    }
}
