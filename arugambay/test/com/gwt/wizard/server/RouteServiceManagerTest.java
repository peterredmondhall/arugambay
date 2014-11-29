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
import com.gwt.wizard.shared.model.RouteInfo.SaveMode;
import com.gwt.wizard.shared.model.AgentInfo;

public class RouteServiceManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    RouteServiceManager rs = new RouteServiceManager();

    AgentInfo userInfo;

    @Before
    public void setUp()
    {
        helper.setUp();
        userInfo = new UserManager().createAgent("test@example.com");
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setContractorId(userInfo.getId());
        new RouteServiceManager().saveRoute(userInfo, routeInfo, SaveMode.ADD);
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
        routes = rs.deleteRoute(userInfo, routeInfo);
        assertEquals(1, routes.size());
    }

    @Test
    public void should_update_route()
    {
        List<RouteInfo> routes = rs.getRoutes(userInfo);
        assertEquals(1, routes.size());
        RouteInfo routeInfo = routes.get(0);
        routeInfo.setPickupType(PickupType.HOTEL);
        routeInfo.setStart("start");
        routeInfo.setEnd("end");
        routeInfo.setPrice(160.00f);
        assertEquals(1, rs.saveRoute(userInfo, routeInfo, RouteInfo.SaveMode.UPDATE).size());
        routeInfo = routes.get(0);
        assertEquals("start", routeInfo.getStart());
        assertEquals("end", routeInfo.getEnd());
        assertEquals(PickupType.HOTEL, routeInfo.getPickupType());
        assertEquals(Float.toString(160.00f), Float.toString(routeInfo.getPrice()));

    }

    @Test
    public void should_add_route()
    {

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setPickupType(PickupType.HOTEL);
        routeInfo.setStart("start");
        routeInfo.setEnd("end");
        routeInfo.setPrice(160.00f);
        List<RouteInfo> routes = rs.saveRoute(userInfo, routeInfo, RouteInfo.SaveMode.ADD);
        routes = rs.saveRoute(userInfo, routeInfo, RouteInfo.SaveMode.ADD);
        assertEquals(2, routes.size());
        routeInfo = routes.get(0);
        assertEquals("start", routeInfo.getStart());
        assertEquals("end", routeInfo.getEnd());
        assertEquals(PickupType.HOTEL, routeInfo.getPickupType());
        assertEquals(Float.toString(160.00f), Float.toString(routeInfo.getPrice()));
    }

}
