package com.ismummy.cardvalidator.helpers.binlistApiResponse;

import lombok.Data;

@Data
public class Bank {
    private String name;
    private String url;
    private String phone;
    private String city;

    public Bank() {
    }
}
