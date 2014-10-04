package com.gwt.wizard.client.dashboard.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.shared.model.RouteInfo;

public class RouteManagementVeiw extends Composite
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    private static PlacesManagementVeiwUiBinder uiBinder = GWT.create(PlacesManagementVeiwUiBinder.class);

    interface PlacesManagementVeiwUiBinder extends UiBinder<Widget, RouteManagementVeiw>
    {
    }

    private final CellTable.Resources tableRes = GWT.create(TableRes.class);
    private List<RouteInfo> routes;

    CellTable<RouteInfo> placesManagementTable;

    @UiField
    HTMLPanel mainPanel;
    @UiField
    HTMLPanel btnContainer;
    @UiField
    VerticalPanel layout;
    private Button addPlaceBtn;
    private Button editPlaceBtn;
    private Button deletePlaceBtn;
    private final SelectionModel<RouteInfo> selectionModel = new MultiSelectionModel<RouteInfo>(null);

    // The list of data to display.

    public RouteManagementVeiw()
    {
        initWidget(uiBinder.createAndBindUi(this));
        initializeWidget();
    }

    private void initializeWidget()
    {
        routes = new ArrayList<>();
        btnContainer.clear();
        mainPanel.clear();
        placesManagementTable = new CellTable<RouteInfo>(13, tableRes);
        // fetching existing places from server
        // Execute the timer to expire 1/2 second in the future
    }

    private void setPlacesManagementPanel()
    {
        setDeletePlaceBtn();
        setEditPlaceBtn();
        setAddPlaceBtn();
    }

    private void setAddPlaceBtn()
    {
        addPlaceBtn = new Button();
        addPlaceBtn.setStyleName("btn btn-primary");
        addPlaceBtn.setText("Add");
        addPlaceBtn.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                // Components
                final PopupPanel newPlacePopUpPanel = new PopupPanel(true);
                final VerticalPanel mainPanel = new VerticalPanel();
                HorizontalPanel hp = new HorizontalPanel();
                final Label errLbl = new Label();
                errLbl.setStyleName("errLbl");
                final Label newStartLbl = new Label("Enter Start");
                final Label newEndLbl = new Label("Enter End");
                final Label newPlaceLbl = new Label("Enter Place");
                final TextBox newCityTxtBox = new TextBox();
                final TextBox newPickupTxtBox = new TextBox();
                final TextBox newPlaceTxtBox = new TextBox();

                mainPanel.getElement().getStyle().setPadding(5, Unit.PX);
                mainPanel.getElement().getStyle().setWidth(265, Unit.PX);

            }
        });
        addPlaceBtn.getElement().getStyle().setFloat(Float.RIGHT);
        addPlaceBtn.getElement().getStyle().setMargin(3, Unit.PX);
        btnContainer.add(addPlaceBtn);
    }

    private void setDeletePlaceBtn()
    {
        deletePlaceBtn = new Button();
        deletePlaceBtn.setStyleName("btn btn-primary");
        deletePlaceBtn.setText("Delete");
        deletePlaceBtn.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                for (RouteInfo p : routes)
                {
                    if (selectionModel.isSelected(p))
                    {
                        service.deleteRoute(p.getId(), new AsyncCallback<Boolean>()
                        {

                            @Override
                            public void onFailure(Throwable caught)
                            {
                                Window.alert("Failed to connect to server");
                            }

                            @Override
                            public void onSuccess(Boolean result)
                            {
                                if (result)
                                {
                                    Window.alert("Selected Place Deleted");
                                    initializeWidget();
                                }
                            }
                        });
                    }
                }
            }
        });
        deletePlaceBtn.getElement().getStyle().setFloat(Float.RIGHT);
        deletePlaceBtn.getElement().getStyle().setMargin(3, Unit.PX);
        btnContainer.add(deletePlaceBtn);
    }

    private void setEditPlaceBtn()
    {
        editPlaceBtn = new Button();
        editPlaceBtn.setStyleName("btn btn-primary");
        editPlaceBtn.setText("Edit");
        editPlaceBtn.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                int count = 0;
                for (RouteInfo p : routes)
                {
                    if (selectionModel.isSelected(p))
                        count++;
                }
                if (count == 1)
                {
                    for (RouteInfo p : routes)
                    {
                        if (selectionModel.isSelected(p))
                        {
                            final Long placeId = p.getId();

                            final PopupPanel editPlacePopUpPanel = new PopupPanel(true);
                            final VerticalPanel vPanel = new VerticalPanel();
                            HorizontalPanel hp = new HorizontalPanel();
                            final Label errLbl = new Label();
                            errLbl.setStyleName("errLbl");

                            final Label editCityLbl = new Label("Enter City");
                            final Label editPickupLbl = new Label("Enter Pickup");
                            final Label editPlaceLbl = new Label("Enter Place");
                            final TextBox editStartTxtBox = new TextBox();
                            final TextBox editEndTxtBox = new TextBox();
                            final TextBox editPlaceTxtBox = new TextBox();

                            editStartTxtBox.setText(p.getStart());
                            editEndTxtBox.setText(p.getEnd());

                            vPanel.getElement().getStyle().setPadding(5, Unit.PX);
                            vPanel.getElement().getStyle().setWidth(265, Unit.PX);

                            Button editPlaceBtn = new Button("Edit");
                            editPlaceBtn.setStyleName("btn btn-primary");
                            editPlaceBtn.addClickHandler(new ClickHandler()
                            {

                                @Override
                                public void onClick(ClickEvent event)
                                {
                                    if ((editStartTxtBox.getText() == null || editStartTxtBox.getText().length() == 0)
                                            || (editEndTxtBox.getText() == null || editEndTxtBox.getText().length() == 0)
                                            || (editPlaceTxtBox.getText() == null || editPlaceTxtBox.getText().length() == 0))
                                    {
                                        errLbl.setText("Please fill up all fields");
                                    }
                                    else
                                    {
                                        RouteInfo routeInfo = new RouteInfo();
                                        routeInfo.setStart(editStartTxtBox.getText());
                                        routeInfo.setEnd(editEndTxtBox.getText());

                                        service.editRoute(placeId, routeInfo, new AsyncCallback<Boolean>()
                                        {

                                            @Override
                                            public void onFailure(Throwable caught)
                                            {
                                                Window.alert("Failed to save place!");
                                            }

                                            @Override
                                            public void onSuccess(Boolean result)
                                            {
                                                if (result)
                                                {
                                                    editPlaceTxtBox.setText("");
                                                    errLbl.setStyleName("successLbl");
                                                    initializeWidget();
                                                    errLbl.setText("Place edited Successfully");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            // Setting up Popup Panel
                            hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                            hp.setSpacing(10);
                            hp.add(editCityLbl);
                            hp.add(editStartTxtBox);
                            vPanel.add(hp);

                            hp = new HorizontalPanel();
                            hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                            hp.setSpacing(10);
                            hp.add(editPickupLbl);
                            hp.add(editEndTxtBox);
                            vPanel.add(hp);

                            hp = new HorizontalPanel();
                            hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                            hp.setSpacing(10);
                            hp.add(editPlaceLbl);
                            hp.add(editPlaceTxtBox);
                            vPanel.add(hp);

                            editPlaceBtn.getElement().getStyle().setFloat(Float.RIGHT);
                            vPanel.add(editPlaceBtn);
                            vPanel.add(errLbl);
                            editPlacePopUpPanel.add(vPanel);
                            editPlacePopUpPanel.addStyleName("addNewPlacePopup");
                            editPlacePopUpPanel.setPopupPosition(event.getClientX(), event.getClientY());
                            editPlacePopUpPanel.show();
                        }
                    }
                }
                else
                    Window.alert("Select one row to edit");
            }
        });
        editPlaceBtn.getElement().getStyle().setFloat(Float.RIGHT);
        editPlaceBtn.getElement().getStyle().setMargin(3, Unit.PX);
        btnContainer.add(editPlaceBtn);
    }
}
