package com.gwt.wizard.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.StatInfo;

@Entity
public class Stat extends ArugamEntity<StatInfo>
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    String type;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    String country;

    public static Stat getStat(StatInfo statInfo)
    {
        Stat stat = new Stat();
        stat.setCountry(statInfo.getCountry());
        stat.setType(statInfo.getType());

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

    @Override
    public void setKey(Key key)
    {
        this.key = key;

    }

    @Override
    public StatInfo getInfo()
    {
        // TODO
        return null;
    }

}