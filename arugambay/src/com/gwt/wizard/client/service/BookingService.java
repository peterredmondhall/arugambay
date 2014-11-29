package com.gwt.wizard.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.RatingInfo;
import com.gwt.wizard.shared.model.RouteInfo;
import com.gwt.wizard.shared.model.StatInfo;
import com.gwt.wizard.shared.model.AgentInfo;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface BookingService extends RemoteService
{
    BookingInfo addBooking(BookingInfo model) throws IllegalArgumentException;

    BookingInfo sendShareRequest(BookingInfo bookingInfo);

    // BookingInfo sendShareAccepted(List<BookingInfo> l);

    AgentInfo getUser() throws IllegalArgumentException;

    List<RouteInfo> getRoutes(AgentInfo userInfo) throws IllegalArgumentException;

    List<RouteInfo> getRoutes() throws IllegalArgumentException;

    List<RouteInfo> deleteRoute(AgentInfo userInfo, RouteInfo placeInfo) throws IllegalArgumentException;

    List<RouteInfo> saveRoute(AgentInfo userInfo, RouteInfo placeInfo, RouteInfo.SaveMode mode) throws IllegalArgumentException;

    List<BookingInfo> getBookings() throws IllegalArgumentException;

    List<BookingInfo> getBookingsForRoute(RouteInfo routeInfo) throws IllegalArgumentException;

    List<RatingInfo> getRatings(RouteInfo routeInfo) throws IllegalArgumentException;

    void addRating(RatingInfo routeInfo) throws IllegalArgumentException;

    BookingInfo getBookingForTransaction(String ref) throws IllegalArgumentException;

    List<BookingInfo> handleShareAccepted(Long id) throws IllegalArgumentException;

    ProfilInfo getPaypalProfil() throws IllegalArgumentException;

    BookingInfo payWithStripe(String token, BookingInfo bookingInfo);

    void sendStat(StatInfo statInfo);

    AgentInfo createDefaultUser();

}
