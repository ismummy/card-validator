package com.ismummy.cardvalidator.helpers.binlistApiResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Country {
    private String numeric;

    private String alpha2;

    private String name;

    private String emoji;

    private String currency;

    private long latitude;

    private long longitude;

}

