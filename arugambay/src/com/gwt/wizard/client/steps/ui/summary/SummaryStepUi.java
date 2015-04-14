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
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.dashboard.ui.Helper;

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
    Label summaryTitle, summaryDetails, labelEmail, labelName, labelSurfboards, labelPax, labelFlightNo, labelLandingTime, labelDate, labelPrice, labelRequirements, labelInterestedSharing, labelRPT;

    @UiField
    Label labelPickupDetail, labelPickupTimeDetail;

    @UiField
    Label labelInterestedSharingField, labelRequirementsField;
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

        labelPickupDetail.setText(Wizard.ROUTEINFO.getPickupType().getLocationType());
        labelPickupTimeDetail.setText(Wizard.ROUTEINFO.getPickupType().getTimeType());

        labelPax.setText(Integer.toString(BOOKINGINFO.getPax()));
        labelSurfboards.setText(Integer.toString(BOOKINGINFO.getSurfboards()));
        labelEmail.setText(BOOKINGINFO.getEmail());
        labelName.setText(BOOKINGINFO.getName());
        labelRequirements.setText(BOOKINGINFO.getRequirements());
        labelPrice.setText(Helper.getDollars(BOOKINGINFO.getRouteInfo()));
        labelInterestedSharing.setText(BOOKINGINFO.getShareWanted() ? "yes please" : "no, thanks");
        prev.setEnabled(true);

        labelInterestedSharing.setVisible(true);
        labelInterestedSharingField.setVisible(true);
        labelRequirementsField.setText("Message");
        pay1.setVisible(false);
        labelPrice.setVisible(false);

        labelRPT.setVisible(true);
        labelSurfboards.setVisible(true);
        switch (BOOKINGINFO.getOrderType())
        {
            case BOOKING:
                summaryTitle.setText("Summary of your order.");
                summaryDetails.setText("");
                pay1.setVisible(true);
                labelPrice.setVisible(true);
                break;
            case SHARE:
                summaryTitle.setText("Summary of your share request.");
                summaryDetails.setText("These details will be sent to the person who booked the taxi and they will contact you directly. This is *NOT* a taxi booking.");
                labelInterestedSharing.setVisible(false);
                labelInterestedSharingField.setVisible(false);
                labelRequirementsField.setText("Message");
                break;
            case SHARE_ANNOUNCEMENT:
                summaryTitle.setText("Summary of your share announcement.");
                summaryDetails.setText("Your share announcement will be listed. This is *NOT* a taxi booking.");
                labelInterestedSharing.setVisible(false);
                labelInterestedSharingField.setVisible(false);
                labelRequirementsField.setText("Message");
                labelRPT.setVisible(false);
                labelSurfboards.setVisible(false);
                break;
            default:
                break;

        }

        // pay2.setVisible(!shared);
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
