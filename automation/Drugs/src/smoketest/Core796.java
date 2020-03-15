package smoketest;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.openqa.selenium.Keys;
import qalib.CheckHttpConnection;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import qalib.SelectRandomURL;

public class Core796 {
	
	
	//static String driverType;
	private static String baseURL;
	
	private static WebDriver driver;
	public static int waittime = 8000;
	@BeforeClass
	public static void startWebDriver() {
		//baseURL = "http://healthline:h34lthl1n3@www.drugs.com/health/testing/generic-9/?url=http://hl-qa.drugs.com";
		//baseURL = "http://www.drugs.com/health/testing/generic-9/?url=http://hl-qa.drugs.com";
		//baseURL = "http://trs:radioshack@staging.livestrong.com/healthline";
		
		//baseURL = "http://hl.drugs.com/";
		baseURL="http://hl-stage.livestrong.com";
		System.setProperty("webdriver.chrome.driver", "E:\\chromedriver.exe");

	driver = new ChromeDriver();
	//driver.get(baseURL);
	driver.manage().window().maximize();
	
	
	}

	@Before
	public void clearDriver() {
		driver.manage().deleteAllCookies();
	}
	@Test
	public void HitUrls()throws InterruptedException {	
		
		try{
			
			
			driver.navigate().to(baseURL + "/health/hypothyroidism/diet-plan");
			//driver.navigate().to(baseURL + "/health/multiple-sclerosis/beating-fatigue");
			System.out.println("Default");
			for(int i=1;i<=100;i++){
				
			
				driver.navigate().to(baseURL + "/health/hypothyroidism/diet-plan"); 
				//driver.navigate().to(baseURL + "/health/multiple-sclerosis/beating-fatigue");
				Thread.sleep(10000);
			  
			 // driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
			  //driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			  System.out.println(i+ " Times...");
			}
			
		}catch (Exception e) {
	System.out.println(e);// TODO: handle exception
		}
	}
	@AfterClass
	public static void endWebDriver() {
		driver.quit();
	}
}
