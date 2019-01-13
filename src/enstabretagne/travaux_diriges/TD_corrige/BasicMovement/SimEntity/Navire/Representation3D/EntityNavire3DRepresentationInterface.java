package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Navire.Representation3D;

import enstabretagne.monitor.interfaces.IMovable;
import javafx.scene.paint.Color;

public interface EntityNavire3DRepresentationInterface extends IMovable{
	Color getColor();
	double getRayon();
	double getLongueur();

}