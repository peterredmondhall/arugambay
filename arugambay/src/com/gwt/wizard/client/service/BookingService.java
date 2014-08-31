package com.gwt.wizard.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;
import com.gwt.wizard.shared.model.ProfilInfo;
import com.gwt.wizard.shared.model.StatInfo;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface BookingService extends RemoteService
{
    BookingInfo save(BookingInfo model) throws IllegalArgumentException;

    BookingInfo sendShareRequest(BookingInfo bookingInfo);

    // BookingInfo sendShareAccepted(List<BookingInfo> l);

    Boolean getUser() throws IllegalArgumentException;

    Boolean deletePlace(Long id) throws IllegalArgumentException;

    Boolean editPlace(Long id, PlaceInfo placeInfo) throws IllegalArgumentException;

    List<BookingInfo> getBookings() throws IllegalArgumentException;

    List<BookingInfo> getBookingsForTour(String ref) throws IllegalArgumentException;

    BookingInfo getBookingForTransaction(String ref) throws IllegalArgumentException;

    List<BookingInfo> getBookingsForShare(String ref) throws IllegalArgumentException;

    ProfilInfo getPaypalProfil() throws IllegalArgumentException;

    void sendStat(StatInfo statInfo);

}
