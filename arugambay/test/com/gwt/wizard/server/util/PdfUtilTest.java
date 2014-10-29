package com.gwt.wizard.server.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.junit.Test;

import com.gwt.wizard.shared.model.BookingInfo;

public class PdfUtilTest
{

    @Test
    public void test() throws IOException
    {
        Path file = Paths.get("xx.pdf");
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setId(1234L);
        bookingInfo.setDate(new Date());
        bookingInfo.setName("Steven Moore");
        bookingInfo.setFlightNo("FL200");
        bookingInfo.setLandingTime("10:00");
        bookingInfo.setPax(22);
        bookingInfo.setSurfboards(23);
        bookingInfo.setEmail("email@taxisurf.com");
        bookingInfo.setRequirements("A Path instance contains the information used to specify the location of a file or directory. At the time it is defined, a Path is provided with a series of one or more names. A root element or a file name might be included, but neither are required. A Path might consist of just a single directory or file name.");

        byte[] buf = PdfUtil.generateTaxiOrder("war/template/order.pdf", bookingInfo);
        Files.write(file, buf);

    }
}
