package com.gwt.wizard.client.steps.ui;

import static com.google.gwt.user.datepicker.client.CalendarUtil.resetTime;
import static com.gwt.wizard.client.core.Wizard.BOOKINGINFO;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.shared.OrderType;

public class ContactStepUi extends Composite
{
    public enum ErrorMsg
    {
        DATE,
        FIRST_NAME,
        LAST_NAME,
        FLIGHTNO,
        EMAIL,
        EMAIL2,
        ARRIVAL
    };

    private static ContactStepUiUiBinder uiBinder = GWT.create(ContactStepUiUiBinder.class);

    interface ContactStepUiUiBinder extends UiBinder<Widget, ContactStepUi>
    {
    }

    // boolean test = false;
    @UiField
    HTMLPanel mainPanel;

    @UiField
    DateBox dateBox;

    @UiField
    ListBox pax, surfboards;

    @UiField
    Label dateErrorMsg, flightErrorMsg, firstNameErrorMsg, lastNameErrorMsg, emailErrorMsg, email2ErrorMsg, arrivalErrorMsg, labelWanttoShare;

    @UiField
    Label labelSharing, labelBooking, labelFlightLandingTime, labelFlightNo;

    @UiField
    TextBox flightLandingTime, flightNo, firstName, lastName, email, email2;

    @UiField
    CheckBox checkboxWanttoShare;

    @UiField
    TextArea requirementsBox;

    public ContactStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));

        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        for (int i = 1; i < 20; i++)
        {
            pax.addItem("" + i);
            surfboards.addItem("" + i);
        }
        dateBox.setFormat(new DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
        checkboxWanttoShare.setValue(true);

//        if (test)
//        {
//            dateBox.setValue(new Date());
//            flightLandingTime.setValue("12:00");
//            flightNo.setText("MH111");
//            firstName.setText("Peter");
//            lastName.setText("Hall");
//            email.setText("info@taxigang.com");
//            email2.setText("info@taxigang.com");
//
//        }
    }

    public Date getDate()
    {
        Date date = dateBox.getValue();
        resetTime(date);
        return date;
    }

    public String getFirstName()
    {
        return firstName.getValue();
    }

    public String getLastName()
    {
        return lastName.getValue();
    }

    public String getEmail()
    {
        return email.getValue();
    }

    public String getEmail2()
    {
        return email2.getValue();
    }

    public String getArrivalTime()
    {
        return flightLandingTime.getText();
    }

    public String getSurfboards()
    {
        return surfboards.getItemText(surfboards.getSelectedIndex());
    }

    public String getPax()
    {
        return pax.getItemText(pax.getSelectedIndex());
    }

    public String getFlightNo()
    {
        return flightNo.getValue();
    }

    public boolean getWantToShare()
    {
        return checkboxWanttoShare.getValue();
    }

    public String getRequirements()
    {
        String requirements = requirementsBox.getText();
        if (requirements == null)
        {
            requirements = "none";
        }
        return requirements;
    }

    public void setErrorMsg(String msg, ErrorMsg errorMsg)
    {
        switch (errorMsg)
        {
            case DATE:
                dateErrorMsg.setText(msg);
                break;
            case FIRST_NAME:
                firstNameErrorMsg.setText(msg);
                break;
            case LAST_NAME:
                lastNameErrorMsg.setText(msg);
                break;
            case EMAIL:
                emailErrorMsg.setText(msg);
                break;
            case EMAIL2:
                email2ErrorMsg.setText(msg);
                break;
            case FLIGHTNO:
                flightErrorMsg.setText(msg);
                break;
            case ARRIVAL:
                arrivalErrorMsg.setText(msg);
                break;
        }
    }

    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);

        next.setVisible(true);
        prev.setEnabled(true);
        prev.setVisible(true);

        boolean sharing = BOOKINGINFO.getOrderType() == OrderType.SHARE;

        checkboxWanttoShare.setVisible(!sharing);
        labelWanttoShare.setVisible(!sharing);

        labelBooking.setVisible(!sharing);
        labelSharing.setVisible(sharing);
        dateBox.setEnabled(!sharing);
        if (sharing)
        {
            dateBox.setValue(BOOKINGINFO.getDate());
        }
        switch (Wizard.ROUTEINFO.getPickupType())
        {
            case AIRPORT:
                labelFlightNo.setText("Flight no. *");
                labelFlightLandingTime.setText("Landing time *");

                break;
            case HOTEL:
                labelFlightNo.setText("Hotel *");
                labelFlightLandingTime.setText("Pickup time *");

                break;
            default:
                break;

        }
    }

    @Override
    public void setHeight(String height)
    {
        super.setHeight(height);
    }

    @Override
    public void setWidth(String width)
    {
        super.setWidth(width);
    }

}
