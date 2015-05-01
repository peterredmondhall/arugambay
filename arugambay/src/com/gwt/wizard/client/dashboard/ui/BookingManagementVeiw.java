package com.gwt.wizard.client.dashboard.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.FluentIterable;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.gwt.wizard.client.GwtDashboard;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.shared.OrderStatus;
import com.gwt.wizard.shared.model.BookingInfo;

public class BookingManagementVeiw extends Composite
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);
    private static DateTimeFormat sdf = DateTimeFormat.getFormat("dd.MM.yyyy");

    private static BookingManagementVeiwUiBinder uiBinder = GWT.create(BookingManagementVeiwUiBinder.class);

    interface BookingManagementVeiwUiBinder extends UiBinder<Widget, BookingManagementVeiw>
    {
    }

    private final CellTable.Resources tableRes = GWT.create(TableRes.class);
    private final List<BookingInfo> BOOKINGS = new ArrayList<>();

    public class BookingInfoComparator implements Comparator<BookingInfo>
    {

        @Override
        public int compare(BookingInfo bi1, BookingInfo bi2)
        {
            return (bi1.getDate().after(bi2.getDate())) ? 1 : -1;
        }
    }

    CellTable<BookingInfo> bookingManagementTable = new CellTable<BookingInfo>(8, tableRes);

    @UiField
    HTMLPanel mainPanel;
    private final SelectionModel<BookingInfo> selectionModel = new MultiSelectionModel<BookingInfo>(null);

    // The list of data to display.

    public BookingManagementVeiw()
    {
        initWidget(uiBinder.createAndBindUi(this));
        fetchBookings();

    }

    private void fetchBookings()
    {
        service.getBookings(GwtDashboard.getAgentInfo(), new AsyncCallback<List<BookingInfo>>()
        {

            @Override
            public void onSuccess(List<BookingInfo> result)
            {
                if (result != null && result.size() > 0)
                {
                    for (BookingInfo booking : result)
                    {
                        if (booking.getStatus().equals(OrderStatus.PAID))
                        {
                            BOOKINGS.add(booking);
                        }
                    }
                }
                setCellTable();
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to Server!");
            }
        });
    }

    private void setCellTable()
    {
        bookingManagementTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<BookingInfo>createCheckboxManager());
        // Checkbox
        Column<BookingInfo, Boolean> checkColumn = new Column<BookingInfo, Boolean>(
                new CheckboxCell(true, false))
        {
            @Override
            public Boolean getValue(BookingInfo object)
            {
                return selectionModel.isSelected(object);
            }
        };

        // Create date column.
        TextColumn<BookingInfo> dateColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return sdf.format(booking.getDate());
            }
        };

        // Create pax column.
        TextColumn<BookingInfo> paxColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return String.valueOf(booking.getPax());
            }
        };
        // Create requirements column.
        TextColumn<BookingInfo> requirementsColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getRequirements();
            }
        };

        TextColumn<BookingInfo> nameColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getName();
            }
        };
        TextColumn<BookingInfo> emailColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getEmail();
            }
        };

        TextColumn<BookingInfo> routeColumn = new TextColumn<BookingInfo>()
        {
            @Override
            public String getValue(BookingInfo booking)
            {
                return booking.getRouteInfo().getKey("");
            }
        };

//        private OrderStatus status;
//        private String flightNo;
//        private String landingTime;

        bookingManagementTable.setTableLayoutFixed(true);
        // Add the columns.
        bookingManagementTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        bookingManagementTable.addColumn(dateColumn, "Date");

        bookingManagementTable.addColumn(nameColumn, "Name");
        bookingManagementTable.addColumn(emailColumn, "Email");
        bookingManagementTable.addColumn(paxColumn, "No. passengers");
        bookingManagementTable.addColumn(requirementsColumn, "Requirements");
        bookingManagementTable.addColumn(routeColumn, "Route");

        bookingManagementTable.setColumnWidth(checkColumn, 40, Unit.PX);
        bookingManagementTable.setColumnWidth(dateColumn, 65, Unit.PX);
        bookingManagementTable.setColumnWidth(requirementsColumn, 200, Unit.PX);
        bookingManagementTable.setColumnWidth(paxColumn, 90, Unit.PX);
        bookingManagementTable.setColumnWidth(nameColumn, 160, Unit.PX);

        // Create a data provider.
        ListDataProvider<BookingInfo> dataProvider = new ListDataProvider<BookingInfo>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(bookingManagementTable);

        dataProvider.setList(
                FluentIterable
                        .from(BOOKINGS)
                        .toSortedList(new BookingInfoComparator()));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(bookingManagementTable);

        // We know that the data is sorted alphabetically by default.
        // bookingManagementTable.getColumnSortList().push(forwardPickupPlaceColumn);
        bookingManagementTable.getElement().getStyle().setMarginTop(2, Unit.PX);
        bookingManagementTable.setWidth("100%");
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().getStyle().setWidth(100, Unit.PCT);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add(bookingManagementTable);
        panel.add(pager);
        mainPanel.add(panel);
    }
}
