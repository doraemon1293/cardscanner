package com.example.cardscanner;

public class Cards {
    private String cardNumber;
    private String expDate;
    private String added;

    public Cards(String cardNumber,  String expDate, String added) {
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.added = added;
    }

    public String getCardNumber(){
        return cardNumber;
    }
    public String getExpDate(){
        return expDate;
    }
    public String getAdded(){
        return added;
    }

}

