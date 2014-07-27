package com.gwt.wizard.client.partyroute.steps.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.DirectionsRenderer;
import com.google.maps.gwt.client.DirectionsRendererOptions;
import com.google.maps.gwt.client.DirectionsRequest;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsService;
import com.google.maps.gwt.client.DirectionsStatus;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.TravelMode;
import com.gwt.wizard.client.core.Showable;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.client.suggest.ui.GoogleMapsSuggestBox;
import com.gwt.wizard.client.util.Utils;

public class FirstStepUi extends Composite implements Showable
{
    private static RouteStepUiUiBinder uiBinder = GWT.create(RouteStepUiUiBinder.class);
    private final BookingServiceAsync bookingService = GWT.create(BookingService.class);

    interface RouteStepUiUiBinder extends UiBinder<Widget, FirstStepUi>
    {
    }

    @UiField
    HTMLPanel mapContainer;

    @UiField
    HTMLPanel mainPanel;

    @UiField
    HorizontalPanel container;

    @UiField
    VerticalPanel addressContainer;

    @UiField
    Button calculateRoute;

    @UiField
    Button newAddress;

    @UiField
    HorizontalPanel btnMenu;

    @UiField
    SuggestBox destinationBox;

    @UiField
    SuggestBox firstPickupBox;

    private LatLng destination;

    private final List<LatLng> places = new ArrayList<>();

    private String destinationName;

    private final List<String> placeNames = new ArrayList<>();

    GoogleMap map;

    public FirstStepUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
        setupElements();
    }

    private void setupElements()
    {
        setUpBtnMenu();
        btnMenu.setVisible(false);
        createMap();

        destinationBox.addSelectionHandler(getSelectionHandler(destinationBox));
        firstPickupBox.addSelectionHandler(getSelectionHandler(firstPickupBox));

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

    @Override
    public void show(boolean visible)
    {
        // TODO Auto-generated method stub

    }

    private void createMap()
    {
        SimplePanel mapWidget = new SimplePanel();
        mapWidget.setSize("510px", "470px");
        MapOptions options = MapOptions.create();
        options.setCenter(LatLng.create(39.509, -98.434));
        options.setZoom(6);
        options.setMapTypeId(MapTypeId.ROADMAP);
        options.setDraggable(true);
        options.setMapTypeControl(true);
        options.setScaleControl(true);
        options.setScrollwheel(true);
        map = GoogleMap.create(mapWidget.getElement(), options);
        mapContainer.add(mapWidget);
    }

    private SelectionHandler<SuggestOracle.Suggestion> getSelectionHandler(final SuggestBox suggestBox)
    {
        SelectionHandler<SuggestOracle.Suggestion> handler = new SelectionHandler<SuggestOracle.Suggestion>()
        {

            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event)
            {
                {
                    if (suggestBox.getText() != null && suggestBox.getText().length() > 0)
                    {
                        final String address = suggestBox.getText();
                        GeocoderRequest request = GeocoderRequest.create();
                        request.setAddress(address);
                        Geocoder geoCoder = Geocoder.create();
                        geoCoder.geocode(request, new Geocoder.Callback()
                        {
                            @Override
                            public void handle(JsArray<GeocoderResult> a, GeocoderStatus b)
                            {
                                if (b == GeocoderStatus.OK)
                                {
                                    GeocoderResult result = a.shift();
                                    map.setCenter(result.getGeometry().getLocation());
                                    MarkerOptions markerOptions = MarkerOptions.create();
                                    markerOptions.setDraggable(false);
                                    Marker marker = Marker.create(markerOptions);
                                    marker.setPosition(result.getGeometry().getLocation());
                                    marker.setMap(map);
                                    if (suggestBox.equals(destinationBox))// destination == null)
                                    {
                                        destination = result.getGeometry().getLocation();
                                        destinationName = address;
                                    }
                                    else
                                    {
                                        places.add(result.getGeometry().getLocation());
                                        placeNames.add(address);
                                        btnMenu.setVisible(true);
                                    }
                                }
                                else
                                {
                                    // Window.alert("Something went wrong in google to get this location, check address");
                                }
                            }
                        });
                    }
                    else
                    {
                        Window.alert("Please Enter Place!");
                    }
                }
            }
        };

        return handler;
    }

    private void setUpBtnMenu()
    {
        newAddress.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                HorizontalPanel hp = new HorizontalPanel();
                Label pickupPlaceLbl = new Label("Pickup " + (places.size() + 1));
                pickupPlaceLbl.setStyleName("destinationlabel");
                GoogleMapsSuggestBox suggestBox = new GoogleMapsSuggestBox();
                hp.add(pickupPlaceLbl);
                hp.add(suggestBox);
                addressContainer.add(hp);
                suggestBox.addSelectionHandler(getSelectionHandler(suggestBox));

                btnMenu.setVisible(false);
            }
        });

        calculateRoute.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                if (destination == null)
                    Window.alert("Please enter destination");
                if (places.size() == 0)
                    Window.alert("Please enter pickup place/places");
                else if (places.size() > 0 && destination != null)
                {
                    List<LatLng> optimizedPlaces = new ArrayList<>();
                    Utils.getOpimimzedPlaces(optimizedPlaces, places, destination);
                    int select = 1;
                    for (final LatLng pickupPlace : optimizedPlaces)
                    {
                        final DirectionsRenderer directionsDisplay = DirectionsRenderer.create();
                        DirectionsRendererOptions options = DirectionsRendererOptions.create();
                        options.setMap(map);
                        directionsDisplay.setOptions(options);
                        DirectionsRequest request = DirectionsRequest.create();
                        request.setOrigin(pickupPlace);
                        if (select < optimizedPlaces.size())
                        {
                            request.setDestination(optimizedPlaces.get(select));
                            select = select + 2;
                        }
                        else
                        {
                            request.setDestination(destination);
                        }
                        request.setTravelMode(TravelMode.DRIVING);
                        DirectionsService.create().route(request, new com.google.maps.gwt.client.DirectionsService.Callback()
                        {
                            @Override
                            public void handle(DirectionsResult a, DirectionsStatus b)
                            {
                                if (b == DirectionsStatus.OK)
                                {
                                    directionsDisplay.setDirections(a);
                                    map.setZoom(16);
                                    map.setCenter(destination);
                                }
                                else
                                {
                                    Window.alert("Failed to fetch directions from server!");
                                }
                            }
                        });
                    }
                    addressContainer.clear();
                    HorizontalPanel hp;
                    for (int i = 0; i < placeNames.size(); i++)
                    {
                        hp = new HorizontalPanel();
                        hp.addStyleName("addressContainerStyle");
                        HTML pickup = new HTML("Pickup " + (i + 1));
                        pickup.setStyleName("destinationlabel");
                        hp.add(pickup);
                        hp.add(new HTML(placeNames.get(i)));
                        addressContainer.add(hp);
                    }
                    hp = new HorizontalPanel();
                    hp.addStyleName("addressContainerStyle");
                    HTML destination = new HTML("Destination");
                    destination.setStyleName("destinationlabel");
                    hp.add(destination);
                    hp.add(new HTML(destinationName));
                    addressContainer.add(hp);
                    btnMenu.setVisible(false);
                }
            }
        });
    }
}
