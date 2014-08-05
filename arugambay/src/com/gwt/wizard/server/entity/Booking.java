package com.gwt.wizard.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.model.BookingInfo;

@Entity
public class Booking implements Serializable, Comparable<Booking>
{

    public Booking()
    {
        status = OrderStatus.BOOKED;
        ref = "TODO";
        instanziated = new Date();
    }

    public String getRef()
    {
        return ref;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String date;
    private String name;
    private String email;
    private String flightNo;
    private String landingTime;
    private int pax;
    private int surfboards;
    private OrderStatus status;
    private String requirements;
    private String tx;
    private final String ref;
    private String client;
    private Date instanziated;

    public Date getInstanziated()
    {
        return instanziated;
    }

    public void setInstanziated(Date instanziated)
    {
        this.instanziated = instanziated;
    }

    public void setStatus(OrderStatus status)
    {
        this.status = status;
    }

    public String getTx()
    {
        return tx;
    }

    public String getClient()
    {
        return client;
    }

    public void setClient(String client)
    {
        this.client = client;
    }

    public void setTx(String tx)
    {
        this.tx = tx;
    }

    public OrderStatus getStatus()
    {
        return status;
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

    public static Booking getBooking(BookingInfo bookingInfo, String client)
    {
        Booking booking = new Booking();
        booking.setDate(bookingInfo.getDate());
        booking.setEmail(bookingInfo.getEmail());
        booking.setName(bookingInfo.getName());
        booking.setFlightNo(bookingInfo.getFlightNo());
        booking.setLandingTime(bookingInfo.getLandingTime());
        booking.setPax(bookingInfo.getPax());
        booking.setSurfboards(bookingInfo.getSurfboards());
        booking.setRequirements(bookingInfo.getRequirements());
        booking.setClient(client);

        return booking;
    }

    public BookingInfo getBookingInfo()
    {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setDate(getDate());
        bookingInfo.setEmail(getEmail());
        bookingInfo.setName(getName());
        bookingInfo.setFlightNo(getFlightNo());
        bookingInfo.setLandingTime(getLandingTime());
        bookingInfo.setPax(getPax());
        bookingInfo.setSurfboards(getSurfboards());
        bookingInfo.setRequirements(getRequirements());
        bookingInfo.setRef(getRef());
        bookingInfo.setStatus(getStatus());
        return bookingInfo;
    }

    public String getFlightNo()
    {
        return flightNo;
    }

    public void setFlightNo(String flightNo)
    {
        this.flightNo = flightNo;
    }

    public String getLandingTime()
    {
        return landingTime;
    }

    public void setLandingTime(String landingTime)
    {
        this.landingTime = landingTime;
    }

    @Override
    public int compareTo(Booking other)
    {
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        return this.instanziated.after(instanziated) ? -1 : 1;
    }

}