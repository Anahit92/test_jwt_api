package com.company.service;

import com.company.model.Card;

import java.util.Map;

public interface CardService {
    Map<String, Object> getCards(String token) throws Throwable;

    Map<String, String> addCard(String token, String number, String type) throws Throwable;

    Card getCardByNumber(String number) throws Throwable;
}
