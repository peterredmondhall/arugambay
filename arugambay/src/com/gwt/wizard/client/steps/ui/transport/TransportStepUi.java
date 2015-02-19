package com.gwt.wizard.client.steps.ui.transport;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.GwtWizard;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.dashboard.ui.Helper;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.client.steps.ui.widget.RatingList;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.RatingInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.StatInfo;

public class TransportStepUi extends Composite
{
    // public static final String WIDTH = "120px";

    private static RouteStepUiUiBinder uiBinder = GWT.create(RouteStepUiUiBinder.class);
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    interface RouteStepUiUiBinder extends UiBinder<Widget, TransportStepUi>
    {
    }

    @UiField
    Panel mainPanel, ratingsPanel, dp, panelMotivation;

    @UiField
    Image imageVehicle;

    @UiField
    Panel routeSuggestionPanel, panelRoute;

    @UiField
    Label labelRouteName;

    @UiField
    Label labelDescription;

    private final Map<String, RouteInfo> mapRouteInfo = Maps.newHashMap();
    private final ScrollPanel sp = new ScrollPanel();
    private final FlowPanel fp = new FlowPanel();

    public TransportStepUi()
    {
        createUi();
        fetchRoutes();
        panelRoute.setVisible(false);
        sp.setHeight(getPanelHeight());
        ratingsPanel.add(sp);
        sp.add(fp);
        panelRoute.setHeight(getPanelHeight());

        FlexTable table = new FlexTable();
        table.setWidget(0, 0, getImage());
        table.setWidget(0, 1, new Label("Guaranteed refund"));
        table.setWidget(1, 0, getImage());
        table.setWidget(1, 1, new Label("Trusted driver"));
        table.setWidget(2, 0, getImage());
        table.setWidget(2, 1, new Label("Sharing function"));
        // containerGrid.setStyleName("progressbar-outer");

        panelMotivation.add(table);
    }

    private Image getImage()
    {
        Image image = new Image("images/big-tick.jpg");
        String size = "40px";
        image.setSize(size, size);
        return image;

    }

    protected void createUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
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

    public void show(boolean visible, Button prev, Button next)
    {
        this.next = next;
        next.setVisible(true);
        next.setEnabled(Wizard.ROUTEINFO != null);
        prev.setVisible(false);
    }

    private void fetchRoutes()
    {
        service.getRoutes(new AsyncCallback<List<RouteInfo>>()
        {

            @Override
            public void onSuccess(List<RouteInfo> routes)
            {
                MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();

                for (RouteInfo routeInfo : routes)
                {
                    String key = routeInfo.getKey(Helper.getDollars(routeInfo));
                    mapRouteInfo.put(key, routeInfo);

                    oracle.add(key);
                }
                final SuggestBox suggestBox = new SuggestBox(oracle);
                setSuggestBoxWidth(suggestBox);
                routeSuggestionPanel.add(suggestBox);
                suggestBox.getElement().setAttribute("placeHolder", "Enter a start or destination eg. Colombo or Arugam Bay");

                SelectionHandler<SuggestOracle.Suggestion> handler = new SelectionHandler<SuggestOracle.Suggestion>()
                {
                    @Override
                    public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event)
                    {
                        String displayString = event.getSelectedItem().getReplacementString();
                        RouteInfo routeInfo = mapRouteInfo.get(displayString);
                        Wizard.ROUTEINFO = routeInfo;
                        displayRoute(/* suggestBox, */);
                    }

                };

                suggestBox.addSelectionHandler(handler);
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to Server!");
            }
        });
    }

    public void displayRoute()
    {
        panelMotivation.setVisible(false);
        RouteInfo routeInfo = Wizard.ROUTEINFO;
        labelRouteName.setText(routeInfo.getKey(Helper.getDollars(routeInfo)));
        imageVehicle.setUrl("/imageservice?image=" + routeInfo.getImage());
        labelDescription.setText(routeInfo.getDescription());

        panelRoute.setVisible(true);
        next.setEnabled(true);
        GwtWizard.SERVICE.getBookingsForRoute(Wizard.ROUTEINFO, new AsyncCallback<List<BookingInfo>>()
        {
            @Override
            public void onSuccess(List<BookingInfo> list)
            {
                Wizard.EXISTING_BOOKINGS_ON_ROUTE = list;
                // suggestBox.getElement().setAttribute("placeHolder", "Enter a start or destination eg. Colombo or Arugam Bay");
            }

            @Override
            public void onFailure(Throwable caught)
            {
            }
        });

        GwtWizard.SERVICE.getRatings(Wizard.ROUTEINFO, new AsyncCallback<List<RatingInfo>>()
        {
            @Override
            public void onSuccess(List<RatingInfo> ratings)
            {
                if (ratings.size() > 0)
                {
                    fp.add(new RatingList(ratings).createRatingForm());
                }
            }

            @Override
            public void onFailure(Throwable caught)
            {
            }
        });
        GwtWizard.sendStat(routeInfo.getKey(""), StatInfo.Update.ROUTE);
    }

    private void setSuggestBoxWidth(SuggestBox suggestBox)
    {

        int suggestBoxWidth = 700;
        if (Wizard.MOBILE)
        {
            suggestBoxWidth = (int) (Wizard.SCREEN_WIDTH * 0.75);
        }
        suggestBox.setWidth(suggestBoxWidth + "px");

    }

    private String getPanelHeight()
    {
        int panelHeight = 200;
        if (Wizard.MOBILE)
        {
            panelHeight = (int) (Wizard.SCREEN_HEIGHT / 2 * 0.5);
        }

        return panelHeight + "px";
    }
}
