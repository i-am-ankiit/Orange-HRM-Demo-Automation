package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actionDriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	// new changes as per thread safe
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src\\main\\resources\\config.properties");
		prop.load(fis);
		logger.info("Configuration properties loaded successfully.");
		
		//start the extent report
		ExtentManager.getReporter();
	}

	@BeforeMethod
	public void setup() throws IOException, Exception {
		System.out.println("Setting up the test environment...:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait();

		logger.info("Test environment setup completed.");
		logger.trace("Trace: Browser launched and configured.");
		logger.error("Error: Sample error log for testing purposes.");
		logger.debug("Debug: Setup method executed.");
		logger.fatal("Fatal: Sample fatal log for testing purposes.");
		logger.warn("Warn: Sample warning log for testing purposes.");

		// initialize ActionDriver only once
//		if(actionDriver == null) {
//			actionDriver = new ActionDriver(driver);
//			logger.info("ActionDriver initialized in BaseClass."+Thread.currentThread().getName());
//		}
//	}
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized in BaseClass." + Thread.currentThread().getId());
	}

	private void launchBrowser() {
		String browser = prop.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			// setup chrome driver
			// driver = new ChromeDriver(); new changes as per thread safe
			driver.set(new ChromeDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("Chrome browser launched.");
		} else if (browser.equalsIgnoreCase("firefox")) {
			// setup firefox driver
			// driver = new FirefoxDriver(); new changes as per thread safe
			driver.set(new FirefoxDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("Firefox browser launched.");
		} else if (browser.equalsIgnoreCase("edge")) {
			// setup edge driver
			// driver = new EdgeDriver();
			driver.set(new EdgeDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("Edge browser launched.");
		} else {
			System.out.println("Please select a valid browser");
		}

	}

	private void configureBrowser() {
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(implicitWait));

		getDriver().manage().window().maximize();

		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to navigate to the URL: " + e.getMessage());
		}

	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Unable to close the browser: " + e.getMessage());
			}
		}
		logger.info("test environment teardown");
		driver.remove(); // Remove the WebDriver instance for the current thread
		actionDriver.remove(); // Remove the ActionDriver instance for the current thread
		// driver = null; // Reset driver to null after quitting
		// actionDriver = null; // Reset actionDriver to null after quitting
		ExtentManager.endTest();
	}

	// Driver getter method
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("Driver is not initialized.");
			throw new IllegalStateException(
					"WebDriver has not been initialized. Call setup() before using getDriver().");
		}
		return driver.get();
	}

	// Driver setter method
	public void setDriver(WebDriver driver) {
		this.driver.set(driver);
	}

	// getter method for ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized.");
			throw new IllegalStateException(
					"ActionDriver has not been initialized. Call setup() before using getActionDriver().");
		}
		return actionDriver.get();
	}

	public void staticWait() {
		LockSupport.parkNanos(5000000000L); // 5 seconds
	}

	// getter for properties
	public static Properties getProp() {
		return prop;
	}

}
