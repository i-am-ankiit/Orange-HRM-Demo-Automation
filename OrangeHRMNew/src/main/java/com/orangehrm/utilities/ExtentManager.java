package com.orangehrm.utilities;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap = new HashMap<>();

    // ================= REPORT INIT =================
    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir")
                    + "/src/test/resources/ExtentReport/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("OrangeHRM Automation Test Report");
            spark.config().setDocumentTitle("OrangeHRM Test Report");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }

    // ================= TEST =================
    public synchronized static ExtentTest startTest(String testName, String description) {
        ExtentTest extentTest = getReporter().createTest(testName, description);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest endTest() {
        getReporter().flush();
        return null;
    }

    public static ExtentTest getCurrentTest() {
        return test.get();
    }

    public static String getCurrentTestName() {
        return test.get().getModel().getName();
    }

    // ================= LOGS =================
    public static void logStep(String logMessage) {
        getCurrentTest().info(logMessage);
    }

    public static void logPass(WebDriver driver, String message) {
        try {
            getCurrentTest().pass(
                    message,
                    MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(driver)).build()
            );
        } catch (Exception e) {
            getCurrentTest().pass(message);
        }
    }

    public static void logFailure(WebDriver driver, String logmessage) {
        String colorMessage =
                "<span style='color:red; font-weight:bold;'>" + logmessage + "</span>";

        getCurrentTest().fail(
                colorMessage,
                MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(driver)).build()
        );
    }

    public static void logSkip(String logMessage) {
        getCurrentTest().skip(logMessage);
    }

    // ================= SCREENSHOT =================
    public synchronized static String takeScreenshot(WebDriver driver) {

        String fileName = "SS_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".png";

        String screenshotDir =
                System.getProperty("user.dir")
                        + "/src/test/resources/ExtentReport/screenshots/";

        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            File dest = new File(screenshotDir + fileName);
            dest.getParentFile().mkdirs();
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 🔥 RETURN RELATIVE PATH (IMPORTANT)
        return "screenshots/" + fileName;
    }

    // ================= UNUSED BUT KEPT (NO BASE64 USED) =================
    public static String convertScreenshotToBase64(File screenshotFile) {
        return ""; // intentionally unused
    }

    public static void attachScreenshotToReport(WebDriver driver, String message) {
        try {
            getCurrentTest().info(
                    message,
                    MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(driver)).build()
            );
        } catch (Exception e) {
            getCurrentTest().info("Failed to attach screenshot");
        }
    }

    public static void registerDriver(WebDriver driver) {
        long threadId = Thread.currentThread().getId();
        driverMap.put(threadId, driver);
    }
}
