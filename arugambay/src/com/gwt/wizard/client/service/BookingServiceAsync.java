package com.gwt.wizard.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.StatInfo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface BookingServiceAsync
{
    void addBooking(BookingInfo bookingInfo, AsyncCallback<BookingInfo> callback)
            throws IllegalArgumentException;

    void sendShareRequest(BookingInfo bookingInfo, AsyncCallback<BookingInfo> callback);

    // void sendShareAccepted(List<BookingInfo> bookingInfo, AsyncCallback<BookingInfo> callback);

    void getRoutes(AsyncCallback<List<RouteInfo>> callback);

    void deleteRoute(RouteInfo placeInfo, AsyncCallback<List<RouteInfo>> callback)
            throws IllegalArgumentException;

    void saveRoute(RouteInfo placeInfo, RouteInfo.SaveMode mode, AsyncCallback<List<RouteInfo>> callback)
            throws IllegalArgumentException;

    void getBookings(AsyncCallback<List<BookingInfo>> callback);

    void getBookingsForTour(Long id, AsyncCallback<List<BookingInfo>> callback);

    void getBookingForTransaction(String ref, AsyncCallback<BookingInfo> callback);

    void getBookingsForShare(Long id, AsyncCallback<List<BookingInfo>> callback);

    void getUser(AsyncCallback<Boolean> callback);

    void getPaypalProfil(AsyncCallback<ProfilInfo> callback);

    void payWithStripe(String token, BookingInfo bookingInfo, AsyncCallback<BookingInfo> callback);

    void sendStat(StatInfo statInfo, AsyncCallback<Void> asyncCallback);

}
