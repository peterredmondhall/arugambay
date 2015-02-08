package com.gwt.wizard.server.util;

import java.io.File;

import org.joda.time.DateTime;
import org.junit.Test;

import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.model.BookingInfo;

public class BookingUtilTest
{

    @Test
    public void test()
    {
        BookingInfo bookingInfo = new BookingInfo();

        bookingInfo.setDate(new DateTime().plusMonths(1).toDate());
        bookingInfo.setFlightNo("some flightNo");
        bookingInfo.setLandingTime("landingTime");
        bookingInfo.setName("name");
        bookingInfo.setEmail("email");
        bookingInfo.setRequirements("requirements");
        bookingInfo.setId(1234L);
        String html = BookingUtil.toConfirmationEmailHtml(bookingInfo, new File("war/template/confirmation.html"), new Profil());
        System.out.println(html);
    }

}
