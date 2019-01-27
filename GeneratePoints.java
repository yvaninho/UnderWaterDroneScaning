package utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.geometry.Point3D;

public class GeneratePoints {

	private Point3D startingPt;
	private Point3D endPoint;
	private Integer nbOscillation;
	private double amp;
	private double d;

	public Integer getNbOsicillation() {
		return nbOscillation;
	}

	public GeneratePoints(Point3D A, Point3D B, int nbOs) {
		this.startingPt = A;
		this.endPoint = B;
		this.nbOscillation = nbOs;
		if (Math.abs(endPoint.getY()) != Math.abs(startingPt.getY()))
			this.amp = Math.abs(endPoint.getY()) - Math.abs(startingPt.getY());
		else
			this.amp = 5; // amplitude max
		this.d = (Math.abs(endPoint.getX()) - Math.abs(startingPt.getX())) / (nbOscillation * 2);
	}

	public Map<String, Point3D> getPoints() {
		if (endPoint.getX() < 0 && endPoint.getY() < 0) {
			System.out.println("construction tangente");
			return tanConstruct();
		}
			
		else if (endPoint.getX() > 0 && endPoint.getY() > 0) {
			System.out.println("construction All");
			return AllConstruct();
		}
		
		else if (endPoint.getX() > 0 && endPoint.getY() < 0) {
			System.out.println("construction sinus");
			return cosConstruct();
		}
		
		else {
			System.out.println("construction cosnus");
			return sinConstruct();
		}
		

	}
	
	

	public Map<String, Point3D> AllConstruct() {
		Map<String, Point3D> positionsClesP = new LinkedHashMap<String, Point3D>();
		ArrayList<Point3D> L = new ArrayList<>();
		ArrayList<Point3D> Ls = new ArrayList<>();
		positionsClesP.put("start", startingPt);
		L.add(startingPt);
		Ls = L;
		int j=0;
		for (int i = 0; i < nbOscillation; i++) {
			int k=0;
			k++;
			Point3D p = Ls.get(L.size() - 1);
			p = p.add(0, amp, 0);
			positionsClesP.put("rect"+(k+j), p);
			L.add(p);

			p = p.add(d, 0, 0);
			k++;
			positionsClesP.put("cir"+(k+j), p);
			L.add(p);
			k++;
			p = p.subtract(0, amp, 0);
			positionsClesP.put("rect"+(k+j), p);
			L.add(p);
			k++;
			p = p.add(d, 0, 0);
			positionsClesP.put("cir"+(k+j), p);
			L.add(p);
			Ls = L;
			j=j+4;
		}
		
		

		if (L.get(L.size() - 1).getY() != endPoint.getY()) {
			Point3D p = Ls.get(L.size() - 1);
			
				p = p.add(0, amp, 0);
			
			System.out.println("boucle :" + " fin point " + p);
			positionsClesP.put("rect"+ (nbOscillation*4+1), p);
			Ls.add(p);
		}

		//positionsClesP.put("endPt", endPoint);
		Ls.add(endPoint);

		return positionsClesP;
	}
	
	
	private Map<String, Point3D> cosConstruct() {
		Map<String, Point3D> positionsClesP = new LinkedHashMap<String, Point3D>();
		ArrayList<Point3D> L = new ArrayList<>();
		ArrayList<Point3D> Ls = new ArrayList<>();
		positionsClesP.put("start", startingPt);
		L.add(startingPt);
		Ls = L;
		int j = 0;
		for (int i = 0; i < nbOscillation; i++) {
			int k=0;
			k++;
			Point3D p = Ls.get(L.size() - 1);
			p = p.subtract(0, amp, 0);
			positionsClesP.put("rect"+(k+j), p);
			L.add(p);
			k++;
			p = p.add(d, 0, 0);

			positionsClesP.put("cir"+(k+j), p);
			L.add(p);
			k++;
			p = p.add(0, amp, 0);
			positionsClesP.put("rect"+(k+j), p);
			L.add(p);
			k++;
			p = p.add(d, 0, 0);
			positionsClesP.put("cir"+(k+j), p);
			L.add(p);
			Ls = L;
			j=j+4;

		}
		

		if (Ls.get(L.size() - 1).getY() != endPoint.getY()) {
			Point3D p = Ls.get(L.size() - 1);
			
			p = p.subtract(0, amp, 0);
			System.out.println("boucle :" + " fin point " + p);
			positionsClesP.put("rect"+ (nbOscillation*4+1), p);
			L.add(p);
			Ls.add(p);
		}

		//positionsClesP.put("endPt", endPoint);
		//System.out.println("Points"+positions);
		//Ls.add(endPoint);

		return positionsClesP;
	}

	private Map<String, Point3D> sinConstruct() {
		Map<String, Point3D> positionsClesP = new LinkedHashMap<String, Point3D>();
		ArrayList<Point3D> L = new ArrayList<>();
		ArrayList<Point3D> Ls = new ArrayList<>();
		positionsClesP.put("start", startingPt);
		L.add(startingPt);
		Ls = L;
		int j=0;
		for (int i = 0; i < nbOscillation; i++) {
			int k=0;
			k++;
			Point3D p = Ls.get(L.size() - 1);
			p = p.add(0, amp, 0);
			positionsClesP.put("rect"+(k+j), p);
			L.add(p);
			System.out.println("boucle : "+i + " : " + p);
			k++;
			p = p.subtract(d, 0, 0);
			positionsClesP.put("cir"+(k+j), p);
			L.add(p);
			System.out.println("boucle : "+i + " : " + p);
			k++;
			p = p.subtract(0, amp, 0);
			positionsClesP.put("rect"+(k+j), p);
			L.add(p);
			System.out.println("boucle : "+i + " : " + p);
			k++;
			p = p.subtract(d, 0, 0);
			positionsClesP.put("cir"+(k+j), p);
			L.add(p);
			System.out.println("boucle : "+i + " : " + p);
			Ls = L;
			j=j+4;

		}
		if (L.get(L.size() - 1).getY() != endPoint.getY()) {
			Point3D p = Ls.get(L.size() - 1);
			
				p = p.add(0, amp, 0);
			

			System.out.println("boucle :" + " fin point " + p);
			positionsClesP.put("rect"+ (nbOscillation*4+1), p);
			Ls.add(p);
		}

		//positionsClesP.put("endPt", endPoint);
		//Ls.add(endPoint);

		return positionsClesP;
	}

	public Map<String, Point3D> tanConstruct() {
		Map<String, Point3D> positionsClesP = new LinkedHashMap<String, Point3D>();
		ArrayList<Point3D> L = new ArrayList<>();
		ArrayList<Point3D> Ls = new ArrayList<>();
		positionsClesP.put("start", startingPt);
		L.add(startingPt);
		Ls = L;
		int j=0;
		for (int i = 0; i < nbOscillation; i++) {
			int k=0;
			k++;
			Point3D p = Ls.get(L.size() - 1);
			p = p.subtract(0, amp, 0);
			positionsClesP.put("rect"+(k+j), p);
			L.add(p);
			k++;
			p = p.subtract(d, 0, 0);
			positionsClesP.put("cir"+(k+j), p);
			L.add(p);
			k++;
			p = p.add(0, amp, 0);
			positionsClesP.put("rect"+(k+j), p);
			L.add(p);
			k++;
			p = p.subtract(d, 0, 0);
			positionsClesP.put("cir"+(k+j), p);
			L.add(p);
			Ls = L;
			j=j+4;

		}

		if (L.get(L.size() - 1).getY() != endPoint.getY()) {
			Point3D p = Ls.get(L.size() - 1);
			
			p = p.subtract(0, amp, 0);
			

			System.out.println("boucle :" + " fin point " + p);
			positionsClesP.put("rect"+ (nbOscillation*4+1), p);
			Ls.add(p);
		}

		//positionsClesP.put("endPt", endPoint);
		//Ls.add(endPoint);

		return positionsClesP;
	}

}
