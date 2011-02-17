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
package com.amalto.workbench.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDCompositor;
import org.eclipse.xsd.XSDDerivationMethod;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDSchemaBuildingTools;

import com.amalto.workbench.dialogs.ComplexTypeInputDialog;
import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;

public class XSDNewComplexTypeDefinition extends UndoAction implements SelectionListener {

    private static Log log = LogFactory.getLog(XSDNewComplexTypeDefinition.class);

    private ComplexTypeInputDialog dialog;

    String typeName = null;

    String superTypeName;

    boolean isAbstract;

    boolean isChoice = false;

    boolean isAll = false;

    public XSDNewComplexTypeDefinition(DataModelMainPage page) {
        super(page);
        setImageDescriptor(ImageCache.getImage(EImage.ADD_OBJ.getPath()));
        setText("Create a Complex Type");
        setToolTipText("Create a new Complex Type which can be referred to by Elements or Entities");
        setDescription(getToolTipText());
    }

    public IStatus doAction() {

        List<XSDComplexTypeDefinition> list = new ArrayList<XSDComplexTypeDefinition>();
        EList<XSDTypeDefinition> types = schema.getTypeDefinitions();
        for (XSDTypeDefinition type : types) {
            if (type instanceof XSDComplexTypeDefinition)
                list.add((XSDComplexTypeDefinition) type);
        }

        dialog = new ComplexTypeInputDialog(this, page.getSite().getShell(), schema, null, list, false);
        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL) {
            return Status.CANCEL_STATUS;
        }

        XSDFactory factory = XSDSchemaBuildingTools.getXSDFactory();
        XSDModelGroup group = factory.createXSDModelGroup();
        XSDParticle subParticle = null;
        XSDElementDeclaration subElement = null;

        subElement = factory.createXSDElementDeclaration();
        subElement.setName("subelement");
        subElement.setTypeDefinition(schema.resolveSimpleTypeDefinition(schema.getSchemaForSchemaNamespace(), "string"));

        subParticle = factory.createXSDParticle();
        subParticle.unsetMaxOccurs();
        subParticle.unsetMinOccurs();
        subParticle.setContent(subElement);
        subParticle.updateElement();

        if (isChoice)
            group.setCompositor(XSDCompositor.CHOICE_LITERAL);
        else if (isAll)
            group.setCompositor(XSDCompositor.ALL_LITERAL);
        else
            group.setCompositor(XSDCompositor.SEQUENCE_LITERAL);
        group.getContents().add(0, subParticle);
        group.updateElement();

        XSDComplexTypeDefinition complexType = factory.createXSDComplexTypeDefinition();
        // complexType.setDerivationMethod(XSDDerivationMethod.EXTENSION_LITERAL);
        XSDTypeDefinition superType = null;
        for (XSDTypeDefinition type : list) {
            if (type.getName().equals(superTypeName)) {
                superType = type;
                break;
            }
        }
        complexType.setName(typeName);
        if (superType != null) {
            complexType.setDerivationMethod(XSDDerivationMethod.EXTENSION_LITERAL);
            complexType.setBaseTypeDefinition(superType);
        }
        if (isAbstract)
            complexType.setAbstract(isAbstract);
        else
            complexType.unsetAbstract();
        schema.getContents().add(complexType);

        complexType.updateElement();

        // add the group
        XSDParticle groupParticle = factory.createXSDParticle();
        groupParticle.unsetMaxOccurs();
        groupParticle.unsetMinOccurs();
        groupParticle.setContent(group);
        groupParticle.updateElement();

        complexType.setContent(groupParticle);
        complexType.updateElement();

        page.refresh();
        page.markDirty();
        return Status.OK_STATUS;
    }

    public void widgetSelected(SelectionEvent e) {
        if (dialog.getReturnCode() == -1)
            return;
        typeName = dialog.getTypeName();
        superTypeName = dialog.getSuperName();
        isAbstract = dialog.isAbstract();
        isChoice = dialog.isChoice();
        isAll = dialog.isAll();

        if (!validateType()) {
            return;
        }
        dialog.close();
    }

    public void widgetDefaultSelected(SelectionEvent e) {

    }

    private boolean validateType() {
        if (!"".equals(typeName)) {
            EList list = schema.getTypeDefinitions();
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                XSDTypeDefinition td = (XSDTypeDefinition) iter.next();
                if (td.getName().equals(typeName)) {
                    if (td instanceof XSDComplexTypeDefinition) {
                        MessageDialog.openError(page.getSite().getShell(), "Error", "This type \"" + typeName
                                + "\" already exists as a Complex Type");
                        return false;
                    }
                }
            }// for
        } else {
            MessageDialog.openError(page.getSite().getShell(), "Error", "Please enter the Complex Type name");
            return false;
        }
        return true;
    }
}
