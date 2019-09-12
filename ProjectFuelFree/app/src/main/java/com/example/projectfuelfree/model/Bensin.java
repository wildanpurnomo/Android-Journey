package com.example.projectfuelfree.model;

public class Bensin {
    public String name;
    public String pricePerLiter;
    public String oktanNumber;

    public Bensin(){

    }

    public Bensin(String name, String pricePerLiter) {
        this.name = name;
        this.pricePerLiter = pricePerLiter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPricePerLiter() {
        return pricePerLiter;
    }

    public void setPricePerLiter(String pricePerLiter) {
        this.pricePerLiter = pricePerLiter;
    }

    public String getOktanNumber() {
        return oktanNumber;
    }

    public void setOktanNumber(String oktanNumber) {
        this.oktanNumber = oktanNumber;
    }
}
