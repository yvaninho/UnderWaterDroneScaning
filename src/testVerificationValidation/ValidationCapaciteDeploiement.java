package testVerificationValidation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;

public class ValidationCapaciteDeploiement extends AbstractTest{
	
		@Test
		public void test() throws FilloException {
			query();
			
			for (int i=0; i<=getListlog().size()-2;i++)
			{
				LogicalDateTime date1 = new LogicalDateTime(getListlog().get(i));  //date du premier drone
				LogicalDateTime date2 = new LogicalDateTime(getListlog().get(i+1));  // date du drone qui suit le precedent
				LogicalDuration d = date2.soustract(date1);
				assertEquals(LogicalDuration.ofMinutes(10).toString(), d.toString()); // date2 - date1
			}
			
			
		}

		@Override
		public String filePath() {
			
			return "deploiementUnderWaterDetection.xlsx";
		}

		@Override
		public void query() throws FilloException {
			
			String strQuery = "Select * from Logs where Message like '%deployé%'";
			Recordset recordset = getConnectionFile().executeQuery(strQuery);
			

			while (recordset.next()) {
				String a;
				a = recordset.getField("Temps Logique");
				getListlog().add(a);		
				
			}
			
			recordset.close();
			
		}
}
