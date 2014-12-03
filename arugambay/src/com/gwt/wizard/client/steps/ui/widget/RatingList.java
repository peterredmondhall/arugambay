package com.gwt.wizard.client.steps.ui.widget;

import java.util.List;

import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwt.wizard.shared.model.RatingInfo;

public class RatingList
{

//    String cwDisclosurePanelDescription = "xxxx";
//
    String cwDisclosurePanelFormAdvancedCriteria = "cwDisclosurePanelFormAdvancedCriteria";
//
//    String cwDisclosurePanelFormDescription = "xxxxxxx";
//
    String cwDisclosurePanelFormLocation = "cwDisclosurePanelFormLocation";
//
//    String cwDisclosurePanelFormName = "xxxxxxx";
//
    String cwDisclosurePanelFormTitle = "";
//
//    String cwDisclosurePanelName = "xxxxxxx";

    List<RatingInfo> ratings;

    public RatingList(List<RatingInfo> ratings)
    {
        this.ratings = ratings;
    }

    /**
     * Create a form that contains undisclosed advanced options.
     */
    public Widget createAdvancedForm()
    {
        // Create a table to layout the form options
        FlexTable layout = new FlexTable();
        layout.setCellSpacing(6);
        layout.setWidth("300px");
        FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

        // Add a title to the form
        layout.setHTML(0, 0, cwDisclosurePanelFormTitle);
        cellFormatter.setColSpan(0, 0, 3);
        cellFormatter.setHorizontalAlignment(
                0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        int i = 0;
        for (RatingInfo ratingInfo : ratings)
        {
            // Create some advanced options
            Grid advancedOptions = new Grid(2, 2);
            advancedOptions.setCellSpacing(6);
            advancedOptions.setHTML(0, 0, cwDisclosurePanelFormLocation);
            advancedOptions.setWidget(0, 1, new TextBox());

            // Add advanced options to form in a disclosure panel
            DisclosurePanel advancedDisclosure = new DisclosurePanel(
                    cwDisclosurePanelFormAdvancedCriteria);
            advancedDisclosure.setAnimationEnabled(true);
            advancedDisclosure.ensureDebugId("cwDisclosurePanel");
            advancedDisclosure.setContent(advancedOptions);
            layout.setWidget(3, 0, advancedDisclosure);
            cellFormatter.setColSpan(3, 0, 2);

            // Add some standard form options
            int stars = i++;
            if (stars <= 5)
            {
                String src = "images/" + stars + "stars.png";
                Image img = new Image(src);
                int width = 20 * stars;
                int heiht = 20;
                img.setWidth(width + "px");
                img.setHeight(heiht + "px");
                layout.setWidget(i + 1, 0, img);
                layout.setWidget(i + 1, 1, new Label("this is my critic xxxxxxxx xxxxxxxxxxx xxxxxxxxxxxxxxxxx"));
                layout.setWidget(i + 1, 2, new Label("nick"));
            }
        }
        // Wrap the contents in a DecoratorPanel
        DecoratorPanel decPanel = new DecoratorPanel();
        decPanel.setWidget(layout);
        return decPanel;
    }
}