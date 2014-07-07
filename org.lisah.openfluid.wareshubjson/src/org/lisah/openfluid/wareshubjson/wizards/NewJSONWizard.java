package org.lisah.openfluid.wareshubjson.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;


public class NewJSONWizard extends Wizard implements INewWizard {

	private NewJSONWizardPage page;
	private ISelection selection;
	
	
	public NewJSONWizard() {
		super();
		setNeedsProgressMonitor(true);
	}


	// =====================================================================
	// =====================================================================	

	
	public void addPages() {
		page = new NewJSONWizardPage(selection);
		addPage(page);
	}

	
	// =====================================================================
	// =====================================================================	

	
	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String fileName = "wareshub.json";
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}


	// =====================================================================
	// =====================================================================	

	
	private void doFinish(
			String containerName,
			String fileName,
			IProgressMonitor monitor)
					throws CoreException {


		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));

		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}

		IContainer container = (IContainer) resource;

		final IFile file = container.getFile(new Path(fileName));

		monitor.beginTask("Creating " + fileName, 2);

		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}

		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
					// TODO open wareshub.json editor instead of default editor
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}

	
	// =====================================================================
	// =====================================================================	

	
	private InputStream openContentStream() {
		String contents = "{\n\n}\n";
		return new ByteArrayInputStream(contents.getBytes());
	}


	// =====================================================================
	// =====================================================================	
	
	
	private void throwCoreException(String message) throws CoreException {
		IStatus status =
				new Status(IStatus.ERROR, "org.lisah.openfluid.wareshubjson", IStatus.OK, message, null);
		throw new CoreException(status);
	}


	// =====================================================================
	// =====================================================================	
	
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}