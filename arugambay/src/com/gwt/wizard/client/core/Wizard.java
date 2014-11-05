package com.gwt.wizard.client.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
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
import com.gwt.wizard.client.steps.ShareConfirmationStep;
import com.gwt.wizard.client.steps.ShareStep;
import com.gwt.wizard.client.steps.TransportStep;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.RouteInfo;

public class Wizard extends Composite
{

    private static WizardUiBinder uiBinder = GWT.create(WizardUiBinder.class);

    interface WizardUiBinder extends UiBinder<Widget, Wizard>
    {
    }

    public static BookingInfo BOOKINGINFO = new BookingInfo();
    public static RouteInfo ROUTEINFO;

    private final List<WizardStep> stepList;
    private final Map<HTML, Integer> headers = new HashMap<HTML, Integer>();
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
        HTML headerHTML = new HTML((headers.size() + 1) + ". " + step.getCaption());
        headers.put(headerHTML, headers.size() + 1);
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

        if (stepList.get(currentstep) instanceof ShareStep)
        {
            ShareStep shareStep = (ShareStep) stepList.get(currentstep);
            int existingOrders = shareStep.getBookingList().size();
            if (existingOrders == 0)
            {
                currentstep++;
            }
        }

        stepList.get(currentstep).getContent().setVisible(true);
        stepList.get(currentstep).show(true, prev, next, cancel);
        updateHeader(currentstep + 1);

    }

    public void onNextShare()
    {
        throw new RuntimeException();
        // FIXMe
//        int current = map.get(currentstep);
//        currentstep.getContent().setVisible(false);
//
//        ((ShareStep) currentstep).onNextShare();
//        current += 1;
//        currentstep = getStep(current);
//        currentstep.getContent().setVisible(true);
//        ((Showable) currentstep.getContent()).show(true, prev, next, cancel);
//        updateHeader(current);

    }

    @UiHandler("cancel")
    public void onCancelClick(ClickEvent event)
    {
        // just move to step 1
        stepList.get(currentstep).getContent().setVisible(false);
        currentstep = 0;	// get first step

        stepList.get(currentstep).getContent().setVisible(true);
        updateHeader(1);
    }

    private void updateHeader(int current)
    {

        for (HTML headerHTML : headers.keySet())
        {
            if (headers.get(headerHTML).intValue() == current)
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

        // show progress bar
        current = current * 100;
        double per = current / stepList.size();
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

        updateHeader(1);

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
