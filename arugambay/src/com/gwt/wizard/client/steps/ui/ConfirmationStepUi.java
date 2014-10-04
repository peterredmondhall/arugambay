package com.gwt.wizard.client.steps.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Showable;
import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.BookingInfo;

public class ConfirmationStepUi extends Composite implements Showable
{

    private static ConfirmationStepUiUiBinder uiBinder = GWT.create(ConfirmationStepUiUiBinder.class);

    interface ConfirmationStepUiUiBinder extends UiBinder<Widget, ConfirmationStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;
    @UiField
    Label labelConfirmationEmail, label1, label2;

    public ConfirmationStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
    }

    @Override
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

    public void setBookingInfo(BookingInfo bookingInfo)
    {

        if (bookingInfo != null)
        {
            if (OrderType.SHARE.equals(bookingInfo.getOrderType()))
            {
                label1.setText("Thank you for your share request");
                label2.setText("A message has been sent to the person who booked the taxi.");

            }
            else
            {
                if (OrderStatus.PAID.equals(bookingInfo.getStatus()))
                {
                    label1.setText("Thank you for your order. A confirmation email has been sent to the following address:");
                    label2.setText("Please print it out and present it to the driver on arrival.");
                }
                else
                {
                    label1.setText("The payment was not successful and no order has been created");
                    label2.setText("Please contact arugamsurf@gmail.com if you think this is a problem.");
                }
            }
        }
        else
        {
            label1.setText("An error has occured processing you order. Please contact arugamsurf@gmail.com");
        }

        labelConfirmationEmail.setText(bookingInfo.getEmail());
    }
}
