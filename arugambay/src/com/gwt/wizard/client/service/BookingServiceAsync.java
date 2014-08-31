package com.gwt.wizard.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.StatInfo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface BookingServiceAsync
{
    void save(BookingInfo bookingInfo, AsyncCallback<BookingInfo> callback)
            throws IllegalArgumentException;

    void sendShareRequest(BookingInfo bookingInfo, AsyncCallback<BookingInfo> callback);

    // void sendShareAccepted(List<BookingInfo> bookingInfo, AsyncCallback<BookingInfo> callback);

    void deletePlace(Long id, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void editPlace(Long id, PlaceInfo placeInfo, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    void getBookings(AsyncCallback<List<BookingInfo>> callback);

    void getBookingsForTour(String ref, AsyncCallback<List<BookingInfo>> callback);

    void getBookingForTransaction(String ref, AsyncCallback<BookingInfo> callback);

    void getBookingsForShare(String ref, AsyncCallback<List<BookingInfo>> callback);

    void getUser(AsyncCallback<Boolean> callback);

    void getPaypalProfil(AsyncCallback<ProfilInfo> callback);

    void sendStat(StatInfo statInfo, AsyncCallback<Void> asyncCallback);

}
