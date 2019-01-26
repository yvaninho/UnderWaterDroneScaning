package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur;

import java.util.HashMap;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.logger.ToRecord;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.monitor.implementation.XYZRotator;
import enstabretagne.simulation.components.IEntity;
import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.simulation.components.data.SimInitParameters;
import enstabretagne.simulation.core.implementation.SimEvent;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.IMover;
import javafx.geometry.Point3D;

@ToRecord(name = "MouvementSequenceur")
public class EntityMouvementSequenceur_ExempleSauvegarde extends EntityMouvementSequenceur implements IMover {

	
	protected HashMap<String, Point3D> pointsClefs ;
	public EntityMouvementSequenceur_ExempleSauvegarde(String name, SimFeatures features ) {
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
		// attente
		Post(new FinStaticPhase1(), LogicalDuration.ofSeconds(1));
	}
	
	@Override
	public void  setPointsClefs(HashMap<String, Point3D> pointsClefs) {
		
		this.pointsClefs = pointsClefs ;
		
	}

	public class FinStaticPhase1 extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process FinStaticPhase1",
					"Fin de la première phase statique " + getParent().getName());
			LogicalDateTime d = getCurrentLogicalDate();
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), ini.getPositionsCles().get("PointCible0"),
					ini.getMaxLinearSpeed());
			mv = rectilinearMover;
			Logger.Information(Owner(), "Process FinStaticPhase1", "Phase mouvement linéaire enclenché");
			Post(new BugCorrection(), mv.getDurationToReach());
		}
	}
	

	public class BugCorrection extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process FinCircularPhase3", "Fin de la troisème phase");
			LogicalDateTime d = getCurrentLogicalDate();
			Point3D dir = new Point3D(1, 1, 0);
			selfRotator = new SelfRotator(d, mv.getPosition(d), mv.getVitesse(d), mv.getPosition(d).add(dir),
					ini.getMaxSelfRotationSpeed());
			mv = selfRotator;
			Logger.Information(Owner(), "Process FinStaticPhase1", "Phase mouvement circulaire enclenché");

			Post(new FinLinearPhase2(), mv.getDurationToReach());

		}
	}

	public class FinLinearPhase extends SimEvent {
	
	private int numPointCle ;
	public FinLinearPhase(int numPointCle) {
		this.numPointCle = numPointCle ;
		
	}
	
		@Override
		public void Process() {

			Logger.Information(Owner(), "Process FinLinearPhase2", "Fin de la deuxième phase");
			LogicalDateTime d = getCurrentLogicalDate();
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), ini.getPositionsCles().get("PointCible"+numPointCle),
					ini.getMaxLinearSpeed());
			mv = rectilinearMover;
			Logger.Information(Owner(), "Process FinLinearPhase", "Phase mouvement circulaire enclenché");
			Post(new FinSelfRotatePhase4(), mv.getDurationToReach());
		}
	}
	
	
	
	public class CircularPhase extends SimEvent {
		
	private int numPointClef ;	
	public CircularPhase (int numPointClef) {
		
		this.numPointClef = numPointClef ;
	}
		

		@Override
		public void Process() {

			Logger.Information(Owner(), "Process FinLinearPhase2", "Fin de la deuxième phase");
			LogicalDateTime d = getCurrentLogicalDate();
			circulrMover = new CircularMover(d, mv.getPosition(d),
					mv.getVitesse(d).normalize().multiply(ini.getMaxLinearSpeed()),
					ini.getPositionsCles().get("PointCible"+this.numPointClef));
			mv = circulrMover;
			Logger.Information(Owner(), "Process Circular"+this.numPointClef, "Phase mouvement circulaire enclenché");
			// Post(new Arret(), mv.getDurationToReach());
			Post(new FinCircularPhase3(), mv.getDurationToReach());
		}
	}

	public class FinLinearPhase2 extends SimEvent {

		@Override
		public void Process() {

			Logger.Information(Owner(), "Process FinLinearPhase2", "Fin de la deuxième phase");
			LogicalDateTime d = getCurrentLogicalDate();
			circulrMover = new CircularMover(d, mv.getPosition(d),
					mv.getVitesse(d).normalize().multiply(ini.getMaxLinearSpeed()),
					ini.getPositionsCles().get("PointCible1"));
			mv = circulrMover;
			Logger.Information(Owner(), "Process FinStaticPhase1", "Phase mouvement circulaire enclenché");
			// Post(new Arret(), mv.getDurationToReach());
			Post(new FinCircularPhase3(), mv.getDurationToReach());
		}
	}

	public class FinCircularPhase3 extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process FinCircularPhase3", "Fin de la troisème phase");
			LogicalDateTime d = getCurrentLogicalDate();
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), ini.getPositionsCles().get("PointCible2"),
					ini.getMaxLinearSpeed());
			mv = rectilinearMover;
			Logger.Information(Owner(), "Process FinStaticPhase1", "Phase mouvement circulaire enclenché");
			Post(new FinSelfRotatePhase4(), mv.getDurationToReach());

		}
	}

	public class FinSelfRotatePhase4 extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process FinStaticPhase1", "Fin de la première phase statique");
			LogicalDateTime d = getCurrentLogicalDate();

			circulrMover = new CircularMover(d, mv.getPosition(d),
					mv.getVitesse(d).normalize().multiply(ini.getMaxLinearSpeed()),
					ini.getPositionsCles().get("PointCible3"));
			mv = circulrMover;
			Logger.Information(Owner(), "Process FinStaticPhase4", "Phase mouvement linéaire enclenché");
			Post(new FinLinearPhase5(), mv.getDurationToReach());
		}
	}

	public class FinLinearPhase5 extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process FinLinearPhase5", "FinLinearPhase5");
			LogicalDateTime d = getCurrentLogicalDate();
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), ini.getPositionsCles().get("PointCible4"),
					ini.getMaxLinearSpeed());
			mv = rectilinearMover;
			Logger.Information(Owner(), "Process FinStaticPhase5", "Phase mouvement linéaire");
			Post(new FinRotatorPhase6(), mv.getDurationToReach());

		}
	}

	public class FinRotatorPhase6 extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process FinRotatorPhase6", "FinRotatorPhase6");
			LogicalDateTime d = getCurrentLogicalDate();
			circulrMover = new CircularMover(d, mv.getPosition(d),
					mv.getVitesse(d).normalize().multiply(ini.getMaxLinearSpeed()),
					ini.getPositionsCles().get("start"));
			mv = circulrMover;
			Logger.Information(Owner(), "Process FinRotatorPhase6", "FinRotatorPhase6");
			Post(new FinCircularPhase7(), mv.getDurationToReach());
		}
	}

	public class FinCircularPhase7 extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process FinCircularPhase7", "FinCircularPhase7");
			LogicalDateTime d = getCurrentLogicalDate();
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), mv.getVitesse(d),
					ini.getPositionsCles().get("PointSousEau"), ini.getMaxPlongeeSpeed());
			mv = rectilinearMover;
			Logger.Information(Owner(), "Process FinCircularPhase7", "FinCircularPhase7");
			Post(new FinPlongee(), mv.getDurationToReach());
		}
	}

	public class FinPlongee extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process FinPlongee", "FinPlongee");
			LogicalDateTime d = getCurrentLogicalDate();
			Point3D dir = XYZRotator.transformX(mv.getRotationXYZ(d));
			selfRotator = new SelfRotator(d, mv.getPosition(d), dir, ini.getPositionsCles().get("ObservationMine"),
					ini.getMaxSelfRotationSpeed());
			mv = selfRotator;
			Post(new AtteinteCible(), mv.getDurationToReach());

		}
	}

	public class AtteinteCible extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process AtteinteCible", "AtteinteCible");
			LogicalDateTime d = getCurrentLogicalDate();
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d), new Point3D(20, 20, -20), 10);
			mv = rectilinearMover;
			Logger.Information(Owner(), "Process AtteinteCible", "AtteinteCible");
			Post(new Arret(), mv.getDurationToReach());
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

}
