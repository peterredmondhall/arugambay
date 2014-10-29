package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;
import static com.gwt.wizard.client.core.Wizard.BOOKINGINFO;

import com.arcbees.stripe.client.CreditCard;
import com.arcbees.stripe.client.CreditCardResponseHandler;
import com.arcbees.stripe.client.StripeFactory;
import com.arcbees.stripe.client.jso.CreditCardResponse;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.client.steps.ui.CreditCardStepUi;
import com.gwt.wizard.client.steps.ui.CreditCardStepUi.ErrorMsg;
import com.gwt.wizard.shared.model.BookingInfo;

public class CreditCardStep implements WizardStep
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    private final CreditCardStepUi ui;
    private final Wizard wizard;
    private boolean stripeInjected;

    public CreditCardStep(Wizard wizard)
    {
        ui = new CreditCardStepUi(this);
        this.wizard = wizard;
    }

    @Override
    public String getCaption()
    {
        return "Payment";
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    public Boolean pay()
    {

        ui.setErrorMsg("", ErrorMsg.NAME);
        if (ui.getCCName() == null || ui.getCCName().trim().length() == 0)
        {
            ui.setErrorMsg(MESSAGES.mayNotBeEmptyErrorMsg(), ErrorMsg.NAME);
            return false;
        }
        ui.setErrorMsg("", ErrorMsg.CVC);
        if (ui.getCvc() == null || ui.getCvc().trim().length() == 0)
        {
            ui.setErrorMsg(MESSAGES.mayNotBeEmptyErrorMsg(), ErrorMsg.CVC);
            return false;
        }
        ui.showProgress();
        payWithStripe();
        return false;
    }

    private void injectStripeJs()
    {

        if (!stripeInjected)
        {
            StripeFactory.get().inject(new Callback<Void, Exception>()
            {
                @Override
                public void onFailure(Exception reason)
                {
                    System.out.println("Failed Injecting Stripe.JS");
                }

                @Override
                public void onSuccess(Void result)
                {
                    stripeInjected = true;
                    injectStripeJs();
                }
            });
        }
        ui.showPayButton();
    }

    void payWithStripe()
    {
        // test for silver mobility
        StripeFactory.get().setPublishableKey("pk_test_rcKuNpP9OpTri7twmZ77UOI5");

        CreditCard creditCard = new CreditCard.Builder()
                // .creditCardNumber(ui.getCCName())
                .creditCardNumber("4242 4242 4242 4242")
                // .cvc(ui.getCvc())
                .cvc("279")
                // .expirationMonth(ui.getCCExpiryMonth())
//                .expirationYear(ui.getCCExpiryYear())
                .expirationMonth(8)
                .expirationYear(2017)
                .name(ui.getCCName())
                // .addressLine1("1093 Charleston rd")
//                .addressLine2("apt. 3")
//                .addressCity("Los Angeles")
//                .addressCountry("United States")
//                .addressState("CA")
//                .addressZip("91257")
                .build();

        StripeFactory.get().getCreditCardToken(creditCard, new CreditCardResponseHandler()
        {
            @Override
            public void onCreditCardReceived(int status, CreditCardResponse creditCardResponse)
            {
                service.payWithStripe(creditCardResponse.getId(), BOOKINGINFO, new AsyncCallback<BookingInfo>()
                {

                    @Override
                    public void onFailure(Throwable caught)
                    {
                        Window.alert("Failed to pay with stripe");
                    }

                    @Override
                    public void onSuccess(BookingInfo bi)
                    {
                        BOOKINGINFO.setStatus(bi.getStatus());
                        wizard.onNextClick(null);
                    }
                });

            }
        });
    }

    @Override
    public Boolean onBack()
    {
        return true;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        ui.show(visible, prev, next, cancel);
        injectStripeJs();
    }

    @Override
    public Boolean onNext()
    {
        return true;
    }

}
