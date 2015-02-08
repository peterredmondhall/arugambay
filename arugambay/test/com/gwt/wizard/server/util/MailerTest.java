package com.gwt.wizard.server.util;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.joda.time.DateTime;
import org.junit.Test;

import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.PickupType;

public class MailerTest
{
    @Test
    public void testFeedbackRequest() throws Exception
    {
        BookingInfo bookingInfo = new BookingInfo();

        bookingInfo.setDate(new DateTime().plusMonths(1).toDate());
        bookingInfo.setFlightNo("some flightNo");
        bookingInfo.setLandingTime("landingTime");
        bookingInfo.setName("peter name");
        bookingInfo.setEmail("email");
        bookingInfo.setRequirements("requirements");
        bookingInfo.setId(1234L);
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setPickupType(PickupType.AIRPORT);
        bookingInfo.setRouteInfo(routeInfo);

        Profil profil = new Profil();
        profil.setTaxisurfUrl("http://taxigangsurf.appspot.com");

        Mailer.templateMap.put(Mailer.FEEDBACK_REQUEST, new File("war/template/feedbackRequest.html"));
        String html = Mailer.setFeedbackRequest(bookingInfo, profil);
        assertEquals(true, html.contains("http://taxigangsurf.appspot.com?review=1234"));
        assertEquals(true, html.contains("peter name"));
    }
}
