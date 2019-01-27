package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.logger.ToRecord;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.simulation.components.IEntity;
import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.simulation.components.data.SimInitParameters;
import enstabretagne.simulation.core.ISimObject;
import enstabretagne.simulation.core.implementation.SimEvent;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefact;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.IMover;
import javafx.geometry.Point3D;

@ToRecord(name = "MouvementSequenceur")
public class EntityMouvementSequenceurDrone extends EntityMouvementSequenceur implements IMover {

	// Variable Pour savoir si la mission est terminée
	protected boolean missionCompleted = false;
	protected HashMap<String, Point3D> pointsClefs;
	// variable pour savoir si le sous-marin est entrain de se diriger vers un
	// artefact qu'il a préalablement détecté
	protected boolean isTrackingArtefact = false;
	// Objet représentant l'artefact qu'on poursuit
	protected EntityArtefact target;
	// variable pour mémoiriser la position courante et le type phase afin de
	// continuer après avoir atteint une cible
	protected Point3D positionBeforeInterruption;
	protected int positionNumberBeforeInterruption;

	/*
	 * Définition des différentes vitesses
	 * 
	 */
	// vitesse en surface 4m/s

	private int vitesseEnsurface = 4;
	// vitesse de plongée du drone est de 1m/s
	private int vitesseDescente = 1;
	// vitesse de remontée en surface à une vitesse de 2m/s
	private int vitesseMontee = 2;
	// vitesse du drone sous l'eau
	private int vitesseSousleau = 3;

	public EntityMouvementSequenceurDrone(String name, SimFeatures features) {
		super(name, features);
	}

	@Override
	public void onParentSet() {

	}

	@Override
	protected void initializeSimEntity(SimInitParameters init) {
		super.initializeSimEntity(init);
	}

	@Override
	protected void AfterActivate(IEntity sender, boolean starting) {
		Logger.Detail(this, "AfterActivate", "Activation de MouvementSequenceur");

		Post(new FinLinearPhase(0), LogicalDuration.ofSeconds(1));
		// on lance le premier scan
		Post(new ScanOcean(), LogicalDuration.ofSeconds(2));
	}

	@Override
	public void setPointsClefs(HashMap<String, Point3D> pointsClefs, int nbPoints) {

		LogicalDateTime d = getCurrentLogicalDate();
		this.pointsClefs = pointsClefs;
		Point3D A = pointsClefs.get("A");
		Point3D B = pointsClefs.get("B");
		int amplitude = (int) (B.getY() - A.getY());
		int L = (int) (B.getX() - A.getX());
		String pointName = "";
		int x;
		int y;
		int k = 1;
		int zPlongee = 0;
		for (int i = 0; i <= nbPoints; i = i + 2) {

			pointName = "PointCible" + i;
			x = ((L * i) / nbPoints);
			y = (amplitude * (k % 2));
			pointsClefs.put(pointName, new Point3D(x, y, zPlongee));
			x = (L * (i + 1)) / nbPoints;
			y = (amplitude * (k % 2));
			pointName = "PointCible" + (i + 1);
			pointsClefs.put(pointName, new Point3D(x, y, zPlongee));
			k++;
		}

	}

	public class FinLinearPhase extends SimEvent {

		private int numPointCle;

		public FinLinearPhase(int numPointCle) {
			this.numPointCle = numPointCle;

		}

		@Override
		public void Process() {

			if (!isTrackingArtefact && this.numPointCle < ini.getPositionsCles().size() - 4) {

				// On sauvegarde le numéro du Point
				positionNumberBeforeInterruption = this.numPointCle;
				Logger.Information(Owner(), "Process FinLinear", "Fin de la  phase Linéaire  N °" + this.numPointCle);
				LogicalDateTime d = getCurrentLogicalDate();
				rectilinearMover = new RectilinearMover(d, mv.getPosition(d),
						ini.getPositionsCles().get("PointCible" + numPointCle), ini.getMaxLinearSpeed());
				mv = rectilinearMover;
				Logger.Information(Owner(), "Process FinLinearPhase N °" + this.numPointCle,
						"Phase mouvement circulaire enclenché");
				Post(new CircularPhase(this.numPointCle + 1), mv.getDurationToReach());

			}

		}
	}

	public class CircularPhase extends SimEvent {

		private int numPointClef;

		public CircularPhase(int numPointClef) {

			this.numPointClef = numPointClef;
		}

		@Override
		public void Process() {

			if (isTrackingArtefact) {

			} else {

				Logger.Information(Owner(), "Process Circulaire ", "Fin N ° " + this.numPointClef);

				LogicalDateTime d = getCurrentLogicalDate();
				// on sauvegarde les paramètres
				positionNumberBeforeInterruption = numPointClef;
				circulrMover = new CircularMover(d, mv.getPosition(d),
						mv.getVitesse(d).normalize().multiply(ini.getMaxLinearSpeed()),
						ini.getPositionsCles().get("PointCible" + this.numPointClef));
				mv = circulrMover;
				Logger.Information(Owner(), "Process Circular",
						"Phase mouvement circulaire enclenché " + this.numPointClef);

				Post(new FinLinearPhase(this.numPointClef + 1), mv.getDurationToReach());

			}
		}
	}

	public class ScanOcean extends SimEvent {

		@Override
		public void Process() {

			Logger.Information(Owner(), "Process Scan ocean", "start scan ocean  ");
			LogicalDateTime d = getCurrentLogicalDate();
			Logger.Information(Owner(), "Process Scan ocean", "Process Scan ocean ");

			// faire le choix de la bouee la plus proche

			List<ISimObject> objectsNear = getEngine().requestSimObject(this::isNear);
			ArrayList listMagnitude = new ArrayList();
			int indexMin = 0, magnitudeMin = Integer.MAX_VALUE, magnitudeCourante = 0;

			// choisir l'artefact le plus proche
			EntityArtefact monArtefact;

			if (objectsNear.size() > 0 && target == null) {

				for (ISimObject object : objectsNear) {

					monArtefact = (EntityArtefact) object;
					magnitudeCourante = (int) monArtefact.getPosition().subtract(getPosition(d)).magnitude();

					if (magnitudeCourante < magnitudeMin) {

						magnitudeMin = magnitudeCourante;
						target = monArtefact;

					}

					System.out.println("Position " + monArtefact.getPosition());

				}

				target.setTracked(true);
				objectsNear.remove(target);
				Post(new Plongee(), LogicalDuration.ofSeconds(1));

			}

			// if (!missionCompleted)
			Post(new ScanOcean(), d.add(LogicalDuration.ofMinutes(1)));

		}

		private boolean isNear(ISimObject o) {

			int portee = 5000;
			if (o == this)
				return false;

			if (o instanceof EntityArtefact) {

				LogicalDateTime time = getCurrentLogicalDate();
				EntityArtefact object = (EntityArtefact) o;
				// ajouter une condition pour ne plus selectionner les artefact déjà selectionné
				return object.getPosition().subtract(mv.getPosition(time)).magnitude() < portee && !object.isDetected()
						&& !object.isTracked();

			}
			return false;
		}

	}

	public class Plongee extends SimEvent {

		@Override
		public void Process() {

			Logger.Information(Owner(), "Process Plongee", " Debut plongée  ");
			LogicalDateTime d = getCurrentLogicalDate();
			// On calcule les coordonnées du point de plongée

			Point3D plongee = getPosition(d).add(0, 0, ini.getPositionsCles().get("plongee").getZ());
			// rectilinearMover = new RectilinearMover(d, mv.getPosition(d), plongee,
			// ini.getMaxLinearSpeed());
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), plongee, vitesseDescente);

			mv = rectilinearMover;

			Post(new TrackTarget(), d.add(LogicalDuration.ofSeconds(1)));
		}
	}

	public class TrackTarget extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub
			isTrackingArtefact = true;
			Logger.Information(Owner(), "Process trackTarget", "start Track Target");
			LogicalDateTime d = getCurrentLogicalDate();
			double rayon = 2;
			Point3D monPoint = new Point3D(1, 0, 0).multiply(rayon);

			// On sauvegarde la position
			positionBeforeInterruption = mv.getPosition(d);
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), target.getPosition().add(monPoint),
					vitesseSousleau);
			mv = rectilinearMover;
			Logger.Information(Owner(), "Process TrackTarget", "TrackTarget");
			Post(new TurnAround(10), mv.getDurationToReach());
			// Post(new Arret(), mv.getDurationToReach());
		}
	}

	public class TurnAround extends SimEvent {

		private double theta;

		public TurnAround(double theta) {

			this.theta = theta;
		}

		@Override
		public void Process() {
			// TODO Auto-generated method stub
			isTrackingArtefact = true;
			Logger.Information(Owner(), "Process turn around target ", "start turn around Target");
			LogicalDateTime d = getCurrentLogicalDate();
			double rayon = 2;
			Point3D monPoint = new Point3D(rayon * Math.cos((theta * Math.PI) / (180)),
					rayon * Math.sin((theta * Math.PI) / (180)), 0);
			Point3D currentPosition = target.getPosition().add(monPoint);
			// circulrMover = new CircularMover(d,
			// mv.getPosition(d),mv.getVitesse(d).normalize().multiply(ini.getMaxLinearSpeed()),
			// currentPosition);
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), currentPosition, 10);
			mv = rectilinearMover;
			// mv = circulrMover;
			Logger.Information(Owner(), "Turn around ", "Phase Turn around angle  " + this.theta);
			if (theta < 360) {
				Post(new TurnAround(this.theta + 10), mv.getDurationToReach());
			} else {

				Post(new ClassifyTarget(), d.add(LogicalDuration.ofSeconds(10)));

			}

		}

	}

	public class ClassifyTarget extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub

			Logger.Information(Owner(), "Classify Target ",
					"Phase Classify Target " + target.getName() + " " + getParent().getName());
			LogicalDateTime d = getCurrentLogicalDate();
			if (target != null) {

				if (target.getName().equals("Objet0")) {
					Logger.Information(Owner(), "Classify Target ", " Object found " + getPosition(d));

				}
				target.setDetected(true);
				target = null;
				isTrackingArtefact = false;
				Post(new ComeBack(), d.add(LogicalDuration.ofSeconds(1)));
			}
		}

	}

	public class ComeBack extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub

			Logger.Information(Owner(), "Process turn come back  ", "start come back");
			LogicalDateTime d = getCurrentLogicalDate();

			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), positionBeforeInterruption, 10);
			mv = rectilinearMover;

			Logger.Information(Owner(), "Come back ",
					"Phase come back  " + mv.getDurationToReach() + positionBeforeInterruption);

			// on termine le mouvement en cours d'éxécution

			if (positionNumberBeforeInterruption % 2 == 0) {

				Post(new FinLinearPhase(positionNumberBeforeInterruption), mv.getDurationToReach());
			}

			else {
				Post(new CircularPhase(positionNumberBeforeInterruption), mv.getDurationToReach());
			}

		}

	}

	public class Arret extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process Arret", "Fin de la phase");
			LogicalDateTime d = getCurrentLogicalDate();
			staticMover = new StaticMover(mv.getPosition(d), mv.getVitesse(d));
			Logger.Information(Owner(), "Process Arret", "Mode arrêt : %s", mv.getPosition(d));
			mv = staticMover;
		}

	}

	public class SendObjectFound extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub

		}
	}

	public class RecieveObjectFound extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub
			missionCompleted = true;
		}
	}

}
