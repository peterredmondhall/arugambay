package com.gwt.wizard.client.steps.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.gwt.wizard.client.core.Showable;

public class ContactStepUi extends Composite implements Showable
{
    public enum ErrorMsg
    {
        FIRST_NAME,
        LAST_NAME,
        FLIGHTNO,
        EMAIL
    };

    private static ContactStepUiUiBinder uiBinder = GWT.create(ContactStepUiUiBinder.class);

    interface ContactStepUiUiBinder extends UiBinder<Widget, ContactStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    @UiField
    DateBox dateBox;

    @UiField
    ListBox pax, surfboards;

    @UiField
    Label flightErrorMsg, firstNameErrorMsg, lastNameErrorMsg, emailErrorMsg;

    @UiField
    TextBox flightLandingTime, flightNo, firstName, lastName, email;

    public ContactStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        for (int i = 1; i < 20; i++)
        {
            pax.addItem("" + i);
            surfboards.addItem("" + i);
        }
    }

    public DateBox getDateBox()
    {
        return dateBox;
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

    public String getLandingTime()
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

    public void setErrorMsg(String msg, ErrorMsg errorMsg)
    {
        switch (errorMsg)
        {
            case FIRST_NAME:
                firstNameErrorMsg.setText(msg);
                break;
            case LAST_NAME:
                lastNameErrorMsg.setText(msg);
                break;
            case EMAIL:
                emailErrorMsg.setText(msg);
                break;
            case FLIGHTNO:
                flightErrorMsg.setText(msg);
                break;
        }
    }

    @Override
    public void show(boolean visible)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
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
