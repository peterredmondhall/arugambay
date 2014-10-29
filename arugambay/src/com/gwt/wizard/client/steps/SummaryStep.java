package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;
import static com.gwt.wizard.client.core.Wizard.BOOKINGINFO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.client.steps.ui.SummaryStepUi;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.BookingInfo;

public class SummaryStep implements WizardStep
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    private final SummaryStepUi ui;

    public SummaryStep()
    {
        ui = new SummaryStepUi();
    }

    @Override
    public String getCaption()
    {
        return MESSAGES.fourthPage();
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    @Override
    public Boolean onNext()
    {
        if (BOOKINGINFO.getOrderType() == OrderType.SHARE)
        {
            service.sendShareRequest(BOOKINGINFO, new AsyncCallback<BookingInfo>()
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
                }
            });
        }

        return true;
    }

    @Override
    public Boolean onBack()
    {
        return true;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        ui.show(visible, prev, next, cancel);
    }

}
