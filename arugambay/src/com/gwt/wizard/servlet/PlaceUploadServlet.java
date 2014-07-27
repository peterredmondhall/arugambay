package com.gwt.wizard.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class PlaceUploadServlet extends HttpServlet
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Set response content type
        response.setContentType("text/html");
        String place = request.getParameter("place");
        if (place != null && place.length() > 0)
        {
            String[] places = place.split(",");
            for (String string : places)
            {
                try
                {
                    Entity e = new Entity("places-data");
                    e.setProperty("place", string);
                    DatastoreServiceFactory.getDatastoreService().put(e);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            response.getWriter().write("Places: " + place + " is saved!!");
        }
    }
}
