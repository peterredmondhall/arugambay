package com.gwt.wizard.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RouteInfo implements IsSerializable
{

    public static final Long PUBLIC = -1L;

    public enum PickupType
    {
        HOTEL,
        AIRPORT
    }

    public enum SaveMode
    {
        UPDATE,
        ADD
    };

    private Long id;

    private String start;
    private String end;
    private String description;
    private Long userId;

    public String getDescription()
    {
        return description;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

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

    public String getKey()
    {
        return getStart() + " to " + getEnd();
    }

}
