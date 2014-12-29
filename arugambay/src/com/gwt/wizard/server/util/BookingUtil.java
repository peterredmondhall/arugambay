package com.gwt.wizard.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.appengine.labs.repackaged.com.google.common.base.Pair;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;

public class BookingUtil
{
    // FIXME
    private static final String DATE = "Date:";
    private static final String FLIGHTNO = "Flight No:";
    private static final String HOTEL = "Hotel:";
    private static final String LANDING_TIME = "Landing Time:";
    private static final String PICKUP_TIME = "Pickup Time:";
    private static final String NAME = "Name:";
    private static final String EMAIL = "Email:";
    private static final String NUM_PAX = "Passengers:";
    private static final String NUM_SURFBOARDS = "Surfboards:";
    private static final String REQS = "Other requirements:";
    private static DateTimeFormatter sdf = DateTimeFormat.forPattern("dd.MM.yyyy");

    public static String toEmailText(BookingInfo bookingInfo)
    {
        List<Pair<String, String>> list = toPairList(bookingInfo);
        return toEmailTextFromList(list);

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<Pair<String, String>> toPairList(BookingInfo bookingInfo)
    {
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        list.add(new Pair(DATE, sdf.print(new DateTime(bookingInfo.getDate()))));
        PickupType pickupType = bookingInfo.getRouteInfo().getPickupType();
        list.add(new Pair(pickupType.getLocationType(), bookingInfo.getFlightNo()));
        list.add(new Pair(pickupType.getTimeType(), bookingInfo.getLandingTime()));
        list.add(new Pair(NAME, bookingInfo.getName()));
        list.add(new Pair(EMAIL, bookingInfo.getEmail()));
        list.add(new Pair(NUM_SURFBOARDS, Integer.toString(bookingInfo.getSurfboards())));
        list.add(new Pair(NUM_PAX, Integer.toString(bookingInfo.getPax())));
        list.add(new Pair(REQS, bookingInfo.getRequirements()));

        return list;
    }

    public static String toEmailTextFromList(List<Pair<String, String>> list)
    {
        StringBuffer sb = new StringBuffer();
        for (Pair<String, String> pair : list)
        {
            sb.append(pair.first + "\t" + pair.second + "\r");
        }

        String msg = sb.toString();
        return msg;
    }

    public static String toConfirmationRequestHtml(BookingInfo bookingInfo, File file, Profil profil)
    {
        String html = getTemplate(file);
        html = toConfirmationEmailHtml(bookingInfo, html);
        html = html.replace("AGREE_SHARE_LINK", profil.getTaxisurfUrl() + "?share=" + bookingInfo.getId());
        return html;
    }

    public static String toConfirmationEmailHtml(BookingInfo bookingInfo, File file)
    {
        String html = getTemplate(file);
        return toConfirmationEmailHtml(bookingInfo, html);
    }

    public static String toConfirmationEmailHtml(BookingInfo bookingInfo, String html)
    {
        String insertion = "";
        for (Pair<String, String> pair : toPairList(bookingInfo))
        {
            insertion += "<tr>\n";
            insertion += "<td colspan=\"2\" class=\"content\">XXX</td>\n".replace("XXX", pair.first);
            insertion += "<td colspan=\"2\" class=\"content\">XXX</td>\n".replace("XXX", pair.second);
            insertion += "</tr>\n";
        }

        return html.replace("<!-- INSERTION -->", insertion);
    }

    public static String getTemplate(File file)
    {

        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String ls = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
        return stringBuilder.toString();

    }

}
