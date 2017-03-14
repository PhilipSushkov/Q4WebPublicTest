package q4WebTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
 
class LocalDriverFactory {
	//private static String responseCodeError = "[4-5][0-9][0-9][A-Za-z ]*";
	private static String sNetworkErrorsShow = "show"; //, sProxyExc = "tacoda.at.atwola.com";
	public static BrowserMobProxy proxy;
	
	
    static WebDriver createInstance(String browserName) throws FileNotFoundException, IOException {
        WebDriver driver = null;
        
		Properties propConf = new Properties();
		propConf.load(new FileInputStream("./Configuration/Configuration.properties"));
		
        if (browserName.toLowerCase().contains("firefox")) {
        	
        	if (propConf.getProperty("NetworkErrors").equals(sNetworkErrorsShow)) {
    			// ######################################
        		BrowserMobProxy proxyTemp = new BrowserMobProxyServer();
        				
    		    // start the proxy
        		proxyTemp.start();
        		
    		    // get the Selenium proxy object
    		    Proxy selProxy = ClientUtil.createSeleniumProxy(proxyTemp);

    		    // configure it as a desired capability
    		    DesiredCapabilities capabilities = new DesiredCapabilities();
    		    capabilities.setCapability(CapabilityType.PROXY, selProxy);
    		    capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                //capabilities.setJavascriptEnabled(true);
                //capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {"--web-security=no", "--ignore-ssl-errors=yes"});

    		    
                LoggingPreferences loggingprefs = new LoggingPreferences();
                loggingprefs.enable(LogType.BROWSER, Level.ALL);
                capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);

                //driver = new PhantomJSDriver(capabilities);
                driver = new FirefoxDriver(capabilities);
                //driver = new ChromeDriver(capabilities);

                proxy = proxyTemp;
        		
        	} else {
        		driver = new FirefoxDriver();
        	}
    		
            return driver;
        }
        if (browserName.toLowerCase().contains("internet")) {
            driver = new InternetExplorerDriver();
            return driver;
        }
        if (browserName.toLowerCase().contains("chrome")) {
            driver = new ChromeDriver();
            return driver;
        }
        return driver;
    }
}
