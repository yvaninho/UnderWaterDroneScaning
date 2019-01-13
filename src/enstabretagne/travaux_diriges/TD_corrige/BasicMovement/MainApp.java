package enstabretagne.travaux_diriges.TD_corrige.BasicMovement;

import enstabretagne.monitor.implementation.FX3DMonitor2;

import enstabretagne.monitor.implementation.UniversalMonitor;

public class MainApp {

	public static void main(String[] args) {

		boolean AFAP = false;

		if (AFAP) {
			UniversalMonitor um = new UniversalMonitor();
			um.loadExperiencePlanFromSettings();
			um.runPlan();
			um.exit();
		} else
			FX3DMonitor2.launch(FX3DMonitor2.class, args);
		;

	}

}
