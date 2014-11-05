package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;
import static com.gwt.wizard.client.GwtWizard.SERVICE;
import static com.gwt.wizard.client.core.Wizard.BOOKINGINFO;
import static com.gwt.wizard.client.core.Wizard.ROUTEINFO;

import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.ContactStepUi;
import com.gwt.wizard.client.steps.ui.ContactStepUi.ErrorMsg;
import com.gwt.wizard.shared.model.BookingInfo;

public class ContactStep implements WizardStep
{
    private static final Logger logger = Logger.getLogger(ContactStep.class.getName());

    private final ContactStepUi ui;

    public ContactStep()
    {
        ui = new ContactStepUi();
    }

    @Override
    public String getCaption()
    {
        return "Contact";
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
        if (ui.getArrivalTime() == null || ui.getArrivalTime().trim().length() == 0)
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

        BOOKINGINFO.setDate(ui.getDate());
        BOOKINGINFO.setLandingTime(ui.getArrivalTime());
        BOOKINGINFO.setName(ui.getFirstName() + "  " + ui.getLastName());
        BOOKINGINFO.setEmail(ui.getEmail());

        BOOKINGINFO.setFlightNo(ui.getFlightNo());
        BOOKINGINFO.setPax(Integer.parseInt(ui.getPax()));
        BOOKINGINFO.setSurfboards(Integer.parseInt(ui.getSurfboards()));
        BOOKINGINFO.setShareWanted(ui.getWantToShare());
        BOOKINGINFO.setRequirements(ui.getRequirements());

        BOOKINGINFO.setRouteInfo(ROUTEINFO);
        SERVICE.addBooking(BOOKINGINFO, new AsyncCallback<BookingInfo>()
        {
            @Override
            public void onSuccess(BookingInfo result)
            {
                BOOKINGINFO.setId(result.getId());
            }

            @Override
            public void onFailure(Throwable caught)
            {
                logger.severe("couldnt add booking");
            }
        });

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
