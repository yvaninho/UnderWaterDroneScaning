package testVerificationValidation;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;

public class ValidationEntityNavireEtBateauArtefact extends AbstractTest {

	private ArrayList<String> listbateau = new ArrayList<>();
	private ArrayList<String> listdrone = new ArrayList<>();
	private ArrayList<String> listartefact = new ArrayList<>();
	private ArrayList<String> listocean = new ArrayList<>();

	@Test
	public void test() throws FilloException {
		query();
		assertEquals(1, listbateau.size()); // test si un Navire est cree
		assertEquals(1, listdrone.size()); // test si un seul drone est cree et deployé
		assertTrue(listartefact.size() <= 40 && listartefact.size() >= 30); // test si le nombre d'artefact est bien
																			// compris entre 30 et 40

		
		assertEquals(1, listocean.size());   // test si un ocean est creer
	}

	@Override
	public String filePath() {
		return "UnderWaterDetection.xlsx";
	}

	@Override
	public void query() throws FilloException {
		String strQuery = "Select * from Logs where Message like 'navire à créer%'";
		Recordset recordset = connection.executeQuery(strQuery);

		String strQuery1 = "Select * from Logs where Message like 'Sous marin N ° 1 deployé %'";
		Recordset recordset1 = connection.executeQuery(strQuery1);

		String strQuery2 = "Select * from Logs where Message like 'artefact à créer %'";
		Recordset recordset2 = connection.executeQuery(strQuery2);
		
		String strQuery3 = "Select * from Logs where Message like 'Ocean à créer%'";
		Recordset recordset3 = connection.executeQuery(strQuery3);

		while (recordset.next()) {
			String a;
			a = recordset.getField("Message");
			listbateau.add(a);
		}

		while (recordset1.next()) {
			String a;
			a = recordset1.getField("Message");
			listdrone.add(a);
		}

		while (recordset2.next()) {
			String a;
			a = recordset2.getField("Message");
			listartefact.add(a);
		}
		
		while (recordset3.next()) {
			String a;
			a = recordset3.getField("Message");
			listocean.add(a);
		}

		recordset.close();
		recordset1.close();
		recordset2.close();
		recordset3.close();
	}

}
