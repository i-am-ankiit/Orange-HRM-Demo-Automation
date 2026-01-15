package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.Loginpage;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {
	
	private Loginpage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setup() throws Exception {
		super.setup();  // MUST call this
		loginPage = new Loginpage(getDriver());
		homePage = new HomePage(getDriver());
	}
	@Test(dataProvider = "validLoginData", dataProviderClass = com.orangehrm.utilities.DataProviders.class)
	public void verifyOrangeHRMogo(String username, String password) throws Exception {
		//ExtentManager.startTest("verifyOrangeHRMogo", "Verify that the OrangeHRM logo is displayed on the home page after login.");
		ExtentManager.logStep("Logging in with valid credentials.");
		loginPage.login("Admin", "admin123");
		ExtentManager.logStep("Validating visibility of OrangeHRM logo on home page.");
		Assert.assertTrue(homePage.isOrangeHRMLogoVisible(), "OrangeHRM logo is not visible on the home page.");
		ExtentManager.logStep("OrangeHRM logo is visible on the home page.");
	}
	
	
	

}
