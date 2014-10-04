package com.gwt.wizard.client.steps.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Showable;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.shared.OrderType;

public class SummaryStepUi extends Composite implements Showable
{
    private static SummaryStepUiUiBinder uiBinder = GWT.create(SummaryStepUiUiBinder.class);
    private static DateTimeFormat sdf = DateTimeFormat.getFormat("dd.MM.yyyy");

    interface SummaryStepUiUiBinder extends UiBinder<Widget, SummaryStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    @UiField
    Label labelEmail, labelName, labelSurfboards, labelPax, labelFlightNo, labelLandingTime, labelDate, labelPrice, labelRequirements, labelInterestedSharing;

    @UiField
    Paypal paypal;

    @UiField
    Label pay1, pay2;

    public SummaryStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        paypal.setUrl();
        // stripe1.setVisible(true);
    }

    @Override
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);

        labelDate.setText(sdf.format(Wizard.bookingInfo.getDate()));
        labelFlightNo.setText(Wizard.bookingInfo.getFlightNo());
        labelLandingTime.setText(Wizard.bookingInfo.getLandingTime());

        labelPax.setText(Integer.toString(Wizard.bookingInfo.getPax()));
        labelSurfboards.setText(Integer.toString(Wizard.bookingInfo.getSurfboards()));
        labelEmail.setText(Wizard.bookingInfo.getEmail());
        labelName.setText(Wizard.bookingInfo.getName());
        labelRequirements.setText(Wizard.bookingInfo.getRequirements());
        labelPrice.setText("US $160");
        labelInterestedSharing.setText(Wizard.bookingInfo.getShareWanted() ? "yes please" : "no, thanks");
        prev.setEnabled(true);

        boolean shared = Wizard.bookingInfo.getOrderType() == OrderType.SHARE;

        pay1.setVisible(!shared);
        pay2.setVisible(!shared);
        labelPrice.setVisible(!shared);
        paypal.setVisible(!shared);
        next.setVisible(true);
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
