package com.gwt.wizard.shared.model;

import java.io.Serializable;

public class ProfilInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String paypalUrl;
    private String paypalAccount;
    private String price;

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getPaypalUrl()
    {
        return paypalUrl;
    }

    public void setPaypalUrl(String paypalUrl)
    {
        this.paypalUrl = paypalUrl;
    }

    public String getPaypalAccount()
    {
        return paypalAccount;
    }

    public void setPaypalAccount(String paypalAccount)
    {
        this.paypalAccount = paypalAccount;
    }

    @Override
    public String toString()
    {
        return "[ProfilInfo paypalAccount=" + paypalAccount + "  paypalUrl=" + paypalUrl + "]";
    }
}
