package com.gwt.wizard.client.steps.ui.summary;

import static com.gwt.wizard.client.core.Wizard.BOOKINGINFO;

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
import com.gwt.wizard.client.dashboard.ui.Helper;
import com.gwt.wizard.shared.OrderType;

public class SummaryStepUi extends Composite
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

//    @UiField
//    Paypal paypal;

    @UiField
    Label pay1;

    public SummaryStepUi()
    {
        createUi();
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        // paypal.setUrl();
        // stripe1.setVisible(true);
    }

    protected void createUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void show(boolean visible, Button prev, Button next)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);

        labelDate.setText(sdf.format(BOOKINGINFO.getDate()));
        labelFlightNo.setText(BOOKINGINFO.getFlightNo());
        labelLandingTime.setText(BOOKINGINFO.getLandingTime());

        labelPax.setText(Integer.toString(BOOKINGINFO.getPax()));
        labelSurfboards.setText(Integer.toString(BOOKINGINFO.getSurfboards()));
        labelEmail.setText(BOOKINGINFO.getEmail());
        labelName.setText(BOOKINGINFO.getName());
        labelRequirements.setText(BOOKINGINFO.getRequirements());
        labelPrice.setText(Helper.getDollars(BOOKINGINFO.getRouteInfo()));
        labelInterestedSharing.setText(BOOKINGINFO.getShareWanted() ? "yes please" : "no, thanks");
        prev.setEnabled(true);

        boolean shared = BOOKINGINFO.getOrderType() == OrderType.SHARE;

        pay1.setVisible(!shared);
        // pay2.setVisible(!shared);
        labelPrice.setVisible(!shared);
        // paypal.setVisible(!shared);
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
