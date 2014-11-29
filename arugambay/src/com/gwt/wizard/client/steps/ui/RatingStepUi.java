package com.gwt.wizard.client.steps.ui;

import static com.gwt.wizard.client.core.Wizard.EXISTING_BOOKINGS_ON_ROUTE;
import static com.gwt.wizard.client.core.Wizard.RATINGINFO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.GwtWizard;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.steps.ConfirmationStep;

public class RatingStepUi extends Composite
{
    private static RatingStepUiUiBinder uiBinder = GWT.create(RatingStepUiUiBinder.class);

    interface RatingStepUiUiBinder extends UiBinder<Widget, RatingStepUi>
    {
    }

    @UiField
    Panel mainPanel, hp2;

    // private final List<RouteInfo> routes = Lists.newArrayList();
    Wizard wizard;
    ConfirmationStep confirmationStep;

    public RatingStepUi(Wizard wizard, ConfirmationStep confirmationStep)
    {

        initWidget(uiBinder.createAndBindUi(this));
//        DockLayoutPanel p = new DockLayoutPanel(Unit.EM);
//        p.addNorth(new HTML("header"), 2);
//        p.addSouth(new HTML("footer"), 2);
//        p.addWest(new HTML("navigation"), 10);
//        p.add(new HTML("xxxx xxxx xxxx"));
//        layoutPanel.add(p);
        mainPanel.setVisible(true);
        hp2.add(getRatingTable());
        this.wizard = wizard;
        this.confirmationStep = confirmationStep;
    }

    @Override
    public void setVisible(boolean visible)
    {
        mainPanel.setVisible(visible);
    }

    @Override
    public void setHeight(String height)
    {
        super.setHeight(height);
    }

    @Override
    public void setWidth(String width)
    {
        super.setWidth(width);
    }

    Button next;

    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        this.next = next;
        next.setVisible(true);
        next.setEnabled(EXISTING_BOOKINGS_ON_ROUTE != null);
        prev.setVisible(false);
        cancel.setText("Cancel");
    }

    RadioBar cleanliness = new RadioBar("cleanliness");
    RadioBar safety = new RadioBar("safety");
    RadioBar punctuality = new RadioBar("punctuality");
    RadioBar professionality = new RadioBar("professionality");
    TextArea critic = new TextArea();

    private DecoratorPanel getRatingTable()
    {
        FlexTable layout = new FlexTable();
        layout.setCellSpacing(6);
        layout.setWidth("300px");
        FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

        // Add a title to the form
        cellFormatter.setColSpan(0, 0, 3);
        cellFormatter.setHorizontalAlignment(
                0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        layout.setWidget(1, 1, new Label("Cleanliness"));
        layout.setWidget(2, 1, new Label("Safety"));
        layout.setWidget(3, 1, new Label("Punctuality"));
        layout.setWidget(4, 1, new Label("Professionality"));

        layout.setWidget(1, 2, cleanliness.getPanel());
        layout.setWidget(2, 2, safety.getPanel());
        layout.setWidget(3, 2, punctuality.getPanel());
        layout.setWidget(4, 2, professionality.getPanel());

        critic.setWidth("400px");
        critic.setHeight("100px");

        layout.setWidget(5, 1, new Label("How was your experience in words?"));
        layout.setWidget(5, 2, critic);
        layout.setWidget(6, 3, getSubmitButton());

        // Wrap the contents in a DecoratorPanel
        DecoratorPanel decPanel = new DecoratorPanel();
        decPanel.setWidget(layout);
        return decPanel;
    }

    private Button getSubmitButton()
    {
        Button button = new Button("Send Feedback");
        button.setStyleName("btn btn-primary");
        button.setSize("120px", "30px");

        button.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                RATINGINFO.setCleanliness(cleanliness.getSelection());
                RATINGINFO.setProfessionality(professionality.getSelection());
                RATINGINFO.setPunctuality(punctuality.getSelection());
                RATINGINFO.setSafety(safety.getSelection());
                RATINGINFO.setCritic(critic.getText());

                GwtWizard.SERVICE.addRating(RATINGINFO, new AsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void xxx)
                    {
                        confirmationStep.setRatingFeedbackConfirmation();
                        wizard.onNextClick(null);
                    }

                    @Override
                    public void onFailure(Throwable caught)
                    {
                    }
                });

            }
        });

        return button;
    }

    class RadioBar
    {
        RadioButton rb1;
        RadioButton rb2;
        RadioButton rb3;
        RadioButton rb4;
        RadioButton rb5;
        FlowPanel panel = new FlowPanel();

        RadioBar(String group)
        {
            rb1 = new RadioButton(group, "");
            rb2 = new RadioButton(group, "");
            rb3 = new RadioButton(group, "");
            rb4 = new RadioButton(group, "");
            rb5 = new RadioButton(group, "");

            panel.add(rb1);
            panel.add(rb2);
            panel.add(rb3);
            panel.add(rb4);
            panel.add(rb5);
        }

        Panel getPanel()
        {
            return panel;
        }

        int getSelection()
        {
            if (rb1.getValue())
                return 1;
            if (rb2.getValue())
                return 2;
            if (rb3.getValue())
                return 3;
            if (rb4.getValue())
                return 4;
            if (rb5.getValue())
                return 5;
            return -1;
        }

    }

}
