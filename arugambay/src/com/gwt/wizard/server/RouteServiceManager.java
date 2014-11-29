package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.entity.Route;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.RouteInfo;

public class RouteServiceManager
{
    private static final Logger logger = Logger.getLogger(RouteServiceManager.class.getName());

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public List<RouteInfo> deleteRoute(AgentInfo userInfo, RouteInfo routeInfo) throws IllegalArgumentException
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
            routes = getRoutes(userInfo);
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

    public List<RouteInfo> saveRoute(AgentInfo userInfo, RouteInfo routeInfo, RouteInfo.SaveMode mode) throws IllegalArgumentException
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
                    route.setContractorId(routeInfo.getContractorId());
                    persist(em, route, routeInfo);
                    break;
                case ADD_WITH_RETURN:
                    route = new Route();
                    route.setContractorId(routeInfo.getContractorId());
                    persist(em, route, routeInfo);
                    route = new Route();
                    route.setContractorId(routeInfo.getContractorId());
                    String start = routeInfo.getEnd();
                    String end = routeInfo.getStart();
                    routeInfo.setStart(start);
                    routeInfo.setEnd(end);
                    persist(em, route, routeInfo);

                    break;

                case UPDATE:
                    route = em.find(Route.class, routeInfo.getId());
                    persist(em, route, routeInfo);
                    break;
            }
//            route.setStart(routeInfo.getStart());
//            route.setEnd(routeInfo.getEnd());
//            route.setPrice(routeInfo.getPrice());
//
//            route.setPickupType(routeInfo.getPickupType());
//            route.setImage(routeInfo.getImage());
//            route.setDescription(routeInfo.getDescription());
//            em.getTransaction().begin();
//            em.persist(route);
//            em.getTransaction().commit();
//            em.detach(route);
            route = em.find(Route.class, route.getKey().getId());
            routes = getRoutes(userInfo);
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

    private void persist(EntityManager em, Route route, RouteInfo routeInfo)
    {
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

    }

    @SuppressWarnings("unchecked")
    public List<RouteInfo> getRoutes(AgentInfo agentInfo) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        List<RouteInfo> routes = new ArrayList<>();
        try
        {
            // find a list of providers being managed by this user
            // List<Contractor> contractorList = em.createQuery("select t from Contractor t").getResultList();
            // System.out.println(contractorList.get(0).getUserId());
            List<Contractor> contractorList = em.createQuery("select t from Contractor t where agentId=" + agentInfo.getId()).getResultList();
            List<Long> contractorIdList = Lists.newArrayList();
            for (Contractor contractor : contractorList)
            {
                contractorIdList.add(contractor.getInfo().getId());
            }

            List<Route> resultList = em.createQuery("select t from Route t ").getResultList();
            for (Route route : resultList)
            {
                RouteInfo routeInfo = route.getInfo();
                if (agentInfo == null || contractorIdList.contains(routeInfo.getContractorId()))
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

    public List<RouteInfo> getRoutes() throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();

        List<RouteInfo> routes = new ArrayList<>();
        try
        {

            List<Route> resultList = em.createQuery("select t from Route t ").getResultList();
            for (Route route : resultList)
            {
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
