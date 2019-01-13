package beRepresentation3D;


import enstabretagne.monitor.Contrat3D;
import enstabretagne.monitor.ObjTo3DMappingSettings;
import enstabretagne.monitor.implementation.Representation3D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

@Contrat3D(contrat = IBoueeRepresentation3D.class)
public class Bouee3DRepresentation2 extends Representation3D {
	
	public Bouee3DRepresentation2(ObjTo3DMappingSettings settings) {
		super(settings);
	}

	IBoueeRepresentation3D bouee3D;
	Group maBouee;
	@Override
	public void init(Group world, Object obj) {
		bouee3D = (IBoueeRepresentation3D) obj;
		maBouee = new Group();
	    
	    PhongMaterial material = new PhongMaterial(bouee3D.getColor());


	    Sphere s = new Sphere(bouee3D.getSize());
	    s.setMaterial(material);
	    maBouee.getChildren().add(s);
	    world.getChildren().add(maBouee);

	}
	
	@Override
	public void update() {
		Point3D p = bouee3D.getPosition();

		maBouee.setTranslateX(p.getX());
		maBouee.setTranslateY(p.getY());
		maBouee.setTranslateZ(p.getZ());
		

	}


}
	