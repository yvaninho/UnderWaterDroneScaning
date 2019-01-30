package enstabretagne.travaux_diriges.TD_corrige.BasicMovement;

import java.util.HashMap;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.monitor.implementation.MovableState;
import enstabretagne.simulation.components.ScenarioId;
import enstabretagne.simulation.core.IScenario;
import enstabretagne.simulation.core.IScenarioInstance;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Scenarios.BasicMvtScenario;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Scenarios.BasicMvtScenarioFeatures;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.BoueeFeatures;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.BoueeInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDroneFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDroneInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavireFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavireInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.EntityOceanFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.EntityOceanInit;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class ScenarioInstanceBasicMovement4 implements IScenarioInstance {

	@Override
	public IScenario getScenarioInstance() {
		BasicMvtScenarioFeatures bsf = new BasicMvtScenarioFeatures("BSF");
		int zPlongee = -10;
		// Cr�ation du drone et des points de passage
		HashMap<String, Point3D> positionsCles = new HashMap<String, Point3D>();
		positionsCles.put("start", new Point3D(0, 0, 0));
		positionsCles.put("plongee", new Point3D(0, 0, zPlongee));


	
		Point3D A = new Point3D(0, 0, zPlongee);
		Point3D B = new Point3D(100, 100, zPlongee);

		positionsCles.put("A",A);
		positionsCles.put("B",B);		
		int amplitude = (int) (B.getY() - A.getY());
		int nbPoints = 12;
		int L = (int) (B.getX() - A.getX());
		String pointName = "";
		int x;
		int y;
		int k = 1;

		MovableState mst;
		EntityMouvementSequenceurInit msi;
		EntityMouvementSequenceurFeature feat;

		// Drone
		mst = new MovableState(new Point3D(0, 0, 0), new Point3D(0, 0, 0), Point3D.ZERO, new Point3D(-100, 0, 0.0),
				new Point3D(0, 0, 0.0), Point3D.ZERO);
		msi = new EntityMouvementSequenceurInit("MSI", mst, 10, 100, 2, 8, positionsCles, nbPoints);
		feat = new EntityMouvementSequenceurFeature("MSF");
		bsf.getDrones().put(new EntityDroneFeature("Drone", 5, 3, Color.BLACK, feat),
				new EntityDroneInit("Drone Observation 1", msi, positionsCles, nbPoints));

		/*
		 * // Cr�ation du drone et des points de passage HashMap<String, Point3D>
		 * positionsCles = new HashMap<String, Point3D>(); positionsCles.put("start",
		 * new Point3D(0, 0, 0));
		 * 
		 * 
		 * positionsCles.put("PointCible0", new Point3D(0, 50, 0));
		 * positionsCles.put("PointCible1", new Point3D(10, 50, 0));
		 * positionsCles.put("PointCible2", new Point3D(10, 0, 0));
		 * positionsCles.put("PointCible3", new Point3D(20, 0, 0));
		 * positionsCles.put("PointCible4", new Point3D(20, 50, 0));
		 * 
		 * Point3D A = new Point3D(0, 0, 0); Point3D B = new Point3D(50,50, 0);
		 * 
		 * int amplitude = (int) (B.getY() - A.getY()); int nbPoints = 4; int L = (int)
		 * (B.getX() - A.getX()); String pointName = ""; int x; int y; int k = 1 ; for
		 * (int i = 0; i <= nbPoints; i = i + 2) {
		 * 
		 * pointName = "PointCible" + i; x = ((L*i) / nbPoints) ; y = (amplitude*(k%2));
		 * positionsCles.put(pointName, new Point3D(x, y, 0)); x = (L *(i+1)) / nbPoints
		 * ; y = (amplitude*(k%2)) ; pointName = "PointCible" + (i+1);
		 * positionsCles.put(pointName, new Point3D(x, y, 0));
		 * 
		 * k++ ; }
		 * 
		 * /*
		 * 
		 * // Drone mst = new MovableState(new Point3D(0, 0, 0), new Point3D(0, 0, 0),
		 * Point3D.ZERO, new Point3D(-100, 0, 0.0), new Point3D(0, 0, 0.0),
		 * Point3D.ZERO); msi = new EntityMouvementSequenceurInit("MSI", mst, 10, 100,
		 * 2, 8, positionsCles); feat = new EntityMouvementSequenceurFeature("MSF");
		 * bsf.getDrones().put(new EntityDroneFeature("Drone", 5, 3, Color.BLACK, feat),
		 * new EntityDroneInit("Drone Observation 1", msi, positionsCles));
		 * 
		 */

		// Cr�ation de l'oc�an
		positionsCles = new HashMap<String, Point3D>();
		MovableState mstOcean = new MovableState(Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO,
				Point3D.ZERO);
		EntityMouvementSequenceurInit msiOcean = new EntityMouvementSequenceurInit("MSIOCEAN", mstOcean, 0, 0, 0, 0,
				null,0 );
		bsf.getOcean().put(new EntityOceanFeature("O1"), new EntityOceanInit("Atlantique", msiOcean));

		LogicalDateTime start = new LogicalDateTime("05/12/2017 06:00");
		LogicalDateTime end = start.add(LogicalDuration.ofMinutes(2));
		BasicMvtScenario bms = new BasicMvtScenario(new ScenarioId("S4"), bsf, start, end);

		return bms;
	}

}
