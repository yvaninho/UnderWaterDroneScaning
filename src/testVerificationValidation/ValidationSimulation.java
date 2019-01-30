package testVerificationValidation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;

public class ValidationSimulation extends AbstractTest {

	private String fonction = null;

	@Test
	public void test() throws FilloException {
		query();
		assertEquals(1, getListlog().size()); // test si il y a un message que l'objet à été trouvéé
		assertEquals("(EntityMouvementSequenceurDrone.java:320)>Process", fonction); // test que c'est bien le drone qui
																						// scan detecte et verifie si
																						// c'est l'artefact
																						// en verifiant si c'est bien
																						// dans sa fonction qu'il est
																						// appelée
	}

	@Override
	public String filePath() {

		return "UnderWaterDetection.xlsx";
	}

	@Override
	public void query() throws FilloException {

		String strQuery = "Select * from Logs where Message like '%found%'";
		Recordset recordset = getConnectionFile().executeQuery(strQuery);

		while (recordset.next()) {
			String a;
			a = recordset.getField("Message");
			fonction = recordset.getField("Fonction");
			getListlog().add(a);

		}

		recordset.close();

	}

}
