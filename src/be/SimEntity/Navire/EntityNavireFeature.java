package be.SimEntity.Navire;

import be.SimEntity.MouvementSequenceur.EntityMouvementSequenceurFeature;
import enstabretagne.simulation.components.data.SimFeatures;
import javafx.scene.paint.Color;

public class EntityNavireFeature extends SimFeatures {

	private double taille;
	private double rayon;
	private Color couleur;
	
	private EntityMouvementSequenceurFeature seq;
	
	public EntityNavireFeature(String id,double taille,double rayon,Color couleur,EntityMouvementSequenceurFeature seq) {
		super(id);
		this.taille = taille;
		this.couleur = couleur;
		this.rayon=rayon;
		this.seq=seq;

	}

	public Color getCouleur() {
		return couleur;
	}
	
	public double getRayon() {
		return rayon;
	}
	public double getTaille() {
		return taille;
	}

	public EntityMouvementSequenceurFeature getSeqFeature() {
		return seq;
	}

}
