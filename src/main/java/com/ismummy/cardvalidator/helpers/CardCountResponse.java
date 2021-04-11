package com.ismummy.cardvalidator.helpers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class CardCountResponse {
    private boolean success;
    private int start;
    private int limit;
    private long size;
    private Map<String, Object> payload;
}
