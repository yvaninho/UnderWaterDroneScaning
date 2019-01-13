package be.Scenarios;

import java.util.HashMap;

import be.SimEntity.Artefact.EntityArtefactFeature;
import be.SimEntity.Artefact.EntityArtefactInit;
import be.SimEntity.Bouee.BoueeFeatures;
import be.SimEntity.Bouee.BoueeInit;
import be.SimEntity.Navire.EntityNavireFeature;
import be.SimEntity.Navire.EntityNavireInit;
import be.SimEntity.Ocean.EntityOceanFeature;
import be.SimEntity.Ocean.EntityOceanInit;
import enstabretagne.simulation.components.data.SimFeatures;

public class BasicMvtScenarioFeatures extends SimFeatures {

	private HashMap<BoueeFeatures, BoueeInit> bouees;
	private HashMap<EntityArtefactFeature, EntityArtefactInit> artefactes;
	private HashMap<EntityNavireFeature, EntityNavireInit> navires;
	private HashMap<EntityOceanFeature, EntityOceanInit> ocean;

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

	public BasicMvtScenarioFeatures(String id) {
		super(id);
		bouees = new HashMap<>();
		navires = new HashMap<>();
		ocean = new HashMap<>();
		artefactes = new HashMap<>();
	}

}
