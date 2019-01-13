package be.SimEntity.Artefact;

import be.SimEntity.MouvementSequenceur.EntityMouvementSequenceurInit;
import enstabretagne.simulation.components.data.SimInitParameters;

public class EntityArtefactInit extends SimInitParameters {
	private EntityMouvementSequenceurInit mvtSeqIni;
	private String name;
	
	public EntityArtefactInit(String nom,EntityMouvementSequenceurInit mvtSeqIni)
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
