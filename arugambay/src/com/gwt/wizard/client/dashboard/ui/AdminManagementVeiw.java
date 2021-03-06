package com.gwt.wizard.client.dashboard.ui;

import static com.gwt.wizard.client.GwtDashboard.getAgentInfo;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.client.GwtDashboard;
import com.gwt.wizard.client.Refresh;
import com.gwt.wizard.client.service.BookingService;
import com.gwt.wizard.client.service.BookingServiceAsync;
import com.gwt.wizard.shared.model.AgentInfo;

public class AdminManagementVeiw extends Composite
{
    private final BookingServiceAsync service = GWT.create(BookingService.class);

    private static AdminManagementVeiwUiBinder uiBinder = GWT.create(AdminManagementVeiwUiBinder.class);

    interface AdminManagementVeiwUiBinder extends UiBinder<Widget, AdminManagementVeiw>
    {
    }

    final ListBox agentListBox = new ListBox();;
    private List<AgentInfo> listAgents;

    @UiField
    HTMLPanel mainPanel;
    @UiField
    HTMLPanel btnContainer;
    @UiField
    VerticalPanel layout;
    private Button uploadBtn;

    public AdminManagementVeiw()
    {
        initWidget(uiBinder.createAndBindUi(this));
        initializeWidget();
    }

    private void initializeWidget()
    {
        btnContainer.clear();
        mainPanel.clear();
        setAdminManagementPanel();
        mainPanel.add(agentListBox);
        createAgentSelection();
        fetchAgents();
    }

    private void createAgentSelection()
    {
        agentListBox.addChangeHandler(new ChangeHandler()
        {

            @Override
            public void onChange(ChangeEvent event)
            {
                AgentInfo agentInfo = listAgents.get(agentListBox.getSelectedIndex());
                GwtDashboard.setAgentInfo(agentInfo);
            }
        });
    }

    private void fetchAgents()
    {
        service.getAgents(new AsyncCallback<List<AgentInfo>>()
        {
            @Override
            public void onSuccess(List<AgentInfo> agents)
            {
                listAgents = agents;
                agentListBox.clear();
                int i = 0;
                for (AgentInfo agent : agents)
                {
                    agentListBox.addItem(agent.getEmail());
                    if (agent.getEmail().equals(getAgentInfo().getEmail()))
                    {
                        agentListBox.setSelectedIndex(i);
                    }
                    i++;
                }

            }

            @Override
            public void onFailure(Throwable caught)
            {
                Refresh.refresh();
            }
        });
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
