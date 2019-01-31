package testVerificationValidation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;

public class ValidationExchangeMessage extends AbstractTest {

	Map<String, String> messageBateau = new HashMap<String, String>();
	Map<String, String> messageSeqSousMarin = new HashMap<String, String>();


	@Test
	public void test() throws FilloException {
		query();
		assertTrue(testMessage());
	}

	@Override
	public String filePath() {

		return "UnderWaterDetection.xlsx";
	}

	@Override
	public void query() throws FilloException {

		String strQuery = "Select * from Logs where Message like 'MissionComplete%'";
		Recordset recordset = connection.executeQuery(strQuery);

		String strQuery1 = "Select * from Logs where Message like '%Receive%'";
		Recordset recordset1 = connection.executeQuery(strQuery1);

		while (recordset.next()) {
			messageBateau.put(recordset.getField("Nom Objet"), recordset.getField("Message"));
		}

		while (recordset1.next()) {
			messageSeqSousMarin.put(recordset1.getField("Nom Objet"), recordset1.getField("Message"));
		}

		recordset.close();
		recordset1.close();

	}

	public boolean testMessage() {
		for (Entry<String, String> receptionB : messageSeqSousMarin.entrySet()) {
			if (receptionB.getKey().contains("Navire")
					&& receptionB.getValue().replace(" ", "").equals("ReceiveInfosArtefact")) {

				for (Entry<String, String> receptionSeq : messageBateau.entrySet()) {
					if (receptionSeq.getKey().equals("monSequenceur")
							&& receptionSeq.getValue().contains("MissionComplete")) {

						return true;

					}
				}
			}

		}
		return false;
	}

}
