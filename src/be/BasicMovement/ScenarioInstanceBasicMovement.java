package be.BasicMovement;

import java.util.HashMap;

import be.Scenarios.BasicMvtScenario;
import be.Scenarios.BasicMvtScenarioFeatures;
import be.SimEntity.Bouee.BoueeFeatures;
import be.SimEntity.Bouee.BoueeInit;
import be.SimEntity.MouvementSequenceur.EntityMouvementSequenceurFeature;
import be.SimEntity.MouvementSequenceur.EntityMouvementSequenceurInit;
import be.SimEntity.Navire.EntityNavireFeature;
import be.SimEntity.Navire.EntityNavireInit;
import be.SimEntity.Ocean.EntityOceanFeature;
import be.SimEntity.Ocean.EntityOceanInit;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.monitor.implementation.MovableState;
import enstabretagne.simulation.components.ScenarioId;
import enstabretagne.simulation.core.IScenario;
import enstabretagne.simulation.core.IScenarioInstance;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class ScenarioInstanceBasicMovement implements IScenarioInstance {

	@Override
	public IScenario getScenarioInstance() {
		BasicMvtScenarioFeatures bsf = new BasicMvtScenarioFeatures("BSF");

		// Cr�ation du navire et des points de passage
		HashMap<String, Point3D> positionsCles = new HashMap<String, Point3D>();
		positionsCles.put("start", new Point3D(0, 0, 0));
		positionsCles.put("PointCible1", new Point3D(10, 10, 0));
		positionsCles.put("PointCible2", new Point3D(30, -10, 0));
		positionsCles.put("PointCible3", new Point3D(20, 0, 0));
		positionsCles.put("PointDirection", new Point3D(20, 20, 0));
		positionsCles.put("PointSousEau", new Point3D(0, 0, -10));
		positionsCles.put("ObservationMine", new Point3D(20, 20, -20));

		MovableState mst = new MovableState(new Point3D(0, 0, 0), new Point3D(1, 1, 0), Point3D.ZERO,
				new Point3D(0, 0, 45.0), new Point3D(10, 5, 0.0), Point3D.ZERO);
		EntityMouvementSequenceurInit msi = new EntityMouvementSequenceurInit("MSI", mst, 10, 100, 2, 8, positionsCles);
		EntityMouvementSequenceurFeature feat = new EntityMouvementSequenceurFeature("MSF");

		bsf.getNavires().put(new EntityNavireFeature("NavireF", 5, 3, Color.BLACK, feat),
				new EntityNavireInit("Navire Observation", msi));

		// Cr�ation de bouees
		int i = 0;
		int N = 10;
		positionsCles = new HashMap<String, Point3D>();
		for (i = 0; i < N; i++) {
			MovableState mstBouee;
			EntityMouvementSequenceurInit msiBouee;

			mstBouee = new MovableState(new Point3D(10 * i, 0, 0), Point3D.ZERO, Point3D.ZERO, Point3D.ZERO,
					Point3D.ZERO, Point3D.ZERO);
			msiBouee = new EntityMouvementSequenceurInit("MSI", mstBouee, 0, 0, 0, 0, positionsCles);
			bsf.getBouees().put(new BoueeFeatures("B1", 5, 1, 3.0), new BoueeInit("B" + i, msiBouee, Color.RED));

		}

		// Cr�ation de l'ocean
		positionsCles = new HashMap<String, Point3D>();
		MovableState mstOcean = new MovableState(Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO,
				Point3D.ZERO);
		EntityMouvementSequenceurInit msiOcean = new EntityMouvementSequenceurInit("MSIOCEAN", mstOcean, 0, 0, 0, 0,
				positionsCles);
		bsf.getOcean().put(new EntityOceanFeature("O1"), new EntityOceanInit("Atlantique", msiOcean));

		LogicalDateTime start = new LogicalDateTime("05/12/2017 06:00");
		LogicalDateTime end = start.add(LogicalDuration.ofMinutes(2));
		BasicMvtScenario bms = new BasicMvtScenario(new ScenarioId("S1"), bsf, start, end);

		return bms;
	}

}
