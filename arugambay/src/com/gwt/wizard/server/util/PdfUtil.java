package com.gwt.wizard.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.io.Files;
import com.gwt.wizard.shared.model.BookingInfo;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfUtil
{

    public final static String FORMDATE = "FormDate";
    public final static String ORDERNO = "OrderNo";
    public final static String NAME = "Name";
    public final static String EMAIL = "Email";
    public final static String PAX = "Passengers";
    public final static String SURFBOARDS = "Surfboards";
    public final static String DATE = "Date";
    public final static String FLIGHTNO = "FlightNo";
    public final static String LANDINGTIME = "LandingTime";
    public final static String PAID = "Paid";
    public final static String OTHER1 = "Other1";
    public final static String OTHER2 = "Other2";
    public final static String OTHER3 = "Other3";
    public final static String ARRIVAL = "Arrival";
    public final static String FLIGHT_HOTEL = "Flight_Hotel";

    static final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");
    static final DateTimeFormatter fmtFormDate = DateTimeFormat.forPattern("dd MMM yyyy");

    static final float TABLE_WIDTH = 452;
    static final float TABLE_Y = 500;
    static final float INSET = 72;

    static final String CUSTOMER_FEEDBACK = "Customer Feedback";

    public byte[] generateTaxiOrder(String path, BookingInfo bookingInfo)
    {
        PdfReader reader;
        final FileInputStream fis;
        try
        {
            fis = new FileInputStream(path);
            reader = new PdfReader(IOUtils.toByteArray(fis));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, out);
            PdfContentByte canvas = stamper.getOverContent(1);

            PdfPTable table = createBookingTable(bookingInfo);
            table.writeSelectedRows(0, 2, 0, 11, INSET, TABLE_Y, canvas);

            Font helvetica = new Font(FontFamily.HELVETICA, 20);
            BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);
            Chunk c1 = new Chunk(bookingInfo.getRouteInfo().getKey(""), helvetica);
            Chunk c2 = new Chunk(CUSTOMER_FEEDBACK, helvetica);

            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(c1), INSET, 540, 0);
            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(c2), INSET, 200, 0);

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

    /**
     * Creates our first table
     * 
     * @return our first table
     */
    public PdfPTable createBookingTable(BookingInfo bookingInfo)
    {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(TABLE_WIDTH);
        table.setWidthPercentage(TABLE_WIDTH / 3f);
        try
        {
            table.setWidths(new int[] { 1, 2 });
        }
        catch (DocumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // the cell object
        PdfPCell cell;
        table.addCell(ORDERNO);
        table.addCell(bookingInfo.getOrderRef());
        table.addCell(NAME);
        table.addCell(bookingInfo.getName());
        table.addCell(EMAIL);
        table.addCell(bookingInfo.getEmail());
        table.addCell(PAX);
        table.addCell("" + bookingInfo.getPax());
        table.addCell(SURFBOARDS);
        table.addCell("" + bookingInfo.getSurfboards());
        table.addCell(DATE);
        table.addCell(fmt.print(new DateTime((bookingInfo.getDate()))));
        table.addCell(bookingInfo.getRouteInfo().getPickupType().getLocationType());
        table.addCell(bookingInfo.getFlightNo());
        table.addCell(bookingInfo.getRouteInfo().getPickupType().getTimeType());
        table.addCell(bookingInfo.getLandingTime());
        table.addCell(PAID);
        table.addCell(bookingInfo.getPaidAmt());

        table.addCell("Other requirements");
        table.addCell(bookingInfo.getRequirements());

        return table;
    }
}
