package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.RequirementsStepUi;
import com.gwt.wizard.shared.model.BookingInfo;

public class RequirementsStep implements WizardStep
{

    private final RequirementsStepUi ui;
    private final BookingInfo bookingInfo;

    public RequirementsStep(BookingInfo bookingInfo)
    {
        ui = new RequirementsStepUi();
        this.bookingInfo = bookingInfo;
    }

    @Override
    public String getCaption()
    {
        return MESSAGES.thirdPage();
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    @Override
    public Boolean onNext()
    {
        bookingInfo.setRequirements(ui.getRequirements());
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
        //
    }
}
