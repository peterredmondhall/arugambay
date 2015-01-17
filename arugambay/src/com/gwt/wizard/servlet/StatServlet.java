package com.gwt.wizard.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gwt.wizard.server.StatManager;
import com.gwt.wizard.shared.model.StatInfo;

public class StatServlet extends HttpServlet
{
    public static final Logger log = Logger.getLogger(StatServlet.class.getName());

    /**
         *
         */
    private static final long serialVersionUID = 1L;

    StatManager statManager = new StatManager();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        String country = req.getHeader("X-AppEngine-Country");
        String info = req.getHeader("X-AppEngine-CityLatLong");

        if (country == null || country.trim().length() == 0)
        {
            country = "XXX";
        }
        log.info("country:" + country);
        log.info("info:" + info);
        final String ip = req.getRemoteAddr();
        StatInfo statInfo = new StatInfo();
        statInfo.setCountry(country);
        statManager.sendStat(statInfo);

    }
}
