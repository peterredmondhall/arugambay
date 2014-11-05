package com.gwt.wizard.client.steps.ui;

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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Wizard;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
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
    HTMLPanel mainPanel;

    @UiField
    Image imageVehicle;

    @UiField
    Panel routeSuggestionPanel, panelRoute;

//    @UiField(provided = true)
//    SuggestBox suggestBox;

    @UiField
    Label labelRouteName, labelPrice, labelDescription;

    // private final List<RouteInfo> routes = Lists.newArrayList();

    private final Map<String, RouteInfo> mapRouteInfo = Maps.newHashMap();

    public TransportStepUi()
    {

        initWidget(uiBinder.createAndBindUi(this));
        fetchRoutes();
        // routeSuggestion.addSelectionHandler(getSelectionHandler(routeSuggestion));
        panelRoute.setVisible(false);

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
        next.setEnabled(false);
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
                SuggestBox suggestBox = new SuggestBox(oracle);
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
                        labelPrice.setText(java.lang.Float.toString(routeInfo.getPrice()));
                        imageVehicle.setUrl("/imageservice?image=" + routeInfo.getImage());
                        labelDescription.setText(routeInfo.getDescription());
                        panelRoute.setVisible(true);
                        Wizard.ROUTEINFO = routeInfo;
                        next.setEnabled(true);

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
