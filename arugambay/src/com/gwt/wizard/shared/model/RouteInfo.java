package com.gwt.wizard.shared.model;

import java.io.Serializable;

public class RouteInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    public enum PickupType
    {
        HOTEL,
        AIRPORT
    }

    private Long id;

    private String start;
    private String end;
    private PickupType pickupType;
    private float price;
    private Long image;

    public Long getImage()
    {
        return image;
    }

    public void setImage(Long image)
    {
        this.image = image;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getStart()
    {
        return start;
    }

    public void setStart(String start)
    {
        this.start = start;
    }

    public String getEnd()
    {
        return end;
    }

    public void setEnd(String end)
    {
        this.end = end;
    }

    public PickupType getPickupType()
    {
        return pickupType;
    }

    public void setPickupType(PickupType pickupType)
    {
        this.pickupType = pickupType;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }

}
