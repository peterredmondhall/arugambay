package com.gwt.wizard.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.common.io.ByteStreams;
import com.gwt.wizard.server.AgentManager;
import com.gwt.wizard.server.BookingServiceManager;
import com.gwt.wizard.server.ContractorManager;
import com.gwt.wizard.server.ImageManager;
import com.gwt.wizard.server.RouteServiceManager;
import com.gwt.wizard.server.entity.Booking;

public class DatasetUploadServlet extends HttpServlet
{
    public static final Logger log = Logger.getLogger(LoginServlet.class.getName());

    private static final long serialVersionUID = 1L;
    ImageManager imageManager = new ImageManager();
    BookingServiceManager bookingServiceManager = new BookingServiceManager();
    RouteServiceManager routeServiceManager = new RouteServiceManager();
    ContractorManager contractorManager = new ContractorManager();
    AgentManager agentManager = new AgentManager();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        try
        {
            ServletFileUpload upload = new ServletFileUpload();
            res.setContentType("text/plain");

            FileItemIterator iterator = upload.getItemIterator(req);
            while (iterator.hasNext())
            {
                FileItemStream item = iterator.next();
                InputStream stream = item.openStream();

                if (item.isFormField())
                {
                    log.warning("Got a form field: " + item.getFieldName());
                }
                else
                {
                    log.warning("Got an uploaded file: " + item.getFieldName() +
                            ", name = " + item.getName());

                    String dataset = new String(ByteStreams.toByteArray(stream));
                    String[] data = dataset.split("<list>");
                    String bookings = data[1];
                    String images = data[2];
                    String routes = data[3];
                    String contractors = data[4];
                    String agents = data[5];

                    bookingServiceManager.deleteAll(Booking.class);
                }
            }
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
    }
}