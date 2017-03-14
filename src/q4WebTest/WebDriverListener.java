package q4WebTest;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
 
public class WebDriverListener implements IInvokedMethodListener {
 
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String browserName = method.getTestMethod().getXmlTest().getLocalParameters().get("browserName");
        	//String browserName = "firefox";
            WebDriver driver = null;
			try {
				driver = LocalDriverFactory.createInstance(browserName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            LocalDriverManager.setWebDriver(driver);
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            WebDriver driver = LocalDriverManager.getDriver();
            if (driver != null) {           	
                driver.quit();
            }
        }
    }
}