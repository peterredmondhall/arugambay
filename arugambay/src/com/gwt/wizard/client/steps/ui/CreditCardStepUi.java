package com.gwt.wizard.client.steps.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Showable;

public class CreditCardStepUi extends Composite implements Showable
{
    public enum ErrorMsg
    {
        NAME,
        CVC
    };

    private static ContactStepUiUiBinder uiBinder = GWT.create(ContactStepUiUiBinder.class);

    interface ContactStepUiUiBinder extends UiBinder<Widget, CreditCardStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    @UiField
    TextBox ccName, ccCvc;

    @UiField
    ListBox ccExpiryMonth, ccExpiryYear;

    @UiField
    Label nameErrorMsg, cvcErrorMsg;

    public CreditCardStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));

        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        for (int i = 1; i < 12; i++)
        {
            ccExpiryMonth.addItem("" + i);
        }
        for (int i = 2014; i < 2025; i++)
        {
            ccExpiryYear.addItem("" + i);
        }
    }

    public String getCCName()
    {
        return ccName.getValue();
    }

    public String getCvc()
    {
        return ccCvc.getValue();
    }

    public Integer getCCExpiryMonth()
    {
        return Integer.parseInt(ccExpiryMonth.getValue(ccExpiryMonth.getSelectedIndex()));
    }

    public Integer getCCExpiryYear()
    {
        return Integer.parseInt(ccExpiryYear.getValue(ccExpiryYear.getSelectedIndex()));
    }

    public void setErrorMsg(String msg, ErrorMsg errorMsg)
    {
        switch (errorMsg)
        {
            case NAME:
                nameErrorMsg.setText(msg);
                break;
            case CVC:
                cvcErrorMsg.setText(msg);
                break;
        }
    }

    @Override
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);

        next.setVisible(true);
        prev.setEnabled(true);
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
