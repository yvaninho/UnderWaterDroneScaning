package testVerificationValidation;

import org.junit.Test;

import com.codoid.products.exception.FilloException;

public class ValidationExchangeMessage extends AbstractTest {
	

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
		
		
	}
	
}
