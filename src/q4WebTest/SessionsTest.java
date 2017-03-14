package q4WebTest;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SessionsTest {
	
	@BeforeMethod
	public void beforeMethod() {
		
	}

	  
	@Test
		public void main() {
	      //First session of WebDriver
	      WebDriver driver = new FirefoxDriver();
	      JavascriptExecutor js = (JavascriptExecutor) driver;
	      
	      //Goto guru99 site
	      driver.get("http://www.barrick.com");
	      WebElement webElement = driver.findElement(By.tagName("script"));
	      System.out.println(webElement.getAttribute("type"));
	      
	       //Object val = js.executeScript("window.GetVersionNumber();");
	       Object val = js.executeScript("return GetVersionNumber();");
	       System.out.println(val);
	     
	      /* if (driver.getTitle().contains("Спец-тех-импекс")) {
	    	  System.out.println("Passed");
	      } else {
	    	  System.out.println("Failed");
	      } */
	      
	      driver.close();
		}
  

	@AfterMethod
	public void afterMethod() {
		
	}

}
