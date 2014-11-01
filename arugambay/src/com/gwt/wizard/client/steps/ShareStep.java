package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.SERVICE;
import static com.gwt.wizard.client.core.Wizard.BOOKINGINFO;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.ShareStepUi;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.BookingInfo;

public class ShareStep implements WizardStep
{

    private final ShareStepUi ui;

    public ShareStep()
    {
        ui = new ShareStepUi();
        SERVICE.getBookingsForTour(BOOKINGINFO.getId(), new AsyncCallback<List<BookingInfo>>()
        {
            @Override
            public void onSuccess(List<BookingInfo> list)
            {
                setBookingList(list);
            }

            @Override
            public void onFailure(Throwable caught)
            {
            }
        });

    }

    public void setBookingList(List<BookingInfo> list)
    {
        if (list.size() == 0)
        {
            onNext();
        }
        else
        {
            ui.setBookingList(list);
        }
    }

    @Override
    public String getCaption()
    {
        return "Sharing";
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    @Override
    public Boolean onNext()
    {
        BOOKINGINFO.setOrderType(OrderType.BOOKING);
        ui.removeTable();
        return true;
    }

    @Override
    public Boolean onBack()
    {
        ui.removeTable();
        return true;
    }

    @Override
    public void clear()
    {

    }

    public void onNextShare()
    {
        BOOKINGINFO.setOrderType(OrderType.SHARE);
        ui.removeTable();
    }

    @Override
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        ui.show(visible, prev, next, cancel);
    }

}
