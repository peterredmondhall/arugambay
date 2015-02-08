package com.gwt.wizard.client;

import static com.gwt.wizard.client.core.Wizard.BOOKINGINFO;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.core.WizardMobile;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.resources.ClientMessages;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.client.steps.ConfirmationStep;
import com.gwt.wizard.client.steps.ContactStep;
import com.gwt.wizard.client.steps.CreditCardStep;
import com.gwt.wizard.client.steps.RatingStep;
import com.gwt.wizard.client.steps.ShareConfirmationStep;
import com.gwt.wizard.client.steps.ShareStep;
import com.gwt.wizard.client.steps.SummaryStep;
import com.gwt.wizard.client.steps.TransportStep;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.RatingInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.StatInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtWizard implements EntryPoint
{
    public static final Logger logger = Logger.getLogger(GwtWizard.class.getName());
    public static final BookingServiceAsync SERVICE = GWT.create(BookingService.class);
    public static ClientMessages MESSAGES = GWT.create(ClientMessages.class);

    private TransportStep transportStep;
    private ShareStep shareStep;
    private ContactStep contactStep;
    private CreditCardStep creditCardStep;
    private SummaryStep summaryStep;
    private ConfirmationStep confirmationStep;
    private ShareConfirmationStep shareConfirmationStep;
    private RatingStep ratingStep;

    public static final int TRANSPORT = 1;
    public static final int SHARE = 2;
    public static final int CONTACT = 3;
    public static final int SUMMARY = 4;
    public static final int CREDITCARD = 5;
    public static final int CONFIRMATION = 6;

    private Wizard wizard;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad()
    {
        Window.setTitle("taxisurfr");

        Wizard.SCREEN_WIDTH = Window.getClientWidth();
        Wizard.SCREEN_HEIGHT = Window.getClientHeight();
        Wizard.MOBILE = Wizard.SCREEN_WIDTH < 500;

        if (Wizard.MOBILE)
        {
            wizard = new WizardMobile();
        }
        else
        {
            wizard = new Wizard();

        }
        transportStep = new TransportStep();
        shareStep = new ShareStep(wizard);
        contactStep = new ContactStep();
        creditCardStep = new CreditCardStep(wizard);
        summaryStep = new SummaryStep();
        confirmationStep = new ConfirmationStep();
        shareConfirmationStep = new ShareConfirmationStep();
        ratingStep = new RatingStep();

        String transaction = Window.Location.getParameter("tx");
        String shareId = Window.Location.getParameter("share");
        String review = Window.Location.getParameter("review");
        String nick = Window.Location.getParameter("nick");
        String defaultuser = Window.Location.getParameter("defaultagent");
        String routeId = Window.Location.getParameter("route");
        if (defaultuser != null)
        {
            createDefaultUser();
            return;
        }
        if (review != null)
        {
            Wizard.RATINGINFO = new RatingInfo();
            Wizard.RATINGINFO.setBookingId(Long.parseLong(review));
            Wizard.RATINGINFO.setAuthor(nick);

            List<WizardStep> l = ImmutableList.of((WizardStep) ratingStep);
            completeSetup(ratingStep, l);
            return;
        }
        if (transaction != null)
        {
            handleTransaction(transaction);
        }
        else if (shareId != null)
        {
            handleShareAccepted(Long.parseLong(shareId));
        }
        else
        {
            List<WizardStep> l = ImmutableList.of(transportStep, shareStep, contactStep, summaryStep, creditCardStep, confirmationStep);
            completeSetup(transportStep, l);
            displayRoute(transportStep, routeId);
        }
        collectStats();

    }

    private void createDefaultUser()
    {
        SERVICE.createDefaultUser(new AsyncCallback<AgentInfo>()
        {

            @Override
            public void onSuccess(AgentInfo agentInfo)
            {
                if (agentInfo != null)
                {
                    RootPanel.get().add(new Label("agent created:" + agentInfo.getEmail()));

                }
                else
                {
                    RootPanel.get().add(new Label("agent not created"));

                }
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to get paypal url!");
            }
        });

    }

    private void collectStats()
    {
        String protocol = Window.Location.getProtocol();
        String url = protocol + "//" + Window.Location.getHost() + "/stat";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

        try
        {
            Request response = builder.sendRequest(null, new RequestCallback()
            {
                @Override
                public void onError(Request request, Throwable exception)
                {
                }

                @Override
                public void onResponseReceived(Request request, Response response)
                {
                }
            });
        }
        catch (RequestException e)
        {
        }

//        
//
//        X-AppEngine-Region
//
//        Name of region from which the request originated. This value only makes sense in the context of the country in X-AppEngine-Country. For example, if the country is "US" and the region is "ca", that "ca" means "California", not Canada.
//        X-AppEngine-City
//
//        Name of the city from which the request originated. For example, a request from the city of Mountain View might have the header value mountain view.
//        X-AppEngine-CityLatLong        
    }

    private void handleTransaction(String transaction)
    {
        SERVICE.getBookingForTransaction(transaction, new AsyncCallback<BookingInfo>()
        {

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to server");
            }

            @Override
            public void onSuccess(BookingInfo bi)
            {
                BOOKINGINFO = bi;
                confirmationStep.setBookingInfo(Wizard.BOOKINGINFO);
                completeSetup(confirmationStep, ImmutableList.of((WizardStep) confirmationStep));

            }
        });

    }

    private void handleShareAccepted(Long shareId)
    {
        SERVICE.handleShareAccepted(shareId, new AsyncCallback<List<BookingInfo>>()
        {

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to server");
            }

            @Override
            public void onSuccess(List<BookingInfo> sharedBookingList)
            {
                // sharers = sharedBookingList;
                BOOKINGINFO = sharedBookingList.get(0);
                shareConfirmationStep.setBookingInfo(sharedBookingList);
                completeSetup(shareConfirmationStep, ImmutableList.of((WizardStep) shareConfirmationStep));
                wizard.activateShareConfirmationStep(shareConfirmationStep);

            }
        });

    }

    private void completeSetup(WizardStep initstep, List<WizardStep> steps)
    {

        wizard.setInitialStep(initstep);
        for (WizardStep step : steps)
        {
            wizard.add(step);
        }
//        wizard.setHeight(height);
//        wizard.setWidth(width);

        wizard.init();
        RootPanel.get().add(wizard);

        SERVICE.getPaypalProfil(new AsyncCallback<ProfilInfo>()
        {

            @Override
            public void onSuccess(ProfilInfo profil)
            {
                Wizard.PROFILINFO = profil;
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to get paypal url!");
            }
        });
    }

    // private List<BookingInfo> sharers;
    public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
    }-*/;

    public static void sendStat(String type)
    {
        StatInfo statInfo = new StatInfo();
        statInfo.setType(type);
        SERVICE.sendStat(statInfo, new AsyncCallback<Void>()
        {

            @Override
            public void onSuccess(Void profil)
            {
            }

            @Override
            public void onFailure(Throwable caught)
            {
            }
        });

    }

    private void displayRoute(final TransportStep transportStep, String routeId)
    {
        if (routeId == null)
        {
            return;
        }
        try
        {
            Long id = Long.parseLong(routeId);
            SERVICE.getRoute(id, new AsyncCallback<RouteInfo>()
            {

                @Override
                public void onSuccess(RouteInfo routeInfo)
                {
                    Wizard.ROUTEINFO = routeInfo;
                    transportStep.displayRoute();
                }

                @Override
                public void onFailure(Throwable caught)
                {
                }
            });
        }
        catch (Exception ex)
        {

        }
    }
}
