package com.gwt.wizard.server;

import static com.gwt.wizard.server.BookingServiceManagerTest.getBookingInfo;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.FinanceInfo;
import com.gwt.wizard.shared.model.RouteInfo;

public class FinanceManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    FinanceManager manager = new FinanceManager();
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

    @Test
    public void should_get_empty_finance_list()
    {
        AgentInfo agentInfo = new AgentInfo();
        List<FinanceInfo> finances = manager.getFinance(agentInfo);
        assertEquals(0, finances.size());
    }

    @Test
    public void should_get_finance_list_with_a_single_transfer()
    {
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setId(99l);

        manager.addTransfer(agentInfo, new Date(), 101L);
        List<FinanceInfo> finances = manager.getFinance(agentInfo);
        assertEquals(1, finances.size());
        FinanceInfo expectedTransfer = new FinanceInfo();
        expectedTransfer.setName("");
        expectedTransfer.setAmount(101L);
        expectedTransfer.setDate(new Date());
        assertEquals(expectedTransfer.getAmount(), finances.get(0).getAmount());
    }

    @Test
    public void should_get_finance_list_with_a_paid_bookings()
    {
        Profil profil = new BookingServiceManager().getProfil();
        BookingServiceManagerTest.testAgentInfo = new BookingServiceImpl().createDefaultUser();

        BookingInfo bookingInfo = getBookingInfo(new DateTime().minusMonths(2).toDate(), "biMinus2", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        manager.addPayment(bookingInfo, new Date());
        AgentInfo agentInfo = new AgentInfo();

        agentInfo.setId(BookingServiceManagerTest.testAgentInfo.getId());
        List<FinanceInfo> finances = manager.getFinance(agentInfo);
        assertEquals(1, finances.size());
        assertEquals(bookingInfo.getName(), finances.get(0).getName());
        assertEquals(bookingInfo.getOrderRef(), finances.get(0).getOrder());
        RouteInfo routeInfo = new RouteServiceManager().getRoutes(BookingServiceManagerTest.testAgentInfo).get(0);
        Long expectedAmount = (long) (routeInfo.getAgentCents());
        assertEquals(expectedAmount, finances.get(0).getAmount());

        agentInfo.setId(BookingServiceManagerTest.testAgentInfo.getId() + 1);
        finances = manager.getFinance(agentInfo);
        assertEquals(0, finances.size());

    }

    @Test
    public void should_sort_with_newest_first()
    {
        Profil profil = new BookingServiceManager().getProfil();
        BookingServiceManagerTest.testAgentInfo = new BookingServiceImpl().createDefaultUser();

        BookingInfo bookingInfo1 = getBookingInfo(new DateTime().minusMonths(2).toDate(), "biMinus2", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        manager.addPayment(bookingInfo1, new DateTime().minusMonths(2).toDate());
        BookingInfo bookingInfo2 = getBookingInfo(new DateTime().plusMonths(2).toDate(), "biMinus2", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        manager.addPayment(bookingInfo2, new DateTime().plusMonths(2).toDate());
        BookingInfo bookingInfo3 = getBookingInfo(new DateTime().plusMonths(2).toDate(), "biMinus2", "landingTime", "passenger name", 10, 11, "email", "reqs", OrderType.BOOKING, true);
        manager.addPayment(bookingInfo3, new DateTime().toDate());
        AgentInfo agentInfo = new AgentInfo();

        agentInfo.setId(BookingServiceManagerTest.testAgentInfo.getId());

        List<FinanceInfo> finances = manager.getFinance(agentInfo);
        assertEquals(3, finances.size());
        assertEquals(bookingInfo2.getName(), finances.get(0).getName());
        assertEquals(bookingInfo3.getName(), finances.get(1).getName());

    }

}
