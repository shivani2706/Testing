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

public class ReplatformSingleVideoPage {
	static Map<String, String> env;
	static String driverType;
	private static String baseURL;
	private static String singlevideo;
	private static int linksCount = 0;
	private static String[] links = null;
	private static String domain;
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
		domain=baseURL;
		singlevideo=env.get("singlevideo");

	}
	@BeforeMethod
	public void clearDriver() {
		driver.manage().deleteAllCookies();
	}public void isElementDisplayed(WebElement elem, String Componant,
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
	@Test(enabled = true, priority = 1, description = "In this test we are checking H1 tag(title) exist")
	public void ReplatformSingleVideotitle() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		try {
			singlevideo = "singlevideo.txt";
			 SelectRandomURL s=new SelectRandomURL();
			 String url= s.choose(new File(singlevideo));
			 driver.get(baseURL+url);
			 Thread.sleep(2000);  
		System.out
				.println("==================Title Test Case Start==================");
		// Check Title
		try {
			WebElement title = locateElement('s', ".css-115ig6b h1");
			isElementDisplayed(title, "Title", messages);
		} catch (Exception e) {
			messages.add("Title missing on Replatform Single Video"
					+ driver.getCurrentUrl() + e.toString());
		}
		}
		catch (Exception e) {
			messages.add(" Problem on Replatform Single Video "
					+ driver.getCurrentUrl() + e.toString());
		}
		finalAssert("Title", messages);
	}
	
	
	@Test(enabled = true, priority = 2, description = "In this test we are checking Center Content.")
	public void ReplatformSingleVideoCenterContent() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Center Content Test Case Start==================");
		// Check Center Content
		try {
			WebElement content = locateElement('s', ".css-1tkhpnj");
			isElementDisplayed(content,
					"Center Content", messages);
		} catch (Exception e) {
			messages.add("Center Content missing on Replatform Single Video "
					+ e.toString());
		}
		finalAssert("Center Content", messages);
	}
	@Test(enabled = true, priority = 3, description = "In this test we are checking Center Video Widget.")
	public void ReplatformSingleVideoWidget() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Video Widget Test Case Start==================");
		// Check Center Content
		try {
			WebElement content = locateElement('s', ".css-1ep6643 .inner-wrapper");
			isElementDisplayed(content,
					"Video Widget", messages);
		} catch (Exception e) {
			messages.add("Video Widget missing on Replatform Single Video "
					+ e.toString());
		}
		finalAssert("Video Widget", messages);
	}
	@Test(enabled = true, priority = 4, description = "In this test we are checking video transcript.")
	public void ReplatformSingleVideoTranscript() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Video Transcript Test Case Start==================");
		// Check Video Transcript link
		try {
			WebElement transcriptlink = locateElement('s', ".css-1ep6643 .css-6efnz3 .css-1nfvlhm");
			isElementDisplayed(transcriptlink,
					"Video Transcript", messages);
			// Check Video Transcript link color
			checkColorOnMouseHover(transcriptlink, "Video Transcript", messages);
			
			// Video Transcript link click and section expand
			try {
				Thread.sleep(3000);
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", transcriptlink);
				jse.executeScript("window.scrollBy(0,-250)", "");
				Thread.sleep(3000);

				Actions action1 = new Actions(driver);
				action1.click(driver.findElement(By.className("css-1nfvlhm")))
						.build().perform();
				Thread.sleep(3000);
				WebElement topbranding = locateElement('s', ".css-14bhubt");
				isElementDisplayed(topbranding, "Video Transcript Section Expanded",
						messages);
				try {
				WebElement transcripttitle = locateElement('s', ".css-14bhubt .css-1ife2we h3");
				isElementDisplayed(transcripttitle,
						"Video Transcript Title in expanded section", messages);
				}catch (Exception e) {
					messages.add("Video Transcript title missing in expanded section on Single Video "
							+ e.toString());
				}
			} catch (Exception e) {
				messages.add("Video Transcript link is not clickable and Section is not expanded on Single Video "
						+ e.toString());
			}
			
		} catch (Exception e) {
			messages.add("Video Transcript Link missing on Replatform Single Video"
					+ e.toString());
		}
		finalAssert("Video Transcript", messages);
	}
	@Test(enabled = true, priority = 5, description = "In this test we are checking Sharebar exist"
		+ "\nColor of FB, Twitter, Print icons before & after mouse hover"
		+ "\nClick FB, Twitter & Email icons"
		+ "\nClick feedback smile & frown icons and check sublinks from overlay.Click 1st link from Smile overlay and closed it.")
public void ReplatformSingleVideoShareTools() throws InterruptedException {
	List<String> messages = new ArrayList<String>();
	JavascriptExecutor jse = (JavascriptExecutor) driver;

	System.out
			.println("==================Sharebar Test Case Start==================");
	// Check Share Tools

	WebElement sharebar = new WebDriverWait(driver, 15)
			.until(ExpectedConditions.visibilityOfElementLocated(By
					.cssSelector(".css-xsfqmw")));
	((JavascriptExecutor) driver).executeScript(
			"arguments[0].scrollIntoView(true);",
			locateElement('s', ".css-10wvu0d"));
	jse.executeScript("window.scrollBy(0,-255)", "");
	Thread.sleep(3000);

	if (!(sharebar.isDisplayed())) {
		messages.add("Sharebar & Feedback section is missing on Replatform Single Video "
				+ driver.getCurrentUrl());
	} else {
		try {
			WebElement printicon = locateElement('s',
					".css-ea7map .print a");
			isElementDisplayed(printicon, "Print Icon", messages);

		} catch (Exception e) {
			messages.add("Print Icon is missing on Replatform Single Video, "
					+ driver.getCurrentUrl());
		}

		// FB Icon
		try {
			WebElement fbicon = locateElement('s',
					".css-ea7map .facebook a");
			isElementDisplayed(fbicon, "FB Icon", messages);

			// Click FB Icon
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", fbicon);

			jse.executeScript("window.scrollBy(0,-150)", "");
			String parentWindowHandler = driver.getWindowHandle(); // Store
																	// your
																	// parent
																	// window
			String subWindowHandler = null;
			fbicon.click();
			Thread.sleep(5000);
			SwitchToPopup();
			if (!driver
					.getCurrentUrl()
					.contains(
							"https://www.facebook.com/login.php?skip_api_login=1&api_key=966242223397117")) {
				messages.add("FB is not opened on clikcing icon "
						+ driver.getCurrentUrl());
			} else {
				System.out
						.println("FB window is open on clicking FB icon from sharbar on "
								+ driver.getCurrentUrl());
			}
			driver.close();
			driver.switchTo().window(parentWindowHandler);

		} catch (Exception e) {
			messages.add("FB Icon is missing on Replatform Single Video, "
					+ driver.getCurrentUrl());
		}

		// Twitter Icon
		try {
			WebElement twittericon = locateElement('s',
					".css-ea7map .twitter a");
			isElementDisplayed(twittericon, "Twitter Icon", messages);

			// Check Twitter Icon color
			checkColorOnMouseHover(twittericon, "Twitter Icon", messages);
				((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", twittericon);
				jse.executeScript("window.scrollBy(0,-150)", "");
			 String parentWindowHandler = driver.getWindowHandle(); // Store
																	// your
																	// parent
																	// window
			String subWindowHandler = null;
			twittericon.click();
			Thread.sleep(5000);
			SwitchToPopup();
			if (!driver.getCurrentUrl().contains(
					"https://twitter.com/intent/tweet?text=")) {
				messages.add("Twitter window is not opened on clicking icon "
						+ driver.getCurrentUrl());
			} else {
				System.out
						.println("Twitter window is open on clicking Twitter icon from sharbar on "
								+ driver.getCurrentUrl());
			}
			driver.close();
			driver.switchTo().window(parentWindowHandler);

		} catch (Exception e) {
			messages.add("Twitter Icon is missing on Replatform Single Video, "
					+ driver.getCurrentUrl());
		}
		// Email Icon
		try {
			WebElement emailicon = locateElement('s', ".css-ea7map .email");
			isElementDisplayed(emailicon, "Email Icon", messages);

			// Click Email Icon
			String parentWindowHandler = driver.getWindowHandle(); // Store
																	// your
																	// parent
																	// window
			String subWindowHandler = null;
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", emailicon);
			jse.executeScript("window.scrollBy(0,-150)", "");
			Thread.sleep(3000);
			emailicon.click();
			Thread.sleep(3000);
			Set<String> handles = driver.getWindowHandles(); // get all
																// window
																// handles
			Iterator<String> iterator = handles.iterator();
			while (iterator.hasNext()) {
				subWindowHandler = iterator.next();
			}
			driver.switchTo().window(subWindowHandler);

			WebElement emailh3 = locateElement('s',
					".css-mjgn4d .css-1iaon9y h3");
			if (!emailh3.isDisplayed()) {
				System.out
						.println("EMAIL window not open on clicking email icon from sharbar on "
								+ driver.getCurrentUrl());
			} else {
				System.out.println("EMAIL POPUP H3 title text is: "
						+ emailh3.getText());

				WebElement hds2 = locateElement('s',
						".css-mjgn4d .window .icon-hl-close");
				((JavascriptExecutor) driver)
						.executeScript("window.scrollTo(0,"
								+ hds2.getLocation().y + ")");
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].click();", hds2);
			}
			driver.switchTo().window(parentWindowHandler);

		} catch (Exception e) {
			messages.add("Email Icon is missing on Replatform Single Video, "
					+ driver.getCurrentUrl());
		}
	}

	// Sharebar End
	// Feedback Smile icon
	try {
		WebElement smileicon = locateElement('s',
				".css-98bjb0 .icon-hl-smile");
		isElementDisplayed(smileicon, "Feedback Smile Icon",
				messages);
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);", smileicon);

		jse.executeScript("window.scrollBy(0,-150)", "");
		String parentWindowHandler = driver.getWindowHandle();
		String subWindowHandler = null;
		smileicon.click();
		System.out.println("Smile icon clicked");
		Thread.sleep(3000);
		try {
			WebElement smileh3 = locateElement('s',
					".css-mjgn4d .window .css-30xuli h3");
			if (smileh3.getText().trim().equals("")) {
				messages.add("Title missing in Smile overlay "
						+ driver.getCurrentUrl());
				System.out
						.println("Title missing on Smile overlay ");
			} else {
				System.out.println("Title exist on Smile overlay ");
			}
		} catch (Exception e) {
			messages.add("Title missing on Smile overlay "
					+ driver.getCurrentUrl());
			System.out.println("Title missing on Smile overlay "
					+ e.toString());
		}

		// check 3 links of Smile overlay using findelements
		try {
			List<WebElement> linksCont = driver.findElement(
					By.className("css-30xuli")).findElements(
					By.className("css-rysign"));

			for (int i = 0; i < linksCont.size(); i++) {
				WebElement comomnlink = linksCont.get(i)
						.findElement(By.className("css-7n5o71"));
				if (comomnlink.getText() == "") {
					System.out
							.println(" hyperlink missing on 'Smile' overlay ");
				} else {
					System.out.println(linksCont.get(i).getText()
							+ " hyperlink exist on Smile overlay ");
				}
			}
		} catch (Exception e) {
			messages.add("Smile icon missing "
					+ driver.getCurrentUrl());
		}
		// click 1st link from Smile overlay
		try {

			WebElement clcik1stlink = locateElement('s',
			".css-mjgn4d .window .css-30xuli .css-rysign");
			clcik1stlink.click();
			Thread.sleep(waittime);
			System.out
					.println("'This article changed my life!' link is clickable from Smile overlay "
							+ driver.getCurrentUrl());
			try {
				WebElement overlaytitle = locateElement('s',
				".css-mjgn4d .window .css-30xuli h3");
				isElementDisplayed(
						overlaytitle,
						"'This article changed my life!' overlay title",
						messages);

				// Click on 'Close' icon from Thanks overlay.
				WebElement closeicon = driver.findElement(
						By.className("css-mjgn4d")).findElement(By.className("window")).findElement(
						By.className("icon-hl-close"));
				((JavascriptExecutor) driver)
						.executeScript("window.scrollTo(0,"
								+ closeicon.getLocation().y + ")");
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].click();", closeicon);
					} catch (Exception e) {
				messages.add("'This article changed my life!' overlay title is missing on Replatform Single Video "
						+ e.toString());
			}
		} catch (Exception e) {
			messages.add("'This article changed my life!' link is not clickable on Smile overlay "
					+ driver.getCurrentUrl());
			System.out
					.println("'This article changed my life!' link is missing on Smile overlay "
							+ e.toString());
		}

	} catch (Exception e) {
		messages.add("Title missing on Smile overlay "
				+ driver.getCurrentUrl());

	}
	// Frown Icon
	try {
		WebElement frownicon = locateElement('s',
		".css-98bjb0 .icon-hl-frown");
		isElementDisplayed(frownicon, "Feedback Frown Icon", messages);
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,"
				+ frownicon.getLocation().y + ")");
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].click();", frownicon);
		Thread.sleep(6000);
		System.out.println("Frown Icon is clicked ");

		// Check 3 links of No overlay using findelements
		try {
			List<WebElement> NolinksCont = driver
			.findElement(By.id("modal-feedback-negative"))
			.findElement(By.className("css-mjgn4d"))
			.findElement(By.className("css-30xuli")).findElements(By.className("css-rysign"));
			
			for (int i = 0; i < NolinksCont.size(); i++) {
				WebElement comomnlink = NolinksCont.get(i).findElement(
						By.className("css-7n5o71"));
				if (comomnlink.getText() == "") {
					System.out
							.println(" hyperlink missing on Frown overlay ");
				} else {
					System.out.println(NolinksCont.get(i).getText()
							+ " hyperlink exist on Frown overlay ");
				}
			}
			// Click on 'Close' icon from overlay.
			WebElement closeicon = driver.findElement(
					By.className("css-mjgn4d")).findElement(By.className("window")).findElement(
					By.className("icon-hl-close"));
			((JavascriptExecutor) driver)
					.executeScript("window.scrollTo(0,"
							+ closeicon.getLocation().y + ")");
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].click();", closeicon);

		} catch (Exception e) {
			messages.add("Hyperlinks missing on Frown overlay "
					+ driver.getCurrentUrl());
		}
	} catch (Exception e) {
		messages.add("Frown Icon missing on feedback widget "
				+ driver.getCurrentUrl());
	}

	finalAssert("Sharebar & Feedback", messages);
}

	@Test(enabled = true, priority = 6, description = "Byline section is dispalyed")
	public void ReplatformSingleVideoByline() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Byline Test Case Start==================");
		// Checked Medical Review at RHS
		try {
			WebElement medreivew = locateElement('s',
					".css-1kx3y31 .css-2vgg1c .css-uyum6x");
			isElementDisplayed(medreivew, "Byline section", messages);
			Thread.sleep(3000);

		} catch (Exception e) {
			messages.add("Problem in Medical Review section at RHS on Replatform Single Video "
					+ driver.getCurrentUrl());
		}
		finalAssert("Byline", messages);
	}
	@Test(enabled = true, priority = 7, description = "In this test we are checking 'Top Stories' title and content")
	public void ReplatformSingleVideoTopStroies() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Top Stories Test Case Start==================");
		// Checked Top Stories widget title at RHS
		try {
			WebElement topstories = locateElement('s', ".css-1kynjxe h2");
			isElementDisplayed(topstories, "'Top Stories' title", messages);

		} catch (Exception e) {
			messages.add("'Top Stories' title is missing at RHS on Replatform Single Video "
					+ e.toString());
		}
		// Checked 'Top Stories' section
		try {
			WebElement topstorie = locateElement('s', ".css-17mrx6g");
			isElementDisplayed(topstorie, "'Top Stories' Content", messages);
		
		} catch (Exception e) {
			messages.add("Top Stories Content missing at RHS on Replatform Single Video "
					+ e.toString());
		}
		finalAssert("Top Stories", messages);
	}
	@Test(enabled = true, priority = 8, description = "In this test we are checking existance 'Read This Next' section title"
		+ "\nChecked All Links from Read Next section is not broken"
		+ "\nColor of a Read This Next section Article Title before & after mouse hover")
public void ReplatformSingleVideoReadNext() throws InterruptedException {
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
	@Test(enabled = true, priority = 9, description = "In this test we are checking Ads like Top LB, Inline LB, DMR, Footer LB")
	public void ReplatformSingleVideoAds() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		String message = "";
		String href = null;
		System.out
				.println("==================Ads Test Case Start==================");
		try {
			// Check Top DLB Ad & Stikiness for 3 seconds
			try {
				Thread.sleep(10000);
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
						String gogleItemId = driver.findElement(By.className("css-1im4ljs"))
						.getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.className("css-1im4ljs"))
						.getAttribute("data-creative-id");
						if (adLbid.isEmpty()) {
							messages.add("Top dlb Ad is not fired..."
									+ driver.getCurrentUrl());
						} else {
							System.out
									.println("Top dlb Ad is fired...Advertisement id is --> "
											+ adLbid);
							System.out
							.println("Top DLB Ad google item id is --> "+gogleItemId);
							System.out
							.println("Top DLB Ad google creative id is --> "+googleCreativeId);
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
				messages.add("Problem with Top dlb Ad... on Replatform Single Video "
						+ e.toString());
			}

			

			// Check DMRs
			try {
			
				List<WebElement> dmrs = driver.findElement(
						By.className("css-1kx3y31")).findElements(
						By.className("css-1ca3fuk"));
				for (int i = 1; i <= dmrs.size(); i++) {
					try {
						String dmr = driver.findElement(
								By.id("dmr" + i + "__slot")).getAttribute(
								"data-google-query-id");
						String gogleItemId = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']//div[@class='css-1im4ljs']")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']//div[@class='css-1im4ljs']")).getAttribute("data-creative-id");
						if (dmr.isEmpty()) {
							messages.add("DMR"
									+ i
									+ " Ad at right rail on Replatform Single Video is not fired.."
									+ driver.getCurrentUrl());
						} else {
							System.out
									.println("DMR"
											+ i
											+ " ad is fired ...advertisement id is --> "
											+ dmr);
							System.out
							.println("DMR"
									+ i
									+ " ad is fired ...google item id is --> "+gogleItemId);
							System.out
							.println("DMR"
									+ i
									+ " ad is fired ...google creative id is --> "+googleCreativeId);
						}
					} catch (Exception e) {
						messages.add("DMR" + i
								+ " Ad is missing on Replatform Single Video Page ");
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		
			
			//Read Next DCMRs
			try {
				for (int i = 1; i < 10; i++) {
					((JavascriptExecutor) driver)
							.executeScript("window.scrollTo(0,document.body.scrollHeight);");
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,-155)");
					Thread.sleep(2000);
				}
				WebElement readnxtContent = driver.findElement(By
						.className("css-1l95nvm"));
				List<WebElement> dcmrs = readnxtContent.findElements(By.className("css-s6ma3s"));
				
				for (int i = 1; i <= 5; i++) {
					try {
						String dcmr = driver.findElement(
								By.id("dcmr" + i + "__slot")).getAttribute(
								"data-google-query-id");
						String gogleItemId = driver.findElement(By.xpath(".//section//div[@class='css-1im4ljs']")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.xpath(".//section//div[@class='css-1im4ljs']")).getAttribute("data-creative-id");
						if (dcmr.isEmpty()) {
							messages.add("DCMR"
									+ i
									+ " Ad at right rail on Replatform Single Video is not fired.."
									+ driver.getCurrentUrl());
						} else {
							System.out
									.println("DCMR"
											+ i
											+ " ad is fired ...advertisement id is --> "
											+ dcmr);
							System.out
							.println("DCMR"
									+ i
									+ " ad is fired ...gogle Item Id is --> "+gogleItemId);
							System.out
							.println("DCMR"
									+ i
									+ " ad is fired ...google Creative Id is --> "+googleCreativeId);
						}
					} catch (Exception e) {
						messages.add("DCMR" + i
								+ " Ad is missing on Replatform Single Video ");
					}
					
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			
			// Footer advertisement
			try {
				String adfooetrLbid = driver.findElement(
						By.id("ad-pb-by-google"))
						.getAttribute("data-ad-client");
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
			messages.add("Replatform Single Video Page Ads test case exception ..."
					+ e.toString());
		}
		finalAssert("Ads", messages);
	}
	@Test(enabled = true, priority = 10, description = "In This test we are checking Meta Title, Description, ROBOTS & Canonical link from view sources.")
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
	@Test(enabled = true, priority = 11, description = "In This test we are checking 'Comscore' tag for beacon.js from view sources.")
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
