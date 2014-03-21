package com.skycloud.tools.excel;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel文件解析.
 * 
 * @creation 2014年3月12日 上午11:09:15
 * @modification 2014年3月12日 上午11:09:15
 * @company Skycloud
 * @author xiweicheng
 * @version 1.0
 * 
 */
public class ExcelParser {

	private static Logger logger = Logger.getLogger(ExcelParser.class);

	public static void main(String[] args) {

		String dir = "C:/Users/thinkpad/Desktop/交大项目_2014-03-05/Excel";

		Collection<File> file = FileUtils.listFiles(FileUtils.getFile(dir), new String[] { "xlsx" }, false);

		for (File file2 : file) {

			System.out.println(file2.getAbsolutePath());

			List<Map<String, Object>> parse = parse(file2.getAbsolutePath());

			List<String> list = new ArrayList<>();

			for (Map<String, Object> map : parse) {
				list.add("'" + map.get("主要用途").toString() + "_" + map.get("IP").toString() + "'");
			}

			String join = StringUtils.join(list, ",");

			System.out.println(join);

			System.out.println();
		}
	}

	public static List<Map<String, Object>> parse(String path) {

		List<Map<String, Object>> mapList = new ArrayList<>();

		OPCPackage pkg = null;
		XSSFWorkbook book = null;
		try {
			pkg = OPCPackage.open(path);
			book = new XSSFWorkbook(pkg);

			// 得到第一页
			XSSFSheet sheet = book.getSheetAt(0);

			// 得到行数
			int rowCount = sheet.getLastRowNum();

			if (rowCount > 0) {// 如果excel表不为空，则开始解析
				// 得到列数
				int cellCount = sheet.getRow(0).getLastCellNum();

				// 解析标题
				XSSFRow head = sheet.getRow(0);
				Map<String, String> headerMap = new HashMap<>();

				for (int i = 0; i < cellCount; i++) {
					headerMap.put(String.valueOf(i), head.getCell(i).getStringCellValue());
				}

				// 解析行数据
				for (int j = 1; j <= rowCount; j++) {
					Map<String, Object> rowMap = new HashMap<>();

					XSSFRow rowValue = sheet.getRow(j);

					for (int k = 0; k < cellCount; k++) {
						String cellvalue = null;
						XSSFCell cell = rowValue.getCell(k);

						if (cell != null) {
							switch (cell.getCellType()) {
							case XSSFCell.CELL_TYPE_STRING:
								cellvalue = cell.getStringCellValue();
								break;
							case XSSFCell.CELL_TYPE_BLANK:
								cellvalue = null;
								break;
							case XSSFCell.CELL_TYPE_NUMERIC:
								if (HSSFDateUtil.isCellDateFormatted(cell)) {
									Date d = cell.getDateCellValue();
									DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
									cellvalue = formater.format(d);
								} else {
									double v = cell.getNumericCellValue();
									long v2 = (long) v;
									if (v2 == v) {
										cellvalue = String.valueOf(v2);
									} else {
										cellvalue = String.valueOf(v);
									}
								}
								break;
							case HSSFCell.CELL_TYPE_ERROR:
								cellvalue = null;
								break;
							case HSSFCell.CELL_TYPE_FORMULA:
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cellvalue = String.valueOf(cell.getNumericCellValue());
								break;
							case HSSFCell.CELL_TYPE_BOOLEAN:
								if ("是".equals(cell.getStringCellValue())) {
									cellvalue = "1";
								} else {
									cellvalue = "0";
								}
								break;
							default:
								cellvalue = null;
								break;
							}
						}
						rowMap.put(headerMap.get(String.valueOf(k)), cellvalue);
					}
					mapList.add(rowMap);
				}
			}
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if (pkg != null) {
				try {
					pkg.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
			}
		}

		return mapList;

	}
}
