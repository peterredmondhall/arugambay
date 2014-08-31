package com.gwt.wizard.client.steps.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.gwt.wizard.client.GwtWizard;
import com.gwt.wizard.client.core.Showable;
import com.gwt.wizard.shared.OrderType;
import com.gwt.wizard.shared.model.BookingInfo;

public class ShareStepUi extends Composite implements Showable
{

    private static ShareStepUiUiBinder uiBinder = GWT.create(ShareStepUiUiBinder.class);

    // private static DateTimeFormat sdf = DateTimeFormat.getFormat("dd.MM.yyyy");

    interface ShareStepUiUiBinder extends UiBinder<Widget, ShareStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;

    @UiField
    VerticalPanel sharePanel, noSharePanel;

    @UiField
    ScrollPanel scrollPanel;

    private final BookingInfo bookingInfo;
    private CellTable<BookingInfo> cellTable;
    private List<BookingInfo> bookingList;

    private Map<String, BookingInfo> shareMap;
    private GwtWizard gwtWizard;

    public ShareStepUi(BookingInfo bookingInfo)
    {
        initWidget(uiBinder.createAndBindUi(this));
        this.bookingInfo = bookingInfo;
        sharePanel.setVisible(false);
        noSharePanel.setVisible(false);
    }

    public void setBookingList(List<BookingInfo> bookingList, GwtWizard gwtWizard)
    {
        this.bookingList = bookingList;
        this.gwtWizard = gwtWizard;
        boolean noShare = bookingList.size() == 0;

        showShareNoShare();

        if (!noShare && cellTable == null)
        {
            cellTable = new CellTable<BookingInfo>();
            shareMap = new HashMap<>();
            for (BookingInfo bookingInfo : bookingList)
            {
                shareMap.put(bookingInfo.getRef(), bookingInfo);
            }
            fillTable();
            scrollPanel.add(cellTable);
        }
    }

    private void fillTable()
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
                    BookingInfo bookingToShare = shareMap.get(selected.getRef());

                    bookingInfo.setDate(bookingToShare.getDate());
                    bookingInfo.setFlightNo(bookingToShare.getFlightNo());
                    bookingInfo.setLandingTime(bookingToShare.getLandingTime());
                    bookingInfo.setOrderType(OrderType.SHARE);
                    bookingInfo.setParentRef(bookingToShare.getRef());
                    gwtWizard.shareBooking(bookingToShare);
                    bookingInfo.setOrderType(OrderType.SHARE);

                    scrollPanel.remove(cellTable);

                }
            }
        });

        // Set the total row count. This isn't strictly necessary, but it affects
        // paging calculations, so its good habit to keep the row count up to date.
        cellTable.setRowCount(bookingList.size(), true);

        // Push the data into the widget.
        cellTable.setRowData(0, bookingList);

    }

    @Override
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        showShareNoShare();

        next.setVisible(true);
        prev.setEnabled(true);
        if (bookingList != null)
        {
            cellTable = new CellTable<BookingInfo>();
            fillTable();
            scrollPanel.add(cellTable);
        }

    }

    private void showShareNoShare()
    {
        if (bookingList == null)
        {

            sharePanel.setVisible(false);
            noSharePanel.setVisible(false);
        }
        else
        {
            boolean shareAvailable = bookingList.size() > 0;

            sharePanel.setVisible(shareAvailable);
            noSharePanel.setVisible(!shareAvailable);
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
        }
    }
}
