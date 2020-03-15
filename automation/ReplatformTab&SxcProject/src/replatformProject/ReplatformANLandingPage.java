package replatformProject;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.assertTrue;
import org.testng.AssertJUnit;


import qalib.CheckAds;
import qalib.CheckHttpConnection;

import org.openqa.selenium.WebDriver;



public class ReplatformANLandingPage {
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
	public void ReplatformANTitleandDesc() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	String href = null;
	System.out
			.println("==================Title and Description Test Case Start==================");
	try{
		WebElement hamburger = driver.findElement(By.id("site-header")).findElement(By.className("css-cnt3tu")).findElement(By.className("header-menu"));
		hamburger.click();
		WebElement hammenu = driver.findElement(By.xpath(".//*[@class='css-akp3jh']/div/div/section[3]/ul/li[24]"));
		hammenu.click();
	// Check title
		try {
			WebElement title = locateElement('s', ".css-plckvd .css-1euiufy h1");
			isElementDisplayed(title, "Title",
					messages);
			
		} catch (Exception e) {
			messages.add("Title missing on AN Landing Page "
					+ driver.getCurrentUrl());
		}
		try {
			WebElement description = locateElement('s', ".css-plckvd .css-1euiufy h2");
			isElementDisplayed(description, "Description below Title",
					messages);
		} catch (Exception e) {
			messages.add("Description missing below title on Replatform AN Landing Page "
					+ driver.getCurrentUrl());
		}
	}
	catch (Exception e) {
		messages.add("Hamburger is not clickable from header "
				+ driver.getCurrentUrl());
	}
	finalAssert("Title and Description", messages);

}

	@Test(enabled = true, priority = 2, description = "In this test we are checking Horizontal Line exist below Description")
public void ReplatformANHorizontalLine() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Horizontal Line Test Case Start==================");
		// Check Horizontal
		try {
			WebElement title = locateElement('s', ".css-fnx8qn");
			isElementDisplayed(title, "Horizontal Line below Description", messages);
		} catch (Exception e) {
			messages.add("Horizontal Line missing below Description on Replatform AN Landing Page "
					+ e.toString());
		}
		finalAssert("Horizontal Line below Description", messages);
	}
	@Test(enabled = true, priority = 3, description = "In this test we are checking All Links are not broken"
		+ "\nChecked Images are not broken"
		+ "\nColor of a Article Title after mouse hover")
public void ReplatformANLandingarticlelinks() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	JavascriptExecutor jse = (JavascriptExecutor) driver;

	System.out
			.println("==================Centerwell AN Article Links Test Case Start==================");
	
	String href = null;
	try {
		List<WebElement> readnextlists = driver.findElement(
				By.className("css-1l95nvm")).findElements(
				By.className("css-1pyb8ul"));
		for (WebElement readnextlist : readnextlists) {
			WebElement lnk = locateElement('s', ".css-1uxu5gb .css-kkxwou");
		
			checkHttpStatus(lnk, "Links from listed AN Landing Page", messages);
			
		}
		System.out
				.println("All Links from Replatform AN Landing Page are not broken "
						+ driver.getCurrentUrl());
	} catch (Exception e) {
		messages.add("Not found or problem for Articles on Replatform AN Landing Page ");
	}
	// Check link color for Article Title 
	WebElement colorforarticletitle = locateElement('s', ".css-1uxu5gb .css-kkxwou .css-18vqdtt");
	// Mouse hover
	checkColorOnMouseHover(colorforarticletitle,
			"Article Title", messages);
	
	// Checked Images
	try {
		List<WebElement> imagelists = driver.findElement(
				By.className("css-1l95nvm")).findElements(
				By.className("css-s6ma3s"));
		for (WebElement imagelist : imagelists) {
			WebElement lnk = locateElement('s', ".css-kkxwou .css-1c7dhp5 .css-81ak43 img");
		
			checkHttpStatus(lnk, "Images from listed Article links", messages);
		}
		System.out
				.println("Images for listed Article links are not missing from AN Landing Page "
						+ driver.getCurrentUrl());
	} catch (Exception e) {
		messages.add("Not found or problem for Images on Replatform AN Landing ");
	}
	
	
	finalAssert("Centerwell AN Article Links", messages);
}
	@Test(enabled = true, priority = 4, description = "In this test we are checking Ads like Top LB, DCMR, Footer LB")
	public void ReplatformANAds() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		String message = "";
		String href = null;
		System.out
				.println("==================Ads Test Case Start==================");
		try {
			// Check Top DLB Ad & Stikiness for 3 seconds
			try {
				Thread.sleep(5000);
				CheckAds TopDlb = new CheckAds(driver);
				message = TopDlb.checkTopDlb();

				if (message != null) {

					messages.add("Top dlb Ad is missing..."
							+ driver.getCurrentUrl());
				} else {
					// Check label ADVERTISEMENT
					try {
						String googleQueryId = driver.findElement(By.id("dlb1__slot"))
								.getAttribute("data-google-query-id");
						String gogleItemId = driver.findElement(By.className("css-1im4ljs")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.className("css-1im4ljs")).getAttribute("data-creative-id");
						if (googleQueryId.isEmpty()) {
							messages.add("Top dlb Ad is not fired..."
									+ driver.getCurrentUrl());
						} else {
							System.out
									.println("Top dlb Ad is fired...google query id is --> "
											+ googleQueryId);
							System.out
							.println("Top dlb Ad is fired...google item id is --> "
									+ gogleItemId);
							System.out
							.println("Top dlb Ad is fired...google creative id is --> "
									+ googleCreativeId);
						}
					} catch (Exception e) {
						messages.add("Top dlb Ad is not fired..."
								+ driver.getCurrentUrl());
					}
					// Scroll page 155px
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,155)");

					// Check stickiness - 1st check that DLB is visible before
					// 3sec of scrolling 150px
					Thread.sleep(2900);

					try {
						WebElement stickyLayer = driver.findElement(By
								.className("css-1031od0"));
						// Check stickiness - if DLB is visible for 3seconds
						// than after 1 second check that it disappeared
						Thread.sleep(1000);
						try {
							stickyLayer = driver.findElement(By
									.className("css-jbcf6e"));
							System.out
									.println("Top dlb Ad disappeared after 3 seconds of scrolling 150px");
						} catch (Exception e) {
							messages.add("Top dlb Ad not disappeared after 3 seconds of scrolling 150px"
									+ driver.getCurrentUrl());
						}
					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				messages.add("Problem with Top dlb Ad... on Replatform AN Landing Page "
						+ e.toString());
			}
	// Check DCMRs
			try {
				String xpath = "//ul[@class='css-1l95nvm']/li";
				
				
				for (int i = 1; i < 5; i++) {
					((JavascriptExecutor) driver)
							.executeScript("window.scrollTo(0,document.body.scrollHeight);");
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,-155)");
					Thread.sleep(3000);
				}
				List<WebElement> dcmrs = driver.findElements(By.xpath(xpath));
			
				int j=1;
				for (int i = 1; i <= dcmrs.size()-10; i=i+2) {
					try {
					
						xpath = "//ul[@class='css-1l95nvm']/li["+ i + "]/section[@class='css-1kx3y31']/div[@class='css-1ca3fuk']/div[@class='css-1im4ljs']";
						Thread.sleep(5000);
						WebElement dcmrBlock=driver.findElement(By.xpath(xpath));
						String dcmr = dcmrBlock.findElement(By.id("dcmr" + j + "__slot")).getAttribute("data-google-query-id");
						String gogleItemId = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']//div[@class='css-1im4ljs']")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']//div[@class='css-1im4ljs']")).getAttribute("data-creative-id");
						if (dcmr.isEmpty()) {
							messages.add("DCMR"
									+ j
									+ " Ad at right rail on Replatform AN Landing Page is not fired.."
									+ driver.getCurrentUrl());
						} else {
							System.out
									.println("DCMR"	+ j	+ " ad is fired ...google query id is --> "+ dcmr);
							System.out
							.println("DCMR"
									+ j
									+ " ad is fired ...google item id is --> "+gogleItemId);
							System.out
							.println("DCMR"
									+ j
									+ " ad is fired ...google creative id is --> "+googleCreativeId);
						}
					} catch (Exception e) {
						messages.add("DCMR" + j
								+ " Ad is missing on Replatform AN Landing Page ");
					}
					j++;
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// Footer advertisement
			try {
				String adfooetrLbid = driver.findElement(
						By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				if (adfooetrLbid.isEmpty()) {
					messages.add("Footer dlb Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					System.out
							.println("Footer dlb Ad is fired...Advertisement id is --> "
									+ adfooetrLbid);
				}
			} catch (Exception e) {
				messages.add("Footer dlb Ad is not fired..."
						+ driver.getCurrentUrl());
			}

		} catch (Exception e) {
			messages.add("Replatform AN Landing Page Ads test case exception ..."
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
