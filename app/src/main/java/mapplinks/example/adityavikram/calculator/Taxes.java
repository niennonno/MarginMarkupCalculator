package com.mapplinks.calculator;

import java.io.Serializable;

/**
 * Created by Aditya Vikram on 8/20/2015.
 */
public class Taxes implements Serializable {
    public  String tax;
    public  Float rate;
    long id;
    public String getTax() { return tax; }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }



}
