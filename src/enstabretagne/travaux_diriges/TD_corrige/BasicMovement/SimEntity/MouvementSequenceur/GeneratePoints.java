package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur;

import java.util.HashMap;

import enstabretagne.base.time.LogicalDateTime;
import javafx.geometry.Point3D;

public class GeneratePoints {
	
	private HashMap<String, Point3D> pointsClefs ;
	
	public GeneratePoints(HashMap<String, Point3D> pointsClefs) {

		this.pointsClefs = pointsClefs ;
	
	}
	
	public HashMap<String, Point3D> getPoints(int nbPoints) {

		Point3D A = pointsClefs.get("A");
		
		Point3D B = pointsClefs.get("B");
		int amplitude = (int) (B.getY() - A.getY());
		int L = (int) (B.getX() - A.getX());
		String pointName = "";
		int x;
		int y;
		int k = 1;
		int zPlongee = 0;
		for (int i = 0; i <= nbPoints; i = i + 2) {

			pointName = "PointCible" + i;
			x = ((L * i) / nbPoints);
			y = (amplitude * (k % 2));
			pointsClefs.put(pointName, new Point3D(x, y, zPlongee));
			x = (L * (i + 1)) / nbPoints;
			y = (amplitude * (k % 2));
			pointName = "PointCible" + (i + 1);
			pointsClefs.put(pointName, new Point3D(x, y, zPlongee));
			k++;
		}
		return pointsClefs;

	}
}
