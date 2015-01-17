package com.gwt.wizard.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gwt.wizard.server.BookingServiceImpl;
import com.gwt.wizard.server.StatManager;

public class RatingServlet extends HttpServlet
{
    public static final Logger log = Logger.getLogger(RatingServlet.class.getName());
    private static final long serialVersionUID = 1L;
    BookingServiceImpl bookingServiceManager = new BookingServiceImpl();
    StatManager statManager = new StatManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        log.info("rating");
        bookingServiceManager.sendRatingRequest();
        statManager.report();

    }
}
