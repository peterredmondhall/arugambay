package com.gwt.wizard.server;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;

public class RouteServiceManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    RouteServiceManager rs = new RouteServiceManager();

    @Before
    public void setUp()
    {
        helper.setUp();
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    @Test
    public void should_fetch_routes()
    {

        List<RouteInfo> routes = rs.getRoutes();
        assertEquals(1, routes.size());
    }

    @Test
    public void should_delete_route()
    {
        List<RouteInfo> routes = rs.getRoutes();
        assertEquals(1, routes.size());
        RouteInfo routeInfo = routes.get(0);
        rs.deleteRoute(routeInfo);
    }

    @Test
    public void should_edit_route()
    {

        List<RouteInfo> routes = rs.getRoutes();
        assertEquals(1, routes.size());
        RouteInfo routeInfo = routes.get(0);
        routeInfo.setPickupType(PickupType.HOTEL);
        routeInfo.setStart("start");
        routeInfo.setEnd("end");
        routeInfo.setPrice(160.00f);
        assertEquals(true, rs.editRoute(routeInfo));
        routes = rs.getRoutes();
        assertEquals(1, routes.size());
        routeInfo = routes.get(0);
        assertEquals("start", routeInfo.getStart());
        assertEquals("end", routeInfo.getEnd());
        assertEquals(PickupType.HOTEL, routeInfo.getPickupType());
        assertEquals(Float.toString(160.00f), Float.toString(routeInfo.getPrice()));

    }

}
