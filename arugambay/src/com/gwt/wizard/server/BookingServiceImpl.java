package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Place;
import com.gwt.wizard.server.entity.User;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.server.util.BookingUtil;
import com.gwt.wizard.server.util.Mailer;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BookingServiceImpl extends RemoteServiceServlet implements
        BookingService
{
    private static final Logger logger = Logger.getLogger(BookingServiceImpl.class.getName());
    public final String WIZARD_DATA_ENTITY = "wizard-data";
    public final String PLACES_DATA_ENTITY = "places-data";

    @Override
    public Boolean saveWizardData(BookingInfo bookingInfo) throws IllegalArgumentException
    {
        logger.info(bookingInfo.getDate());
        EntityManager em = getEntityManager();
        try
        {
            Booking booking = Booking.getBooking(bookingInfo, getPlaces(em));
            em.persist(booking);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
        Mailer.send(bookingInfo);
        return true;
    }

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    private Map<String, Place> getPlaces(EntityManager em)
    {
        Map<String, Place> places = new HashMap<String, Place>();
        @SuppressWarnings("unchecked")
        List<Place> listPlaces = em.createQuery("select t from Place t").getResultList();
        for (Place place : listPlaces)
        {
            places.put(place.getPlace(), place);
        }
        return places;
    }

    @Override
    public List<String> getPlaceList()
    {
        EntityManager em = getEntityManager();
        List<String> placeList = Lists.newArrayList();
        try
        {
            @SuppressWarnings("unchecked")
            List<Place> resultList = em.createQuery("select t from Place t").getResultList();
            for (Place place : resultList)
            {
                placeList.add(place.getPlace());
            }
        }
        finally
        {
            em.close();
        }

        return placeList;
    }

    // @Override
    public void addPlace(PlaceInfo placeInfo)
    {
        EntityManager em = getEntityManager();
        try
        {
            Place place = Place.getPlace(placeInfo);
            em.persist(place);
        }
        finally
        {
            em.close();
        }

    }

    public Map<String, PlaceInfo> getPlacesForName() throws IllegalArgumentException
    {
        Map<String, PlaceInfo> map = new HashMap<String, PlaceInfo>();
        for (PlaceInfo placeInfo : getPlaces())
        {
            map.put(placeInfo.getPickup(), placeInfo);
        }
        return map;
    }

    public Map<Long, PlaceInfo> getPlacesForId() throws IllegalArgumentException
    {
        Map<Long, PlaceInfo> map = new HashMap<Long, PlaceInfo>();
        for (PlaceInfo placeInfo : getPlaces())
        {
            map.put(placeInfo.getId(), placeInfo);
        }
        return map;
    }

    @Override
    public List<PlaceInfo> getPlaces() throws IllegalArgumentException
    {
        List<PlaceInfo> places = new ArrayList<>();
        EntityManager em = getEntityManager();
        try
        {
            @SuppressWarnings("unchecked")
            List<Place> resultList = em.createQuery("select t from Place t").getResultList();
            for (Place place : resultList)
            {
                PlaceInfo placeInfo = new PlaceInfo();
                placeInfo.setId(place.getKey().getId());
                placeInfo.setCity(place.getCity());
                placeInfo.setPickup(place.getPickup());
                placeInfo.setPlace(place.getPlace());
                places.add(placeInfo);
            }
        }
        finally
        {
            em.close();
        }

        return places;
    }

    @Override
    public Boolean savePlace(PlaceInfo placeInfo) throws IllegalArgumentException
    {
        try
        {
            EntityManager em = getEntityManager();
            try
            {
                Place place = Place.getPlace(placeInfo);
                em.persist(place);
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

    @Override
    public Boolean deletePlace(Long id) throws IllegalArgumentException
    {
        try
        {
            EntityManager em = getEntityManager();
            try
            {
                Place place = em.find(Place.class, id);
                em.remove(place);
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

    @Override
    public Boolean editPlace(Long id, PlaceInfo placeInfo) throws IllegalArgumentException
    {
        try
        {
            EntityManager em = getEntityManager();
            try
            {
                Place place = em.find(Place.class, id);
                place.setCity(placeInfo.getCity());
                place.setPickup(placeInfo.getPickup());
                place.setPlace(placeInfo.getPlace());
                em.persist(place);
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

    @Override
    public List<BookingInfo> getBookings() throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        List<BookingInfo> bookings = new ArrayList<>();
        Map<Long, PlaceInfo> places = getPlacesForId();
        try
        {
            @SuppressWarnings("unchecked")
            List<Booking> resultList = em.createQuery("select t from Booking t").getResultList();
            for (Booking booking : resultList)
            {
                BookingInfo bookingInfo = BookingUtil.getBookingInfo(booking, places);
                bookings.add(bookingInfo);
            }
        }
        finally
        {
            em.close();
        }
        return bookings;
    }

    private User getUserFromSession()
    {
        Object obj = getThreadLocalRequest().getSession().getAttribute("user");
        if (obj == null)
        {
            return null;
        }
        return (User) obj;
    }

    @Override
    public Boolean getUser() throws IllegalArgumentException
    {
        User user = getUserFromSession();
        // load user devices
        if (user == null)
        {
            return false;
        }
        return true;
    }
}
