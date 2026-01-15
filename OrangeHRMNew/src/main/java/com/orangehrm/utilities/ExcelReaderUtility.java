package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;


public class ExcelReaderUtility {

	public static List<String[]> getSheetdata(String filePath, String sheetName) {
		List<String[]> data = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Sheet " + sheetName + " does not exist in " + filePath);
			}
			// iterate through rows
			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue; // skip header row
				}
				List<String> rowData = new ArrayList<>();
				for (Cell cell : row) {
					rowData.add(getcellValue(cell));
				}
				// convert rowdata to array and add to data list
				data.add(rowData.toArray(new String[0]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}

	private static String getcellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			}
			return String.valueOf((long) cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return "";
		}

	}
}
