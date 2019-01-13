package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur;

import java.util.HashMap;

import enstabretagne.monitor.interfaces.IMovable;
import enstabretagne.simulation.components.data.SimInitParameters;
import javafx.geometry.Point3D;

public class EntityMouvementSequenceurInit extends SimInitParameters {
	private IMovable etatInitial;
	private String name;
	
	private double maxLinearSpeed;
	private double maxPlongeeSpeed;
	private double maxUnderWaterSpeed;
	private double maxSelfRotationSpeed;
	private HashMap<String, Point3D> positionsCles;
	
	public double getMaxPlongeeSpeed() {
		return maxPlongeeSpeed;
	}
	
	public double getMaxUnderWaterSpeed() {
		return maxUnderWaterSpeed;
	}
	
	public EntityMouvementSequenceurInit(String nom,IMovable etatInitial,
											double maxLinearSpeed,
											double maxSelfRotationSpeed,
											double maxPlongeeSpeed,
											double maxUnderWaterSpeed,
											HashMap<String, Point3D> positionsCles)
	{
		this.etatInitial = etatInitial;
		this.name = nom;
		this.maxLinearSpeed=maxLinearSpeed;
		this.maxPlongeeSpeed = maxPlongeeSpeed;
		this.maxUnderWaterSpeed=maxUnderWaterSpeed;
		this.maxSelfRotationSpeed= maxSelfRotationSpeed;
		this.positionsCles = positionsCles;
	}
	
	public double getMaxLinearSpeed() {
		return maxLinearSpeed;
	}
	
	public double getMaxSelfRotationSpeed() {
		return maxSelfRotationSpeed;
	}
	
	public HashMap<String, Point3D> getPositionsCles() {
		return positionsCles;
	}	
	
	public String getName() {
		return name;
	}
	
	public IMovable getEtatInitial() {
		return etatInitial;
	}

}
