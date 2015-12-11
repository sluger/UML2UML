package at.jku.uml2uml.gui;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import at.jku.uml2uml.launcher.EMFTVMLauncher;

/*
 *  (c) Stefan Luger 2013
 *  A simple graphical user interface for a more convenient transformation execution.
 *  Simply choose from one of the available transformation scenarios and specify the in/out- as well as an optional target model.
 *  Filepaths only work for Windows systems! In case of using Unix, you have to change file separators.
 */

public class Window {
	Display display = new Display();
	Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
	String workDir = (String) System.getProperty("user.dir").subSequence(0,
			System.getProperty("user.dir").lastIndexOf('\\'));

	public Window() {
		init();
		shell.pack();
		shell.setSize(600, 175);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private void init() {
		shell.setText("UML2UML Transformation");
		shell.setLayout(new GridLayout(4, false));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);

		// metamodel
		Label labelMM = new Label(shell, SWT.NONE);
		labelMM.setText("Metamodel name:");
		labelMM.setToolTipText("The metamodel used for the transformation.");
		final Text textMM = new Text(shell, SWT.NONE);
		textMM.setText("UML2");
		textMM.setEditable(false);
		textMM.setEnabled(false);
		final Text textMMPath = new Text(shell, SWT.NONE);
		textMMPath.setText("http://www.eclipse.org/uml2/4.0.0/UML");
		textMMPath.setEditable(false);
		textMMPath.setEnabled(false);
		Label labelMMPH = new Label(shell, SWT.NONE);
		labelMMPH.setVisible(false);

		// module
		Label labelModule = new Label(shell, SWT.NONE);
		labelModule.setText("Module name:");
		labelModule
				.setToolTipText("The ATL module file (*.atl) which contains the transformation rules.");
		final Text textModule = new Text(shell, SWT.NONE);
		textModule.setEditable(false);
		textModule.setEnabled(false);
		final Text textModulePath = new Text(shell, SWT.NONE);
		textModulePath.setLayoutData(data);
		textModulePath.setEditable(true);
		Button buttonModule = new Button(shell, SWT.PUSH);
		buttonModule.setText("Browse");
		buttonModule.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.NULL);
				String[] ext = { "*.atl" };
				dialog.setFilterExtensions(ext);
				dialog.setFilterPath("../Transformations/inplace/");
				String path = dialog.open();
				if (path != null) {
					File file = new File(path);
					if (file.isFile()) {
						textModulePath.setText(".."
								+ file.getParent().substring(workDir.length())
										.replace('\\', '/') + '/');
						textModule.setText(file.getName().substring(0,
								file.getName().lastIndexOf('.')));
					} else
						textModulePath.setText("");
				}
			}
		});

		// in/out model
		Label labelSTM = new Label(shell, SWT.NONE);
		labelSTM.setText("In/Out model name:");
		labelSTM.setToolTipText("The UML file (*.uml) which will be transformed by the module specified above.");
		final Text textSTM = new Text(shell, SWT.NONE);
		textSTM.setText("INOUT");
		textSTM.setEditable(false);
		textSTM.setEnabled(false);
		final Text textSTMPath = new Text(shell, SWT.NONE);
		// textSTMPath.setText("");
		textSTMPath.setLayoutData(data);
		textSTMPath.setEditable(true);
		Button buttonSTM = new Button(shell, SWT.PUSH);
		buttonSTM.setText("Browse");
		buttonSTM.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.NULL);
				String[] ext = { "*.uml" };
				dialog.setFilterExtensions(ext);
				dialog.setFilterPath("../Models/papyrus/models/");
				String path = dialog.open();
				if (path != null) {
					File file = new File(path);
					if (file.isFile())
						textSTMPath.setText(".."
								+ file.getAbsolutePath()
										.substring(workDir.length())
										.replace('\\', '/'));
					else
						textSTMPath.setText("");
				}
			}
		});

		// target model
		Label labelTarget = new Label(shell, SWT.NONE);
		labelTarget.setText("Save as*:");
		labelTarget
				.setToolTipText("Optionally saving the target model as a different file (*.uml) to prohibit overwriting the In/Out model file.\nIn order to overwrite the In/Out model file, leave no space (\"\").");
		final Text textTarget = new Text(shell, SWT.NONE);
		textTarget.setEditable(false);
		textTarget.setEnabled(false);
		final Text textTargetPath = new Text(shell, SWT.NONE);
		textTargetPath.setLayoutData(data);
		textTargetPath.setEditable(true);
		Button buttonTarget = new Button(shell, SWT.PUSH);
		buttonTarget.setText("Browse");
		buttonTarget.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.NULL);
				String[] ext = { "*.uml" };
				dialog.setFilterExtensions(ext);
				dialog.setFilterPath("../Models/papyrus/models/");
				String path = dialog.open();
				if (path != null) {
					File file = new File(path);
					if (file.isFile()) {
						textTargetPath.setText(".."
								+ file.getAbsolutePath()
										.substring(workDir.length())
										.replace('\\', '/'));
					} else
						textTargetPath.setText("");
				}
			}
		});

		Button buttonTransform = new Button(shell, SWT.PUSH);
		buttonTransform
				.setBackground(new Color(Display.getCurrent(), 0, 255, 0));
		buttonTransform.setText("Transform");
		buttonTransform.setToolTipText("Press to conduct transformation.");
		buttonTransform.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					new EMFTVMLauncher(textMM.getText(), "IN", "OUT", textSTM
							.getText(), textMMPath.getText(), "",
							textTargetPath.getText(), textSTMPath.getText(),
							textModule.getText(), textModulePath.getText())
							.launch();
				} catch (Exception e2) {
					System.err
							.println("ERROR: Make sure the right ATL module and filepaths are specified correctly! "
									+ e2);
				}

			}
		});

		Label labelPH = new Label(shell, SWT.NONE);
		labelPH.setVisible(false);

		Text textInfo = new Text(shell, SWT.NONE);
		textInfo.setEnabled(false);
		textInfo.setText("Transformation debug information is displayed on console.");
	}

	public static void main(String[] args) {
		new Window();
	}
}