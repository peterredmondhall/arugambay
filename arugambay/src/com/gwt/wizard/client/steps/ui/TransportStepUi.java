package com.gwt.wizard.client.steps.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class TransportStepUi extends Composite
{
    public static final String WIDTH = "120px";

    private static RouteStepUiUiBinder uiBinder = GWT.create(RouteStepUiUiBinder.class);

    interface RouteStepUiUiBinder extends UiBinder<Widget, TransportStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    public TransportStepUi()
    {

        initWidget(uiBinder.createAndBindUi(this));
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

    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        next.setVisible(true);
        prev.setEnabled(false);
        cancel.setText("Cancel");

    }

}
