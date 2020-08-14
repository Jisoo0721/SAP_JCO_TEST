package gs.scm.UTIL;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtil {

	public Connection DBCon = null;
	Statement stmt = null;
	ResultSet rs = null;
	public PreparedStatement ps = null;
	public CallableStatement cs = null;

	public Connection DBOpen() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			InetAddress local;
			local = InetAddress.getLocalHost();
			String ip = local.getHostAddress();
			System.out.println("local ip : " + ip);
			//if (ip.equals("10.247.242.111") || ip.equals("10.247.246.14") ) { // 로컬, 개발서버인 경우
			if (ip.equals("172.24.32.61")) { // 로컬, 개발서버인 경우
				String connectionUrl = "jdbc:sqlserver://172.24.32.61:1433;"
						+ "databaseName=T3SMARTSCM;user=zionex;password=viazio2020!@#$;";
				DBCon = DriverManager.getConnection(connectionUrl); // 개발서버
			}
			else if (ip.equals("localhost"))
			{
				String connectionUrl = "jdbc:sqlserver://127.0.0.1:1433;"
						+ "databaseName=T3SMARTSCM;user=sa;password=viazio;";
				DBCon = DriverManager.getConnection(connectionUrl); // 개발서버				
			}
			// else if (ip.equals("10.247.241.14") ) { // 운영 서버인 경우
			// 	String connectionUrl = "jdbc:sqlserver://10.247.243.200:1433;"
			// 			+ "databaseName=T3SMARTSCM;user=zionex;password=zio!@#$2019;";
			// 	DBCon = DriverManager.getConnection(connectionUrl);
			// } 
			return DBCon;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Get Db Select
	 * 
	 * @param query
	 * @return
	 */
	public ResultSet GetSelect(String query) {
		try {
			Statement lstmt = DBCon.createStatement();
			return lstmt.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String GetRsDataString(ResultSet lrs, String colName) {
		try {
			return lrs.getString(colName) == null ? "" : lrs.getString(colName);
		} catch (SQLException e) {
			return "";
		}
	}

	/**
	 * 로그용 아이디 생성
	 * 
	 * @return
	 */
	public String CreateLogID() {
		String id = null;
		try {
			stmt = DBCon.createStatement();
			rs = stmt.executeQuery("SELECT REPLACE(NEWID(),'-','') as ID");

			while (rs.next()) {
				id = rs.getString("ID");
			}
			rs.close();
			stmt.close();

			if (id == null)
				return null;
			else
				return id;
		} catch (Exception e) {
			return null;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 현재 시간 가져옴
	 * 
	 * @return
	 */
	public Date GetSysDate() {
		Date sysDate = null;
		try {
			stmt = DBCon.createStatement();
			rs = stmt.executeQuery("SELECT GETDATE() AS GetSysDate");

			while (rs.next()) {
				sysDate = rs.getDate("GetSysDate");
			}
			rs.close();
			stmt.close();

			if (sysDate == null)
				return null;
			else
				return sysDate;
		} catch (Exception e) {
			return null;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * IF DB Log 동록
	 * 
	 * @param logid
	 * @param pgmid
	 * @param IF_ROLE_TYPE
	 * @param beginDate
	 * @param state
	 * @param totalCNT
	 * @param successCNT
	 * @param errCNT
	 * @param UserID
	 * @return
	 */
	public boolean SetLog(	String logid, String pgmid, String IF_ROLE_TYPE, String state,
							String IFID, String SDATE, String STIME, int totalCNT,
							int successCNT, int errCNT, Date START_DTTM
						) {
		// // IF 처리 전 BATCH Call
		// if(state.equals("P"))
		// this.Call_IF_BF_BATCH(state, pgmid, logid);
		//
		// boolean returnVal = false;
		// // 진행 중은 그대로 처리, 완료시에는 토탈 > 0, 에러 = 0 일 경우에만 성공으로 처리
		// state = state.equals("P") ? state : (errCNT == 0 && totalCNT > 0) ?
		// "S" : "E";

		boolean returnVal = false;

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));

		try {
			
			cs = DBCon.prepareCall("exec SP_IF_PGM_LOG ?, ?, ?, ?, ?, ?, ?, ?, ?, ?;");
			cs.setString(1, logid);
			cs.setString(2, pgmid);
			cs.setString(3, IF_ROLE_TYPE);
			cs.setString(4, state);
			cs.setString(5, IFID);
			cs.setString(6, SDATE);
			cs.setString(7, STIME);
			cs.setInt(8, totalCNT);
			cs.setInt(9, successCNT);
			cs.setInt(10, errCNT);
			
			cs.execute();

			returnVal = true;
		} catch (Exception e) {
			returnVal = false;
		} finally {
			try {
				cs.close();
			} catch (Exception e) {
			}
		}

		// IF 완료 로그 등록 후 BATCH Call
		if (state.equals("S") || state.equals("E"))
			this.Call_IF_AF_BATCH(state, pgmid, logid);

		return returnVal;
	}

	/**
	 * IF 처리 전 BATCH SP 호출
	 * 
	 * @param state
	 * @param pgmid
	 * @param logid
	 * @return
	 */
	public boolean Call_IF_BF_BATCH(String state, String pgmid, String logid) {
		try {
			// Log
			cs = DBCon.prepareCall("exec SP_IF_BF_BATCH(?, ?, ?); ");
			cs.setString(1, state); // state
			cs.setString(2, pgmid); // pgmid
			cs.setString(3, logid); // logid
			cs.execute();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				cs.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * IF 처리 후 BATCH SP 호출
	 * 
	 * @param state
	 * @param pgmid
	 * @param logid
	 * @return
	 */
	public boolean Call_IF_AF_BATCH(String state, String pgmid, String logid) {
		try {
			/*
			 * 20190509 임시 주석 - 프로시저 미생성 // Log cs =
			 * DBCon.prepareCall("begin SP_IF_AF_BATCH(?, ?, ?); end;");
			 * cs.setString(1, state); // state cs.setString(2, pgmid); // pgmid
			 * cs.setString(3, logid); // logid cs.execute();
			 */
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				cs.close();
			} catch (Exception e) {
			}
		}
	}

	//Exception Log 
	public boolean AddErrorLog(String logid, String pgmid, String err_msg) {
		try {
			// Log
			cs = DBCon.prepareCall("exec SP_IF_PGM_ERROR ?, ?, ?;");
			cs.setString(1, logid); // id
			cs.setString(2, pgmid); // pgm_id
			cs.setString(3, err_msg);
			cs.execute();

			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				cs.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Get BasePreDate
	 * 
	 * @return
	 */
	public String GetBasePreDate() {
		try {
			String baseDate = null;

			stmt = DBCon.createStatement();
			rs = stmt.executeQuery("SELECT CONVERT(NVARCHAR(50), DATEADD(DAY, -1, GETDATE()), 112) AS BASEDATE");

			while (rs.next()) {
				baseDate = rs.getString("BASEDATE");
			}
			rs.close();
			stmt.close();

			if (baseDate == null)
				return null;
			else
				return baseDate;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				cs.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Get GetBaseDateToday
	 * 
	 * @return
	 */
	public String GetBaseDateToday() {
		try {
			String baseDate = null;

			stmt = DBCon.createStatement();
			rs = stmt.executeQuery("SELECT CONVERT(NVARCHAR(50), GETDATE(), 112) AS BASEDATE");

			while (rs.next()) {
				baseDate = rs.getString("BASEDATE");
			}
			rs.close();
			stmt.close();

			if (baseDate == null)
				return null;
			else
				return baseDate;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				cs.close();
			} catch (Exception e) {
			}
		}
	}

	public void DBClose() {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cs != null) {
			try {
				cs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (DBCon != null) {
			try {
				if (!DBCon.getAutoCommit()) {
					DBCon.setAutoCommit(true);
				}
				DBCon.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

	/**
	 * Get PreMonth
	 * 
	 * @return
	 */
	public String GetPreMonth() {
		try {
			String baseDate = null;

			stmt = DBCon.createStatement();
			rs = stmt.executeQuery("SELECT CONVERT(NVARCHAR(6), DATEADD(DAY, -1, GETDATE()), 112) AS BASEDATE");

			while (rs.next()) {
				baseDate = rs.getString("BASEDATE");
			}
			rs.close();
			stmt.close();

			if (baseDate == null)
				return null;
			else
				return baseDate;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				cs.close();
			} catch (Exception e) {
			}
		}
	}

}