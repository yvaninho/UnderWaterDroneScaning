package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Messages.Constantes;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Messages.Messages;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefact;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.IMover;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavire;
import javafx.geometry.Point3D;

@ToRecord(name = "MouvementSequenceur")
public class EntityMouvementSequenceurDrone extends EntityMouvementSequenceur implements IMover {

	// Variable Pour savoir si la mission est terminee
	protected boolean missionCompleted = false;
	protected HashMap<String, Point3D> pointsClefs;
	// Variable pour savoir si le sous-marin est entrain de se diriger vers un
	// Artefact qu'il a prealablement detecte
	protected boolean isTrackingArtefact = false;
	// Objet representant l'artefact qu'on poursuit
	protected EntityArtefact target;
	// variable pour memoiriser la position courante et le type phase afin de
	// continuer apres avoir atteint une cible
	protected Point3D positionBeforeInterruption;
	protected int positionNumberBeforeInterruption;
	/*
	 * Definition des différentes vitesses
	 * 
	 */
	// utiliser pour accelerer la vitesse de simulation
	private int facteurDacceleration = 1;
	// vitesse en surface 4m/s

	private int vitesseEnsurface = 4 * facteurDacceleration;
	// vitesse de plongee du drone est de 1m/s
	private int vitessePlongee = 1 * facteurDacceleration;
	// vitesse de remontee en surface Ã  une vitesse de 2m/s
	private int vitesseMontee = 2 * facteurDacceleration;
	// vitesse du drone sous l'eau
	private int vitesseSousleau = 3 * facteurDacceleration;
	/** Définiton du niveau d'energie ***/
	private int niveauDenergie = 27000;

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
		// Logger.Detail(this, "Taille ", "Taille "+this.pointsClefs.size());
		Logger.Detail(this, "AfterActivate", "positions " + ini.getPositionsCles());
		Post(new LinearPhase(1));
		Post(new Consumption());
		// on lance le premier scan
		Post(new ScanOcean(), LogicalDuration.ofSeconds(2));
	}

	@Override
	public void setPointsClefs(HashMap<String, Point3D> pointsClefs, int nbPoints) {

		int nbOscillations = nbPoints / 4;
		GeneratePoints generatePoints = new GeneratePoints(pointsClefs.get("A"), pointsClefs.get("B"), nbOscillations);
		pointsClefs.putAll(generatePoints.getPoints());

	}

	public class Consumption extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub

			// si le drone se déplace
			if (!(mv instanceof StaticMover)) {
				niveauDenergie -= 4;
			}

			// si le niveau d'energie est inférieur à un seuil alors on remonte vers le
			// bateau

			if (niveauDenergie < 4) {

				Post(new Montee());

			} else if (!missionCompleted) {
				Post(this, LogicalDuration.ofSeconds(1));
			}

		}

	}

	public class LinearPhase extends SimEvent {

		private int numPointCle;

		public LinearPhase(int numPointCle) {
			this.numPointCle = numPointCle;

		}

		@Override
		public void Process() {

			if (!isTrackingArtefact && this.numPointCle < ini.getPositionsCles().size() - 4) {

				LogicalDateTime d = getCurrentLogicalDate();
				// On sauvegarde le numero du Point
				positionNumberBeforeInterruption = this.numPointCle;

				Logger.Information(Owner(), "Process Linear", String.format(Messages.StartLinearPhase,
						mv.getPosition(d), ini.getPositionsCles().get("PointCible" + numPointCle)));

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

			LogicalDateTime d = getCurrentLogicalDate();

			if (isTrackingArtefact) {

			} else {

				Logger.Information(Owner(), "Process Circular", String.format(Messages.StartCirculaire,
						mv.getPosition(d), ini.getPositionsCles().get("PointCible" + numPointClef)));

				// on sauvegarde les parametres
				positionNumberBeforeInterruption = numPointClef;
				circulrMover = new CircularMover(d, mv.getPosition(d),
						mv.getVitesse(d).normalize().multiply(ini.getMaxLinearSpeed()),
						ini.getPositionsCles().get("PointCible" + this.numPointClef));
				mv = circulrMover;

				Post(new LinearPhase(this.numPointClef + 1), mv.getDurationToReach());

			}
		}
	}

	public class ScanOcean extends SimEvent {

		@Override
		public void Process() {

			Logger.Information(Owner(), "Process Scan ocean", String.format(Messages.ScanOcean, getParent().getName()));
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
				Post(new ScanOcean(), LogicalDuration.ofMinutes(1));
			}

		}

		private boolean isNear(ISimObject o) {

			int portee = 5000;
			if (o instanceof EntityArtefact) {

				LogicalDateTime time = getCurrentLogicalDate();
				EntityArtefact object = (EntityArtefact) o;
				// ajouter une condition pour ne plus selectionner les artefact deja
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

			LogicalDateTime d = getCurrentLogicalDate();
			Logger.Information(Owner(), "Process Plongee",
					String.format(Messages.Plongee,getParent().getName(), getPosition(d)));
			Point3D plongee = getPosition(d).add(0, 0, ini.getPositionsCles().get("plongee").getZ());
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), plongee, vitessePlongee);
			mv = rectilinearMover;
			Post(new TrackTarget(), mv.getDurationToReach());
		}
	}

	public class TrackTarget extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub
			isTrackingArtefact = true;
			LogicalDateTime d = getCurrentLogicalDate();
			Logger.Information(Owner(), "Process trackTarget", String.format(Messages.TrackTarget,getParent().getName(), getPosition(d)));
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
			LogicalDateTime d = getCurrentLogicalDate();
			Logger.Information(Owner(), "Process TurnAroundtarget ",
					String.format(Messages.TurnAround, getParent().getName(), getPosition(d)));
			double rayon = 2;
			Point3D monPoint = new Point3D(rayon * Math.cos((theta * Math.PI) / (180)),
					rayon * Math.sin((theta * Math.PI) / (180)), 0);
			Point3D currentPosition = target.getPosition().add(monPoint);
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), currentPosition, vitesseSousleau);
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
			LogicalDateTime d = getCurrentLogicalDate();
			Logger.Information(Owner(), "Process ClassifyTarget ",
					String.format(Messages.ClassifyTarget, getParent().getName(), getPosition(d)));

			if (target != null) {

				if (target.getName().equals("Objet0")) {
					Logger.Information(Owner(), "Classify Target ",
							String.format(Messages.ObjectFound, getParent().getName(), getPosition(d)));

					// on met la durée dans un fichier afin de faire la moyenne sans avoir à faire
					// un filtre sur les log
					File ff = new File("analyse/durees.csv"); // définir l'arborescence
					try {
						ff.createNewFile();
						FileWriter ffw = new FileWriter(ff, true);
						double duree = LogicalDuration.soustract(d, Constantes.tempsDepart).getTotalOfMinutes();
					
						ffw.append(duree + ";");

						ffw.close(); // fermer le fichier à la fin des traitements

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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
			LogicalDateTime d = getCurrentLogicalDate();
			Logger.Information(Owner(), "Process Comeback  ", String.format(Messages.ComeBack, getPosition(d)));
			if (!missionCompleted) {

				rectilinearMover = new RectilinearMover(d, mv.getPosition(d), positionBeforeInterruption,
						vitesseEnsurface);
				mv = rectilinearMover;

				Logger.Information(Owner(), "Come back ", String.format(Messages.ComeBack, getPosition(d)));
				// on termine le mouvement en cours d'execution
				if (positionNumberBeforeInterruption % 2 == 0) {

					Post(new LinearPhase(positionNumberBeforeInterruption), mv.getDurationToReach());
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
			LogicalDateTime d = getCurrentLogicalDate();
			Logger.Information(Owner(), "Process Montee", String.format(Messages.Montee, getPosition(d)));
			// On calcule les coordonnées du point de Montée
			if (mv.getPosition(d).getZ() < 0) {
				Point3D pointSurface = new Point3D(mv.getPosition(d).getX(), mv.getPosition(d).getY(), 0);
				rectilinearMover = new RectilinearMover(d, mv.getPosition(d), pointSurface, vitesseMontee);
				mv = rectilinearMover;
				Post(new ComeBack(), mv.getDurationToReach());
			} else {
				Post(new ComeBack());
			}
		}
	}

	// ordre envoye par le bateau
	public class MissionCompleted extends SimEvent {

		@Override
		public void Process() {
			// TODO Auto-generated method stub
			// retourner au bateau
			LogicalDateTime d = getCurrentLogicalDate();
			Logger.Information(Owner(), "Process MissionComplete",
					String.format(Messages.MissionCompleted, getPosition(d)));
			// On calcule les coordonnees du point de plongee
			missionCompleted = true;
			Post(new Montee(), LogicalDuration.ofSeconds(1));
		}
	}

	public class Arret extends SimEvent {
		@Override
		public void Process() {
			LogicalDateTime d = getCurrentLogicalDate();
			staticMover = new StaticMover(mv.getPosition(d), mv.getVitesse(d));
			Logger.Information(Owner(), "Process Arret", String.format(Messages.Arret, mv.getPosition(d)));
			mv = staticMover;
		}

	}

}