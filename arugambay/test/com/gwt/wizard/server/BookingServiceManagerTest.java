package com.gwt.wizard.server;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.gwt.wizard.server.entity.ArchivedBooking;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.server.entity.Rating;
import com.gwt.wizard.server.util.Mailer;
import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ContractorInfo;
import com.gwt.wizard.shared.model.RatingInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;
import com.gwt.wizard.shared.model.RouteInfo.SaveMode;

public class BookingServiceManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    BookingServiceManager bs = new BookingServiceManager();
    RatingManager ratingManager = new RatingManager();

    AgentInfo testAgentInfo;
    ContractorInfo contractorInfo;

    RouteInfo standardRoute;
    RouteInfo routeContractor2Route1;
    RouteInfo routeWithAssociated;
    RouteInfo routeAssociated;

    @Before
    public void setUp()
    {
        helper.setUp();
        new BookingServiceManager().getProfil();
        testAgentInfo = new BookingServiceImpl().createDefaultUser();
        List<RouteInfo> routeInfoList = new RouteServiceManager().getRoutes(testAgentInfo);
        List<ContractorInfo> contractors = new ContractorManager().getContractors(testAgentInfo);

        standardRoute = routeInfoList.get(0);
        routeContractor2Route1 = routeInfoList.get(1);
        routeAssociated = create_route(testAgentInfo, contractors.get(1), 0L);
        routeWithAssociated = create_route(testAgentInfo, contractors.get(1), routeAssociated.getId());
    }

    public static RouteInfo create_route(AgentInfo agentInfo, ContractorInfo contractorInfo, Long associatedRouteId)
    {
        RouteInfo associatedRoute = new RouteInfo();
        associatedRoute.setStart("start");
        associatedRoute.setEnd(":end");
        associatedRoute.setPickupType(PickupType.AIRPORT);
        associatedRoute.setCents(101L);
        associatedRoute.setAgentCents(100L);
        associatedRoute.setContractorId(contractorInfo.getId());
        associatedRoute.setAssociatedRoute(associatedRouteId);
        associatedRoute = new RouteServiceManager().addRoute(agentInfo, associatedRoute, SaveMode.ADD);
        return associatedRoute;

    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    public static BookingInfo getBookingInfo(RouteInfo routeInfo, Date date, String flightNo, String landingTime, String name, int pax, int surfboards, String email, String reqs, OrderType orderType, boolean shareWanted)
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
        bi.setName(name);
        bi.setRouteId(routeInfo.getId());

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
        return getStandardBookingInfo(standardRoute);
    }

    private BookingInfo getStandardBookingInfo(RouteInfo routeInfo)
    {
        return getBookingInfo(routeInfo, getDateInOneMonth(), "flightNo", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
    }

    private BookingInfo create_a_booking()
    {
        return create_a_booking(standardRoute);
    }

    private BookingInfo create_a_booking(RouteInfo routeInfo)
    {
        return bs.addBookingWithClient(getStandardBookingInfo(routeInfo), "client");
    }

    private List<RatingInfo> create_a_rating(Profil profil)
    {
        List<BookingInfo> bookings = bs.getBookings();
        assertEquals(1, bookings.size());
        BookingInfo bi = bs.setPayed(profil, bookings.get(0), OrderStatus.PAID);

        RatingInfo ratingInfo = new RatingInfo();
        ratingInfo.setAuthor("author");
        ratingInfo.setCleanliness(5);
        ratingInfo.setProfessionality(4);
        ratingInfo.setPunctuality(3);
        ratingInfo.setSafety(2);
        ratingInfo.setCritic("good");
        ratingInfo.setBookingId(bi.getId());

        ratingManager.add(ratingInfo);
        return ratingManager.getRatings(bi.getRouteInfo());

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

    private Date getDateTwoDaysPrevious()
    {
        return new DateTime().minusDays(2).toDate();
    }

    private void create_a_shared_booking()
    {
        List<BookingInfo> list = bs.getBookings();
        BookingInfo parentBookingInfo = list.get(0);
        BookingInfo shareBooking = new BookingInfo();

        RouteInfo routeInfo = new RouteServiceManager().getRoutes().get(0);
        shareBooking.setRouteInfo(routeInfo);
        shareBooking.setRouteId(routeInfo.getId());

        shareBooking.setParentId(parentBookingInfo.getId());
        shareBooking.setDate(parentBookingInfo.getDate());
        shareBooking.setFlightNo("sharer flight");
        shareBooking.setLandingTime(parentBookingInfo.getLandingTime());

        shareBooking.setPax(22);
        shareBooking.setSurfboards(23);
        shareBooking.setEmail("sharerEmail");
        shareBooking.setName("Peter Sharer");

        shareBooking.setStatus(OrderStatus.BOOKED);
        shareBooking.setOrderType(OrderType.SHARE);

        bs.addBookingWithClient(shareBooking, "clientShare");

        boolean tested = false;
        List<BookingInfo> existingBookings = bs.getBookings();
        for (BookingInfo bookingInfo : existingBookings)
        {
            if (bookingInfo.getOrderType().equals(OrderType.SHARE))
            {
                list = new BookingServiceImpl().handleShareAccepted(bookingInfo.getId());
                assertEquals(2, list.size());
                assertEquals(parentBookingInfo.getId(), list.get(0).getId());
                assertEquals(bookingInfo.getId(), list.get(1).getId());
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
    public void should_return_bookings_on_associated_routes()
    {

        BookingInfo create_a_booking1 = create_a_booking(routeWithAssociated);
        BookingInfo create_a_booking2 = create_a_booking(routeAssociated);
        bs.setPayed(bs.getProfil(), create_a_booking1, OrderStatus.PAID);
        bs.setPayed(bs.getProfil(), create_a_booking2, OrderStatus.PAID);
        List<BookingInfo> bookings = bs.getBookingsForRoute(routeWithAssociated);
        assertEquals(2, bookings.size());

        bookings = bs.getBookingsForRoute(routeAssociated);
        assertEquals(OrderStatus.PAID, bookings.get(0).getStatus());
        assertEquals(1, bookings.size());

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
    public void should_cancel_at_booking()
    {
        create_a_booking();

        List<BookingInfo> bookings = bs.getBookings();
        assertEquals(1, bookings.size());
        BookingInfo bookingInfo = bookings.get(0);
        bs.cancel(bookingInfo);
        bookings = bs.getBookings();
        assertEquals(1, bookings.size());
        assertEquals(OrderStatus.CANCELED, bookings.get(0).getStatus());
    }

    @Test
    public void should_return_only_bookings_for_agent()
    {
        BookingInfo bi1 = getStandardBookingInfo();
        BookingInfo bi2 = getStandardBookingInfo();

        RouteInfo routeInfo1 = new RouteServiceManager().getRoutes(testAgentInfo).get(0);
        RouteInfo routeInfo2 = new RouteServiceManager().getRoutes(testAgentInfo).get(1);

        List<Contractor> contractor = new ContractorManager().getAll(Contractor.class);
        assertEquals(contractor.get(0).getAgentId(), contractor.get(1).getAgentId());

        bi1.setRouteInfo(routeInfo1);
        bi2.setRouteInfo(routeInfo2);

        bs.addBookingWithClient(bi1, "client");
        bs.addBookingWithClient(bi2, "client");

        AgentInfo agent1 = new AgentManager().getAgent("test@example.com");
        AgentInfo agent2 = new AgentManager().getAgent("agent@example.com");

        assertEquals(2, bs.getBookings().size());
        assertEquals(0, bs.getBookings(agent2.getId()).size());
        assertEquals(2, bs.getBookings(agent1.getId()).size());
    }

    @Test
    public void should_return_a_contractor()
    {
        BookingInfo bookingInfo = create_a_booking();

        ContractorInfo ci = bs.getContractor(bookingInfo);
        assertEquals("test:contractor0", ci.getName());
    }

    @Test
    public void should_create_a_shared_booking()
    {
        create_a_booking();
        confirm_payment();
        Mailer.templateMap.put(Mailer.SHARE_ACCEPTED, new File("war/template/shareAccepted.html"));
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
    public void should_delete_all()
    {
        create_a_booking();
        bs.deleteAll(Booking.class);
        List<BookingInfo> bookings = bs.getBookings();
        assertEquals(0, bookings.size());

    }

    @Test
    public void should_create_dataset()
    {
        create_a_booking();
        create_a_rating(new Profil());

        String dataset = bs.dump(Booking.class);
        assertEquals(3, (dataset.split("com.gwt.wizard.shared.model.BookingInfo").length));
        dataset = ratingManager.dump(Rating.class);
        assertEquals(3, (dataset.split("com.gwt.wizard.shared.model.RatingInfo").length));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_import_booking_from_a_string() throws IOException
    {
        create_a_booking();
        create_a_rating(new Profil());
        String dataset = bs.dump(Booking.class);

        bs.importDataset(dataset, Booking.class);
        List<BookingInfo> bookings = bs.getBookings();
        assertEquals(1, bookings.size());

        dataset = ratingManager.dump(Rating.class);

        ratingManager.importDataset(dataset, Rating.class);
        assertEquals(1, ratingManager.getAll(Rating.class).size());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_import_booking() throws IOException
    {
        URL url = Resources.getResource("dataset.txt");
        String dataset = Resources.toString(url, Charsets.UTF_8);
        assertEquals(true, dataset.length() > 100);

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

    @Test
    public void should_add_rating()
    {
        create_a_booking();
        List<RatingInfo> ratings = create_a_rating(new Profil());

        assertEquals(1, ratings.size());
        RatingInfo ratingInfo = ratings.get(0);
        assertEquals("author", ratingInfo.getAuthor());
        assertEquals(new Integer(5), ratingInfo.getCleanliness());
        assertEquals(new Integer(4), ratingInfo.getProfessionality());
        assertEquals(new Integer(3), ratingInfo.getPunctuality());
        assertEquals(new Integer(2), ratingInfo.getSafety());
        assertEquals("good", ratingInfo.getCritic());

    }

    @Test
    public void should_return_paid_unrated()
    {
        Profil profil = new Profil();
        BookingInfo bookingInfoPaid = getBookingInfo(standardRoute, getDateTwoDaysPrevious(), "flightNo", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        bookingInfoPaid = bs.addBookingWithClient(bookingInfoPaid, "client");
        bs.setPayed(profil, bookingInfoPaid, OrderStatus.PAID);

        BookingInfo bookingInfoUnpaid = getBookingInfo(standardRoute, getDateInOneMonth(), "flightNo", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        bs.addBookingWithClient(bookingInfoUnpaid, "client");

        List<BookingInfo> bookings = bs.getListFeedbackRequest();
        assertEquals(1, bookings.size());
        bookings = bs.getListFeedbackRequest();
        assertEquals(0, bookings.size());

    }

    @Test
    public void should_compare_dates()
    {
        BookingInfo biMinus2 = getBookingInfo(standardRoute, new DateTime().minusMonths(2).toDate(), "flightNo", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        BookingInfo binow = getBookingInfo(standardRoute, new DateTime().now().toDate(), "flightNo", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        BookingInfo biPlus2 = getBookingInfo(standardRoute, new DateTime().plusMonths(2).toDate(), "flightNo", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);

        List<BookingInfo> list = Lists.newArrayList(binow, biPlus2, biMinus2);
        List<BookingInfo> expected = Lists.newArrayList(biMinus2, binow, biPlus2);
        Collections.sort(list, new BookingServiceManager().new BookingInfoComparator());

        assertEquals(list, expected);
    }

    @Test
    public void should_archive()
    {

        BookingInfo biMinus2 = getBookingInfo(standardRoute, new DateTime().minusMonths(2).toDate(), "biMinus2", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        BookingInfo binow = getBookingInfo(standardRoute, new DateTime().now().toDate(), "binow", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        BookingInfo biPlus2 = getBookingInfo(standardRoute, new DateTime().plusMonths(2).toDate(), "biPlus2", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        bs.addBookingWithClient(biMinus2, "client");
        bs.addBookingWithClient(binow, "client");
        bs.addBookingWithClient(biPlus2, "client");

        assertEquals(bs.getAll(Booking.class).size(), 3);
        assertEquals(bs.getAll(ArchivedBooking.class).size(), 0);

        List<BookingInfo> archiveList = bs.getArchiveList();
        assertEquals(archiveList.size(), 1);
        assertEquals(archiveList.get(0).getDate(), biMinus2.getDate());

        bs.archive(archiveList.get(0));

        assertEquals(bs.getAll(Booking.class).size(), 2);
        assertEquals(bs.getAll(ArchivedBooking.class).size(), 1);
    }

}
