package be.SimEntity.Artefact.Representation3D;

import enstabretagne.monitor.interfaces.IMovable;
import javafx.scene.paint.Color;

public interface EntityArtefact3DRepresentationInterface extends IMovable{
	Color getColor();
	double getRayon();
	double getLongueur();

}