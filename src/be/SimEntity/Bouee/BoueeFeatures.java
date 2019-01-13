package be.SimEntity.Bouee;

import be.SimEntity.MouvementSequenceur.EntityMouvementSequenceurFeature;
import enstabretagne.simulation.components.data.SimFeatures;

public class BoueeFeatures extends SimFeatures {

	private double vitesseMax;
	private double accelerationMax;
	private double taille;
	
	public double getVitesseMax() {
		return vitesseMax;
	}
	
	public double getAccelerationMax() {
		return accelerationMax;
	}
	
	public BoueeFeatures(String id,double vitesseMax,double accelerationMax,double taille) {
		super(id);
		this.vitesseMax = vitesseMax;
		this.accelerationMax = accelerationMax;
		this.taille = taille;
	}

	public double getTaille() {
		return taille;
	}

	public EntityMouvementSequenceurFeature getSeqFeature() {
		return null;
	}

}
