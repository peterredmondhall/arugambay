package com.gwt.wizard.server;

import static org.joda.time.DateTime.now;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Config;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.server.entity.Route;
import com.gwt.wizard.server.entity.Stat;
import com.gwt.wizard.server.jpa.EMF;
import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.StatInfo;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BookingServiceManager extends Manager
{
    private static final Logger logger = Logger.getLogger(BookingServiceManager.class.getName());
    public final String WIZARD_DATA_ENTITY = "wizard-data";
    public final String PLACES_DATA_ENTITY = "places-data";
    static final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");

    public BookingInfo addBookingWithClient(BookingInfo bookingInfo, String client) throws IllegalArgumentException
    {
        BookingInfo result = null;
        logger.info(bookingInfo.toString());
        EntityManager em = getEntityManager();
        try
        {
            Booking booking = Booking.getBooking(bookingInfo, client);
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
            em.detach(booking);

            Route route = em.find(Route.class, bookingInfo.getRouteInfo().getId());
            result = booking.getBookingInfo(route.getInfo());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
        return result;
    }

    private RouteInfo getRouteInfo(long id, EntityManager em)
    {
        Route route = em.find(Route.class, id);
        return route.getInfo();
    }

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    private static final long ALL_BOOKINGS = -1;

    public List<BookingInfo> getBookings()
    {
        return getBookings(ALL_BOOKINGS);
    }

    public List<BookingInfo> getBookings(Long agentId) throws IllegalArgumentException
    {
        EntityManager em = getEntityManager();
        List<BookingInfo> bookings = new ArrayList<>();
        try
        {
            @SuppressWarnings("unchecked")
            List<Booking> resultList = em.createQuery("select t from Booking t order by instanziated desc").getResultList();
            for (Booking booking : resultList)
            {
                em.detach(booking);
                RouteInfo routeInfo = getRouteInfo(booking.getRoute(), em);
                BookingInfo bookingInfo = booking.getBookingInfo(routeInfo);
                Contractor contractor = em.find(Contractor.class, routeInfo.getContractorId());
                if (agentId == ALL_BOOKINGS || contractor.getAgentId().equals(agentId))
                {
                    bookings.add(bookingInfo);
                }
            }
        }
        catch (Exception ex)
        {
            logger.severe(ex.getMessage());
        }
        finally
        {
            em.close();
        }
        return bookings;
    }

    public BookingInfo getBookingForTransactionWithClient(Profil profil, String client, OrderStatus hasPaid) throws IllegalArgumentException
    {

        EntityManager em = getEntityManager();
        BookingInfo bookingInfo = null;
        try
        {
            String query = "select t from Booking t where client='" + client + "' order by instanziated desc";
            @SuppressWarnings("unchecked")
            List<Booking> resultList = em.createQuery(query).getResultList();
            if (resultList.size() > 0)
            {
                Booking booking = resultList.get(0);
                booking.setStatus(hasPaid);
                em.getTransaction().begin();
                em.persist(booking);
                em.getTransaction().commit();

                em.detach(booking);
                bookingInfo = resultList.get(0).getBookingInfo(getRouteInfo(resultList.get(0).getRoute(), em));
            }
        }
        finally
        {
            em.close();
        }
        return bookingInfo;
    }

    public BookingInfo setPayed(Profil profil, BookingInfo bi, OrderStatus orderStatus) throws IllegalArgumentException
    {

        EntityManager em = getEntityManager();
        BookingInfo bookingInfo = null;
        try
        {
            Booking booking = em.find(Booking.class, bi.getId());

            booking.setStatus(orderStatus);
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();

            em.detach(booking);
            bookingInfo = booking.getBookingInfo(getRouteInfo(booking.getRoute(), em));
        }
        finally
        {
            em.close();
        }
        return bookingInfo;
    }

    public ProfilInfo getPaypalProfil() throws IllegalArgumentException
    {
        ProfilInfo profilInfo = getProfil().getInfo();
        logger.info(profilInfo.toString());
        return profilInfo;

    }

    public boolean getMaintenceAllowed()
    {
        EntityManager em = getEntityManager();
        boolean maintenanceAllowed = false;
        try
        {
            Config config = (Config) em.createQuery("select t from Config t").getSingleResult();
            if (config.getMaintenceAllowed() == null)
            {
                logger.info("maintence allowed not avail - setting false");
                em.getTransaction().begin();
                config.setMaintenceAllowed(false);
                em.persist(config);
                em.getTransaction().commit();
                em.detach(config);
            }
            else
            {
                maintenanceAllowed = config.getMaintenceAllowed();
            }
        }
        finally
        {
            em.close();
        }
        return maintenanceAllowed;
    }

    public Profil getProfil()
    {
        EntityManager em = getEntityManager();
        Profil profil = null;
        try
        {
            Config config = null;
            List<Config> configList = em.createQuery("select t from Config t").getResultList();
            if (configList.size() == 0)
            {
                em.getTransaction().begin();
                ;
                config = new Config();
                config.setProfil("test");
                config.setMaintenceAllowed(true);
                em.persist(config);
                em.getTransaction().commit();
            }
            else
            {
                config = configList.get(0);
            }
            logger.info("Using config profil:" + config.getProfil());

            List<Profil> profilList = em.createQuery("select t from Profil t where name ='" + config.getProfil() + "'").getResultList();

            if (profilList.size() == 0)
            {
                em.getTransaction().begin();
                profil = new Profil();
                profil.setPaypalAccount(PaypalPaymentChecker.TEST_ACCT);
                profil.setPaypalAT(PaypalPaymentChecker.TEST_AT);
                profil.setPaypalURL(PaypalPaymentChecker.TEST_PAYPAL_URL);
                profil.setTest(true);
                profil.setName("test");
                profil.setTaxisurfUrl("http://taxigangsurf.appspot.com");
                em.persist(profil);
                em.getTransaction().commit();

            }
            else
            {
                profil = profilList.get(0);
                logger.info("Using config profil:" + profil.getName());
            }
        }
        finally
        {
            em.close();
        }
        return profil;
    }

    public List<BookingInfo> getBookingsForRoute(final RouteInfo routeInfo) throws IllegalArgumentException
    {

        Predicate<BookingInfo> acceptEven = new Predicate<BookingInfo>()
        {
            @Override
            public boolean apply(BookingInfo bookingInfo)
            {
                return new DateTime(bookingInfo.getDate()).isAfter(now()) &&
                        OrderType.BOOKING == bookingInfo.getOrderType() &&
                        OrderStatus.PAID == bookingInfo.getStatus() &&
                        routeInfo.getId().equals(bookingInfo.getRouteInfo().getId()) &&
                        bookingInfo.getShareWanted();
            }
        };
        List<BookingInfo> bookings = getBookings();
        System.out.println("booking:" + bookings.get(0).getDate());
        System.out.println("now:" + now());
        List<BookingInfo> current = Lists.newArrayList(Collections2.filter(bookings, acceptEven));

        logger.info("share candidates size = " + current.size());
        return current;
    }

    public BookingInfo setShareAccepted(BookingInfo bookingInfo)
    {
        EntityManager em = getEntityManager();
        try
        {
            Booking booking = em.find(Booking.class, bookingInfo.getId());

            booking.setStatus(OrderStatus.SHARE_ACCEPTED);
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();

            em.detach(booking);
            bookingInfo = booking.getBookingInfo(getRouteInfo(booking.getRoute(), em));

        }
        finally
        {
            em.close();
        }
        return bookingInfo;
    }

    public void sendStat(StatInfo statInfo)
    {
        EntityManager em = getEntityManager();
        try
        {
            Stat stat = Stat.getStat(statInfo);
            em.persist(stat);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
    }

    public BookingInfo getBooking(Long id)
    {
        EntityManager em = getEntityManager();
        Booking booking = em.find(Booking.class, id);
        em.detach(booking);
        return booking.getBookingInfo(getRouteInfo(booking.getRoute(), em));
    }

    public List<BookingInfo> getListFeedbackRequest()
    {
        EntityManager em = getEntityManager();
        List<BookingInfo> bookings = new ArrayList<>();
        try
        {
            @SuppressWarnings("unchecked")
            List<Booking> resultList = em.createQuery("select t from Booking t").getResultList();
            for (Booking booking : resultList)
            {
                if (booking.getRated() != null && !booking.getRated())
                {
                    DateTime bookingDate = new DateTime(booking.getDate());
                    if (bookingDate.plusDays(1).isBefore(DateTime.now()))
                    {
                        em.getTransaction().begin();
                        booking.setRated(true);
                        em.persist(booking);
                        em.getTransaction().commit();
                        em.detach(booking);
                        RouteInfo routeInfo = getRouteInfo(booking.getRoute(), em);
                        BookingInfo bookingInfo = booking.getBookingInfo(routeInfo);
                        bookings.add(bookingInfo);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            logger.severe(ex.getMessage());
        }
        finally
        {
            em.close();
        }
        return bookings;

    }

}
