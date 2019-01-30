package testVerificationValidation;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;

public abstract class AbstractTest {
	public abstract String filePath();
	public abstract void query() throws FilloException; 
	
	protected ArrayList<String> listLog;
	protected Connection connection;
	protected Fillo fillo ;
	
	public ArrayList<String> getListlog() {
		return listLog;
	}

	public Connection load () throws FilloException {
		
		connection = fillo.getConnection(filePath());
		return connection;
	}
	
	
	@Before
	public void before() throws FilloException {
		fillo = new Fillo();
		listLog =  new ArrayList<>();
		this.connection=load();
	}
	
	@After
	public void after() {
		connection.close();
		
	}

	public Connection getConnectionFile() {
		return connection;
	}
	
}
