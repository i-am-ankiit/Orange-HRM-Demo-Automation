package com.orangehrm.listeners;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class TestListener implements org.testng.ITestListener {
	
	@Override
	public void onTestStart(org.testng.ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.startTest(testName, "Execution of " + testName);
		ExtentManager.logStep("Starting test: " + testName);
		System.out.println("Test Started: " + testName);
	}
//Trigger when a test succeeds
	@Override
	public void onTestSuccess(org.testng.ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logPass(BaseClass.getDriver(), "Test Passed: " + testName);
		
		System.out.println("Test Passed: " + result.getName());
	}
//Trigger when a test fails
	@Override
	public void onTestFailure(org.testng.ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failure = result.getThrowable().getMessage();
		ExtentManager.logStep("Test Failed: " + testName + " - " + failure);
		ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed: " + testName);
	}
//Trigger when a test is skipped
	@Override
	public void onTestSkipped(org.testng.ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped: " + testName);
		System.out.println("Test Skipped: " + result.getName());
	}

	@Override
	public void onStart(org.testng.ITestContext context) {
		ExtentManager.getReporter();	 // Initialize ExtentReports
		System.out.println("Test Suite Started: " + context.getName());
	}

	@Override
	public void onFinish(org.testng.ITestContext context) {
		// Flush ExtentReports
		ExtentManager.endTest();
		System.out.println("Test Suite Finished: " + context.getName());
	}

}
//added by user
