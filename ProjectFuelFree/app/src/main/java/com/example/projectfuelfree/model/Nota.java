package com.example.projectfuelfree.model;

public class Nota {
    public String token;
    public String dateOfTransaction;
    public String productBought;
    public String nominalBought;
    public String literGet;

    public Nota(){

    }

    public Nota(String token, String dateOfTransaction, String productBought, String nominalBought, String literGet) {
        this.token = token;
        this.dateOfTransaction = dateOfTransaction;
        this.productBought = productBought;
        this.nominalBought = nominalBought;
        this.literGet = literGet;
    }

    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getProductBought() {
        return productBought;
    }

    public void setProductBought(String productBought) {
        this.productBought = productBought;
    }

    public String getNominalBought() {
        return nominalBought;
    }

    public void setNominalBought(String nominalBought) {
        this.nominalBought = nominalBought;
    }
}
