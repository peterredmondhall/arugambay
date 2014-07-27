package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.TransportStepUi;
import com.gwt.wizard.shared.model.BookingInfo;

public class TransportStep implements WizardStep
{

    private final TransportStepUi ui;
    private final BookingInfo bookingInfo;
    private static DateTimeFormat sdf = DateTimeFormat.getFormat("dd.MM.yyyy");

    public TransportStep(BookingInfo bookingInfo)
    {
        ui = new TransportStepUi();
        this.bookingInfo = bookingInfo;
    }

    @Override
    public String getCaption()
    {
        return MESSAGES.firstPage();
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    @Override
    public Boolean onNext()
    {
//        ui.getErrMsg().setText("");
//
//        if (ui.getDateBox().getValue() == null)
//        {
//            ui.getErrMsg().setText(MESSAGES.dateRequiredError());
//            return false;
//        }
////        if (ui.getDateBox().getValue().before(new Date()))
////        {
////            ui.getErrMsg().setText(MESSAGES.futureDateRequiredError());
////            return false;
////        }
//
//        bookingInfo.setDate(sdf.format(ui.getDateBox().getValue()));
//        bookingInfo.setForwardPickupPlace(ui.getForwardPickupBox().getItemText(ui.getForwardPickupBox().getSelectedIndex()));
//        bookingInfo.setForwardPickupTime(ui.getForwardTimeBox().getItemText(ui.getForwardTimeBox().getSelectedIndex()));
//        bookingInfo.setReturnPickupPlace(ui.getReturnPickupBox().getItemText(ui.getReturnPickupBox().getSelectedIndex()));
//        bookingInfo.setReturnPickupTime(ui.getReturnTimeBox().getItemText(ui.getReturnTimeBox().getSelectedIndex()));
//
//        bookingInfo.setPax(ui.getPassengerDetail(PassengerDetail.TOT_PASS));
//        bookingInfo.setPaxRollatoren(ui.getPassengerDetail(PassengerDetail.PASS_WITH_ROLLATOR));
//        bookingInfo.setPaxFoldableWheelchair(ui.getPassengerDetail(PassengerDetail.PASS_WITH_FOLDABLE));
//        bookingInfo.setPaxRollstuhl(ui.getPassengerDetail(PassengerDetail.PASS_REQ_WHEELCHAIR));
//        bookingInfo.setWithReturn(ui.getWithReturn());

        return true;
    }

    @Override
    public Boolean onBack()
    {
        return true;
    }

    @Override
    public void clear()
    {
//        ui.getDateBox().setValue(null);
    }

}
