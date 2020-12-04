package com.company.service;

import com.company.DataAccess;
import com.company.model.Card;
import com.company.model.User;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.*;
import java.util.UUID;

@Service
public class CardServiceImpl implements CardService {

    private final Connection con = new DataAccess().getConnection();

    @Override
    public Map<String, Object> getCards(String token) {
        Map<String, Object> map = new HashMap<>();
        UUID id = getUserByToken(token).getId();
        List<Map<String, String>> currency = getCurrencyAccount(id);

        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format(
                     "select * from card where id in (SELECT card_id from account where user_id = '%s')", id))) {
            while (resultSet.next()) {
                map.put("card_number", resultSet.getString("number"));
                map.put("card_type", resultSet.getString("type"));
                map.put("currency_accounts", currency);
                map.put("total_amount", currencyCalculator(currency));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, String> addCard(String token, String number, String type) throws Throwable {
        Map<String, String> map = new HashMap<>();
        User user = getUserByToken(token);
        UUID card_id = UUID.randomUUID();
        String query = "insert into public.card values (?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setObject(1, card_id);
            preparedStatement.setString(2, number);
            preparedStatement.setString(3, type);
            preparedStatement.execute();
            map.put("message", String.format("card added for %s user", user.getId_number()));
            addAccount(user.getId(), card_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Card getCardByNumber(String number) throws Throwable {
        Card card = new Card();
        String query = String.format("SELECT * FROM public.card where number = '%s'", number);
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (!resultSet.isBeforeFirst()) {
                throw new IllegalArgumentException("card not found");
            }
            while (resultSet.next()) {
                card.setId((UUID) resultSet.getObject("id"));
                card.setNumber(resultSet.getString("number"));
                card.setType(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    public void addAccount(UUID user_id, UUID card_id) throws Throwable {
        String query = "insert into public.account values (?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setObject(1, UUID.randomUUID());
            preparedStatement.setObject(2, user_id);
            preparedStatement.setObject(3, card_id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Object currencyCalculator(List<Map<String, String>> currency) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.currencyfreaks.com/latest?apikey=8f6753fc1dc1445eb1fc604637dd0e62&symbols=AMD,USD,EUR,GEL"))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject ob = new JSONObject(response.body());
        double total = 0.0;
        for (Map<String, String> obj : currency) {
            Double rate = 0.0;
            switch (obj.get("currency")) {
                case "AMD":
                    rate = Double.valueOf(ob.getJSONObject("rates").getString("AMD"));
                    break;
                case "USD":
                    rate = Double.valueOf(ob.getJSONObject("rates").getString("USD"));
                    break;
                case "EUR":
                    rate = Double.valueOf(ob.getJSONObject("rates").getString("EUR"));
                    break;
                case "GEL":
                    rate = Double.valueOf(ob.getJSONObject("rates").getString("GEL"));
                    break;
            }
            total = total + (Double.valueOf(obj.get("amount")) * 1.0 / rate);
        }
        return total;
    }

    private User getUserByToken(String token) {
        User user = new User();
        String query = String.format("SELECT * FROM public.user where token = '%s'", token);
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (!resultSet.isBeforeFirst()) {
                throw new IllegalArgumentException("user not found");
            }
            while (resultSet.next()) {
                user.setId((UUID) resultSet.getObject("id"));
                user.setId_number(resultSet.getString("id_number"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setToken(resultSet.getString("token"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private List<Map<String, String>> getCurrencyAccount(UUID user_id) {
        List<Map<String, String>> list = new ArrayList();

        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format(
                     "select * from currency_account \n" +
                             "inner join account on account.card_id= currency_account.card_id\n" +
                             "inner join currency on currency_account.currency_id = currency.id where account.user_id = '%s'", user_id))) {
            if (!resultSet.isBeforeFirst()) {
                throw new IllegalArgumentException("Account not found");
            }
            while (resultSet.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("amount", resultSet.getString("amount"));
                map.put("currency", resultSet.getString("name"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}