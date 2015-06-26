package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.GwtWizard;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.transport.TransportStepMobileUi;
import com.gwt.wizard.client.steps.ui.transport.TransportStepUi;
import com.gwt.wizard.shared.model.StatInfo;

public class TransportStep implements WizardStep
{

    private final TransportStepUi ui;

    public TransportStep(Wizard wizard)
    {
        if (Wizard.MOBILE)
        {
            ui = new TransportStepMobileUi(wizard);
        }
        else
        {
            ui = new TransportStepUi(wizard);
        }
    }

    @Override
    public String getCaption()
    {
        return Wizard.MOBILE ? "" : MESSAGES.firstPage();
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    public void init(Button prev)
    {
        ui.show(true, prev);
    }

    @Override
    public Boolean onNext()
    {
        GwtWizard.sendStat("step:Transport", StatInfo.Update.TYPE);
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
    public void show(boolean visible, Button prev)
    {
        ui.show(visible, prev);
    }

    public void displayRoute()
    {
        ui.displayRoute();
    }
}
