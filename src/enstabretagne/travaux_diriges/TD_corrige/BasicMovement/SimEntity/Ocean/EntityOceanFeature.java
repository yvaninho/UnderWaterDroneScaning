package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean;

import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurFeature;

public class EntityOceanFeature extends SimFeatures {

	
	public EntityOceanFeature(String id) {
		super(id);
	}
	
	public EntityMouvementSequenceurFeature getSeqFeature() {
		return null;
	}


}
