package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

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

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public List<RouteInfo> deleteRoute(RouteInfo routeInfo) throws IllegalArgumentException
    {
        List<RouteInfo> routes = null;
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            Route place = em.find(Route.class, routeInfo.getId());
            em.remove(place);
            em.flush();
            em.getTransaction().commit();
            em.close();
            em = getEntityManager();
            routes = getRoutesWithEM(em);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
        return routes;
    }

    public List<RouteInfo> saveRoute(RouteInfo routeInfo, RouteInfo.SaveMode mode) throws IllegalArgumentException
    {
        List<RouteInfo> routes = null;
        EntityManager em = getEntityManager();
        try
        {
            Route route = null;
            switch (mode)
            {
                case ADD:
                    route = new Route();
                    break;

                case UPDATE:
                    route = em.find(Route.class, routeInfo.getId());
                    break;
            }
            route.setStart(routeInfo.getStart());
            route.setEnd(routeInfo.getEnd());
            route.setPrice(routeInfo.getPrice());
            route.setPickupType(routeInfo.getPickupType());
            route.setImage(routeInfo.getImage());
            route.setDescription(routeInfo.getDescription());
            em.getTransaction().begin();
            em.persist(route);
            em.flush();
            em.getTransaction().commit();
            em.persist(route);
            em.clear();
            em.close();
            em = getEntityManager();
            routes = getRoutesWithEM(em);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }

        return routes;
    }

    @SuppressWarnings("unchecked")
    public List<RouteInfo> getRoutes() throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        return getRoutesWithEM(em);

    }

    private List<RouteInfo> getRoutesWithEM(EntityManager em) throws IllegalArgumentException
    {
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
                em.flush();
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
        return routes;
    }

}
