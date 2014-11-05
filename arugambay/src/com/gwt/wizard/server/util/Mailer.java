package com.gwt.wizard.server.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
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

import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.model.BookingInfo;

public class Mailer
{
    private static final Logger log = Logger.getLogger(Mailer.class.getName());

    public static void sendShareRequest(BookingInfo parentBooking, BookingInfo bookingInfo, Profil profil)
    {

        String emailMsg = BookingUtil.toEmailText(bookingInfo);
        String html = BookingUtil.toConfirmationRequestHtml(bookingInfo, new File("template/shareRequest.html"), profil);
        send(emailMsg, parentBooking.getEmail(), html, null);
        send(emailMsg, profil.getMonitorEmail(), html, null);
        send(emailMsg, profil.getContractorEmail(), html, null);
    }

    public static void sendShareAccepted(String email, BookingInfo parentBookingInfo)
    {
        String html = BookingUtil.toConfirmationEmailHtml(parentBookingInfo, new File("template/shareAccepted.html"));
        send("emailMsg", email, html, null);

    }

    public static void sendConfirmation(BookingInfo bookingInfo, Profil profil)
    {

        String emailMsg = BookingUtil.toEmailText(bookingInfo);
        String html = "error";
        html = BookingUtil.toConfirmationEmailHtml(bookingInfo, new File("template/confirmation.html"));
        html = html.replace("INSERT_ORDERFORM", profil.getTaxisurfUrl() + "/orderform?order=" + bookingInfo.getId());
        byte[] pdfData = PdfUtil.generateTaxiOrder("template/order.pdf", bookingInfo);
        String email = bookingInfo.getEmail();
        send(emailMsg, email, html, pdfData);
        send(emailMsg, profil.getMonitorEmail(), html, pdfData);
        send(emailMsg, profil.getArugamBayEmail(), html, pdfData);
    }

    private static void send(String msgBody, String toEmail, String htmlBody, byte[] pdfData)
    {
        if (toEmail != null)
        {
            // ...
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            try
            {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("peterredmondhall@gmail.com", "taxisurf"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(toEmail, "Arugam Taxi"));
                msg.setSubject("Arugam Taxi");
                msg.setText(msgBody);

                Multipart mp = new MimeMultipart();

                // html
                {
                    MimeBodyPart htmlPart = new MimeBodyPart();
                    htmlPart.setContent(htmlBody, "text/html");
                    mp.addBodyPart(htmlPart);
                }

                // pdf
                if (pdfData != null)
                {
//                    log.info("pdf:" + pdfData.length);
//                    int counter = 0;
//                    for (byte b : pdfData)
//                    {
//                        log.info(" " + (char) b);
//                        if (counter++ > 6)
//                            break;
//                    }
//                    MimeBodyPart attachment = new MimeBodyPart();
//                    attachment.setFileName("manual.pdf");
//                    attachment.setContent(pdfData, "application/pdf");
//                    mp.addBodyPart(attachment);

//                    MimeBodyPart attachment = new MimeBodyPart();
//                    attachment.setFileName("whatever");
//                    attachment.setDisposition(Part.ATTACHMENT); // Is this needed, and does it work?
//                    DataSource dataSource = new ByteArrayDataSource(pdfData, "application/pdf");
//                    attachment.setDataHandler(new DataHandler(dataSource));
//                    mp.addBodyPart(attachment);
                }
                msg.setContent(mp);

                Transport.send(msg);
                log.info("sent message to :" + toEmail);

            }
            catch (AddressException e)
            {
                log.log(Level.SEVERE, "address exception :" + e.getMessage());
                throw new RuntimeException(e);
            }
            catch (MessagingException e)
            {
                throw new RuntimeException(e);
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(e);

            }
        }
    }

}
