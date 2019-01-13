package be.SimEntity.Navire;

import be.SimEntity.MouvementSequenceur.EntityMouvementSequenceurInit;
import enstabretagne.simulation.components.data.SimInitParameters;

public class EntityNavireInit extends SimInitParameters {
	private EntityMouvementSequenceurInit mvtSeqIni;
	private String name;
	
	public EntityNavireInit(String nom,EntityMouvementSequenceurInit mvtSeqIni)
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
