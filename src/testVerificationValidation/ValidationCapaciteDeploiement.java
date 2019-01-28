package testVerificationValidation;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class ValidationBateau {

	public static void main(String[] args) throws FilloException {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		Fillo fillo = new Fillo();
		Connection connection = fillo.getConnection("log\\1548548316685BasicMovement.xlsx");
		System.out.println("Fichier Chargé");
		// String strQuery="Select * from Logs where ID=100 and name='John'";
		String strQuery = "Select * from Logs Message like '%'";
		Recordset recordset = connection.executeQuery(strQuery);

		while (recordset.next()) {
			System.out.println(recordset);
			//System.out.println(recordset.getField("Message"));
		}

		recordset.close();
		connection.close();

	}

}
