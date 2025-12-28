package com.orangehrm.actionDriver;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) throws Exception {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(explicitWait));
		System.out.println("ActionDriver initialized with explicit wait of " + explicitWait + " seconds.");
	}

	// method to click on element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyborder(by, "green"); // Apply green border to highlight the element
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked on element: " + elementDescription);
			logger.info("Clicked on element: " + elementDescription);
		} catch (Exception e) {
			applyborder(by, "red"); // Apply red border to indicate failure
			System.out.println("Exception while clicking on element: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Exception while clicking on element: " + elementDescription);
			logger.error("Exception while clicking on element: " + by.toString() + " - " + e.getMessage());
		}
	}

	// method to type text in input field
	public void type(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyborder(by, "green"); // Apply blue border to highlight the input field
//			driver.findElement(by).clear();
//			driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Typed value '" + value + "' in input field: " + getElementDescription(by));
		} catch (Exception e) {
			// TODO: handle exception
			applyborder(by, "red"); // Apply red border to indicate failure
			logger.error("Exception while typing in input field: " + e.getMessage());
		}
	}

	// method to get text from element
	public String getText(By by) {
		try {
			applyborder(by, "green"); // Apply green border to highlight the element
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyborder(by, "red"); // Apply red border to indicate failure
			logger.info("Exception while getting text from element: " + e.getMessage());
			return null;
		}

	}

	// Method to compare expected and actual text
	public boolean comparetext(By by, String expectedText) {
		try {
			applyborder(by, "green"); // Apply green border to highlight the element
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (actualText.equals(expectedText)) {
				logger.info("Text matches: " + actualText);
				ExtentManager.logPass(driver, "Text matches: " + actualText);
			} else {
				applyborder(by, "red"); // Apply red border to indicate failure
				logger.info("Text does not match. Expected: " + expectedText + ", Actual: " + actualText);
				ExtentManager.logFailure(driver, "Text does not match. Expected: " + expectedText + ", Actual: " + actualText);
				return false;
			}
		} catch (Exception e) {
			
			// TODO: handle exception
			logger.error("Exception while comparing text: " + e.getMessage());
			ExtentManager.logFailure(driver, "Exception while comparing text");
		}
		return false;
	}

	// method to check if element is displayed
	public boolean isElementDisplayed(By by) {
		try {
			applyborder(by, "green"); // Apply green border to highlight the element
			waitForElementToBeVisible(by);
			logger.info("Element is displayed: " + getElementDescription(by));
			ExtentManager.logStep("Element is displayed: " + getElementDescription(by));
			ExtentManager.logPass(driver, "Element is displayed: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyborder(by, "red"); // Apply red border to indicate failure
			logger.error("Element not displayed: " + e.getMessage());
			ExtentManager.logFailure(driver, "Element not displayed: " + getElementDescription(by));
			return false;
		}
	}

	// wait for element to be clickable
	public void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Exception while waiting for element to be clickable: " + e.getMessage());
		}
	}
	//wait for the page load
	public void waitForPageLoad(int timeoutInSeconds) {
		wait.withTimeout(java.time.Duration.ofSeconds(timeoutInSeconds)).until(WebDriver -> 
		    ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
		logger.info("Page loaded successfully");
				
	}
	//Scroll to element
	public void scrollToElement(By by) {
		try {
			applyborder(by, "blue"); // Apply blue border to highlight the element
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(by));
		} catch (Exception e) {
			applyborder(by, "red"); // Apply red border to indicate failure
			// TODO: handle exception
			logger.error("Exception while scrolling to element: " + e.getMessage());
		}
		
	}

	// wait for element to be visible
	public void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Exception while waiting for element to be visible: " + e.getMessage());
		}

	}
	//method to get the description of an element
	public String getElementDescription(By locator) {
	    //check for null driver
	    if (driver == null) 
	    	return "Driver is not initialized.";
	    	if(locator == null) 
	    		return "Locator is null.";
	    	
	try {    //find elemt
	    	WebElement element = driver.findElement(locator);
	    	//get element description
	    	String name = element.getDomAttribute("name");
	    	String id = element.getDomAttribute("id");
	    	String text = element.getText();
	    	String className = element.getDomAttribute("class");
	    	String placeholder = element.getDomAttribute("placeholder");
	    if(isNullOrEmpty(name)) {
	    		return "Element with name: " + name;
	    	}else if(isNullOrEmpty(id)) {
	    		return "Element with id: " + id;
	    	}
	    	else if(isNullOrEmpty(text)) {
	    		return "Element with text: " + truncateString(text, 30);
	    }
	    	else if(isNullOrEmpty(className)) {
	    		return "Element with class: " + className;
	    	}
	    	else if(isNullOrEmpty(placeholder)) {
	    		return "Element with placeholder: " + placeholder;
	    	}
	    	else {
	    		return "Element with tag name: " + element.getTagName();
	    	}
	    } catch (Exception e) {
	    		logger.error("Exception while getting element description: " + e.getMessage());
	    		return "Exception occurred while retrieving element description: " + e.getMessage();
	    }
	    	}	
	    	//return description based on element attributes
	    	
	    	
	    	//utility method for check not NULL or empty
	private boolean isNullOrEmpty(String str) {
	    return str != null && !str.isEmpty();
	}
	
	//utility method for truncate string
	public String truncateString(String str, int maxLength) {
	    if (str == null) {
	        return null;
	    }
	    if (str.length() <= maxLength) {
	        return str;
	    }
	    return str.substring(0, maxLength) + "...";
	}
	//Utility method to border an element for highlighting
	public void applyborder(By by, String color) {
		WebElement element = driver.findElement(by);
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.border='3px solid " + color + "'", driver.findElement(by));
			logger.info("Applied border to element: " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Exception while applying border to element: " + e.getMessage());
	}
	}
	}

