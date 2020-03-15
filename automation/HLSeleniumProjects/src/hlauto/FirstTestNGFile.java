package hlauto;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
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

public class FirstTestNGFile {
	public String baseUrl = null;
	public WebDriver driver = new FirefoxDriver();
	public String test_environment = "stage";
	public String expected = null;
	public String actual = null;
	// public static final String USERNAME = "charleschoy1";
	// public static final String ACCESS_KEY = "vFsUZ2xNxifvbmKYgPCt";
	// public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@hub.browserstack.com/wd/hub";
	
	// private WebDriver driver; 
	
	public String setTestingEnv(String testing_env) {
		switch (testing_env) {
		case "stage": case "Stage":
			baseUrl = "http://sfc-stage02.healthline.com";
			break;
		case "qa": case "QA":
			baseUrl = "http://sfc-stage01.healthline.com";
			break;
		case "Prod": case "prod":
			baseUrl = "http://www.healthline.com";
			break;
		case "sfc-web01":
			baseUrl = "http://sfc-web01.healthline.com";
			break;
		case "njc-web01":
			baseUrl = "http://njc-web01.healthline.com";
			break;
		}
		return baseUrl;
	}
	
	public String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return (sdf.format(cal.getTime()));
	}
	
	public void output_options(List <WebElement> element) {
		for (WebElement dropdown : element) {
		    System.out.println(dropdown.getText());
		}
	}
	
	public void wait_for_element(String element_id) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element_id)));
	}
	
	public void verify_page(String expected_title) {
		expected = expected_title;
		actual = driver.getTitle();
		AssertJUnit.assertEquals(expected, actual);
	}
	
	public void hoverMouseOnTopicsAndTools() {
		String expected_title = "Symptom Checker : Check Your Medical Symptoms";
		driver.findElement(By.linkText("Symptom Checker")).click();
		verify_page(expected_title);
		WebElement element = driver.findElement(By.linkText("Topics & Tools"));
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		wait_for_element("box-health-topics");
	}
	
	public void selectFromTopicsAndTools(String link) {
		hoverMouseOnTopicsAndTools();
		WebElement health_topics = driver.findElement(By.id("box-health-topics"));
		WebElement health_tools = driver.findElement(By.id("box-health-tools"));
		switch (link) {
		case "Symptom Checker": case "BodyMaps": case "Clinical Trials": case "Pill Identifier": case "Doctor Search": case "Health News": case "Newsletter Sign-Up": case "Diabetes Mine":
			health_tools.findElement(By.linkText(link)).click();
			break;
		default:
			health_topics.findElement(By.linkText(link)).click();
			break;	
		}
	}
	
	@BeforeClass
/*	public void setUp() throws Exception {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browser", "Firefox");
		caps.setCapability("browser_version", "43.0");
		caps.setCapability("os", "Windows");
		caps.setCapability("os_version", "7"); 
		caps.setCapability("browserstack.debug", "true"); 		//This enable Visual Logs
		driver = new RemoteWebDriver(new URL(URL), caps);
	} */
	
	@BeforeTest
	public void launchBrowser() {
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(setTestingEnv(test_environment));
	}
	
	@BeforeMethod
	public void verifyHomepageTitle() {
		expected = "Medical Information & Trusted Health Advice: Healthline";
		verify_page(expected);
	}
	
	@Test(priority = 0)
	public void testTopicsAndToolsMenu() {
		hoverMouseOnTopicsAndTools();
		List<WebElement> health_topics = driver.findElements(By.id("box-health-topics"));
		// Print out the links on the health topics menu.
		output_options(health_topics);
		// Print out the links on the health tools menu.
		List<WebElement> health_tools = driver.findElements(By.id("box-health-tools"));
		output_options(health_tools);
	}
	
	@Test(priority = 1)
	public void testSelectTopicFromTopicsAndToolsMenu() {
		String expected_title = "GERD";
		selectFromTopicsAndTools("Acid Reflux");
		verify_page(expected_title);
	}
	
	@Test(priority = 1)
	public void testSelectToolFromTopicsAndToolsMenu() {
		String expected_title = "Human Body: Explore the Human Anatomy in 3D";
		selectFromTopicsAndTools("BodyMaps");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		verify_page(expected_title);
	}
	
	@Test(priority = 2)
	public void pillIdentifier() {
		driver.findElement(By.linkText("Pill Identifier")).click();
		expected = "Pill Identifier: Identify Drugs by Picture, Shape, Color, Number";
		verify_page(expected);
	}
	
	@Test(priority = 3)
	public void symptomChecker() {
		driver.findElement(By.linkText("Symptom Checker")).click();
		expected = "Symptom Checker : Check Your Medical Symptoms";
		verify_page(expected);
	}
	
	@AfterMethod
	public void goBackToHomePage() {
		driver.findElement(By.xpath("//img[contains(@src,'/resources/healthline/images/v3/hl-logo.png')]")).click();
		// driver.get(setTestingEnv(test_environment));
	}
	
	@AfterTest
	public void terminateBrower() {
		driver.quit();
	}
	
	 @AfterClass  
	 public void tearDown() throws Exception {  
		 driver.quit();  
	  }  
	
}
