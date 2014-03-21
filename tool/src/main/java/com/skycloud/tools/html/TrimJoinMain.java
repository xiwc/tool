package com.skycloud.tools.html;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TrimJoinMain {

	private static final String FILE_PATH = "C:/xiwc/temp/JoinStringUse.txt";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		InputStream fis = new FileInputStream(FILE_PATH);
		BufferedReader brReader = new BufferedReader(new InputStreamReader(fis));

		StringBuffer sBuffer = new StringBuffer();
		String line = null;
		sBuffer.append("var html = '");
		while ((line = brReader.readLine()) != null) {
			sBuffer.append(line.trim());
		}
		sBuffer.append("';");

		brReader.close();

		System.out.println(sBuffer.toString());

	}

}
