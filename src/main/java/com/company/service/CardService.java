package com.company.service;

import java.util.Map;

public interface CardService {
    Map<String, Object> getCards(String token) throws Throwable;
}
