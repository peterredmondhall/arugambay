package com.gwt.wizard.client.steps.ui;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class Paypal extends Composite
{

    private static PaypalBinder uiBinder = GWT.create(PaypalBinder.class);

    interface PaypalBinder extends UiBinder<Widget, Paypal>
    {
    }

    @UiField
    HTMLPanel paypalPanel;

    public Paypal()
    {

        initWidget(uiBinder.createAndBindUi(this));
        System.out.println("host" + Window.Location.getHost());
        System.out.println("path" + Window.Location.getPath());
        System.out.println("path" + Window.Location.getPort());
        System.out.println("path" + Window.Location.getParameterMap());
        String params = "";
        Map<String, List<String>> map = Window.Location.getParameterMap();
        for (String key : map.keySet())
        {
            if (params.length() == 0)
            {
                params = "?" + params;
            }
            else
            {
                params = "&" + params;
            }
            params += (key + "=\"" + map.get(key) + "\"");
        }
        String returnPath = Window.Location.getHost() + ":" + Window.Location.getPath() + params;
        paypalPanel.getElementById("hiddenReturn").setAttribute("value", returnPath);
        paypalPanel.getElementById("hiddenCancelReturn").setAttribute("value", returnPath);

//        paypalPanel.setAction("https://www.sandbox.paypal.com/cgi-bin/webscr");
//        paypalPanel.setMethod(FormPanel.METHOD_POST);
//        paypalPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
//        // Create a panel to hold all of the form widgets.
//        VerticalPanel panel = new VerticalPanel();
//        paypalPanel.setWidget(panel);
//
//        Image image = new Image();
//        image.addClickHandler(new ClickHandler()
//        {
//            @Override
//            public void onClick(ClickEvent event)
//            {
//                System.out.println("on click");
//                try
//                {
//                    paypalPanel.submit();
//                }
//                catch (Exception ex)
//                {
//                    System.out.println("problem");
//                }
//            }
//        });
//        image.setUrl("https://www.paypal.com/en_US/i/btn/btn_buynow_LG.gif");
//        panel.add(image);
//        for (String[] h : hiddenList)
//        {
//            Hidden hidden = new Hidden(h[0], h[1]);
//            hidden.setName(h[0]);
//            hidden.setDefaultValue(h[1]);
//            panel.add(hidden);
//        }
//        paypalForm.addSubmitCompleteHandler(new SubmitCompleteHandler()
//        {
//
//            @Override
//            public void onSubmitComplete(SubmitCompleteEvent event)
//            {
//                System.out.println("complete");
//                // TODO Auto-generated method stub
//
//            }
//        });
//
    }

    @Override
    public void setVisible(boolean visible)
    {
        // buttonPanel.setVisible(visible);
    }

}
