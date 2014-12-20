package com.gwt.wizard.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gwt.wizard.server.AgentManager;
import com.gwt.wizard.server.BookingServiceManager;
import com.gwt.wizard.server.ContractorManager;
import com.gwt.wizard.server.ImageManager;
import com.gwt.wizard.server.RouteServiceManager;
import com.gwt.wizard.server.entity.Agent;
import com.gwt.wizard.server.entity.ArugamImage;
import com.gwt.wizard.server.entity.Booking;
import com.gwt.wizard.server.entity.Contractor;
import com.gwt.wizard.server.entity.Route;

public class DumpServlet extends HttpServlet
{
    public static final Logger log = Logger.getLogger(DumpServlet.class.getName());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    RouteServiceManager routeServiceManager = new RouteServiceManager();
    ImageManager imageManager = new ImageManager();
    BookingServiceManager bookingServiceManager = new BookingServiceManager();

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        if (!bookingServiceManager.getMaintenceAllowed())
        {
            log.info("not allowed");
            return;
        }
        String bookings = bookingServiceManager.dump(Booking.class);
        String routes = routeServiceManager.dump(Route.class);
        String images = imageManager.dump(ArugamImage.class);
        String contractors = new ContractorManager().dump(Contractor.class);
        String agents = new AgentManager().dump(Agent.class);
        String content = bookings + images + routes + contractors + agents;
        byte[] bytes = null;
        try
        {
            bytes = content.getBytes();

            resp.setContentType("application/txt");
            resp.addHeader("Content-Disposition", "inline; filename=\"dataset.txt\"");
            resp.setContentLength(bytes.length);

            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            OutputStream out = resp.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];

            int count = 0;
            while ((count = in.read(buf)) >= 0)
            {
                out.write(buf, 0, count);
            }
            in.close();
            out.close();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

    }
}
