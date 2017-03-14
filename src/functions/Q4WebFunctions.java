package functions;

import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.Reporter;

import io.appium.java_client.ios.IOSDriver;
import q4WebTest.HttpDownloadUtility;
import q4WebTest.LocalDriverManager;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

public class Q4WebFunctions {
	public static Properties propUI;
	public static Properties propConf;
	
	//#############################################################################
	//1.1 Connect to SharedUIMap.properties
	public void Q4Web_BF_ConnectToSharedUIMap(String sPathSharedUIMap) throws FileNotFoundException, IOException {
		propUI = new Properties();
		propUI.load(new FileInputStream(sPathSharedUIMap));
	}
	
	//#############################################################################
	//1.2 Connect to Configuration.properties
	public void Q4Web_BF_ConnectToConfiguration(String sPathConfiguration) throws FileNotFoundException, IOException {
		propConf = new Properties();
		propConf.load(new FileInputStream(sPathConfiguration));
	}
	
	//#############################################################################
	//3. Take Screenshot
	public void Q4Web_BF_TakeScreenshot (WebDriver driver, String sShotName, String sPageName) {
		String msg, path = null, pathBefore = "../../../";
		String res;
		try {
			
			// Take screenshot and save it in source object
			File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
			// Define path where Screenshot will be saved
			path = "ScreenShots/" + sPageName + "/" + sShotName + ".png";
			
			//Copy the source file at the screenshot path
			FileUtils.copyFile(source,  new File(path));
			msg = "Screenshot <a href=\"" + pathBefore + path + "\">" + sShotName + "</a> captured<br>";
			res = "Pass";
		} catch (IOException e) {
			msg = "Failed to capture screenshot:" + e.getMessage();
			res = "Fail";
		} catch (WebDriverException wde) {
			msg = "Failed to capture screenshot:" + wde.getMessage();
			res = "Fail";
		}
		
		//Reporter.log function as used in TestNG test
		Reporter.log(msg, true);
		//Reporter.log("<br><img scr=\"../../" + path + "\"><br>", true);
	
	}
	
	
	//#############################################################################
	//4. Split the string to array
	public boolean Q4Web_BF_SplitToArray(String sParam1, String sParam2, String sSpliter) {
		String spltParam[] = sParam1.split(sSpliter);
		boolean bCheck = false;
		for (int n=0; n<spltParam.length; n++) {
			/* if (sParam2.matches(spltParam[n])) {
				System.out.println("- " +spltParam[n]);
			} */
			
			try {
				if (sParam2.matches(spltParam[n])) {
					bCheck = true;
					break;
				}				
			} catch (PatternSyntaxException pse) {
				break;
			}
		}
		return bCheck;	
	}
	
	
	//#############################################################################
	//4.1 Split the string to array
	public static boolean Q4Web_BF_ExpectedAndActual(String sExpected, String sActual) {
		boolean bCheck = false;
		sActual = sActual.trim().toLowerCase();
		
		try {
			 if (sActual.matches(sExpected)) {
				bCheck = true;
			}
		} catch (PatternSyntaxException pse) {
			Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Pattern Syntax Exception for RegEx - <strong>" + sExpected + "</strong><br>", true);
		}
		return bCheck;
	}
	
	
	//#############################################################################
	//5. Build URLs List
	public List<String> Q4Web_BF_BuildUrlsList(WebDriver driver, By by) {
		List<WebElement> eUrls = driver.findElements(by);
		List<String> urlsList = new ArrayList<String>();
		for (WebElement eUrl:eUrls)
			urlsList.add(eUrl.getAttribute("textContent"));
		return urlsList;	
	}	
	
	//#############################################################################
	//5.1 Build URLs List
	public List<String> Q4Web_BF_AddUrlsList(WebDriver driver, By by, List<String> urlsList, String sExpected) {
		List<WebElement> eUrls = driver.findElements(by);


			for (WebElement eUrl:eUrls) {
				try {				
				//Reporter.log(eUrl.getAttribute("href") + "<br>", true);
				//Reporter.log("-- " + eUrl.getAttribute("baseURI") + "<br>", true);
				if ((!eUrl.getAttribute("href").equals(eUrl.getAttribute("baseURI"))) && (!eUrl.getAttribute("href").equals(eUrl.getAttribute("baseURI") + "#"))) {
					try {
						if (eUrl.getAttribute("href").matches(sExpected)) {
							//Reporter.log(eUrl.getAttribute("href") + "<br>", true);	
							urlsList.add(eUrl.getAttribute("href"));
						}
					} catch (PatternSyntaxException pse) {
						Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> Pattern Syntax Exception for RegEx - <strong>" + sExpected + "</strong><br>", true);
					}
				}
				} catch (NullPointerException e) {
					
				} catch (StaleElementReferenceException se) {
					
				}
			}
		return urlsList;	
	}
	
	//#############################################################################
	//5.2 URL add slash
	public String Q4Web_BF_UrlAddSlash(String searchTerm, String sSlash, String sHttp) {
		searchTerm = searchTerm.trim();
		
		String sFirstChar = searchTerm.substring(0, 7);
		//System.out.println(sFirstChar);
		
		if (!sFirstChar.equals(sHttp)) {
			searchTerm = sHttp + searchTerm;
		}
		
		String sLastChar = searchTerm.substring(searchTerm.length()-1, searchTerm.length());
		if (sLastChar.equals(sSlash)) {
			return searchTerm;
		} else {
			searchTerm = searchTerm + sSlash;
			return searchTerm;
		}
		
		
	}	
	
	//#############################################################################
	//5.3 URL add slash
	public static boolean Q4Web_BF_CompareValues(List<String> sList, String sValue) {
		boolean sSame = false;
		for (int i = 0; i < sList.size(); i++) {
			if (sList.get(i).equals(sValue)) {
				sSame = true;
			}
		}
		return sSame;
	}
	
	//#############################################################################
	//6. Create New Excel Sheet for any page
	public void Q4Web_BF_CreateNewExcelSheet(String sSheetName, String[][] sValues) {
		HSSFWorkbook workbook = null;
		try {
			try {
				FileInputStream inpFile = new FileInputStream(new File(propConf.getProperty("DataExcelFile")));
				workbook = new HSSFWorkbook(inpFile);
			} catch (FileNotFoundException e) {
				workbook = new HSSFWorkbook();
			}
			
			HSSFSheet sheet = null;
			
			try {
				sheet = workbook.createSheet(sSheetName);
			} catch (IllegalArgumentException e) {
				sheet = workbook.getSheet(sSheetName);
				for (int index = sheet.getLastRowNum(); index >= sheet.getFirstRowNum(); index--) {
					sheet.removeRow(sheet.getRow(index));
				}
			}
		 
			Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();
		
			int count = 0;
			while (count < sValues.length) {
				data.put(Integer.valueOf(count+1), sValues[count]);
				count++;			
			}	
		
			Set<Integer> keyset = data.keySet();
			int rownum = 0;
			for (Integer key : keyset) {
				Row row = sheet.createRow(rownum++);
				Object [] objArr = data.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					if(obj instanceof Date) 
						cell.setCellValue((Date)obj);
					else if(obj instanceof Boolean)
						cell.setCellValue((Boolean)obj);
					else if(obj instanceof String)
						cell.setCellValue((String)obj);
					else if(obj instanceof Double)
						cell.setCellValue((Double)obj);
				}
			}
		
		    FileOutputStream outFile = 
		            new FileOutputStream(new File(propConf.getProperty("DataExcelFile")));
		    workbook.write(outFile);
		    outFile.close();
		    //Reporter.log("Excel was written successfully.<br>");
		     
		} catch (IOException e) {
		    e.printStackTrace();
		}	
		
	}
	
	//#############################################################################
	//7. Read Excel File for completing any form
	public String[][] Q4Web_BF_ReadExcelSheet(String sSheetName, int columnsTotal, String sExcept) throws InterruptedException {
		try {
		     
		    FileInputStream file = new FileInputStream(new File(propConf.getProperty("DataExcelFile")));
		     
		    //Get the workbook instance for XLS file 
		    HSSFWorkbook workbook = new HSSFWorkbook(file);
		 
		    //Get first sheet from the workbook
		    HSSFSheet sheet = workbook.getSheet(sSheetName);
		     
		    //Iterate through each rows from first sheet
		    Iterator<Row> rowIterator = sheet.iterator();
		    
		    //String str[][] = new String[sheet.getLastRowNum()+1][columnsTotal];
		    ArrayList<String[]> zoom = new ArrayList();
		    
		    //System.out.print(sheet.getLastRowNum() + "\n");
		    
			while(rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        String temp[] = new String[row.getLastCellNum()];
		         
		        //System.out.print(row.getCell(0).getStringCellValue() + "\n");
		        if (!String.valueOf(row.getCell(0)).equals(sExcept)) {
			        //zoom.add(new String[]{row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue()});			        	
		        	for (int i=0; i<row.getLastCellNum(); i++) {
		        		if (row.getCell(i).getCellType() == 0)
		        			temp[i] = Integer.toString(((int)row.getCell(i).getNumericCellValue()));
			        	if (row.getCell(i).getCellType() == 1)
			        		temp[i] = row.getCell(i).getStringCellValue();
			        	//System.out.print(temp[i] + "\n");
		        	}
		        	zoom.add(temp);
		        }
		    }
		    file.close();
		    
		    String str[][] = zoom.toArray(new String[zoom.size()][columnsTotal]);
		    
		    return str;
		     
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return null;	
	}

	//#############################################################################
	//8. Values from SharedUIMap file for any page
	public String[][] Q4Web_BF_ArrayValues(String arrayValues[]) {
		int columnsNumber = arrayValues.length; // number of columns
		int n = 0;
		
		try {
			Enumeration KeyValues = propUI.keys();
			while (KeyValues.hasMoreElements()) {
				String key = (String) KeyValues.nextElement();
				   if (key.contains(arrayValues[0]) ) {
				   n = n + 1;
				   }
			}
			
			String str[][] = new String[n][columnsNumber];
			
			KeyValues = propUI.keys();
			while (KeyValues.hasMoreElements()) {
				String key = (String) KeyValues.nextElement();
				for (int num=0; num<arrayValues.length; num++)
				{
					if (key.contains(arrayValues[num]) ) {
						String val1=key.substring(arrayValues[num].length()+1);
						int i = Integer.parseInt(val1);
						String value = propUI.getProperty(key);
						str[i][num] = value;
						//System.out.println(i + " " + str[i][num] + ":- " + key);
						} 					
					}
				}
			return str;

		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//#############################################################################
	//9. isElementPresent NATIVE_APP
	public boolean Q4Web_BF_isElementPresent(WebDriver driver, By element) {
		try {
				driver.findElement(element);
				return true;
			} catch (NoSuchElementException ae) {
				return false;
			} catch (WebDriverException wde) {
				return false;				
			}
	}
	

	//#############################################################################
	//10. Check if Element is present
	 public boolean Q4Web_BF_isElementAssert(WebDriver driver, String sParam1) {
		try {
			   Assert.assertTrue(Q4Web_BF_isElementPresent(driver, By.xpath(propUI.getProperty(sParam1))));
			   return true;
			} 
		catch (AssertionError e) {
			try {
				   Assert.assertTrue(Q4Web_BF_isElementPresent(driver, By.id(propUI.getProperty(sParam1))));
				   return true;
				} 
			catch (AssertionError ae) {
				try {
					   Assert.assertTrue(Q4Web_BF_isElementPresent(driver, By.className(propUI.getProperty(sParam1))));
					   return true;
					} 
				catch (AssertionError aeu) {
					   return false;
					}
				}
			}
		
	}
	 
	 
	//#############################################################################
	//11. Check if Activity Page is opened
	public boolean Q4Touch_BF_isElementAssertClassName(IOSDriver driver, String sParam1) {
		try {
				Assert.assertTrue(Q4Web_BF_isElementPresent(driver, By.className(propUI.getProperty(sParam1))));
				return true;
			} 
		catch (AssertionError ae) {
				return false;
			}
			
		}
	
	
	//#############################################################################
	//12. URL add slash
	public String Q4Web_BF_GetVersion(String searchTerm, String ID, WebDriver driver, String sExpectedVersion) {
		
		JavascriptExecutor js = (JavascriptExecutor) LocalDriverManager.getDriver();
		String sVersion = null;
		
    	try {
    		if (Q4Web_BF_isElementPresent(driver, By.className(sExpectedVersion))) {
    			Reporter.log("<font color=\"green\">Passed:</font> Web-site <strong>" + searchTerm + "</strong> was opened successfully!<br>", true);
    			Reporter.log("<font size=\"3\"><strong>Title: </strong>" + driver.getTitle() + "</font><br>", true);
    			sVersion = driver.findElement(By.className(sExpectedVersion)).getAttribute("textContent");
    		} else if (!js.executeScript("return GetVersionNumber();").equals(null)) {
    			Reporter.log("<font color=\"green\">Passed:</font> Web-site <strong>" + searchTerm + "</strong> was opened successfully!<br>", true);
    			Reporter.log("<font size=\"3\"><strong>Title: </strong>" + driver.getTitle() + "</font><br>", true);	
    			sVersion = js.executeScript("return GetVersionNumber();").toString();
    		} else {
    			Reporter.log("<font color=\"red\">Failed:</font> Web-site <strong>" + searchTerm + "</strong> wasn't opened correctly!<br>", true);
    			Assert.fail("Failed: Web-site " + searchTerm + " wasn't opened correctly!. Test failed.");
    		}    		
    	} catch (WebDriverException wde) {
			Reporter.log("<font color=\"red\">Failed:</font> The version of Web-site <strong>" + searchTerm + "</strong> can not be defined correctly!<br>", true);
			Assert.fail("Failed: The version of Web-site " + searchTerm + " can not be defined correctly!. Test failed.");    		
    	}
    	return sVersion;
	}	
	
	
	//#############################################################################
	//13.1 get HTTP Response code
	public static int Q4Web_BF_GetResponseCode(String urlString) throws MalformedURLException, IOException {
		try {
		    URL url = new URL(urlString);
		    HttpURLConnection huc = (HttpURLConnection)url.openConnection();
		    huc.setRequestMethod("GET");
		    //System.out.println(huc.getContentLength());
		    //huc.connect();
		    
		    //System.out.println(Integer.toString(huc.getContentLength()));
			//System.out.println(Integer.toString(huc.getInputStream().available()));
		    
		    int iResponseCode = huc.getResponseCode();
		    huc.disconnect();
		    
		    return iResponseCode;			
		} 
		catch (Exception e) {
	        e.printStackTrace();
	        return 404;
	    }
	}
	
	//#############################################################################
	//13.2 get HTTP Response size
	public static int Q4Web_BF_GetResponseSize(String urlString) throws MalformedURLException, IOException {
		try {
		    URL url = new URL(urlString);
		    HttpURLConnection huc = (HttpURLConnection)url.openConnection();
		    huc.setRequestMethod("GET");
		    //System.out.println(huc.getContentLength());
		    //huc.connect();
		    
		    //System.out.println(Integer.toString(huc.getContentLength()));
			//System.out.println(Integer.toString(huc.getInputStream().available()));
		    
		    int iResponseSize = huc.getContentLength();
		    huc.disconnect();
		    
		    return iResponseSize;			
		} 
		catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	
	//#############################################################################
	//13. get HTTP Response code
	public static int Q4Web_BF_GetContentLength(String urlString) throws MalformedURLException, IOException {
		try {
		    URL url = new URL(urlString);
		    HttpURLConnection huc = (HttpURLConnection)url.openConnection();
		    huc.setRequestMethod("GET");
		    
		    //System.out.println(urlString);

		    //huc.connect();

		    //System.out.println(Integer.toString(huc.getContentLength()));
			//System.out.println(Integer.toString(huc.getInputStream().available()));
		    
		    int iContentLength = huc.getContentLength();
		    huc.disconnect();
		    
		    return iContentLength;			
		} 
		catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	
	//#############################################################################
	//13.2 get HTTP Response code
	public static int Q4Web_BF_GetInputStream(String urlString) throws MalformedURLException, IOException {
		try {
		    URL url = new URL(urlString);
		    HttpURLConnection huc = (HttpURLConnection)url.openConnection();
		    //Thread.sleep(10000);
		    huc.setRequestMethod("GET");
		    
		    System.out.println(urlString);

		   //huc.connect();
		    //Thread.sleep(5000);
		    
			/* BufferedReader in = new BufferedReader(
			        new InputStreamReader(huc.getInputStream()));
		    
		    System.out.println(Integer.toString(huc.getContentLength()));
			System.out.println(Integer.toString(huc.getInputStream().available()));
			System.out.println(Integer.toString(huc.getInputStream().read()));
			System.out.println(Integer.toString(in.read())); */

			//print result
			//System.out.println(response.toString());
		    
		    huc.disconnect();
		    return huc.getInputStream().available();			
		} 
		catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	
	
	//#############################################################################
	//13.3 post HTTP Response code
	public static int Q4Web_BF_PostResponseCode(String urlString) throws MalformedURLException, IOException {
		try {
		    URL url = new URL(urlString);
		    HttpURLConnection huc = (HttpURLConnection)url.openConnection();
		    huc.setRequestMethod("POST");
		    huc.setRequestProperty("Accept", "*/*");
			huc.setRequestProperty("User-Agent", "Mozilla/5.0");
			//huc.setRequestProperty("Content-Length", Integer.toString((urlString.split("\\?")[1]).length()));
			huc.setRequestProperty("Content-Length", "0");
			huc.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = urlString.split("\\?")[1];
			
			// Send post request
			huc.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(huc.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			//huc.connect();
			int responseCode = huc.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			System.out.println(huc.getResponseMessage());
			System.out.println(huc.getRequestProperty("Content-Length"));
			System.out.println(huc.getRequestProperty("Accept-Language"));
			System.out.println(huc.getContentLengthLong());

		    return huc.getResponseCode();			
		} 
		catch (Exception e) {
	        e.printStackTrace();
	        return 404;
	    }
	}
	
	
	//#############################################################################
	//13.4 HEAD of HTTP Response code	
	public static boolean Q4Web_BF_LinkExists(String URLName){
	    try {
	        HttpURLConnection.setFollowRedirects(false);
	        HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
	        con.setRequestMethod("HEAD");
	        //Reporter.log("HTTP_OK: " + HttpURLConnection.HTTP_OK + "<br>", true);
	        return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	//#############################################################################
	//13.1 Http Downloader		
	 public static String Q4Web_BF_HttpDownloader(String fileURL, String saveDir, String fileName) {
	     try {
	         return HttpDownloadUtility.downloadFile(fileURL, saveDir, fileName);
	     } catch (IOException ex) {
	            ex.printStackTrace();
	         return "";   
	     }
	 }
			 
}
