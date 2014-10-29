package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.gwt.wizard.server.entity.Route;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RouteServiceManager
{
    private static final Logger logger = Logger.getLogger(RouteServiceManager.class.getName());
    public final String WIZARD_DATA_ENTITY = "wizard-data";
    public final String PLACES_DATA_ENTITY = "places-data";
    static final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public Boolean deleteRoute(RouteInfo routeInfo) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        try
        {
            Route place = em.find(Route.class, routeInfo.getId());
            em.remove(place);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
        return false;
    }

    public Boolean editRoute(RouteInfo routeInfo) throws IllegalArgumentException
    {
        try
        {
            EntityManager em = getEntityManager();
            try
            {
                Route route = em.find(Route.class, routeInfo.getId());
                route.setStart(routeInfo.getStart());
                route.setEnd(routeInfo.getEnd());
                route.setPrice(routeInfo.getPrice());
                route.setPickupType(routeInfo.getPickupType());
                route.setImage(routeInfo.getImage());
                em.getTransaction().begin();
                em.persist(route);
                em.getTransaction().commit();
            }
            finally
            {
                em.close();
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public List<RouteInfo> getRoutes() throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        List<RouteInfo> routes = new ArrayList<>();
        try
        {
            List<Route> resultList = em.createQuery("select t from Route t ").getResultList();
            if (resultList.isEmpty())
            {
                RouteInfo routeInfo = new RouteInfo();
                routeInfo.setStart("Arugam Bay");
                routeInfo.setEnd("Colombo Airport");
                routeInfo.setPickupType(PickupType.AIRPORT);
                Route route = Route.getRoute(routeInfo);
                em.getTransaction().begin();
                em.persist(route);
                em.getTransaction().commit();
                resultList = em.createQuery("select t from Route t ").getResultList();
            }
            for (Route route : resultList)
            {
                em.detach(route);
                RouteInfo routeInfo = route.getInfo();
                routes.add(routeInfo);
            }
        }
        catch (Exception ex)
        {
            logger.severe("getting routes");
        }
        finally
        {
            em.close();
        }
        return routes;
    }

}
