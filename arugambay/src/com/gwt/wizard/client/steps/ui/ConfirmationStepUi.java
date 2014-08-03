package com.gwt.wizard.client.steps.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Showable;
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

    private final BookingInfo bookingInfo;

    public ConfirmationStepUi(BookingInfo bookingInfo)
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        this.bookingInfo = bookingInfo;
        if (bookingInfo.getRef() != null)
        {
            label1.setText("Thank you for your order. A confirmation email has been sent to the following address:");
            labelConfirmationEmail.setText(bookingInfo.getEmail());
            label2.setText("Please print it out and present it to the driver on arrival.");
        }
        else
        {
            label1.setText("An error has occured processing you order. Please contact arugamsurf@gmail.com");
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
