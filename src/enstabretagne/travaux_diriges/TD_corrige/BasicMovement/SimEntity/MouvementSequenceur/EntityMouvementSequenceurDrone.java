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
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavire;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavire.ReceiveInfosArtefact;
import javafx.geometry.Point3D;

@ToRecord(name = "MouvementSequenceur")
public class EntityMouvementSequenceurDrone extends EntityMouvementSequenceur implements IMover {

	// Variable Pour savoir si la mission est terminee
	protected boolean missionCompleted = false;
	protected HashMap<String, Point3D> pointsClefs;
	// variable pour savoir si le sous-marin est entrain de se diriger vers un
	// artefact qu'il a prealablement detecte
	protected boolean isTrackingArtefact = false;
	// Objet representant l'artefact qu'on poursuit
	protected EntityArtefact target;
	// variable pour memoiriser la position courante et le type phase afin de
	// continuer apres avoir atteint une cible
	protected Point3D positionBeforeInterruption;
	protected int positionNumberBeforeInterruption;

	/*
	 * Definition des differentes vitesses
	 * 
	 */
	// vitesse en surface 4m/s

	private int vitesseEnsurface = 4 * 4;
	// vitesse de plongee du drone est de 1m/s
	private int vitesseDescente = 1 * 4;
	// vitesse de remontee en surface Ã  une vitesse de 2m/s
	private int vitesseMontee = 2 * 4;
	// vitesse du drone sous l'eau
	private int vitesseSousleau = 3 * 4;

	/** Définiton du niveau d'energie ***/
	private int niveauDenergie = 22000;

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
		// Post(new ScanOcean(), LogicalDuration.ofSeconds(2));
	}

	@Override
	public void setPointsClefs(HashMap<String, Point3D> pointsClefs, int nbPoints) {

		GeneratePoints generatePoints = new GeneratePoints(pointsClefs);

		this.pointsClefs = generatePoints.getPoints(nbPoints);

	}

	public class Consumption extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub

			// si le drone se déplace
			if (!(mv instanceof StaticMover)) {
				niveauDenergie--;
			}

			// si le niveau d'energie est inférieur à un seuil alors on remonte vers le
			// bateau

			if (niveauDenergie < 4) {

			}

			Post(this, LogicalDuration.ofSeconds(1));

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

				// On sauvegarde le numero du Point
				positionNumberBeforeInterruption = this.numPointCle;
				Logger.Information(Owner(), "Process FinLinear",
						"Start   Lineaire phase  N °" + this.numPointCle + " by " + getParent().getName());
				LogicalDateTime d = getCurrentLogicalDate();
				rectilinearMover = new RectilinearMover(d, mv.getPosition(d),
						ini.getPositionsCles().get("PointCible" + numPointCle), ini.getMaxLinearSpeed());
				mv = rectilinearMover;
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

				Logger.Information(Owner(), "Process Circulaire ",
						"Start Circulaire Phase  N° " + " by " + getParent().getName());

				LogicalDateTime d = getCurrentLogicalDate();
				// on sauvegarde les parametres
				positionNumberBeforeInterruption = numPointClef;
				circulrMover = new CircularMover(d, mv.getPosition(d),
						mv.getVitesse(d).normalize().multiply(ini.getMaxLinearSpeed()),
						ini.getPositionsCles().get("PointCible" + this.numPointClef));
				mv = circulrMover;
				Logger.Information(Owner(), "Process Circular",
						"Phase mouvement circulaire enclenchee " + this.numPointClef);

				Post(new FinLinearPhase(this.numPointClef + 1), mv.getDurationToReach());

			}
		}
	}

	public class ScanOcean extends SimEvent {

		@Override
		public void Process() {

			Logger.Information(Owner(), "Process Scan ocean", "start scan ocean  ");
			LogicalDateTime d = getCurrentLogicalDate();
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

				}

				target.setTracked(true);
				objectsNear.remove(target);
				Post(new Plongee(), LogicalDuration.ofSeconds(1));

			}

			if (!missionCompleted) {
				Post(new ScanOcean(), LogicalDuration.ofSeconds(1));
			}

		}

		private boolean isNear(ISimObject o) {

			int portee = 5000;
			if (o instanceof EntityArtefact) {

				LogicalDateTime time = getCurrentLogicalDate();
				EntityArtefact object = (EntityArtefact) o;
				// ajouter une condition pour ne plus selectionner les artefact dejÃ 
				// selectionne
				return object.getPosition().subtract(mv.getPosition(time)).magnitude() < portee && !object.isDetected()
						&& !object.isTracked();

			}
			return false;
		}

	}

	public class Plongee extends SimEvent {

		@Override
		public void Process() {

			Logger.Information(Owner(), "Process Plongee", " Start Plongee  ");
			LogicalDateTime d = getCurrentLogicalDate();

			Point3D plongee = getPosition(d).add(0, 0, ini.getPositionsCles().get("plongee").getZ());

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
			Logger.Information(Owner(), "Process trackTarget", "Start Track Target");
			LogicalDateTime d = getCurrentLogicalDate();
			double rayon = 2;
			Point3D monPoint = new Point3D(1, 0, 0).multiply(rayon);
			// On sauvegarde la position
			positionBeforeInterruption = mv.getPosition(d);
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), target.getPosition().add(monPoint),
					vitesseSousleau);
			mv = rectilinearMover;
			Post(new TurnAround(10), mv.getDurationToReach());

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
			Logger.Information(Owner(), "Process TurnAroundtarget ", "Start Turn Around Target");
			LogicalDateTime d = getCurrentLogicalDate();
			double rayon = 2;
			Point3D monPoint = new Point3D(rayon * Math.cos((theta * Math.PI) / (180)),
					rayon * Math.sin((theta * Math.PI) / (180)), 0);
			Point3D currentPosition = target.getPosition().add(monPoint);
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), currentPosition, 10);
			mv = rectilinearMover;
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

			Logger.Information(Owner(), "Process ClassifyTarget ",
					"Phase Classify Target " + target.getName() + " " + getParent().getName());
			LogicalDateTime d = getCurrentLogicalDate();
			if (target != null) {

				if (target.getName().equals("Objet0")) {
					Logger.Information(Owner(), "Classify Target ", " Object found " + getPosition(d));

					missionCompleted = true;

				}

				target.setDetected(true);
				List<ISimObject> objectsNavire = getEngine().requestSimObject(this::isNavire);
				EntityNavire navire = (EntityNavire) objectsNavire.get(0);
				navire.Post(navire.new ReceiveInfosArtefact(target), LogicalDuration.ofSeconds(1));
				target = null;
				isTrackingArtefact = false;
				Post(new Montee(), d.add(LogicalDuration.ofSeconds(1)));
			}

		}

		private boolean isNavire(ISimObject o) {

			if (o instanceof EntityNavire) {

				return true;
			}

			return false;

		}
	}

	public class ComeBack extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub
			Logger.Information(Owner(), "Process Comeback  ", "Start ComeBack");
			LogicalDateTime d = getCurrentLogicalDate();

			if (!missionCompleted) {

				rectilinearMover = new RectilinearMover(d, mv.getPosition(d), positionBeforeInterruption,
						vitesseEnsurface);
				mv = rectilinearMover;

				Logger.Information(Owner(), "Come back ",
						"Phase come back  " + mv.getDurationToReach() + positionBeforeInterruption);
				// on termine le mouvement en cours d'execution
				if (positionNumberBeforeInterruption % 2 == 0) {

					Post(new FinLinearPhase(positionNumberBeforeInterruption), mv.getDurationToReach());
				}

				else {
					Post(new CircularPhase(positionNumberBeforeInterruption), mv.getDurationToReach());
				}

			}

			// au cas ou la mission est terminee
			else {
				rectilinearMover = new RectilinearMover(d, mv.getPosition(d), ini.getPositionsCles().get("A"),
						vitesseEnsurface);
				mv = rectilinearMover;
				Post(new Arret(), mv.getDurationToReach());

			}
		}

	}

	public class Montee extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub
			// retourner au bateau

			Logger.Information(Owner(), "Process Montee", "Start Montee");
			LogicalDateTime d = getCurrentLogicalDate();

			// On calcule les coordonnées du point de Montée
			Point3D pointSurface = new Point3D(mv.getPosition(d).getX(), mv.getPosition(d).getY(), 0);

			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), pointSurface, vitesseMontee);
			mv = rectilinearMover;
			Post(new ComeBack(), mv.getDurationToReach());
		}
	}

	// ordre envoye par le bateau
	public class MissionCompleted extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub

			// retourner au bateau
			Logger.Information(Owner(), "Process MissionComplete", "MissionComplete");
			LogicalDateTime d = getCurrentLogicalDate();
			// On calcule les coordonnees du point de plongee
			missionCompleted = true;
			Post(new Montee(), LogicalDuration.ofSeconds(1));
		}
	}

	public class Arret extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process Arret", "Start phase");
			LogicalDateTime d = getCurrentLogicalDate();
			staticMover = new StaticMover(mv.getPosition(d), mv.getVitesse(d));
			Logger.Information(Owner(), "Process Arret", "Mode arret : %s", mv.getPosition(d));
			mv = staticMover;
		}

	}

}
