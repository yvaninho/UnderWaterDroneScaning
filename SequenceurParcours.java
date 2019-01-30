package utils;

import java.awt.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import enstabretagne.BE.AnalyseSousMarine.SimEntity.Artefact.EntityArtefact;
import enstabretagne.BE.AnalyseSousMarine.SimEntity.Artefact.IMover;
import enstabretagne.BE.AnalyseSousMarine.SimEntity.MouvementSequenceur.CircularMover;
import enstabretagne.BE.AnalyseSousMarine.SimEntity.MouvementSequenceur.EntityMouvementSequenceur;
import enstabretagne.BE.AnalyseSousMarine.SimEntity.MouvementSequenceur.RectilinearMover;
import enstabretagne.BE.AnalyseSousMarine.SimEntity.MouvementSequenceur.SelfRotator;
import enstabretagne.BE.AnalyseSousMarine.SimEntity.MouvementSequenceur.StaticMover;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.monitor.implementation.XYZRotator2;
import enstabretagne.simulation.components.IEntity;
import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.simulation.components.data.SimInitParameters;
import enstabretagne.simulation.core.ISimObject;
import enstabretagne.simulation.core.implementation.SimEvent;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

public final class SequenceurParcours extends EntityMouvementSequenceur implements IMover {

	public ArrayList<Entry<String, Point3D>> list;
	
	public ArrayList<Entry<String, Point3D>> removeFirst( ArrayList<Entry<String, Point3D>> l) {
		ArrayList<Entry<String, Point3D>> l2 = new ArrayList<Entry<String, Point3D>>();
		java.util.List<Entry<String, Point3D>> en = l.subList(1, l.size());
		for (Entry<String, Point3D> e : en)
			l2.add(e);
		l=l2;
		return l;
	}
	
	public SequenceurParcours(String name, SimFeatures features) {
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
		ini.getPositionsClesP().remove("start");
		Map<String,Point3D> map = ini.getPositionsClesP();
		list = new ArrayList<Map.Entry< String, Point3D>>(map.entrySet());
		Logger.Information("default", "Process fin Mouvement rectiligne", "rect to point  %s ",list.get(0).getValue());
		
		Post(new rect(list.get(0).getValue()));
		list = removeFirst(list);
		Logger.Information("default", "Process fin Mouvement rectiligne", "liste point  %s ",list);
		
		
		
		//Post(new Rotation(mv.getPosition(getCurrentLogicalDate()),list.get(0).getValue()),
			//	LogicalDuration.ofSeconds(1));
		//Logger.Information("default", "Process Arret", "Date a init: %s",getCurrentLogicalDate());
		/*
		Post(new Rotation(mv.getPosition(getCurrentLogicalDate()),
				ini.getPositionsClesP().get(ini.getPositionsClesP().keySet().iterator().next())),
				LogicalDuration.ofSeconds(1));*/
	}

	public class Rotation extends SimEvent {
		private Point3D pt;
		private Point3D ptto;

		public Rotation(Point3D point, Point3D pointTo) {
			this.pt = point;
			this.ptto = pointTo;
		}

		public Point3D getPt() {
			return pt;
		}

		public Point3D getptTo() {
			return ptto;
		}

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process Rotation", "Debut Rotation");
			LogicalDateTime d = getCurrentLogicalDate();
			//Logger.Information(Owner(), "Process Arret", "Date avant rotation: %s",getCurrentLogicalDate());
			

			Point3D dir = XYZRotator2.getTransformByAngle(mv.getRotationXYZ(d)).transform(Rotate.X_AXIS);
			//Point3D dir2 = XYZRotator2.getTransformByAngle(getptTo()).transform(Rotate.X_AXIS);
			Logger.Information(Owner(), "Process Rotation une", "direction position sur place %s", dir);
			Logger.Information(Owner(), "Process Rotation une", "direction  vers  le point %s",getptTo());
			selfRotator = new SelfRotator(d, mv.getPosition(d), dir, getptTo(), ini.getMaxSelfRotationSpeed());
			mv = selfRotator;
			//Logger.Information(Owner(), "Process Rotation ", "point de fin %s", ini.getPositionsClesP().get("start"));
			//Logger.Information(Owner(), "Process Rotation ", "point de fin %s", ini.getPositionsClesP().get("endPt"));
			
			/*
			Map<String,Point3D> map = ini.getPositionsClesP();
			// add some entries
			ArrayList<Entry<String, Point3D>> entryList =
			    new ArrayList<Map.Entry< String, Point3D>>(map.entrySet());
			Entry<String, Point3D> lastEntry =
			    entryList.get(entryList.size()-1);
			
			Logger.Information(Owner(), "Process Rotation une", "point to %s", lastEntry);
			*/
			//Post(new Arret(),getDurationToReach());
			//Post(new Parcours(), mv.getDurationToReach());
			//Logger.Information(Owner(), "Process Arret", "Date apres rotation: %s",getCurrentLogicalDate());
			//
			//Post(new rect(), mv.getDurationToReach());
			ini.getPositionsClesP().remove("start");

		}
	}

	public class rect extends SimEvent {
		private Point3D to;
		private int position;

		public rect(Point3D pt) {
			this.to = pt;
			//this.position = i;
		}

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process Arret", "Date avant l'instant rectiligne: %s",getCurrentLogicalDate());
			LogicalDateTime d = getCurrentLogicalDate();
			Logger.Information(Owner(), "Process Mouvement rectiligne", "Début phase rectiligne");
			
			rectilinearMover = new RectilinearMover(d, mv.getPosition(d) ,this.to, ini.getMaxLinearSpeed());
			mv = rectilinearMover;
			
			
			if (list.get(0).getValue() == list.get(list.size()-1).getValue()) {
				Post(new Arret(),mv.getDurationToReach());
			}
			else {
				Logger.Information(Owner(), "Process fin Mouvement rectiligne", "fin phase retiligne");
				
				Logger.Information(Owner(), "Process fin Mouvement rectiligne", "Circular to point  %s ",list);
				Post(new circular(list.get(0).getValue()),mv.getDurationToReach());
				list = removeFirst(list);
				
			}

			
			Logger.Information(Owner(), "Process Arret", "Datetoreach apres rectiligne: %s",getDurationToReach());
			//Logger.Information(Owner(), "Process Arret", "Date apres rectiligne: %s",getCurrentLogicalDate());
			
		}

	}

	public class circular extends SimEvent {
		private Point3D to;
		private int position;

		public circular(Point3D pt) {
			this.to = pt;
			//this.position = i;
		}

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process Mouvement Circulaire", "Début phase circulaire");
			Logger.Information(Owner(), "Process Arret", "Date a l'instant circulaire: %s",getCurrentLogicalDate());
			LogicalDateTime d = getCurrentLogicalDate();
			circulrMover = new CircularMover(d, mv.getPosition(d),
			mv.getVitesse(d).normalize().multiply(ini.getMaxLinearSpeed()), this.to);
			mv = circulrMover;
			Logger.Information(Owner(), "Process fin Mouvement Circulaire", "fin phase circulaire");
			Post(new rect(list.get(0).getValue()),mv.getDurationToReach());
			Logger.Information(Owner(), "Process Arret", "Date apres circulaire: %s",getCurrentLogicalDate());
			if (list.size() !=1)
				list = removeFirst(list);
		}

	}

	public class Parcours extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process Parcours suivi", "debut parcours");
			//new rect(ini.getPositionsClesP().get("rect1")).Process();
			//new circular(ini.getPositionsClesP().get("cir1")).Process();
			//Post(new rect(ini.getPositionsClesP().get("rect1")), mv.getDurationToReach());
			/*
			for (String key : ini.getPositionsClesP().keySet()) {
				if (key.substring(0, 4).equals("rect"))
					new rect(ini.getPositionsClesP().get(key)).Process();
				else if (key.substring(0, 3).equals("cir"))
					new circular(ini.getPositionsClesP().get(key)).Process();
			}
			*/

			Logger.Information(Owner(), "Process Fin Parcours suivi", "Fin de parcours");
			Post(new Arret(), mv.getDurationToReach());
		}
	}
	
	
	public class Scan extends SimEvent{
		
		private boolean detectBlackArtefact(ISimObject o){
			if(o==this) return false;
			
			if(o instanceof EntityArtefact)
			{
				EntityArtefact std = (EntityArtefact) o;
				
				boolean found=true;
				LogicalDateTime d=getCurrentLogicalDate();
				double distance = mv.getPosition(d).subtract(std.getPosition()).magnitude();
				if (distance <= 5000) {
					return found;
				}
				
			}
			return false;
		}

		@Override
		public void Process() {
			// TODO Auto-generated method stub
			java.util.List<ISimObject> object = getEngine().requestSimObject(this::detectBlackArtefact);
			
			if (object.size() > 0) {
				
			}
			
			LogicalDateTime d=getCurrentLogicalDate();
			Post(this,d.add(LogicalDuration.ofMinutes(1)));
			
		}
		
		
	}

	public class Arret extends SimEvent {

		@Override
		public void Process() {
			Logger.Information(Owner(), "Process Arret", "Debut de la phase Arret");
			LogicalDateTime d = getCurrentLogicalDate();
			Point3D dir = XYZRotator2.getTransformByAngle(mv.getRotationXYZ(d)).transform(Rotate.X_AXIS);
			staticMover = new StaticMover(mv.getPosition(d), dir);
			Logger.Information(Owner(), "Process Arret", "Mode arrêt : %s",mv.getPosition(d));
			Logger.Information(Owner(), "Process Arret", "Mode arrêt : %s",dir);
			mv = staticMover;
		}

	}
}