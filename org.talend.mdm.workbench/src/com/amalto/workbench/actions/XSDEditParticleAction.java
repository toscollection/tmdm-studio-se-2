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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDIdentityConstraintDefinition;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDXPathDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDSchemaBuildingTools;
import org.w3c.dom.Element;

import com.amalto.workbench.dialogs.BusinessElementInputDialog;
import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;
import com.amalto.workbench.providers.datamodel.SchemaTreeContentProvider;
import com.amalto.workbench.utils.IConstants;
import com.amalto.workbench.utils.Util;

public class XSDEditParticleAction extends UndoAction implements SelectionListener {

    private static Log log = LogFactory.getLog(XSDEditParticleAction.class);

    private BusinessElementInputDialog dialog = null;

    private XSDParticle selParticle = null;

    private String elementName;

    private String refName;

    private int minOccurs;

    private int maxOccurs;

    private String initEleName = "";

    public XSDEditParticleAction(DataModelMainPage page) {
        super(page);
        setImageDescriptor(ImageCache.getImage(EImage.EDIT_OBJ.getPath()));
        setText("Edit Element");
        setToolTipText("Edit the Business Element Name and Cardinality.");
    }

    public IStatus doAction() {
        try {
            IStructuredSelection selection = (IStructuredSelection) page.getTreeViewer().getSelection();
            selParticle = (XSDParticle) selection.getFirstElement();

            if (!(selParticle.getTerm() instanceof XSDElementDeclaration))
                return Status.CANCEL_STATUS;

            XSDElementDeclaration decl = (XSDElementDeclaration) selParticle.getContent();

            XSDElementDeclaration ref = null;
            if (decl.isElementDeclarationReference()) {
                // it is a ref
                ref = decl.getResolvedElementDeclaration();
            }

            EList eDecls = decl.getSchema().getElementDeclarations();

            ArrayList<String> elementDeclarations = new ArrayList<String>();
            for (Iterator iter = eDecls.iterator(); iter.hasNext();) {
                XSDElementDeclaration d = (XSDElementDeclaration) iter.next();
                if (d.getTargetNamespace() != null && d.getTargetNamespace().equals(IConstants.DEFAULT_NAME_SPACE))
                    continue;
                elementDeclarations.add(d.getQName() + (d.getTargetNamespace() != null ? " : " + d.getTargetNamespace() : ""));
            }
            elementDeclarations.add("");

            IStructuredContentProvider provider = (IStructuredContentProvider) page.getTreeViewer().getContentProvider();

            XSDIdentityConstraintDefinition identify = null;
            XSDXPathDefinition keyPath = null;
            List<Object> keyInfo = Util.getKeyInfo(decl);
            if (keyInfo != null && keyInfo.size() > 0) {
                identify = (XSDIdentityConstraintDefinition) keyInfo.get(0);
                keyPath = (XSDXPathDefinition) keyInfo.get(1);
                identify.getFields().remove(keyPath);
            }
            initEleName = decl.getName();
            dialog = new BusinessElementInputDialog(this, page.getSite().getShell(), "Edit Business Element", decl.getName(),
                    ref == null ? null : ref.getQName(), elementDeclarations, selParticle.getMinOccurs(),
                    selParticle.getMaxOccurs(), false);
            dialog.setBlockOnOpen(true);
            int ret = dialog.open();
            if (ret == Window.CANCEL) {
                return Status.CANCEL_STATUS;
            }

            // find reference
            XSDElementDeclaration newRef = null;
            if (!"".equals(refName.trim())) {
                newRef = Util.findReference(refName, schema);
                if (newRef == null) {
                    MessageDialog.openError(this.page.getSite().getShell(), "Error", "The Referenced Element " + refName
                            + " cannot be found");
                    return Status.CANCEL_STATUS;
                }
            }// ref

            decl.setName("".equals(this.elementName) ? null : this.elementName);
            if (keyPath != null) {
                XSDFactory factory = XSDSchemaBuildingTools.getXSDFactory();
                XSDXPathDefinition field = factory.createXSDXPathDefinition();
                field.setVariety(keyPath.getVariety());
                field.setValue(elementName);
                identify.getFields().add(field);
            }

            if (newRef != null) {
                decl.setResolvedElementDeclaration(newRef);
                decl.setTypeDefinition(null);
                Element elem = decl.getElement();
                if (elem.getAttributes().getNamedItem("type") != null)//$NON-NLS-1$
                    elem.getAttributes().removeNamedItem("type");//$NON-NLS-1$
                decl.updateElement();
            } else if (ref != null) {
                // fliu
                // no more element declarations --> we create a new Declaration with String simpleType definition
                // instead
                // FIXME: dereferecning and element is buggy

                XSDFactory factory = XSDSchemaBuildingTools.getXSDFactory();
                XSDElementDeclaration newD = (XSDElementDeclaration) factory.createXSDElementDeclaration();
                newD.setName(this.elementName);
                newD.updateElement();
                XSDSimpleTypeDefinition stringType = ((SchemaTreeContentProvider) page.getTreeViewer().getContentProvider())
                        .getXsdSchema().getSchemaForSchema()
                        .resolveSimpleTypeDefinition(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001, "string");

                newD.setTypeDefinition(stringType);
                if (selParticle.getContainer() instanceof XSDModelGroup) {
                    XSDModelGroup group = ((XSDModelGroup) selParticle.getContainer());
                    ((XSDModelGroup) selParticle.getContainer()).getContents().remove(selParticle);
                    selParticle = factory.createXSDParticle();
                    selParticle.setContent(newD);
                    group.getContents().add(selParticle);
                }
            }

            if (Util.changeElementTypeToSequence(decl, maxOccurs) == Status.CANCEL_STATUS) {
                return Status.CANCEL_STATUS;
            }

            selParticle.setMinOccurs(this.minOccurs);
            if (maxOccurs > -1) {
                selParticle.setMaxOccurs(this.maxOccurs);
            } else {
                if (selParticle.getElement().getAttributeNode("maxOccurs") != null)//$NON-NLS-1$
                    selParticle.getElement().getAttributeNode("maxOccurs").setNodeValue("unbounded");//$NON-NLS-1$//$NON-NLS-2$
                else {
                    selParticle.getElement().setAttribute("maxOccurs", "unbounded");//$NON-NLS-1$//$NON-NLS-2$
                }
            }

            selParticle.updateElement();

            page.refresh();
            page.markDirty();

        } catch (Exception e) {
            // e.printStackTrace();
            log.error(e.getStackTrace());
            MessageDialog.openError(page.getSite().getShell(), "Error", "An error occured trying to Edit a Business Elementt: "
                    + e.getLocalizedMessage());
            return Status.CANCEL_STATUS;
        }

        return Status.OK_STATUS;
    }

    public void runWithEvent(Event event) {
        super.runWithEvent(event);
    }

    /********************************
     * Listener to input dialog
     */
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    public void widgetSelected(SelectionEvent e) {
        if (dialog.getReturnCode() == -1)
            return; // there was a validation error
        elementName = dialog.getElementName();
        refName = dialog.getRefName();
        minOccurs = dialog.getMinOccurs();
        maxOccurs = dialog.getMaxOccurs();

        // check that this element does not already exist
        XSDModelGroup group = (XSDModelGroup) selParticle.getContainer();
        // get position of the selected particle in the container
        for (Iterator iter = group.getContents().iterator(); iter.hasNext();) {
            XSDParticle p = (XSDParticle) iter.next();
            if (p.getTerm() instanceof XSDElementDeclaration) {
                XSDElementDeclaration thisDecl = (XSDElementDeclaration) p.getTerm();
                if (thisDecl.getName().equals(elementName) && initEleName != null && !initEleName.equalsIgnoreCase(elementName)) {
                    MessageDialog.openError(page.getSite().getShell(), "Error", "The Business Element " + elementName
                            + " already exists.");
                    return;
                }
            }
        }// for
        dialog.close();
    }

}
