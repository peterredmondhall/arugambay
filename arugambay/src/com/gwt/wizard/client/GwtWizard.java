package com.gwt.wizard.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.resources.ClientMessages;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.client.steps.ConfirmationStep;
import com.gwt.wizard.client.steps.ContactStep;
import com.gwt.wizard.client.steps.SummaryStep;
import com.gwt.wizard.client.steps.TransportStep;
import com.gwt.wizard.shared.model.BookingInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtWizard implements EntryPoint
{

    private final BookingServiceAsync service = GWT.create(BookingService.class);
    public static ClientMessages MESSAGES = GWT.create(ClientMessages.class);

    private TransportStep transportStep;
    private ContactStep organizerStep;
    private SummaryStep summaryStep;
    private ConfirmationStep confirmationStep;

    BookingInfo bookingInfo = new BookingInfo();

    public static final int TRANSPORT = 1;
    public static final int CONTACT = 2;
    public static final int SUMMARY = 3;
    public static final int CONFIRMATION = 4;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad()
    {
        transportStep = new TransportStep(bookingInfo);
        organizerStep = new ContactStep(bookingInfo);
        summaryStep = new SummaryStep(bookingInfo);
        confirmationStep = new ConfirmationStep(bookingInfo);

        String ref = Window.Location.getParameter("tx");
        if (ref != null)
        {

            service.getBooking(ref, new AsyncCallback<BookingInfo>()
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
                    setupWizard(bookingInfo);
                }
            });
        }
        else
        {
            setupWizard(null);
        }

    }

    private void setupWizard(BookingInfo bf)
    {

        Wizard wizard = new Wizard();
        if (bf == null)
        {
            wizard.add(transportStep);
            wizard.add(organizerStep);
            wizard.add(summaryStep);
        }
        else
        {
            wizard.add(confirmationStep);
        }

        wizard.setHeight("500px");
        wizard.setWidth("800px");

        wizard.addSaveBookingCallback(new ICallback()
        {
            @Override
            public void execute()
            {
                service.save(bookingInfo, new AsyncCallback<Boolean>()
                {
                    @Override
                    public void onSuccess(Boolean result)
                    {
                    }

                    @Override
                    public void onFailure(Throwable caught)
                    {
                    }
                });
            }
        });

        RootPanel.get().add(wizard.getWidget());
    }
}
