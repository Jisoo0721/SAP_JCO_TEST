package gs.scm.ItemMaster;
import com.sap.conn.jco.JCoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gs.scm.test.TestJcoFunctionCalls;

@Service
public class ItemService {

	@Autowired
	protected ItemMasterAPI itemApi;
	@Autowired
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
