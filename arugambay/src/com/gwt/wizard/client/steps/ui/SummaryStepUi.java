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
import com.gwt.wizard.shared.model.BookingInfo;

public class SummaryStepUi extends Composite implements Showable
{
    private final BookingInfo bookingInfo;

    private static SummaryStepUiUiBinder uiBinder = GWT.create(SummaryStepUiUiBinder.class);

    interface SummaryStepUiUiBinder extends UiBinder<Widget, SummaryStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    @UiField
    Label labelEmail, labelName, labelSurfboards, labelPax, labelFlightNo, labelLandingTime, labelDate, labelPrice, labelRequirements;

    @UiField
    Paypal paypal;

    public SummaryStepUi(BookingInfo bookingInfo)
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        this.bookingInfo = bookingInfo;
        paypal.setUrl();
    }

    @Override
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);

        labelDate.setText(bookingInfo.getDate());
        labelFlightNo.setText(bookingInfo.getFlightNo());
        labelLandingTime.setText(bookingInfo.getLandingTime());

        labelPax.setText(Integer.toString(bookingInfo.getPax()));
        labelSurfboards.setText(Integer.toString(bookingInfo.getSurfboards()));
        labelEmail.setText(bookingInfo.getEmail());
        labelName.setText(bookingInfo.getName());
        labelRequirements.setText(bookingInfo.getRequirements());
        labelPrice.setText("US $160");

        prev.setEnabled(true);
        next.setVisible(false);
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
