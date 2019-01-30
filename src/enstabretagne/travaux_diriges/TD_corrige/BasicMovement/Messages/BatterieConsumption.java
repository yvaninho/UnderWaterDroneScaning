package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Messages;

import enstabretagne.simulation.core.implementation.SimEvent;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurDrone;

public class BatterieConsumption extends SimEvent {
	private EntityMouvementSequenceurDrone droneMouvSeq;

	public BatterieConsumption(EntityMouvementSequenceurDrone droneMouvSeq) {

		this.droneMouvSeq = droneMouvSeq;
	}

	@Override
	public void Process() {
		// TODO Auto-generated method stub

	}

}
