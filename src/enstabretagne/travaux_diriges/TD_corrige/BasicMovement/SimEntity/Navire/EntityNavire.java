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
import enstabretagne.simulation.core.implementation.SimEvent;
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

@ToRecord(name = "Navire")
public class EntityNavire extends SimEntity implements IMovable, EntityNavire3DRepresentationInterface {

	private EntityMouvementSequenceur rmv;
	private EntityNavireInit NavireInit;
	private EntityNavireFeature NavireFeature;
	private List<EntityDrone> drones;
	private int nbDroneseMax;

	public EntityNavire(String name, SimFeatures features) {
		super(name, features);
		NavireFeature = (EntityNavireFeature) features;
		drones = new ArrayList<EntityDrone>();
		nbDroneseMax = 5;
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
		Post(new DroneLunch(), getCurrentLogicalDate().add(LogicalDuration.ofSeconds(1)));

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
		Logger.Information(this, "BeforeDeactivate", "Sur le point de se désactiver");
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

			Logger.Detail(this, "DroneLunch.Process", "Création du sous marin");
			int zPlongee = 0;
			// Création du drone et des points de passage
			HashMap<String, Point3D> positionsCles = new HashMap<String, Point3D>();
			positionsCles.put("start", getPosition());
			positionsCles.put("plongee", new Point3D(getPosition().getX(), getPosition().getY(), zPlongee));
			Point3D A = new Point3D(getPosition().getX(), getPosition().getY(), zPlongee);
			int rayon = 10000;
			int xb = (int) (getPosition().getX()
					+ (rayon * Math.cos(0.2 + 2 * Math.PI * (drones.size() / (double) nbDroneseMax))));
			int yb = (int) (getPosition().getY()
					+ (rayon * Math.sin(0.2 + 2 * Math.PI * (drones.size() / (double) nbDroneseMax))));
			Point3D B = new Point3D(xb, yb, zPlongee);
			Logger.Detail(this, "DroneLunch.Process",
					"Création du sous marin au point " + (drones.size() / nbDroneseMax) + " " + B);

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

				Post(new DroneLunch(), getCurrentLogicalDate().add(LogicalDuration.ofSeconds(600)));
			}

		}

	}
}
