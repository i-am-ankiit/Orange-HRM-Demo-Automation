package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	private static final String EXCEL_FILE_PATH = System.getProperty("user.dir")
			+ "/src/test/resources/testData/TestData.xlsx";
	@DataProvider(name = "validLoginData")
	public static Object[][] validLoginData() {
		return getSheetData("ValidLoginData");
	}
	
	@DataProvider(name = "invalidLoginData")
	public static Object[][] invalidLoginData() {
		return getSheetData("InvalidLoginData");
	}
	
	private static Object[][] getSheetData(String sheetName) {
		List<String[]> sheetdata =  ExcelReaderUtility.getSheetdata(EXCEL_FILE_PATH, sheetName);
		
		Object[][] data = new Object[sheetdata.size()][sheetdata.get(0).length];
		for (int i = 0; i < sheetdata.size(); i++) {
			data[i] = sheetdata.get(i);
		}
		return data;
	}

}
