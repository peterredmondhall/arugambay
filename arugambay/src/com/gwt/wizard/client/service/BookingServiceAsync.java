package com.gwt.wizard.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.wizard.shared.model.AgentInfo;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ContractorInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.RatingInfo;
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

    void getRoutes(AgentInfo userInfo, AsyncCallback<List<RouteInfo>> callback);

    void getContractors(AgentInfo userInfo, AsyncCallback<List<ContractorInfo>> callback);

    void getRoutes(AsyncCallback<List<RouteInfo>> callback);

    void deleteRoute(AgentInfo userInfo, RouteInfo placeInfo, AsyncCallback<List<RouteInfo>> callback)
            throws IllegalArgumentException;

    void deleteContractor(AgentInfo userInfo, ContractorInfo placeInfo, AsyncCallback<List<ContractorInfo>> callback)
            throws IllegalArgumentException;

    void saveRoute(AgentInfo userInfo, RouteInfo placeInfo, RouteInfo.SaveMode mode, AsyncCallback<List<RouteInfo>> callback)
            throws IllegalArgumentException;

    void saveContractor(AgentInfo userInfo, ContractorInfo placeInfo, ContractorInfo.SaveMode mode, AsyncCallback<List<ContractorInfo>> callback)
            throws IllegalArgumentException;

    void getBookings(AsyncCallback<List<BookingInfo>> callback);

    void getBookingsForRoute(RouteInfo id, AsyncCallback<List<BookingInfo>> callback);

    void getRatings(RouteInfo routeInfo, AsyncCallback<List<RatingInfo>> callback);

    void addRating(RatingInfo statInfo, AsyncCallback<Void> asyncCallback);

    void getBookingForTransaction(String ref, AsyncCallback<BookingInfo> callback);

    void handleShareAccepted(Long id, AsyncCallback<List<BookingInfo>> callback);

    void getUser(AsyncCallback<AgentInfo> callback);

    void getPaypalProfil(AsyncCallback<ProfilInfo> callback);

    void payWithStripe(String token, BookingInfo bookingInfo, AsyncCallback<BookingInfo> callback);

    void sendStat(StatInfo statInfo, AsyncCallback<Void> asyncCallback);

    void createDefaultUser(AsyncCallback<AgentInfo> asyncCallback);

}
