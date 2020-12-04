package com.company.service;

import java.util.Map;

public interface CurrencyAccountService {
    Map<String, String> addAmount(String token, String card_number, String currency, Double amount) throws Throwable;
}
