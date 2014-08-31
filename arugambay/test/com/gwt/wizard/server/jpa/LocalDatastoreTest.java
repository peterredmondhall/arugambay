package com.gwt.wizard.server.jpa;

import static org.junit.Assert.assertEquals;

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
import com.gwt.wizard.server.BookingServiceManager;
import com.gwt.wizard.server.PaymentChecker;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.BookingInfo;

public class LocalDatastoreTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    BookingServiceManager bs = new BookingServiceManager();

    @Before
    public void setUp()
    {
        helper.setUp();
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    private BookingInfo getBookingInfo(Date date, String flightNo, String landingTime, int pax, int surfboards, String email, String reqs, OrderType orderType, boolean shareWanted)
    {
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
        bs.saveWithClient(getStandardBookingInfo(), "client");

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
        List<BookingInfo> list = bs.getBookingsForTour("ref");
        BookingInfo parentBookingInfo = list.get(0);
        BookingInfo bi = new BookingInfo();
        bi.setParentRef(parentBookingInfo.getRef());
        bi.setDate(parentBookingInfo.getDate());
        bi.setFlightNo(parentBookingInfo.getFlightNo());
        bi.setLandingTime(parentBookingInfo.getLandingTime());

        bi.setPax(22);
        bi.setSurfboards(23);
        bi.setEmail("sharerEmail");

        bi.setStatus(OrderStatus.BOOKED);
        bi.setOrderType(OrderType.SHARE);

        bs.saveWithClient(bi, "clientShare");

        boolean tested = false;
        for (BookingInfo bookingInfo : bs.getBookings())
        {
            if (bookingInfo.getOrderType().equals(OrderType.SHARE))
            {
                list = bs.getBookingsForShare(bookingInfo.getRef());
                assertEquals(2, list.size());
                assertEquals(parentBookingInfo.getRef(), list.get(0).getRef());
                assertEquals(bookingInfo.getRef(), list.get(1).getRef());
                tested = true;
                break;
            }
        }
        assertEquals(true, tested);

    }

    @Test
    public void should_return_only_paidfor_future_bookings()
    {

        BookingInfo bi = getStandardBookingInfo();
        bi.setDate(new DateTime().plusYears(20).toDate());
        bs.saveWithClient(bi, "client");
        List<BookingInfo> list = bs.getBookingsForTour("ref");
        // bookin is not paid for
        assertEquals(0, list.size());

        bi = getStandardBookingInfo();
        bi.setDate(new DateTime().plusYears(20).toDate());
        bi.setOrderType(OrderType.SHARE);
        bs.saveWithClient(bi, "client");
        list = bs.getBookingsForTour("ref");
        // bookin is not BOOKING
        assertEquals(0, list.size());

        create_a_booking();
        bi = bs.getBookingForTransactionWithClient(bs.getProfil(), "client", OrderStatus.PAID);
        List<BookingInfo> bookings = bs.getBookings();
        assertEquals(OrderStatus.PAID, bookings.get(0).getStatus());
        list = bs.getBookingsForTour("ref");
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
        List<BookingInfo> list = bs.getBookingsForTour("ref");
        assertEquals(1, list.size());
        Date date = list.get(0).getDate();
        assertEquals(false, date instanceof org.datanucleus.store.types.sco.simple.Date);
        System.out.println(date);

    }

    @Test
    @Ignore
    // find a working tx
    public void should_check_transaction()
    {
        Profil profil = new Profil();
        profil.setPaypalAT(PaymentChecker.TEST_AT);
        profil.setPaypalURL(PaymentChecker.TEST_PAYPAL_URL);
        OrderStatus hasPaid = new PaymentChecker("6LH559390U430214T", profil).hasClientPaid();
        assertEquals(OrderStatus.PAID, hasPaid);
    }

}
