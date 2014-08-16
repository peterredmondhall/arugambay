package com.gwt.wizard.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.base.Pair;
import com.gwt.wizard.shared.model.BookingInfo;

public class BookingUtil
{

    private static final String DATE = "Date:";
    private static final String FLIGHTNO = "Flight No:";
    private static final String LANDING_TIME = "Landing Time:";
    private static final String NAME = "Name:";
    private static final String EMAIL = "Email:";
    private static final String NUM_PAX = "Passengers:";
    private static final String NUM_SURFBOARDS = "Surfboards:";
    private static final String REQS = "Other requirements:";

    public static String toEmailText(BookingInfo bookingInfo)
    {
        List<Pair<String, String>> list = toPairList(bookingInfo);
        return toEmailTextFromList(list);

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<Pair<String, String>> toPairList(BookingInfo bookingInfo)
    {
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        list.add(new Pair(DATE, bookingInfo.getDate()));
        list.add(new Pair(FLIGHTNO, bookingInfo.getFlightNo()));
        list.add(new Pair(LANDING_TIME, bookingInfo.getLandingTime()));
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
        System.out.println(msg);
        return msg;
    }

    public static String toEmailHtml(BookingInfo bookingInfo, File file)
    {
        String html = readFile(file);
        return toEmailHtml(bookingInfo, html);
    }

    public static String toEmailHtml(BookingInfo bookingInfo, String html)
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

    public static String readFile(File file)
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