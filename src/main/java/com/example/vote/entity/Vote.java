package com.example.vote.entity;

public class Vote {
    private int id;
    private String bet1;
    private String bet2;
    private String bet3;
    private int betType;

    public Vote() {
    }

    public Vote(int id, String bet1, String bet2, String bet3, int betType) {
        this.id = id;
        this.bet1 = bet1;
        this.bet2 = bet2;
        this.bet3 = bet3;
        this.betType = betType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBet1() {
        return bet1;
    }

    public void setBet1(String bet1) {
        this.bet1 = bet1;
    }

    public String getBet2() {
        return bet2;
    }

    public void setBet2(String bet2) {
        this.bet2 = bet2;
    }

    public String getBet3() {
        return bet3;
    }

    public void setBet3(String bet3) {
        this.bet3 = bet3;
    }

    public int getBetType() {
        return betType;
    }

    public void setBetType(int betType) {
        this.betType = betType;
    }
}
