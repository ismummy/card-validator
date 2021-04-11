package com.ismummy.cardvalidator.utils;

public enum CardType {
    DEBIT(Constants.DEBIT), CREDIT(Constants.CREDIT);

    private String type;

    CardType(String type) { this.setType(type); }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
