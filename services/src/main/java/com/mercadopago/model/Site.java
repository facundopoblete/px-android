package com.mercadopago.model;

import java.io.Serializable;

public class Site implements Serializable {

    private String id;
    private String currencyId;

    public Site(String id, String currencyId) {
        this.id = id;
        this.currencyId = currencyId;
    }

    public String getId() {
        return id;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }
}