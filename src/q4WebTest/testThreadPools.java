package q4WebTest;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;

public class testThreadPools {
	
	@BeforeMethod
	public void beforeMethod() {
		
	}
	
	  @Test(invocationCount = 1, threadPoolSize = 3)
	  public void loadTest() {

		System.out.printf("[START] Thread Id : %s is started!%n", Thread.currentThread().getId());
			
		WebDriver driver = new FirefoxDriver();
		driver.get("http://bellushealth.q4web.local");
		
		//perform whatever actions, like login, submit form or navigation
		System.out.printf("[END] Thread Id : %s%n", Thread.currentThread().getId());
			
		driver.quit();

	  }
	
	@AfterMethod
	public void afterMethod() {
		
	}

}
