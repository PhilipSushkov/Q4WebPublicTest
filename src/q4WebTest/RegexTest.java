package q4WebTest;

import org.testng.annotations.Test;

import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;

public class RegexTest {
	public static List<String> slDatesX = new ArrayList();
	
  @Test
  public void f() throws InterruptedException {
	  int numStockValues = 0;
	  
      //System.out.println(test("http://www.bellushealth.com/English/news/News/default.aspx".toLowerCase()));
      System.out.println(test("Press Release RSS Feed".toLowerCase()));
      //System.out.println("Investor - rss feeds".matches(".*rss[- ]feed[s]*"));
	  
  }
  
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
	  
  }
  
  public static boolean test(String testString){  
      //Pattern p = Pattern.compile(".+(investor|news)[a-z,-]*/news[a-z,-]*/default.aspx");
	  Pattern p = Pattern.compile(".+[a-zA-Z- ]*");
      Matcher m = p.matcher(testString);  
      return m.matches();  
  } 
  

}
