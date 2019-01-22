package enstabretagne.travaux_diriges.TD_corrige.BasicMovement;

import java.util.HashMap;
import java.util.Random;

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
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.Representation3D.TypeArtefact;
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

public class ScenarioInstanceBasicMovementOfSysteme implements IScenarioInstance {

	@Override
	public IScenario getScenarioInstance() {
		BasicMvtScenarioFeatures bsf = new BasicMvtScenarioFeatures("BSF");
		
		//Création du navire et des points de passage
		/*HashMap<String,Point3D> positionsCles = new HashMap<String, Point3D>();
		positionsCles.put("start", new Point3D(0,0,0));
		positionsCles.put("PointCible1", new Point3D(10,10,0));
		positionsCles.put("PointCible2", new Point3D(30,-10,0));
		positionsCles.put("PointCible3", new Point3D(20,0,0));
		positionsCles.put("PointDirection", new Point3D(20,20,0));
		positionsCles.put("PointSousEau", new Point3D(0,0,-10));
		positionsCles.put("ObservationMine", new Point3D(20,20,-20));*/
		
///////////////////////////////////moi ////////////////////////////////////////		
		HashMap<String,Point3D> positionsCles = new HashMap<String, Point3D>();
		positionsCles.put("start", new Point3D(0,0,0));
		positionsCles.put("PointCible1", new Point3D(0,0,0));
		positionsCles.put("PointCible2", new Point3D(0,0,0));
		positionsCles.put("PointCible3", new Point3D(0,0,0));
		positionsCles.put("PointDirection", new Point3D(0,0,0));
		positionsCles.put("PointSousEau", new Point3D(0,0,0));
		positionsCles.put("ObservationMine", new Point3D(0,0,0));
	
		
		
///////////////////////////////////moi ////////////////////////////////////////			

		
		MovableState mst; 
		EntityMouvementSequenceurInit msi; 
		EntityMouvementSequenceurFeature feat; 

		//Navire3
		mst = new MovableState(Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO);
		msi = new EntityMouvementSequenceurInit("MSI", mst, 0, 0,0,0, positionsCles);
		feat = new EntityMouvementSequenceurFeature("MSF");
		bsf.getNavires().put(new EntityNavireFeature("NavireF",5,3,Color.BROWN,feat), new EntityNavireInit("Navire Observation 2", msi));

		//Création de artefact
		int i=0;
		
	    int seed = 42;
	    int Xocean = 10000;
	    int Yocean = 10000;
	    int Zocean = -3000;
	    int Zmax = -1000;
	    int NbArtefactmax = 40;
	    int NbArtefactmin = 30;
	    
	    int ProportionSphere = 60;
	    int ProportionCube = 30;
	    int ProportionCylindre = 10;
	    
		//Random rand = new Random(seed);
		MoreRandom generateur = new MoreRandom();
		generateur.setSeed(seed);
		int NB = (int)(generateur.nextUniform(NbArtefactmax, NbArtefactmin));
		int NbSphere = (NB*ProportionSphere)/100;
		int NbBox = (NB*ProportionCube)/100;
		int NbCylinder = (NB*ProportionCylindre)/100;
		
		//// on déclare les 3 types d'artefactes 
		
		
		TypeArtefact Sphere = TypeArtefact.SPHERE;
		TypeArtefact Cube = TypeArtefact.BOX;
		TypeArtefact Cylinder = TypeArtefact.CYLINDER;
		
		
		////////////////////////////////////////////////////
		
		
		positionsCles = new HashMap<String, Point3D>();
		
		
		
		////creation des spheres//////////////
		
		for(i=0;i<NbSphere;i++) 
		{
			MovableState mstArtefact;
			EntityMouvementSequenceurInit msiArtefact;
			EntityMouvementSequenceurFeature mssArtefact;
			int X = (int)(generateur.nextUniform(-Xocean, Xocean));
			int Y = (int)(generateur.nextUniform(-Yocean, Yocean));
			int Z = (int)(generateur.nextUniform(Zocean, Zmax));
			
			mssArtefact = new EntityMouvementSequenceurFeature("B"+i);
			
			mstArtefact = new MovableState(new Point3D(X,Y,-Z), Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO);
			msiArtefact= new EntityMouvementSequenceurInit("MSI", mstArtefact, 0, 0,0,0, positionsCles);
			bsf.getArtefactes().put(new EntityArtefactFeature("B1",5,1,Color.RED,mssArtefact,Sphere ), new EntityArtefactInit("B"+i,msiArtefact));
			
			
		}
		
	////création des cubes
		
		for(i=0;i<NbBox;i++) 
		{
			MovableState mstArtefact;
			EntityMouvementSequenceurInit msiArtefact;
			EntityMouvementSequenceurFeature mssArtefact;
			int X = (int)(generateur.nextUniform(-Xocean, Xocean));
			int Y = (int)(generateur.nextUniform(-Yocean, Yocean));
			int Z = (int)(generateur.nextUniform(Zocean, Zmax));
		
			mssArtefact = new EntityMouvementSequenceurFeature("Cu"+i);
			
			mstArtefact = new MovableState(new Point3D(X,Y,-Z), Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO);
			msiArtefact= new EntityMouvementSequenceurInit("MSI", mstArtefact, 0, 0,0,0, positionsCles);
			bsf.getArtefactes().put(new EntityArtefactFeature("Cu1",5,1,Color.YELLOW,mssArtefact,Cube ), new EntityArtefactInit("Cu"+i,msiArtefact));
			
			
		}
		
		///création des Cylindres 
		
		for(i=0;i<NbCylinder;i++) 
		{
			MovableState mstArtefact;
			EntityMouvementSequenceurInit msiArtefact;
			EntityMouvementSequenceurFeature mssArtefact;
			int X = (int)(generateur.nextUniform(-Xocean, Xocean));
			int Y = (int)(generateur.nextUniform(-Yocean, Yocean));
			int Z = (int)(generateur.nextUniform(Zocean, Zmax));
			
			mssArtefact = new EntityMouvementSequenceurFeature("Cy"+i);
			
			mstArtefact = new MovableState(new Point3D(X,Y,100), Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO);
			msiArtefact= new EntityMouvementSequenceurInit("MSI", mstArtefact, 0, 0,0,0, positionsCles);
			bsf.getArtefactes().put(new EntityArtefactFeature("Cy1",5,1,Color.GREEN,mssArtefact,Cylinder ), new EntityArtefactInit("Cy"+i,msiArtefact));
			
			
		}
		
		//////////////////////creation de l'objet///////////////////
		
		
		for(i=0;i<1;i++) 
		{
			MovableState mstArtefact;
			EntityMouvementSequenceurInit msiArtefact;
			EntityMouvementSequenceurFeature mssArtefact;
			int X = (int)(generateur.nextUniform(-Xocean, Xocean));
			int Y = (int)(generateur.nextUniform(-Yocean, Yocean));
			int Z = (int)(generateur.nextUniform(Zocean, Zmax));
		
			mssArtefact = new EntityMouvementSequenceurFeature("Objet"+i);
			
			mstArtefact = new MovableState(new Point3D(X,Y,-Z), Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO);
			msiArtefact= new EntityMouvementSequenceurInit("MSI", mstArtefact, 0, 0,0,0, positionsCles);
			bsf.getArtefactes().put(new EntityArtefactFeature("Objet1",5,1,Color.BLACK,mssArtefact,Cube ), new EntityArtefactInit("Objet"+i,msiArtefact));
			
			
		}
		
		
		
		
		



		/* int N = 10;
		positionsCles = new HashMap<String, Point3D>();
		for (i = 0; i < N; i++) {
			MovableState mstBouee;
			EntityMouvementSequenceurInit msiBouee;

			mstBouee = new MovableState(new Point3D(10 * i, 0, 0), Point3D.ZERO, Point3D.ZERO, Point3D.ZERO,
					Point3D.ZERO, Point3D.ZERO);
			msiBouee = new EntityMouvementSequenceurInit("MSI", mstBouee, 0, 0, 0, 0, positionsCles);
			bsf.getBouees().put(new BoueeFeatures("B1", 5, 1, 3.0), new BoueeInit("B" + i, msiBouee, Color.RED));

		}
		*/
		
		//Création de l'ocean
		positionsCles = new HashMap<String, Point3D>();
		MovableState mstOcean = new MovableState(Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO, Point3D.ZERO);
		EntityMouvementSequenceurInit msiOcean = new EntityMouvementSequenceurInit("MSIOCEAN", mstOcean, 0, 0,0,0, positionsCles);
		bsf.getOcean().put(new EntityOceanFeature("O1"), new EntityOceanInit("Atlantique", msiOcean));
		
		
		LogicalDateTime start = new LogicalDateTime("05/12/2017 06:00");
		LogicalDateTime end = start.add(LogicalDuration.ofMinutes(2));
		BasicMvtScenario bms = new BasicMvtScenario(new ScenarioId("S2"), bsf, start, end);
		
		return bms;
	}


}
