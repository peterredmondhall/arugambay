package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.TransportStepUi;

public class RatingStep implements WizardStep
{

    private final TransportStepUi ui;

    public RatingStep()
    {
        ui = new TransportStepUi();
    }

    @Override
    public String getCaption()
    {
        return MESSAGES.firstPage();
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
