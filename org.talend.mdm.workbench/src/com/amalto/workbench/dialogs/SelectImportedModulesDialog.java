// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package com.amalto.workbench.dialogs;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.w3c.dom.Document;

import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.providers.TypesLabelProvider;
import com.amalto.workbench.providers.XSDTreeLabelProvider;
import com.amalto.workbench.providers.datamodel.SchemaTreeContentProvider;
import com.amalto.workbench.providers.datamodel.TypesTreeContentProvider;
import com.amalto.workbench.utils.Util;
import com.amalto.workbench.utils.XtentisException;
import com.amalto.workbench.webservices.WSDataModelPK;
import com.amalto.workbench.webservices.WSRegexDataModelPKs;
import com.amalto.workbench.webservices.XtentisPort;

public class SelectImportedModulesDialog extends Dialog {

	protected static Log log = LogFactory
			.getLog(SelectImportedModulesDialog.class);

	private String title;

	private Shell shell;

	private TreeObject treeObject;

	private TypesTreeContentProvider typeprovider = new TypeContentProvider();
	private SchemaTreeContentProvider entityprovider = new SchemaContentProvider();

	private class TypeContentProvider extends TypesTreeContentProvider {

		public TypeContentProvider() {
			super(null, null);
		}

		@Override
		public Object[] getElements(Object parent) {
			if (null == parent) {
				return null;
			}
			if (parent instanceof XSDSchema) {
				return getChildren(parent);
			}
			if (parent instanceof Collection<?>) {
				Collection<?> col = (Collection<?>) parent;
				Collection<XSDTypeDefinition> ret = new LinkedList<XSDTypeDefinition>();
				for (Object obj : col) {
					if (obj instanceof XSDTypeDefinition) {
						ret.add((XSDTypeDefinition) obj);
					}
				}
				return ret.toArray();
			}
			return null;
		}
	}

	private class SchemaContentProvider extends SchemaTreeContentProvider {
		public SchemaContentProvider() {
			super(null, null);
		}

		@Override
		public Object[] getElements(Object parent) {
			if (null == parent) {
				return null;
			}
			if (parent instanceof XSDSchema) {
				return getChildren(parent);
			}
			if (parent instanceof Collection<?>) {
				Collection<?> col = (Collection<?>) parent;
				Collection<XSDElementDeclaration> ret = new LinkedList<XSDElementDeclaration>();
				for (Object obj : col) {
					if (obj instanceof XSDElementDeclaration) {
						ret.add((XSDElementDeclaration) obj);
					}
				}
				return ret.toArray();
			}
			return null;
		}

	}

	public SelectImportedModulesDialog(Shell parentShell, TreeObject treeObj, String title) {
		super(parentShell);
		this.shell = parentShell;
		this.treeObject = treeObj;
		this.title = title;
	}

	public void addEntity(XSDSchema xsdSchema) {
		addSchemaContent(entityprovider, xsdSchema);
		entityViewer.refresh();
	}

	public void addTypes(XSDSchema xsdSchema) {
		addSchemaContent(typeprovider, xsdSchema);
		typeViewer.refresh();
	}

	final Collection<XSDSchemaContent> addContent = new LinkedList<XSDSchemaContent>();

	public Collection<XSDSchemaContent> getXSDContents() {
		return addContent;
	}

	protected void addSchemaContent(SchemaTreeContentProvider provider,
			XSDSchema xsdSchema) {
		if (null == xsdSchema || null == provider) {
			return;
		}
		Object[] objs = provider.getChildren(xsdSchema);
		if (null == objs) {
			return;
		}
		for (Object object : objs) {
			if (object instanceof XSDSchemaContent) {
				addContent.add((XSDSchemaContent) object);
			}
		}
	}

	CheckboxTableViewer entityViewer;
	CheckboxTableViewer typeViewer;

	CheckboxTableViewer createTableViewer(Composite parent, String columnName,
			IContentProvider contentProvider, ILabelProvider lableProvider) {
		Composite com = new Composite(parent, SWT.NONE);
		com.setLayout(new GridLayout(1, false));
		final CheckboxTableViewer viewer = CheckboxTableViewer.newCheckList(
				com, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK
						| SWT.BORDER);
		viewer.setCheckStateProvider(new ICheckStateProvider() {

			public boolean isChecked(Object element) {
				return true;
			}

			public boolean isGrayed(Object element) {
				return false;
			}
		});
		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(viewer.getControl());
		viewer.getTable().setLinesVisible(true);
		viewer.setContentProvider(contentProvider);
		viewer.getTable().setHeaderVisible(true);
		TableLayout lay = new TableLayout();
		TableColumn column = new TableColumn(viewer.getTable(), SWT.CENTER);
		column.setText(columnName);
		lay.addColumnData(new ColumnWeightData(200, 100));
		viewer.getTable().setLayout(lay);
		viewer.setLabelProvider(lableProvider);

		ToolBar toolBar = new ToolBar(com, SWT.HORIZONTAL | SWT.FLAT);

		createToolBarItem(toolBar,
				Messages.RepositoryCheckTreeViewer_SelectAll,
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						viewer.setAllChecked(true);
					}
				});
		createToolBarItem(toolBar,
				Messages.RepositoryCheckTreeViewer_DeselectAll,
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						viewer.setAllChecked(false);
					}
				});
		return viewer;
	}

	private ToolItem createToolBarItem(ToolBar toolBar, String title,
			SelectionListener listen) {
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setText(title);
		item.setToolTipText(title);
		item.addSelectionListener(listen);
		return item;
	}
	
	
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText(this.title);

		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) composite.getLayout();
		layout.numColumns = 2;
		SashForm form = new SashForm(composite, SWT.HORIZONTAL);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true,
				1, 1);
		data.widthHint = 400;
		data.heightHint = 300;
		form.setLayoutData(data);
		entityViewer = createTableViewer(form, "Entities", entityprovider,
				new XSDTreeLabelProvider());
		typeViewer = createTableViewer(form, "Types", typeprovider,
				new TypesLabelProvider());

		form.setWeights(new int[] { 3, 5 });
		Composite compositeBtn = new Composite(composite, SWT.FILL);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
				false, 1, 1));
		compositeBtn.setLayout(new GridLayout(1, false));

		Button addXSDFromLocal = new Button(compositeBtn, SWT.PUSH | SWT.FILL);
		addXSDFromLocal.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
				false, 1, 1));
		addXSDFromLocal.setText(Messages.AddXsdFromlocal);
		addXSDFromLocal.setToolTipText(Messages.AddXsdSchemaFromlocal);
		addXSDFromLocal.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			};

			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog fd = new FileDialog(shell.getShell(), SWT.SAVE);
				fd.setFilterExtensions(new String[] { "*.xsd" });//$NON-NLS-1$
				fd.setText(Messages.ImportXSDSchema);
				String filename = fd.open();
				if (filename == null) {
					return;
				}
				URL url = getSourceURL("file:///" + filename);
				XSDSchema schema = createSchema(url);
				if (null == schema) {
					return;
				}
				addEntity(schema);
				addTypes(schema);
			}
		});
		if (Util.IsEnterPrise()) {
			Button addXSDFromWebSite = new Button(compositeBtn, SWT.PUSH
					| SWT.FILL);
			addXSDFromWebSite.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
					false, false, 1, 1));
			addXSDFromWebSite.setText(Messages.AddTypesDataModels);
			addXSDFromWebSite.setToolTipText(Messages.AddFromModelTypes);
			addXSDFromWebSite.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(
						org.eclipse.swt.events.SelectionEvent e) {
				};

				public void widgetSelected(
						org.eclipse.swt.events.SelectionEvent e) {
					MDMXSDSchemaEntryDialog dlg = new MDMXSDSchemaEntryDialog(
							shell.getShell(), Messages.SelectXSDSchemaWebSite);
					try {
						List<String> schemaList = new ArrayList<String>();
						resolveSchemaList(schemaList, dlg);
					} catch (Exception es) {
						log.error(es.getMessage(), es);
						return;
					}

					dlg.setBlockOnOpen(true);
					dlg.open();
					if (dlg.getReturnCode() == Window.OK) {
						List<String> urls = dlg.getMDMDataModelUrls();
						for (String datamodel : urls) {
							URL url = getSourceURL("type:" + datamodel);
							XSDSchema schema = createSchema(url);
							if (null == schema) {
								return;
							}
							addTypes(schema);
						}
					}
				}
			});
		}

		Button impXSDFromExchange = new Button(compositeBtn, SWT.PUSH
				| SWT.FILL);
		impXSDFromExchange.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
				false, false, 1, 1));
		impXSDFromExchange.setText(Messages.ImportFromExchangeServer);
		impXSDFromExchange.setToolTipText(Messages.ImportSchemaFromServer);
		impXSDFromExchange.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				StringBuffer repository = new StringBuffer();
				ImportExchangeOptionsDialog dlg = new ImportExchangeOptionsDialog(
						shell.getShell(), null, false, repository);
				dlg.setBlockOnOpen(true);
				int ret = dlg.open();
				if (ret == Window.OK) {
					File dir = new File(repository.toString());
					for (File file : dir.listFiles()) {
						if (file.getName().endsWith(".xsd")) {//$NON-NLS-1$
							XSDSchema schema = createSchema(getSourceURL("file:///"
									+ file.getPath()));
							if (null == schema) {
								return;
							}
							addEntity(schema);
							addTypes(schema);
							file.deleteOnExit();
						}
					}
				}
			}

		});

		Button addXSDFromInputDlg = new Button(compositeBtn, SWT.PUSH
				| SWT.FILL);
		addXSDFromInputDlg.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
				false, false, 1, 1));
		addXSDFromInputDlg.setText(Messages.AddXsdFromOther);
		addXSDFromInputDlg.setToolTipText(Messages.AddFromOtherSite);
		addXSDFromInputDlg.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			};

			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				InputDialog id = new InputDialog(shell.getShell(),
						Messages.AddXsdFromOther, Messages.EnterTextUrl,
						"", new IInputValidator() { //$NON-NLS-1$

							public String isValid(String newText) {
								if ((newText == null) || "".equals(newText)) {
									return Messages.NameNotEmpty;
								}
								return null;
							};
						});

				id.setBlockOnOpen(true);
				int ret = id.open();
				if (ret == Window.CANCEL) {
					return;
				}
				XSDSchema schema = createSchema(getSourceURL(id.getValue()));
				if (null == schema) {
					return;
				}
				addEntity(schema);
				addTypes(schema);
			}
		});
		entityViewer.setInput(addContent);
		typeViewer.setInput(addContent);
		return composite;
	}

	protected XSDSchema createSchema(URL url) {
		InputStream stream = null;
		try {
			stream = url.openStream();
			return createSchema(stream);
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			IOUtils.closeQuietly(stream);
		}
		return null;
	}

	private static final String DEFAULT_PROTOCAL = "(http|https|file|ftp):(\\//|\\\\)(.*):(.*)";//$NON-NLS-1$
	private Pattern urlPattern = Pattern.compile(DEFAULT_PROTOCAL);

	protected URL getSourceURL(String path) {
		Matcher match = urlPattern.matcher(path);
		if (match.matches()) {
			try {
				return new URL(path);
			} catch (MalformedURLException e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	protected XSDSchema createSchema(InputStream stream) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory.setValidating(false);
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document document = documentBuilder.parse(stream);
			return XSDSchemaImpl.createSchema(document.getDocumentElement());
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	private void resolveSchemaList(List<String> schemaList,
			MDMXSDSchemaEntryDialog dlg) throws Exception {

		boolean resolved = resolveSchemaList(schemaList);
		if (!resolved) {
			return;
		}

		String currentModelName = treeObject.getName();
		schemaList.remove(currentModelName);

		dlg.create();
		dlg.retrieveDataModels(schemaList, false);
	}

	protected boolean resolveSchemaList(List<String> schemaList)
			throws XtentisException, RemoteException {
		XtentisPort port = getPort();
		if (port == null) {
			MessageDialog.openError(getShell(), Messages._Error,
					Messages.ServerNotNull);
			return false;
		}
		WSDataModelPK[] xdmPKs = port.getDataModelPKs(
				new WSRegexDataModelPKs("")).getWsDataModelPKs();//$NON-NLS-1$
		if (xdmPKs != null) {
			for (WSDataModelPK xdmPK : xdmPKs) {
				String name = xdmPK.getPk();
				if (!name.startsWith("XMLSCHEMA")) {//$NON-NLS-1$
					schemaList.add(name);
				}
			}
		}

		return true;
	}

	@Override
	protected void okPressed() {
		addContent.clear();
		Object[] objs = entityViewer.getCheckedElements();
		if (null != objs) {
			for (Object obj : objs) {
				if (obj instanceof XSDSchemaContent) {
					addContent.add((XSDSchemaContent) obj);
				}
			}
		}
		objs = typeViewer.getCheckedElements();
		if (null != objs) {
			for (Object obj : objs) {
				if (obj instanceof XSDSchemaContent) {
					addContent.add((XSDSchemaContent) obj);
				}
			}
		}
		super.okPressed();
	}

	protected XtentisPort getPort() throws XtentisException {
		return Util.getPort(treeObject);
	}
}
