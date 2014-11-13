package com.gwt.wizard.client.dashboard.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.RouteInfo.SaveMode;

public class RouteManagementVeiw extends Composite
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    private static PlacesManagementVeiwUiBinder uiBinder = GWT.create(PlacesManagementVeiwUiBinder.class);

    interface PlacesManagementVeiwUiBinder extends UiBinder<Widget, RouteManagementVeiw>
    {
    }

    private final CellTable.Resources tableRes = GWT.create(TableRes.class);
    private List<RouteInfo> ROUTES;
    NumberFormat usdFormat = NumberFormat.getFormat(".00");

    CellTable<RouteInfo> routeManagementTable;

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
    private final RouteInfo.PickupType[] listPickupType;
    private Long imageId;

    // The list of data to display.

    public RouteManagementVeiw()
    {
        initWidget(uiBinder.createAndBindUi(this));
        listPickupType = new RouteInfo.PickupType[RouteInfo.PickupType.values().length];
        int i = 0;
        for (RouteInfo.PickupType t : RouteInfo.PickupType.values())
        {
            listPickupType[i++] = t;
        }
        initializeWidget();
    }

    private void initializeWidget()
    {
        ROUTES = new ArrayList<>();
        btnContainer.clear();
        mainPanel.clear();
        routeManagementTable = new CellTable<RouteInfo>(13, tableRes);
        fetchRoutes();
    }

    private void initializeWidget(List<RouteInfo> routeInfo)
    {
        ROUTES = routeInfo;
        btnContainer.clear();
        mainPanel.clear();
        routeManagementTable = new CellTable<RouteInfo>(13, tableRes);
        setCellTable();
        setRouteManagementPanel();
    }

    private void fetchRoutes()
    {
        service.getRoutes(new AsyncCallback<List<RouteInfo>>()
        {

            @Override
            public void onSuccess(List<RouteInfo> routes)
            {

                for (RouteInfo route : routes)
                {
                    ROUTES.add(route);
                }
                setCellTable();
                setRouteManagementPanel();
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to Server!");
            }
        });
    }

    private void setRouteManagementPanel()
    {
        setDeleteRouteBtn();
        setUpdateRouteBtn();
        setAddRouteBtn();
    }

    private void setAddRouteBtn()
    {
        addPlaceBtn = new Button();
        addPlaceBtn.setStyleName("btn btn-primary");
        addPlaceBtn.setText("Add");
        addPlaceBtn.addClickHandler(new RouteAddEditClickHandler(SaveMode.ADD));
        addPlaceBtn.getElement().getStyle().setFloat(Float.RIGHT);
        addPlaceBtn.getElement().getStyle().setMargin(3, Unit.PX);
        btnContainer.add(addPlaceBtn);
    }

    private void setDeleteRouteBtn()
    {
        deletePlaceBtn = new Button();
        deletePlaceBtn.setStyleName("btn btn-primary");
        deletePlaceBtn.setText("Delete");
        deletePlaceBtn.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                for (RouteInfo p : ROUTES)
                {
                    if (selectionModel.isSelected(p))
                    {
                        service.deleteRoute(p, new AsyncCallback<List<RouteInfo>>()
                        {

                            @Override
                            public void onFailure(Throwable caught)
                            {
                                Window.alert("Failed to connect to server");
                            }

                            @Override
                            public void onSuccess(List<RouteInfo> result)
                            {
                                initializeWidget(result);
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

    private void setUpdateRouteBtn()
    {
        editPlaceBtn = new Button();
        editPlaceBtn.setStyleName("btn btn-primary");
        editPlaceBtn.setText("Edit");
        editPlaceBtn.addClickHandler(new RouteAddEditClickHandler(SaveMode.UPDATE));

        editPlaceBtn.getElement().getStyle().setFloat(Float.RIGHT);
        editPlaceBtn.getElement().getStyle().setMargin(3, Unit.PX);
        btnContainer.add(editPlaceBtn);
    }

    private void addPopupPanel(String label, Widget widget, Grid grid, int row)
    {
        grid.setWidget(row, 0, new Label(label));
        grid.setWidget(row, 1, widget);
    }

    private void setCellTable()
    {
        routeManagementTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<RouteInfo>createCheckboxManager());

        // Checkbox
        Column<RouteInfo, Boolean> checkColumn = new Column<RouteInfo, Boolean>(
                new CheckboxCell(true, false))
        {
            @Override
            public Boolean getValue(RouteInfo object)
            {
                return selectionModel.isSelected(object);
            }
        };

        Column<RouteInfo, ImageResource> imageColumn =
                new Column<RouteInfo, ImageResource>(new ImageResourceCell())
                {
                    @Override
                    public ImageResource getValue(final RouteInfo routeInfo)
                    {
                        return new ArugamImageResource(routeInfo);
                    }
                };

        TextColumn<RouteInfo> startColumn = new TextColumn<RouteInfo>()
        {
            @Override
            public String getValue(RouteInfo route)
            {
                return route.getStart();
            }
        };

        TextColumn<RouteInfo> endColumn = new TextColumn<RouteInfo>()
        {
            @Override
            public String getValue(RouteInfo route)
            {
                return route.getEnd();
            }
        };

        TextColumn<RouteInfo> priceColumn = new TextColumn<RouteInfo>()
        {
            @Override
            public String getValue(RouteInfo route)
            {
                return usdFormat.format(route.getPrice());
            }
        };

        TextColumn<RouteInfo> descriptionColumn = new TextColumn<RouteInfo>()
        {
            @Override
            public String getValue(RouteInfo route)
            {
                if (route.getDescription() == null)
                {
                    return "";
                }
                if (route.getDescription().length() > 20)
                {
                    return route.getDescription().substring(0, 20) + "...";
                }
                return route.getDescription();
            }
        };

        TextColumn<RouteInfo> pickuptypeColumn = new TextColumn<RouteInfo>()
        {
            @Override
            public String getValue(RouteInfo route)
            {
                if (route.getPickupType() != null)
                {
                    return route.getPickupType().name();
                }
                return "error";
            }
        };

        routeManagementTable.setTableLayoutFixed(true);
        // Add the columns.

        routeManagementTable.addColumn(checkColumn, "Select");
        routeManagementTable.addColumn(imageColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        routeManagementTable.addColumn(startColumn, "Start");
        routeManagementTable.addColumn(endColumn, "End");
        routeManagementTable.addColumn(priceColumn, "Price USD");
        routeManagementTable.addColumn(descriptionColumn, "Description");
        routeManagementTable.addColumn(pickuptypeColumn, "Pickuptype");

        // Create a data provider.
        ListDataProvider<RouteInfo> dataProvider = new ListDataProvider<RouteInfo>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(routeManagementTable);

        // Add the data to the data provider, which automatically pushes it to the
        // widget.
        List<RouteInfo> list = dataProvider.getList();
        for (RouteInfo booking : ROUTES)
        {
            list.add(booking);
        }

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(routeManagementTable);

        // We know that the data is sorted alphabetically by default.
        // bookingManagementTable.getColumnSortList().push(forwardPickupPlaceColumn);
        routeManagementTable.getElement().getStyle().setMarginTop(2, Unit.PX);
        routeManagementTable.setWidth("100%");
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().getStyle().setWidth(100, Unit.PCT);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add((routeManagementTable));
        // panel.add(pager);
        ScrollPanel scrollPanel = new ScrollPanel(panel);
        scrollPanel.setHeight("400px");
        mainPanel.add(scrollPanel);
    }

    private Widget getUploader(final Button saveButton)
    {
        final FormPanel form = new FormPanel();
        form.setAction("/.gupld");

        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        // Create a panel to hold all of the form widgets.
        VerticalPanel panel = new VerticalPanel();
        form.setWidget(panel);

        // Create a FileUpload widget.
        FileUpload upload = new FileUpload();
        upload.setName("uploadFormElement");
        panel.add(upload);

        // Add a 'submit' button.
        panel.add(new Button("Upload file", new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                saveButton.setEnabled(false);
                form.submit();
            }
        }));

        form.addSubmitCompleteHandler(new SubmitCompleteHandler()
        {

            @Override
            public void onSubmitComplete(SubmitCompleteEvent event)
            {
                String[] parts = event.getResults().split(("\\*\\*\\*"));
                imageId = Long.parseLong(parts[1]);
                saveButton.setEnabled(true);
            }
        });

        return form;

    }

    class RouteAddEditClickHandler implements ClickHandler
    {
        RouteInfo.SaveMode mode;

        public RouteAddEditClickHandler(RouteInfo.SaveMode mode)
        {
            this.mode = mode;
        }

        @Override
        public void onClick(ClickEvent event)
        {
            switch (mode)
            {
                case UPDATE:
                    update(event);
                    break;
                case ADD:
                    add(event);
                    break;
            }
        }

        public void update(ClickEvent event)
        {
            int count = 0;
            for (RouteInfo p : ROUTES)
            {
                if (selectionModel.isSelected(p))
                    count++;
            }
            if (count == 1)
            {
                for (RouteInfo p : ROUTES)
                {
                    if (selectionModel.isSelected(p))
                    {
                        final Long routeId = p.getId();
                        showEditPopup(p, routeId, mode, event);

                    }
                }
            }
            else
                Window.alert("Select one row to edit");
        }

        public void add(ClickEvent event)
        {
            showEditPopup(new RouteInfo(), 0, mode, event);
        }

    }

    private void showEditPopup(RouteInfo ri, final long routeId, final SaveMode mode, ClickEvent event)
    {
        final PopupPanel editPlacePopUpPanel = new PopupPanel(true);
        final VerticalPanel vPanel = new VerticalPanel();
        Grid grid = new Grid(6, 2);
        vPanel.add(grid);
        final Label errLbl = new Label();
        errLbl.setStyleName("errLbl");
        final TextBox editStartTxtBox = new TextBox();
        final TextBox editEndTxtBox = new TextBox();
        final TextArea editDescriptionBox = new TextArea();
        final TextBox editPriceTxtBox = new TextBox();
        final ListBox editPickupTypeBox = new ListBox();

        editStartTxtBox.setText(ri.getStart());
        editEndTxtBox.setText(ri.getEnd());
        editDescriptionBox.setText(ri.getDescription());
        imageId = ri.getImage();

        editPriceTxtBox.setText(usdFormat.format(ri.getPrice()));
        int i = 0;
        for (RouteInfo.PickupType t : RouteInfo.PickupType.values())
        {
            editPickupTypeBox.addItem(t.name());
            if (t.equals(ri.getPickupType()))
            {
                editPickupTypeBox.setSelectedIndex(i);
            }
            i++;
        }

        Button saveBtn = new Button("Save");
        saveBtn.setStyleName("btn btn-primary");
        saveBtn.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                RouteInfo routeInfo = new RouteInfo();
                try
                {
                    String price = editPriceTxtBox.getText();
                    java.lang.Float priceL = java.lang.Float.parseFloat(price);
                    routeInfo.setPrice(priceL);
                }
                catch (Exception ex)
                {
                    errLbl.setText("Enter a price");
                    return;
                }
                if (
                Strings.isNullOrEmpty(editStartTxtBox.getText()) ||
                        Strings.isNullOrEmpty(editEndTxtBox.getText()) ||
                        Strings.isNullOrEmpty(editPriceTxtBox.getText())
                )
                {
                    errLbl.setText("Please fill up all fields");
                    return;
                }
                else
                {
                    routeInfo.setId(routeId);
                    routeInfo.setStart(editStartTxtBox.getText());
                    routeInfo.setEnd(editEndTxtBox.getText());
                    routeInfo.setDescription(editDescriptionBox.getText());
                    routeInfo.setPickupType(listPickupType[editPickupTypeBox.getSelectedIndex()]);

                    if (imageId != null)
                    {
                        routeInfo.setImage(imageId);
                    }

                    service.saveRoute(routeInfo, mode, new AsyncCallback<List<RouteInfo>>()
                    {

                        @Override
                        public void onFailure(Throwable caught)
                        {
                            Window.alert("Failed to save place!");
                        }

                        @Override
                        public void onSuccess(List<RouteInfo> routes)
                        {
                            editPlacePopUpPanel.setVisible(false);
                            initializeWidget();
                        }
                    });
                }
            }
        });
        // Setting up Popup Panel
        int row = 0;
        addPopupPanel("Start", editStartTxtBox, grid, row++);
        addPopupPanel("Destination", editEndTxtBox, grid, row++);
        addPopupPanel("Price USD", editPriceTxtBox, grid, row++);
        addPopupPanel("Pickuptype", editPickupTypeBox, grid, row++);
        addPopupPanel("Description", editDescriptionBox, grid, row++);

        addPopupPanel("Image", getUploader(saveBtn), grid, row++);

        editPlaceBtn.getElement().getStyle().setFloat(Float.RIGHT);
        vPanel.add(saveBtn);
        vPanel.add(errLbl);
        editPlacePopUpPanel.add(vPanel);
        editPlacePopUpPanel.addStyleName("addNewPlacePopup");
        editPlacePopUpPanel.setPopupPosition(event.getClientX(), event.getClientY());
        editPlacePopUpPanel.show();

    }
}