package com.gwt.wizard.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.gwt.wizard.shared.model.BookingInfo;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfUtil
{

    public final static String FORMDATE = "FormDate";
    public final static String ORDERNO = "OrderNo";
    public final static String NAME = "Name";
    public final static String EMAIL = "Email";
    public final static String PAX = "Pax";
    public final static String SURFBOARDS = "Surfboards";
    public final static String DATE = "Date";
    public final static String FLIGHTNO = "FlightNo";
    public final static String LANDINGTIME = "LandingTime";
    public final static String PAID = "Paid";
    public final static String OTHER1 = "Other1";
    public final static String OTHER2 = "Other2";
    public final static String OTHER3 = "Other3";

    static final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");
    static final DateTimeFormatter fmtFormDate = DateTimeFormat.forPattern("dd MMM yyyy");

    public static byte[] generateTaxiOrder(String path, BookingInfo bookingInfo)
    {
        PdfReader reader;
        final FileInputStream fis;
        try
        {
            fis = new FileInputStream(path);
            reader = new PdfReader(IOUtils.toByteArray(fis));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, out);
            AcroFields form = stamper.getAcroFields();
            form.setField(FORMDATE, fmtFormDate.print(new DateTime(new Date())));
            form.setField(ORDERNO, bookingInfo.getOrderNo());
            form.setField(DATE, fmt.print(new DateTime((bookingInfo.getDate()))));
            form.setField(EMAIL, bookingInfo.getEmail());
            form.setField(NAME, bookingInfo.getName());
            form.setField(PAX, "" + bookingInfo.getPax());
            form.setField(SURFBOARDS, "" + bookingInfo.getSurfboards());
            form.setField(FLIGHTNO, bookingInfo.getFlightNo());
            form.setField(LANDINGTIME, bookingInfo.getLandingTime());
            form.setField(PAID, bookingInfo.getPaidAmt());

            List<String> chunks = Lists.newArrayList(Splitter.fixedLength(80).split(bookingInfo.getRequirements()));
            form.setField(OTHER1, chunks.size() > 0 ? chunks.get(0) : "");
            form.setField(OTHER2, chunks.size() > 1 ? chunks.get(1) : "");
            form.setField(OTHER3, chunks.size() > 2 ? chunks.get(2) : "");
            stamper.close();
            reader.close();
            fis.close();

            return out.toByteArray();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (DocumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] get()
    {
        File f = new File("template/test.pdf");

        try
        {
            return Files.toByteArray(f);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
