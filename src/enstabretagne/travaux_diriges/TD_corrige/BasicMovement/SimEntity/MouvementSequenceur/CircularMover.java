package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.monitor.implementation.MovableState;
import enstabretagne.monitor.implementation.XYZRotator;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.IMover;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

public class CircularMover implements IMover{

	MovableState initState;
	XYZRotator rotator;
	Point3D axeVersAvant;
	Point3D axeRot;
	
	Rotate rotPos;
	Rotate rotVit;
	double vRot;
	Point3D centre;
	double r;
	
	Point3D cible;
	Point3D vecteurRayon;
	
	/*
	 * LogicalDateTime d: date d'initialisation du mover
	 * Point3D posInit : position initiale au moment de l'activation du mover
	 * Point3D cible : cible que le mover doit atteindre par un mouvement circulaire.
	 * Rq: le diamètre du demi-cercle est fait du segment [posInit;cible]
	 * 
	 * Point3D vIni : vecteur vitesse initiale. il déterminera le sens du cercle en fonction de la cible
	 */
	public CircularMover(LogicalDateTime d, Point3D posInit,Point3D vIni,Point3D cible) {
		
		initState = new MovableState(posInit, vIni, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO,
				Point3D.ZERO);
		initState.setT(d);
		InitCircularMover(initState, cible);
		
	}
	
	public void InitCircularMover(MovableState initState,Point3D cible) {
		rotator = new XYZRotator();
		this.cible = cible;
		Point3D targetDir = cible.subtract(initState.getPosition());
		centre = targetDir.multiply(.5).add(initState.getPosition());
		vecteurRayon=initState.getPosition().subtract(centre);
		r=vecteurRayon.magnitude();
		
		axeVersAvant = initState.getVitesse(); 
		
		this.axeRot = axeVersAvant.crossProduct(targetDir);
		if(axeRot.equals(Point3D.ZERO))
			this.axeRot = Rotate.Z_AXIS;
		
		rotPos = new Rotate();
		rotPos.setAxis(axeRot);
		rotPos.setPivotX(centre.getX());
		rotPos.setPivotY(centre.getY());
		rotPos.setPivotZ(centre.getZ());

		rotVit = new Rotate();
		rotVit.setAxis(axeRot);

		vRot = initState.getVitesse().magnitude()/r*180/Math.PI;
		
	}


	public Point3D getPosition(LogicalDateTime d) {
		double dt = d.soustract(initState.getT()).DoubleValue();
		rotPos.setAngle(vRot*dt);

		Point3D ptemp=initState.getPosition();
		Point3D nVecteurRayon=rotPos.transform(ptemp);
		return nVecteurRayon;
	}

	public Point3D getVitesse(LogicalDateTime d) {
		double dt = d.soustract(initState.getT()).DoubleValue();
		Point3D v;
		rotVit.setAngle(vRot*dt);
		v= rotVit.transform(initState.getVitesse());
				
		return v;
	}
	public Point3D getAcceleration(LogicalDateTime d) {
		return axeVersAvant.crossProduct(axeRot).multiply(initState.getVitesse().magnitude()*initState.getVitesse().magnitude()/r);
	}
	
	
	public Point3D getRotationXYZ(LogicalDateTime d) {
		Point3D v = getVitesse(d);
		Point3D vHor = new Point3D(v.getX(), v.getY(), 0);
		Point3D xyz;  
		if(vHor.equals(Point3D.ZERO))
			xyz = initState.getRotationXYZ();
		else {
			double angle = Rotate.X_AXIS.angle(vHor)*Math.signum((Rotate.X_AXIS.crossProduct(vHor)).dotProduct(Rotate.Z_AXIS));

			xyz = new Point3D(0,0,angle);
		}
		
		
		return xyz;
		
	}

	public Point3D getVitesseRotationXYZ(LogicalDateTime d) {
		return initState.getVitesseRotationXYZ();


	}
	public Point3D getAccelerationRotationXYZ(LogicalDateTime d) {
		return initState.getAccelerationRotationXYZ();
	}

	@Override
	public LogicalDuration getDurationToReach() {
		double v = initState.getVitesse().magnitude();
		if(v!=0) {
			double dt = vecteurRayon.magnitude()*Math.PI/v;
			return LogicalDuration.ofSeconds(dt);
		}
		else
		return LogicalDuration.POSITIVE_INFINITY;
	}
}
