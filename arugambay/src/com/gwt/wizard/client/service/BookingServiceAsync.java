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
    void save(BookingInfo bookingInfo, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void deletePlace(Long id, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void editPlace(Long id, PlaceInfo placeInfo, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void getBookings(AsyncCallback<List<BookingInfo>> callback);

    void getBooking(String ref, AsyncCallback<BookingInfo> callback);

    void getUser(AsyncCallback<Boolean> callback);

}
