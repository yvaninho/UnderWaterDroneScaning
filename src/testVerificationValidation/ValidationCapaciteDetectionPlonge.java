package testVerificationValidation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

import org.junit.Test;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import javafx.geometry.Point3D;

public class ValidationCapaciteDetectionPlonge extends AbstractTest {

	private ArrayList<String> listtracking = new ArrayList<>();
	private ArrayList<String> listplongee = new ArrayList<>();
	private ArrayList<String> listrotation360 = new ArrayList<>();
	private ArrayList<String> listremontee = new ArrayList<>();

	@Test
	public void test() throws FilloException {
		query();
		testDureeScan(); // test si les scan s'effectue tous après 1 minute
		testplongeeRotationMontee(); // test si chaque plongée s'effectue a une distance de 10m

	}

	@Override
	public String filePath() {

		return "1548885638358UnderWaterDetection.xlsx";
	}

	@Override
	public void query() throws FilloException {
		String strQuery = "Select * from Logs where Message like '%start scan ocean Drone Observation 1%'";
		Recordset recordset = getConnectionFile().executeQuery(strQuery);

		String strQuery1 = "Select * from Logs where Message like '%Start Track Target%'";
		Recordset recordset1 = getConnectionFile().executeQuery(strQuery1);

		String strQuery2 = "Select * from Logs where Message like '%Start Plongee Drone Observation 1%'";
		Recordset recordset2 = getConnectionFile().executeQuery(strQuery2);

		String strQuery3 = "Select * from Logs where Message like '%Start Turn Around%'";
		Recordset recordset3 = getConnectionFile().executeQuery(strQuery3);

		String strQuery4 = "Select * from Logs where Message like '%Start Montee %'";
		Recordset recordset4 = getConnectionFile().executeQuery(strQuery4);

		while (recordset.next()) {
			String a;
			a = recordset.getField("Temps Logique");
			getListlog().add(a);

		}

		while (recordset1.next()) {
			String a;
			a = recordset1.getField("Message");
			listtracking.add(a);
		}

		while (recordset2.next()) {
			String a;
			a = recordset2.getField("Message");
			listplongee.add(a);
		}

		while (recordset3.next()) {
			String a;
			a = recordset3.getField("Message");
			listrotation360.add(a);

		}

		while (recordset4.next()) {
			String a;
			a = recordset4.getField("Message");
			listremontee.add(a);
		}

		recordset.close();
		recordset1.close();
		recordset2.close();
		recordset3.close();
		recordset4.close();

	}

	public void testDureeScan() {

		assertTrue(0 != getListlog().size()); // test quil y a bien une fonction scan : l'evenement existe bien
		for (int i = 0; i <= getListlog().size() - 2; i++) {
			LogicalDateTime date1 = new LogicalDateTime(getListlog().get(i)); // date du 1er scan
			LogicalDateTime date2 = new LogicalDateTime(getListlog().get(i + 1)); // date du scan suivant
			LogicalDuration d = date2.soustract(date1);
			assertEquals(LogicalDuration.ofMinutes(1).toString(), d.toString()); // date2 - date1
		}
	}

	public void testplongeeRotationMontee() {

		assertTrue(0 != listplongee.size()); // test quil y a bien une fonction plongee : l'evenement existe bien
		assertTrue(0 != listremontee.size()); // test quil y a bien une fonction remontee : l'evenement existe bien
		assertTrue(0 != listrotation360.size()); // test quil y a bien une fonction rotation : l'evenement existe bien

		ArrayList<String> coords = new ArrayList<>();
		ArrayList<String> coords2 = new ArrayList<>();

		for (int i = 0; i <= listplongee.size() - 1; i++) {
			coords.add(listplongee.get(i).split("Point3D")[1]);
			coords2.add(listtracking.get(i).split("Point3D")[1]);
		}

		// test distance de plongé
		for (int i = 0; i <= coords.size() - 2; i++) {

			Point3D pt1 = new Point3D(new Double(coords.get(i).split("x")[1].substring(3, 4)),
					new Double(coords.get(i).split("y")[1].substring(3, 4)),
					new Double(coords.get(i).split("z")[1].substring(3, 7))); // point
			// en plongee
			Point3D pt2 = new Point3D(new Double(coords2.get(i).split("x")[1].substring(3, 4)),
					new Double(coords2.get(i).split("y")[1].substring(3, 4)),
					new Double(coords2.get(i).split("z")[1].substring(3, 7))); // point
			// en arrivee de plongee
			Point3D d = pt1.subtract(pt2);
			assertEquals(new Point3D(0, 0, 10).toString(), d.toString());
		}

	}

}
