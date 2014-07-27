package com.gwt.wizard.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwt.wizard.client.core.PartyRouteWizard;
import com.gwt.wizard.client.partyroute.steps.FirstStep;
import com.gwt.wizard.client.resources.ClientMessages;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtPartyRoute implements EntryPoint
{

    private final BookingServiceAsync service = GWT.create(BookingService.class);
    public static ClientMessages MESSAGES = GWT.create(ClientMessages.class);

    private FirstStep firstStep;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad()
    {
        firstStep = new FirstStep();

        PartyRouteWizard wizard = new PartyRouteWizard();
        wizard.add(firstStep);

        wizard.setHeight("500px");
        wizard.setWidth("800px");

        wizard.addFinishCallback(new ICallback()
        {
            @Override
            public void execute()
            {
            }
        });

        RootPanel.get().add(wizard.getWidget());
    }
}
