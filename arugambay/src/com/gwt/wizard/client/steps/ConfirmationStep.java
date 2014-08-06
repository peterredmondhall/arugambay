package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.ConfirmationStepUi;
import com.gwt.wizard.shared.model.BookingInfo;

public class ConfirmationStep implements WizardStep
{

    private final ConfirmationStepUi ui;

    public ConfirmationStep(BookingInfo bookingInfo)
    {
        ui = new ConfirmationStepUi(bookingInfo);
    }

    @Override
    public String getCaption()
    {
        return MESSAGES.fifthPage();
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

    public void init(Button prev, Button next, Button cancel)
    {
        ui.show(true, prev, next, cancel);
    }
}
