package com.gwt.wizard.client.steps.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.core.Showable;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;

public class TransportStepUi extends Composite implements Showable
{
    public static final String WIDTH = "120px";

    public enum PassengerDetail
    {
        TOT_PASS,
        PASS_WITH_ROLLATOR,
        PASS_WITH_FOLDABLE,
        PASS_REQ_WHEELCHAIR
    }

    private static RouteStepUiUiBinder uiBinder = GWT.create(RouteStepUiUiBinder.class);
    private final BookingServiceAsync bookingService = GWT.create(BookingService.class);

    interface RouteStepUiUiBinder extends UiBinder<Widget, TransportStepUi>
    {
    }

    @UiField
    HTMLPanel mainPanel;
//    @UiField
//    DateBox dateBox;
//    @UiField
//    ListBox forwardPickupBox;
//    @UiField
//    ListBox forwardTimeBox;
//    @UiField
//    ListBox returnTimeBox;
//    @UiField
//    ListBox returnPickupBox;
//
//    @UiField
//    RadioButton rb2, rb1;
//
//    @UiField
//    Label errMsg, labelReturnPickup, labelReturnTime;
//
//    boolean withReturn = true;
//
//    @UiField
//    ListBox totalPassengersBox,
//            passengersWithRollatorBox,
//            passengersWithFoldableWheelchairBox,
//            passengersWithheelchairTransportBox;

    Map<PassengerDetail, ListBox> listBoxMap = new HashMap<PassengerDetail, ListBox>();

//    public ListBox getForwardPickupBox()
//    {
//        return forwardPickupBox;
//    }
//
//    public ListBox getForwardTimeBox()
//    {
//        return forwardTimeBox;
//    }
//
//    public ListBox getReturnTimeBox()
//    {
//        return returnTimeBox;
//    }
//
//    public ListBox getReturnPickupBox()
//    {
//        return returnPickupBox;
//    }

    public TransportStepUi()
    {

        initWidget(uiBinder.createAndBindUi(this));
    }

//        dateBox.setFormat(new DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
//        initTimeBox(forwardTimeBox);
//        initTimeBox(returnTimeBox);
//
//        bookingService.getPlaceList(new AsyncCallback<List<String>>()
//        {
//
//            @Override
//            public void onSuccess(List<String> result)
//            {
//                if (result != null && result.size() > 0)
//                {
//                    for (String place : result)
//                    {
//                        forwardPickupBox.addItem(place);
//                        returnPickupBox.addItem(place);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable caught)
//            {
//                Window.alert("Failed to fetch places from server!");
//            }
//        });
//
//        listBoxMap.put(PassengerDetail.TOT_PASS, totalPassengersBox);
//        listBoxMap.put(PassengerDetail.PASS_WITH_ROLLATOR, passengersWithRollatorBox);
//        listBoxMap.put(PassengerDetail.PASS_WITH_FOLDABLE, passengersWithFoldableWheelchairBox);
//        listBoxMap.put(PassengerDetail.PASS_REQ_WHEELCHAIR, passengersWithheelchairTransportBox);
//
//        for (PassengerDetail pdDetail : PassengerDetail.values())
//        {
//            initPaxBox(listBoxMap.get(pdDetail));
//            listBoxMap.get(pdDetail).setWidth(WIDTH);
//        }
//        forwardPickupBox.setWidth(WIDTH);
//        returnPickupBox.setWidth(WIDTH);
//        forwardTimeBox.setWidth(WIDTH);
//        returnTimeBox.setWidth(WIDTH);
//        dateBox.setWidth("110px");
//        ClickHandler handler = new ClickHandler()
//        {
//
//            @Override
//            public void onClick(ClickEvent event)
//            {
//                withReturn = !withReturn;
//                update();
//            }
//        };
//        rb1.addClickHandler(handler);
//        rb2.addClickHandler(handler);
//        update();
//    }
//
//    private void update()
//    {
//        rb1.setValue(withReturn);
//        rb2.setValue(!withReturn);
//        labelReturnPickup.setVisible(withReturn);
//        labelReturnTime.setVisible(withReturn);
//        returnPickupBox.setVisible(withReturn);
//        returnTimeBox.setVisible(withReturn);
//
//    }

//    public boolean getReturn()
//    {
//        return withReturn;
//    }
//
//    public DateBox getDateBox()
//    {
//        return dateBox;
//    }

    public Integer getPassengerDetail(PassengerDetail passengerDetail)
    {
        String selection = listBoxMap.get(passengerDetail).getItemText(listBoxMap.get(passengerDetail).getSelectedIndex());
        return Integer.parseInt(selection);
    }

//    public Label getErrMsg()
//    {
//        return errMsg;
//    }

    @Override
    public void setVisible(boolean visible)
    {
        mainPanel.setVisible(visible);
        // mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
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
    public void show(boolean visible, Button prev, Button next, Button cancel)
    {
        next.setVisible(true);
        prev.setEnabled(false);
    }

}
