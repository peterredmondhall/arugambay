package com.gwt.wizard.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.maps.gwt.client.LatLng;
import com.gwt.wizard.client.util.Utils;

//WORKS with GWT 2.6 or greater
public class RouteCalculationTest extends GWTTestCase
{

    @Override
    public String getModuleName()
    {
//        return "gwt_partyroute";
        return "com.gwt.wizard.Gwt_partyroute";
    }

    public void testSimple()
    {
        // Islamabad
        LatLng destination = LatLng.create(33.729388, 73.093146);

        // Adding Places
        List<LatLng> places = new ArrayList<>();
        // Lahore
        places.add(LatLng.create(31.554606, 74.357158));
        // Karachi
        places.add(LatLng.create(24.861462, 67.009939));
        // Sargodha
        places.add(LatLng.create(32.083741, 72.67186));

        List<LatLng> optimizedPlaces = new ArrayList<>();

        optimizedPlaces = Utils.getOpimimzedPlaces(optimizedPlaces, places, destination);

        System.out.println(optimizedPlaces.get(0).toString());
        System.out.println(optimizedPlaces.get(1).toString());
        System.out.println(optimizedPlaces.get(2).toString());

        assertTrue(true);
    }
}
