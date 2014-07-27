package com.gwt.wizard.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface BookingServiceAsync
{
    void saveWizardData(BookingInfo bookingInfo, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void getPlaceList(AsyncCallback<List<String>> callback);

    void savePlace(PlaceInfo place, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void deletePlace(Long id, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void editPlace(Long id, PlaceInfo placeInfo, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void getPlaces(AsyncCallback<List<PlaceInfo>> callback);

    void getBookings(AsyncCallback<List<BookingInfo>> callback);

    void getUser(AsyncCallback<Boolean> callback);

}
