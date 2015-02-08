package com.gwt.wizard.client.dashboard.ui;

import com.google.gwt.i18n.client.NumberFormat;
import com.gwt.wizard.shared.model.RouteInfo;

public class Helper
{
    private static NumberFormat usdFormat = NumberFormat.getFormat(".00");

    public static String getDollars(RouteInfo routeInfo)
    {
        if (routeInfo.getCents() != null)
        {
            Double d = (double) routeInfo.getCents() / 100;
            return "US$" + usdFormat.format(d);
        }
        return "no price";

    }

}
