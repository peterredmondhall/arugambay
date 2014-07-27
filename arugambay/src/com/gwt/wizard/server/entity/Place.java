package com.gwt.wizard.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.PlaceInfo;

@Entity
public class Place implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String city;
    private String place;
    private String pickup;

    public Key getKey()
    {
        return key;
    }

    public void setKey(Key key)
    {
        this.key = key;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getPickup()
    {
        return pickup;
    }

    public void setPickup(String pickup)
    {
        this.pickup = pickup;
    }

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public static Place getPlace(PlaceInfo placeInfo)
    {
        Place place = new Place();
        place.setCity(placeInfo.getCity());
        place.setPlace(placeInfo.getPlace());
        place.setPickup(placeInfo.getPickup());
        return place;
    }
}