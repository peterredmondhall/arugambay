package com.gwt.wizard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Place;
import com.gwt.wizard.server.entity.User;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.server.util.Mailer;
import com.gwt.wizard.shared.OrderStatus;
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
    public Boolean save(BookingInfo bookingInfo) throws IllegalArgumentException
    {
        logger.info(bookingInfo.getDate());
        EntityManager em = getEntityManager();
        try
        {
            Booking booking = Booking.getBooking(bookingInfo, getClient());
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
        return true;
    }

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
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
        try
        {
            @SuppressWarnings("unchecked")
            List<Booking> resultList = em.createQuery("select t from Booking t").getResultList();
            for (Booking booking : resultList)
            {
                BookingInfo bookingInfo = booking.getBookingInfo();
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

    @Override
    public BookingInfo getBooking(String tx) throws IllegalArgumentException
    {
        OrderStatus hasPaid = new PaymentChecker(tx).hasClientPaid();

        EntityManager em = getEntityManager();
        BookingInfo bookingInfo = null;
        try
        {
            String query = "select t from Booking t where client='" + getClient() + "'";
            @SuppressWarnings("unchecked")
            List<Booking> resultList = em.createQuery(query).getResultList();
            if (resultList.size() == 1)
            {
                Booking booking = resultList.get(0);
                booking.setStatus(hasPaid);
                em.persist(booking);

                bookingInfo = resultList.get(0).getBookingInfo();
                Mailer.send(bookingInfo);
            }
        }
        finally
        {
            em.close();
        }
        return bookingInfo;
    }

    private String getClient()
    {
        return getThreadLocalRequest().getRemoteHost();
    }
}
