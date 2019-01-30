package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.logger.ToRecord;
import enstabretagne.monitor.interfaces.IMovable;
import enstabretagne.simulation.components.IEntity;
import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.simulation.components.data.SimInitParameters;
import enstabretagne.simulation.components.implementation.SimEntity;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.Representation3D.IBoueeRepresentation3D;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceur;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

@ToRecord(name="Bouee")
public class Bouee extends SimEntity implements IMovable,IBoueeRepresentation3D{
	

	
	private EntityMouvementSequenceur rmv;
	private boolean isDetected = false ;


	public Bouee(String name, SimFeatures features) {
		super(name, features);
	}

	@Override
	public void onParentSet() {
		
	}

	@Override
	protected void initializeSimEntity(SimInitParameters init) {
		BoueeInit bi = (BoueeInit) getInitParameters();
		
		rmv = (EntityMouvementSequenceur) createChild(EntityMouvementSequenceur.class, "monSequenceur", ((BoueeFeatures) getFeatures()).getSeqFeature());
		rmv.initialize(bi.getMvtSeqInit());
	}

	@Override
	protected void BeforeActivating(IEntity sender, boolean starting) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void AfterActivate(IEntity sender, boolean starting) {
		Logger.Detail(this, "AfterActivate", "Activation de la bou�e %s","test");
		rmv.activate();
//		Logger.Data(this);
	}
	

	@ToRecord(name="Position")
	@Override
	public Point3D getPosition() {
		return rmv.getPosition(getCurrentLogicalDate());
	}

	@ToRecord(name="Vitesse")
	@Override
	public Point3D getVitesse() {
		return rmv.getVitesse(getCurrentLogicalDate());
	}

	@ToRecord(name="Acceleration")
	@Override
	public Point3D getAcceleration() {
		return rmv.getAcceleration(getCurrentLogicalDate());
	}

	@Override
	public Color getColor() {
		return ((BoueeInit) getInitParameters()).getColor();
	}

	@Override
	public double getSize() {
		return ((BoueeFeatures) getFeatures()).getTaille();
	}

	@Override
	protected void BeforeDeactivating(IEntity sender, boolean starting) {
		rmv.deactivate();		
	}

	@Override
	protected void AfterDeactivated(IEntity sender, boolean starting) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void AfterTerminated(IEntity sender, boolean restart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point3D getVitesseRotationXYZ() {
		return rmv.getVitesseRotationXYZ(getCurrentLogicalDate());
	}

	@Override
	public Point3D getAccelerationRotationXYZ() {
		return rmv.getAccelerationRotationXYZ(getCurrentLogicalDate());
	}

	@Override
	public Point3D getRotationXYZ() {
		return rmv.getRotationXYZ(getCurrentLogicalDate());
	}

	public boolean isDetected() {
		return isDetected;
	}

	public void setDetected(boolean isDetected) {
		this.isDetected = isDetected;
	}

}
