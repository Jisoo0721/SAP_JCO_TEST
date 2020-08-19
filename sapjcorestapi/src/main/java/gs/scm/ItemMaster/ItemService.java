package gs.scm.ItemMaster;
import com.sap.conn.jco.JCoException;

import org.springframework.stereotype.Service;

import gs.scm.test.TestJcoFunctionCalls;

@Service
public class ItemService {

	protected ItemMasterAPI itemApi;
	protected TestJcoFunctionCalls test;

	public void getFlightList(ItemMasterTable params) {

		try {
			test.pingDestination();
			test.requestSystemDetails();
			test.callWithStructure();
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
