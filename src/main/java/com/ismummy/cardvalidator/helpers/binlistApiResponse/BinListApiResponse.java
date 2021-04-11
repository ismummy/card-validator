package com.ismummy.cardvalidator.helpers.binlistApiResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BinListApiResponse {
    private Number number;

    private String scheme;

    private String type;

    private String brand;

    private boolean prepaid;

    private Country country;

    private Bank bank;
}
