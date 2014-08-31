package com.gwt.wizard.client;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.resources.ClientMessages;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.client.steps.ConfirmationStep;
import com.gwt.wizard.client.steps.ContactStep;
import com.gwt.wizard.client.steps.ShareConfirmationStep;
import com.gwt.wizard.client.steps.ShareStep;
import com.gwt.wizard.client.steps.SummaryStep;
import com.gwt.wizard.client.steps.TransportStep;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.StatInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtWizard implements EntryPoint
{

    private final BookingServiceAsync service = GWT.create(BookingService.class);
    public static ClientMessages MESSAGES = GWT.create(ClientMessages.class);

    private TransportStep transportStep;
    private ShareStep shareStep;
    private ContactStep contactStep;
    private SummaryStep summaryStep;
    private ConfirmationStep confirmationStep;
    private ShareConfirmationStep shareConfirmationStep;

    BookingInfo bookingInfo = new BookingInfo();

    public static final int TRANSPORT = 1;
    public static final int SHARE = 2;
    public static final int CONTACT = 3;
    public static final int SUMMARY = 4;
    public static final int CONFIRMATION = 5;

    private Wizard wizard;

    // private List<BookingInfo> sharers;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad()
    {
        Window.setTitle("Arugam Bay Taxi");
        transportStep = new TransportStep(bookingInfo);
        shareStep = new ShareStep(bookingInfo);
        contactStep = new ContactStep(bookingInfo);
        summaryStep = new SummaryStep(bookingInfo);
        confirmationStep = new ConfirmationStep(bookingInfo);
        shareConfirmationStep = new ShareConfirmationStep();

        collectStats();

        String transaction = Window.Location.getParameter("tx");
        String shareRef = Window.Location.getParameter("share");
        if (transaction != null)
        {
            handleTransaction(transaction);
        }
        else if (shareRef != null)
        {
            handleShareAccepted(shareRef);
        }
        else
        {
            List<WizardStep> l = ImmutableList.of(transportStep, shareStep, contactStep, summaryStep, confirmationStep);
            completeSetup(transportStep, l);
        }

    }

    private void collectStats()
    {
        StatInfo statInfo = new StatInfo();
        String country = Window.Location.getParameter("X-AppEngine-Country");
        statInfo.setCountry(country);

        service.sendStat(statInfo, new AsyncCallback<Void>()
        {

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to server");
            }

            @Override
            public void onSuccess(Void bi)
            {

            }
        });

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
        service.getBookingForTransaction(transaction, new AsyncCallback<BookingInfo>()
        {

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to server");
            }

            @Override
            public void onSuccess(BookingInfo bi)
            {
                bookingInfo = bi;
                confirmationStep.setBookingInfo(bookingInfo);
                completeSetup(confirmationStep, ImmutableList.of((WizardStep) confirmationStep));

            }
        });

    }

    private void handleShareAccepted(String shareRef)
    {
        service.getBookingsForShare(shareRef, new AsyncCallback<List<BookingInfo>>()
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
                bookingInfo = sharedBookingList.get(0);
                shareConfirmationStep.setBookingInfo(sharedBookingList);
                completeSetup(shareConfirmationStep, ImmutableList.of((WizardStep) shareConfirmationStep));
            }
        });

    }

    private void completeSetup(WizardStep initstep, List<WizardStep> steps)
    {
        wizard = new Wizard();
        wizard.setInitialStep(initstep);
        for (WizardStep step : steps)
        {
            wizard.add(step);
        }
        final GwtWizard thisGwtWizard = this;
        wizard.setHeight("500px");
        wizard.setWidth("800px");

        wizard.addSaveBookingCallback(new ICallback()
        {
            @Override
            public void execute()
            {
                service.save(bookingInfo, new AsyncCallback<BookingInfo>()
                {
                    @Override
                    public void onSuccess(BookingInfo result)
                    {
                        bookingInfo.setRef(result.getRef());
                    }

                    @Override
                    public void onFailure(Throwable caught)
                    {
                    }
                });
            }
        });

        wizard.addGetBookingListCallback(new ICallback()
        {
            @Override
            public void execute()
            {
                service.getBookingsForTour(bookingInfo.getRef(), new AsyncCallback<List<BookingInfo>>()
                {
                    @Override
                    public void onSuccess(List<BookingInfo> list)
                    {
                        shareStep.setBookingList(list, thisGwtWizard);
                    }

                    @Override
                    public void onFailure(Throwable caught)
                    {
                    }
                });
            }
        });

        wizard.addSendShareRequestCallback(new ICallback()
        {
            @Override
            public void execute()
            {
                service.sendShareRequest(bookingInfo, new AsyncCallback<BookingInfo>()
                {

                    @Override
                    public void onFailure(Throwable caught)
                    {
                        Window.alert("Failed to connect to server");
                    }

                    @Override
                    public void onSuccess(BookingInfo bi)
                    {
                        confirmationStep.setBookingInfo(bi);
                    }
                });
            }
        });

//        wizard.addSendShareAcceptedCallback(new ICallback()
//        {
//            @Override
//            public void execute()
//            {
//                service.sendShareAccepted(sharers, new AsyncCallback<BookingInfo>()
//                {
//
//                    @Override
//                    public void onFailure(Throwable caught)
//                    {
//                        Window.alert("Failed to connect to server");
//                    }
//
//                    @Override
//                    public void onSuccess(BookingInfo success)
//                    {
//                        wizard.activateShareConfirmationStep(shareConfirmationStep);
//                    }
//                });
//            }
//        });

        wizard.init();
        RootPanel.get().add(wizard);
    }

    public void shareBooking(BookingInfo bookingToShare)
    {
        wizard.onNextShare();

    }
}
