package com.company.service;

import com.company.model.Currency;

public interface CurrencyService {
    Currency getCurrencyByName(String name) throws Throwable;
}