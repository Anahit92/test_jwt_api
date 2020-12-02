package com.company.model;

import java.util.UUID;

public class Card {

    private UUID id;
    private String number;
    private String type;

	public Card(){
	}
	
    public Card(UUID id, String number, String type) {
        this.id = id;
        this.number = number;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
