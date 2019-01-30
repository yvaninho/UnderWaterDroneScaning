package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.Representation3D;

import enstabretagne.monitor.Contrat3D;
import enstabretagne.monitor.ObjTo3DMappingSettings;
import enstabretagne.monitor.implementation.Representation3D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.Sphere;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;


@Contrat3D(contrat = EntityArtefact3DRepresentationInterface.class)
public class EntityArtefact3DRepresentation extends Representation3D {

	public EntityArtefact3DRepresentation(ObjTo3DMappingSettings settings) {
		super(settings);
	}

	EntityArtefact3DRepresentationInterface artefact3D;
	Group artefact;
	int r1 = 3;
	int h = 5;

	// ici mettre les objets3D représentant l'entité
	// Sphere sph;

	@Override
	public void init(Group world, Object obj) {
		artefact3D = (EntityArtefact3DRepresentationInterface) obj;
		switch (artefact3D.getType()) {
		case SPHERE:
			initSphere(world, artefact3D);
			break;
		case BOX:
			initBox(world, artefact3D);
			break;

		case CYLINDER:
			initCylinder(world, artefact3D);
			break;
		}
	}
	//////////////////// moi//////////////////////////////////

	public void initSphere(Group world, EntityArtefact3DRepresentationInterface artefact3D) {
		artefact = new Group();
		PhongMaterial material = new PhongMaterial(artefact3D.getColor());
		Sphere s = new Sphere(r1);
		s.setMaterial(material);
		s.setTranslateX(h / 2);
		artefact.getChildren().add(s);
		world.getChildren().add(artefact);

	}

	public void initBox(Group world, EntityArtefact3DRepresentationInterface artefact3D) {
		artefact = new Group();
		PhongMaterial material = new PhongMaterial(artefact3D.getColor());
		Box b = new Box(r1, r1, r1);
		b.setMaterial(material);
		b.setTranslateZ(r1);
		b.setTranslateX(h / 2); // commenté par moi meme
		artefact.getChildren().add(b);
		world.getChildren().add(artefact);

	}

	public void initCylinder(Group world, EntityArtefact3DRepresentationInterface artefact3D) {
		artefact = new Group();
		PhongMaterial material = new PhongMaterial(artefact3D.getColor());
		Cylinder c = new Cylinder(r1, h);
		c.setMaterial(material);
		c.setTranslateX(h / 2);
		artefact.getChildren().add(c);
		world.getChildren().add(artefact);

	}

	////////////////////////////////////////////
	@Override
	public void update() {
		Point3D p = artefact3D.getPosition();

		artefact.setTranslateX(p.getX());
		artefact.setTranslateY(p.getY());
		artefact.setTranslateZ(p.getZ());
		Point3D rot = artefact3D.getRotationXYZ();
		addRotate(artefact, rot);

	}

}
