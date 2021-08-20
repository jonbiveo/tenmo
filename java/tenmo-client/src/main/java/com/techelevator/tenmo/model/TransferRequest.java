package com.techelevator.tenmo.model;

public class TransferRequest {

    private Integer userFrom;
    private Integer userTo;
    private int amount;

    public TransferRequest() {
    }

    public TransferRequest(Integer userFrom, Integer userTo, int amount) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.amount = amount;
    }

    public Integer getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(Integer userFrom) {
        this.userFrom = userFrom;
    }

    public Integer getUserTo() {
        return userTo;
    }

    public void setUserTo(Integer userTo) {
        this.userTo = userTo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }



}
