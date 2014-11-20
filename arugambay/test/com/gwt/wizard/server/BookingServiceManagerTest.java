package com.gwt.wizard.server;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.SaveMode;
import com.gwt.wizard.shared.model.UserInfo;

public class BookingServiceManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    BookingServiceManager bs = new BookingServiceManager();

    UserInfo userInfo;

    @Before
    public void setUp()
    {
        helper.setUp();
        userInfo = new UserManager().createUser("test@example.com");
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setUserId(userInfo.getId());
        new RouteServiceManager().saveRoute(routeInfo, SaveMode.ADD);
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    private BookingInfo getBookingInfo(Date date, String flightNo, String landingTime, int pax, int surfboards, String email, String reqs, OrderType orderType, boolean shareWanted)
    {
        RouteInfo routeInfo = new RouteServiceManager().getRoutes(userInfo.getId()).get(0);

        BookingInfo bi = new BookingInfo();
        bi.setDate(date);
        bi.setFlightNo(flightNo);
        bi.setLandingTime(landingTime);
        bi.setPax(pax);
        bi.setSurfboards(surfboards);
        bi.setEmail(email);
        bi.setRequirements(reqs);
        bi.setOrderType(orderType);
        bi.setShareWanted(shareWanted);

        bi.setRouteInfo(routeInfo);
        return bi;

    }

    private void validateBookingInfo(BookingInfo bi,
            String ref,
            String date,
            String flightNo,
            String landingTime,
            int pax,
            int surfboards,
            String email,
            String reqs,
            OrderStatus status,
            OrderType type,
            boolean shareWanted)
    {
        assertEquals(date, new SimpleDateFormat("dd.MM.yyyy").format(bi.getDate()));
        assertEquals(flightNo, bi.getFlightNo());
        assertEquals(landingTime, bi.getLandingTime());
        assertEquals(pax, bi.getPax());
        assertEquals(surfboards, bi.getSurfboards());
        assertEquals(email, bi.getEmail());
        assertEquals(reqs, bi.getRequirements());
        assertEquals(status, bi.getStatus());
        assertEquals(type, bi.getOrderType());
        assertEquals(shareWanted, bi.getShareWanted());

    }

    private BookingInfo getStandardBookingInfo()
    {
        return getBookingInfo(getDateInOneMonth(), "flightNo", "landingTime", 10, 11, "email", "reqs", OrderType.BOOKING, true);
    }

    private void create_a_booking()
    {
        bs.addBookingWithClient(getStandardBookingInfo(), "client");

    }

    private void confirm_payment()
    {
        // confirm_payment

        BookingInfo bi = bs.getBookingForTransactionWithClient(bs.getProfil(), "client", OrderStatus.FAILED);
        List<BookingInfo> bookings = bs.getBookings();
        assertEquals(OrderStatus.FAILED, bookings.get(0).getStatus());

        bi = bs.getBookingForTransactionWithClient(bs.getProfil(), "client", OrderStatus.PAID);
        bookings = bs.getBookings();
        assertEquals(OrderStatus.PAID, bookings.get(0).getStatus());

    }

    private Date getDateInOneMonth()
    {
        return new DateTime().plusMonths(1).toDate();
    }

    private void create_a_shared_booking()
    {
        List<BookingInfo> list = bs.getBookings();
        BookingInfo parentBookingInfo = list.get(0);
        BookingInfo bi = new BookingInfo();
        bi.setRouteInfo(new RouteServiceManager().getRoutes(UserInfo.PUBLIC).get(0));
        bi.setParentId(parentBookingInfo.getId());
        bi.setDate(parentBookingInfo.getDate());
        bi.setFlightNo(parentBookingInfo.getFlightNo());
        bi.setLandingTime(parentBookingInfo.getLandingTime());

        bi.setPax(22);
        bi.setSurfboards(23);
        bi.setEmail("sharerEmail");

        bi.setStatus(OrderStatus.BOOKED);
        bi.setOrderType(OrderType.SHARE);

        bs.addBookingWithClient(bi, "clientShare");

        boolean tested = false;
        for (BookingInfo bookingInfo : bs.getBookings())
        {
            if (bookingInfo.getOrderType().equals(OrderType.SHARE))
            {
//                Boo
//                list = new List<>bs.getBookingsForShare(bookingInfo.getId());
//                assertEquals(2, list.size());
//                assertEquals(parentBookingInfo.getId(), list.get(0).getId());
//                assertEquals(bookingInfo.getId(), list.get(1).getId());
//                tested = true;
//                break;
            }
        }
        assertEquals(true, tested);

    }

    @Test
    public void should_return_only_paidfor_future_bookings()
    {

        BookingInfo bi = getStandardBookingInfo();
        bi.setDate(new DateTime().plusYears(20).toDate());
        bs.addBookingWithClient(bi, "client");

        List<BookingInfo> list = bs.getBookingsForRoute(bi.getRouteInfo());
        // bookin is not paid for
        assertEquals(0, list.size());

        bi = getStandardBookingInfo();
        bi.setDate(new DateTime().plusYears(20).toDate());
        bi.setOrderType(OrderType.SHARE);
        bs.addBookingWithClient(bi, "client");
        list = bs.getBookingsForRoute(bi.getRouteInfo());
        // bookin is not BOOKING
        assertEquals(0, list.size());

        create_a_booking();
        bi = bs.getBookingForTransactionWithClient(bs.getProfil(), "client", OrderStatus.PAID);
        List<BookingInfo> bookings = bs.getBookings();
        assertEquals(OrderStatus.PAID, bookings.get(0).getStatus());
        list = bs.getBookingsForRoute(bi.getRouteInfo());
        // booking is in the past
        assertEquals(1, list.size());

    }

    @Test
    public void should_create_a_booking()
    {
        create_a_booking();

        List<BookingInfo> bookings = bs.getBookings();
        assertEquals(1, bookings.size());
        String expectedDate = new SimpleDateFormat("dd.MM.yyyy").format(getDateInOneMonth());
        validateBookingInfo(bookings.get(0), "ref", expectedDate, "flightNo", "landingTime", 10, 11, "email", "reqs", OrderStatus.BOOKED, OrderType.BOOKING, true);

        confirm_payment();
    }

    @Test
    public void should_create_a_shared_booking()
    {
        create_a_booking();
        confirm_payment();
        create_a_shared_booking();
    }

    @Test
    public void should_create_serializable_date()
    {
        create_a_booking();
        confirm_payment();
        List<BookingInfo> list = bs.getBookingsForRoute(bs.getBookings().get(0).getRouteInfo());
        assertEquals(1, list.size());
        Date date = list.get(0).getDate();
        assertEquals(true, date instanceof Serializable);
        System.out.println(date);

    }

    @Test
    public void should_change_booking_to_paid()
    {
        create_a_booking();
        Profil profil = new Profil();
        List<BookingInfo> bookings = bs.getBookings();
        assertEquals(1, bookings.size());
        BookingInfo bi = bs.setPayed(profil, bookings.get(0), OrderStatus.PAID);
        assertEquals(OrderStatus.PAID, bi.getStatus());

        bookings = bs.getBookings();
        assertEquals(1, bookings.size());
        assertEquals(OrderStatus.PAID, bookings.get(0).getStatus());

    }

    @Test
    @Ignore
    // find a working tx
    public void should_check_transaction()
    {
        Profil profil = new Profil();
        profil.setPaypalAT(PaypalPaymentChecker.TEST_AT);
        profil.setPaypalURL(PaypalPaymentChecker.TEST_PAYPAL_URL);
        OrderStatus hasPaid = new PaypalPaymentChecker("6LH559390U430214T", profil).hasClientPaid();
        assertEquals(OrderStatus.PAID, hasPaid);
    }

}
