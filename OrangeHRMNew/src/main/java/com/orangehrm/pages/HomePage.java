package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actionDriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	
	private ActionDriver actionDriver;
	
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By OrangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");
	
//	//initialize ActionDriver in constructor
//	public HomePage(WebDriver driver) throws Exception {
//		this.actionDriver = new ActionDriver(driver);
//	}
		//initialize ActionDriver in constructor
	public HomePage(WebDriver driver) throws Exception {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	
	//method to click on Admin tab
	public boolean isAdminTabVisible() throws Exception {
		return actionDriver.isElementDisplayed(adminTab);
	}
	
	public boolean isOrangeHRMLogoVisible() throws Exception {
		return actionDriver.isElementDisplayed(OrangeHRMLogo);
	}
	
	//method to logout
	public void logout() throws Exception {
		actionDriver.click(userIDButton);
		actionDriver.click(logoutButton);
	}
}
