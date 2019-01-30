package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.logger.ToRecord;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.monitor.implementation.MovableState;
import enstabretagne.monitor.interfaces.IMovable;
import enstabretagne.simulation.components.IEntity;
import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.simulation.components.data.SimInitParameters;
import enstabretagne.simulation.components.implementation.SimEntity;
import enstabretagne.simulation.core.ISimObject;
import enstabretagne.simulation.core.implementation.SimEvent;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Messages.Messages;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefact;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.Bouee;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.BoueeInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDrone;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDroneFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDroneInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceur;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurDrone;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.Representation3D.EntityNavire3DRepresentationInterface;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurDrone.MissionCompleted;

@ToRecord(name = "Navire")
public class EntityNavire extends SimEntity implements IMovable, EntityNavire3DRepresentationInterface {

	private EntityMouvementSequenceur rmv;
	private EntityNavireInit NavireInit;
	private EntityNavireFeature NavireFeature;
	private List<EntityDrone> drones;
	private List<EntityArtefact> listDiscoveredArtefact;
	private int nbDroneseMax;

	public EntityNavire(String name, SimFeatures features) {
		super(name, features);
		NavireFeature = (EntityNavireFeature) features;
		drones = new ArrayList<EntityDrone>();
		nbDroneseMax = 5;
		listDiscoveredArtefact = new ArrayList<EntityArtefact>();
	}

	@Override
	public void onParentSet() {

	}

	@Override
	protected void initializeSimEntity(SimInitParameters init) {
		NavireInit = (EntityNavireInit) getInitParameters();

		rmv = (EntityMouvementSequenceur) createChild(EntityMouvementSequenceur.class, "monSequenceur",
				((EntityNavireFeature) getFeatures()).getSeqFeature());
		rmv.initialize(NavireInit.getMvtSeqInitial());

	}

	@Override
	protected void BeforeActivating(IEntity sender, boolean starting) {

	}

	@Override
	protected void AfterActivate(IEntity sender, boolean starting) {
		Logger.Detail(this, "AfterActivate", "Activation de Navire");
		rmv.activate();
		Post(new DroneLunch());

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
		Logger.Information(this, "BeforeDeactivate", "Sur le point de se d�sactiver");
		for (EntityDrone drone : drones) {
			drone.deactivate();
			drone.terminate(starting);
		}

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
		return NavireFeature.getCouleur();
	}

	@Override
	public double getRayon() {
		return NavireFeature.getRayon();
	}

	@Override
	public double getLongueur() {
		return NavireFeature.getTaille();
	}

	public class DroneLunch extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub

			Logger.Detail(this, "DroneLunch.Process", String.format(Messages.DroneLaunch,(drones.size()+1)));
			int zPlongee = -10;
			// Cr�ation du drone et des points de passage
			HashMap<String, Point3D> positionsCles = new HashMap<String, Point3D>();
			positionsCles.put("start", getPosition());
			positionsCles.put("plongee", new Point3D(getPosition().getX(), getPosition().getY(), zPlongee));
			Point3D A = new Point3D(getPosition().getX(), getPosition().getY(), zPlongee);
			int rayon = 800;
			int xb = (int) (getPosition().getX()
					+ (rayon * Math.cos( 2 * Math.PI * (drones.size() / (double) nbDroneseMax))));
			int yb = (int) (getPosition().getY()
					+ (rayon * Math.sin( 2 * Math.PI * (drones.size() / (double) nbDroneseMax))));
			Point3D B = new Point3D(xb, yb, zPlongee);
/*			Logger.Detail(this, "DroneLunch.Process",
					"Cr�ation du sous marin au point " + (drones.size() / nbDroneseMax) + " " + B);*/

			positionsCles.put("A", A);
			positionsCles.put("B", B);
			int nbPoints = 10;
			MovableState mst;
			EntityMouvementSequenceurInit msi;
			EntityMouvementSequenceurFeature feat;
			mst = new MovableState(getPosition(), new Point3D(0, 0, 0), Point3D.ZERO, new Point3D(-100, 0, 0.0),
					new Point3D(0, 0, 0.0), Point3D.ZERO);
			msi = new EntityMouvementSequenceurInit("MSI", mst, 10, 100, 2, 8, positionsCles, nbPoints);
			feat = new EntityMouvementSequenceurFeature("MSF");
			EntityDroneInit i = new EntityDroneInit("Drone Observation " + (drones.size() + 1), msi, positionsCles,
					nbPoints);
			EntityDroneFeature f = new EntityDroneFeature("Drone", 10, 10, Color.BLACK, feat);
			SimEntity b = createChild(EntityDrone.class, i.getName(), f);
			b.initialize(i);
			b.activate();
			drones.add((EntityDrone) b);

			if (drones.size() < nbDroneseMax) {

				Post(new DroneLunch(), getCurrentLogicalDate().add(LogicalDuration.ofMinutes(10)));
			}

		}

	}

	public class ReceiveInfosArtefact extends SimEvent {

		private EntityArtefact artefact;

		public ReceiveInfosArtefact(EntityArtefact artefact_) {
			// TODO Auto-generated constructor stub
			this.artefact = artefact_;

		}

		@Override
		public void Process() {
			// TODO Auto-generated method stub

			Logger.Information(Owner(), " Start ReceiveInfosArtefact ", " ReceiveInfosArtefact ");
			listDiscoveredArtefact.add(artefact);

			if (artefact.getName().equals("Objet0")) {

				// On demande � tous les drones de revenir � la base.
				List<ISimObject> objectsSeqDrones = getEngine().requestSimObject(this::isDroneSequenceur);
				EntityMouvementSequenceurDrone droneSeq;
				System.out.println(" taille " + objectsSeqDrones.size());
				for (ISimObject objet : objectsSeqDrones) {
					
					droneSeq = (EntityMouvementSequenceurDrone) objet;
					droneSeq.Post(droneSeq.new MissionCompleted(), LogicalDuration.ofSeconds(3));

				}

			}

		}

		private boolean isDroneSequenceur(ISimObject o) {

			if (o instanceof EntityMouvementSequenceurDrone) {
				return true;
			}
			return false;
		}
	}

}
