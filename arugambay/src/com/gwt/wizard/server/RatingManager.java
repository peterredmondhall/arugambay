package com.gwt.wizard.server;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Rating;
import com.gwt.wizard.server.entity.Route;
import com.gwt.wizard.shared.model.RatingInfo;
import com.gwt.wizard.shared.model.RouteInfo;

public class RatingManager extends Manager
{
    private static final Logger logger = Logger.getLogger(RatingManager.class.getName());

    public void add(RatingInfo ratingInfo)
    {
        EntityManager em = getEntityManager();
        try
        {
            Booking booking = em.find(Booking.class, ratingInfo.getBookingId());
            Route route = em.find(Route.class, booking.getRoute());
            ratingInfo.setContractorId(route.getContractorId());
            em.getTransaction().begin();
            Rating rating = Rating.getRating(ratingInfo);
            em.persist(rating);
            em.getTransaction().commit();

        }
        catch (Exception ex)
        {
            logger.severe("persisiting ratingInfo:" + ratingInfo);
            ex.printStackTrace();
        }

    }

    public List<RatingInfo> getRatings(RouteInfo routeInfo)
    {

        EntityManager em = getEntityManager();
        List<RatingInfo> ratings = newArrayList();

        try
        {
            List<Rating> resultList = em.createQuery("select t from Rating t where contractorId=" + routeInfo.getContractorId()).getResultList();
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
