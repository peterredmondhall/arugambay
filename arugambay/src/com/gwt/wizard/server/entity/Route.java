package com.gwt.wizard.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;

@Entity
public class Route extends ArugamEntity<RouteInfo>
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String start;
    private String end;
    private String description;
    private Long contractorId;

    public Long getContractorId()
    {
        return contractorId;
    }

    public void setContractorId(Long contractorId)
    {
        this.contractorId = contractorId;
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
    private Long cents = 10000L;

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

    public void setCents(Long cents)
    {
        this.cents = cents;
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

    @Override
    public void setKey(Key key)
    {
        this.key = key;
    }

    public static Route getRoute(RouteInfo routeInfo)
    {
        Route route = new Route();
        route.setStart(routeInfo.getStart());
        route.setEnd(routeInfo.getEnd());
        route.setDescription(routeInfo.getDescription());
        route.setCents(routeInfo.getCents());
        route.setPickupType(routeInfo.getPickupType());
        route.setImage(routeInfo.getImage());
        route.setContractorId(routeInfo.getContractorId());
        return route;
    }

    @Override
    public RouteInfo getInfo()
    {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setId(key.getId());
        routeInfo.setStart(start);
        routeInfo.setEnd(end);
        routeInfo.setDescription(description);
        routeInfo.setCents(cents);

        routeInfo.setPickupType(pickupType);
        routeInfo.setImage(image);
        if (contractorId == null)
        {
            routeInfo.setContractorId(4840028442198016L);
        }
        else
        {
            routeInfo.setContractorId(contractorId);
        }
        return routeInfo;
    }
}