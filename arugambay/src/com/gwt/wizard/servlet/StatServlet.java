package com.gwt.wizard.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.com.google.common.collect.ImmutableMap;
import com.gwt.wizard.server.StatManager;
import com.gwt.wizard.shared.Currency;
import com.gwt.wizard.shared.model.StatInfo;

public class StatServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    public static final Logger log = Logger.getLogger(StatServlet.class.getName());
    private static final Map<String, Currency> currencyMap = ImmutableMap.of(
            "GB", Currency.GBP,
            "CZ", Currency.EUR,
            "AU", Currency.AUD,
            "DE", Currency.EUR,
            "FR", Currency.EUR
            );

    StatManager statManager = new StatManager();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        String country = req.getHeader("X-AppEngine-Country");
        String cityLatLong = req.getHeader("X-AppEngine-CityLatLong");
        String region = req.getHeader("X-AppEngine-Region");
        String city = req.getHeader("X-AppEngine-City");
        String src = req.getParameter("src");
        Long session = Long.parseLong(req.getParameter("session"));
        if (country == null || country.trim().length() == 0)
        {
            country = "XXX";
        }
        log.info("country:" + country);
        log.info("cityLatLong:" + cityLatLong);
        log.info("region:" + region);
        log.info("city:" + city);
        final String ip = req.getRemoteAddr();
        StatInfo statInfo = new StatInfo();
        statInfo.setDetail("country");
        statInfo.setSrc(src);
        statInfo.setCountry(country);
        statInfo.setIdent(session);
        statManager.createSessionStat(statInfo);

        PrintWriter writer = resp.getWriter();
        String currency = currencyMap.get(country) != null ? currencyMap.get(country).name() : "USD";
        log.info("currency:" + currency);
        writer.print(currency);
        writer.close();

    }
}
