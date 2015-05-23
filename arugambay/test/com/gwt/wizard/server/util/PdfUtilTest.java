package com.gwt.wizard.server.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.junit.Test;

import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;

public class PdfUtilTest
{

    @Test
    public void test() throws IOException
    {
        Path file = Paths.get("xx.pdf");
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setId(123456789L);
        bookingInfo.setDate(new Date());
        bookingInfo.setName("Steven Moore");
        bookingInfo.setFlightNo("FL200");
        bookingInfo.setLandingTime("10:00");
        bookingInfo.setPax(22);
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setPickupType(PickupType.HOTEL);
        routeInfo.setCents(100L);
        bookingInfo.setRouteInfo(routeInfo);
        bookingInfo.setSurfboards(23);
        bookingInfo.setEmail("email@taxisurf.com");
        bookingInfo.setRequirements("A Path instance contains the information used to specify the location of a file or directory. At the time it is defined, a Path is provided with a series of one or more names. A root element or a file name might be included, but neither are required. A Path might consist of just a single directory or file name.");

        byte[] buf = new PdfUtil().generateTaxiOrder("war/template/order_blank.pdf", bookingInfo, null, null);
        Files.write(file, buf);

    }
}
