package com.company.service;

import com.company.DataAccess;
import com.company.model.Currency;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final Connection con = new DataAccess().getConnection();

    @Override
    public Currency getCurrencyByName(String name) throws Throwable {
        Currency currency = new Currency();
        String query = String.format("SELECT * FROM public.currency where name = '%s'", name);
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (!resultSet.isBeforeFirst()) {
                throw new IllegalArgumentException("currency not found");
            }
            while (resultSet.next()) {
                currency.setId((UUID) resultSet.getObject("id"));
                currency.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }

}