package com.gwt.wizard.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.resources.ClientMessages;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.client.steps.ContactStep;
import com.gwt.wizard.client.steps.PaymentStep;
import com.gwt.wizard.client.steps.RequirementsStep;
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

    private TransportStep routeStep;
    private ContactStep organizerStep;
    private RequirementsStep requirementsStep;
    private SummaryStep summaryStep;
    private PaymentStep confirmationStep;

    BookingInfo bookingInfo = new BookingInfo();

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad()
    {
        routeStep = new TransportStep(bookingInfo);
        organizerStep = new ContactStep(bookingInfo);
        requirementsStep = new RequirementsStep(bookingInfo);
        summaryStep = new SummaryStep(bookingInfo);
        confirmationStep = new PaymentStep(bookingInfo);

        Wizard wizard = new Wizard();
        wizard.add(routeStep);
        wizard.add(organizerStep);
        wizard.add(requirementsStep);
        wizard.add(summaryStep);
        wizard.add(confirmationStep);

        wizard.setHeight("500px");
        wizard.setWidth("800px");

        wizard.addFinishCallback(new ICallback()
        {
            @Override
            public void execute()
            {
                service.saveWizardData(bookingInfo, new AsyncCallback<Boolean>()
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
