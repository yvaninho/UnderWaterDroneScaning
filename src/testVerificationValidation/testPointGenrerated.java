package testVerificationValidation;

import java.util.Map;

import enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.MouvementSequenceur.GeneratePoints;
import javafx.geometry.Point3D;

public class testPointGenrerated {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GeneratePoints g = new GeneratePoints(new Point3D(0,0,0) , new Point3D(800,0,0), 4);
		Map<String, Point3D> Point = g.getPoints();
		//Map<Integer, Map<String, Point3D>> Point = g.getPoints();
		System.out.println("size : "+Point.size()+"\nPoints"+Point);
		
		/*
		for (Integer key : Point.keySet()) {
			Map<String, Point3D> p = Point.get(key);
			for (String key2 : p.keySet())
				System.out.println(Point.get(key2));
		
		}*/
			
	}

}
