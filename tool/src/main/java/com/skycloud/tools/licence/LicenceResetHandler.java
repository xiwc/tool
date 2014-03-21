package com.skycloud.tools.licence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 * Licence reset 处理类.
 * 
 * @creation 2013-9-9 上午9:59:09
 * @modification 2013-9-9 上午9:59:09
 * @company Skycloud
 * @author xiweicheng
 * @version 1.0
 *
 */
public class LicenceResetHandler {
	
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

	/**
	 * 处理方法.
	 * 
	 * @param newLicence
	 */
	public void doReset(String newLicence) {

		initConnection();

		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("UPDATE skycloud_licence_web_used\n");
		sqlSb.append("SET content = ?\n");
		sqlSb.append("WHERE\n");
		sqlSb.append("	id = 1;\n");

		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(sqlSb.toString());
			ps.setString(1, newLicence);

			int cnt = ps.executeUpdate();

			if (cnt == 1) {
				JOptionPane.showMessageDialog(null, "Licence Reset Success...");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
