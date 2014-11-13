package com.gwt.wizard.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gwt.wizard.server.BookingServiceManager;
import com.gwt.wizard.server.util.PdfUtil;
import com.gwt.wizard.shared.model.BookingInfo;

public class PDFRendererServlet extends HttpServlet
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    BookingServiceManager bookingService = new BookingServiceManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String bookingId = req.getParameter("order");
        if (bookingId != null && bookingId.length() > 0)
        {
            byte[] bytes = null;
            try
            {
                BookingInfo bookingInfo = bookingService.getBooking(Long.parseLong(bookingId));
                if (bookingInfo != null)
                {
                    bytes = PdfUtil.generateTaxiOrder("template/order.pdf", bookingInfo);

                    resp.setContentType("application/pdf");
                    resp.addHeader("Content-Disposition", "inline; filename=\"data.pdf\"");
                    resp.setContentLength(bytes.length);

                    ServletOutputStream sos = resp.getOutputStream();
                    sos.write(bytes);
                    sos.flush();
                    sos.close();
                }
                else
                {
                    resp.getWriter().write("Booking with id: " + bookingId + " not fetched!");
                }

            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
    }
}