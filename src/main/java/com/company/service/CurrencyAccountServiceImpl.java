package com.company.service;

import com.company.DataAccess;
import com.company.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CurrencyAccountServiceImpl implements CurrencyAccountService {

    private final Connection con = new DataAccess().getConnection();

    @Autowired
    CardService cardService;

    @Autowired
    CurrencyService currencyService;

    @Override
    public Map<String, String> addAmount(String token, String card_number, String currency, Double amount) throws Throwable {
        Map<String, String> map = new HashMap<>();
        Card card = cardService.getCardByNumber(card_number);
        UUID currency_id = currencyService.getCurrencyByName(currency).getId();
        String query = "insert into public.currency_account values (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setObject(1, UUID.randomUUID());
            preparedStatement.setObject(2, card.getId());
            preparedStatement.setObject(3, currency_id);
            preparedStatement.setDouble(4, amount);
            preparedStatement.execute();
            map.put("message", String.format("amount added on %s card", card.getNumber()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}