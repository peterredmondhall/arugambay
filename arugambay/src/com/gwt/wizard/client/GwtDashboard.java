package com.gwt.wizard.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwt.wizard.client.dashboard.ui.DashboardVeiw;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtDashboard implements EntryPoint
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    public static Long USERINFOID;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad()
    {
        service.getUser(new AsyncCallback<Long>()
        {

            @Override
            public void onSuccess(Long result)
            {
                if (result == null)
                {
                    Window.Location.replace("/login.html");
                }
                else
                {
                    USERINFOID = result;
                    RootPanel.get().add(new DashboardVeiw());
                }

            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to connect to server");
            }
        });
    }
}
