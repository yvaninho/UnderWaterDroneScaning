package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone;

import enstabretagne.simulation.components.data.SimInitParameters;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurInit;

public class EntityDroneInit extends SimInitParameters {
	private EntityMouvementSequenceurInit mvtSeqIni;
	private String name;
	
	public EntityDroneInit(String nom,EntityMouvementSequenceurInit mvtSeqIni)
	{
		this.mvtSeqIni = mvtSeqIni;
		this.name = nom;
	}
	
	public String getName() {
		return name;
	}
	
	public EntityMouvementSequenceurInit getMvtSeqInitial() {
		return  mvtSeqIni;
	}

}