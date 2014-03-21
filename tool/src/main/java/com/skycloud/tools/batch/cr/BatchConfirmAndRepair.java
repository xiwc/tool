package com.skycloud.tools.batch.cr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 对触发器当前告警为OK状态的为确认和修复的告警,进行批量的确认.
 * @author xiweicheng
 *
 */
public class BatchConfirmAndRepair {
	
	// 驱动程序名
	static String driver = "com.mysql.jdbc.Driver";
	// URL指向要访问的数据库名
	static String url = "jdbc:mysql://192.168.35.76:3306/skycloud?characterEncoding=UTF-8";
	// MySQL配置时的用户名
	static String user = "skycloud";
	// MySQL配置时的密码
	static String password = "skycloud*123";
	// 连接
	static Connection conn;
	
	private BatchConfirmAndRepair(){}
	
	/**
	 * 程序执行入口.
	 */
	public static void Exec(String ipAndPort, String dbName, String userName, String pwd, ICallBack callBack) {
		
		url = "jdbc:mysql://" + ipAndPort + "/" + dbName + "?characterEncoding=UTF-8";
		user = userName;
		password = pwd;
		
		// 加载驱动程序
		try {
			Class.forName(driver);
			// 连续数据库
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sql = "" +
				"SELECT DISTINCT " +
				"	h.`host`, " +
				"	t.triggerid, " +
				"	t.`value` " +
				"FROM " +
				"	`triggers` t " +
				"INNER JOIN( " +
				"	SELECT " +
				"		MAX(e.eventid), " +
				"		e.objectid " +
				"	FROM " +
				"		`events` e " +
				"	WHERE " +
				"		e.object = 0 " +
				"	GROUP BY " +
				"		e.objectid " +
				")t1 ON t.triggerid = t1.objectid " +
				"INNER JOIN functions f ON f.triggerid = t.triggerid " +
				"INNER JOIN items i ON i.itemid = f.itemid " +
				"INNER JOIN `hosts` h ON h.hostid = i.hostid " +
				"INNER JOIN hosts_groups hg ON hg.hostid = h.hostid " +
				"INNER JOIN skycloud_groupstype_host sgh ON sgh.hostid = h.hostid " +
				"INNER JOIN skycloud_grouptype sg ON sg.typeid = sgh.typeid " +
				"WHERE " +
				"	t.`value` IN(0) " +
				"AND t.`status` = 0 " +
				"AND t.lastchange <> 0 " +
				"AND t.flags <> 2 " +
				"AND t.priority IN(2, 3, 4, 5) " +
				"AND i.`status` = 0 " +
				"AND h.`status` = 0 " +
				"AND NOT EXISTS( " +
				"	SELECT " +
				"		1 " +
				"	FROM " +
				"		skycloud_alarm_ok " +
				"	WHERE " +
				"		triggerid = t.triggerid " +
				"	AND confirm = 1 " +
				"	AND `repair` = 1 " +
				"	AND triggerid IN( " +
				"		SELECT " +
				"			triggerid " +
				"		FROM " +
				"			`triggers` " +
				"		WHERE " +
				"			`value` = 0 " +
				"	) " +
				") " +
				"AND NOT EXISTS( " +
				"	SELECT " +
				"		1 " +
				"	FROM " +
				"		skycloud_alarm_hidden " +
				"	WHERE " +
				"		triggerid = t.triggerid " +
				"); ";

		try {
			
			System.out.println("批量处理开始...");
			callBack.invoke(">>批量处理开始...");
			
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 结果集
			ResultSet rs = statement.executeQuery(sql);

			String triggerid = null;
			String host = null;
			
			int index = 1;

			while (rs.next()) {
				
				callBack.invoke("--当前处理第 " + (index++) + "条.");
				
				triggerid = rs.getString("triggerid");
				host = rs.getString("host");
				// 输出结果
				System.out.println("[hostname]" + host + " -> [triggerid]" + triggerid);
				callBack.invoke("[hostname]" + host + " -> [triggerid]" + triggerid);
				
				String sqlTmp = "";
				if(exists(triggerid)){
					sqlTmp = "UPDATE skycloud_alarm_ok SET confirm=1, repair=1 WHERE triggerid=" + triggerid;
				}else{
					sqlTmp = "INSERT INTO skycloud_alarm_ok (triggerid, confirm, repair) VALUES('" + triggerid + "', 1, 1)";
				}
				
				statement.addBatch(sqlTmp);
				
				System.out.println("[处理SQL]" + sqlTmp);
				callBack.invoke("[处理SQL]" + sqlTmp);
			}
			
			int[] size = statement.executeBatch();
			
			System.out.println(">>批量处理完了...");
			System.out.println("批量确认&修复[" + size.length + "]条告警.");
			
			callBack.invoke(">>批量处理完了...");
			callBack.invoke("==批量确认&修复[" + size.length + "]条告警.");

			rs.close();
			statement.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断数据是否已经存在.
	 * @param triggerid
	 * @return
	 * @throws SQLException
	 */
	private static boolean exists(String triggerid) throws SQLException {
		
		String sql = "SELECT * FROM skycloud_alarm_ok WHERE triggerid=" + triggerid;
		Statement statement = conn.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		
		boolean ret = resultSet.next();
		
		resultSet.close();
		statement.close();
		
		return ret;
		
	}
	
	static interface ICallBack{
		void invoke(String msg);
	}

}
