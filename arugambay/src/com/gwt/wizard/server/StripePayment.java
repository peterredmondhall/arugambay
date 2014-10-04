package com.gwt.wizard.server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gwt.wizard.server.entity.Profil;
import com.gwt.wizard.shared.model.BookingInfo;
import com.stripe.Stripe;
import com.stripe.model.Charge;

public class StripePayment
{
    private static final Logger logger = Logger.getLogger(StripePayment.class.getName());

    public boolean charge(String card, BookingInfo bookingInfo, Profil profil)
    {
        try
        {
            // test for silver mobility
            Stripe.apiKey = "sk_test_TCIbuNPlBRe4VowPhqekTO1L";

            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", 400);
            chargeParams.put("currency", "eur");
            chargeParams.put("card", card); // obtained with Stripe.js
            // chargeParams.put("card", "tok_14Z3GJ2dwz89c3UCRVpzKXWz"); // obtained with Stripe.js
            chargeParams.put("description", "Charge for test@example.com");
            // chargeParams.put("metadata", initialMetadata);

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
