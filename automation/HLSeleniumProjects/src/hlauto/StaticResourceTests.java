/*
 * Author: chaz
 * Date: 07.28.17
 * This script needs to have eclipse launched as an administrator user
 * since the hosts file needs to be updated for the Static Resource testing.
 */
package hlauto;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.BeforeClass;
import edu.umass.cs.benchlab.har.*;
import edu.umass.cs.benchlab.har.tools.HarFileReader;
import edu.umass.cs.benchlab.har.tools.HarFileWriter;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Reporter;
import com.google.gson.JsonParseException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class StaticResourceTests {
	static Map<String, String> env;
	static String driverType;
	static String protocol = "http://www.";
	static String domain = "healthline.com";
	static hlUtils hlu = new hlUtils();
	private static WebDriver driver;
	static String htmlSrc = "";
	static String testUrl = "";
	static String article = "/health/anxiety-complications";
	static final LinkedHashMap<String, String> serverIP = createMap();
	private static LinkedHashMap<String, String> createMap() {
		LinkedHashMap<String, String> tmpMap = new LinkedHashMap<String, String>();
		tmpMap.put("sfc-web01", "64.84.32.3");
		tmpMap.put("sfc-web02", "64.84.32.4");
		tmpMap.put("sfc-web03", "64.84.32.5");
		tmpMap.put("sfc-web04", "64.84.32.6");
		tmpMap.put("sfc-web05", "64.84.32.7");
		tmpMap.put("sfc-web07", "64.84.32.8");
		tmpMap.put("sfc-web08", "64.84.32.9");
		tmpMap.put("sfc-web09", "64.84.32.10");
		tmpMap.put("njc-web01", "75.98.71.20");
		tmpMap.put("njc-web02", "75.98.71.21");
		tmpMap.put("njc-web03", "75.98.71.22");
		tmpMap.put("njc-web04", "75.98.71.23");
		tmpMap.put("njc-web05", "75.98.71.24");
		tmpMap.put("njc-web06", "75.98.71.25");
		tmpMap.put("njc-web07", "75.98.71.67");
		tmpMap.put("njc-web08", "75.98.71.61");
		return tmpMap;	
	}
	public static int waittime = 8000;
	
	public WebDriver createInstance() {
		System.setProperty("webdriver.chrome.driver", "C:\\Chromedriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		return driver;
	}
	
	@BeforeTest (enabled = false)
	public void setUp() {
		env = System.getenv();
		driverType = env.get("DRIVER");
		
		switch (driverType) {
		case "chrome": case "Chrome":
			System.setProperty("webdriver.chrome.driver", "C:\\Chromedriver\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--incognito");
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(capabilities);
			break;
		case "firefox": case "Firefox":
			driver = new FirefoxDriver();
			break;
		case "ie": case "IE":
			System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			break;
		default:
			System.out.println("Unknown driver, check parameter...");
			break;	
		}
	}
	
	@Test (priority = 1)
	public void testServers() throws InterruptedException {
		serverIP.forEach((k,v)-> {
			System.out.println("Now updating hosts file with ip number: " + v);
			hlu.updateHostFile(v);
			System.out.println("Now testing server: " + k + " ip number: " + v);
			testUrl = protocol + domain + article;
			try {
				WebDriver driver = null;
				driver = createInstance();
				driver.get(testUrl);
				Thread.sleep(waittime);
				htmlSrc = driver.getPageSource();
				// System.out.println("HTML Source ->");
				// System.out.println(htmlSrc);
				hlu.patternMatchServer(k, htmlSrc);
				driver.close();
				driver.quit();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void endWebDriver() {
		driver.quit();
	}
}
