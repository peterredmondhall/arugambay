package com.gwt.wizard.client.dashboard.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class DashboardVeiw extends Composite
{

    private static DashboardVeiwUiBinder uiBinder = GWT.create(DashboardVeiwUiBinder.class);

    interface DashboardVeiwUiBinder extends UiBinder<Widget, DashboardVeiw>
    {
    }

    @UiField
    HTMLPanel menuContainer;

    @UiField
    HTMLPanel dataContainer;

    @UiField
    Anchor bookingManagement;

    @UiField
    Anchor placesManagement;
    private final HTMLPanel displayContainer = new HTMLPanel("");

    public DashboardVeiw()
    {
        initWidget(uiBinder.createAndBindUi(this));
        setMenu();
        dataContainer.add(displayContainer);
    }

    private void setMenu()
    {
        bookingManagement.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                displayContainer.clear();
                displayContainer.add(new BookingManagementVeiw());
            }
        });

        placesManagement.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                displayContainer.clear();
                displayContainer.add(new PlacesManagementVeiw());
            }
        });
        displayContainer.add(new BookingManagementVeiw());
    }
}
