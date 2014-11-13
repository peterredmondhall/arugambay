package com.gwt.wizard.server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gwt.wizard.server.util.Mailer;
import com.gwt.wizard.shared.model.BookingInfo;
import com.stripe.Stripe;
import com.stripe.model.Charge;

public class StripePayment
{
    private static final Logger logger = Logger.getLogger(StripePayment.class.getName());

    public boolean charge(String card, BookingInfo bookingInfo, String stripeSecret)
    {
        try
        {
            Stripe.apiKey = stripeSecret;

            Map<String, Object> chargeParams = new HashMap<String, Object>();
            int cents = (int) (bookingInfo.getRouteInfo().getPrice() * 100);
            chargeParams.put("amount", cents);
            chargeParams.put("currency", "usd");
            chargeParams.put("card", card); // obtained with Stripe.js
            chargeParams.put("description", "Taxi Charges Sri Lanka");

            logger.info("charging " + cents);
            Charge charge = Charge.create(chargeParams);
            logger.info("charging successful");
            return charge.getPaid();
        }

        catch (Exception exception)
        {
            logger.log(Level.SEVERE, exception.getMessage(), exception);
            Mailer.sendError(exception);
        }
        return false;
    }
}
