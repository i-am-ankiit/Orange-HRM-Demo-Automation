package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actionDriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class Loginpage {
	
	private ActionDriver actionDriver;
	
	//Define locators using By class
	private By usernameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[text()=\" Login \"]");
	private By errorMessage = By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");
	
//	public Loginpage(WebDriver driver) throws Exception {
//		this.actionDriver = new ActionDriver(driver);
//		
//	}
	//initialize ActionDriver in constructor
	public Loginpage(WebDriver driver) throws Exception {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void login(String username, String password) throws Exception {
		actionDriver.type(usernameField, username);
		actionDriver.type(passwordField, password);
		actionDriver.click(loginButton);
	}
	
	// Method to get error message text
	public boolean isErrorMessageDisplayed() throws Exception {
		return actionDriver.isElementDisplayed(errorMessage);
	}
	
	//method to get error message text
	public String getErrorMessageText() throws Exception {
		return actionDriver.getText(errorMessage);
	}
	
	//verify error message
	public boolean verifyErrorMessage(By expectedMessage) throws Exception {
	return actionDriver.comparetext(expectedMessage, getErrorMessageText());
		}
	}


