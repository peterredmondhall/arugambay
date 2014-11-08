package com.gwt.wizard.shared.model;

import java.io.Serializable;
import java.util.Date;

import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.OrderType;

public class BookingInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Date date;

    private String name;
    private String email;
    private Long id;
    private RouteInfo routeInfo;

    private Long parentId;
    private Boolean shareWanted;
    private OrderStatus status;
    private OrderType orderType;
    private String flightNo;
    private String landingTime;
    private int pax = 0;
    private int surfboards = 0;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Boolean getShareWanted()
    {
        return shareWanted;
    }

    public void setShareWanted(Boolean shareWanted)
    {
        this.shareWanted = shareWanted;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public OrderType getOrderType()
    {
        return orderType;
    }

    public void setOrderType(OrderType orderType)
    {
        this.orderType = orderType;
    }

    public OrderStatus getStatus()
    {
        return status;
    }

    public void setStatus(OrderStatus status)
    {
        this.status = status;
    }

    public String getLandingTime()
    {
        return landingTime;
    }

    public void setLandingTime(String landingTime)
    {
        this.landingTime = landingTime;
    }

    public String getFlightNo()
    {
        return flightNo;
    }

    public void setFlightNo(String flightNo)
    {
        this.flightNo = flightNo;
    }

    public void setSurfboards(int surfboards)
    {
        this.surfboards = surfboards;
    }

    private String requirements = "";

    public String getRequirements()
    {
        return requirements;
    }

    public void setRequirements(String requirements)
    {
        this.requirements = requirements;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public int getSurfboards()
    {
        return surfboards;
    }

    public int getPax()
    {
        return pax;
    }

    public void setPax(int pax)
    {
        this.pax = pax;
    }

    public String getOrderNo()
    {
        int len = id.toString().length();
        return id.toString().substring(len - 5, len - 1);
    }

    public String getPaidAmt()
    {
        return "USD" + routeInfo.getPrice();
    }

    public RouteInfo getRouteInfo()
    {
        return routeInfo;
    }

    public void setRouteInfo(RouteInfo routeInfo)
    {
        this.routeInfo = routeInfo;
    }

}
