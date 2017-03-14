package q4WebTest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import functions.Q4WebFunctions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class TickerTechChart extends Q4WebFunctions {
	public static boolean bCheckLocal = false;
	public static List<String> sActualNot = new ArrayList();
	public static String sActual = null, sExpected = null;
	private static int iResponseCodeOk, iResponseCodeChart;
	private static String sPathName, sPathBefore, sMonthClick, sQuarterClick, sYearClick, s5YearsClick, sByFrameChart, sByFrameServletChart;
	private static long iMonth, iQuarter, iYear, i5Years;
	
	
	public static void beforeMethod() {
		sPathName = "ScreenShots/Q4Web";
		sPathBefore = "../../../";
		iResponseCodeOk = 200;
		iResponseCodeChart = 30;
		sMonthClick = "0"; sQuarterClick = "1"; sYearClick = "2"; s5YearsClick = "3";
		iMonth = 0; iQuarter = 0; iYear = 0; i5Years = 0;
		
		sByFrameChart = propUI.getProperty("Pgs_Smoke_ByFrameChart");
		sByFrameServletChart = propUI.getProperty("Pgs_Smoke_ByFrameServletChart");
	}
	
	public static void getCheck(String ID, WebDriver driver, WebElement webElement, String[] spltExpectedSecond, List<String> sActuals, boolean bCheck, String CheckName) throws MalformedURLException, IOException, InterruptedException 
	{
		beforeMethod();
		
		sActualNot = sActuals;
		bCheckLocal = bCheck;
		
		for (int n=0; n<spltExpectedSecond.length; n++) {
			try {
				String sChartUrls = webElement.getAttribute("src");								
				
				Reporter.log("&nbsp;&nbsp;&nbsp;" + sChartUrls.split("\\?")[0] + "<br>", true);
				int contentLength = Q4Web_BF_GetContentLength(sChartUrls);
				
				if (contentLength > iResponseCodeChart) {
					sExpected = spltExpectedSecond[n];
					sActual = "available";	
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> " + CheckName + " iFrame is available<br>", true);
				} else {
					sExpected = spltExpectedSecond[n];
					sActual = "not available";	
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> " + CheckName + " iFrame is unavailable<br>", true);
					if (!Q4Web_BF_CompareValues(sActuals, CheckName + " iFrame is " + sActual)) {
						sActualNot.add(CheckName + " iFrame is " + sActual);
					}
				}
				
				driver.switchTo().frame(webElement);
				List<WebElement> iframeElements = driver.findElements(By.xpath(sByFrameChart));
				
				
				// Chart Default
				WebElement webChart = driver.findElement(By.xpath(sByFrameServletChart));
				int statusCodeChart = Q4Web_BF_GetResponseCode(webChart.getAttribute("src"));
				if (statusCodeChart==iResponseCodeOk) {
					sExpected = spltExpectedSecond[n];
					sActual = "available";	
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> Stock Chart is available<br>", true);														
				} else {
					sExpected = spltExpectedSecond[n];
					sActual = "not available";	
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Stock Chart is unavailable<br>", true);
					if (!Q4Web_BF_CompareValues(sActuals, "Stock Chart is " + sActual)) {
						sActualNot.add("Stock Chart " + sActual);
					}														
				}
				
				String sFileNameDefault = Q4Web_BF_HttpDownloader(webChart.getAttribute("src"), sPathName, ID+"ChartDefault.");
				if (!sFileNameDefault.equals("")) {
					File fDefault = new File(sPathName  + File.separator + sFileNameDefault);
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed: </font>Default Chart Size is <strong>" + fDefault.length() + "</strong> bytes<br>", true);
				} else {
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed: </font>Default Chart Size is not defined<br>", true);
				}
				
				JavascriptExecutor js = (JavascriptExecutor) driver;
				
				// Chart Month
				js.executeScript("CT_handleMouseClick(" + sMonthClick + ");");
				Thread.sleep(2000);
				String sFileNameMonth = Q4Web_BF_HttpDownloader(webChart.getAttribute("src"), sPathName, ID+"ChartMonth.");
				if (!sFileNameMonth.equals("")) {
					File fMonth = new File(sPathName  + File.separator + sFileNameMonth);
					iMonth = fMonth.length();
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed: </font>Month Chart Size is <strong>" + iMonth + "</strong> bytes<br>", true);
					Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Screenshot <a href=\"" + sPathBefore + sPathName  + File.separator + sFileNameMonth + "\">" + sFileNameMonth + "</a> captured<br>", true);
				} else {
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed: </font>Month Chart Size is not defined!<br>", true);
					sActual = "doesn't work properly";	
					if (!Q4Web_BF_CompareValues(sActuals, "Month Chart " + sActual)) {
						sActualNot.add("Month Chart " + sActual);
					}	
				}
				
				
				// Chart Quarter
				js.executeScript("CT_handleMouseClick(" + sQuarterClick + ");");
				Thread.sleep(2000);
				String sFileNameQuarter = Q4Web_BF_HttpDownloader(webChart.getAttribute("src"), sPathName, ID+"ChartQuarter.");
				if (!sFileNameQuarter.equals("")) {
					File fQuarter = new File(sPathName  + File.separator + sFileNameQuarter);
					iQuarter = fQuarter.length();
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed: </font>Quarter Chart Size is <strong>" + iQuarter + "</strong> bytes<br>", true);
					Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Screenshot <a href=\"" + sPathBefore + sPathName  + File.separator + sFileNameQuarter + "\">" + sFileNameQuarter + "</a> captured<br>", true);
				} else {
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed: </font>Quarter Chart Size is not defined!<br>", true);
					sActual = "doesn't work properly";	
					if (!Q4Web_BF_CompareValues(sActuals, "Quarter Chart " + sActual)) {
						sActualNot.add("Quarter Chart " + sActual);
					}	
				}	
				
				
				// Chart Year
				js.executeScript("CT_handleMouseClick(" + sYearClick + ");");
				Thread.sleep(2000);
				String sFileNameYear = Q4Web_BF_HttpDownloader(webChart.getAttribute("src"), sPathName, ID+"ChartYear.");
				if (!sFileNameYear.equals("")) {
					File fYear = new File(sPathName  + File.separator + sFileNameYear);
					iYear = fYear.length();
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed: </font>Year Chart Size is <strong>" + iYear + "</strong> bytes<br>", true);
					Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Screenshot <a href=\"" + sPathBefore + sPathName  + File.separator + sFileNameYear + "\">" + sFileNameYear + "</a> captured<br>", true);
				} else {
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed: </font>Year Chart Size is not defined!<br>", true);
					sActual = "doesn't work properly";	
					if (!Q4Web_BF_CompareValues(sActuals, "Year Chart " + sActual)) {
						sActualNot.add("Year Chart " + sActual);
					}
				}	
				
				// Chart 5 Years
				js.executeScript("CT_handleMouseClick(" + s5YearsClick + ");");
				Thread.sleep(2000);
				String sFileName5Years = Q4Web_BF_HttpDownloader(webChart.getAttribute("src"), sPathName, ID+"Chart5Years.");
				if (!sFileName5Years.equals("")) {
					File f5Years = new File(sPathName  + File.separator + sFileName5Years);
					i5Years = f5Years.length();
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed: </font>5 Years Chart Size is <strong>" + i5Years + "</strong> bytes<br>", true);
					Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Screenshot <a href=\"" + sPathBefore + sPathName  + File.separator + sFileName5Years + "\">" + sFileName5Years + "</a> captured<br>", true);
				} else {
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed: </font>5 Years Chart Size is not defined!<br>", true);
					sActual = "doesn't work properly";	
					if (!Q4Web_BF_CompareValues(sActuals, "5 Years Chart " + sActual)) {
						sActualNot.add("5 Years Chart " + sActual);
					}
				}	
				
				// Check if the chart changes
				if ( (iMonth!=iQuarter) && (iQuarter!=iYear) && (iYear!=i5Years) ) 
				{
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed: </font>TickerTech Chart works well<br>", true);
				} else {
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed: </font>TickerTech Chart doesn't work properly when you change the period of time!<br>", true);
					sActual = "doesn't work properly";	
					if (!Q4Web_BF_CompareValues(sActuals, "TickerTech Chart " + sActual)) {
						sActualNot.add("TickerTech Chart " + sActual);		
					}	
				}
				
				driver.switchTo().defaultContent();
				
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			} catch (NullPointerException ae) {
				ae.printStackTrace();
			} catch (WebDriverException wde) {
				wde.printStackTrace();
			}
			
		} // sCheckTypeIFrame	- Stock Chart
		
	}
	

}
