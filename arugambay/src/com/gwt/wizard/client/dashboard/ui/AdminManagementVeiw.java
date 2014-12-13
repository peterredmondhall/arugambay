package com.gwt.wizard.client.dashboard.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.shared.model.ContractorInfo;

public class AdminManagementVeiw extends Composite
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    private static AdminManagementVeiwUiBinder uiBinder = GWT.create(AdminManagementVeiwUiBinder.class);

    interface AdminManagementVeiwUiBinder extends UiBinder<Widget, AdminManagementVeiw>
    {
    }

    private final CellTable.Resources tableRes = GWT.create(TableRes.class);
    NumberFormat usdFormat = NumberFormat.getFormat(".00");

    CellTable<ContractorInfo> contractorManagementTable;

    @UiField
    HTMLPanel mainPanel;
    @UiField
    HTMLPanel btnContainer;
    @UiField
    VerticalPanel layout;
    private Button uploadBtn;

    final TextBox editContractorNameTxtBox = new TextBox();

    final Label nameLabel = new Label("Name");

    // The list of data to display.

    public AdminManagementVeiw()
    {
        initWidget(uiBinder.createAndBindUi(this));
        initializeWidget();
    }

    private void initializeWidget()
    {
        btnContainer.clear();
        mainPanel.clear();
        contractorManagementTable = new CellTable<ContractorInfo>(13, tableRes);
        // TODO fetch agents
        // fetchContractors();
        setAdminManagementPanel();
    }

    private void setAdminManagementPanel()
    {
        setUploadBtn();
    }

    private void setUploadBtn()
    {
        uploadBtn = new Button();
        uploadBtn.setStyleName("btn btn-primary");
        uploadBtn.setText("Dataset");
        uploadBtn.getElement().getStyle().setFloat(Float.RIGHT);
        uploadBtn.getElement().getStyle().setMargin(3, Unit.PX);
        btnContainer.add(getUploader(uploadBtn));
    }

    private Widget getUploader(final Button saveButton)
    {
        final FormPanel form = new FormPanel();
        form.setAction("/.datasetupld");

        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        // Create a panel to hold all of the form widgets.
        VerticalPanel panel = new VerticalPanel();
        form.setWidget(panel);

        // Create a FileUpload widget.
        FileUpload upload = new FileUpload();
        upload.setName("uploadFormElement");
        panel.add(upload);

        // Add a 'submit' button.
        panel.add(new Button("Upload file", new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                saveButton.setEnabled(false);
                form.submit();
            }
        }));

        form.addSubmitCompleteHandler(new SubmitCompleteHandler()
        {

            @Override
            public void onSubmitComplete(SubmitCompleteEvent event)
            {
                String[] parts = event.getResults().split(("\\*\\*\\*"));
                // imageId = Long.parseLong(parts[1]);
                saveButton.setEnabled(true);
            }
        });

        return form;

    }
}
