package be.SimEntity.MouvementSequenceur;


import be.SimEntity.Bouee.IMover;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.monitor.implementation.MovableState;
import enstabretagne.monitor.implementation.XYZRotator;
import javafx.geometry.Point3D;

public class RectilinearMover implements IMover{

	MovableState initState;
	Point3D target;
	Point3D direction;

	/*
	 * LogicalDateTime d: date d'initialisation du mover
	 * Point3D posInit : position initiale au moment de l'activation du mover
	 * Point3D target : cible que le mover doit atteindre
	 * double vIni : vitesse initiale
	 * 
	 */

	public RectilinearMover(LogicalDateTime d, Point3D posInit, Point3D target, double vIni) {
		
		this.target = target;
		direction = target.subtract(posInit);


		Point3D vInit = target.subtract(posInit).normalize().multiply(vIni);
		Point3D rotationXYZ =XYZRotator.computeRotationXYZ(posInit, direction);

		initState = new MovableState(posInit, vInit, Point3D.ZERO, rotationXYZ, Point3D.ZERO, Point3D.ZERO);
		initState.setT(d);
		
	}
	public RectilinearMover(LogicalDateTime d, Point3D posInit, Point3D orientationIni, Point3D target, double vIni) {
		
		this.target = target;
		direction = target.subtract(posInit);


		Point3D vInit = target.subtract(posInit).normalize().multiply(vIni);

		Point3D rotationXYZ =XYZRotator.computeRotationXYZ(posInit, orientationIni);

		initState = new MovableState(posInit, vInit, Point3D.ZERO, rotationXYZ, Point3D.ZERO, Point3D.ZERO);
		initState.setT(d);
		
	}
	

	public LogicalDuration getDurationToReach() {
		double d = direction.magnitude();
		double v = initState.getVitesse().magnitude();
		if(v != 0)
			return LogicalDuration.ofSeconds(d/v);
		else
			return LogicalDuration.POSITIVE_INFINITY;
					
	}
	
	public Point3D getPosition(LogicalDateTime d) {
		Point3D p = Point3D.ZERO;
		double dt = d.soustract(initState.getT()).DoubleValue();

			double s=getVitesse(d).magnitude();
			p= initState.getPosition().add(initState.getVitesse().normalize().multiply(dt*s));

			return p;
	}

	public Point3D getVitesse(LogicalDateTime d) {
		double dt = d.soustract(initState.getT()).DoubleValue();
		return initState.getVitesse().add(initState.getAcceleration().multiply(dt));
	}
	public Point3D getAcceleration(LogicalDateTime d) {
		return initState.getAcceleration();
	}

	public Point3D getVitesseRotationXYZ(LogicalDateTime currentLogicalDate) {
		return initState.getVitesseRotationXYZ();
	}

	public Point3D getAccelerationRotationXYZ(LogicalDateTime currentLogicalDate) {
		return initState.getAccelerationRotationXYZ();
	}

	public Point3D getRotationXYZ(LogicalDateTime currentLogicalDate) {
		return initState.getRotationXYZ();
	}

	
}
