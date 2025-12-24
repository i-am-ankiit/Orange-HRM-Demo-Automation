package com.orangehrm.utilities;

import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
//import com.orangehrm.utilities.TakeScreenshotUtil;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {
	
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	//hashmap class can also be used instead of ThreadLocal
	private static Map<Long,WebDriver> driverMap = new HashMap<>();
	
	//initialize ExtentReports instance
	
	public synchronized static ExtentReports getReporter() {
		if(extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("OrangeHRM Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Test Report");
			spark.config().setTheme(Theme.DARK);	
			
			extent = new ExtentReports();
			extent.attachReporter(spark);
			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User", System.getProperty("user.name"));
	}
	
			
		return extent;
		
	}
	//start test method
	public synchronized static ExtentTest startTest(String testName, String description) {
		ExtentTest extentTest = getReporter().createTest(testName, description);
		test.set(extentTest);
		return extentTest;
	}
	//end test method
	public static ExtentTest endTest() {
		getReporter().flush();
		return null;
	}
// Get current  thread test
	public static ExtentTest getCurrentTest() {
		return test.get();
	}
	//
	//method to get the name of current test
	public static String getCurrentTestName() {
		return test.get().getModel().getName();
	}
	
	//log step method
	public static void logStep(String logMessage) {
		getCurrentTest().info(logMessage);
		
	}
	//log a step with screenshot
	public static void logStepWithScreenshot(String logMessage, String base64Screenshot) {
		getCurrentTest().info(logMessage).addScreenCaptureFromBase64String(base64Screenshot);
	}
	
	//log failure method
	public static void logFailure(WebDriver webDriver, String logMessage, String base64Screenshot) {
		String colormessage = String.format("<span style='color:red;font-weight:bold;'>%s</span>", logMessage);
		getCurrentTest().fail(colormessage).addScreenCaptureFromBase64String(base64Screenshot);
	}
	//skip test method
	public static void logSkip(String logMessage) {
		getCurrentTest().skip(logMessage);
	}
	//take ss with datetime stamp
	public synchronized static String takeScreenshot(WebDriver driver, String screenShotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
		//format datetime
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
		
		//Save screenshot to a file
		String despath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/screenshots/" + screenShotName + "_" + timestamp + ".png";
		File destination = new File(despath);
		destination.getParentFile().mkdirs(); //create directories if not exist
		try {
			FileUtils.copyFile(src, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//convert screenshot to base64
		String base64Screenshot = convertScreenshotToBase64(destination);
		return base64Screenshot;
		
	}
	//convert screenshot to base64
	public static String convertScreenshotToBase64(File screenshotFile) {
		String base64Format = "";
		
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return base64Format;
		
	}
	//Attach screenshot to test report using base64
	public static void attachScreenshotToReport(WebDriver driver,String message) {
		try {
			String base64Screenshot = takeScreenshot(driver, getCurrentTestName());
			getCurrentTest().info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
		} catch (Exception e) {
			getCurrentTest().info("Failed to attach screenshot: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void registerDriver(WebDriver driver) {
		long threadId = Thread.currentThread().getId();
		driverMap.put(threadId, driver);
	}
	

}
