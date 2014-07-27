package com.gwt.wizard.client.dashboard.ui;

import com.google.gwt.user.cellview.client.CellTable;

public interface TableRes extends CellTable.Resources
{
    @Override
    @Source({ CellTable.Style.DEFAULT_CSS, "com/gwt/wizard/client/dashboard/ui/table.css" })
    TableStyle cellTableStyle();

    interface TableStyle extends CellTable.Style
    {
    }
}
