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

public class PaymentStepUi extends Composite implements Showable
{

    private static ConfirmationStepUiUiBinder uiBinder = GWT.create(ConfirmationStepUiUiBinder.class);

    interface ConfirmationStepUiUiBinder extends UiBinder<Widget, PaymentStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;
    @UiField
    Label labelConfirmationEmail;

    private final BookingInfo bookingInfo;

    public PaymentStepUi(BookingInfo bookingInfo)
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        this.bookingInfo = bookingInfo;
    }

    @Override
    public void show(boolean visible)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
        labelConfirmationEmail.setText(bookingInfo.getEmail());
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
