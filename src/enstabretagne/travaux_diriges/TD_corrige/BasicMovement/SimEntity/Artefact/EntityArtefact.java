package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.logger.ToRecord;
import enstabretagne.monitor.interfaces.IMovable;
import enstabretagne.simulation.components.IEntity;
import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.simulation.components.data.SimInitParameters;
import enstabretagne.simulation.components.implementation.SimEntity;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.Representation3D.EntityArtefact3DRepresentationInterface;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceur;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceur_Exemple1;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

@ToRecord(name = "Artefact")
public class EntityArtefact extends SimEntity implements IMovable, EntityArtefact3DRepresentationInterface  {

	private EntityMouvementSequenceur rmv;
	private EntityArtefactInit artefactInit;
	private EntityArtefactFeature aretefactFeatures;

	public EntityArtefact(String name, SimFeatures features) {
		super(name, features);
		aretefactFeatures = (EntityArtefactFeature) features;
	}

	@Override
	public void onParentSet() {

	}

	@Override
	protected void initializeSimEntity(SimInitParameters init) {
		artefactInit = (EntityArtefactInit) getInitParameters();

		rmv = (EntityMouvementSequenceur_Exemple1) createChild(EntityMouvementSequenceur_Exemple1.class, "monSequenceur",
				((EntityArtefactFeature) getFeatures()).getSeqFeature());
		rmv.initialize(artefactInit.getMvtSeqInitial());

	}

	@Override
	protected void BeforeActivating(IEntity sender, boolean starting) {

	}

	@Override
	protected void AfterActivate(IEntity sender, boolean starting) {
		Logger.Detail(this, "AfterActivate", "Activation de Navire");
		rmv.activate();
	}

	@ToRecord(name = "Position")
	@Override
	public Point3D getPosition() {
		return rmv.getPosition(getCurrentLogicalDate());
	}

	@ToRecord(name = "Vitesse")
	@Override
	public Point3D getVitesse() {
		return rmv.getVitesse(getCurrentLogicalDate());
	}

	@ToRecord(name = "Acceleration")
	@Override
	public Point3D getAcceleration() {
		return rmv.getAcceleration(getCurrentLogicalDate());
	}

	@ToRecord(name = "VitesseRotation")
	@Override
	public Point3D getVitesseRotationXYZ() {
		return rmv.getVitesseRotationXYZ(getCurrentLogicalDate());
	}

	@ToRecord(name = "AccelerationRotation")
	@Override
	public Point3D getAccelerationRotationXYZ() {
		return rmv.getAccelerationRotationXYZ(getCurrentLogicalDate());
	}

	@ToRecord(name = "Rotation")
	@Override
	public Point3D getRotationXYZ() {
		return rmv.getRotationXYZ(getCurrentLogicalDate());
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
	public Color getColor() {
		return aretefactFeatures.getCouleur();
	}

	@Override
	public double getRayon() {
		return aretefactFeatures.getRayon();
	}

	@Override
	public double getLongueur() {
		return aretefactFeatures.getTaille();
	}


}
