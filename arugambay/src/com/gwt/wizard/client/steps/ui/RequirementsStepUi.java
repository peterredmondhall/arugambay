package com.gwt.wizard.client.steps.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Showable;

public class RequirementsStepUi extends Composite implements Showable
{

    private static RequirementsStepUiUiBinder uiBinder = GWT.create(RequirementsStepUiUiBinder.class);

    interface RequirementsStepUiUiBinder extends UiBinder<Widget, RequirementsStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;
    @UiField
    TextArea requirementsBox;

    public RequirementsStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        requirementsBox.setSize("200px", "100px");
    }

    public String getRequirements()
    {
        return requirementsBox.getText();
    }

    @Override
    public void show(boolean visible)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
    }

}
