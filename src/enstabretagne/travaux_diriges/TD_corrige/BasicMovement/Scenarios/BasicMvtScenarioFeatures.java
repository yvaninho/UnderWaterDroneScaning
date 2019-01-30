package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Scenarios;

import java.util.HashMap;

import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefactFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefactInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.BoueeFeatures;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.BoueeInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDroneFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDroneInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavireFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavireInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.EntityOceanFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.EntityOceanInit;

public class BasicMvtScenarioFeatures extends SimFeatures {

	private HashMap<BoueeFeatures, BoueeInit> bouees;
	private HashMap<EntityArtefactFeature, EntityArtefactInit> artefactes;
	private HashMap<EntityNavireFeature, EntityNavireInit> navires;
	private HashMap<EntityOceanFeature, EntityOceanInit> ocean;
	private HashMap<EntityDroneFeature, EntityDroneInit> drones;

	public HashMap<EntityOceanFeature, EntityOceanInit> getOcean() {
		return ocean;
	}

	public HashMap<EntityNavireFeature, EntityNavireInit> getNavires() {
		return navires;
	}

	public HashMap<BoueeFeatures, BoueeInit> getBouees() {
		return bouees;
	}

	public HashMap<EntityArtefactFeature, EntityArtefactInit> getArtefactes() {
		return artefactes;
	}

	public HashMap<EntityDroneFeature, EntityDroneInit> getDrones() {
		return drones;
	}
	
	public BasicMvtScenarioFeatures(String id) {
		super(id);
		bouees = new HashMap<>();
		navires = new HashMap<>();
		ocean = new HashMap<>();
		artefactes = new HashMap<>();
		drones = new HashMap<>();
	}

}
