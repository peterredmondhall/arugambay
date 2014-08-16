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

import com.gwt.wizard.shared.model.BookingInfo;

public class Mailer
{
    private static final Logger log = Logger.getLogger(Mailer.class.getName());

    public static void send(BookingInfo bookingInfo)
    {

        String emailMsg = BookingUtil.toEmailText(bookingInfo);
        String html = "error";
        html = BookingUtil.toEmailHtml(bookingInfo, new File("template/confirmation.html"));
        String email = bookingInfo.getEmail();
        send(emailMsg, email, html);
        send(emailMsg, "hall@hall-services.de", html);
        send(emailMsg, "arugamsurf@gmail.com", html);
    }

    private static void send(String msgBody, String toEmail, String htmlBody)
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

            // html
            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            mp.addBodyPart(htmlPart);
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
