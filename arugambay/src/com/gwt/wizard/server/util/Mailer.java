package com.gwt.wizard.server.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.model.BookingInfo;

public class Mailer
{
    private static final Logger log = Logger.getLogger(Mailer.class.getName());
    public static Map<String, File> templateMap = Maps.newHashMap();

    private static final String DISPATCHER = "dispatch@taxisurfr.com";

    private static File getFile(String path)
    {
        File file = templateMap.get(path);
        if (file == null)
        {
            templateMap.put(path, new File(path));
        }
        return templateMap.get(path);
    }

    public static final String SHARE_REQUEST = "template/shareRequest.html";
    public static final String FEEDBACK_REQUEST = "template/feedbackRequest.html";
    public static final String SHARE_ACCEPTED = "template/shareAccepted.html";
    public static final String CONFIRMATION = "template/confirmation.html";

    public static void sendShareRequest(BookingInfo parentBooking, BookingInfo bookingInfo, Profil profil)
    {

        String html = BookingUtil.toConfirmationRequestHtml(bookingInfo, getFile(SHARE_REQUEST), profil);
        send(parentBooking.getEmail(), html, null);
        send(profil.getMonitorEmail(), html, null);
        send(profil.getContractorEmail(), html, null);
    }

    public static void sendShareAccepted(String email, BookingInfo parentBookingInfo)
    {
        String html = BookingUtil.toConfirmationEmailHtml(parentBookingInfo, getFile(SHARE_ACCEPTED));
        send(email, html, null);

    }

    public static void sendConfirmation(BookingInfo bookingInfo, Profil profil)
    {

        String html = "error";
        html = BookingUtil.toConfirmationEmailHtml(bookingInfo, getFile(CONFIRMATION));
        html = html.replace("INSERT_ORDERFORM", profil.getTaxisurfUrl() + "/orderform?order=" + bookingInfo.getId());
        byte[] pdfData = new PdfUtil().generateTaxiOrder("template/order.pdf", bookingInfo);
        String email = bookingInfo.getEmail();
        send(email, html, pdfData);
        send(profil.getMonitorEmail(), html, pdfData);
        send(profil.getArugamBayEmail(), html, pdfData);
    }

    private static void send(String toEmail, String htmlBody, byte[] pdfData)
    {
        if (toEmail != null)
        {
            log.info("send:" + toEmail);
            // ...
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            try
            {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(DISPATCHER, "taxisurfr"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(toEmail, toEmail));
                msg.setSubject("Arugam Taxi");
                // msg.setText(msgBody);

                Multipart mp = new MimeMultipart();

                // html
                {
                    MimeBodyPart htmlPart = new MimeBodyPart();
                    htmlPart.setContent(htmlBody, "text/html");
                    mp.addBodyPart(htmlPart);
                }

                // pdf
                msg.setContent(mp);

                Transport.send(msg);
                log.info("sent message to :" + toEmail);

            }
            catch (AddressException e)
            {
                log.log(Level.SEVERE, "address exception :" + e.getMessage());
                sendError(e);
                throw new RuntimeException(e);
            }
            catch (MessagingException e)
            {
                sendError(e);
                throw new RuntimeException(e);
            }
            catch (UnsupportedEncodingException e)
            {
                sendError(e);
                throw new RuntimeException(e);

            }
        }
    }

    public static void sendError(Exception exception)
    {
        send("peterredmondhall@gmail.com", exception.getMessage(), null);
    }

    public static String setFeedbackRequest(BookingInfo bookingInfo, Profil profil)
    {
        String html = BookingUtil.toConfirmationRequestHtml(bookingInfo, getFile(FEEDBACK_REQUEST), profil);
        log.info("send:" + html.substring(0, 100));
        html = html.replace("___NAME__", bookingInfo.getName());
        html = html.replace("___LINK__", profil.getTaxisurfUrl() + "?review=" + bookingInfo.getId());

        send(bookingInfo.getEmail(), html, null);

        return html;

    }

}
