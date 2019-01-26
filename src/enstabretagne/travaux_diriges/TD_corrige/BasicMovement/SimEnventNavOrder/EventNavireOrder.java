package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEnventNavOrder;

import enstabretagne.simulation.core.implementation.SimEvent;
import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.EntityDrone;

public class EventNavireOrder extends SimEvent  {
	
	private EntityDrone drone ;
	
	public EventNavireOrder( EntityDrone drone ) {
		
		this.drone = drone ;
		
	}

	@Override
	public void Process() {
		// TODO Auto-generated method stub
	
	}

}
