package gs.scm.test;

import java.util.Date;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.monitor.JCoDestinationMonitor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gs.scm.Constants;
import gs.scm.JcoConnection;

@Controller
public class Monitoring {

	// protected TestJcoFunctionCalls test;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) throws JCoException {

		// 연결정보 세팅
		JcoConnection.createDestination();

		// 목적지 설정
		JCoDestination jcoDestination = JCoDestinationManager.getDestination(Constants.DESTINATION_NAME);
		// 설정부분 모니터링
		JCoDestinationMonitor monitor = jcoDestination.getMonitor();
		System.out.println("Peak Limit: " + monitor.getPeakLimit());
		System.out.println("Pool Capacity: " + monitor.getPoolCapacity());
		System.out.println("Max Used Count: " + monitor.getMaxUsedCount());
		System.out.println("Pooled Connection Count: " + monitor.getPooledConnectionCount());
		System.out.println("Used Connection Count: " + monitor.getUsedConnectionCount());
		System.out.println("Last Activity Timestamp: " + new Date(monitor.getLastActivityTimestamp()));

		// ping 테스트
		System.out.println("Pinging " + jcoDestination.getDestinationID() + " ...");
		jcoDestination.ping();
		System.out.println("Ping ok");

		model.addAttribute("text", "Peak Limit: " + monitor.getPeakLimit());
		return "home";
	}	
}
