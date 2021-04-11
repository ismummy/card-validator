package com.ismummy.cardvalidator.models;

import com.ismummy.cardvalidator.utils.CardType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;

    @Column(name = "createdAt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    private String scheme;

    @Enumerated(EnumType.STRING)
    private CardType type;

    private String bank;

    public Card() {
    }

    public Card(String cardNumber, String scheme, CardType type, String bank) {
        this.cardNumber = cardNumber;
        this.scheme = scheme;
        this.type  = type;
        this.bank = bank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getScheme() {
        return scheme;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", createdAt=" + createdAt +
                ", scheme='" + scheme + '\'' +
                ", type=" + type +
                ", bank='" + bank + '\'' +
                '}';
    }
}
