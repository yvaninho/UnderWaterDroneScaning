package enstabretagne.travaux_diriges.TD_corrige.BasicMovement;

import java.util.HashMap;


import enstabretagne.base.math.MoreRandom;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.monitor.implementation.MovableState;
import enstabretagne.simulation.components.ScenarioId;
import enstabretagne.simulation.core.IScenario;
import enstabretagne.simulation.core.IScenarioInstance;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Scenarios.BasicMvtScenario;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Scenarios.BasicMvtScenarioFeatures;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefactFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefactInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.BoueeFeatures;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.BoueeInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.EntityMouvementSequenceurInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavireFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavireInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.EntityOceanFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.EntityOceanInit;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class ScenarioInstanceBasicMovement3 implements IScenarioInstance {

	@Override
	public IScenario getScenarioInstance() {
		BasicMvtScenarioFeatures bsf = new BasicMvtScenarioFeatures("BSF");

		// Création du navire et des points de passage
		HashMap<String, Point3D> positionsCles = new HashMap<String, Point3D>();
		positionsCles.put("start", new Point3D(0, 0, 0));
		positionsCles.put("PointCible1", new Point3D(10, 10, 0));
		positionsCles.put("PointCible2", new Point3D(30, -10, 0));
		positionsCles.put("PointCible3", new Point3D(20, 0, 0));
		positionsCles.put("PointDirection", new Point3D(20, 20, 0));
		positionsCles.put("PointSousEau", new Point3D(0, 0, -10));
		positionsCles.put("ObservationMine", new Point3D(20, 20, -20));

		MovableState mst;
		EntityMouvementSequenceurInit msi;
		EntityMouvementSequenceurFeature feat;
		int width = 200;
		int height = 200;

		// création du bateau

		mst = new MovableState(new Point3D(0, 0, 0), new Point3D(1, 1, 0), Point3D.ZERO, new Point3D(0, 0, 45.0),
				new Point3D(10, 5, 0.0), Point3D.ZERO);
		msi = new EntityMouvementSequenceurInit("MSI", mst, 10, 100, 2, 8, positionsCles, 0);
		feat = new EntityMouvementSequenceurFeature("MSF");

		bsf.getNavires().put(new EntityNavireFeature("NavireF", 5, 3, Color.DARKSLATEGREY, feat),
				new EntityNavireInit("Navire Observation", msi));

		/*
		 * 
		 * // création des artefactes on a entre 30 et 40 artefactes suivant une lois //
		 * uniforme
		 * 
		 * MoreRandom generateur = new MoreRandom(); // On définit le germe
		 * generateur.setSeed(42); int nombreArtefacts = (int)
		 * generateur.nextUniform(30, 40);
		 * 
		 * double proportionSphereRouge = 0.6, proportionCylindreJaune = 0.3,
		 * proportionCubeVert = 0.1;
		 * 
		 * int x, y; for (int i = 0; i < (int) (nombreArtefacts * proportionCubeVert);
		 * i++) {
		 * 
		 * x = (int) generateur.nextUniform(-width/2, width/2); y = (int)
		 * generateur.nextUniform(-height/2, height/2);
		 * 
		 * mst = new MovableState(new Point3D(x, y, 0), new Point3D(1, 1, 0),
		 * Point3D.ZERO, new Point3D(0, 0, 45.0), new Point3D(10, 5, 0.0),
		 * Point3D.ZERO); msi = new EntityMouvementSequenceurInit("Artefact MSI", mst,
		 * 10, 100, 2, 8, positionsCles); feat = new
		 * EntityMouvementSequenceurFeature("Artefact MSF"); bsf.getArtefactes().put(new
		 * EntityArtefactFeature("Artefact"+i, 5, 3, Color.RED, feat), new
		 * EntityArtefactInit("Artefact Observation 1", msi));
		 * 
		 * }
		 * 
		 * for (int i = 0; i < (int) (nombreArtefacts * proportionCylindreJaune); i++) {
		 * 
		 * x = (int) generateur.nextUniform(-width/2, width/2); y = (int)
		 * generateur.nextUniform(-height/2, height/2); mst = new MovableState(new
		 * Point3D(x, y, 0), new Point3D(1, 1, 0), Point3D.ZERO, new Point3D(0, 0,
		 * 45.0), new Point3D(10, 5, 0.0), Point3D.ZERO); msi = new
		 * EntityMouvementSequenceurInit("Artefact MSI", mst, 10, 100, 2, 8,
		 * positionsCles); feat = new EntityMouvementSequenceurFeature("Artefact MSF");
		 * bsf.getArtefactes().put(new EntityArtefactFeature("Artefact"+i, 5, 3,
		 * Color.YELLOW, feat), new EntityArtefactInit("Artefact Observation "+i, msi));
		 * 
		 * }
		 * 
		 * for (int i = 0; i < (int) (nombreArtefacts * proportionCubeVert); i++) {
		 * 
		 * x = (int) generateur.nextUniform(-width/2, width/2); y = (int)
		 * generateur.nextUniform(-height/2, height/2); mst = new MovableState(new
		 * Point3D(x, y, 0), new Point3D(1, 1, 0), Point3D.ZERO, new Point3D(0, 0,
		 * 45.0), new Point3D(10, 5, 0.0), Point3D.ZERO); msi = new
		 * EntityMouvementSequenceurInit("Artefact MSI", mst, 10, 100, 2, 8,
		 * positionsCles); feat = new EntityMouvementSequenceurFeature("Artefact MSF");
		 * bsf.getArtefactes().put(new EntityArtefactFeature("Artefact", 5, 3,
		 * Color.GREEN, feat), new EntityArtefactInit("Artefact Observation "+i, msi));
		 * 
		 * }
		 * 
		 */
		// Création de bouees
		/*
		 * int i = 0; int N = 10; positionsCles = new HashMap<String, Point3D>(); for (i
		 * = 0; i < N; i++) { MovableState mstBouee; EntityMouvementSequenceurInit
		 * msiBouee;
		 * 
		 * mstBouee = new MovableState(new Point3D(10 * i, 0, 0), Point3D.ZERO,
		 * Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO); msiBouee = new
		 * EntityMouvementSequenceurInit("MSI", mstBouee, 0, 0, 0, 0, positionsCles);
		 * bsf.getBouees().put(new BoueeFeatures("B1", 5, 1, 3.0), new BoueeInit("B" +
		 * i, msiBouee, Color.RED));
		 * 
		 * }
		 * 
		 */
		// Création de l'ocean
		positionsCles = new HashMap<String, Point3D>();
		MovableState mstOcean = new MovableState(Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO,
				Point3D.ZERO);
		EntityMouvementSequenceurInit msiOcean = new EntityMouvementSequenceurInit("MSIOCEAN", mstOcean, 0, 0, 0, 0,
			null, 0);
		bsf.getOcean().put(new EntityOceanFeature("O1"), new EntityOceanInit("Atlantique", msiOcean));

		LogicalDateTime start = new LogicalDateTime("05/12/2017 06:00");
		LogicalDateTime end = start.add(LogicalDuration.ofMinutes(2));
		BasicMvtScenario bms = new BasicMvtScenario(new ScenarioId("S3"), bsf, start, end);

		return bms;
	}

}
