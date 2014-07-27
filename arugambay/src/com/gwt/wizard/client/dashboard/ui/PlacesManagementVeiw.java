package com.gwt.wizard.client.dashboard.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
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
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.shared.model.PlaceInfo;

public class PlacesManagementVeiw extends Composite
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    private static PlacesManagementVeiwUiBinder uiBinder = GWT.create(PlacesManagementVeiwUiBinder.class);

    interface PlacesManagementVeiwUiBinder extends UiBinder<Widget, PlacesManagementVeiw>
    {
    }

    private final CellTable.Resources tableRes = GWT.create(TableRes.class);
    private List<PlaceInfo> PLACES = new ArrayList<>();

    CellTable<PlaceInfo> placesManagementTable;

    @UiField
    HTMLPanel mainPanel;
    @UiField
    HTMLPanel btnContainer;
    @UiField
    VerticalPanel layout;
    private Button addPlaceBtn;
    private Button editPlaceBtn;
    private Button deletePlaceBtn;
    private final SelectionModel<PlaceInfo> selectionModel = new MultiSelectionModel<PlaceInfo>(null);

    // The list of data to display.

    public PlacesManagementVeiw()
    {
        initWidget(uiBinder.createAndBindUi(this));
        initializeWidget();
    }

    private void initializeWidget()
    {
        PLACES = new ArrayList<>();
        btnContainer.clear();
        mainPanel.clear();
        placesManagementTable = new CellTable<PlaceInfo>(13, tableRes);
        // fetching existing places from server
        fetchPlaces();
        Timer timer = new Timer()
        {
            @Override
            public void run()
            {
                // Setting up placesManagementPanel
                setPlacesManagementPanel();
                // Setting up CellTable
                setPlacesManagementCellTable();
            }
        };
        // Execute the timer to expire 1/2 second in the future
        timer.schedule(500);
    }

    private void fetchPlaces()
    {
        service.getPlaces(new AsyncCallback<List<PlaceInfo>>()
        {
            @Override
            public void onSuccess(List<PlaceInfo> result)
            {
                if (result != null && result.size() > 0)
                {
                    for (PlaceInfo place : result)
                    {
                        PLACES.add(place);
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to fetch places from server!");
            }
        });
    }

    private void setPlacesManagementCellTable()
    {
        placesManagementTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<PlaceInfo>createCheckboxManager());
        // Checkbox
        Column<PlaceInfo, Boolean> checkColumn = new Column<PlaceInfo, Boolean>(
                new CheckboxCell(true, false))
        {
            @Override
            public Boolean getValue(PlaceInfo object)
            {
                return selectionModel.isSelected(object);
            }
        };

        // Create place column.
        TextColumn<PlaceInfo> cityColumn = new TextColumn<PlaceInfo>()
        {
            @Override
            public String getValue(PlaceInfo place)
            {
                return place.getCity();
            }
        };
        // Create place column.
        TextColumn<PlaceInfo> pickupColumn = new TextColumn<PlaceInfo>()
        {
            @Override
            public String getValue(PlaceInfo place)
            {
                return place.getPickup();
            }
        };
        // Create place column.
        TextColumn<PlaceInfo> placeColumn = new TextColumn<PlaceInfo>()
        {
            @Override
            public String getValue(PlaceInfo place)
            {
                return place.getPlace();
            }
        };

        // Add the columns.
        placesManagementTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        placesManagementTable.setColumnWidth(checkColumn, 30, Unit.PX);
        placesManagementTable.addColumn(cityColumn, "City");
        placesManagementTable.addColumn(pickupColumn, "Pickup");
        placesManagementTable.addColumn(placeColumn, "Place");

        // Create a data provider.
        ListDataProvider<PlaceInfo> dataProvider = new ListDataProvider<PlaceInfo>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(placesManagementTable);
        // Add the data to the data provider, which automatically pushes it to the widget.
        List<PlaceInfo> list = dataProvider.getList();
        for (PlaceInfo place : PLACES)
        {
            list.add(place);
        }

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(placesManagementTable);

        placesManagementTable.setWidth("100%");
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().getStyle().setWidth(100, Unit.PCT);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add(placesManagementTable);
        panel.add(pager);
        mainPanel.add(panel);
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
                final Label newCityLbl = new Label("Enter City");
                final Label newPickupLbl = new Label("Enter Pickup");
                final Label newPlaceLbl = new Label("Enter Place");
                final TextBox newCityTxtBox = new TextBox();
                final TextBox newPickupTxtBox = new TextBox();
                final TextBox newPlaceTxtBox = new TextBox();

                mainPanel.getElement().getStyle().setPadding(5, Unit.PX);
                mainPanel.getElement().getStyle().setWidth(265, Unit.PX);

                Button creatNewPlaceBtn = new Button("Create");
                creatNewPlaceBtn.setStyleName("btn btn-primary");
                creatNewPlaceBtn.addClickHandler(new ClickHandler()
                {

                    @Override
                    public void onClick(ClickEvent event)
                    {
                        String city = (newCityTxtBox.getText() == null || newPlaceTxtBox.getText().length() == 0) ? "" : newCityTxtBox.getText();
                        String pickup = (newPickupTxtBox.getText() == null || newPickupTxtBox.getText().length() == 0) ? "" : newPickupTxtBox.getText();
                        String place = (newPlaceTxtBox.getText() == null || newPlaceTxtBox.getText().length() == 0) ? "" : newPlaceTxtBox.getText();

                        if (city.length() == 0 || pickup.length() == 0 || place.length() == 0)
                        {
                            errLbl.setText("Please fill up all fields");
                        }
                        else
                        {
                            PlaceInfo placeInfo = new PlaceInfo();
                            placeInfo.setCity(city);
                            placeInfo.setPickup(pickup);
                            placeInfo.setPlace(place);

                            service.savePlace(placeInfo, new AsyncCallback<Boolean>()
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
                                        newCityTxtBox.setText("");
                                        newPickupTxtBox.setText("");
                                        newPlaceTxtBox.setText("");

                                        errLbl.setStyleName("successLbl");
                                        errLbl.setText("Place created Successfully");
                                        initializeWidget();
                                    }
                                }
                            });
                        }
                    }
                });
                // Setting up Popup Panel
                creatNewPlaceBtn.getElement().getStyle().setFloat(Float.RIGHT);
                hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                hp.setSpacing(10);
                hp.add(newCityLbl);
                hp.add(newCityTxtBox);
                mainPanel.add(hp);

                hp = new HorizontalPanel();
                hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                hp.setSpacing(10);
                hp.add(newPickupLbl);
                hp.add(newPickupTxtBox);
                mainPanel.add(hp);

                hp = new HorizontalPanel();
                hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                hp.setSpacing(10);
                hp.add(newPlaceLbl);
                hp.add(newPlaceTxtBox);
                mainPanel.add(hp);

                mainPanel.add(creatNewPlaceBtn);
                mainPanel.add(errLbl);
                newPlacePopUpPanel.add(mainPanel);
                newPlacePopUpPanel.addStyleName("addNewPlacePopup");
                newPlacePopUpPanel.setPopupPosition(event.getClientX(), event.getClientY());
                newPlacePopUpPanel.show();
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
                for (PlaceInfo p : PLACES)
                {
                    if (selectionModel.isSelected(p))
                    {
                        service.deletePlace(p.getId(), new AsyncCallback<Boolean>()
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
                for (PlaceInfo p : PLACES)
                {
                    if (selectionModel.isSelected(p))
                        count++;
                }
                if (count == 1)
                {
                    for (PlaceInfo p : PLACES)
                    {
                        if (selectionModel.isSelected(p))
                        {
                            final Long placeId = p.getId();
                            final String cityName = p.getCity();
                            final String pickupName = p.getPickup();
                            final String placeName = p.getPlace();

                            final PopupPanel editPlacePopUpPanel = new PopupPanel(true);
                            final VerticalPanel vPanel = new VerticalPanel();
                            HorizontalPanel hp = new HorizontalPanel();
                            final Label errLbl = new Label();
                            errLbl.setStyleName("errLbl");

                            final Label editCityLbl = new Label("Enter City");
                            final Label editPickupLbl = new Label("Enter Pickup");
                            final Label editPlaceLbl = new Label("Enter Place");
                            final TextBox editCityTxtBox = new TextBox();
                            final TextBox editPickupTxtBox = new TextBox();
                            final TextBox editPlaceTxtBox = new TextBox();

                            editCityTxtBox.setText(cityName);
                            editPickupTxtBox.setText(pickupName);
                            editPlaceTxtBox.setText(placeName);

                            vPanel.getElement().getStyle().setPadding(5, Unit.PX);
                            vPanel.getElement().getStyle().setWidth(265, Unit.PX);

                            Button editPlaceBtn = new Button("Edit");
                            editPlaceBtn.setStyleName("btn btn-primary");
                            editPlaceBtn.addClickHandler(new ClickHandler()
                            {

                                @Override
                                public void onClick(ClickEvent event)
                                {
                                    if ((editCityTxtBox.getText() == null || editCityTxtBox.getText().length() == 0)
                                            || (editPickupTxtBox.getText() == null || editPickupTxtBox.getText().length() == 0)
                                            || (editPlaceTxtBox.getText() == null || editPlaceTxtBox.getText().length() == 0))
                                    {
                                        errLbl.setText("Please fill up all fields");
                                    }
                                    else
                                    {
                                        PlaceInfo placeInfo = new PlaceInfo();
                                        placeInfo.setCity(editCityTxtBox.getText());
                                        placeInfo.setPickup(editPickupTxtBox.getText());
                                        placeInfo.setPlace(editPlaceTxtBox.getText());

                                        service.editPlace(placeId, placeInfo, new AsyncCallback<Boolean>()
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
                            hp.add(editCityTxtBox);
                            vPanel.add(hp);

                            hp = new HorizontalPanel();
                            hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                            hp.setSpacing(10);
                            hp.add(editPickupLbl);
                            hp.add(editPickupTxtBox);
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
