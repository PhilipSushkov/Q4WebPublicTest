package q4WebTest;

import org.openqa.selenium.*;
import org.testng.annotations.*;

import functions.Q4WebFunctions;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.Reporter;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;

public class ThreadLocalDemo extends Q4WebFunctions {
	private static final String SEARCH_TERMS = "search-terms";
	private static final int THREADS = 5;
	private static final String sSourceFile = "file", sSourceExcel = "excel", sAppearanceHidden = "hidden", sAppearanceOpen = "open", sNetworkErrorsShow = "show";
	private String[][] sitesList;
	private static String sSpliterFirst, sSpliterSecond, sSlash, sHttp, sImp, sNotImp;
	private static String sCheckTypeContains, sCheckTypeEquals, sCheckTypePdf, sCheckTypeIFrame, sCheckTypeQ4Chart, sCheckTypeRssFeeds;
	private static String sByTagName, sByClassName, sByXpath, sByCssSelector;
	private static int pageLoadTimeout;
	private static BrowserMobProxy proxyLoc;

	@BeforeMethod
 	public void beforeMethod() {
		sSpliterFirst = "!";
		sSpliterSecond = ";";
		sSlash = "/";
		sHttp = "http://";
		sImp = "1"; sNotImp = "0";
		
		pageLoadTimeout = Integer.valueOf(propConf.getProperty("pageLoadTimeout"));
		
		sCheckTypeContains = propUI.getProperty("Pgs_Smoke_CheckTypeContains");
		sCheckTypeEquals = propUI.getProperty("Pgs_Smoke_CheckTypeEquals");
		sCheckTypePdf = propUI.getProperty("Pgs_Smoke_CheckTypePdf");
		sCheckTypeIFrame = propUI.getProperty("Pgs_Smoke_CheckTypeIFrame");
		sCheckTypeQ4Chart = propUI.getProperty("Pgs_Smoke_CheckTypeQ4Chart");
		sCheckTypeRssFeeds = propUI.getProperty("Pgs_Smoke_CheckTypeRSSFeeds");
		
		sByTagName = propUI.getProperty("Pgs_Smoke_ByTagName");
		sByClassName = propUI.getProperty("Pgs_Smoke_ByClassName");
		sByXpath = propUI.getProperty("Pgs_Smoke_ByXpath");
		sByCssSelector = propUI.getProperty("Pgs_Smoke_ByCssSelector");
	}
  
	@Test (dataProvider = SEARCH_TERMS, threadPoolSize = THREADS)
    public void Q4Web_SmokeMigrationTest(String ID, String searchTerm) throws InterruptedException, FileNotFoundException, IOException {
		
		if (propConf.getProperty("NetworkErrors").equals(sNetworkErrorsShow)) {
			proxyLoc = new BrowserMobProxyServer();
			proxyLoc = LocalDriverFactory.proxy;
			//System.out.println("!!!" + LocalDriverFactory.proxy.getPort());
			//System.out.println("proxyLoc: " + proxyLoc.getPort());			
		}

		WebDriver driver = LocalDriverManager.getDriver();

		//driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
		searchTerm = Q4Web_BF_UrlAddSlash(searchTerm, sSlash, sHttp);
		
		try {
			//driver.manage().window().setSize(new Dimension(0, 0));
			invokeBrowser(searchTerm);
			Thread.sleep(2000);	
		} catch (TimeoutException e) {
			driver.findElement(By.tagName("body")).sendKeys("Keys.ESCAPE");
		}

	    // Check if the web-site opens
		openHomePage(searchTerm, ID);

		/*
		
		try {
			invokeBrowser(searchTerm + propUI.getProperty("Pgs_Smoke_SiteMap"));
			Thread.sleep(2000);			
		} catch (TimeoutException e) {
			driver.findElement(By.tagName("body")).sendKeys("Keys.ESCAPE");
		}
		
	
		// Build Urls List
		List<String> urlsList = Q4Web_BF_BuildUrlsList(driver, By.tagName(propUI.getProperty("Pgs_Smoke_SiteUrls")));
		
		try {
			invokeBrowser(searchTerm);
			Thread.sleep(2000);			
		} catch (TimeoutException e) {
			driver.findElement(By.tagName("body")).sendKeys("Keys.ESCAPE");
		}
		
		// Add additional Urls to List
		urlsList = Q4Web_BF_AddUrlsList(driver, By.xpath(propUI.getProperty("Url_Smoke_CheckHomeUrlsAll")), urlsList, propUI.getProperty("Url_Smoke_CheckHomeUrlsRegEx"));
			
		
		// Build Pages List for testing
		String[][] pagesList = createTestPagesList(new String [] {"Pgs_Smoke_ID", "Pgs_Smoke_PageLink", "Pgs_Smoke_PageCheckName", "Pgs_Smoke_PageParam", "Pgs_Smoke_PageBy", 
				"Pgs_Smoke_PageCheckType", "Pgs_Smoke_PageExpected", "Pgs_Smoke_PageName"}, propUI.getProperty("Pgs_Smoke_SheetNamePages"), propUI.getProperty("Pgs_Smoke_ID_0"));
					
		// ###########################################################################################################
		
		// Main Loop	
		for (int numPage=0; numPage<pagesList.length; numPage++)
		{
			boolean bUrls = false;
			// Go to Testing Page
			for (int i=0; i<urlsList.size(); i++) 
			{
					
				bUrls = Q4Web_BF_SplitToArray(pagesList[numPage][1].toLowerCase(), urlsList.get(i).toLowerCase(), sSpliterFirst);
					
				if (bUrls) {
					
					Reporter.log("<br><font size=\"3\"><strong> ----- " + pagesList[numPage][7] + " ----- </strong></font>", true);
					Reporter.log("<a href=\"#Output-" + ID + "-" + Integer.toString(i) + pagesList[numPage][7] +"\" onClick=\'toggleBox(\"Output-" + ID + "-" + Integer.toString(i) + pagesList[numPage][7] +"\", this, \"Show output\", \"Hide output\");'>Show output</a></br>", true);
					Reporter.log("<font color=\"green\">Passed:</font> <font color=\"darkblue\"><strong><a href=\"" + urlsList.get(i) + "\">" + urlsList.get(i) + "</strong></a></font><br>", true);
					Reporter.log("<div class=\'log\' id=\"Output-" + ID + "-" + Integer.toString(i) + pagesList[numPage][7] + "\">", true);
					
					// If it needs to show Network Errors
					if (propConf.getProperty("NetworkErrors").equals(sNetworkErrorsShow)) {
						try {
							proxyLoc.newHar("Har-" + ID + "-" + Integer.toString(i));
							proxyLoc.newPage("Page-" + ID + "-" + Integer.toString(i));
							invokeBrowser(urlsList.get(i));
							
							//Thread.sleep(2000);
							Har har = proxyLoc.getHar();
							Thread.sleep(6000);
							
							for (HarEntry entry : har.getLog().getEntries()) {
								HarRequest request = entry.getRequest();
								HarResponse response = entry.getResponse();
								
								if (Integer.toString(response.getStatus()).matches("[4-5][0-9][0-9][A-Za-z ]*")) {
									Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">ERROR: </font>" + request.getUrl() + " " + response.getStatus() + "<br>", true);
								}
							} 
								
						} catch (TimeoutException e) {
							Reporter.log("<font color=\"red\">Failed:</font> Timeout Exception for URL - <strong>" + urlsList.get(i) + "</strong><br>", true);
							Reporter.log("</div>", true);
							driver.findElement(By.tagName("body")).sendKeys("Keys.ESCAPE");
							//break;
						}	
						
						LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
				        for (LogEntry entry : logEntries) {
				        	if (entry.getLevel().toString().equals("ERROR")) {
				        		Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">ERROR: </font>" + entry.getLevel() + " " + entry.getMessage() + "<br>", true);	        		
				        	}
				        }						
					} else {
						try {
							invokeBrowser(urlsList.get(i));
							Thread.sleep(2000);
						} catch (TimeoutException e) {
							Reporter.log("<font color=\"red\">Failed:</font> Timeout Exception for URL - <strong>" + urlsList.get(i) + "</strong><br>", true);
							Reporter.log("</div>", true);
							driver.findElement(By.tagName("body")).sendKeys("Keys.ESCAPE");							
						}
					}
					
					
					// Split all checking parameters
					String spltCheckName[] = pagesList[numPage][2].split(sSpliterFirst);
					String spltParam[] = pagesList[numPage][3].split(sSpliterFirst);
					String spltBy[] = pagesList[numPage][4].split(sSpliterFirst);
					String spltCheckType[] = pagesList[numPage][5].split(sSpliterFirst);
					String spltExpectedFirst[] = pagesList[numPage][6].split(sSpliterFirst);
							
					List<String> sActuals = new ArrayList();
					boolean bCheck = false;
					String sActual = null;
					String sExpected = null;
					for (int numCheckName=0; numCheckName<spltCheckName.length; numCheckName++) {
						
						List<WebElement> webElements = null;
						
						// Find an element for testing based on input parameters: ByTagName, ByClassName, ByXpath
						try {
							if (spltBy[numCheckName].equals(sByTagName)) {
								webElements = driver.findElements(By.tagName(spltParam[numCheckName]));
							} else if (spltBy[numCheckName].equals(sByClassName)) {
								webElements = driver.findElements(By.className(spltParam[numCheckName]));
							} else if (spltBy[numCheckName].equals(sByXpath)) {
								webElements = driver.findElements(By.xpath(spltParam[numCheckName]));
							} else if (spltBy[numCheckName].equals(sByCssSelector)) {
								webElements = driver.findElements(By.cssSelector(spltParam[numCheckName]));
							}// if						
						} catch (NoSuchElementException ae) {
							Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Element isn't found - <strong>" + spltBy[numCheckName] + ": " 
											+ spltParam[numCheckName] + "</strong><br>", true);
						} catch (Exception e) {
							Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Exception invoked with the element - <strong>" + spltBy[numCheckName] + ": "
											+ spltParam[numCheckName] + "</strong><br>", true);
						} //try NoSuchElementException
						
						// Split Check Type Parameters
						String sCheckTypeSecond[] = spltCheckType[numCheckName].split(sSpliterSecond);
						String sCheckType = sCheckTypeSecond[0];
						String sCheckTypeImp = sNotImp;
						if (sCheckTypeSecond.length > 1) {
							sCheckTypeImp = sCheckTypeSecond[1];							
						}

						Reporter.log("<font size=\"2\"><strong>" + Integer.toString(numCheckName+1) + ". Checkpoint Name: </strong>" + spltCheckName[numCheckName] + "</font><br>", true);							
				
						if (webElements.size() > 0) {
							bCheck = false;
							for (WebElement webElement:webElements) 
							{
								try {

									// Split Expected Parameters
									String spltExpectedSecond[] = spltExpectedFirst[numCheckName].split(sSpliterSecond);
									
									// ###################################################
									// sCheckTypeContains
									if (sCheckType.equals(sCheckTypeContains)) {
										CheckTypeContains.getCheck(webElement, spltExpectedSecond, sActuals, bCheck);
										if (CheckTypeContains.bCheckLocal) {
											bCheck = CheckTypeContains.bCheckLocal;
											sExpected = CheckTypeContains.sExpected;
											sActual = CheckTypeContains.sActual;											
										} else 
											sActuals = CheckTypeContains.sActualNot;
									
									// ###################################################	
									// sCheckTypeEquals
									} else if (sCheckType.equals(sCheckTypeEquals)) {
										CheckTypeEquals.getCheck(webElement, spltExpectedSecond, sActuals, bCheck);
										if (CheckTypeEquals.bCheckLocal) {
											bCheck = CheckTypeEquals.bCheckLocal;
											sExpected = CheckTypeEquals.sExpected;
											sActual = CheckTypeEquals.sActual;											
										} else 
											sActuals = CheckTypeEquals.sActualNot;
									
									// ###################################################
									// sCheckTypePdf	
									} else if (sCheckType.equals(sCheckTypePdf)) {
										CheckTypePdf.getCheck(webElement, spltExpectedSecond, sActuals, bCheck);
										sExpected = CheckTypePdf.sExpected;
										sActual = CheckTypePdf.sActual;											
										sActuals = CheckTypePdf.sActualNot;										
										
									// ###################################################
									// sCheckTypeIFrame	- Ticker Tech Stock Chart
									} else if (sCheckType.equals(sCheckTypeIFrame)) {
										TickerTechChart.getCheck(ID, driver, webElement, spltExpectedSecond, sActuals, bCheck, spltCheckName[numCheckName]);
										sExpected = TickerTechChart.sExpected;
										sActual = TickerTechChart.sActual;											
										sActuals = TickerTechChart.sActualNot;											
											
									// ###################################################
									// sCheckTypeQ4Chart - Q4 Stock Chart
									} else if (sCheckType.equals(sCheckTypeQ4Chart)) {
										Q4StockChart.getCheck(ID, driver, webElement, spltExpectedSecond, sActuals, bCheck, spltCheckName[numCheckName]);
										sExpected = Q4StockChart.sExpected;
										sActual = Q4StockChart.sActual;											
										sActuals = Q4StockChart.sActualNot;
										
									// ###################################################
									// sCheckTypeRssFeed - RSS Feed
									} else if (sCheckType.equals(sCheckTypeRssFeeds)) {
										try {
											if (!webElement.getAttribute("text").trim().equals("")) {
												RssFeeds.getCheck(ID, driver, spltExpectedSecond, sActuals, bCheck);
												sExpected = RssFeeds.getExpected();
												sActual = RssFeeds.getActual();											
												sActuals = RssFeeds.getActualNot();												
											}	
										} catch (StaleElementReferenceException e) {
											break;
										}
											
											
									} //if
									
									
									
									if (bCheck) {
										break;
									} // if (bCheck)
												
								} catch (NullPointerException ae) {
									
								} catch (ArrayIndexOutOfBoundsException be) {
									Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Array Index Out Of Bounds Exception<br>", true);
								} //try NullPointerException
										
							} //for (WebElement
							
							try {
								if ((bCheck) || (sActuals.size()==0)) {
									Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> Actual result <strong>" + sActual + "</strong> is the same as expected result.<br>", true);
								} else {
									if (sCheckTypeImp.equals(sImp)) {
										Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"orange\">Info:</font> Actual result is NOT THE SAME as expected result: <strong>" + spltExpectedFirst[numCheckName] +
												"</strong> or NO SUCH ELEMENT!<br>", true);
										Reporter.log("&nbsp;&nbsp;&nbsp; --- Actual result(s) --- <br>", true);
										try {
											for (int num=0; num<sActuals.size(); num++){
												Reporter.log("&nbsp;&nbsp;&nbsp;" + sActuals.get(num) + "<br>", true);	
											}									
										} catch (NullPointerException e) {
											Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> No Actual results added<br>", true);
										}										
									}


								} // if (bCheck)
								sActuals.clear();
							} catch (ArrayIndexOutOfBoundsException be) {
								sActuals.clear();
								Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Array Index Out Of Bounds Exception<br>", true);
							}
								
						} else {
							if (sCheckTypeImp.equals(sImp)) {
								Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"orange\">Info:</font> Element isn't found - <strong>" + spltBy[numCheckName] + ": "
										+ spltParam[numCheckName] + "</strong><br>", true);								
							}

						} //if (webElements.size() > 0)
						
					} // for (int numCheckName=0;
					
					Q4Web_BF_TakeScreenshot(driver, i+"Srn"+ID, "Q4Web");
					//Reporter.log("<p>&nbsp;</p>", true);
					Reporter.log("</div>", true);
					break;
					
				} // if (bUrls)

			} // for (int i=0;
			
			if (!bUrls) {
				Reporter.log("<br><font size=\"3\"><strong> ----- " + pagesList[numPage][7] + " ----- </strong></font><br>", true);
				Reporter.log("<font color=\"orange\">Info:</font> Page isn't found - <strong>" + pagesList[numPage][1] + "</strong><br>", true);
			}
			
		} // for (int numPage=0 - Main Loop
	
		
		if (propConf.getProperty("NetworkErrors").equals(sNetworkErrorsShow)) {
			proxyLoc.stop();			
		}

		*/
		
    }
 
	
	// Check if the web-site opens and check the version
    private void openHomePage(String searchTerm, String ID) throws InterruptedException {
    	
    	Reporter.log("<h2><a href=\"" + searchTerm + "\">" + searchTerm + "</a></h2>", true);
    	
    	if (propConf.getProperty("Appearance").equals(sAppearanceHidden)) {
        	Reporter.log("<a href=\"#Output-" + ID + "\" onClick=\'toggleBox(\"Output-" + ID + "\", this, \"Show output\", \"Hide output\");'>Show output</a>", true);
        	Reporter.log("<script src=\"js/toggle-boxes.js\" type=\"text/javascript\"></script>", true);
        	Reporter.log("<link href=\"css/toggle-boxes.css\" rel=\"stylesheet\" type=\"text/css\" />", true);	   		
    	}

    	Reporter.log("<div class=\'log\' id=\"Output-" + ID + "\">", true); 
    	
    	//JavascriptExecutor js = (JavascriptExecutor) LocalDriverManager.getDriver();
    	
    	String sVersion = Q4Web_BF_GetVersion(searchTerm, ID, LocalDriverManager.getDriver(), propUI.getProperty("Lbl_Smoke_Version"));
    		
		if (sVersion.equals(propUI.getProperty("Exp_Smoke_Version"))) {
			Reporter.log("<font color=\"green\">Passed:</font> The version is correct - <strong>" + sVersion + "</strong><br>", true);				
		} else {
			Reporter.log("<font color=\"red\">Failed.</font> The version is incorrect: Expected result: <strong>" + propUI.getProperty("Exp_Smoke_Version") + 
					"</strong>, but Actual result is: <strong>" + sVersion + "</strong><br>", true);
		}
		
		//Q4Web_BF_TakeScreenshot(LocalDriverManager.getDriver(), "HomeSrn"+ID, "Q4Web");
    }
    
    
    private void invokeBrowser(String url) {
    	LocalDriverManager.getDriver().manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
    	try {
        	LocalDriverManager.getDriver().get(url);   		
    	} catch (TimeoutException e) {
    		LocalDriverManager.getDriver().findElement(By.tagName("body")).sendKeys("Keys.ESCAPE");
    	}
    }
    
    // Create Test Pages List
    private String[][] createTestPagesList(String[] sParam, String sSheetName, String sSplitSymbol) throws InterruptedException, FileNotFoundException, IOException {
    	
    	// Values from SharedUIMap file for Create Account page
    	String parPageNames[] = sParam; //{sParam1, sParam2, sParam3};
    	
    	if (propConf.getProperty("Source").equals(sSourceFile)) {
        	String[][] createPageValues = Q4Web_BF_ArrayValues(parPageNames);
        	// Create New Excel File
        	Q4Web_BF_CreateNewExcelSheet(sSheetName, createPageValues);    		
    	}

    	// Read Excel File for completing Create Account form
    	String[][] pagesList = Q4Web_BF_ReadExcelSheet(sSheetName, parPageNames.length, sSplitSymbol);

    	return pagesList;   	
    }
    

    @DataProvider(name = SEARCH_TERMS, parallel = true)
    public Object[][] getSearchTerms() throws InterruptedException, FileNotFoundException, IOException {
    	
		// connect to Configuration.properties
		Q4Web_BF_ConnectToConfiguration("./Configuration/Configuration.properties");		
		
		// connect to SharedUIMap.properties
		Q4Web_BF_ConnectToSharedUIMap(propConf.getProperty("SharedUIMap"));
    	
		// Values from SharedUIMap file for URLs Migration
		String parSiteNames[] = {"Par_Smoke_ID", "Par_Smoke_URLs"};
		
		if (propConf.getProperty("Source").equals(sSourceFile)) {
			String[][] createSiteValues = Q4Web_BF_ArrayValues(parSiteNames);
			// Create New Excel File
			Q4Web_BF_CreateNewExcelSheet(propUI.getProperty("Par_Smoke_SheetNameURLs"), createSiteValues);			
		}

		// Read Excel File for completing Create Account form
		sitesList = Q4Web_BF_ReadExcelSheet(propUI.getProperty("Par_Smoke_SheetNameURLs"), parSiteNames.length, propUI.getProperty("Par_Smoke_ID_0"));

		return sitesList;
    }

	@AfterMethod
	public void afterMethod() {
		Reporter.log("</div>", true);
		//LocalDriverManager.getDriver().close();
	}

	@AfterTest(alwaysRun=true)
	public void afterTest() {
		if (LocalDriverManager.getDriver() != null) {
			LocalDriverManager.getDriver().quit();
		}
	}

}
