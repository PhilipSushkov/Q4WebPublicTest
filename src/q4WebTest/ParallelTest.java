package q4WebTest;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;
import java.util.concurrent.TimeUnit;
import static org.testng.Assert.assertNotNull;

public class ParallelTest {
    public static final String SEARCH_TERMS = "search-terms";
    private WebDriver driver;

    @BeforeMethod
    @Parameters({"browser"})
    public void beforeMethod(@Optional("chrome") String browser){
        /* driver = getBrowser(browser);
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); */
    }

    private WebDriver getBrowser() {
        return new FirefoxDriver();
    }

    /* @Test(description = "Check parallel selenium works.",
            dataProvider = SEARCH_TERMS)    
    public class SessionHandling {
    public void parallelSeleniumTest(String searchTerm) throws InterruptedException {
    	//driver = new FirefoxDriver();
    	//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //driver.get("http://google.com");
    	//driver.get(searchTerm);
        WebElement search = driver.findElement(By.id("gbqfq"));
        new Actions(driver)
                .sendKeys(search, searchTerm)
                .sendKeys(search, Keys.ENTER)
                .perform();
        String firstResult = driver.findElements(By.className("r")).get(0).getText();
        assertNotNull(firstResult);
        System.out.println(firstResult);
    	//driver.quit();
    }
    } */
    
	@Test (dataProvider = SEARCH_TERMS)
	public class SessionHandling {
		public void main(String searchTerm) {
	      //First session of WebDriver
	      WebDriver driver = new FirefoxDriver();
	      //Goto guru99 site
	      driver.get("http://bellushealth.q4web.local");
	       
	      //Second session of WebDriver
	      WebDriver driver2 = new FirefoxDriver();
	      //Goto guru99 site
	      driver2.get("http://stornoway.q4web.local");
		}
	}

    @DataProvider(name = SEARCH_TERMS, parallel = true)
    public Object[][] getSearchTerms(){
        return new Object[][]{
                {"http://bellushealth.q4web.local"},
                {"http://stornoway.q4web.local"},
                {"http://goldcorp.q4web.local"}
        };
    }
    
    
    @AfterMethod
    public void afterMethod(){
       // driver.quit();
    }
    
}
