/* This Script requires BrowserMobProxy
 * Be sure to set the testing environment: Stage01 and  
 * article: /health/cold-flu/sore-throat-natural-remedies in the 
 * Run Configurations, use the Key Value pairs.
 */
package hlauto;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.gargoylesoftware.htmlunit.javascript.host.Set;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
	
public class HarFileChrome {
	//System.setProperty("webdriver.chrome.driver", "/Users/rachnagupta/Documents/chromedriver/chromedriver" ); 
	//  String driverPath = "/Users/rachnagupta/Documents/chromedriver/";
	String sFileName = "C:\\ResultsHARfile\\SeleniumtestAdOps.har";        
	public WebDriver driver;
	public BrowserMobProxy proxy;
	static hlUtils hlu = new hlUtils();
	static Map<String, String> env;
	public String test_environment = null;
	public String baseUrl;
	public String articleUrl;
	
	@BeforeTest
	public void setUp() {
	            
		// start the proxy
	    proxy = new BrowserMobProxyServer();
	    proxy.start(0);
	    //get the Selenium proxy object - org.openqa.selenium.Proxy;
	    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
	    // configure it as a desired capability
	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);       
	    //set chromedriver system property
	    System.setProperty("webdriver.chrome.driver", "c:\\Chromedriver\\chromedriver.exe" ); 
	    driver = new ChromeDriver(capabilities);       
	    // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
	    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
	    // create a new HAR 
	    proxy.newHar("SeleniumtestAdOps");
	    env = System.getenv();
		test_environment = env.get("testing_environment");
		System.out.println("The test_environment is: " + test_environment);	
		baseUrl = hlu.setTestingEnv(test_environment);
		articleUrl = env.get("article_url");
	}
	        
	@Test (priority = 0)
	public void testLoadArticle() throws InterruptedException {

		driver.manage().window().maximize();
		// Output the start time of this script.
	    System.out.println("Start run date and time is: " + hlu.getDate());
	      
	    driver.manage().deleteAllCookies();
	    //Test URL (with lots of inline lbs and MRs)
	    driver.get(baseUrl + articleUrl);
	                  
	    String pagetitle = driver.getTitle();
	                    
	    if (!pagetitle.contains ("Achoo!")) {
	    	WebElement element = driver.findElement(By.id("dmr4__slot"));
	    	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	           
	        Thread.sleep(5000);
	                
	        WebElement element1 = driver.findElement(By.id("sourcesLink"));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element1);
	        Thread.sleep(5000);
	                                
	        ((JavascriptExecutor)driver).executeScript("scroll(0,document.body.scrollHeight)");
	        Thread.sleep(5000);
	                           
	    } else {
	        System.out.print("\n Page is 404: " + pagetitle );        
	    }
	            
	                     
	}
	
	@Test (priority = 1)
	public void testGetHarFile() {
		// get the HAR data
	    Har har = proxy.getHar();
	    // Write HAR Data in a File
	    File harFile = new File(sFileName);
	    try {
	    	har.writeTo(harFile);
	    } catch (IOException ex) {
	    	System.out.println (ex.toString());
	    	System.out.println("Could not find file " + sFileName);
	    }
	}
	
	@Test (priority = 2)
	public void testParseHarFile() {
		hlu.parseHarFile(sFileName);
	}	
	        
	@AfterTest 
	public void tearDown() {
		if (driver != null) {
			proxy.stop();
			driver.quit();
		}
		System.out.println("End run date and time is: " + hlu.getDate());
	}    
}
