package q4WebTest;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import functions.Q4WebFunctions;

public class RssFeeds extends Q4WebFunctions {
	private static List<String> sActualNot = new ArrayList();
	public static boolean bCheckLocal = false;
	private static String sActual = null, sExpected = null;
	private static WebDriver driver;
	private static List<String[]> lHrefRss = new ArrayList();
	private static String sByHrefRss, sByTitle, sByFeedContent;

	@BeforeMethod
	public static void beforeMethod() {
		sByHrefRss = propUI.getProperty("Elm_Smoke_ByHrefRss");
		sByTitle = propUI.getProperty("Elm_Smoke_ByTitleRss");
		sByFeedContent = propUI.getProperty("Elm_Smoke_ByFeedContent");
	}
	
	@Test
	public static void getCheck(String ID, WebDriver driver, String[] spltExpectedSecond, List<String> sActuals, boolean bCheck) throws InterruptedException {			
		
		beforeMethod();
		
		sActualNot = sActuals;
		bCheckLocal = bCheck;

	    //driver.get("http://www.bellushealth.com/English/investors/RSS-Feeds/default.aspx");
	    //Thread.sleep(2000);
	    
	    List<WebElement> webElementsHrefRss = driver.findElements(By.cssSelector(sByHrefRss));
	    
        for (WebElement webElementHrefRss:webElementsHrefRss) {
        	if (!webElementHrefRss.getAttribute("text").trim().equals("")) {
        		Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> <strong>" + webElementHrefRss.getAttribute("text").trim() + "</strong> - " + webElementHrefRss.getAttribute("href") + "<br>", true);
            	//System.out.println(webElementHrefRss.getAttribute("text") + " - " + webElementHrefRss.getAttribute("href"));
            	String temp[] = {webElementHrefRss.getAttribute("text").trim(), webElementHrefRss.getAttribute("href")};
            	lHrefRss.add(temp);        		
        	}
        }
        
        
        for (int n=0; n<lHrefRss.size(); n++) {
        	driver.navigate().to(lHrefRss.get(n)[1]);
        	Thread.sleep(2000);
        	
        	Reporter.log("&nbsp;&nbsp;&nbsp;<strong>" + lHrefRss.get(n)[0] + ":</strong><br>", true);
        			
        	// Try to find Feed Title
        	try {
            	WebElement webElementsRssTitle = driver.findElement(By.cssSelector(sByTitle));
            	Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> Title <strong>" + webElementsRssTitle.getAttribute("textContent") + "</strong> exists<br>", true);
            	//System.out.println(webElementsRssTitle.getAttribute("textContent"));        		
        	} catch (NoSuchElementException e) {
        		Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Feed Title wasn't found! - <strong>" + sByTitle +"</strong><br>", true);
        		sActualNot.add("No such element: <strong>" + sByTitle + "</strong>");
        	}
        	
        	// Try to find Length of Feed Content
        	try {
        		WebElement webElementsFeedContent = driver.findElement(By.cssSelector(sByFeedContent));
        		if (webElementsFeedContent.getAttribute("textContent").length() > 0) {
                	Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> Length of Feed Content: <strong>" + webElementsFeedContent.getAttribute("textContent").length() + "</strong><br>", true);        			
        		} else {
                	Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"blue\">Info:</font> Length of Feed Content: <strong>" + webElementsFeedContent.getAttribute("textContent").length() + "</strong><br>", true);
        		}       		
        	} catch (NoSuchElementException e) {
        		Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Length of Feed Content wasn't defined! - <strong>" + sByFeedContent +"</strong><br>", true);
        		sActualNot.add("Length of Feed Content wasn't defined: <strong>" + sByFeedContent + "</strong>");
        	}
        	
        	//WebElement webElementsFeedContent = driver.findElement(By.cssSelector(sByFeedContent));
        	//System.out.println("Length of content: " + webElementsFeedContent.getAttribute("textContent").length());
        	
        	driver.navigate().back();
        	Thread.sleep(2000);        	
        }
		
        
		if (sActualNot.size()==0) {
			sExpected = spltExpectedSecond[0];
			sActual = "available";	
			Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> RSS Feeds is available<br>", true);
		} else {
			sExpected = spltExpectedSecond[0];
			sActual = "not available";	
			Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> RSS Feeds is NOT available<br>", true);
			if (!Q4Web_BF_CompareValues(sActuals, "RSS Feeds is " + sActual)) {
				sActualNot.add("RSS Feeds is " + sActual);
			}
		}
	    
	}


	@AfterMethod
	public void afterMethod() {
		driver.quit();
	}
	
	
	public static String getExpected() {
		return sExpected;
	}
	
	public static String getActual() {
		return sActual;
	}
	
	public static List<String> getActualNot() {
		return sActualNot;
	}
	

}
