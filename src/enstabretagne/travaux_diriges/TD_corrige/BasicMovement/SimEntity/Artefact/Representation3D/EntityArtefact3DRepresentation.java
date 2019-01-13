package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Artefact.Representation3D;

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
		artefact = new Group();
		PhongMaterial material = new PhongMaterial(artefact3D.getColor());
		Sphere s = new Sphere(r1);
		s.setMaterial(material);
		s.setTranslateX(h / 2);
		artefact.getChildren().add(s);
		world.getChildren().add(artefact);

	}

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
