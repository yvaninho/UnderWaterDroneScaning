package testVerificationValidation;

import java.util.ArrayList;

import org.junit.Test;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;

public class ValidationMouvementDrone extends AbstractTest {
	ArrayList<String> listeMouvements = new ArrayList<String>();
	ArrayList<String> liste = new ArrayList<String>();
	int cpt=0;
	
	@Test
	public void test() throws FilloException {
		query();
	}

	@Override
	public String filePath() {
		
		return "UnderWaterDetection.xlsx";
	}

	@Override
	public void query() throws FilloException {
		String strQuery="Select * from Logs where Message like 'Start%'";
		Recordset recordset=connection.executeQuery(strQuery);
		while(recordset.next()){
			listeMouvements.add(recordset.getField("Message"));
		}
		recordset.close();

		
	}
	
	public boolean testMouvement() {
		
		for (int i=0; i<listeMouvements.size(); i++){
			liste.add(listeMouvements.get(i));
			System.out.println("----------" + listeMouvements.get(i));
			liste.spliterator();

		}
		for(int j=0; j<liste.size();j++){
			if((liste.get(j).contains("Lineaire")) && (liste.get(j+1).contains("circulaire"))){
				cpt++;
			}
		}
		if(cpt++ >=2 )
			return true;
	
		return false;
		
	}

}
