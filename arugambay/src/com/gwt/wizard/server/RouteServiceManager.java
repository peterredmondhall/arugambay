package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.entity.Route;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;
import com.gwt.wizard.shared.model.RouteInfo.SaveMode;

public class RouteServiceManager extends Manager
{
    private static final Logger logger = Logger.getLogger(RouteServiceManager.class.getName());

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
                case ADD_WITH_RETURN:
                    route = new Route();
                    persist(em, route, routeInfo);
                    if (mode.equals(SaveMode.ADD_WITH_RETURN))
                    {
                        route = new Route();
                        String start = routeInfo.getEnd();
                        String end = routeInfo.getStart();
                        routeInfo.setStart(start);
                        routeInfo.setEnd(end);
                        routeInfo.setPickupType(PickupType.HOTEL);
                        persist(em, route, routeInfo);
                    }
                    break;

                case UPDATE:
                    route = em.find(Route.class, routeInfo.getId());
                    persist(em, route, routeInfo);
                    break;
            }

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
        route.setCents(routeInfo.getCents());
        route.setAgentCents(routeInfo.getAgentCents());

        route.setPickupType(routeInfo.getPickupType());
        route.setImage(routeInfo.getImage());
        route.setDescription(routeInfo.getDescription());
        route.setContractorId(routeInfo.getContractorId());
        em.getTransaction().begin();
        em.persist(route);
        em.getTransaction().commit();
        em.detach(route);
        route = em.find(Route.class, route.getKey().getId());

    }

    @SuppressWarnings("unchecked")
    public List<RouteInfo> getRoutes(AgentInfo agentInfo) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        List<RouteInfo> routes = new ArrayList<>();
        try
        {
            logger.info("getting routes for agent email " + agentInfo.getEmail() + " id " + agentInfo.getId());

            // find a list of providers being managed by this user
            List<Contractor> contractorList = em.createQuery("select t from Contractor t where agentId=" + agentInfo.getId()).getResultList();

            List<Long> contractorIdList = Lists.newArrayList();
            for (Contractor contractor : contractorList)
            {
                contractorIdList.add(contractor.getInfo().getId());
                logger.info("contractorId:" + contractor.getInfo().getId());
            }

            List<Route> resultList = em.createQuery("select t from Route t ").getResultList();
            logger.info("get route count " + resultList.size());
            for (Route route : resultList)
            {
                RouteInfo routeInfo = route.getInfo();
                logger.info("routeInfo.getContractorId() " + routeInfo.getContractorId());

                if (agentInfo == null || contractorIdList.contains(routeInfo.getContractorId()))
                {
                    routes.add(routeInfo);
                }
            }
            logger.info("returning routeinfo count " + routes.size());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.severe("getting routes for " + agentInfo.getEmail() + "  :" + ex.getMessage());
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

            logger.info("get all routes returned no. of routes" + resultList.size());
            for (Route route : resultList)
            {
                RouteInfo routeInfo = route.getInfo();
                routes.add(routeInfo);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "getting all routes" + ex.getMessage(), ex);
        }
        return routes;

    }

    public RouteInfo getRoute(Long routeId)
    {
        RouteInfo routeInfo = null;
        try
        {
            List<Route> resultList = getEntityManager().createQuery("select t from Route t ").getResultList();

            // Route route = (Route) getEntityManager().createQuery("select t from Route t where id=routeId").getSingleResult();

            Route route = getEntityManager().find(Route.class, routeId);
            routeInfo = route.getInfo();

        }
        catch (Exception ex)
        {
            logger.log(Level.INFO, "getting route failed " + routeId + ex.getMessage(), ex);

        }
        return routeInfo;
    }

}
