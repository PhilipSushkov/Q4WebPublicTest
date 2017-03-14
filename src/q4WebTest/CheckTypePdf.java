package q4WebTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import functions.Q4WebFunctions;

import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class CheckTypePdf extends Q4WebFunctions {
	public static boolean bCheckLocal = false;
	public static List<String> sActualNot = new ArrayList();
	public static String sActual = null, sExpected = null, sTypePdfExp = propUI.getProperty("Pgs_Smoke_CheckTypePdfExp");
	private static int iResponseCodeOk = 200;		
	
	public static void getCheck(WebElement webElement, String[] spltExpectedSecond, List<String> sActuals, boolean bCheck) throws MalformedURLException, IOException {
		sActualNot = sActuals;
		bCheckLocal = bCheck;
		
		for (int n=0; n<spltExpectedSecond.length; n++) {
		    
			if (Q4Web_BF_ExpectedAndActual(sTypePdfExp, webElement.getAttribute("href").trim())) {
				
				int statusCode = Q4Web_BF_GetResponseCode(webElement.getAttribute("href").trim());
				int statusSize = Q4Web_BF_GetResponseSize(webElement.getAttribute("href").trim());
				
				if (statusCode==iResponseCodeOk) {
					sExpected = spltExpectedSecond[n];
					sActual = Integer.toString(statusCode);
					
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"green\">Passed:</font> " + webElement.getAttribute("textContent") + ": " + webElement.getAttribute("href").trim() + ". Response Code: <strong>" + Integer.toString(statusCode) + 
							"</strong>. Size: <strong>" + Integer.toString(statusSize) + "</strong><br>", true);
					
					break;
				} else {
					sExpected = spltExpectedSecond[n];
					sActual = Integer.toString(statusCode);
					Reporter.log("&nbsp;&nbsp;&nbsp;<font color=\"red\">Failed:</font> " + webElement.getAttribute("textContent") + ": <a href=\"" + webElement.getAttribute("href").trim() + "\">" + webElement.getAttribute("href").trim() + 
							"</a>. Response Code: <strong>" + Integer.toString(statusCode) + "</strong><br>", true);
					String sResponseMessage = webElement.getAttribute("textContent") + ". Response Code: <strong>" + Integer.toString(statusCode) + "</strong>";
					if (!Q4Web_BF_CompareValues(sActuals, sResponseMessage)) {
						sActualNot.add(sResponseMessage);
					}
				}												
			} // if (Q4Web_BF_Expected		
		} // for (int n=0;
		
	}
	

}
