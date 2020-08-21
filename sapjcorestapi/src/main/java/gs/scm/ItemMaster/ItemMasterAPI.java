package gs.scm.ItemMaster;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gs.scm.Constants;
import gs.scm.JcoConnection;
import gs.scm.UTIL.DBUtil;
import gs.scm.test.TestJcoFunctionCalls;

@Controller
@RequestMapping(value = "/item", method = RequestMethod.GET)
public class ItemMasterAPI
{
	public CallableStatement cs = null;
	public Connection DBCon = null;
	protected TestJcoFunctionCalls test;	
	{			
		try {
			
			// 연결정보 세팅
			JcoConnection.createDestination();

			 List<Map<String, Object>> list =null;  //조회된 데이터를 담을 리스트
						
			// 서버 연결정보 세팅 
			JCoDestination jcoDestination = JCoDestinationManager.getDestination(Constants.DESTINATION_NAME);

			// ping test 다시 실행
			System.out.println("Pinging " + jcoDestination.getDestinationID() + " ...");
			jcoDestination.ping();
			System.out.println("Ping ok");			
			
			// 연결할 티코드 담기
			JCoFunction function = jcoDestination.getRepository().getFunction("Z_PESCM_IF_ITEM_MST");

			// Import 파라미터 전달
			function.getImportParameterList().setValue("I_WERKS", "1600");

			System.out.println("Calling Z_PESCM_IF_ITEM_MST");

			// 연결을 통해서 Import 실행
			function.execute(jcoDestination);

			// 연결 이후 Export 받을 테이블 전달 function에서 테이블 호출
			//JCoParameterList exports = function.getExportParameterList();
			JCoTable table = function.getTableParameterList().getTable("ET_DATA");

			list = new ArrayList<Map<String, Object>>();

			System.out.println("행 숫자 : " + table.getNumRows());
			// 루프 돌면서 데이터 조회
			for( int i=0; i < table.getNumRows(); i++)
			{
			  Map<String, Object> map = new HashMap<String, Object>();
			  
			  map.put("", table.getString("IF_BASE_DATE"));
			  map.put("", table.getString("PLNT_CD"));
			  map.put("", table.getString("ITEM_CD"));
			  map.put("", table.getString("PLNT_NM"));
			  map.put("", table.getString("ITEM_NM"));
			  map.put("", table.getString("ITEM_TYPE_CD"));
			  map.put("", table.getString("ITEM_TYPE_NM"));
			  map.put("", table.getString("ITEM_DIV_CD"));
			  map.put("", table.getString("ITEM_DIV_NM"));
			  map.put("", table.getString("ITEM_GRP_CD"));
			  map.put("", table.getString("ITEM_GRP_NM"));
			  map.put("", table.getString("ITEM_LVL_1_CD"));
			  map.put("", table.getString("ITEM_LVL_1_NM"));
			  map.put("", table.getString("GRADE_CD"));
			  map.put("", table.getString("GRADE_NM"));
			  map.put("", table.getString("GRADE_TYPE_CD"));
			  map.put("", table.getString("GRADE_TYPE_NM"));
			  map.put("", table.getString("PACK_TYPE_CD"));
			  map.put("", table.getString("PACK_TYPE_NM"));
			  map.put("", table.getString("PRDT_CYCLE"));
			  map.put("", table.getString("ITEM_UNIT"));
			  map.put("", table.getString("PACK_UNIT"));
			  map.put("", table.getString("PACK_STND"));
			  map.put("", table.getString("OG_YN"));
			  map.put("", table.getString("USE_YN"));
			  map.put("", table.getString("ATTRBT_1"));
			  map.put("", table.getString("ATTRBT_2"));
			  map.put("", table.getString("ATTRBT_3"));
			  map.put("", table.getString("ATTRBT_4"));
			  map.put("", table.getString("ATTRBT_5"));
			  map.put("", table.getString("CRTN_USER_ID"));
			  map.put("", table.getString("CRTN_DTTM"));
			  map.put("", table.getString("MDFY_USER_ID"));
			  map.put("", table.getString("MDFY_DTTM"));
			  map.put("", table.getString("IF_STUS_TYPE"));

			  // 리스트에 추가
			  list.add(map);

			}

			// DB 연결, 쿼리 준비
			// DBUtil db = new DBUtil();
			// String query = null;

			// // 디비 준비상태
			// db.ps = db.DBCon.prepareStatement(query);

			// Export 데이터 ItemMaster 객체로 전달
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
}