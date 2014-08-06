package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.TransportStepUi;
import com.gwt.wizard.shared.model.BookingInfo;

public class TransportStep implements WizardStep
{

    private final TransportStepUi ui;
    private final BookingInfo bookingInfo;
    private static DateTimeFormat sdf = DateTimeFormat.getFormat("dd.MM.yyyy");

    public TransportStep(BookingInfo bookingInfo)
    {
        ui = new TransportStepUi();
        this.bookingInfo = bookingInfo;
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
}
