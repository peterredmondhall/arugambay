package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.gwt.wizard.server.entity.Route;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.UserInfo;

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
            Route route = em.find(Route.class, routeInfo.getId());
            em.getTransaction().begin();
            em.remove(route);
            em.getTransaction().commit();
            em.flush();
            route = em.find(Route.class, routeInfo.getId());
            routes = getRoutes(routeInfo.getId());
        }
        catch (Exception e)
        {
            logger.severe("deleting route");
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
                    route.setUserId(routeInfo.getUserId());
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
            em.getTransaction().commit();
            em.detach(route);
            route = em.find(Route.class, route.getKey().getId());
            routes = getRoutes(routeInfo.getUserId());
        }

        catch (Exception e)
        {
            logger.severe("saving route");
        }
        finally
        {
            em.close();
        }
        return routes;
    }

    @SuppressWarnings("unchecked")
    public List<RouteInfo> getRoutes(Long userId) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        List<RouteInfo> routes = new ArrayList<>();
        try
        {
            List<Route> resultList = em.createQuery("select t from Route t ").getResultList();
            for (Route route : resultList)
            {
                RouteInfo routeInfo = route.getInfo();
                if (userId.equals(UserInfo.PUBLIC) || userId.equals(routeInfo.getUserId()))
                {
                    routes.add(routeInfo);
                }
            }
        }
        catch (Exception ex)
        {
            logger.severe("getting routes");
        }
        return routes;

    }
}
