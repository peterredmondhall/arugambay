package com.gwt.wizard.client.steps.ui.transport;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
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
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.client.steps.ui.widget.RatingList;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.RatingInfo;
import com.gwt.wizard.shared.model.RouteInfo;

public class TransportStepUi extends Composite
{
    // public static final String WIDTH = "120px";

    private static RouteStepUiUiBinder uiBinder = GWT.create(RouteStepUiUiBinder.class);
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    interface RouteStepUiUiBinder extends UiBinder<Widget, TransportStepUi>
    {
    }

    @UiField
    Panel mainPanel, ratingsPanel, dp;

    @UiField
    Image imageVehicle;

    @UiField
    Panel routeSuggestionPanel, panelRoute;

    @UiField
    Label labelRouteName, labelPrice;

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
        sp.setHeight("100px");
        ratingsPanel.add(sp);
        sp.add(fp);

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
        next.setEnabled(Wizard.RATINGINFO != null);
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
                    mapRouteInfo.put(routeInfo.getKey(), routeInfo);
                    oracle.add(routeInfo.getKey());
                }
                final SuggestBox suggestBox = new SuggestBox(oracle);
                suggestBox.setWidth(Wizard.MOBILE ? "300px" : "500px");
                routeSuggestionPanel.add(suggestBox);
                suggestBox.getElement().setAttribute("placeHolder", "Enter a start or destination eg. Colombo or Arugam Bay");

                SelectionHandler<SuggestOracle.Suggestion> handler = new SelectionHandler<SuggestOracle.Suggestion>()
                {
                    @Override
                    public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event)
                    {
                        String displayString = event.getSelectedItem().getReplacementString();
                        RouteInfo routeInfo = mapRouteInfo.get(displayString);
                        labelRouteName.setText(routeInfo.getKey());
                        if (routeInfo.getCents() != null)
                        {
                            NumberFormat usdFormat = NumberFormat.getFormat(".00");
                            Double d = (double) routeInfo.getCents() / 100;
                            labelPrice.setText("$" + usdFormat.format(d));
                        }
                        imageVehicle.setUrl("/imageservice?image=" + routeInfo.getImage());
                        labelDescription.setText(routeInfo.getDescription());

                        panelRoute.setVisible(true);
                        Wizard.ROUTEINFO = routeInfo;
                        next.setEnabled(true);
                        GwtWizard.SERVICE.getBookingsForRoute(Wizard.ROUTEINFO, new AsyncCallback<List<BookingInfo>>()
                        {
                            @Override
                            public void onSuccess(List<BookingInfo> list)
                            {
                                Wizard.EXISTING_BOOKINGS_ON_ROUTE = list;
                                suggestBox.getElement().setAttribute("placeHolder", "Enter a start or destination eg. Colombo or Arugam Bay");
                            }

                            @Override
                            public void onFailure(Throwable caught)
                            {
                            }
                        });

                        if (false)
                        {
                            GwtWizard.SERVICE.getRatings(Wizard.ROUTEINFO, new AsyncCallback<List<RatingInfo>>()
                            {
                                @Override
                                public void onSuccess(List<RatingInfo> ratings)
                                {

                                    fp.add(new RatingList(ratings).createAdvancedForm());

                                }

                                @Override
                                public void onFailure(Throwable caught)
                                {
                                }
                            });
                        }

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
}
