package com.gwt.wizard.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RouteInfo implements IsSerializable
{

    public static final Long PUBLIC = -1L;

    public enum PickupType
    {

        HOTEL("Hotel", "Pickup Time:"),
        AIRPORT("Flight No:", "Landing Time:"),
        TRAINSTATION("Train station", "Train arrival time:");

        String locationType;
        String timeType;

        private PickupType(String locationType, String timeType)
        {
            this.locationType = locationType;
            this.timeType = timeType;
        }

        public String getLocationType()
        {
            return locationType;
        }

        public String getTimeType()
        {
            return timeType;
        }
    }

    public enum SaveMode
    {
        UPDATE,
        ADD,
        ADD_WITH_RETURN
    };

    private Long id;

    private String start;
    private String end;
    private String description;
    private Long contractorId;

    public String getDescription()
    {
        return description;
    }

    public Long getContractorId()
    {
        return contractorId;
    }

    public void setContractorId(Long contactorId)
    {
        this.contractorId = contactorId;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    private PickupType pickupType;
    private Long cents;
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

    public Long getCents()
    {
        return cents;
    }

    public void setCents(Long cents)
    {
        this.cents = cents;
    }

    public String getKey()
    {
        return getStart() + " to " + getEnd();
    }

}
