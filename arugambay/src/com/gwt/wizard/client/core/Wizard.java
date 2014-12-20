package com.gwt.wizard.client.core;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.steps.ConfirmationStep;
import com.gwt.wizard.client.steps.CreditCardStep;
import com.gwt.wizard.client.steps.ShareConfirmationStep;
import com.gwt.wizard.client.steps.ShareStep;
import com.gwt.wizard.client.steps.TransportStep;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.RatingInfo;
import com.gwt.wizard.shared.model.RouteInfo;

public class Wizard extends Composite
{

    private static WizardUiBinder uiBinder = GWT.create(WizardUiBinder.class);

    interface WizardUiBinder extends UiBinder<Widget, Wizard>
    {
    }

    public static BookingInfo BOOKINGINFO = new BookingInfo();
    public static List<BookingInfo> EXISTING_BOOKINGS_ON_ROUTE;
    public static RouteInfo ROUTEINFO;
    public static ProfilInfo PROFILINFO;
    public static RatingInfo RATINGINFO;

    private final List<WizardStep> stepList;
    private final Map<WizardStep, HTML> headers = Maps.newHashMap();
    private int currentstep;
    private WizardStep initstep;

    @UiField
    HTMLPanel mainPanel;
    @UiField
    HTMLPanel steps;
    @UiField
    FlowPanel header;
    @UiField
    HTML progressBar;

    @UiField
    Button prev;
    @UiField
    Button next;
    @UiField
    Button cancel;

    public Wizard()
    {
        stepList = Lists.newArrayList();
        initWidget(uiBinder.createAndBindUi(this));
        steps.clear();
        header.clear();
        mainPanel.setVisible(false);
        next.ensureDebugId("button_next");
        currentstep = 0;
    }

    public void add(WizardStep step)
    {
        HTML headerHTML = new HTML((stepList.size() + 1) + ". " + step.getCaption());
        headers.put(step, headerHTML);
        header.add(headerHTML);

        step.getContent().setVisible(false);
        steps.add(step.getContent());

        stepList.add(step);
    }

    @Override
    public void setHeight(String height)
    {
        mainPanel.setHeight(height);
    }

    @Override
    public void setWidth(String width)
    {
        mainPanel.setWidth(width);
    }

    @UiHandler("prev")
    public void onPrevClick(ClickEvent event)
    {
        stepList.get(currentstep).getContent().setVisible(false);
        stepList.get(currentstep).onBack();

        currentstep--;

        stepList.get(currentstep).getContent().setVisible(true);
        stepList.get(currentstep).show(true, prev, next, cancel);
        updateHeader(currentstep);
    }

    @UiHandler("next")
    public void onNextClick(ClickEvent event)
    {
        // validation, don't move forward if there are any error on current step
        if (!stepList.get(currentstep).onNext())
        {
            return;
        }

        stepList.get(currentstep).getContent().setVisible(false);

        currentstep++;

        handleStep();

        stepList.get(currentstep).getContent().setVisible(true);
        stepList.get(currentstep).show(true, prev, next, cancel);
        updateHeader(currentstep);

    }

    private void handleStep()
    {
        if (stepList.get(currentstep) instanceof ShareStep)
        {
            if (EXISTING_BOOKINGS_ON_ROUTE.size() == 0)
            {
                currentstep++;
            }
        }
        if (stepList.get(currentstep) instanceof CreditCardStep)
        {
            if (OrderType.SHARE.equals(BOOKINGINFO.getOrderType()))
            {
                currentstep++;
            }
        }

    }

    @UiHandler("cancel")
    public void onCancelClick(ClickEvent event)
    {
        // just move to step 1
        stepList.get(currentstep).getContent().setVisible(false);
        currentstep = 0;	// get first step

        stepList.get(currentstep).getContent().setVisible(true);
        stepList.get(currentstep).show(true, prev, next, cancel);
        updateHeader(0);
    }

    private void updateHeader(int current)
    {
        for (int i = 0; i < stepList.size(); i++)
        {
            HTML headerHTML = headers.get(stepList.get(i));
            if (i == current)
            {
                headerHTML.addStyleName("header-active");
                headerHTML.removeStyleName("header-disable");
            }
            else
            {
                headerHTML.addStyleName("header-disable");
                headerHTML.removeStyleName("header-active");
            }
        }
//        for (HTML headerHTML : headers.keySet())
//        {
//            if (headers.get(headerHTML).intValue() == current)
//            {
//                headerHTML.addStyleName("header-active");
//                headerHTML.removeStyleName("header-disable");
//            }
//            else
//            {
//                headerHTML.addStyleName("header-disable");
//                headerHTML.removeStyleName("header-active");
//            }
//        }

        // show progress bar
        double per = (current + 1) * 100 / stepList.size();
        progressBar.setWidth(Math.round(per) + "%");
    }

//    /**
//     * must called to show wizard
//     */
//    @Override
//    public Composite getWidget()
//    {
//        for (WizardStep step : map.keySet())
//        {
//            if (step instanceof TransportStep)
//            {
//                currentstep = step;
//                currentstep.getContent().setVisible(true);
//
//                updateHeader(1);
//
//                mainPanel.setVisible(true);
//                ((TransportStep) step).init(prev, next, cancel);
//
//                break;
//            }
//            if (step instanceof ConfirmationStep)
//            {
//                currentstep = step;
//                currentstep.getContent().setVisible(true);
//
//                updateHeader(1);
//
//                mainPanel.setVisible(true);
//                ((ConfirmationStep) step).init(prev, next, cancel);
//
//                break;
//            }
//            if (step instanceof ShareConfirmationStep)
//            {
//                currentstep = step;
//                currentstep.getContent().setVisible(true);
//
//                updateHeader(1);
//
//                mainPanel.setVisible(true);
//                // sendShareAcceptedCb.execute();
//
//                break;
//            }
//        }
//        return this;
//    }

    public void init()
    {
        stepList.get(currentstep).getContent().setVisible(true);

        updateHeader(0);

        mainPanel.setVisible(true);
        if (initstep instanceof TransportStep)
        {
            ((TransportStep) initstep).init(prev, next, cancel);
        }
        if (initstep instanceof ConfirmationStep)
        {
            ((ConfirmationStep) initstep).init(prev, next, cancel);
        }

    }

    public void setInitialStep(WizardStep initstep)
    {
        this.initstep = initstep;

    }

    public void shareBooking(BookingInfo ref)
    {

    }

    public void activateShareConfirmationStep(ShareConfirmationStep step)
    {
        step.init(prev, next, cancel);
    }
}
