package com.gwt.wizard.client.steps.ui;

import static com.gwt.wizard.client.core.Wizard.BOOKINGINFO;
import static com.gwt.wizard.client.core.Wizard.EXISTING_BOOKINGS_ON_ROUTE;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.BookingInfo;

public class ShareStepUi extends Composite
{

    private static ShareStepUiUiBinder uiBinder = GWT.create(ShareStepUiUiBinder.class);

    interface ShareStepUiUiBinder extends UiBinder<Widget, ShareStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    @UiField
    VerticalPanel sharePanel, noSharePanel;

    @UiField
    ScrollPanel scrollPanel;

    private CellTable<BookingInfo> cellTable;

    private Map<Long, BookingInfo> shareMap;
    private final Wizard wizard;

    public ShareStepUi(Wizard wizard)
    {
        initWidget(uiBinder.createAndBindUi(this));
        sharePanel.setVisible(false);
        noSharePanel.setVisible(false);
        this.wizard = wizard;
    }

    private void fillTable(String flightNoHote, String landingTimePickup)
    {
        cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

        DateCell dateCell = new DateCell();
        Column<BookingInfo, Date> dateColumn = new Column<BookingInfo, Date>(dateCell)
        {
            @Override
            public Date getValue(BookingInfo object)
            {
                return object.getDate();
            }
        };
        cellTable.addColumn(dateColumn, "Date");

        TextColumn<BookingInfo> flightNoColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo object)
            {
                return object.getFlightNo();
            }
        };
        cellTable.addColumn(flightNoColumn, "Flight no.");

        TextColumn<BookingInfo> landingTimeColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo object)
            {
                return object.getLandingTime();
            }
        };
        cellTable.addColumn(landingTimeColumn, "Landing time");

        // Add a text column to show the address.

        // Add a selection model to handle user selection.
        final SingleSelectionModel<BookingInfo> selectionModel = new SingleSelectionModel<BookingInfo>();
        cellTable.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
        {
            @Override
            public void onSelectionChange(SelectionChangeEvent event)
            {
                BookingInfo selected = selectionModel.getSelectedObject();
                if (selected != null)
                {
                    BookingInfo bookingToShare = shareMap.get(selected.getId());

                    BOOKINGINFO.setDate(bookingToShare.getDate());
                    BOOKINGINFO.setFlightNo(bookingToShare.getFlightNo());
                    BOOKINGINFO.setLandingTime(bookingToShare.getLandingTime());
                    BOOKINGINFO.setOrderType(OrderType.SHARE);
                    BOOKINGINFO.setParentId(bookingToShare.getId());

                    scrollPanel.remove(cellTable);
                    wizard.onNextClick(null);

                }
            }
        });

        // Set the total row count. This isn't strictly necessary, but it affects
        // paging calculations, so its good habit to keep the row count up to date.
        cellTable.setRowCount(EXISTING_BOOKINGS_ON_ROUTE.size(), true);

        // Push the data into the widget.
        cellTable.setRowData(0, EXISTING_BOOKINGS_ON_ROUTE);

    }

    public void show(boolean visible, Button prev, Button next)
    {

        next.setVisible(true);
        prev.setEnabled(true);
        prev.setVisible(true);
        BOOKINGINFO.setOrderType(OrderType.BOOKING);

        showShareNoShare();
    }

    private void showShareNoShare()
    {
        boolean shareAvailable = EXISTING_BOOKINGS_ON_ROUTE.size() > 0;

        sharePanel.setVisible(shareAvailable);
        noSharePanel.setVisible(!shareAvailable);

        if (shareAvailable && cellTable == null)
        {
            cellTable = new CellTable<BookingInfo>();
            shareMap = new HashMap<>();
            for (BookingInfo bookingInfo : EXISTING_BOOKINGS_ON_ROUTE)
            {
                shareMap.put(bookingInfo.getId(), bookingInfo);
            }
            fillTable(Wizard.ROUTEINFO.getPickupType().getLocationType(), Wizard.ROUTEINFO.getPickupType().getTimeType());
            scrollPanel.add(cellTable);
        }
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

    public void removeTable()
    {
        if (cellTable != null)
        {
            scrollPanel.remove(cellTable);
            cellTable = null;
        }
    }
}
