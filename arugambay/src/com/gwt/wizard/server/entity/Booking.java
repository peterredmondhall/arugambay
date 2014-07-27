package com.gwt.wizard.server.entity;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.model.BookingInfo;

@Entity
public class Booking implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String date;
    private boolean withReturn;
    private String forwardPickupPlace;
    private String reference;
    private String forwardPickupTime;
    private String returnPickupPlace;
    private String returnPickupTime;

    private String name;
    private String email;
    private int pax = 0;
    private int surfboards = 0;

    private String requirements = "";

    public boolean isWithReturn()
    {
        return withReturn;
    }

    public void setWithReturn(boolean withReturn)
    {
        this.withReturn = withReturn;
    }

    public String getForwardPickupPlace()
    {
        return forwardPickupPlace;
    }

    public void setForwardPickupPlace(String forwardPickupPlace)
    {
        this.forwardPickupPlace = forwardPickupPlace;
    }

    public String getForwardPickupTime()
    {
        return forwardPickupTime;
    }

    public void setForwardPickupTime(String forwardPickupTime)
    {
        this.forwardPickupTime = forwardPickupTime;
    }

    public String getReturnPickupPlace()
    {
        return returnPickupPlace;
    }

    public void setReturnPickupPlace(String returnPickupPlace)
    {
        this.returnPickupPlace = returnPickupPlace;
    }

    public String getReturnPickupTime()
    {
        return returnPickupTime;
    }

    public void setReturnPickupTime(String returnPickupTime)
    {
        this.returnPickupTime = returnPickupTime;
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

    public int getPax()
    {
        return pax;
    }

    public void setPax(int pax)
    {
        this.pax = pax;
    }

    public String getRequirements()
    {
        return requirements;
    }

    public void setRequirements(String requirements)
    {
        this.requirements = requirements;
    }

    public String getReference()
    {
        return reference;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public int getSurfboards()
    {
        return surfboards;
    }

    public void setSurfboards(int surfboards)
    {
        this.surfboards = surfboards;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

    public static Booking getBooking(BookingInfo bookingInfo, Map<String, Place> listPlaces)
    {
        Booking booking = new Booking();
        booking.setDate(bookingInfo.getDate());
        booking.setEmail(bookingInfo.getEmail());

        return booking;
    }

}