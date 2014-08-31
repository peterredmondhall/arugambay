package com.gwt.wizard.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gwt.wizard.shared.model.StatInfo;

public class StatServlet extends HttpServlet
{
    /**
         *
         */
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        String country = req.getHeader("X-AppEngine-Country");
        final String ip = req.getRemoteAddr();
        StatInfo statInfo = new StatInfo();
        statInfo.setCountry(country);
    }
}
