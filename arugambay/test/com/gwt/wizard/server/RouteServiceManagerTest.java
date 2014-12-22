package com.gwt.wizard.server;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;

public class RouteServiceManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    RouteServiceManager rs = new RouteServiceManager();

    AgentInfo agentInfo;

    @Before
    public void setUp()
    {
        helper.setUp();
        new BookingServiceManager().getProfil();
        agentInfo = new BookingServiceImpl().createDefaultUser();
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
        assertEquals(4, routes.size());
    }

    @Test
    public void should_delete_route()
    {
        List<RouteInfo> routes = rs.getRoutes();
        assertEquals(4, routes.size());
        RouteInfo routeInfo = routes.get(0);
        routes = rs.deleteRoute(agentInfo, routeInfo);
        assertEquals(3, routes.size());
    }

    @Test
    public void should_update_route()
    {
        List<RouteInfo> routes = rs.getRoutes(agentInfo);
        assertEquals(4, routes.size());
        RouteInfo routeInfo = routes.get(0);
        routeInfo.setPickupType(PickupType.HOTEL);
        routeInfo.setStart("start");
        routeInfo.setEnd("end");
        routeInfo.setCents(16000L);
        assertEquals(4, rs.saveRoute(agentInfo, routeInfo, RouteInfo.SaveMode.UPDATE).size());
        routeInfo = routes.get(0);
        assertEquals("start", routeInfo.getStart());
        assertEquals("end", routeInfo.getEnd());
        assertEquals(PickupType.HOTEL, routeInfo.getPickupType());
        assertEquals(Double.toString(16000f), Double.toString(routeInfo.getCents()));

    }

    @Test
    public void should_add_route()
    {
        List<RouteInfo> routes = rs.getRoutes(agentInfo);
        Long contractorId = routes.get(0).getContractorId();

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setPickupType(PickupType.HOTEL);
        routeInfo.setStart("start");
        routeInfo.setEnd("end");
        routeInfo.setCents(16000L);
        routeInfo.setContractorId(contractorId);
        routes = rs.saveRoute(agentInfo, routeInfo, RouteInfo.SaveMode.ADD);
        assertEquals(5, routes.size());
        routeInfo = routes.get(4);
        assertEquals("start", routeInfo.getStart());
        assertEquals("end", routeInfo.getEnd());
        assertEquals(PickupType.HOTEL, routeInfo.getPickupType());
        assertEquals(Double.toString(16000), Double.toString(routeInfo.getCents()));
    }
}
