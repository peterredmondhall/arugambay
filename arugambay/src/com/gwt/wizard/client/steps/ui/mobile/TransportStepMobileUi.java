package com.gwt.wizard.client.steps.ui.mobile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class TransportStepMobileUi extends TransportStepUi
{
    private static TransportStepMobileUiBinder uiBinder = GWT.create(TransportStepMobileUiBinder.class);

    interface TransportStepMobileUiBinder extends UiBinder<Widget, TransportStepMobileUi>
    {
    }

    @Override
    protected void createUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
