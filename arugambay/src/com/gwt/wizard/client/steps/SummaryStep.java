package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.SummaryStepUi;
import com.gwt.wizard.shared.model.BookingInfo;

public class SummaryStep implements WizardStep
{

    private final SummaryStepUi ui;

    public SummaryStep(BookingInfo bookingInfo)
    {
        ui = new SummaryStepUi(bookingInfo);
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
