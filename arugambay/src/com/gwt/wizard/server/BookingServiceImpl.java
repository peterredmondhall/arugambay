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
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ContractorInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.RatingInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;
import com.gwt.wizard.shared.model.RouteInfo.SaveMode;
import com.gwt.wizard.shared.model.StatInfo;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BookingServiceImpl extends RemoteServiceServlet implements
        BookingService
{
    private static final Logger logger = Logger.getLogger(BookingServiceImpl.class.getName());

    private final BookingServiceManager bookingServiceManager = new BookingServiceManager();
    private final RouteServiceManager routeServiceManager = new RouteServiceManager();
    private final AgentManager agentManager = new AgentManager();
    private final ContractorManager contractorManager = new ContractorManager();
    private final StatManager statManager = new StatManager();
    private final RatingManager ratingManager = new RatingManager();
    private final StripePayment stripePayment = new StripePayment();

    @Override
    public BookingInfo addBooking(BookingInfo bookingInfo) throws IllegalArgumentException
    {
        return bookingServiceManager.addBookingWithClient(bookingInfo, getClient());
    }

    @Override
    public List<RouteInfo> deleteRoute(AgentInfo userInfo, RouteInfo placeInfo) throws IllegalArgumentException
    {
        return routeServiceManager.deleteRoute(userInfo, placeInfo);
    }

    @Override
    public List<RouteInfo> saveRoute(AgentInfo userInfo, RouteInfo placeInfo, RouteInfo.SaveMode mode) throws IllegalArgumentException
    {
        return routeServiceManager.saveRoute(userInfo, placeInfo, mode);
    }

    @Override
    public List<RouteInfo> getRoutes(AgentInfo userInfo) throws IllegalArgumentException
    {
        return routeServiceManager.getRoutes(userInfo);
    }

    @Override
    public List<RouteInfo> getRoutes() throws IllegalArgumentException
    {
        return routeServiceManager.getRoutes();
    }

    @Override
    public List<BookingInfo> getBookings(AgentInfo agentInfo) throws IllegalArgumentException
    {
        return bookingServiceManager.getBookings(agentInfo.getId());
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
        logger.info("route inquiry:" + routeInfo.getStart() + " -> " + routeInfo.getEnd());
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
            Mailer.sendShareAccepted(sharerBookingInfo.getEmail(), parentBookingInfo, bookingServiceManager.getProfil());
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
        logger.info(statInfo.getDetail());
        statManager.updateSessionStat(statInfo);
    }

    @Override
    public AgentInfo getUser() throws IllegalArgumentException
    {
        AgentInfo userInfo = null;
        User user = getUserFromSession();
        if (user != null)
        {
            userInfo = agentManager.getAgent(user.getEmail());
        }
        return userInfo;
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
    public List<RatingInfo> getRatings(RouteInfo routeInfo) throws IllegalArgumentException
    {
        return ratingManager.getRatings(routeInfo);
    }

    @Override
    public void addRating(RatingInfo ratingInfo) throws IllegalArgumentException
    {
        ratingManager.add(ratingInfo);
    }

    @Override
    public AgentInfo createDefaultUser()
    {
        String DEFAULTUSEREMAIL = "test@example.com";
        if (bookingServiceManager.getMaintenceAllowed())
        {
            for (String testAgent : new String[] { "test", "agent" })
            {
                String agentEmail = testAgent + "@example.com";
                AgentInfo agentInfo = agentManager.getAgent(agentEmail);
                if (agentInfo != null)
                {
                    List<RouteInfo> routes = routeServiceManager.getRoutes(agentInfo);
                    for (RouteInfo routeInfo : routes)
                    {
                        routeServiceManager.deleteRoute(agentInfo, routeInfo);
                    }
                    List<ContractorInfo> listContractors = contractorManager.getContractors(agentInfo);
                    for (ContractorInfo contractorInfo : listContractors)
                    {
                        contractorManager.delete(contractorInfo);
                    }
                }
                else
                {
                    agentInfo = new AgentManager().createAgent(agentEmail);

                }
                for (int i = 0; i < 2; i++)
                {
                    ContractorInfo contractorInfo = new ContractorInfo();
                    contractorInfo.setAgentId(agentInfo.getId());
                    contractorInfo.setName(testAgent + ":contractor" + i);
                    contractorInfo = new ContractorManager().createContractor(contractorInfo);
                    RouteInfo routeInfo = new RouteInfo();
                    routeInfo.setStart(testAgent + "start" + i);
                    routeInfo.setEnd(testAgent + ":end" + i);
                    routeInfo.setPickupType(PickupType.AIRPORT);
                    routeInfo.setContractorId(contractorInfo.getId());
                    new RouteServiceManager().saveRoute(agentInfo, routeInfo, SaveMode.ADD);
                }
            }
            return new AgentManager().getAgent(DEFAULTUSEREMAIL);
        }
        else
        {
            logger.info("maintence not allowed");
            return null;
        }
    }

    @Override
    public List<ContractorInfo> getContractors(AgentInfo agentInfo) throws IllegalArgumentException
    {
        return contractorManager.getContractors(agentInfo);
    }

    @Override
    public List<ContractorInfo> deleteContractor(AgentInfo agentInfo, ContractorInfo contractorInfo) throws IllegalArgumentException
    {
        return contractorManager.deleteContractor(agentInfo, contractorInfo);
    }

    @Override
    public List<ContractorInfo> saveContractor(AgentInfo agentInfo, ContractorInfo contractorInfo, ContractorInfo.SaveMode mode) throws IllegalArgumentException
    {
        return contractorManager.saveContractor(agentInfo, contractorInfo, mode);
    }

    @Override
    public List<AgentInfo> getAgents() throws IllegalArgumentException
    {
        return agentManager.getAgents();
    }

    public void sendRatingRequest()
    {
        Profil profil = bookingServiceManager.getProfil();
        List<BookingInfo> list = bookingServiceManager.getListFeedbackRequest();
        logger.info("todo size:" + list.size());
        for (BookingInfo bi : list)
        {
            Mailer.setFeedbackRequest(bi, profil);
        }

        for (BookingInfo bi : bookingServiceManager.getArchiveList())
        {
            bookingServiceManager.archive(bi);
        }

    }

    @Override
    public RouteInfo getRoute(Long routeId) throws IllegalArgumentException
    {
        return routeServiceManager.getRoute(routeId);
    }

}
