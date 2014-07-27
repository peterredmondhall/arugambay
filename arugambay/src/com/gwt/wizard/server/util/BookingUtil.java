package com.gwt.wizard.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.labs.repackaged.com.google.common.base.Pair;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;

public class BookingUtil
{

    private static final String FORWARD_PICKUP = "Hinfahrt von:";
    private static final String FORWARD_TIME = "Hinfahrt um:";
    private static final String RETURN_PICKUP = "Rückfahrt von:";
    private static final String RETURN_TIME = "Rückfahrt um:";

    public static String toEmailText(BookingInfo bookingInfo)
    {
        List<Pair<String, String>> list = toPairList(bookingInfo);
        return toEmailTextFromList(list);

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<Pair<String, String>> toPairList(BookingInfo bookingInfo)
    {
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        list.add(new Pair("Veranstaltung am:", bookingInfo.getDate()));
//        list.add(new Pair(FORWARD_PICKUP, bookingInfo.getForwardPickupPlace()));
//        list.add(new Pair(FORWARD_TIME, bookingInfo.getForwardPickupTime()));
//        list.add(new Pair(RETURN_PICKUP, bookingInfo.getReturnPickupPlace()));
//        list.add(new Pair(RETURN_TIME, bookingInfo.getReturnPickupTime()));
//        list.add(new Pair("Anzahl Passagiere:", Integer.toString(bookingInfo.getPax())));
//        list.add(new Pair("Passagiere mit Rollatoren:", Integer.toString(bookingInfo.getPaxRollatoren())));
//        list.add(new Pair("Passagiere mit klappbaren Rollstuhl:", Integer.toString(bookingInfo.getPaxFoldableWheelchair())));
//        list.add(new Pair("Passagiere Rollstuhltransport:", Integer.toString(bookingInfo.getPaxRollstuhl())));
//        list.add(new Pair("Begleiter - Name:", bookingInfo.getCompanionName()));
//        list.add(new Pair("Begleiter - Email:", bookingInfo.getCompanionEmail()));
//        list.add(new Pair("Organisator - Name:", bookingInfo.getOrganizerName()));
//        list.add(new Pair("Organisator - Email:", bookingInfo.getOrganizerEmail()));
        // list.add(new Pair("Andere Details:", bookingInfo.getRequirements()));

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

    public static BookingInfo getBookingInfo(Booking booking, Map<Long, PlaceInfo> places)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
