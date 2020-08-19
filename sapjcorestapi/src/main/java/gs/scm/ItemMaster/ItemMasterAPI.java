package gs.scm.ItemMaster;

import java.sql.CallableStatement;
import java.sql.Connection;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gs.scm.Constants;
import gs.scm.UTIL.DBUtil;
import gs.scm.test.TestJcoFunctionCalls;
@Controller
@RequestMapping(value = "/", method = RequestMethod.GET)
public class ItemMasterAPI
{
	public CallableStatement cs = null;
	public Connection DBCon = null;
	protected TestJcoFunctionCalls test;	
	{			
		try {

			test.pingDestination();
			test.requestSystemDetails();
			test.callWithStructure();
						
			// 서버 연결정보 세팅 
			JCoDestination jcoDestination = JCoDestinationManager.getDestination(Constants.DESTINATION_NAME);

			// 연결할 티코드 담기
			JCoFunction function = jcoDestination.getRepository().getFunction("Z_PESCM_IF_ITEM_MST");

			// Import 파라미터 전달
			function.getImportParameterList().setValue("I_WERKS", "1600");

			System.out.println("Calling Z_PESCM_IF_ITEM_MST");

			// 연결을 통해서 Import 실행
			function.execute(jcoDestination);

			// 연결 이후 Export 받을 테이블 전달
			JCoParameterList exports = function.getExportParameterList();
			JCoTable tabe = function.getTableParameterList().getTable("ET_DATA");

			// DB 연결, 쿼리 준비
			DBUtil db = new DBUtil();
			String query = null;

			// 디비 준비상태
			db.ps = db.DBCon.prepareStatement(query);

			// Export 데이터 ItemMaster 객체로 전달
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
}