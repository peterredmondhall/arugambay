package com.gwt.wizard.server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            chargeParams.put("amount", bookingInfo.getRouteInfo().getPrice() * 100);
            chargeParams.put("currency", "usd");
            chargeParams.put("card", card); // obtained with Stripe.js
            chargeParams.put("description", "Taxi Charges Sri Lanka");

            Charge charge = Charge.create(chargeParams);
            return charge.getPaid();
        }

        catch (Exception exception)
        {
            logger.log(Level.SEVERE, exception.getMessage(), exception);
        }
        return false;
    }
}
