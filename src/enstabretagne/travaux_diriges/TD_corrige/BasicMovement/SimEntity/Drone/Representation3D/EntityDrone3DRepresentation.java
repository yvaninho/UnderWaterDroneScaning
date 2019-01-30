package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Drone.Representation3D;

import enstabretagne.monitor.Contrat3D;
import enstabretagne.monitor.ObjTo3DMappingSettings;
import enstabretagne.monitor.implementation.Representation3D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.Sphere;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

@Contrat3D(contrat = EntityDrone3DRepresentationInterface.class)
public class EntityDrone3DRepresentation extends Representation3D {

	public EntityDrone3DRepresentation(ObjTo3DMappingSettings settings) {
		super(settings);
	}

	EntityDrone3DRepresentationInterface Navire3D;
	Group bateau;
	int r1 = 3;
	int h = 5;

	// ici mettre les objets3D représentant l'entité
	// Sphere sph;

	@Override
	public void init(Group world, Object obj) {
		Navire3D = (EntityDrone3DRepresentationInterface) obj;
		bateau = new Group();
		PhongMaterial material = new PhongMaterial(Navire3D.getColor());

		Cylinder cy = new Cylinder(r1, h * 2);
		cy.setMaterial(material);
		cy.setRotationAxis(Rotate.Z_AXIS);
		cy.setRotate(90.0);
		cy.setTranslateX(-h / 2);
		bateau.getChildren().add(cy);

		Sphere s = new Sphere(r1);
		s.setMaterial(material);
		s.setTranslateX(h / 2);
		bateau.getChildren().add(s);

		double c = r1;
		Box b = new Box(c, c, c);
		material = new PhongMaterial(Color.BLUEVIOLET);
		b.setMaterial(material);
		b.setTranslateZ(r1);
		b.setTranslateX(h / 2 - c);
		bateau.getChildren().add(b);
		world.getChildren().add(bateau);

	}

	@Override
	public void update() {
		Point3D p = Navire3D.getPosition();

		bateau.setTranslateX(p.getX());
		bateau.setTranslateY(p.getY());
		bateau.setTranslateZ(p.getZ());
		Point3D rot = Navire3D.getRotationXYZ();
		addRotate(bateau, rot);

	}

}
