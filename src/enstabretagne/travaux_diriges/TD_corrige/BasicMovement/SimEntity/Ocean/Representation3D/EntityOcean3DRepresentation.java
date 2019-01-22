package enstabretagne.travaux_diriges.TD_corrige.BasicMovement.SimEntity.Ocean.Representation3D;

import enstabretagne.monitor.Contrat3D;
import enstabretagne.monitor.ObjTo3DMappingSettings;
import enstabretagne.monitor.implementation.Representation3D;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.Sphere;
import javafx.scene.shape.Box;

@Contrat3D(contrat = EntityOcean3DRepresentationInterface.class)
public class EntityOcean3DRepresentation extends Representation3D {
	
	public EntityOcean3DRepresentation(ObjTo3DMappingSettings settings) {
		super(settings);
	}

	EntityOcean3DRepresentationInterface Ocean3D;
	
	//ici mettre les objets3D représentant l'entité 
	//Sphere sph;

	@Override
	public void init(Group world, Object obj) {
		Ocean3D = (EntityOcean3DRepresentationInterface) obj;
		PhongMaterial OceanMaterial = new PhongMaterial();
		OceanMaterial.setDiffuseColor(Color.BLACK);

		double size = 200;
		double epaisseurs = 10;
		Group g = new Group();

		double alpha0 = 0.1;
		double max = size / epaisseurs;
		int i = 0;
		for (i = 0; i < max; i++) {
			Box b = new Box(size, size, epaisseurs);
			PhongMaterial seaMaterial = new PhongMaterial();
			double alpha = alpha0 - i / max * alpha0;
			seaMaterial.setDiffuseColor(Color.rgb(0,0, 255, alpha));

			b.setTranslateZ(-epaisseurs * (i + 1) / 2);
			b.setMaterial(seaMaterial);

			g.getChildren().add(b);
		}

		
		world.getChildren().add(g);

	}

	@Override
	public void update() {
//		sph.setTranslateX(Ocean3D.getPosition().getX());
//		sph.setTranslateY(Ocean3D.getPosition().getY());
//		sph.setTranslateZ(Ocean3D.getPosition().getZ());
	}


}
