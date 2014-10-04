package com.gwt.wizard.client.steps;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.GwtWizard;
import com.gwt.wizard.client.core.Wizard;
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
    }

    public void setBookingList(List<BookingInfo> list, GwtWizard gwtWizard)
    {
        ui.setBookingList(list, gwtWizard);
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
        Wizard.bookingInfo.setOrderType(OrderType.BOOKING);
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
        Wizard.bookingInfo.setOrderType(OrderType.SHARE);
        ui.removeTable();
    }
}
