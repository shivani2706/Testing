package hlauto;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class HLNewsletterTest extends hlauto.hlUtils {
	public WebDriver driver;
	static Map<String, String> env;
	public String test_environment = null;
	public String actual = null;
	static hlUtils hlu = new hlUtils();
	static int waitTime = 8000;
	public String baseUrl;

	
	public void wait_for_element(String element_id) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element_id)));
	}
	
	public void verify_page(String expected_title) {
		String expected = null;
		String actual = null;
		
		expected = expected_title;
		actual = driver.getTitle();
		AssertJUnit.assertEquals(expected, actual);
	}
	
	public boolean verifyTextPresent(String value) {
		return driver.getPageSource().contains(value);
	}
	
	@BeforeTest
	public void setUp() {
		driver = new FirefoxDriver();
		env = System.getenv();
		test_environment = env.get("testing_environment");
		System.out.println("The test_environment is: " + test_environment);	
		baseUrl = hlu.setTestingEnv(test_environment);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();		
	}
	
	@Test(priority = 0) 
	public void testNewsletterWidget() throws InterruptedException {
		String homePageTitle = "Medical Information & Trusted Health Advice: Healthline";
		String pageTitle = "Sign up for newsletters ";
		String newsletter = "Fibromyalgia";		
		String newsLetterSelectedText = null;
		
		try {
			driver.get(baseUrl);
			Thread.sleep(waitTime);
			// hlu.wait_for_element("logo");
			verify_page(homePageTitle);
			// Click on the Check out our other newsletters link.
			System.out.println("Now clicking on the \'Check out our other newsletters link\'");
			driver.get("http://www.healthline.com/health/newsletter-signup?ref=global"); // I loaded the url directly, since the click on link didn't work - might be due to the old plugins I was trying for GA.
			// driver.findElement(By.linkText("Check out our other newsletters")).click();
			// driver.findElement(By.xpath(".//*[@id='hl']/div[1]/div/div/div/div/div/div/div/div/div/div/div/div[2]/div/div[2]/div[2]/div[2]/ul/li[3]/div/h5/a")).click();
			Thread.sleep(waitTime);
			// Verify the Header on the page.
			AssertJUnit.assertTrue(verifyTextPresent(pageTitle));
			// Enter email address.
			driver.findElement(By.id("your_email2")).sendKeys("cchoy@healthline.com");
			// Click on the submit button.
			driver.findElement(By.id("next-btn")).click();
			Thread.sleep(waitTime);
			// Verify the selected newsletter appears on the Sign up widget.
			newsLetterSelectedText = driver.findElement(By.xpath("//form[@id='contact']/div/div/div[11]/div")).getText();
			AssertJUnit.assertEquals(newsletter, newsLetterSelectedText);
			System.out.println("The value of newsLetterSelectedText: " + newsLetterSelectedText);
			// Click on the Fibromyalgia check box.
			driver.findElement(By.xpath("//form[@id='contact']/div/div/div[11]/div")).click();
			// Click on the Sign Up button.
			driver.findElement(By.id("submit3")).click();
			// Verify the response on the page.
			Thread.sleep(waitTime);
			AssertJUnit.assertTrue(verifyTextPresent("Now we're in"));
			AssertJUnit.assertTrue(verifyTextPresent("this together."));
		} catch (Exception e) {
			e.printStackTrace();	
		}	
	}
	
	@Test(priority = 1)
	public void testSailThru() throws InterruptedException {
		
		try {
			driver.get(hlu.getSailThruUrl(test_environment));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterTest
	public void terminateBrower() {
		driver.quit();
	}
}
