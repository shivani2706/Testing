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
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.assertTrue;
import org.testng.AssertJUnit;

import qalib.CheckAds;
import qalib.CheckHttpConnection;
import qalib.SelectRandomURL;
import org.openqa.selenium.WebDriver;

import com.google.sitebricks.client.Web;
import com.sun.org.apache.xerces.internal.util.Status;
import com.thoughtworks.selenium.webdriven.commands.GetText;

import bsh.commands.dir;

public class RepaltformSponsoredbarOnArticlewithLandingPage {
	static Map<String, String> env;
	static String driverType;
	private static String baseURL;
	private static String SponsoreBarPreprod;
	private static String SponsoreBarStage;
	private static String envType;
	private static String preprod = "https//frontend-preprod.healthline.com";
	private static String stage = "https//frontend-stage.healthline.com";
	private static String domain;
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
		domain=baseURL;
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		SponsoreBarPreprod=env.get("SponsoreBarPreprod");
		SponsoreBarStage=env.get("SponsoreBarStage");
	
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
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Color of " + Componant + " after mouse hover: "
				+ elem.getCssValue("color"));
		if (elem.getCssValue("color").indexOf("rgba(236, 28, 130") < 0) {
			messages.add(Componant
					+ " color not changed to pink on mouse hover on "
					+ driver.getCurrentUrl());
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

	@Test(enabled = true, priority = 1, description = "In this test we are checking Sponsored exist")
	public void Sponsored() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		try {
			if (baseURL.contains("frontend-stage.healthline.com")) {
				SponsoreBarStage = "SponsoreBarStage.txt";
				SelectRandomURL s= new SelectRandomURL();
				String url = s.choose(new File(SponsoreBarStage));
				driver.get(baseURL + url);

			} else if (baseURL.contains("frontend-preprod.healthline.com")) {
				SponsoreBarPreprod = "SponsoreBarPreprod.txt";
				SelectRandomURL s = new SelectRandomURL();
				String url = s.choose(new File(SponsoreBarPreprod));
				driver.get(baseURL + url);
			}
			
			 Thread.sleep(2000);   
		System.out
				.println("==================Sponsoredbar Test Case Start==================");
		
		
		// Check sponseredbar
		try {
			WebElement Sponsoredbar = locateElement('s', ".css-ez87q1");
			isElementDisplayed(Sponsoredbar, "Sponsoredbar", messages);
		} catch (Exception e) {
			messages.add("Sponsoredbar missing on Replatform Article "
					+ driver.getCurrentUrl() + e.toString());
		}
		}
		catch (Exception e) {
			messages.add("Problem on Sponsoredbar Article "
					+ driver.getCurrentUrl() + e.toString());
		}
		finalAssert("Sponsoredbar", messages);
	}

	@Test(enabled = true, priority = 2, description = "In this test we are checking Sponsored Content exist")
	public void ReplatformSponsorContent() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Sponsored Content Test Case Start==================");
		// Check Center Content
		try {
			
			WebElement content = driver.findElement(By.xpath("//div[@class='css-o44is']/div[@class='css-ez87q1']/div"));
			isElementDisplayed(content, "Sponsor Content in Sponsoredbar",
					messages);
		} catch (Exception e) {
			messages.add("Sponsor Content missing in Sponsoredbar on Article "
					+ e.toString());
		}
		try {
			WebElement Sponsortitle= driver.findElement(By.xpath("//div[@class='css-o44is']/div[@class='css-ez87q1']/div/div/div/div/a"));
			isElementDisplayed(Sponsortitle, "Title in Sponsoredbar",
					messages);
			Thread.sleep(2000);
			checkColorOnMouseHover(Sponsortitle,
					"Title", messages);
		} catch (Exception e) {
			messages.add("Title missing in Sponsoredbar on Article "
					+ e.toString());
		}
		finalAssert("Sponsored Content", messages);
	}

	@Test(enabled = true, priority = 3, description = "In this test we are checking Sponsored Thumbnail exist")
	public void ReplatformSponsorThumb() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Thumbnail Test Case Start==================");
		// Check thumb
		try {
			WebElement thumb= driver.findElement(By.xpath("//div[@class='css-o44is']/div[@class='css-ez87q1']/div/div/a/img"));
			if (thumb != null){
			isElementDisplayed(thumb, "Thumbnail in Sponsoredbar", messages);
			 CheckHttpConnection urlHttpConn = new CheckHttpConnection(thumb.getAttribute("src"));
             int status = urlHttpConn.testHttpConn();
             if(status==200){
           }
			else{
				
			}
           }
		} catch (Exception e) {
			messages.add("Thumbnail missing in Sponsoredbar on Article "
					+ e.toString());
		}
		finalAssert("Thumbnail", messages);
	}

	@Test(enabled = true, priority = 4, description = "In this test we are checking Sponsored Logo exist")
	public void ReplatformSponsorLogo() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		//System.out.println("==================Logo Test Case Start==================");
		// Check Logo
		try {
			WebElement Logo =null;
			Logo= driver.findElement(By.xpath("//div[@class='css-o44is']/div[@class='css-ez87q1']/div/a/img"));
			if (Logo != null)
			{
			          CheckHttpConnection urlHttpConn = new CheckHttpConnection(Logo.getAttribute("src"));
		              int status = urlHttpConn.testHttpConn();
		              if(status==200){
		              }else {
		            	  messages.add("Logo missing in Sponsoredbar on Article ");
					}
			}
		} catch (Exception e) {
			
		}
	}

	@Test(enabled = true, priority = 5, description = "In this test we are checking 'Taking Charge of Vaginal Health' title, logo, thumb, learn more link in sponsored bar"
			+ "\nLearn More link click and open the page the back to page")
	public void ReplatformLearnMore() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Learn More link Test Case Start==================");
		// Learn More link
		try {
			WebElement LearnMore= driver.findElement(By.xpath("//div[@class='css-ez87q1']/div/div/div/span/a"));
			isElementDisplayed(LearnMore, "'Learn more' link", messages);

			// Check Learn More link color
			Thread.sleep(2000);
			checkColorOnMouseHover(LearnMore, "'Learn more' link", messages);

			// Learn More link click and open the page the back to page
			LearnMore.click();
			try {
				WebElement articleH1 = locateElement('s', ".banner_cont h1");
				if (articleH1.getText() == "") {
					messages.add("Content missing on " + driver.getCurrentUrl());
				} else {
					System.out.println("Content exist on About Us Page "
							+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				messages.add("Content missing on " + driver.getCurrentUrl()
						+ e.toString());
			}

		driver.navigate().back();
		} catch (Exception e) {
			messages.add("Learn More link is Not displayed on Article "
					+ e.toString());
		}
		finalAssert("Learn More link", messages);
	}
	
	@Test(enabled = true, priority = 6, description = "In this test we are checking H1 tag(title) exist")
	public void ReplatformArtilceTitle() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================Title Test Case Start==================");
		// Check Title
		try {
			WebElement title = driver.findElement(By.className("h1"));
			isElementDisplayed(title, "Title", messages);
		} catch (Exception e) {
			messages.add("Title missing on Article "
					+ e.toString());
		}
		finalAssert("Title", messages);
	}
	@Test(enabled = true, priority = 7, description = "In this test we are checking Center Content exist Below landscape Image")
	public void ReplatformArtilceCenterContent() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Center Content Test Case Start==================");
		// Check Center Content
		try {
			WebElement content = locateElement('s', ".css-1tkhpnj");
			isElementDisplayed(content,
					"Center Content ", messages);
		} catch (Exception e) {
			messages.add("Center Content missing on Article "
					+ e.toString());
		}
		finalAssert("Center Content", messages);
	}
	@Test(enabled = true, priority = 8, description = "In this test we are checking Sharebar exist"
		+ "\nColor of FB, Twitter, Print, feedback smile & frown icons before & after mouse hover"
		+ "\nClick FB, Twitter & Email icons"+ "\nClick feedback smile & frown icons and check sublinks from overlay.Click 1st link from Smile overlay and closed it.")
public void ReplatformArtilceShareTools() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	JavascriptExecutor jse = (JavascriptExecutor) driver;

	System.out
			.println("==================Sharebar Test Case Start==================");
	// Check Share Tools
	try {
		WebElement content = locateElement('s', ".css-xsfqmw .css-1bh05wg");
		isElementDisplayed(content,
				"Sharebar & Feedback", messages);
	} catch (Exception e) {
		messages.add("Sharebar & Feedback missing on Article "
				+ e.toString());
	}
	
	finalAssert("Sharebar & Feedback", messages);
}


@Test(enabled = true, priority = 9, description = "In This test we are checking Byline exist at RHS")
public void ReplatformArtilceByline() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	JavascriptExecutor jse = (JavascriptExecutor) driver;

	System.out
			.println("==================Byline Test Case Start==================");
	// Check Byline at RHS
	try {
		
		WebElement byline = locateElement('s', ".css-1kx3y31 .css-1al5k4j");
		isElementDisplayed(byline, "Byline at RHS", messages);
	} catch (Exception e) {
		messages.add("Byline is missing at RHS on Article "
				+ driver.getCurrentUrl());
	}
	finalAssert("Byline", messages);
}
	@Test(enabled = true, priority = 10, description = ""
			+ "\n From more in widget checked title and View All link and click it"
			+ "\nCheck content from Landing Page for all widgets.")
	public void ReplatformMorein() throws InterruptedException {
		try {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================More in Section Test Case Start==================");
		// Checked more in section with title
		// at RHS
		try {
			WebElement morein = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']/div[2]/div/h2"));
			isElementDisplayed(morein,
					"Title for more in section at RHS", messages);
			Thread.sleep(2000);
			} catch (Exception e) {
			messages.add("Title is missing at RHS on Replatform Article "
					+ e.toString());
		}
		// Checked 'View all' link in 'More in' section
		try {
			WebElement Viewalllink = driver.findElement(By.className("css-1kx3y31")).findElement(By.linkText("View all"));
			isElementDisplayed(Viewalllink, "'View all' link at RHS", messages);
			Thread.sleep(2000);
			checkColorOnMouseHover(Viewalllink, "'View all' link at RHS", messages);

			Viewalllink.click();
			// 'View all' link click and open the page the back to page
			System.out.println("::Landing Page::");
			
			// Check Top DLB Ad & Stikiness for 3 seconds
			String message = "";
			try {
				Thread.sleep(5000);
				CheckAds TopDlb = new CheckAds(driver);
				message = TopDlb.checkTopDlb();
				if (message != null) {
					messages.add("Top DLB Ad is missing..."
							+ driver.getCurrentUrl());
				} else
				{
					// Check label ADVERTISEMENT
					try {
						String adLbid = driver.findElement(By.id("dlb1__slot"))
								.getAttribute("data-google-query-id");
						String gogleItemId = driver.findElement(By.className("css-1im4ljs")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.className("css-1im4ljs")).getAttribute("data-creative-id");
						if (adLbid.isEmpty()) {
							messages.add("Top DLB Ad is not fired..."
									+ driver.getCurrentUrl());
						} else {
							System.out
									.println("Top DLB Ad is fired...Advertisement id is --> "
											+ adLbid);
							System.out
							.println("Top dlb Ad is fired...google item id is --> "
									+ gogleItemId);
							System.out
							.println("Top dlb Ad is fired...google creative id is --> "
									+ googleCreativeId);
						}
					} catch (Exception e) {
						messages.add("Top DLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}
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
									.className("css-1im4ljs"));
							System.out
									.println("Top DLB Ad disappeared after 3 seconds of scrolling 150px");
						} catch (Exception e) {
							messages.add("Top DLB Ad not disappeared after 3 seconds of scrolling 150px"
									+ driver.getCurrentUrl());
						}
					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				messages.add("Problem with Top DLB Ad... on Replatform Tabbed Article"
						+ e.toString());
			}
			//checked DMR
			try {

				WebElement mrAdSlot = driver
						.findElement(By.className("css-1kx3y31"))
						.findElement(By.className("css-1ca3fuk")).findElement(By.className("css-1im4ljs")).findElement(By.id("dmr1__slot"));
			
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
			System.out.println("-> Banner Image: ");
			try {
				WebElement articleH1 = locateElement('s', ".css-15z5o8l img");
				if (articleH1.getText() == "") {
					messages.add("Banner image missing on "
							+ driver.getCurrentUrl());
				} else {
					System.out
							.println("Banner image exist on Landing Page "
									+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				messages.add("Banner image missing on "
						+ driver.getCurrentUrl() + e.toString());
			}
			String href = null;
			
			// 1st widget: Main link and List on links from secondary section
			System.out.println("-> 1st Widget:");
			try {
				// Checked Main link color and status
					WebElement mainarticle = driver.findElement(By.className("css-17h13hu")).findElement(By.className("css-k008qs"));
					if (mainarticle != null){
						try {
						WebElement mainarticletitle = driver.findElement(By.className("css-17h13hu")).findElement(By.className("css-k008qs")).findElement(By.className("css-l5eukn")).findElement(By.tagName("a"));
						((JavascriptExecutor) driver).executeScript(
								"arguments[0].scrollIntoView(true);",
								mainarticle);
						jse.executeScript("window.scrollBy(0,-150)", "");
						checkHttpStatus(mainarticletitle, "Check main article title",messages);
						Thread.sleep(2000);
						checkColorOnMouseHover(mainarticletitle,
								"Main article title", messages);
						System.out
								.println("Main article title link is not broken "+ driver.getCurrentUrl());
						} catch (Exception e) {
							messages.add("Exception in main section of 1st widget " +e.toString());
						}
						// Checked List of links in Secondary section
						try {
							WebElement secondarysection = driver.findElement(
									By.className("css-k008qs")).findElement(
									By.className("css-1c7e928"));
							List<WebElement> readnextlists = secondarysection
									.findElement(By.className("css-1l95nvm"))
									.findElements(By.className("css-1pgksly"));
							for (WebElement readnextlist : readnextlists) {
								WebElement lnk = locateElement('s',
										".css-egoa7 .css-kkxwou .css-v366hx img");
								checkHttpStatus(lnk, "Secondary section list", messages);
							}
							System.out
									.println("All Links from Secondary section are not broken of 1st widget "
											+ driver.getCurrentUrl());
						} catch (Exception e) {
							messages.add("Problem in Secondary section of 1st widget ");
						}
					}
			} catch (Exception e) {
				messages.add("1st widget is not found or problem with 1st widget ");
			}
			// 2nd widget of Video
			
			try {
				WebElement videowidget = null;
				videowidget = driver.findElement(By.className("css-17h13hu")).findElement(By.className("css-10ib5jr"));
				// Checked video widget title
				if (videowidget != null)
					{
					System.out.println("-> 2nd Widget Video:");
					videowidget = driver.findElement(By.className("css-17h13hu")).findElement(By.className("css-10ib5jr")).findElement(By.tagName("h3"));
					isElementDisplayed(videowidget,"'Video Widget Title", messages);
					try {
						videowidget = locateElement('s',
						".css-17h13hu .css-1vu5noj");
						isElementDisplayed(videowidget, "Video Widget", messages);
					} catch (Exception e) {
						messages.add("Video Widget is missing ");
					}
				}
			} catch (Exception e) {
				
			}

			// 3rd widget
		try {
				jse.executeScript("window.scrollBy(0,950)", "");
				// Checked 3rd widget title
				WebElement thrdwidget = driver.findElement(By.className("css-24iemg"));
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);",
						thrdwidget);
				jse.executeScript("window.scrollBy(0,-150)", "");
				
				if (thrdwidget != null)
				{
					System.out.println("-> 3rd Widget:");
				try {
					
					WebElement thrdrdwidgettitl = driver.findElement(By.className("css-24iemg")).findElement(By.className("css-gvp40g")).findElement(By.tagName("h3"));
				//WebElement thrdrdwidgettitl = locateElement('s',".css-17h13hu .css-24iemg h3");
					isElementDisplayed(thrdrdwidgettitl,
							"3rd widget title",
							messages);
				} catch (Exception e) {
					messages.add("3rd widget title is missing ");
				}
				// Checked List of links in 3rd widget
				try {

					WebElement sectnlinks = locateElement('s',
							".css-17h13hu .css-1qe13yq .css-13pmxen");
					((JavascriptExecutor) driver).executeScript(
							"arguments[0].scrollIntoView(true);", sectnlinks);
					jse.executeScript("window.scrollBy(0,-150)", "");
					List<WebElement> listlinks = sectnlinks.findElement(
							By.className("css-1l95nvm")).findElements(
							By.className("css-mmmoe3"));
					for (WebElement listlink : listlinks) {
						WebElement lnk = locateElement('s',
								".css-mmmoe3 .css-kkxwou .css-1oka6nl img");
						checkHttpStatus(lnk, "List of links from 3rd widget",
								messages);
					}
					System.out
							.println("All Links from 3rd widget are not broken of 3rd widget "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Problem in links of 3rd widget ");
				}
			}
			} catch (Exception e) {
							}

			// 4th widget
			

			try {
				WebElement fourthwidget = driver.findElement(By.className("css-dfhmkm"));
				if (fourthwidget != null)
				{
					System.out.println("-> 4th Widget:");
				// Checked 4th widget title
				try {
					
					WebElement fourthwidgettitl = driver.findElement(By.className("css-dfhmkm")).findElement(By.className("css-gvp40g")).findElement(By.tagName("h3"));
					isElementDisplayed(fourthwidgettitl,
							"4th widget title",
							messages);
				} catch (Exception e) {
					messages.add("4th widget title is missing ");
				}
				// Checked List of links in 4th widget
				try {
					WebElement forthsectnlinks = driver
							.findElement(By
									.xpath(".//div[@class='css-1u4m0k4']/div[4]/div[2]"));
					((JavascriptExecutor) driver).executeScript(
							"arguments[0].scrollIntoView(true);",
							forthsectnlinks);
					jse.executeScript("window.scrollBy(0,-150)", "");
					List<WebElement> listlinks = forthsectnlinks.findElement(
							By.className("css-qycm9a")).findElements(
							By.className("css-mj7fxz"));
					for (WebElement listlink : listlinks) {
						WebElement lnk = locateElement('s',
								".css-1uxu5gb .css-kkxwou .css-s55wq3 img");
						checkHttpStatus(lnk, "List of links from 4th widget",
								messages);
					}
					System.out
							.println("All Links from 4th widget are not broken of 4th widget "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
				
				}
				}
			} catch (Exception e) {
				
			}
			// 5th widget
			

			try {
				WebElement fifthwidget = driver.findElement(By.className("css-eyp0vq"));
				if (fifthwidget != null)
				{
					System.out.println("-> 5th Widget:");
				// Checked 5th widget title
				try {
					WebElement fifthwidgettitl = driver.findElement(By.className("css-eyp0vq")).findElement(By.className("css-gvp40g")).findElement(By.tagName("h3"));
					isElementDisplayed(fifthwidgettitl,
							"5th widget title",
							messages);
				} catch (Exception e) {
					messages.add("5th widget title is missing ");
				}
				// Checked List of links in 5th widget
				try {

					WebElement fifhtsectnlinks = driver
							.findElement(By
									.xpath(".//div[@class='css-1u4m0k4']/div[5]/div[2]"));
					((JavascriptExecutor) driver).executeScript(
							"arguments[0].scrollIntoView(true);",
							fifhtsectnlinks);
					jse.executeScript("window.scrollBy(0,-150)", "");
					List<WebElement> listlinks = fifhtsectnlinks.findElement(
							By.className("css-18mywpg")).findElements(
							By.className("css-1bez6h7"));
					for (WebElement listlink : listlinks) {
						WebElement lnk = locateElement('s', ".css-qk8f9b");
						checkHttpStatus(lnk, "List of links from 5th widget",
								messages);
					}
					WebElement lnk = locateElement('s', ".css-qk8f9b");
					checkColorOnMouseHover(lnk, "links from 5th widgte",
							messages);
					System.out
							.println("All Links are not broken from 5th widget "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Problem in links of 5th widget ");
				}
			
				}
			} catch (Exception e) {
				
			}
			 driver.navigate().back();
			
		} catch (Exception e) {
			messages.add("'View all' link missing at RHS on Replatform Article "+ e.toString());
		}

		finalAssert("More in Section", messages);
		} catch (Exception e) {
			
		}
	}
	@Test(enabled = true, priority = 11, description = "In this test we are checking Ads like Top LB, Inline LB,DMR, Footer LB")
	public void ReplatformArtilceAds() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		String message = "";
		String href = null;
		System.out
				.println("==================Ads on Article Test Case Start==================");
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
						String adLbid = driver.findElement(By.id("dlb1__slot"))
								.getAttribute("data-google-query-id");
						String gogleItemId = driver.findElement(By.className("css-1im4ljs")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.className("css-1im4ljs")).getAttribute("data-creative-id");
						if (adLbid.isEmpty()) {
							messages.add("Top dlb Ad is not fired..."
									+ driver.getCurrentUrl());
						} else {
							System.out
									.println("Top dlb Ad is fired...Advertisement id is --> "
											+ adLbid);
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
							((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,155)");

					// Check stickiness - 1st check that DLB is visible before
					// 3sec of scrolling 150px
					Thread.sleep(2900);

					try {
						WebElement stickyLayer = driver.findElement(By
								.className("css-37r8be"));
						// Check stickiness - if DLB is visible for 3seconds
						// than after 1 second check that it disappeared
						Thread.sleep(1000);
						try {
							stickyLayer = driver.findElement(By
									.className("css-1vwmzfs"));
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
				messages.add("Problem with Top dlb Ad... on Article "
						+ e.toString());
			}

			// Inline DLB2
			Thread.sleep(2000);
			((JavascriptExecutor) driver)
					.executeScript("window.scrollTo(0,document.body.scrollHeight);");
			Thread.sleep(2000);
		
			try {

				WebElement inaAdSlot = driver
						.findElement(By.className("css-1tkhpnj"))
						.findElement(By.className("css-qlq0pr")).findElement(By.className("css-1im4ljs")).findElement(By.id("dlb2__slot"));
			
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", inaAdSlot);
				jse.executeScript("window.scrollBy(0,-150)", "");
				if (!(inaAdSlot.isDisplayed())) {
					System.out
					.println("DLB2 Ad is Not displayed  "
							+ message);
				}
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("dlb2__slot"))).getAttribute("data-google-query-id");
				String gogleItemId = driver.findElement(By.className("css-qlq0pr")).findElement(By.className("css-1im4ljs")).getAttribute("data-line-item-id");
				String googleCreativeId = driver.findElement(By.className("css-qlq0pr")).findElement(By.className("css-1im4ljs")).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					System.out
					.println("DLB2 Ad is not fired... ");
				}else{
					
					System.out
					.println("DLB2 Ad is fired ...Advertisement id is --> "+googleQueryId);
					System.out
					.println("DLB2 is fired..." + " Ad is fired...google item id is --> "+gogleItemId);
					System.out
					.println("DLB2 is fired..." +" Ad is fired...google creative id is --> "+googleCreativeId);
					
				}
			} catch (Exception e) {
				messages.add("DLB2 Ad is Not displayed "
						+ driver.getCurrentUrl());
			}
			// INA ad
			try {

				WebElement inaAdSlot = driver
						.findElement(By.className("css-1tkhpnj"))
						.findElement(By.className("css-1im4ljs"));
			
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", inaAdSlot);
				jse.executeScript("window.scrollBy(0,-150)", "");
				if (!(inaAdSlot.isDisplayed())) {
					System.out
					.println("INA Ad is Not displayed  "
							+ message);
				}
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("ina"))).getAttribute("data-google-query-id");
				
				if(googleQueryId.isEmpty()){
					System.out
					.println("INA Ad is not fired... ");
				}else{
					
					System.out
					.println("INA Ad is fired ...Advertisement id is --> "+googleQueryId);
					
				}
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());
			}
			// Check DMRs
			try {

				
				WebElement dmrAdSlot = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']/div[3]/div[1]/div/div[@id='dmr1__slot']"));
			
				if (!(dmrAdSlot.isDisplayed())) {
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
			messages.add("Article Ads test case exception ..."
					+ e.toString());
		}
		finalAssert("Ads on Article", messages);
	}
	@Test(enabled = true, priority = 12, description = "In this test we are checking existance 'Read This Next' section title"
		+ "\nChecked All Links from Read Next section is not broken"
		+ "\nColor of a Read This Next section Article Title before & after mouse hover")
public void ReplatformArtilceReadNext() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	JavascriptExecutor jse = (JavascriptExecutor) driver;

	System.out
			.println("==================Read This Next Test Case Start==================");
	// Checked Read Next Section title

	try {
		WebElement readnext = locateElement('s',
		".css-10ib5jr h3");
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);", readnext);
		jse.executeScript("window.scrollBy(0,-200)", "");
		isElementDisplayed(readnext, "'Read This Next' section title",
				messages);
	} catch (Exception e) {
		messages.add("'Read This Next' section title is missing on Article "
				+ e.toString());
	}

	String href = null;
	try {
		List<WebElement> readnextlists = driver.findElement(
				By.className("css-1l95nvm")).findElements(
				By.className("css-s6ma3s"));
			for (WebElement readnextlist : readnextlists) {
			WebElement lnk = locateElement('s', ".css-kkxwou .css-u80ajq a");
		
			checkHttpStatus(lnk, "'Read Next' section", messages);
		}
		System.out
		.println("All Links from Read Next section from Article are not broken "
				+ driver.getCurrentUrl());
	
	} catch (Exception e) {
		messages.add("Read Next section not found or problem with Read Next section links on Article ");
	}
	// Check link color for Article Title from Read This Next Section
	WebElement colorforarticletitle = locateElement('s', ".css-u80ajq a");
		// Mouse hover
	checkColorOnMouseHover(colorforarticletitle,
			"Article Title", messages);
	// Check link color for Article 'READ MORE' link 
	// Section
	
	WebElement readmorelink = locateElement('s', ".css-u80ajq .css-120snmw a");
	checkColorOnMouseHover(readmorelink,
			"''READ MORE' link'", messages);
	finalAssert("Read This Next", messages);
}
	
	@Test(enabled = true, priority = 13, description = "In This test we are checking Meta Title, Description, ROBOTS & Canonical link from view sources.")
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
			// Check 'Robots' from view source
			try{
				String Source = driver.getPageSource();
				String xpath = "//meta[@name='ROBOTS']";
				
				WebElement meta = driver.findElement((By.xpath(xpath)));
				String metaname = meta.getAttribute("name");
				System.out.println("Meta name: "+ metaname);
				}
				catch (Exception e) {
					System.out.println("Robots not found from view sources "+driver.getCurrentUrl());
				}
				// Check 'Description' from view source
				try{
					String Source = driver.getPageSource( );
					String xpath = "//meta[@name='description']";
					
					WebElement meta = driver.findElement((By.xpath(xpath)));
					String metaDescription = meta.getAttribute("content");
					System.out.println("Meta Description: "+ metaDescription);
					
				}
				catch (Exception e) {
					System.out.println("Description not found from view sources "+driver.getCurrentUrl());
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
	@Test(enabled = true, priority = 14, description = "In This test we are checking 'Comscore' tag for beacon.js from view sources.")
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
