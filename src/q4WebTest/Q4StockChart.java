package q4WebTest;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import functions.Q4WebFunctions;

public class Q4StockChart extends Q4WebFunctions {
	public static boolean bCheckLocal = false;
	public static List<String> sActualNot = new ArrayList();
	public static String sActual = null, sExpected = null;
	private static String sMonthClick, sQuarterClick, sHalfYearClick, sYTDClick, sYearClick, sAllClick, 
	s5YearsClick, s10YearsClick, sMaxClick;
	private static String sByChartBtn, sTimeMatches, sByChartTimeLine, sTimeLineMatches, sByHighchartsSeries; //sColourBlack = "rgba(0, 0, 0, 1)"
	
	public static void beforeMethod() {
		//sPathName = "ScreenShots/Q4Web";
		//sPathBefore = "../../../";
		
		sMonthClick = "1[Mm]"; sQuarterClick = "3[Mm]"; sHalfYearClick = "6[Mm]"; sYTDClick = "YTD"; sYearClick = "1[Yy]"; sAllClick = "All";
		s5YearsClick = "5[Yy]"; s10YearsClick = "10[Yy]"; sMaxClick = "[MAXmax]";
		
		sTimeMatches = sMonthClick + "|" + sQuarterClick + "|" + sHalfYearClick + "|" + sYTDClick + "|" + sYearClick + "|" + sAllClick + "|" + 
		s5YearsClick + "|" + s10YearsClick + "|" + sMaxClick;
		
		sTimeLineMatches = ".*[0-9 \\.]*(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|[0-9])[0-9 \\']*";
		
		sByChartBtn = propUI.getProperty("Elm_Smoke_ChartBtn");
		sByChartTimeLine = propUI.getProperty("Elm_Smoke_ChartTimeLine");
		sByHighchartsSeries = propUI.getProperty("Elm_Smoke_ChartSeries");
	}
	
	public static void getCheck(String ID, WebDriver driver, WebElement webElementChart, String[] spltExpectedSecond, List<String> sActuals, boolean bCheck, String CheckName) throws InterruptedException {
		int numStockValues = 0;
		int numStockValuesMax = Integer.parseInt(propUI.getProperty("Val_Q4StockChart_NumStockValueMax"));
		List<String> sDatesPevious = new ArrayList();
		
		beforeMethod();
		
		sActualNot = sActuals;
		bCheckLocal = bCheck;
		
		Thread.sleep(2000);
		
		for (int n=0; n<spltExpectedSecond.length; n++) {
			try {

				// Find Period Time buttons
			    List<WebElement> webElementsBtn = webElementChart.findElements(By.cssSelector(sByChartBtn));
			    
			    if (webElementsBtn.size()==0) {
			    	webElementsBtn = webElementChart.findElements(By.cssSelector("tspan"));
			    }
			    
			    
			    List<String> sButtons = new ArrayList();
			    for (WebElement webElementBtn:webElementsBtn) {
			    	String sBtnText;
			    	
			    	try {
				    	sBtnText = webElementBtn.findElement(By.cssSelector("text")).getAttribute("textContent");			    		
			    	} catch (NoSuchElementException e) {
				    	sBtnText = webElementBtn.getAttribute("textContent");			    		
			    	}
			    	
			    	if (sBtnText.matches(sTimeMatches)) {
			    		sButtons.add(sBtnText);
			    		
			    		// Click the buttons
			    		webElementBtn.click();
			    		Thread.sleep(2000);
			    		
			    		try {
							// Find Time Line values
					        List<WebElement> webElementsTimeLine = webElementChart.findElements(By.cssSelector(sByChartTimeLine));
					        List<String> sDates = new ArrayList();
					        for (WebElement webElementTimeLine:webElementsTimeLine) {
					      	   if (webElementTimeLine.getAttribute("textContent").matches(sTimeLineMatches)) {
					      		  sDates.add(webElementTimeLine.getAttribute("textContent"));
					      	   }	  
					        }
					        
					        /* List<WebElement> webElementsFromTo = webElementChart.findElements(By.cssSelector("input[name='min']"));
					        if (webElementsFromTo.size() == 2) {
	 					        System.out.println("From: " + webElementsFromTo.get(0).getAttribute("value"));
	 					        System.out.println("To: " + webElementsFromTo.get(1).getAttribute("value"));					        	
					        } */

					        
					    	if (sDates.size()>0) {
					    		Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"blue\">Info:</font> Time Line values for <strong>" + sBtnText + "</strong> - <strong>" + sDates.toString() + "</strong></br>", true);	
					    		if (!sDatesPevious.equals(sDates)) {
					    			Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> Time Line values for <strong>" + sBtnText + "</strong> was changed<br>", true);
					    		} else {
					    			Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"orange\">Info:</font> Time Line values for <strong>" + sBtnText + "</strong> was NOT changed! The button <strong>" + sBtnText + "</strong> might be NOT active.<br>", true);
					    		}
					    	} else {
					    		Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Time Line does NOT have any values for <strong>" + sBtnText + "</strong>!<br>", true);
								if (!Q4Web_BF_CompareValues(sActuals, "Time Line does NOT have any values for " + sBtnText)) {
									sActualNot.add("Time Line does NOT have any values for " + sBtnText);
								}
					    	}
					    	
					    	sDatesPevious.clear();
					    	sDatesPevious.addAll(sDates);
					    	sDates.clear();
					    	
					        List<WebElement> webElementsChartSeries = webElementChart.findElements(By.cssSelector(sByHighchartsSeries));
					        int iChartNum = 0;
					        for (WebElement webElementChartSeries:webElementsChartSeries) {
					      	  if (webElementChartSeries.getAttribute("visibility").equals("visible")) {
					      		  iChartNum++;
					      		  //System.out.println("path d: " + webElementChartSeries.findElement(By.cssSelector("path")).getAttribute("d"));
					      		  String spltParam[] = webElementChartSeries.findElement(By.cssSelector("path")).getAttribute("d").split(" ");
					      		  
					          	  for (int i=0; i<spltParam.length; i++) {
					          		  try {
					          			  //System.out.println(spltParam[i]);
					          			  if (spltParam[i].matches("[A-Za-z]")) {
					          				  i=i+2;
					          			  } else {
					          				  i++;
					          			  }
					          			  
					          			  long dStockValue =  (long)Double.parseDouble(spltParam[i]);
					          			  //System.out.println("- " + Long.toString(dStockValue));
					          			  if (dStockValue != 0) {
					          				  numStockValues++;
					          			  }
					          			  
					          			  if (numStockValues >= numStockValuesMax) {
					          				  Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> Visible Stock Chart #" +iChartNum + " has at least 20 stock values > 0<br>", true);
					          				  break;
					          			  }
					          			  
					          		  } catch (NumberFormatException e) {
					          			  System.out.println("Number Format Exception");
					          		  }

					          	   } // for (int i=0;
					          	  
					          	  if (numStockValues < numStockValuesMax) {
					          		Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Visible Stock Chart #" +iChartNum + " does not have at least 20 stock values more than 0!<br>", true);
									if (!Q4Web_BF_CompareValues(sActuals, "Visible Stock Chart #" +iChartNum + " does not have at least 20 stock values more than 0!")) {
										sActualNot.add("Visible Stock Chart #" +iChartNum + " does not have at least 20 stock values more than 0!");
									}
					          	  }
					          	  
					          	  numStockValues = 0;
					          	  
					      	  } // if (webElementChartSeries
					      	  
					       } // for (WebElement webElementChartSeries
					    	
			    		} catch (NoSuchElementException e) {
			    			e.printStackTrace();
			    		}
			    		
			
			    	}
			    	
			    }
				
		    	if (sButtons.size()>0) {
		    		Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"blue\">Info:</font> Time period buttons exist - <strong>" + sButtons.toString() + "</strong></br>", true);
		    	}
				
		    	
				if (sActualNot.size()==0) {
					sExpected = spltExpectedSecond[n];
					sActual = "available";	
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> " + CheckName + " is available<br>", true);
				} else {
					sExpected = spltExpectedSecond[n];
					sActual = "not available";	
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> " + CheckName + " is NOT available<br>", true);
					if (!Q4Web_BF_CompareValues(sActuals, CheckName + " is " + sActual)) {
						sActualNot.add(CheckName + " is " + sActual);
					}
				}
	
				
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			} catch (NullPointerException ae) {
				ae.printStackTrace();
			} catch (WebDriverException wde) {
				wde.printStackTrace();
			}
			
		} // sCheckTypeQ4Chart	- Stock Chart
		
		
	}

}
