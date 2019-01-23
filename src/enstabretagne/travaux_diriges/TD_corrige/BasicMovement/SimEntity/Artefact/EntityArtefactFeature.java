package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact;

import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.Representation3D.TypeArtefact;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurFeature;
import javafx.scene.paint.Color;

public class EntityArtefactFeature extends SimFeatures {

	private double taille;
	private double rayon;
	private Color couleur;
	private TypeArtefact typeDeArtefact;
	private EntityMouvementSequenceurFeature seq;
	
	public EntityArtefactFeature(String id,double taille,double rayon,Color couleur,EntityMouvementSequenceurFeature seq, TypeArtefact typeDeArtefact) {
		super(id);
		this.taille = taille;
		this.couleur = couleur;
		this.rayon=rayon;
		this.seq=seq;
		this.typeDeArtefact = typeDeArtefact;

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
	public TypeArtefact getTypeArtefact() {
		return typeDeArtefact;
	}

}
