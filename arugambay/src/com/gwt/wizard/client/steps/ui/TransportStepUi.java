package com.gwt.wizard.client.steps.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Showable;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;

public class TransportStepUi extends Composite implements Showable
{
    public static final String WIDTH = "120px";

    public enum PassengerDetail
    {
        TOT_PASS,
        PASS_WITH_ROLLATOR,
        PASS_WITH_FOLDABLE,
        PASS_REQ_WHEELCHAIR
    }

    private static RouteStepUiUiBinder uiBinder = GWT.create(RouteStepUiUiBinder.class);
    private final BookingServiceAsync bookingService = GWT.create(BookingService.class);

    interface RouteStepUiUiBinder extends UiBinder<Widget, TransportStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    Map<PassengerDetail, ListBox> listBoxMap = new HashMap<PassengerDetail, ListBox>();

    public TransportStepUi()
    {

        initWidget(uiBinder.createAndBindUi(this));
    }

    public Integer getPassengerDetail(PassengerDetail passengerDetail)
    {
        String selection = listBoxMap.get(passengerDetail).getItemText(listBoxMap.get(passengerDetail).getSelectedIndex());
        return Integer.parseInt(selection);
    }

    @Override
    public void setVisible(boolean visible)
    {
        mainPanel.setVisible(visible);
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

    @Override
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        next.setVisible(true);
        prev.setEnabled(false);
        cancel.setText("Cancel");

    }

}
