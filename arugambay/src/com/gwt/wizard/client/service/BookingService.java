package com.gwt.wizard.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gwt.wizard.shared.model.BookingInfo;
import com.gwt.wizard.shared.model.PlaceInfo;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface BookingService extends RemoteService
{
    Boolean save(BookingInfo model) throws IllegalArgumentException;

    Boolean getUser() throws IllegalArgumentException;

    Boolean deletePlace(Long id) throws IllegalArgumentException;

    Boolean editPlace(Long id, PlaceInfo placeInfo) throws IllegalArgumentException;

    List<BookingInfo> getBookings() throws IllegalArgumentException;

    BookingInfo getBooking(String ref) throws IllegalArgumentException;

}
