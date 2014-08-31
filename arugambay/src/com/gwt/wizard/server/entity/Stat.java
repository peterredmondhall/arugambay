package com.gwt.wizard.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.StatInfo;

@Entity
public class Stat implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    String country;

    public static Stat getStat(StatInfo statInfo)
    {
        Stat stat = new Stat();
        stat.setCountry(statInfo.getCountry());
        return stat;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

}