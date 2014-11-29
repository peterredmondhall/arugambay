package com.gwt.wizard.server;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.gwt.wizard.server.entity.Rating;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.model.RatingInfo;
import com.gwt.wizard.shared.model.RouteInfo;

public class RatingManager
{
    private static final Logger logger = Logger.getLogger(RatingManager.class.getName());

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public void add(RatingInfo ratingInfo)
    {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            Rating rating = Rating.getRating(ratingInfo);
            em.persist(rating);
            em.getTransaction().commit();

        }
        catch (Exception ex)
        {
            logger.severe("persisiting ratingInfo:" + ratingInfo);
        }

    }

    public List<RatingInfo> getRatings(RouteInfo routeInfo)
    {
        EntityManager em = getEntityManager();
        List<RatingInfo> ratings = newArrayList();
        try
        {
            List<Rating> resultList = em.createQuery("select t from Rating t where routeId='" + routeInfo.getId() + "'").getResultList();
            for (Rating rating : resultList)
            {
                ratings.add(rating.getInfo());
            }
        }
        catch (Exception ex)
        {
            logger.severe("getting ratings");
        }
        return ratings;
    }
}
