package com.skycloud.tools.sql.alias;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * Alias 处理类.
 * 
 * @creation 2014年1月10日 下午3:24:34
 * @modification 2014年1月10日 下午3:24:34
 * @company Skycloud
 * @author xiweicheng
 * @version 1.0
 * 
 */
public class AliasHandlerMain {

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

	QueryRunner queryRunner = new QueryRunner();

	{
		initConnection();
	}

	/**
	 * 初始化数据源连接.
	 */
	private void initConnection() {

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
	}

	public static void main(String[] args) throws Exception {
		AliasHandlerMain main = new AliasHandlerMain();
		main.doWork();
	}

	private void doWork() throws SQLException {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT\n");
		sqlSb.append("	g.groupid,\n");
		sqlSb.append("	g.`name`,\n");
		sqlSb.append("	g.internal,\n");
		sqlSb.append("	sa.object,\n");
		sqlSb.append("	COUNT(hg.hostgroupid) AS cnt\n");
		sqlSb.append("FROM\n");
		sqlSb.append("	groups AS g\n");
		sqlSb.append("LEFT JOIN skycloud_alias AS sa ON g.groupid = sa.objectid\n");
		sqlSb.append("LEFT JOIN hosts_groups AS hg ON hg.groupid = g.groupid\n");
		sqlSb.append("WHERE\n");
		sqlSb.append("	sa.type = 0\n");
		sqlSb.append("AND g.internal = 0\n");
		sqlSb.append("GROUP BY\n");
		sqlSb.append("	g.groupid\n");


		List<Map<String, Object>> query = queryRunner.query(conn, sqlSb.toString(), new MapListHandler());

		for (Map<String, Object> map : query) {
			if (StringUtils.isEmpty((CharSequence) map.get("object"))) {
				System.out.println(map);
				doSave(getString(map, "groupid"), "0", getString(map, "name"));
			}
		}

		DbUtils.close(conn);
		
		System.out.println(UUID.randomUUID().toString());

	}

	static final String EMPTY_STRING = "";

	static String getString(Map<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return String.valueOf(map.get(key));
		} else {
			return EMPTY_STRING;
		}
	}

	void doSave(String objectid, String type, String object) throws SQLException {

		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("INSERT INTO skycloud_alias (\n");
		sqlSb.append("	objectid,\n");
		sqlSb.append("	type,\n");
		sqlSb.append("	object,\n");
		sqlSb.append("	departmentid,\n");
		sqlSb.append("	userid\n");
		sqlSb.append(")\n");
		sqlSb.append("VALUES\n");
		sqlSb.append("	(?,?,?,?,?)\n");

		queryRunner.update(conn, sqlSb.toString(), objectid, type, object, "0", "1");

	}
}
