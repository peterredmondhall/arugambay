package com.gwt.wizard.client.partyroute.steps;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.partyroute.steps.ui.FirstStepUi;

public class FirstStep implements WizardStep
{

    private final FirstStepUi ui;
    private static DateTimeFormat sdf = DateTimeFormat.getFormat("dd.MM.yyyy");

    public FirstStep()
    {
        ui = new FirstStepUi();
    }

    @Override
    public String getCaption()
    {
        return "Party Route";
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    @Override
    public Boolean onNext()
    {
        return false;
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
