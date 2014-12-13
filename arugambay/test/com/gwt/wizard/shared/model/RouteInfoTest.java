package com.gwt.wizard.shared.model;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.gwt.wizard.shared.model.RouteInfo.PickupType;

public class RouteInfoTest
{

    @Test
    public void test() throws IOException
    {
        PickupType pickupType = RouteInfo.PickupType.HOTEL;
        System.out.println(pickupType.getLocationType());
        Assert.assertTrue(pickupType.getTimeType().equals("Pickup Time"));
        Assert.assertTrue(pickupType.getLocationType().equals("Hotel"));
    }
}
