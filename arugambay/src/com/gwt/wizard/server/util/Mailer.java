package com.gwt.wizard.server.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
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
import javax.mail.util.ByteArrayDataSource;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ContractorInfo;

public class Mailer
{
    private static final Logger log = Logger.getLogger(Mailer.class.getName());
    public static Map<String, File> templateMap = Maps.newHashMap();

    private static final String DISPATCHER = "dispatch@taxisurfr.com";
    private static final String EMAILIT = "peter24lasagna@emailitin.com";

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
        send(parentBooking.getEmail(), html, null, "booker");
        send(profil.getMonitorEmail(), html, null, "monitor");
    }

    public static void sendShareAccepted(String email, BookingInfo parentBookingInfo, Profil profil)
    {
        String html = BookingUtil.toConfirmationEmailHtml(parentBookingInfo, getFile(SHARE_ACCEPTED), profil);
        send(email, html, null, "sharer");
        send(profil.getMonitorEmail(), html, null, "monitor");

    }

    public static void sendConfirmation(BookingInfo bookingInfo, Profil profil, ContractorInfo contractorInfo)
    {

        String html = "error";
        html = BookingUtil.toConfirmationEmailHtml(bookingInfo, getFile(CONFIRMATION), profil);
        html = html.replace("INSERT_ORDERFORM", profil.getTaxisurfUrl() + "/orderform?order=" + bookingInfo.getId());

        byte[] pdfData = new PdfUtil().generateTaxiOrder("template/order.pdf", bookingInfo);
        String email = bookingInfo.getEmail();
        send(email, html, pdfData, "customer");
        send(profil.getMonitorEmail(), html, pdfData, "monitor");
        send(profil.getArugamBayEmail(), html, pdfData, "agent");
        if (contractorInfo != null)
        {
            send(contractorInfo.getEmail(), html, pdfData, "contractor");
        }
        emailit(pdfData, bookingInfo.getOrderRef());
    }

    private static void send(String toEmail, String htmlBody, byte[] pdfData, String role)
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
                log.info(String.format("sent message to (%s):%s", role, toEmail));

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

    private static void emailit(byte[] pdfData, String orderRef)
    {
        // ...
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try
        {

            // email
            MimeMessage msg = new MimeMessage(session);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("order " + orderRef);
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            // construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(pdfData, "application/pdf");
            messageBodyPart.setHeader("Content-Transfer-Encoding", "base64");
            messageBodyPart.setDataHandler(new DataHandler(dataSource));
            messageBodyPart.setFileName("order_" + orderRef + ".pdf");

            multipart.addBodyPart(messageBodyPart);

            // construct message
            msg.setHeader("Content-Type", "multipart/mixed");
            msg.setFrom(new InternetAddress(DISPATCHER, "taxisurfr"));
            msg.setSubject("Test");
            msg.setRecipients(Message.RecipientType.TO, EMAILIT);
            msg.setSentDate(new java.util.Date());
            msg.setContent(multipart);

            // send email
            Transport.send(msg);
            log.info("sent message to :" + EMAILIT);

        }
        catch (AddressException ex)
        {
            log.severe(ex.getMessage());
        }
        catch (MessagingException e)
        {
            log.severe(e.getMessage());
        }
        catch (UnsupportedEncodingException e)
        {
            log.severe(e.getMessage());
        }
    }

    public static void sendError(Exception exception)
    {
        send("peterredmondhall@gmail.com", exception.getMessage(), null, "admin");
    }

    public static String setFeedbackRequest(BookingInfo bookingInfo, Profil profil)
    {
        String html = BookingUtil.toConfirmationRequestHtml(bookingInfo, getFile(FEEDBACK_REQUEST), profil);
        log.info("send:" + html.substring(0, 100));
        html = html.replace("___NAME__", bookingInfo.getName());
        html = html.replace("___LINK__", profil.getTaxisurfUrl() + "?review=" + bookingInfo.getId());

        send(bookingInfo.getEmail(), html, null, "booker");

        return html;

    }

    public static void sendReport(String report)
    {
        send("hall@hall-services.de", report, null, "admin");
    }

}
