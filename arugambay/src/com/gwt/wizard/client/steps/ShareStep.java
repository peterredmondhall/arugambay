package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.core.Wizard.BOOKINGINFO;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.ShareStepUi;
import com.gwt.wizard.shared.OrderType;

public class ShareStep implements WizardStep
{

    private final ShareStepUi ui;

    public ShareStep(Wizard wizard)
    {
        ui = new ShareStepUi(wizard);
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
