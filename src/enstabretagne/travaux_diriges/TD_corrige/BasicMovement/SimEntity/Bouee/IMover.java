package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import javafx.geometry.Point3D;

public interface IMover {
	LogicalDuration getDurationToReach();
	public Point3D getPosition(LogicalDateTime d);
	public Point3D getVitesse(LogicalDateTime d);
	public Point3D getAcceleration(LogicalDateTime d);


	public Point3D getRotationXYZ(LogicalDateTime d);
	public Point3D getVitesseRotationXYZ(LogicalDateTime d);
	public Point3D getAccelerationRotationXYZ(LogicalDateTime d);

}
