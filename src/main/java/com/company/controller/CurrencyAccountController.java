package com.company.controller;

import com.company.service.CurrencyAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
public class CurrencyAccountController {

    @Autowired
    CurrencyAccountService currencyAccountService;

    @RequestMapping(method = RequestMethod.POST, value = "/card/amount")
    @ResponseBody
    public Map<String, String> addCardAmount(@RequestParam("token") String token,
                                             @RequestParam("card_number") String card_number,
                                             @RequestParam("currency") String currency,
                                             @RequestParam("amount") Double amount) {
        try {
            return currencyAccountService.addAmount(token, card_number, currency, amount);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameter", e);
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", e);
        }
    }

}