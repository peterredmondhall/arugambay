package com.gwt.wizard.client.steps.ui;

import static com.gwt.wizard.client.core.Wizard.EXISTING_BOOKINGS_ON_ROUTE;

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
    public static final String WIDTH = "120px";

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

//    @UiField(provided = true)
//    SuggestBox suggestBox;

    @UiField
    Label labelRouteName, labelPrice;

    @UiField
    Label labelDescription;

    // private final List<RouteInfo> routes = Lists.newArrayList();

    private final Map<String, RouteInfo> mapRouteInfo = Maps.newHashMap();
    private final ScrollPanel sp = new ScrollPanel();
    private final FlowPanel fp = new FlowPanel();

    public TransportStepUi()
    {

        initWidget(uiBinder.createAndBindUi(this));
        fetchRoutes();
        // routeSuggestion.addSelectionHandler(getSelectionHandler(routeSuggestion));
        panelRoute.setVisible(false);
        sp.setHeight("100px");
        ratingsPanel.add(sp);
        sp.add(fp);

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

    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        this.next = next;
        next.setVisible(true);
        next.setEnabled(EXISTING_BOOKINGS_ON_ROUTE != null);
        prev.setVisible(false);
        cancel.setText("Cancel");
    }

    // List<Integer> numbers = Lists.newArrayList(1, 2, 3, 6, 10, 34, 57, 89);

//    List<Integer> evenNumbers = Lists.newArrayList(Collections2.filter(numbers, acceptEven));
//    Integer found = Collections.binarySearch(evenNumbers, 57);
//    assertThat(found, lessThan(0));

//    private SelectionHandler<SuggestOracle.Suggestion> getSelectionHandler(final SuggestBox suggestBox)
//    {
//        SelectionHandler<SuggestOracle.Suggestion> handler = new SelectionHandler<SuggestOracle.Suggestion>()
//        {
//
//            @Override
//            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event)
//            {
//                {
//                    final String entry = routeSuggestion.getText();
//                    if (entry != null && entry.length() > 0)
//                    {
//                        Predicate<RouteInfo> acceptMatches = new Predicate<RouteInfo>()
//                        {
//
//                            @Override
//                            public boolean apply(RouteInfo routeInfo)
//                            {
//                                return routeInfo.getStart().contains(entry) || routeInfo.getEnd().contains(entry);
//                            }
//
//                        };
//                        List<RouteInfo> visibleList = Lists.newArrayList(Collections2.filter(routes, acceptMatches));
//                        // suggestBox.getSuggestOracle().
//
//                    }
//                    else
//                    {
//                        Window.alert("Please enter your start or destinatin!");
//                    }
//                }
//            }
//        };
//
//        return handler;
//    }

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
                suggestBox.setWidth("600px");
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
                        GwtWizard.SERVICE.getBookingsForRoute(Wizard.ROUTEINFO, new AsyncCallback<List<BookingInfo>>()
                        {
                            @Override
                            public void onSuccess(List<BookingInfo> list)
                            {
                                Wizard.EXISTING_BOOKINGS_ON_ROUTE = list;
                                next.setEnabled(true);
                                suggestBox.getElement().setAttribute("placeHolder", "Enter a start or destination eg. Colombo or Arugam Bay");
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

                                fp.add(new RatingList(ratings).createAdvancedForm());
//                                    DecoratorPanel dp = new DecoratorPanel();
//                                    HorizontalPanel hp = new HorizontalPanel();
//                                    Label stars = new Label("*****");
//                                    Label 
//                                    dp.add(new Label(ri.getCritic()));
//                                    fp.add(dp);

                            }

                            @Override
                            public void onFailure(Throwable caught)
                            {
                            }
                        });

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
