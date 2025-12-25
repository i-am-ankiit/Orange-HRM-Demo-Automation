package com.orangeHRM.test;

import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.Loginpage;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass{

	private Loginpage loginPage;
	private HomePage homePage;
	@BeforeMethod
	
	public void setup() throws Exception {
	    super.setup();  // MUST call this

	    loginPage = new Loginpage(getDriver());
	    homePage = new HomePage(getDriver());
	}

	@Test
		public void testvalidLogin() throws Exception {
			//ExtentManager.startTest("testvalidLogin", "Verify that a user can log in with valid credentials.");
			ExtentManager.logStep("Navigating to Login Page.");
			loginPage.login("Admin", "admin123");
			ExtentManager.logStep("Logging in with valid credentials.");
			Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab is not visible, login might have failed.");
			ExtentManager.logStep("validation successful, Admin tab is visible.");
			homePage.logout();
			ExtentManager.logStep("Logged out successfully.");
			staticWait();
			
		}
	@Test
	public void invalidLogintest() throws Exception {
		//ExtentManager.startTest("invalidLogintest", "Verify that an error message is displayed for invalid login attempts.");
		ExtentManager.logStep("Navigating to Login Page.");
		loginPage.login("Admin", "admin");
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message is not displayed for invalid login.");
		ExtentManager.logStep("Error message is displayed for invalid login.");
		String actualErrorMessage = loginPage.getErrorMessageText();
		Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message text does not match expected.");
	}
		
	}
	
	

