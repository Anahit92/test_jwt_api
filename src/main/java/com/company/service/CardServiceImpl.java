package com.company.service;

import com.company.DataAccess;
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

    private Connection con = new DataAccess().getConnection();

    @Override
	public Map<String, Object> getCards(String token) {
        HashMap<String, Object> map = new HashMap<>();
        UUID id = getUserByToken(token).getId();
        //Map<String, Object> currency = getCurrencyAccount(id);
        List<Object> currency = getCurrencyAccount(id);
		try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format(
                    "select * from card where id in (SELECT card_id from account where user_id = '%s')", id))) {
                while (resultSet.next()) {
                    map.put("card_number", resultSet.getString("number"));
                    map.put("card_type", resultSet.getString("type"));
                    map.put("currency_accounts", currency.toString());
                    try {
                        map.put("total_amount", currencyCalculator(currency));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return map;
	}

    private Object currencyCalculator(List<Object> currency) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.currencyfreaks.com/latest?apikey=8f6753fc1dc1445eb1fc604637dd0e62&symbols=AMD,USD,EUR,GEL"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject ob = new JSONObject(response.body());
        double total = 0.0;
        for (Object obj: currency ) {
            Double rate = 0.0;
            switch ((String) ((JSONObject) obj).get("currency")) {
                case "AMD": rate = Double.valueOf(ob.getJSONObject("rates").getString("AMD")); break;
                case "USD":  rate = Double.valueOf(ob.getJSONObject("rates").getString("USD")); break;
                case "EUR":  rate = Double.valueOf(ob.getJSONObject("rates").getString("EUR")); break;
                case "GEL":  rate = Double.valueOf(ob.getJSONObject("rates").getString("GEL")); break;
            }
            total = total + ( Double.valueOf ((String) ((JSONObject) obj).get("amount") ) * 1.0 / rate );
        }
        return total;
    }

    private User getUserByToken(String token) {
        User user = new User();
        String query = String.format("SELECT * FROM public.user where token = '%s'", token);
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                user.setId((UUID) resultSet.getObject("id"));
                user.setId_number( resultSet.getString("id_number"));
                user.setPassword( resultSet.getString("password"));
                user.setPhone( resultSet.getString("phone"));
                user.setToken( resultSet.getString("token"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private List<Object> getCurrencyAccount(UUID user_id) {
        List<Object> list = new ArrayList();
        //HashMap<String, JSONObject> map = new HashMap<>();
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format(
                     "select * from currency_account \n" +
                             "inner join account on account.card_id= currency_account.card_id\n" +
                             "inner join currency on currency_account.currency_id = currency.id where account.user_id = '%s'", user_id))) {
            while (resultSet.next()) {
                JSONObject ob = new JSONObject();
                ob.put("amount", resultSet.getString("amount"));
                ob.put("currency", resultSet.getString("name"));
                list.add(ob);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}