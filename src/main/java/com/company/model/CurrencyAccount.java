package com.company.model;

import java.util.UUID;

public class CurrencyAccount {
    private UUID id;
    private UUID card_id;
    private UUID currency_id;
    private int amount;

    public CurrencyAccount() {

    }

    public CurrencyAccount(UUID id, UUID card_id, UUID currency_id, int amount) {
        this.id = id;
        this.card_id = card_id;
        this.currency_id = currency_id;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCard_id() {
        return card_id;
    }

    public void setCard_id(UUID card_id) {
        this.card_id = card_id;
    }

    public UUID getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(UUID currency_id) {
        this.currency_id = currency_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}