// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.repository.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;
import org.talend.mdm.repository.ui.dialogs.xpath.XpathSelectDialog2;

import com.amalto.workbench.dialogs.ViewInputDialog;

/**
 * DOC hbhong class global comment. this class is compatible with ViewInputDialog, but works for Repository view
 */
public class ViewInputDialog2 extends ViewInputDialog {

    /**
     * DOC hbhong ViewInputDialog2 constructor comment.
     * 
     * @param site
     * @param treeParent
     * @param parentShell
     * @param dialogTitle
     * @param dialogMessage
     * @param initialValue
     * @param validator
     * @param isTransfor
     */
    public ViewInputDialog2(IWorkbenchPartSite site, Shell parentShell, String dialogTitle, String dialogMessage,
            String initialValue, IInputValidator validator, boolean isTransfor) {
        super(site, null, parentShell, dialogTitle, dialogMessage, initialValue, validator, isTransfor);
    }

    public void widgetSelected(SelectionEvent e) {
        dlg = new XpathSelectDialog2(getParentShell(), "Select one Entity", site, false, null);
        dlg.setBlockOnOpen(true);
        dlg.open();

        if (dlg.getReturnCode() == IDialogConstants.OK_ID) {
            text.setText(value + dlg.getEntityName());
            dlg.close();
        }
    }
}
