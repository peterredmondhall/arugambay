package com.gwt.wizard.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;

@Entity
public class Route implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String start;
    private String end;
    private String description;
    private Long providerId;

    public Long getUserId()
    {
        return providerId;
    }

    public void setProviderId(Long providerId)
    {
        this.providerId = providerId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    private PickupType pickupType;
    private Float price;

    private Long image;

    public Key getKey()
    {
        return key;
    }

    public String getStart()
    {
        return start;
    }

    public void setStart(String start)
    {
        this.start = start;
    }

    public Long getImage()
    {
        return image;
    }

    public void setImage(Long image)
    {
        this.image = image;
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

//    public Blob getImage()
//    {
//        return image;
//    }
//
//    public void setImage(Blob image)
//    {
//        this.image = image;
//    }

    public void setKey(Key key)
    {
        this.key = key;
    }

    public static Route getRoute(RouteInfo routeInfo, Long providerId)
    {
        Route route = new Route();
        route.setStart(routeInfo.getStart());
        route.setEnd(routeInfo.getEnd());
        route.setDescription(routeInfo.getDescription());
        route.setPrice(routeInfo.getPrice());
        route.setPickupType(routeInfo.getPickupType());
        route.setImage(routeInfo.getImage());
        route.setProviderId(providerId);
        return route;
    }

    public RouteInfo getInfo()
    {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setId(key.getId());
        routeInfo.setStart(start);
        routeInfo.setEnd(end);
        routeInfo.setDescription(description);
        routeInfo.setPrice(price);
        routeInfo.setPickupType(pickupType);
        routeInfo.setImage(image);
        routeInfo.setContractorId(providerId);
        return routeInfo;
    }

}