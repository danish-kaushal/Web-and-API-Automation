package com.systemone.utils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UITestSeleniumHelper {

	static Logger logger = LoggerFactory.getLogger(UITestSeleniumHelper.class);
	public WebDriver driver = null;
	public static Duration TIMEOUT = Duration.ofSeconds(60);

	/**
	 * Open the new browser window by creating the web driver
	 */
	public WebDriver openBrowser() {
		driver = new ChromeDriver();
		return driver;
	}

	/**
	 * To Maximize the browser
	 */
	public void maximizeWindow() {
		logger.info("Maximize window...");
		driver.manage().window().maximize();		
	}

	/**
	 * Close the current browser tab/window
	 */
	public void closeBrowser() {
		logger.info("Closing browser...");
		driver.close();
	}

	/**
	 * Goto a particular url
	 * 
	 * @param url
	 */
	public void navigateTo(String url) {
		logger.info("Going to url - " + url);
		driver.navigate().to(url);
	}

	/**
	 * Wait for Web Element to be loaded
	 * 
	 * @param xpath
	 */
	public void waitForElementToBeLoaded(String xpath) {
		By locator = By.xpath(xpath);
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

	}

	/**
	 * To find the element by Xpath
	 * 
	 * @param xpath
	 */
	public WebElement findElementByXPath(String xpath) {
		logger.info("Find an element by xpath: " + xpath);
		waitForElementToBeLoaded(xpath);
		return driver.findElement(By.xpath(xpath));
	}

	/**
	 * Wait for Web Element to be loaded
	 * 
	 * @param linktext
	 */
	public void waitForElementToBeLoadedByLinkText(String linkText) {
		By locator = By.linkText(linkText);
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * To find the element by link text i.e anchor tag
	 * 
	 * @param linktext
	 */
	public WebElement findElementByLinkText(String linkText) {
		logger.info("Find elements by linkText: " + linkText);
		waitForElementToBeLoadedByLinkText(linkText);
		return driver.findElement(By.linkText(linkText));
	}

	/**
	 * To find the elements by xpath
	 * 
	 * @param common xpath of elements
	 */
	public List<WebElement> findElementsByXPath(String xpath) {
		logger.info("Find elements by xpath: " + xpath);
		waitForElementToBeLoaded(xpath);
		return driver.findElements(By.xpath(xpath));
	}

	/**
	 * To wait until the javaScript loads
	 */
	public void waitUntilJSReady() {
		logger.info("Executing " + new Throwable().getStackTrace()[0].getMethodName());

		new WebDriverWait(driver, TIMEOUT).until(
				webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
	}
	
	/**
	 * To wait until the jQuery loads
	 */
	public void waitUntilJQueryReady() {
		logger.info(new Throwable().getStackTrace()[0].getMethodName());
		
		new WebDriverWait(driver, TIMEOUT).until(
			      webDriver -> ((JavascriptExecutor) webDriver).executeScript("return jQuery.active == 0").equals(true));
	}
	
	/**
	 * Wait for some given time secs.
	 * 
	 * @param secs
	 */
	public void waitSimply(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * To check if the page has been refreshed
	 */
	public boolean checkUserIsRegistered(String title){
		
		String beforeRegister = title;
		
		String afterRegister = pageTitle();
		
		if(afterRegister.equals(beforeRegister)) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * To scroll the page down
	 */
	public void scrollPageDown() {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scrollBy(0,250)", "");
	}
	
	/**
	 * To scroll the page down
	 * Param pixels
	 */
	public void scrollPageDown(int numPixels) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scrollBy(0,"+numPixels+")", "");
		waitSimply(1);
	}
	
	/**
	 *Scroll into view the element
	 */
	public void scrollIntoView(WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView({block: \"center\"});", element);
		waitSimply(1);
	}

	/**
	 * To get the page title
	 */
	public String pageTitle() {
		return driver.getTitle();
	}
	
	/**
	 * Get the current date
	 */
	public String date() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        System.out.println("Formatted Date : " + formattedDate);
		return formattedDate;	
	}

	/**
	 * Take screenshot
	 * 
	 * @param filePath
	 */
	public void takeScreenshot(String filePath) {
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		if (screenshot == null) {
			logger.warn("Can't take the screenshot!");
			return;
		}
		File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
		File destFile = new File(filePath);
		try {
			FileUtils.copyFile(srcFile, destFile);
			org.testng.Reporter.log(destFile.getCanonicalPath(), true);
		} catch (IOException ioe) {
			logger.warn("Exception while creating the snapshot file!"
					+ ioe.getMessage());
			ioe.printStackTrace();
		}

	}

}
