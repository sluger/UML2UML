package at.jku.uml2uml.launcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.emftvm.EmftvmFactory;
import org.eclipse.m2m.atl.emftvm.ExecEnv;
import org.eclipse.m2m.atl.emftvm.Metamodel;
import org.eclipse.m2m.atl.emftvm.Model;
import org.eclipse.m2m.atl.emftvm.impl.resource.EMFTVMResourceFactoryImpl;
import org.eclipse.m2m.atl.emftvm.util.DefaultModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.ModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.TimingData;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;

/*
 *  (c) Stefan Luger 2013
 *  ATL EMFTVM programmatical launch configuration launcher class.
 */
public class EMFTVMLauncher {
	private String metaModelName, sourceModelName, targetModelName,
			sourceTargetModelName;
	private String metaModelPath, sourceModelPath, targetModelPath,
			sourceTargetModelPath;
	private String moduleName, modulePath;

	ResourceSet emftvmRs;
	ResourceSet umlRs;

	ExecEnv env;

	private Metamodel metaModel;
	private Model sourceModel, targetModel, sourceTargetModel;

	ModuleResolver mr;
	TimingData td;

	/*
	 * the constructor provides all necessary transformation settings
	 */
	public EMFTVMLauncher(String metaModelName, String sourceModelName,
			String targetModelName, String sourceTargetModelName,
			String metaModelPath, String sourceModelPath,
			String targetModelPath, String sourceTargetModelPath,
			String moduleName, String modulePath) {
		// initialize UML resource
		initUMLResource();
		initEMFTVMResource();

		// initialize execution environment
		initExecutionEnvironment();

		// initialize model names and file paths
		initTransformation(metaModelName, sourceModelName, targetModelName,
				sourceTargetModelName, metaModelPath, sourceModelPath,
				targetModelPath, sourceTargetModelPath, moduleName, modulePath);

		// instantiate EMFTVM related objects
		mr = new DefaultModuleResolver(this.modulePath, new ResourceSetImpl());
		td = new TimingData();
	}

	/*
	 * initialize UML resource
	 */
	private void initUMLResource() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		this.umlRs = new ResourceSetImpl();
		this.umlRs.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("uml", new UMLResourceFactoryImpl());
	}

	/*
	 * initialize EMFTVM resource
	 */
	private void initEMFTVMResource() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"emftvm", new EMFTVMResourceFactoryImpl());
		this.emftvmRs = new ResourceSetImpl();
		this.emftvmRs.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("emftvm", new EMFTVMResourceFactoryImpl());
	}

	/*
	 * initialize execution environment
	 */
	private void initExecutionEnvironment() {
		env = EmftvmFactory.eINSTANCE.createExecEnv();
	}

	/*
	 * initialize model names and file paths
	 */
	private void initTransformation(String metaModelName,
			String sourceModelName, String targetModelName,
			String sourceTargetModelName, String metaModelPath,
			String sourceModelPath, String targetModelPath,
			String sourceTargetModelPath, String moduleName, String modulePath) {
		this.metaModelName = metaModelName;
		this.sourceModelName = sourceModelName;
		this.targetModelName = targetModelName;
		this.sourceTargetModelName = sourceTargetModelName;
		this.metaModelPath = metaModelPath;
		this.sourceModelPath = sourceModelPath;
		this.targetModelPath = targetModelPath;
		this.sourceTargetModelPath = sourceTargetModelPath;
		this.moduleName = moduleName;
		this.modulePath = modulePath;
	}

	/*
	 * load/inject models
	 */
	private void loadModels() throws FileNotFoundException {
		// load meta model
		metaModel = EmftvmFactory.eINSTANCE.createMetamodel();
		metaModel.setResource(umlRs.getResource(URI.createURI(metaModelPath),
				true));
		env.registerMetaModel(metaModelName, metaModel);

		// load source model
		// sourceModel = EmftvmFactory.eINSTANCE.createModel();
		// sourceModel.setResource(umlRs.getResource(
		// URI.createURI(sourceModelPath), true));
		// env.registerInputModel(sourceModelName, sourceModel);

		// load target model
		if (targetModelPath != "") {
			targetModel = EmftvmFactory.eINSTANCE.createModel();
			targetModel.setResource(umlRs.createResource(URI
					.createFileURI(targetModelPath)));
			env.registerOutputModel(targetModelName, targetModel);
		}
		// load optional combined source and target model
		sourceTargetModel = EmftvmFactory.eINSTANCE.createModel();
		sourceTargetModel.setResource(umlRs.getResource(
				URI.createURI(sourceTargetModelPath), true));
		env.registerInOutModel(sourceTargetModelName, sourceTargetModel);

		env.loadModule(mr, moduleName);
		td.finishLoading();
	}

	/*
	 * save models
	 */
	private void saveModels() throws ATLCoreException {
		try {
			// targetModel.getResource().save(Collections.emptyMap());

			if (targetModelPath != "")
				sourceTargetModel.getResource().setURI(
						URI.createURI(targetModelPath));
			
			sourceTargetModel.getResource().save(Collections.emptyMap());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * launch transformation
	 */
	public void launch() {
		try {
			loadModels();

			env.run(td);

			td.finish();

			saveModels();
			System.out.println("TEST: model transformation successful ...");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ATLCoreException e) {
			e.printStackTrace();
		}
	}
}
