package replatformProject;
import java.io.File;
import java.util.Map;
import static java.util.Arrays.asList;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.junit.Ignore;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.MouseAction;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.assertTrue;
import org.testng.AssertJUnit;


import qalib.CheckAds;
import qalib.CheckHttpConnection;
import qalib.SelectRandomURL;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.webkitRTCPeerConnection;

public class ReplatformSxCLandingPage {

static Map<String, String> env;
static String driverType;
private static String baseURL;

private static int linksCount = 0;
private static String[] links = null;

private static WebDriver driver;
public static int waittime = 2000;
static int dayOfWeek;
static int dayOfMonth;

int rnd;

@BeforeClass
public static void startWebDriver() {

	env = System.getenv();
	driverType = env.get("DRIVER");
	baseURL = env.get("URL");
	System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
	driver = new ChromeDriver();
	driver.manage().window().maximize();
	driver.get(baseURL + "/health/alp");
}

@BeforeMethod
public void clearDriver() {
	driver.manage().deleteAllCookies();
}

public void isElementDisplayed(WebElement elem, String Componant,
		List<String> messages) {
	if (!(elem.isDisplayed())) {
		messages.add(Componant + " is not displayed on "
				+ driver.getCurrentUrl());

	} else {
		System.out.println(Componant + " is displayed on "
				+ driver.getCurrentUrl());
	}
}

public WebElement locateElement(char tagType, String elementName) {
	WebElement el = null;

	switch (tagType) {
	case 'i':
		el = new WebDriverWait(driver, 15).until(ExpectedConditions
				.visibilityOfElementLocated(By.id(elementName)));
		break;
	case 'c':
		el = new WebDriverWait(driver, 15).until(ExpectedConditions
				.visibilityOfElementLocated(By.className(elementName)));
		break;
	case 'x':
		el = new WebDriverWait(driver, 15).until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(elementName)));
		break;
	case 's':
		el = new WebDriverWait(driver, 15).until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(elementName)));
		break;
	default:
		el = new WebDriverWait(driver, 15).until(ExpectedConditions
				.visibilityOfElementLocated(By.tagName(elementName)));
	}

	return el;
}

public void finalAssert(String testcase, List<String> messages) {
	String dispMessage = "";
	if (messages.size() > 0) {
		for (String msg : messages) {
			dispMessage = dispMessage + msg + "\n";
		}
	}
	AssertJUnit.assertTrue(dispMessage, dispMessage == "");
	System.out.println("==================" + testcase
			+ " Test Case End==================");
}

public void checkColorOnMouseHover(WebElement elem, String Componant,
		List<String> messages) {
	// Mouse hover on nav link
	Actions action = new Actions(driver);
	action.moveToElement(elem).perform();
	try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("Color of " + Componant + " after mouse hover: "
			+ elem.getCssValue("color"));
	if(elem.getCssValue("color").indexOf("rgba(236, 28, 130")<0){
	messages.add(Componant+" color not changed to pink on mouse hover on "+ driver.getCurrentUrl());	
}
}

public void SwitchToPopup() {
	String subWindowHandler = null;
	Set<String> handles = driver.getWindowHandles(); // get all window
														// handles
	Iterator<String> iterator = handles.iterator();
	while (iterator.hasNext()) {
		subWindowHandler = iterator.next();
	}
	driver.switchTo().window(subWindowHandler);
}

public void checkHttpStatus(WebElement elem, String testcase,
		List<String> messages) {
	String href = elem.getAttribute("href");
	if (href != null) {
		CheckHttpConnection urlHttpConn = new CheckHttpConnection(href);
		int status = urlHttpConn.testHttpConn();

		if (status != 200 && status != 301) {
			messages.add(href + " Broken links inside " + testcase
					+ " Status-->" + status);
		}
	}
}
@Test(enabled = true, priority = 1, description = "In this test we are checking H1 tag(title) and H2 tag(Description) exist")
public void ReplatformSxCTitleandDesc() throws InterruptedException {
List<String> messages = new ArrayList<String>();
String href = null;
System.out
		.println("==================Title and Description Test Case Start==================");
	try{
		WebElement hamburger = driver.findElement(By.id("site-header")).findElement(By.className("css-cnt3tu")).findElement(By.className("header-menu"));
		hamburger.click();
		WebElement hammenu = driver.findElement(By.xpath(".//*[@class='css-akp3jh']/div/div/section[4]/ul/li[1]/a"));
		hammenu.click();
	
// Check title
	try {
		WebElement title = locateElement('s', ".css-1r63ggk .css-1euiufy h1");
		isElementDisplayed(title, "Title",
				messages);
		
	} catch (Exception e) {
		messages.add("Title missing on SxC Landing Page "
				+ driver.getCurrentUrl());
	}
	try {
		WebElement description = locateElement('s', ".css-1r63ggk .css-1euiufy h2");
		isElementDisplayed(description, "Description below Title",
				messages);
	} catch (Exception e) {
		messages.add("Description missing below title on Replatform SxC Landing Page "
				+ driver.getCurrentUrl());
	}
	}
	catch (Exception e) {
		messages.add("Hamburger is not clickable from header "
				+ driver.getCurrentUrl());
	}
finalAssert("Title and Description", messages);


}
@Test(enabled = true, priority = 2, description = "In this test we are checking SxC Search Widget exist"+ "\nChecked text box and button"+ "\nEnter symptom and click button"+ "\nChecked SxC result page and back to Landing page.")
public void ReplatformSxCSearchWidget() throws InterruptedException {
List<String> messages = new ArrayList<String>();
String href = null;
System.out
		.println("==================SxC Search Widget Test Case Start==================");
// Check Search Widget
	try {
		WebElement title = locateElement('s', ".css-1r63ggk .css-wks7f2");
		isElementDisplayed(title, "SxC Search Widget",
				messages);
		
		// Check Text box
		WebElement txtbox = driver.findElement(By.xpath(".//div[@class='css-1r63ggk']//form[@class='css-wks7f2']/div[@class='css-1fk7trd']"));
		isElementDisplayed(txtbox, "Text box", messages);
		
		// Check Subscribe button
		WebElement send = driver.findElement(By.xpath(".//div[@class='css-1r63ggk']//form[@class='css-wks7f2']/input[@class='css-umv194']"));
		isElementDisplayed(send, "'SEE POSSIBLE CAUSES' button", messages);
		
		// Enter few letters and select any
		WebElement entertxt = driver.findElement(By.className("css-1fk7trd")).findElement(By.className("autocomplete-wrapper")).findElement(By.className("autocomplete"));
		entertxt.clear();
		entertxt.sendKeys("as");
		Thread.sleep(5000);
		WebElement drpdon = driver.findElement(By.className("css-1iziur3")).findElement(By.className("css-1eelrvt"));
		drpdon.click();
		System.out.println("Click Symptom link from dropdown and redirected to "+ driver.getCurrentUrl());		
		try {
			Thread.sleep(2000);
			WebElement articleContent = locateElement('s', ".css-1sec1gm");
			if (articleContent.getText() == "") {
				messages.add(" Content missing on "
						+ driver.getCurrentUrl());
			}
			System.out.println("Content exist on Replatform SxC Result "
					+ driver.getCurrentUrl());
		} catch (Exception e) {
			messages.add("Content missing on "
					+ driver.getCurrentUrl()+ e.toString());
		}
		driver.navigate().back();
		Thread.sleep(waittime);
	} catch (Exception e) {
		messages.add("SxC Search Widget missing on SxC Landing Page "
				+ driver.getCurrentUrl());
	}

finalAssert("SxC Search Widget", messages);


}
@Test(enabled = true, priority = 3, description = "In this test we are checking All Links are not broken"
	+ "\nChecked Images are not broken"
	+ "\nColor of a Article Title after mouse hover")
public void ReplatformSxCLandingarticlelinks() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	JavascriptExecutor jse = (JavascriptExecutor) driver;

	System.out
			.println("=================='Commonly searched symptoms' Test Case Start==================");
	
	String href = null;
	try {
		List<WebElement> symptomslists = driver.findElement(
				By.className("css-2hyak")).findElement(By.className("css-g2l2jy")).findElements(
				By.className("css-1l4c1lj"));
		for (WebElement symptomslist : symptomslists) {
			WebElement lnk = locateElement('s', ".css-q6l7mk");
		
			checkHttpStatus(lnk, "Checked links from 'Commonly searched symptoms' Section", messages);
		}
		System.out
				.println("All Links from 'Commonly searched symptoms' Section are not broken "
						+ driver.getCurrentUrl());
		} catch (Exception e) {
		messages.add("Not found or problem for 'Commonly searched symptoms' Section");
	}
		finalAssert("'Commonly searched symptoms' Section Links", messages);
}
@Test(enabled = true, priority = 4, description = "In this test we are checking Ad like DMR")
public void ReplatformSxCAds() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	JavascriptExecutor jse = (JavascriptExecutor) driver;

	String message = "";
	String href = null;
	System.out
			.println("==================Ads Test Case Start==================");
	try {
	// Check DMR
		
		try {

			WebElement mrAdSlot = driver
									.findElement(By.className("css-1kx3y31")).findElement(By.className("css-1ca3fuk")).findElement(By.id("dmr1__slot"));
		
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", mrAdSlot);
			jse.executeScript("window.scrollBy(0,-150)", "");
			if (!(mrAdSlot.isDisplayed())) {
				System.out
				.println("DMR1 Ad is Not displayed  "
						+ message);
			}
			String googleQueryId=new WebDriverWait(driver, 25)
			.until(ExpectedConditions.visibilityOfElementLocated(By.id("dmr1__slot"))).getAttribute("data-google-query-id");
			String gogleItemId = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']//div[@class='css-1im4ljs']")).getAttribute("data-line-item-id");
			String googleCreativeId = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']//div[@class='css-1im4ljs']")).getAttribute("data-creative-id");
			if(googleQueryId.isEmpty()){
				System.out
				.println("DMR1 Ad is not fired... ");
			}else{
				
				System.out
				.println("DMR1 Ad is fired ...Advertisement id is --> "+googleQueryId);
				System.out
				.println("DMR1 ad is fired ...google item id is --> "+gogleItemId);
				System.out
				.println("DMR1 ad is fired ...google item id is --> "+googleCreativeId);
				
			}
		} catch (Exception e) {
			messages.add("DMR1 Ad is Not displayed "
					+ driver.getCurrentUrl());
		}

		} catch (Exception e) {
		messages.add("Replatform SxC Landing Page Ads test case exception ..."
				+ e.toString());
	}
	finalAssert("Ads", messages);
}
@Test(enabled = true, priority = 5, description = "In This test we are checking Meta Title & Canonical link from view sources.")
public void ReplatformViewSource() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	JavascriptExecutor jse = (JavascriptExecutor) driver;
	System.out
			.println("==================ViewSource Test Case Start==================");
	// Check Title from view source
		try{
		
		String Source = driver.getPageSource();
		String page_title; 
		page_title = driver.getTitle();
		System.out.println("Page Title: "+ page_title);
		}
		catch (Exception e) {
			System.out.println("Title not found from view sources "+ driver.getCurrentUrl());
		}
			// Check 'Canonical Link' from view source
				try{
					String Source = driver.getPageSource( );
					String xpath = "//link[@rel='canonical']";
					WebElement meta = driver.findElement((By.xpath(xpath)));
					String metalink = meta.getAttribute("href");
					System.out.println("Canonical Link: "+ metalink);
				}
				catch (Exception e) {
						System.out.println("Canonical link not found from view sources "+driver.getCurrentUrl());
					}
	
				finalAssert("ViewSource", messages);
}
@Test(enabled = true, priority = 6, description = "In This test we are checking 'Comscore' tag for beacon.js from view sources.")
public void ReplatformComscore() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	JavascriptExecutor jse = (JavascriptExecutor) driver;
	System.out
			.println("=================='Comscore' tag Test Case Start==================");
	
		// Check 'beacon.js' from view source
		try{
			String Source = driver.getPageSource();
			String xpath = "//script[@src='https://sb.scorecardresearch.com/beacon.js']";
			
			WebElement meta = driver.findElement((By.xpath(xpath)));
			String metaname = meta.getAttribute("src");
			System.out.println("'Comscore' tag: "+ metaname);
			}
			catch (Exception e) {
				System.out.println("'Comscore' tag is not found from view sources "+driver.getCurrentUrl());
			}
		finalAssert("'Comscore' tag", messages);
}
@AfterClass
public static void endWebDriver() {
	driver.quit();
}
}

