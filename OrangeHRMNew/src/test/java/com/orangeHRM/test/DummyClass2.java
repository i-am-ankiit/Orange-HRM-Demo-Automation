package com.orangeHRM.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {
	@Test
	public void dummyTest() {
		//ExtentManager.startTest("dummyTest", "This is a dummy test to verify the test setup.");
		String title = getDriver().getTitle();
		ExtentManager.logStep("Page title is: " + title);
		assert title.equals("OrangeHRM"): "Title does not match!";
		
		System.out.println("Dummy test executed successfully.");
		ExtentManager.logStep("Dummy test completed successfully.");
	}

}
