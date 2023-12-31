package com.systemone.ui.functional;

import static org.testng.Assert.assertEquals;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.systemone.utils.UITestSeleniumHelper;

public class WebPortalAutomation {
	Logger logger = LoggerFactory.getLogger(WebPortalAutomation.class.getName());
	UITestSeleniumHelper webAppHelper =  new UITestSeleniumHelper();
	
	public WebPortalAutomation(UITestSeleniumHelper helper) {
		this.webAppHelper = helper;
	}

	public void register(String f_Name, String l_Name, String email, String password, boolean newsletter_Subs) {
		logger.info("To Register a user " + new Throwable().getStackTrace()[0].getMethodName());	

		logger.info("To enter the values to input fields");

		// Declare and initialize a Map
		LinkedHashMap <String, String> elementId_ValueMap = new LinkedHashMap<>();

		// Add key-value pairs
		elementId_ValueMap.put("input-firstname", f_Name);
		elementId_ValueMap.put("input-lastname", l_Name);
		elementId_ValueMap.put("input-email", email);
		elementId_ValueMap.put("input-password", password);
		
		logger.info("Scroll into view the element");
		webAppHelper.scrollPageDown(300);

		// Iterate through the Map
		for (Map.Entry <String, String> entry : elementId_ValueMap.entrySet()) {

			logger.info("Enter the value to " + entry.getKey() + " field");

			String xpath = entry.getKey();
			String value = entry.getValue();

			System.out.println("xpath : " + xpath + "\n" + "value : " + value);

			WebElement input_Field =  webAppHelper.findElementByXPath("//input[@id = '" + xpath + "']");
			
			input_Field.click();
			input_Field.clear();
			input_Field.sendKeys(value);
			
			webAppHelper.waitSimply(1);
		}

		logger.info("To opt in to subscribe to the newsletter");

		WebElement element;

		if (newsletter_Subs) {
			logger.info("Click on Yes for the Newsletter field");
			element = webAppHelper.findElementByXPath("//label[@for = 'input-newsletter-yes']");

			logger.info("Scroll into view the element");
			webAppHelper.scrollIntoView(element);

			element.click();
		} 
		else {
			logger.info("click on No for the Newsletter field");
			element = webAppHelper.findElementByXPath("//label[@for = 'input-newsletter-no']");
			
			logger.info("Scroll into view the element");
			webAppHelper.scrollIntoView(element);

			element.click();
		}

		logger.info("Accept the privacy policy");
		webAppHelper.findElementByXPath("//input[@name = 'agree']").click();
		
		webAppHelper.waitSimply(1);
		
		logger.info("Click on the 'Continue' button");
		WebElement ele = webAppHelper.findElementByXPath("//button[text() = 'Continue']");
		
		Actions action = new Actions(webAppHelper.driver);
		
		// Move to the element and click
        action.moveToElement(ele).click().build().perform();
        
        webAppHelper.waitSimply(3);
	}
	
	public void contactFormValidation() {
		logger.info("To validate the form " + new Throwable().getStackTrace()[0].getMethodName());
		
        Map <String, List<String>> linkedHashMap = new LinkedHashMap<>();

        // Add key-value pairs with multiple values
        linkedHashMap.put("input-name", Arrays.asList("error-name", "Name must be between 3 and 32 characters!"));
        linkedHashMap.put("input-email", Arrays.asList("error-email", "E-Mail Address does not appear to be valid!"));
        linkedHashMap.put("input-enquiry", Arrays.asList("error-enquiry", "Enquiry must be between 10 and 3000 characters!"));

        // Iterate over the LinkedHashMap
        for (Map.Entry<String, List<String>> entry : linkedHashMap.entrySet()) {
        	logger.info("To assert the errored field " + entry.getKey());
        	
            String xpath = entry.getKey();
            List<String> values = entry.getValue();
            
            String id = values.get(0);
            String errorText = values.get(1);

            System.out.print("xpath : " + xpath + "\n" + "id : " + id + "\n" + "errorText : " + errorText);
            
            logger.info("To assert that errored field has red border");
            WebElement inputField = webAppHelper.findElementByXPath("//*[@id = '" + xpath + "']");
            
            assertEquals(inputField.getAttribute("class"), "form-control is-invalid");   
                
            logger.info("To assert that error text is shown");
            assertEquals(webAppHelper.findElementByXPath("//div[@id = '" + id + "']").getText(), errorText);
        }
    }
	
	public void fillContactForm(String name, String email, String enquiry) {
		logger.info("To fill up the contact form " + new Throwable().getStackTrace()[0].getMethodName());
		
		LinkedHashMap <String, String> elementId_ValueMap = new LinkedHashMap<>();

		elementId_ValueMap.put("input-name", name);
		elementId_ValueMap.put("input-email", email);
		elementId_ValueMap.put("input-enquiry", enquiry);

		// Iterate through the Map
		for (Map.Entry <String, String> entry : elementId_ValueMap.entrySet()) {

			logger.info("Enter the value to " + entry.getKey() + " field");

			String xpath = entry.getKey();
			String value = entry.getValue();

			System.out.println("xpath : " + xpath + "\n" + "value : " + value);

			WebElement input_Field =  webAppHelper.findElementByXPath("//*[@id = '" + xpath + "']");
			
			webAppHelper.scrollIntoView(input_Field);
			
			input_Field.click();
			input_Field.clear();
			input_Field.sendKeys(value);
			
			webAppHelper.waitSimply(1);
		}
		
		logger.info("Click on the submit button");
		webAppHelper.findElementByXPath("//button[text()= 'Submit']").click();
		
		webAppHelper.waitUntilJQueryReady();
		
		logger.info("To assert the success message");
		String successMessage = webAppHelper.findElementByXPath("//div[@id = 'common-success']//div[@id = 'content']/p").getText();
		System.out.println("successMessage : " + successMessage);
		
		assertEquals(successMessage, "Your enquiry has been successfully sent to the store owner!", 
				"Success message not received as expected");
	}	
}
