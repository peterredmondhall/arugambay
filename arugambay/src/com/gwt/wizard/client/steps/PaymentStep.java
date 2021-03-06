package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.ConfirmationStepUi;

public class PaymentStep implements WizardStep
{

    private final ConfirmationStepUi ui;

    public PaymentStep()
    {
        ui = new ConfirmationStepUi();
    }

    @Override
    public String getCaption()
    {
        return Wizard.MOBILE ? "" : MESSAGES.fifthPage();
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    @Override
    public Boolean onNext()
    {
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

}
