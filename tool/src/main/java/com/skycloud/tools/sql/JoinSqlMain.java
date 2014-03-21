package com.skycloud.tools.sql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JoinSqlMain {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		InputStream fis = JoinSqlMain.class.getResourceAsStream("/sql001.sql");
		BufferedReader brReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

		StringBuffer sBuffer = new StringBuffer();
		StringBuffer sBuffer2 = new StringBuffer();
		String line = null;
		sBuffer.append("String sql = \"\" +\r\n");
		sBuffer2.append("// ");
		while ((line = brReader.readLine()) != null) {
			sBuffer.append("\"").append(line).append(" \" +").append("\r\n");
			sBuffer2.append(line.trim()).append(" ");
		}
		sBuffer.delete(sBuffer.length() - 4, sBuffer.length());
		sBuffer.append(";");

		System.out.println(sBuffer2.toString() + "\r\n" + sBuffer.toString());
	}

}
