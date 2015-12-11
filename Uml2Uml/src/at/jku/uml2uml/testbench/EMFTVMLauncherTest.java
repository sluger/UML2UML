package at.jku.uml2uml.testbench;

import at.jku.uml2uml.launcher.EMFTVMLauncher;

/*
 *  (c) Stefan Luger 2013
 *  Latest ATL EMFTVM transformation configuration launcher for testing purposes.
 *  Simply (un)comment and the model used as well as specify the ATL module scenario.
 */
public class EMFTVMLauncherTest {
	public static void main(String[] args) {
		String metaModelName = "UML2";
		String sourceModelName = "IN";
		String targetModelName = "OUT";
		String sourceTargetModelName = "INOUT";

		String metaModelPath = "http://www.eclipse.org/uml2/4.0.0/UML";

//		String sourceModelPath = "../Models/papyrus/models/LightSwitch.uml";
//		String targetModelPath = "../Models/papyrus/models/LightSwitch_Out.uml";
//		String sourceTargetModelPath = "../Models/papyrus/models/LightSwitch_InOut.uml";

//		 String sourceModelPath = "../Models/papyrus/models/VOD.uml";
//		 String targetModelPath = "../Models/papyrus/models/VOD_Out.uml";
//		 String sourceTargetModelPath =
//		 "../Models/papyrus/models/VOD_InOut.uml";

//		 String sourceModelPath = "../Models/papyrus/models/VOD_AR.uml";
//		 String targetModelPath = "../Models/papyrus/models/VOD_AR_Out.uml";
//		 String sourceTargetModelPath =
//		 "../Models/papyrus/models/VOD_AR_InOut.uml";

		 String sourceModelPath = "../Models/papyrus/models/Inheritance.uml";
		 String targetModelPath =
		 "../Models/papyrus/models/Inheritance_Out.uml";
		 String sourceTargetModelPath =
		 "../Models/papyrus/models/Inheritance_InOut.uml";

		// ATL module name and folder path
		String moduleName = "Scenario01";
		String modulePath = "../Transformations/inplace/";

		EMFTVMLauncher l = new EMFTVMLauncher(metaModelName, sourceModelName,
				targetModelName, sourceTargetModelName, metaModelPath,
				sourceModelPath, targetModelPath, sourceTargetModelPath,
				moduleName, modulePath);
		l.launch();
	}
}
