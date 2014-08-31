package com.gwt.wizard.client.core;

import static com.gwt.wizard.client.GwtWizard.CONTACT;

import java.util.HashMap;
import java.util.Map;

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
import com.gwt.wizard.client.GwtWizard;
import com.gwt.wizard.client.ICallback;
import com.gwt.wizard.client.steps.ConfirmationStep;
import com.gwt.wizard.client.steps.ShareConfirmationStep;
import com.gwt.wizard.client.steps.ShareStep;
import com.gwt.wizard.client.steps.TransportStep;
import com.gwt.wizard.shared.model.BookingInfo;

public class Wizard extends Composite
{

    private static WizardUiBinder uiBinder = GWT.create(WizardUiBinder.class);

    interface WizardUiBinder extends UiBinder<Widget, Wizard>
    {
    }

    private ICallback saveBookingCb;
    private ICallback getBookingListCb;
    private ICallback sendShareRequestCb;
    // private ICallback sendShareAcceptedCb;

    private final Map<WizardStep, Integer> map = new HashMap<WizardStep, Integer>();
    private final Map<HTML, Integer> headers = new HashMap<HTML, Integer>();
    private WizardStep currentstep;
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
        map.clear();
        initWidget(uiBinder.createAndBindUi(this));
        steps.clear();
        header.clear();
        mainPanel.setVisible(false);
        next.ensureDebugId("button_next");

    }

    public void add(WizardStep step)
    {
        HTML headerHTML = new HTML((headers.size() + 1) + ". " + step.getCaption());
        headers.put(headerHTML, headers.size() + 1);
        header.add(headerHTML);

        step.getContent().setVisible(false);
        steps.add(step.getContent());

        if (!map.containsKey(step))
            map.put(step, map.size() + 1);
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
        int current = map.get(currentstep);
        currentstep.getContent().setVisible(false);
        currentstep.onBack();

        current -= 1;
        currentstep = getStep(current);

        currentstep.getContent().setVisible(true);
        ((Showable) currentstep.getContent()).show(true, prev, next, cancel);
        updateHeader(current);
    }

    @UiHandler("next")
    public void onNextClick(ClickEvent event)
    {
        // validation, don't move forward if there are any error on current step
        if (!currentstep.onNext())
        {
            return;
        }

        int current = map.get(currentstep);
        currentstep.getContent().setVisible(false);

        if (current == CONTACT)
        {
            if (saveBookingCb != null)
            {
                saveBookingCb.execute();
            }

        }
        if (current == GwtWizard.TRANSPORT)
        {
            if (getBookingListCb != null)
            {
                getBookingListCb.execute();
            }

        }
        if (current == GwtWizard.SUMMARY)
        {
            if (sendShareRequestCb != null)
            {
                sendShareRequestCb.execute();
            }
        }
        currentstep.onNext();

        current += 1;

        currentstep = getStep(current);

        currentstep.getContent().setVisible(true);
        ((Showable) currentstep.getContent()).show(true, prev, next, cancel);
        updateHeader(current);

    }

    public void onNextShare()
    {
        int current = map.get(currentstep);
        currentstep.getContent().setVisible(false);

        ((ShareStep) currentstep).onNextShare();
        current += 1;
        currentstep = getStep(current);
        currentstep.getContent().setVisible(true);
        ((Showable) currentstep.getContent()).show(true, prev, next, cancel);
        updateHeader(current);

    }

    @UiHandler("cancel")
    public void onCancelClick(ClickEvent event)
    {
        // just move to step 1
        currentstep.getContent().setVisible(false);
        currentstep = getStep(1);	// get first step

        currentstep.getContent().setVisible(true);
        updateHeader(1);
    }

    private WizardStep getStep(int stepNo)
    {
        for (WizardStep step : map.keySet())
        {
            if (map.get(step).intValue() == stepNo)
            {
                return step;
            }
        }

        return null;
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
        double per = current / map.size();
        progressBar.setWidth(Math.round(per) + "%");
    }

    private void clearAll()
    {
        for (WizardStep step : map.keySet())
        {
            step.clear();
        }
    }

    /**
     * must called to show wizard
     */
    @Override
    public Composite getWidget()
    {
        for (WizardStep step : map.keySet())
        {
            if (step instanceof TransportStep)
            {
                currentstep = step;
                currentstep.getContent().setVisible(true);

                updateHeader(1);

                mainPanel.setVisible(true);
                ((TransportStep) step).init(prev, next, cancel);

                break;
            }
            if (step instanceof ConfirmationStep)
            {
                currentstep = step;
                currentstep.getContent().setVisible(true);

                updateHeader(1);

                mainPanel.setVisible(true);
                ((ConfirmationStep) step).init(prev, next, cancel);

                break;
            }
            if (step instanceof ShareConfirmationStep)
            {
                currentstep = step;
                currentstep.getContent().setVisible(true);

                updateHeader(1);

                mainPanel.setVisible(true);
                // sendShareAcceptedCb.execute();

                break;
            }
        }
        return this;
    }

    public void init()
    {
        currentstep = initstep;
        currentstep.getContent().setVisible(true);

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
//        if (initstep instanceof ShareConfirmationStep)
//        {
//            sendShareAcceptedCb.execute();
//        }

    }

    public void setInitialStep(WizardStep initstep)
    {
        this.initstep = initstep;

    }

    public void addSaveBookingCallback(ICallback cb)
    {
        this.saveBookingCb = cb;
    }

    public void addGetBookingListCallback(ICallback cb)
    {
        this.getBookingListCb = cb;
    }

    public void addSendShareRequestCallback(ICallback cb)
    {
        this.sendShareRequestCb = cb;
    }

//    public void addSendShareAcceptedCallback(ICallback cb)
//    {
//        this.sendShareAcceptedCb = cb;
//    }

    public void shareBooking(BookingInfo ref)
    {

    }

    public void activateShareConfirmationStep(ShareConfirmationStep step)
    {
        step.init(prev, next, cancel);

    }

}
