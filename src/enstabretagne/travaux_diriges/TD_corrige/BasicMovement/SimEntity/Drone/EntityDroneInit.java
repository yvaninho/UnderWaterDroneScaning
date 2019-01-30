package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone;

import java.util.HashMap;

import enstabretagne.simulation.components.data.SimInitParameters;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurInit;
import javafx.geometry.Point3D;

public class EntityDroneInit extends SimInitParameters {
	private EntityMouvementSequenceurInit mvtSeqIni;
	private String name;
	private HashMap<String, Point3D> positionsCles;
	private int nbPoints;
	
	public EntityDroneInit(String nom, EntityMouvementSequenceurInit mvtSeqIni, HashMap<String, Point3D> points, int nbPoints) {
		this.mvtSeqIni = mvtSeqIni;
		this.name = nom;
		this.positionsCles = points;
		this.nbPoints = nbPoints ;

	}

	public String getName() {
		return name;
	}

	public EntityMouvementSequenceurInit getMvtSeqInitial() {
		return mvtSeqIni;
	}

	public HashMap<String, Point3D> getPointsCles() {

		return positionsCles;
	}
	public int getNbPoints () {
		
		return this.nbPoints  ;
	}

}
