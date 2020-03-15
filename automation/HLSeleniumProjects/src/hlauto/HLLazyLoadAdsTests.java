package hlauto;

import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.testng.AssertJUnit;
import org.testng.annotations.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class HLLazyLoadAdsTests extends hlauto.hlUtils {
	static WebDriver driver;
	static Map<String, String> env;
	public String test_environment = null;
	public String actual = null;
	static hlUtils hlu = new hlUtils();
	static int waitTime = 4000;
	public String baseUrl;
	public String ABTestArticle = "/health/raised-skin-bump-v1";
	
	public static boolean isClickable(WebElement webe) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5, 25);
			wait.until(ExpectedConditions.elementToBeClickable(webe));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@BeforeTest
	public void setUp() {
		driver = new FirefoxDriver();
		env = System.getenv();
		test_environment = env.get("testing_environment");
		System.out.println("The test_environment is: " + test_environment);	
		baseUrl = hlu.setTestingEnv(test_environment) + ABTestArticle;
		// baseUrl = "http://sfc-stage02.healthline.com";
		// driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test(priority = 0) 
	public void testLazyLoadAdMR1() throws InterruptedException {
		try {
			driver.get(baseUrl);
			Thread.sleep(waitTime);
			// driver.findElement(By.xpath(".//*[@id='mr1Block1']/div[1]/div/div[1]")).click();
			while (isClickable(driver.findElement(By.xpath(".//*[@id='mr1Block1']/div[1]/div/div[1]"))) != true ) {
				isClickable(driver.findElement(By.xpath(".//*[@id='mr1Block1']/div[1]/div/div[1]")));
				System.out.println("MR1 is still loading...");
			}
			
		} catch (Exception e) {
			e.printStackTrace();	
		}		
	}
	
	@Test(priority = 1)
	public void testLazyLoadAdLB() throws InterruptedException {
		try {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,700)", "");
			Thread.sleep(waitTime);
			driver.findElement(By.xpath(".//*[@id='gpt-ad-lb-4']")).click();
			
		} catch (Exception e) {
			e.printStackTrace();	
		}	
	}
	
	@Test(priority = 2)
	public void testLazyLoadAdMR2() throws InterruptedException {
		try {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,350)", "");
			Thread.sleep(waitTime);
			driver.findElement(By.xpath(".//*[@id='mr1Block2']/div[1]/div/div[1]")).click();
		} catch (Exception e) {
		e.printStackTrace();	
		}
	}
	
	@Test(priority = 3)
	public void testLazyLoadAdMR3() throws InterruptedException {
		try {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,350)", "");
			Thread.sleep(waitTime);
			driver.findElement(By.xpath(".//*[@id='mr1Block3']/div[1]/div/div[1]")).click();
		} catch (Exception e) {
		e.printStackTrace();	
		}
	}

	@AfterTest
	public void terminateBrower() {
		driver.quit();
	}
}