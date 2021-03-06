package q4WebTest;

import java.util.ArrayList;
import java.util.List;

import functions.Q4WebFunctions;

import org.openqa.selenium.WebElement;

public class CheckTypeContains extends Q4WebFunctions {
	public static String spltCheckName, spltParam, spltBy;
	public static boolean bCheckLocal = false;
	public static List<String> sActualNot = new ArrayList();
	public static String sActual = null, sExpected = null;
		
	
	public static void setParams(String CheckName, String Param, String By) {
		spltCheckName = CheckName;
		spltParam = Param;
		spltBy = By;
	}
	
	public static void getCheck(WebElement webElement, String[] spltExpectedSecond, List<String> sActuals, boolean bCheck) {
		sActualNot = sActuals;
		bCheckLocal = bCheck;
		for (int n=0; n<spltExpectedSecond.length; n++) {
			if (Q4Web_BF_ExpectedAndActual(spltExpectedSecond[n], webElement.getAttribute("textContent"))) {
				bCheckLocal = true;
				sExpected = spltExpectedSecond[n];
				sActual = webElement.getAttribute("textContent");
				break;
			} else {
				if (!Q4Web_BF_CompareValues(sActuals, webElement.getAttribute("textContent").trim())) {
					sActualNot.add(webElement.getAttribute("textContent").trim());	
				}
			}
		} // for (int n=0;

	}
	
	

}
