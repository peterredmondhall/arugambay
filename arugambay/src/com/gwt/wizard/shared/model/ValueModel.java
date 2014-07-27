package com.gwt.wizard.shared.model;

import java.io.Serializable;
import java.util.Date;

public class ValueModel implements Serializable
{

    public Date date;
    public String time;
    public String place;

    public ValueModel()
    {
    }

    public ValueModel(Date date, String time, String place)
    {
        super();
        this.date = date;
        this.time = time;
        this.place = place;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

}