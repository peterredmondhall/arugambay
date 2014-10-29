package com.gwt.wizard.server;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.server.entity.User;
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
    private final StripePayment stripePayment = new StripePayment();

    @Override
    public BookingInfo addBooking(BookingInfo bookingInfo) throws IllegalArgumentException
    {
        return bookingServiceManager.saveWithClient(bookingInfo, getClient());
    }

    @Override
    public Boolean deleteRoute(RouteInfo placeInfo) throws IllegalArgumentException
    {
        return routeServiceManager.deleteRoute(placeInfo);
    }

    @Override
    public Boolean editRoute(RouteInfo placeInfo) throws IllegalArgumentException
    {
        return routeServiceManager.editRoute(placeInfo);
    }

    @Override
    public List<RouteInfo> getRoutes() throws IllegalArgumentException
    {
        return routeServiceManager.getRoutes();
    }

    @Override
    public List<BookingInfo> getBookings() throws IllegalArgumentException
    {
        return bookingServiceManager.getBookings();
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
    public List<BookingInfo> getBookingsForTour(Long id) throws IllegalArgumentException
    {
        return bookingServiceManager.getBookingsForTour(id);
    }

    @Override
    public List<BookingInfo> getBookingsForShare(Long ref) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        List<BookingInfo> list = bookingServiceManager.getBookingsForShare(ref);
        BookingInfo parentBookingInfo = bookingServiceManager.setShareAccepted(list.get(0));
        BookingInfo sharerBookingInfo = bookingServiceManager.setShareAccepted(list.get(1));
        if (sharerBookingInfo.getStatus() == OrderStatus.SHARE_ACCEPTED)
        {
            Mailer.sendShareAccepted(sharerBookingInfo.getEmail(), parentBookingInfo);
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
        if (stripePayment.charge(token, bookingInfo, profil))
        {
            bookingInfo = bookingServiceManager.setPayed(profil, bookingInfo, OrderStatus.PAID);
        }
        return bookingInfo;
    }

    @Override
    public void sendStat(StatInfo statInfo)
    {
        bookingServiceManager.sendStat(statInfo);
    }

}
