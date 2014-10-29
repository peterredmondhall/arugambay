package com.gwt.wizard.client.steps.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.model.BookingInfo;

public class ShareConfirmationStepUi extends Composite
{

    private static ShareConfirmationStepUiUiBinder uiBinder = GWT.create(ShareConfirmationStepUiUiBinder.class);

    interface ShareConfirmationStepUiUiBinder extends UiBinder<Widget, ShareConfirmationStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;
    @UiField
    Label labelConfirmationEmail, label1, label2;

    @UiField
    Label labelName, labelEmail, labelFlightNo, labelLandingTime, labelPax, labelSurfboards;

    public ShareConfirmationStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
    }

    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
        next.setVisible(false);
        prev.setVisible(false);
        cancel.setVisible(false);

        cancel.setText("New Order");
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

    public void setBookingInfo(List<BookingInfo> bookingInfo)
    {
        if (bookingInfo != null)
        {
            if (OrderStatus.PAID.equals(bookingInfo.get(0).getStatus()))
            {
                BookingInfo sharer = bookingInfo.get(1);
                label1.setText("Thank you for sharing your ride.");
                label2.setText("Below are the details of your fellow passenger.");
                labelName.setText(sharer.getName());
                labelEmail.setText(sharer.getEmail());
                labelFlightNo.setText(sharer.getFlightNo());
                labelLandingTime.setText(sharer.getLandingTime());
                labelPax.setText("" + sharer.getPax());
                labelSurfboards.setText("" + sharer.getSurfboards());
            }
            else
            {
                label1.setText("The payment was not successful and no order has been created");
                label2.setText("Please contact arugamsurf@gmail.com if you think this is a problem.");
            }
        }
        else
        {
            label1.setText("An error has occured processing you order. Please contact arugamsurf@gmail.com");
        }

        labelConfirmationEmail.setText(bookingInfo.get(0).getEmail());
    }
}
