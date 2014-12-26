package com.gwt.wizard.client.steps.ui.summary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class SummaryStepMobileUi extends SummaryStepUi
{

    private static SummaryStepUiUiBinder uiBinder = GWT.create(SummaryStepUiUiBinder.class);

    interface SummaryStepUiUiBinder extends UiBinder<Widget, SummaryStepMobileUi>
    {
    }

    @Override
    protected void createUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
