package com.company.controller;

import com.company.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Controller
public class CardController {

    @Autowired
    CardService cardService;
	
    @RequestMapping(method = RequestMethod.POST, value="/data/cards")
    @ResponseBody
    public Map<String, Object> getCards(@RequestParam("token") String token) {
        try {
            return cardService.getCards(token);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid parameter", e);
        } catch (Throwable e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", e);
        }
    }

}
