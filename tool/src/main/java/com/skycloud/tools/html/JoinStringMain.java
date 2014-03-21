package com.skycloud.tools.html;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JoinStringMain {

	private static final String FILE_PATH = "C:/xiwc/temp/JoinStringUse.txt";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		InputStream fis = new FileInputStream(FILE_PATH);
		BufferedReader brReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

		StringBuffer sBuffer = new StringBuffer();
		StringBuffer sBuffer2 = new StringBuffer();
		String line = null;
		sBuffer.append("var html = '' +\r\n");
		sBuffer2.append("// ");
		while ((line = brReader.readLine()) != null) {
			sBuffer.append("'").append(line).append("' +").append("\r\n");
			sBuffer2.append(line.trim());
		}
		sBuffer.delete(sBuffer.length() - 4, sBuffer.length());
		sBuffer.append(";");

		brReader.close();

		System.out.println(sBuffer2.toString() + "\r\n" + sBuffer.toString());
	}

}
