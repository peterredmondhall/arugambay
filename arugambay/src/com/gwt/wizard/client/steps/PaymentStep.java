package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.PaymentStepUi;
import com.gwt.wizard.shared.model.BookingInfo;

public class PaymentStep implements WizardStep
{

    private final PaymentStepUi ui;

    public PaymentStep(BookingInfo bookingInfo)
    {
        ui = new PaymentStepUi(bookingInfo);
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
}
