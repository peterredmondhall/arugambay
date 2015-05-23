package com.gwt.wizard.client.dashboard.ui;

import java.util.Comparator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.gwt.wizard.client.GwtDashboard;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.shared.model.FinanceInfo;

public class FinanceManagementVeiw extends Composite
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);
    private static DateTimeFormat sdf = DateTimeFormat.getFormat("dd.MM.yyyy");

    private static FinanceManagementVeiwUiBinder uiBinder = GWT.create(FinanceManagementVeiwUiBinder.class);

    interface FinanceManagementVeiwUiBinder extends UiBinder<Widget, FinanceManagementVeiw>
    {
    }

    private final CellTable.Resources tableRes = GWT.create(TableRes.class);
    private final List<FinanceInfo> FINANCES = Lists.newArrayList();

    public class FinanceInfoComparator implements Comparator<FinanceInfo>
    {

        @Override
        public int compare(FinanceInfo bi1, FinanceInfo bi2)
        {
            return (bi1.getDate().after(bi2.getDate())) ? -1 : 1;
        }
    }

    CellTable<FinanceInfo> summaryTable = new CellTable<FinanceInfo>(20, tableRes);
    NumberFormat usdFormat = NumberFormat.getFormat(".00");

    @UiField
    HTMLPanel mainPanel;
    private final SelectionModel<FinanceInfo> selectionModel = new MultiSelectionModel<FinanceInfo>(null);

    // The list of data to display.

    public FinanceManagementVeiw()
    {
        initWidget(uiBinder.createAndBindUi(this));
        fetchFinancess();

    }

    private void fetchFinancess()
    {
        service.getFinances(GwtDashboard.getAgentInfo(), new AsyncCallback<List<FinanceInfo>>()
        {

            @Override
            public void onSuccess(List<FinanceInfo> result)
            {
                if (result != null && result.size() > 0)
                {
                    for (FinanceInfo financeInfo : result)
                    {
                        FINANCES.add(financeInfo);
                    }
                }
                setSummaryCellTable();
                setTransferCellTable();
                setPaymentsCellTable();
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to Server!");
            }
        });
    }

    private CellTable<FinanceInfo> setPaymentsCellTable()
    {
        CellTable<FinanceInfo> table = new CellTable<FinanceInfo>(20, tableRes);

        table.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<FinanceInfo>createCheckboxManager());

        // Create date column.
        TextColumn<FinanceInfo> dateColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo booking)
            {
                return sdf.format(booking.getDate());
            }
        };

        // Create requirements column.
        TextColumn<FinanceInfo> typeColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo booking)
            {
                return booking.getType().name();
            }
        };

        // Create requirements column.
        TextColumn<FinanceInfo> priceColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo booking)
            {
                if (booking.getAmount() != null)
                {
                    Double d = (double) booking.getAmount() / 100;
                    return usdFormat.format(d);
                }
                return "error";
            }
        };

        TextColumn<FinanceInfo> orderColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo info)
            {
                return info.getOrder();
            }
        };

        TextColumn<FinanceInfo> nameColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo info)
            {
                return info.getName();
            }
        };

        table.setTableLayoutFixed(true);
        // Add the columns.
        table.addColumn(typeColumn, "Type");
        table.addColumn(orderColumn, "Order");
        table.addColumn(nameColumn, "Name");
        table.addColumn(dateColumn, "Date");
        table.addColumn(priceColumn, "Amount USD");

        // Create a data provider.
        ListDataProvider<FinanceInfo> dataProvider = new ListDataProvider<FinanceInfo>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(table);

        // Add the data to the data provider, which automatically pushes it to the
        // widget.
        setData(dataProvider, FinanceInfo.Type.PAYMENT);

        addTable(table, "400px");
        return table;
    }

    private CellTable<FinanceInfo> setSummaryCellTable()
    {
        CellTable<FinanceInfo> paymentsTable = new CellTable<FinanceInfo>(20, tableRes);

        paymentsTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<FinanceInfo>createCheckboxManager());

        // Create requirements column.
        TextColumn<FinanceInfo> priceColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo booking)
            {
                if (booking.getAmount() != null)
                {
                    Double d = (double) booking.getAmount() / 100;
                    return usdFormat.format(d);
                }
                return "error";
            }
        };

        TextColumn<FinanceInfo> descColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo info)
            {
                return info.getName();
            }
        };

        paymentsTable.setTableLayoutFixed(true);
        // Add the columns.
        paymentsTable.addColumn(descColumn, "");
        paymentsTable.addColumn(priceColumn, "Balance USD");

        // Create a data provider.
        ListDataProvider<FinanceInfo> dataProvider = new ListDataProvider<FinanceInfo>();

        // Connect the table to the data provider.

        dataProvider.addDataDisplay(paymentsTable);

        FinanceInfo payments = new FinanceInfo();
        payments.setName("Total payments");
        Long paymentsAmt = 0L;
        FinanceInfo transfers = new FinanceInfo();
        transfers.setName("Total transfers");
        Long transfersAmt = 0L;
        List<FinanceInfo> summary = Lists.newArrayList();
        FinanceInfo balance = new FinanceInfo();
        balance.setName("Balance");
        summary.add(payments);
        summary.add(transfers);
        summary.add(balance);

        for (FinanceInfo financeInfo : FINANCES)
        {
            switch (financeInfo.getType())
            {
                case PAYMENT:
                    paymentsAmt += financeInfo.getAmount();
                    break;
                case TRANSFER:
                    transfersAmt += financeInfo.getAmount();
                    break;
                default:
                    break;

            }
        }
        payments.setAmount(paymentsAmt);
        transfers.setAmount(transfersAmt);
        balance.setAmount(paymentsAmt - transfersAmt);
        // Add the data to the data provider, which automatically pushes it to the
        // widget.
        dataProvider.setList(summary);

        addTable(paymentsTable, "100px");

        return paymentsTable;
    }

    private void setData(ListDataProvider<FinanceInfo> dataProvider, final FinanceInfo.Type type)
    {
        Predicate<FinanceInfo> predicate = new Predicate<FinanceInfo>()
        {
            @Override
            public boolean apply(FinanceInfo input)
            {
                return type.equals(input.getType());
            }
        };
        dataProvider.setList(
                FluentIterable
                        .from(FINANCES)
                        .filter(predicate)
                        .toSortedList(new FinanceInfoComparator()));
    }

    private void setTransferCellTable()
    {
        CellTable<FinanceInfo> table = new CellTable<FinanceInfo>(10, tableRes);

        table.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<FinanceInfo>createCheckboxManager());

        // Create date column.
        TextColumn<FinanceInfo> dateColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo booking)
            {
                return sdf.format(booking.getDate());
            }
        };

        TextColumn<FinanceInfo> priceColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo booking)
            {
                if (booking.getAmount() != null)
                {
                    Double d = (double) booking.getAmount() / 100;
                    return usdFormat.format(d);
                }
                return "error";
            }
        };

        TextColumn<FinanceInfo> nameColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo info)
            {
                return "";
            }
        };
        // Create requirements column.
        TextColumn<FinanceInfo> typeColumn = new TextColumn<FinanceInfo>()
        {
            @Override
            public String getValue(FinanceInfo booking)
            {
                return booking.getType().name();
            }
        };

        table.setTableLayoutFixed(true);
        // Add the columns.
        table.addColumn(typeColumn, "Type");
        table.addColumn(dateColumn, "Date");
        table.addColumn(priceColumn, "Amount USD");

        // Create a data provider.
        ListDataProvider<FinanceInfo> dataProvider = new ListDataProvider<FinanceInfo>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(table);

        // Add the data to the data provider, which automatically pushes it to the
        // widget.
        setData(dataProvider, FinanceInfo.Type.TRANSFER);

        addTable(table, "200px");
    }

    private void addTable(CellTable<FinanceInfo> table, String height)
    {
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(table);

        // We know that the data is sorted alphabetically by default.
        // bookingManagementTable.getColumnSortList().push(forwardPickupPlaceColumn);
        table.getElement().getStyle().setMarginTop(2, Unit.PX);
        table.setWidth("100%");
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().getStyle().setWidth(100, Unit.PCT);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add(table);
        ScrollPanel scrollPanel = new ScrollPanel(panel);
        scrollPanel.setHeight(height);
        mainPanel.add(scrollPanel);
    }
}
