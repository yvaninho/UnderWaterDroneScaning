package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.Scenarios;

import java.util.Map;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.components.IEntity;
import enstabretagne.simulation.components.ScenarioId;
import enstabretagne.simulation.components.data.SimFeatures;
import enstabretagne.simulation.components.data.SimInitParameters;
import enstabretagne.simulation.components.implementation.SimEntity;
import enstabretagne.simulation.components.implementation.SimScenario;
import enstabretagne.simulation.core.implementation.SimEvent;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefact;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefactFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.EntityArtefactInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.Bouee;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.BoueeFeatures;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Bouee.BoueeInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDrone;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDroneFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDroneInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavire;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavireFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.EntityNavireInit;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.EntityOcean;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.EntityOceanFeature;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.EntityOceanInit;

public class BasicMvtScenario extends SimScenario{

	public BasicMvtScenario(ScenarioId id, SimFeatures features, LogicalDateTime start, LogicalDateTime end) {
		super(id, features, start, end);
		
	}
	
	@Override
	protected void initializeSimEntity(SimInitParameters init) {
		super.initializeSimEntity(init);
	}
	
	@Override
	protected void AfterActivate(IEntity sender, boolean starting) {
		super.AfterActivate(sender, starting);
		
		BasicMvtScenarioFeatures feature = (BasicMvtScenarioFeatures) getFeatures();
		Logger.Detail(this, "afteractivate", "taille liste bouees = %s", feature.getBouees().size());

		for(Map.Entry<BoueeFeatures, BoueeInit> e : feature.getBouees().entrySet())
		{
			Logger.Detail(this, "afteractivate", "bouee à créer = %s , %s", e.getValue(),e.getKey());
			Post(new BoueeArrival(e.getValue(),e.getKey()));
		}
		
		for(Map.Entry<EntityNavireFeature, EntityNavireInit> e : feature.getNavires().entrySet())
		{
			Logger.Detail(this, "afteractivate", "navire à créer = %s , %s", e.getValue(),e.getKey());
			Post(new NavireArrival(e.getValue(),e.getKey()));
		}
		
		
		for(Map.Entry<EntityArtefactFeature, EntityArtefactInit> e : feature.getArtefactes().entrySet() )
		{
			Logger.Detail(this, "afteractivate", "artefact à créer = %s , %s", e.getValue(),e.getKey());
			Post(new ArtefactArrival(e.getValue(),e.getKey()));
		}	
		
		
		for(Map.Entry<EntityDroneFeature, EntityDroneInit> e : feature.getDrones().entrySet() )
		{
			Logger.Detail(this, "afteractivate", "Drone  à créer = %s , %s", e.getValue(),e.getKey());
			Post(new DroneArrival(e.getValue(),e.getKey()));
		}	
				
		
		for(Map.Entry<EntityOceanFeature, EntityOceanInit> e : feature.getOcean().entrySet())
		{
			Logger.Detail(this, "afteractivate", "navire à créer = %s , %s", e.getValue(),e.getKey());
			Post(new OceanArrival(e.getValue(),e.getKey()));
		}
		
	
	}
	
	class BoueeArrival extends SimEvent
	{
		private BoueeInit i;
		private BoueeFeatures f;
		public BoueeInit getI() {
			return i;
		}
		
		public BoueeFeatures getF() {
			return f;
		}
		
		
		public BoueeArrival(BoueeInit i, BoueeFeatures f) {
			this.i=i;
			this.f=f;
		}

		@Override
		public void Process() {
			Logger.Detail(this, "BoueeArrival.Process", "Création de la baie " + i);
			SimEntity b = createChild(Bouee.class, i.getName() , f);
			b.initialize(getI());
			b.activate();
		}
		
	}

	class NavireArrival extends SimEvent
	{
		private EntityNavireInit i;
		private EntityNavireFeature f;
		
		public EntityNavireInit getI() {
			return i;
		}
		
		public EntityNavireFeature getF() {
			return f;
		}
		
		
		
		public NavireArrival(EntityNavireInit i, EntityNavireFeature f) {
			this.i=i;
			this.f=f;
		}

		@Override
		public void Process() {
			Logger.Detail(this, "NavireArrival.Process", "Création du Navire" + i);
			SimEntity b = createChild(EntityNavire.class, i.getName() , f);
			b.initialize(getI());
			b.activate();
		}
		
	}




	class DroneArrival extends SimEvent
	{
		private EntityDroneInit i;
		private EntityDroneFeature f;
		
		public EntityDroneInit getI() {
			return i;
		}
		
		public EntityDroneFeature getF() {
			return f;
		}
		
		
		public DroneArrival(EntityDroneInit i, EntityDroneFeature f) {
			this.i=i;
			this.f=f;
		}

		@Override
		public void Process() {
			Logger.Detail(this, "DroneArrival.Process", "Création du Navire" + i);
			SimEntity b = createChild(EntityDrone.class, i.getName() , f);
			b.initialize(getI());
			b.activate();
		}
		
	}

	
	class OceanArrival extends SimEvent
	{
		private EntityOceanInit i;
		private EntityOceanFeature f;
		
		public EntityOceanInit getI() {
			return i;
		}
		
		public EntityOceanFeature getF() {
			return f;
		}
		
		
		public OceanArrival(EntityOceanInit i, EntityOceanFeature f) {
			this.i=i;
			this.f=f;
		}

		@Override
		public void Process() {
			Logger.Detail(this, "OceanArrival.Process", "Création de l'océan" + i);
			SimEntity b = createChild(EntityOcean.class, i.getName() , f);
			b.initialize(getI());
			b.activate();
		}
		
	}


	class ArtefactArrival extends SimEvent
	{
		private EntityArtefactInit i;
		private EntityArtefactFeature f;
		
		public  EntityArtefactInit getI() {
			return i;
		}
		
		public EntityArtefactFeature getF() {
			return f;
		}
		
		
		public ArtefactArrival(EntityArtefactInit i, EntityArtefactFeature f) {
			this.i=i;
			this.f=f;
		}

		@Override
		public void Process() {
			Logger.Detail(this, "ArtefactArrival.Process", "Création de l'artefact " + i);
			SimEntity b = createChild(EntityArtefact.class, i.getName() , f);
			b.initialize(getI());
			b.activate();
		}
		
	}



	
}
