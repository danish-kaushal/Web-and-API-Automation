package com.systemone.functional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.systemone.ui.functional.WebPortalAutomation;
import com.systemone.utils.UITestSeleniumHelper;

public class WebPortalAutomationTests {
	UITestSeleniumHelper helper = new UITestSeleniumHelper();
	WebPortalAutomation webPortal = null;
	Logger logger = LoggerFactory.getLogger(WebPortalAutomationTests.class.getName());

	public static String WEBAPP_URL = System.getProperty("webuitest.url", "https://demo.opencart.com/");

	@BeforeClass
	public void launchbrowser() {
		helper.openBrowser();
		helper.maximizeWindow();
		helper.navigateTo(WEBAPP_URL);
		webPortal = new WebPortalAutomation(helper);
	}

	@Test(enabled = true, priority = 1)
	public void registerAccount() {
		logger.info("To Test " + new Throwable().getStackTrace()[0].getMethodName());

		logger.info("Click on 'My Account'");
		helper.findElementByLinkText("My Account").click();
		
		helper.waitSimply(1);
		
		logger.info("Click on 'Register' option under 'My Account' dropdown");
		helper.findElementByLinkText("Register").click();

		helper.waitUntilJSReady();

		logger.info("To get the page title");
		String title = helper.pageTitle();
		System.out.println("title : " + title);

		logger.info("Assert the page title");
		assertEquals(title, "Register Account");

		logger.info("To generate the fake data");

		Faker faker = new Faker();

		String firstName = faker.name().firstName();

		String lastName = faker.name().lastName();

		String email = faker.internet().safeEmailAddress();

		String password = faker.idNumber().valid();
		
		System.out.println("firstName : " + firstName + "\n" + "lastName : " + lastName + "\n" + "email : " + email + "\n" + 
				"password : " + password);

		logger.info("To enter details in the form");
		webPortal.register(firstName, lastName, email, password, true);

		logger.info("To check whether the user has registered in the system after filling out the form");
		boolean is_User_Registered = helper.checkUserIsRegistered(title);
		System.out.println("is_User_Registered : " + is_User_Registered);
		
		if(is_User_Registered) {
			String page_Source = helper.driver.getPageSource();
			
			assertTrue(page_Source.contains("created") || page_Source.contains("successfully"));
		}
		else {
			fail("Registration is unsuccessful");
		}
	}

	@Test(enabled = true, priority = 2)
	public void sortingFunctionality() {
		logger.info("To Test " + new Throwable().getStackTrace()[0].getMethodName());

		logger.info("Click on the desktops from navbar");
		helper.findElementByLinkText("Desktops").click();

		logger.info("Click on the 'Show All Desktops' under desktops");
		helper.findElementByLinkText("Show All Desktops").click();

		helper.waitUntilJSReady();
		
		logger.info("To get the title of the webpage");
		String pageTitle = helper.pageTitle();
		System.out.println("pageTitle : " + pageTitle);

		logger.info("Assert Page title");
		assertEquals(pageTitle, "Desktops");

		helper.scrollPageDown(400);

		logger.info("Select the items limit to 100");
		helper.findElementByXPath("//select[@id = 'input-limit']").click();

		logger.info("Set the limit to 100 from dropdown");
		helper.findElementByXPath("//option[text() = '100']").click();

		helper.waitUntilJSReady();

		logger.info("Fetching the price of all the products");

		ArrayList <Double> actualList = new ArrayList <Double>();

		for(WebElement ele: helper.findElementsByXPath("//span[@class = 'price-new']")) {
			actualList.add(Double.parseDouble((ele).getText().substring(1).replace(",", "")));
		}

		logger.info("The actual list from the web page before sorting : ");
		System.out.println(actualList);

		//descending order
		Collections.sort(actualList, Collections.reverseOrder());

		logger.info("The sorted list in Descending order with java collections class : ");
		System.out.println(actualList);

		logger.info("Click on the dropdown");
		helper.findElementByXPath("//select[@id = 'input-sort']").click();

		logger.info("Click on the 'Price (High > Low)' option from dropdown");
		helper.findElementByXPath("//option[text() = 'Price (High > Low)']").click();

		helper.waitUntilJSReady();

		logger.info("Getting the list of prices after applying sorting filter");

		ArrayList <Double> descList = new ArrayList <Double>();

		for(WebElement ele: helper.findElementsByXPath("//span[@class = 'price-new']")) {
			descList.add(Double.parseDouble((ele).getText().substring(1).replace(",", "")));
		}

		logger.info("The actual list from the web page after sorting : ");
		System.out.println(descList);

		logger.info("Asserting both arraylists are sorted in Descending order");
		assertTrue(actualList.equals(descList));
	}

	@Test(enabled = true, dependsOnMethods = {"sortingFunctionality"})
	public void contactForm() {
		logger.info("Test case execution " + new Throwable().getStackTrace()[0].getMethodName());
		
		logger.info("Go to the contact form page");
		helper.findElementByXPath("//i[@class= 'fas fa-phone']").click();
		
		helper.waitUntilJSReady();
		
		logger.info("Click on the submit button to validate the errored fields");
		WebElement submitButton = helper.findElementByXPath("//button[text()= 'Submit']");
		
		helper.scrollIntoView(submitButton);
		
		submitButton.click();
		
		helper.waitUntilJQueryReady();
		
		logger.info("To validate the contact form without putting any value");
		webPortal.contactFormValidation();
		
		logger.info("To validate the contact form after entering all required details");
		webPortal.fillContactForm("Danish Kaushal", "Testing@test.com", "Testing...");
	}

	@AfterClass
	public void closeBrowser() {
		helper.closeBrowser();	
	}

	@AfterMethod(alwaysRun = true)
	public void captureSummary(ITestResult result) throws HeadlessException, AWTException, IOException {
		String testName = result.getName();

		// Take screenshot
		if (!result.isSuccess()) {
			String filePath = "target" + File.separator + "screenshots" + File.separator + getClass().getName()
					+ File.separator + testName + ".png";
			logger.info("Taking screentshot..." + filePath);
			helper.takeScreenshot(filePath);
			logger.info(result.getThrowable().toString());
		}
	}
}
