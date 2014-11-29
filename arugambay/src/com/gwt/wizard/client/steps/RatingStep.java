package com.gwt.wizard.client.steps;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.RatingStepUi;

public class RatingStep implements WizardStep
{

    private final RatingStepUi ui;

    public RatingStep(Wizard wizard, ConfirmationStep confirmationStep)
    {
        ui = new RatingStepUi(wizard, confirmationStep);
    }

    @Override
    public String getCaption()
    {
        return "Feedback";
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    public void init(Button prev, Button next, Button cancel)
    {
        ui.show(true, prev, next, cancel);
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
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        ui.show(visible, prev, next, cancel);
    }

}
