package com.gwt.wizard.server;

import static com.gwt.wizard.shared.OrderStatus.SHARE_ACCEPTED;

import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.users.User;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.server.util.Mailer;
import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.StatInfo;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BookingServiceImpl extends RemoteServiceServlet implements
        BookingService
{
    private static final Logger logger = Logger.getLogger(BookingServiceImpl.class.getName());
//    public final String WIZARD_DATA_ENTITY = "wizard-data";
//    public final String PLACES_DATA_ENTITY = "places-data";

    private final BookingServiceManager bookingServiceManager = new BookingServiceManager();
    private final RouteServiceManager routeServiceManager = new RouteServiceManager();
    private final UserManager userManager = new UserManager();
    private final StripePayment stripePayment = new StripePayment();

    @Override
    public BookingInfo addBooking(BookingInfo bookingInfo) throws IllegalArgumentException
    {
        return bookingServiceManager.addBookingWithClient(bookingInfo, getClient());
    }

    @Override
    public List<RouteInfo> deleteRoute(RouteInfo placeInfo) throws IllegalArgumentException
    {
        return routeServiceManager.deleteRoute(placeInfo);
    }

    @Override
    public List<RouteInfo> saveRoute(RouteInfo placeInfo, RouteInfo.SaveMode mode) throws IllegalArgumentException
    {
        return routeServiceManager.saveRoute(placeInfo, mode);
    }

    @Override
    public List<RouteInfo> getRoutes(Long userId) throws IllegalArgumentException
    {
        return routeServiceManager.getRoutes(userId);
    }

    @Override
    public List<BookingInfo> getBookings() throws IllegalArgumentException
    {
        return bookingServiceManager.getBookings();
    }

    @Override
    public BookingInfo getBookingForTransaction(String tx) throws IllegalArgumentException
    {
        Profil profil = bookingServiceManager.getProfil();
        OrderStatus hasPaid = new PaypalPaymentChecker(tx, profil).hasClientPaid();

        BookingInfo bookingInfo = bookingServiceManager.getBookingForTransactionWithClient(profil, getClient(), hasPaid);
        if (bookingInfo != null)
        {
            Mailer.sendConfirmation(bookingInfo, profil);
        }
        return bookingInfo;

    }

    @Override
    public ProfilInfo getPaypalProfil() throws IllegalArgumentException
    {
        ProfilInfo profilInfo = bookingServiceManager.getProfil().getInfo();
        logger.info(profilInfo.toString());
        return profilInfo;

    }

    private String getClient()
    {
        return getThreadLocalRequest().getRemoteHost();
    }

    @Override
    public List<BookingInfo> getBookingsForRoute(RouteInfo routeInfo) throws IllegalArgumentException
    {
        return bookingServiceManager.getBookingsForRoute(routeInfo);
    }

    @Override
    public List<BookingInfo> handleShareAccepted(Long sharerId) throws IllegalArgumentException
    {
        BookingInfo sharer = bookingServiceManager.getBooking(sharerId);
        BookingInfo parentBookingInfo = bookingServiceManager.getBooking(sharer.getParentId());

        BookingInfo sharerBookingInfo = bookingServiceManager.setShareAccepted(sharer);
        List<BookingInfo> list = null;
        if (sharerBookingInfo.getStatus() == SHARE_ACCEPTED)
        {
            list = Lists.newArrayList(parentBookingInfo, sharerBookingInfo);
            Mailer.sendShareAccepted(sharerBookingInfo.getEmail(), parentBookingInfo);
        }
        else
        {
            logger.severe("share failed " + sharerId);
        }
        return list;
    }

    @Override
    public BookingInfo sendShareRequest(BookingInfo bookingInfo)
    {
        Profil profil = bookingServiceManager.getProfil();
        BookingInfo parentBooking = bookingServiceManager.getBooking(bookingInfo.getParentId());
        Mailer.sendShareRequest(parentBooking, bookingInfo, profil);
        return bookingInfo;
    }

    @Override
    public BookingInfo payWithStripe(String token, BookingInfo bookingInfo)
    {
        Profil profil = bookingServiceManager.getProfil();
        String refusal = stripePayment.charge(token, bookingInfo, profil.getStripeSecret());
        if (refusal == null)
        {
            bookingInfo = bookingServiceManager.setPayed(profil, bookingInfo, OrderStatus.PAID);
            if (bookingInfo != null)
            {
                Mailer.sendConfirmation(bookingInfo, profil);
            }
        }
        else
        {
            bookingInfo.setStripeRefusalReason(refusal);
        }
        return bookingInfo;
    }

    @Override
    public void sendStat(StatInfo statInfo)
    {
        bookingServiceManager.sendStat(statInfo);
    }

    @Override
    public Long getUser() throws IllegalArgumentException
    {
        Long userInfoId = null;
        User user = getUserFromSession();
        if (user != null)
        {
            userInfoId = userManager.getUser(user).getId();
        }
        return userInfoId;
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

}
