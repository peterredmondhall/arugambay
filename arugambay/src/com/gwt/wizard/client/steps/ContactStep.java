package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.ContactStepUi;
import com.gwt.wizard.client.steps.ui.ContactStepUi.ErrorMsg;
import com.gwt.wizard.shared.model.BookingInfo;

public class ContactStep implements WizardStep
{
    private static DateTimeFormat sdf = DateTimeFormat.getFormat("dd.MM.yyyy");

    private final ContactStepUi ui;
    private final BookingInfo bookingInfo;

    public ContactStep(BookingInfo bookingInfo)
    {
        ui = new ContactStepUi();
        this.bookingInfo = bookingInfo;
    }

    @Override
    public String getCaption()
    {
        return MESSAGES.secondPage();
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isEmailValid(String email)
    {
        boolean result = false;
        if (email != null)
        {
            result = email.matches(EMAIL_PATTERN);
        }
        return result;
    }

    @Override
    public Boolean onNext()
    {

        ui.setErrorMsg("", ErrorMsg.DATE);
        if (ui.getDate() == null)
        {
            ui.setErrorMsg(MESSAGES.mayNotBeEmptyErrorMsg(), ErrorMsg.DATE);
            return false;
        }
        ui.setErrorMsg("", ErrorMsg.FLIGHTNO);
        if (ui.getFlightNo() == null || ui.getFlightNo().trim().length() == 0)
        {
            ui.setErrorMsg(MESSAGES.mayNotBeEmptyErrorMsg(), ErrorMsg.FLIGHTNO);
            return false;
        }
        ui.setErrorMsg("", ErrorMsg.ARRIVAL);
        if (ui.getLandingTime() == null || ui.getLandingTime().trim().length() == 0)
        {
            ui.setErrorMsg(MESSAGES.mayNotBeEmptyErrorMsg(), ErrorMsg.ARRIVAL);
            return false;
        }
        ui.setErrorMsg("", ErrorMsg.FIRST_NAME);
        if (ui.getFirstName() == null || ui.getFirstName().trim().length() == 0)
        {
            ui.setErrorMsg(MESSAGES.mayNotBeEmptyErrorMsg(), ErrorMsg.FIRST_NAME);
            return false;
        }
        ui.setErrorMsg("", ErrorMsg.LAST_NAME);
        if (ui.getLastName() == null || ui.getLastName().trim().length() == 0)
        {
            ui.setErrorMsg(MESSAGES.mayNotBeEmptyErrorMsg(), ErrorMsg.LAST_NAME);
            return false;
        }
        ui.setErrorMsg("", ErrorMsg.EMAIL);
        if (ui.getEmail() == null || !isEmailValid(ui.getEmail()))
        {
            ui.setErrorMsg(MESSAGES.mustBeValidEmailErrorMsg(), ErrorMsg.EMAIL);
            return false;
        }
        ui.setErrorMsg("", ErrorMsg.EMAIL2);
        if (ui.getEmail2() == null || !isEmailValid(ui.getEmail2()))
        {
            ui.setErrorMsg(MESSAGES.mustBeValidEmailErrorMsg(), ErrorMsg.EMAIL2);
            return false;
        }
        ui.setErrorMsg("", ErrorMsg.EMAIL2);
        if (!ui.getEmail2().equals(ui.getEmail()))
        {
            ui.setErrorMsg(MESSAGES.mustBeEqualEmail(), ErrorMsg.EMAIL2);
            return false;
        }
        ui.setErrorMsg("", ErrorMsg.FLIGHTNO);
        if (ui.getLastName() == null || ui.getLastName().trim().length() == 0)
        {
            ui.setErrorMsg(MESSAGES.mayNotBeEmptyErrorMsg(), ErrorMsg.FLIGHTNO);
            return false;
        }

        bookingInfo.setDate(sdf.format(ui.getDate()));
        bookingInfo.setLandingTime(ui.getLandingTime());
        bookingInfo.setName(ui.getFirstName() + "  " + ui.getLastName());
        bookingInfo.setEmail(ui.getEmail());

        bookingInfo.setFlightNo(ui.getFlightNo());
        bookingInfo.setPax(Integer.parseInt(ui.getPax()));
        bookingInfo.setSurfboards(Integer.parseInt(ui.getSurfboards()));
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

    }
}
