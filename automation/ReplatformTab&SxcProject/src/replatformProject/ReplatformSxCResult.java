package replatformProject;

import static java.util.Arrays.asList;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.xerces.util.URI;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Ignore;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.assertTrue;
import org.testng.AssertJUnit;

import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.webkitRTCPeerConnection;

import qalib.CheckAds;
import qalib.CheckHttpConnection;
import qalib.SelectRandomURL;

public class ReplatformSxCResult {
	static Map<String, String> env;
	static String driverType;
	private static String baseURL;
	private static String sxcfile; 
	private static int linksCount = 0;
	private static String[] links = null;
	private static String domain;
	private static WebDriver driver;
	public static int waittime = 2000;
	static int dayOfWeek;
	static int dayOfMonth;
	public static boolean newHeader = true;
	public static boolean newFooter = true;
	int rnd;
	public static List<HashMap<String,String>> hashMaps = new ArrayList<HashMap<String,String>>();
	public static HashMap<String, String> gaEvents1=new HashMap<String, String>();
	
	@BeforeClass
	public static void startWebDriver() {

		env = System.getenv();
		driverType = env.get("DRIVER");
		baseURL = env.get("URL");
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		DesiredCapabilities caps = DesiredCapabilities.chrome();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        driver = new ChromeDriver(caps);
		driver.manage().window().maximize();
		domain=baseURL;
		sxcfile=env.get("sxcfile");
		
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

	@Test(enabled = true, priority = 1, description = "In This test we are checking Global Header exist."
			+ "\nHamburger Menu, Newsletter link, HL Logo, Search widget exists or not. "
			+ "\nCheck newsletter link is exist click link and check overlay , form submit & thank you message dispalyed")
	public void ReplatformSxCHeader() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Header Test Case Start==================");
		try {
			sxcfile = "sxcfile.txt";
			 SelectRandomURL s=new SelectRandomURL();
			 String url= s.choose(new File(sxcfile));
			 driver.get(baseURL+url);
			 Thread.sleep(2000);  
			WebElement header = locateElement('i', "site-header");
			isElementDisplayed(header, "Global Header", messages);
			try {
				// Check for Hamburger Menu from Header
				WebElement menu = locateElement('s',
						"#site-header .header-menu");
				isElementDisplayed(menu, "Hamburger Menu", messages);

			} catch (Exception e) {
				messages.add("Hamburger Menu not displayed from Replatform SxC Page Header "
						+ driver.getCurrentUrl());
			}
			// Check for HL Logo from Header
			try {
				WebElement logo = locateElement('s',
						"#site-header .css-1c8s6kl");
				isElementDisplayed(logo, "HL logo", messages);
				// Check HL logo color
				checkColorOnMouseHover(logo, "HL logo", messages);

			} catch (Exception e) {
				messages.add("HL logo not displayed from Replatform SxC Page Header."
						+ driver.getCurrentUrl());
			}
			// Check for Newsletter link from Header
			try {
				WebElement newsletter = locateElement('s',
						"#site-header .css-1fzexpt .css-vafyp3");

				isElementDisplayed(newsletter, "Newsletter link", messages);

				checkColorOnMouseHover(newsletter, "Newsletter link", messages);

				newsletter.click();
				WebElement overlay = locateElement('s', ".window");
				if (overlay.isDisplayed()) {
					System.out
							.println("Newsletter overlay displayed on clicking Newsletter link from global header");
					WebElement email = locateElement('s',
							".window .css-1ue4rxx .css-2pf9vp .css-1qjre05");
					email.clear();
					email.sendKeys("kinjal@healthline.com");
					Thread.sleep(waittime);
					WebElement send = locateElement('s',
							".window .css-1ue4rxx .css-yk1wj3 .css-1274o8r");
					send.click();
					try {
						WebElement responceText = locateElement('s',
								".css-zz2nqw .css-8qzwss p");

						if (responceText.getText() == "") {
							messages.add("Thank you message not display after newsletter form submit from Newsletter Overlay.");
						} else {
							System.out
									.println("Thank you message display after newsletter form submit from Newsletter Overlay.");
						}
					} catch (Exception e) {
						messages.add("Thank you message not display after newsletter form submit from Newsletter Overlay.");
					}
					WebElement Close = locateElement('s',
							".window .icon-hl-close");
					Close.click();
				}

			} catch (Exception e) {
				messages.add("Newsletter link not displayed from Replatform SxC Page Header.."
						+ driver.getCurrentUrl() + e.toString());
			}
			String message = "";
			// Check for Search from Header
			try {
				WebElement search = locateElement('s',
						".css-1gnp2g5 .css-r0bbdu");
				isElementDisplayed(search, "Search text box", messages);
				// Enter few letters and select any
				WebElement searchtext = search.findElement(By.className("autocomplete-wrapper")).findElement(By.className("autocomplete"));
				searchtext.clear();
				searchtext.sendKeys("as");
				Thread.sleep(5000);
				WebElement drpdon = driver.findElement(By.className("css-1kgxb1r"));
				if (!(drpdon.isDisplayed())) {
					System.out
					.println("Search Dropdown is Not displayed  "
							+ message);
				}
				System.out
				.println("Search Dropdown is displayed.."
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Search not displayed from Replatform SxC Result."
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Global Header is not displayed on Replatform SxC Page."
					+ e.toString());
		}

		finalAssert("Header", messages);
	}

	@Test(enabled = true, priority = 2, description = "In this test we are checking H1 tag(title) exist")
	public void ReplatformSxCTitle() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Title Test Case Start==================");
		// Check Title
		try {
			WebElement topbranding = locateElement('s', ".css-kcf06k h1");
			isElementDisplayed(topbranding, "Title", messages);
		} catch (Exception e) {
			messages.add("Title missing on Replatform SxC Page " + e.toString());
		}
		finalAssert("Title", messages);
	}

	@Test(enabled = true, priority = 3, description = "In this test we are checking Center Content exist")
	public void ReplatformSxCcentercontent() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================Center Content Test Case Start==================");
		try {
			// Check Center Content
			WebElement content1 = locateElement('s', ".css-tu0zzi p");
			isElementDisplayed(content1, "Center Content", messages);
		} catch (Exception e) {
			messages.add("Center Content is missing on Replatform SxC Page "
					+ e.toString());
		}
		finalAssert("Center Content", messages);
	}

	@Test(enabled = true, priority = 4, description = "In this test we are checking Read more link is clickable or not"
			+ "'Med review info' in Read more content exist"
			+ "\n'Article Resource' link click and section expanded in Read more content")
	public void ReplatformSxCReadmorelink() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Read More Link Test Case Start==================");
		// Click Read more Link
		try {
			WebElement readmorelink = driver.findElement(
					By.className("css-tu0zzi")).findElement(By.tagName("a"));
			readmorelink.click();
			Thread.sleep(waittime);

			// Check 'Med review info' in Read more content
			try {
				WebElement medrvw = locateElement('s',
						".css-1d0yp70 .css-1gby43k");
				isElementDisplayed(medrvw,
						"'Med review info' in Read more content", messages);

			} catch (Exception e) {
				messages.add("Medically reviewed is missing in Read more content on Replatform SxC Page "
						+ driver.getCurrentUrl());
			}
			// Article Resources
			try {
				WebElement articlesource = locateElement('s', ".css-mxupii .css-1xn3edc .css-1lrkj93 p");
				isElementDisplayed(articlesource, "Article Resources link",
						messages);

				// Article Resources link click and section expand
				try {
					Thread.sleep(3000);
					((JavascriptExecutor) driver)
							.executeScript(
									"arguments[0].scrollIntoView(true);",
									articlesource);
					jse.executeScript("window.scrollBy(0,-250)", "");
					Actions action1 = new Actions(driver);
					action1.click(
							driver.findElement(By.className("css-1lrkj93")))
							.build().perform();
					Thread.sleep(3000);
					WebElement topbranding = locateElement('s', ".css-whm7bm");
					isElementDisplayed(topbranding,
							"Article Resources Section", messages);
				} catch (Exception e) {
					messages.add("Article Resources link is not clickable and Section is not expanded on Replatform SxC Page "
							+ e.toString());
				}
			} catch (Exception e) {
				messages.add("Article Resources link is Not displayed on Replatform SxC Page "
						+ e.toString());
			}
		} catch (Exception e) {
			messages.add("Read more link is not clickable at Center on Replatform SxC Page "
					+ driver.getCurrentUrl());
		}
		finalAssert("Read more Link", messages);
	}

	@Test(enabled = true, priority = 5, description = "In this test we are checking Sharebar exist"
			+ "\nColor of FB, Twitter, Print icons before & after mouse hover"
			+ "\nClick FB, Twitter & Email icons" +"\nClick feedback smile & frown icons and check sublinks from overlay.Click 1st link from Smile overlay and closed it.")
	public void ReplatformSxCShareTools() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Sharebar Test Case Start==================");
		// Check Share Tools
		try {
			WebElement sharebar = new WebDriverWait(driver, 15)
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.cssSelector(".css-mxupii .css-1bh05wg")));
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", sharebar);
			jse.executeScript("window.scrollBy(0,-150)", "");
			if (!(sharebar.isDisplayed())) {
				messages.add("Sharebar & Feedback section is missing on Replatform SxC Page "
						+ driver.getCurrentUrl());
			} else {
				try {
					WebElement printicon = locateElement('s',
							".css-ea7map .print a");
					isElementDisplayed(printicon, "Print Icon", messages);
				} catch (Exception e) {
					messages.add("Print Icon is missing on Replatform SxC Page, "
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
					messages.add("FB Icon is missing on Replatform SxC Page, "
							+ driver.getCurrentUrl());
				}

				// Twitter Icon
				try {
					WebElement twittericon = locateElement('s',
							".css-ea7map .twitter a");
					isElementDisplayed(twittericon, "Twitter Icon", messages);
					// Check Twitter Icon color
					checkColorOnMouseHover(twittericon, "Twitter Icon",
							messages);

					// Click Twitter Icon
					((JavascriptExecutor) driver).executeScript(
							"arguments[0].scrollIntoView(true);", twittericon);

					jse.executeScript("window.scrollBy(0,-150)", "");
					String parentWindowHandler = driver.getWindowHandle(); // Store
																			// your
																			// parent
																			// window
					twittericon.click();
					Thread.sleep(5000);
					SwitchToPopup();
					if (!driver
							.getCurrentUrl()
							.contains(
									"https://twitter.com/intent/tweet?url=")) {
						messages.add("Twitter window is not opened on clikcing icon "
								+ driver.getCurrentUrl());
					} else {
						System.out
						.println("Twitter window is open on clicking Twitter icon from sharbar on "
								+ driver.getCurrentUrl());
					}
					driver.close();
					driver.switchTo().window(parentWindowHandler);

				} catch (Exception e) {
					messages.add("Twitter Icon is missing on Replatform SxC Page, "
							+ driver.getCurrentUrl());
				}
				// Email Icon
				try {

					WebElement emailicon = locateElement('s',
							".css-ea7map .email");
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
					messages.add("Email Icon is missing on Replatform SxC Page, "
							+ driver.getCurrentUrl());
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
							messages.add("'This article changed my life!' overlay title is missing on Replatform SxC Page "
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
					isElementDisplayed(frownicon, "Feedback Frown Icon",
							messages);
					((JavascriptExecutor) driver)
							.executeScript("window.scrollTo(0,"
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
							WebElement comomnlink = NolinksCont.get(i)
									.findElement(By.className("css-7n5o71"));
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

			}
		} catch (Exception e) {
			messages.add("Sharebar & Feedback section is missing on Replatform SxC Page "
					+ driver.getCurrentUrl() + " " + e.toString());
		}
		finalAssert("Sharebar & Feedback", messages);
	}

	@Test(enabled = true, priority = 6, description = "In this test we are checking Possible Condition H2 tag(title)")
	public void ReplatformSxCPosCond() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Possible Condition Test Case Start==================");

		// Check Possible Condition title
		try {
			WebElement possiblecondition = locateElement('s', ".css-kcf06k h2");
			
			isElementDisplayed(
					possiblecondition,
					"Possible Condition title with reuslt number",
					messages);
		} catch (Exception e) {
			messages.add("Possible Conditions title with reuslt number is missing on Replatform SxC Page "
					+ driver.getCurrentUrl());
		}
		finalAssert("Possible Condition", messages);
	}

	@Test(enabled = true, priority = 7, description = "In this test we are checking 'Add symptoms...link with Plus '+' sign' is exist"
			)
	public void ReplatformSxCAddSymLink() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("=================='Add symptoms...link with Plus '+' sign' Test Case Start==================");

		// Check 'Add symptoms to narrow your search link with Plus '+' sign'
		try {
			WebElement narrowlink = locateElement('s',
					".css-kcf06k .css-jenvb9 .css-u6vufr");
			isElementDisplayed(narrowlink,
					"'Add symptoms to narrow your search' link with '+' sign",
					messages);
			// Check 'Add symptoms to narrow your search link with Plus '+'
			// sign' color
			checkColorOnMouseHover(narrowlink,
					"'Add symptoms to narrow your search' link with '+' sign",
					messages);
		} catch (Exception e) {
			messages.add("'Add symptoms to narrow your search' link with '+' sign is missing on Replatform SxC Page "
					+ driver.getCurrentUrl());
		}
		finalAssert("Add symptoms...link with Plus '+' sign", messages);
	}

	@Test(enabled = true, priority = 8, description = "In This test we are checking 'Add symptoms...' link clickable and Overlay"
			+ "\nClick symptom link from overlay"
			+ "\nCheck result & status after clicking from overlay")
	public void ReplatformSxCOverlayclick() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("=================='Add symptoms...' Overlay Test Case Start==================");
		// Click 'Add symptoms to narrow your search link with Plus '+' sign'
		// link for Overlay
		try {
			WebElement clicknarrowlink = locateElement('s',
					".css-kcf06k .css-jenvb9 .css-u6vufr");
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,"
					+ clicknarrowlink.getLocation().y + ")");
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].click();", clicknarrowlink);
			Thread.sleep(waittime);
			System.out
					.println("'Add symptoms to narrow your search' link with '+' is clickable on Replatform SxC Page "
							+ driver.getCurrentUrl());
		} catch (Exception e) {
			messages.add("'Add symptoms to narrow your search' link with '+' is  NOT clickable on Replatform SxC Page "
					+ driver.getCurrentUrl());
		}
		// Click symptom link from overlay
		try {

			WebElement overlay = locateElement('s', ".css-kcf06k .css-wppsoi");
			List<WebElement> symptomlinks = overlay.findElements(By
					.className("css-1ftt91h"));

			WebElement clicklink = symptomlinks.get(1).findElement(By.className("css-1xiilcr"));
			
			clicklink.click();
			Thread.sleep(waittime);
			System.out
					.println("Link is clickable in the overlay on Replatform SxC Page "
							+ driver.getCurrentUrl());
		} catch (Exception e) {
			messages.add("'Link is NOT clickable in the overlay on  Replatform SxC Page "
					+ driver.getCurrentUrl());
		}
		// Check result
		String href = null;
		try {

			List<WebElement> featuredTools = driver.findElement(By.className("css-1ie23w1")).findElement(By.className("css-1iruc8t")).findElements(By
					.className("css-vusf7j"));
			for (WebElement featuredTool : featuredTools) {
				WebElement lnk = featuredTool.findElement(By.tagName("a"));
				checkHttpStatus(lnk, "SxC resutl Section", messages);
			}
			System.out
					.println("All Links from Replatform SxC Page are not broken "
							+ driver.getCurrentUrl());
		} catch (Exception e) {
			messages.add("Result not found or problem Replatform SxC Page ");
		}
		finalAssert("'Add symptoms...' Overlay", messages);
	}

	@Test(enabled = true, priority = 9, description = "In This test we are checking Byline exist at RHS")
	public void ReplatformSxCByline() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Byline Test Case Start==================");
		// Check Byline at RHS
		try {
			WebElement byline = locateElement('s', ".css-1kx3y31 .css-2vgg1c .css-uyum6x .css-1gby43k a");
			isElementDisplayed(byline, "Byline at RHS", messages);
			
			byline.click();
			System.out.println("Author name is clickbale "+ driver.getCurrentUrl());
			try {
				WebElement articleContent = locateElement('s', ".banner_cont .container-inner .banner_info h1");
				if (articleContent.getText() == "") {
					messages.add(" Content missing on "
							+ driver.getCurrentUrl());
				}
				System.out.println("Content exist on Medical team Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add(" Content missing on "
						+ driver.getCurrentUrl() + e.toString());
			}
		
		driver.navigate().back();
		} catch (Exception e) {
			messages.add("Byline is missing at RHS on Replatform SxC Page "
					+ driver.getCurrentUrl());
		}
		finalAssert("Byline", messages);
	}

	@Test(enabled = true, priority = 10, description = "In this test we are checking 'Top Stories' title and content")
	public void ReplatformSxCTopStroies() throws InterruptedException {
		List<String> messages = new ArrayList<String>();

		System.out
				.println("==================Top Stories Test Case Start==================");
		// Checked Top Stories widget title at RHS
		try {
			WebElement topstories = locateElement('s', ".css-1kynjxe h2");
			isElementDisplayed(topstories, "Top Stories widget title at RHS",
					messages);

		} catch (Exception e) {
			messages.add("'Top Stories' title is missing at RHS on Replatform SxC Page "
					+ driver.getCurrentUrl());
		}
		// Checked 'Top Stories' section
		try {
			WebElement topstorie = locateElement('s', ".css-17mrx6g");
			isElementDisplayed(topstorie, "'Top Stories' Content", messages);
		
		} catch (Exception e) {
			messages.add("Top Stories Content missing at RHS on Replatform SxC Page "
					+ e.toString());
		}
		finalAssert("Top Stories", messages);
	}

	@Test(enabled = true, priority = 11, description = "In this test we are checking existance Share bar section"
			+ "\nNav links section"
			+ "\nNewsletter Widget and it's working or not"
			+ "\nHON CODE & TRUSTe Logo, Copy right text")
	public void ReplatformSxCFooter() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Footer Test Case Start==================");
		try {
			((JavascriptExecutor) driver)
					.executeScript("window.scrollBy(0,155)");
			WebElement footer = locateElement('s', ".css-bl1qmu");
			isElementDisplayed(footer, "Global Footer", messages);

			try {
				WebElement sharebar = locateElement('s',
				".css-uvuaek .css-1bpumci");
				isElementDisplayed(sharebar, "Share bar section", messages);
				List<WebElement> symptomlinks = sharebar.findElements(By
						.className("css-1bz72lk"));
				// Check facebook
				WebElement fb = sharebar.findElement(By
						.className("icon-hl-circle-facebook"));
				isElementDisplayed(fb, "Facebook icon", messages);

				// Check pinterest
				WebElement pinterest = sharebar.findElement(By
						.className("icon-hl-circle-pinterest"));
				isElementDisplayed(pinterest, "Pinterest icon", messages);

				// Check twitter
				WebElement twitter = sharebar.findElement(By
						.className("icon-hl-circle-twitter"));
				isElementDisplayed(twitter, "Twitter icon", messages);

				// Check google plus
				WebElement gplus = sharebar.findElement(By
						.className("icon-hl-circle-google"));
				isElementDisplayed(gplus, "Google plus icon", messages);

			} catch (Exception e) {
				messages.add("Problem in Sharebar section in Replatform SxC Page Footer..."
						+ e.toString());
			}
			// Check Nav links section from Footer
			try {
				WebElement navlinks = locateElement('s', ".css-1kxa0ff");
				isElementDisplayed(navlinks, "Nav links", messages);
			} catch (Exception e) {
				messages.add("Problem in Nav link section in Replatform SxC Page Footer..."
						+ e.toString());
			}
		
			// Check for Newsletter Widget from Footer
			try {
				WebElement newsletter = locateElement('s', ".css-3218dg .css-krli0h .css-1xfrjc1");
				isElementDisplayed(newsletter, "Newsletter Widget", messages);

				// Check newsletter title
				WebElement ntitle = locateElement('s', "h2");
				isElementDisplayed(ntitle, "Newsletter Widget's Title",
						messages);

				// Check newsletter summary
				WebElement nsummary = locateElement('s', ".css-1fmy93m");
				isElementDisplayed(nsummary, "Newsletter Widget's Description",
						messages);
				// Check Email field
				WebElement nfield = driver.findElement(By.xpath(".//form[@action='/newsletter-signup']/div[@class='css-1sf7hp4']"));
				Thread.sleep(waittime);
				isElementDisplayed(nfield, "Email field", messages);

				// Check Subscribe button
				WebElement nbutton = driver.findElement(By.xpath(".//form[@action='/newsletter-signup']/button"));
				isElementDisplayed(nbutton, "Subscribe button", messages);
				
					for (int i = 1; i < 10; i++) {
					((JavascriptExecutor) driver)
							.executeScript("window.scrollTo(0,document.body.scrollHeight);");
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,-155)");
					Thread.sleep(2000);
				}
				// Enter email id
				nfield.findElement(By.className("css-h4abg4")).sendKeys("kinjal@healthline.com");
				nbutton.click();
				Thread.sleep(3000);
				try {
					WebElement responceText = locateElement('s',
							".css-1xfrjc1 h2");
					if (responceText.getText().indexOf("thank you!") < 0) {
						messages.add("Thank you! message not display after clicking Subscribe button.");
					} else {
						System.out
								.println("Thank you! message display display after clicking Subscribe button.");
					}
				} catch (Exception e) {
					messages.add("Thank you! message not display after clicking Subscribe button.");
				}

			} catch (Exception e) {
				messages.add("Problem in Newsletter Widget in Replatform SxC Page Footer... "
						+ e.toString());
			}
			// Copyright
			try {

				WebElement copytext = locateElement('s',
				".css-1i6m0f3 .css-1cabjds .css-dzcuj9");
				isElementDisplayed(copytext, "Copyright Text", messages);
			} catch (Exception e) {
				messages.add("Copyright Text is missing on Replatform SxC Page "
						+ e.toString());
			}
			// Honcode
			try {
				WebElement honlogo = locateElement('s',
						".css-1i6m0f3 .css-1cabjds .css-zduqor img");
				isElementDisplayed(honlogo, "Honcode logo", messages);
			} catch (Exception e) {
				messages.add("Honcode logo not displayed in Replatform SxC Page Footer  "
						+ e.toString());
			}
			// Truste logo
			try {

				WebElement truste = locateElement('s',
						".css-1i6m0f3 .css-1cabjds .css-1laalhl img");
				isElementDisplayed(truste, "TRUSTe logo", messages);
			} catch (Exception e) {
				messages.add("TRUSTe logo not displayed in Replatform SxC Page Footer  "
						+ e.toString());
			}
		} catch (Exception e) {
			messages.add("Problem in Footer Replatform SxC Page..."
					+ e.toString());
		}
		finalAssert("Footer", messages);
	}

	@Test(enabled = true, priority = 12, description = "In this test we are checking Ads like Top LB, Inline LB, DMR, RNA, Footer LB")
	public void ReplatformSxCAds() throws InterruptedException {
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

					messages.add("Top DLB Ad is missing..."
							+ driver.getCurrentUrl());
				} else {
					// Check label ADVERTISEMENT
					try {
						String googleQueryId = driver.findElement(By.id("dlb1__slot"))
								.getAttribute("data-google-query-id");
						String gogleItemId = driver.findElement(By.className("css-1im4ljs"))
						.getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.className("css-1im4ljs"))
						.getAttribute("data-creative-id");
						if (googleQueryId.isEmpty()) {
							messages.add("Top DLB Ad is not fired..."
									+ driver.getCurrentUrl());
						} else {
							System.out
									.println("Top DLB Ad is fired...google Query Id is --> "
											+ googleQueryId);
							System.out
							.println("Top DLB Ad google item id is --> "+gogleItemId);
							System.out
							.println("Top DLB Ad google creative id is --> "+googleCreativeId);
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
								.className("css-37r8be"));
						// Check stickiness - if DLB is visible for 3seconds
						// than after 1 second check that it disappeared
						Thread.sleep(1000);
						try {
							stickyLayer = driver.findElement(By
									.className("css-1vwmzfs"));
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
				messages.add("Problem with Top DLB Ad... on Replatform SxC Page "
						+ e.toString());
			}
			Thread.sleep(2000);
			((JavascriptExecutor) driver)
					.executeScript("window.scrollTo(0,document.body.scrollHeight);");
			Thread.sleep(2000);

			// Inline DLBs
			try {
				List<WebElement> tag = driver.findElements(By
						.xpath(".//li[@class='css-vusf7j']"));

				// set counter to check inline dlb after 5th result then 15th,
				// 25th, 35th....
				int adcounter = 2;
				int j = 4;
				for (int i = j; i < tag.size() - 1;) {

					String xpath = ".//li[@class='css-vusf7j']["
							+ i
							+ "]/following-sibling::li[@class='css-vqovw9']/div";

					try {
						WebElement topAd = driver.findElement(By.xpath(xpath));
						WebElement advt = topAd.findElement(By.tagName("div"));

						Thread.sleep(5000);
						if (advt.getAttribute("id").equalsIgnoreCase(
								"DLB" + adcounter + "__slot")) {

							String inlineLB = advt
									.getAttribute("data-google-query-id");
							String gogleItemId = topAd.getAttribute("data-line-item-id");
							String googleCreativeId = topAd.getAttribute("data-creative-id");
							if (inlineLB.isEmpty()) {
								messages.add("Inline DLB"
										+ adcounter
										+ " is not fired...on Replatform SxC Page "
										+ driver.getCurrentUrl());
							} else {
								System.out
								.println("Inline DLB "
										+ adcounter
										+ " Ad is fired...google query id is --> "
										+ inlineLB);
						System.out
						.println("Inline DLB "
								+ adcounter
								+ " Ad is fired...google item id is --> "+gogleItemId);
						System.out
						.println("Inline DLB "
								+ adcounter
								+ " Ad is fired...google creative id is --> "+googleCreativeId);
							}
						} else {
							messages.add("Inline DLB " + adcounter
									+ " Ad is missing after section " + (i + 1));
						}
					} catch (Exception e) {
						messages.add("Inline DLB " + adcounter
								+ " Ad is missing after section " + (i + 1)
								+ e.toString());
					}
					adcounter = adcounter + 1;

					i = i + 10;
				}

			} catch (Exception E) {
				messages.add("Problem with Inline DLB Ad on Replatform SxC Page "
						+ E.toString());
			}
			// Check DMRs
			try {
				// int wdth;
				List<WebElement> dmrs = driver.findElement(
						By.className("css-1kx3y31")).findElements(
						By.className("css-1eh39tj"));
				for (int i = 1; i <= dmrs.size(); i++) {
					try {
						
						String dmr = driver.findElement(
								By.id("dmr" + i + "__slot")).getAttribute(
								"data-google-query-id");
						String gogleItemId = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']//div[@class='css-1im4ljs']")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.xpath(".//section[@class='css-1kx3y31']//div[@class='css-1im4ljs']")).getAttribute("data-creative-id");
						Thread.sleep(5000);
						if (dmr.isEmpty()) {
							messages.add("DMR"
									+ i
									+ " Ad at right rail on Replatform SxC Page is not fired.."
									+ driver.getCurrentUrl());
						} else {
							System.out
							.println("DMR"
									+ i
									+ " ad is fired ...google query id is --> "
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
								+ " Ad is missing on Replatform SxC Page ");
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
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					System.out
							.println("Footer DLB Ad is fired...Advertisement id is --> "
									+ adfooetrLbid);
				}
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Replatform SxC Ads test case exception ..."
					+ e.toString());
		}
		finalAssert("Ads", messages);

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
	@Test(enabled = true, priority = 14,description = "In this test we are checking GA Events.")
    public void testGAEvents(){
		List<String> messages = new ArrayList<String>();
  		boolean msgFound=false;
  		LogEntries perflogentries = driver.manage().logs().get(LogType.PERFORMANCE);
  		for (LogEntry entry : perflogentries) {
  			int startval=0;
  			int endval=0;

  			try {
   				if ((entry.getMessage().contains("collect?v"))==true)
  				{
  				JSONParser parser = new JSONParser();
  				JSONObject jsonObj = (JSONObject) parser.parse(entry.getMessage());
  				String ur= printJsonObject((JSONObject)jsonObj,gaEvents1);
  				
  			   Set<String> keys = gaEvents1.keySet();
		        for (String keyy : keys)
		        {
		           System.out.println(keyy+"="+gaEvents1.get(keyy));
		           if(gaEvents1.get(keyy).equals("Facebook")){
		        	  msgFound=true; 
		           }
		        }
  				 
  				}
  			} 
  			catch (Exception e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
  		} 
  		
if(msgFound=false){
	messages.add("GA Event for Hamburger Menu Click is not registered ...");
}
finalAssert("GA Events", messages);		
     
    }
	@Test(enabled = true, priority = 15, description = "In This test we are checking 'Comscore' tag for beacon.js from view sources.")
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
   public String printJsonObject(JSONObject jsonObj,HashMap<String, String> gaEvents1){
	String str="";
			for (Object key : jsonObj.keySet()) {
			        //based on you key types
			        String keyStr = (String)key;
			        Object keyvalue = jsonObj.get(keyStr);

			        //Print key and value
			        if (keyStr.equals("url")){
			        str=URLDecoder.decode(keyvalue.toString());
			        URL url;
					try {
						url = new URL(str);
					
			        String query = url.getQuery();
			        if (query!=null){
			        Map<String, String> map = getQueryMap(query,gaEvents1);
			     
			        }
			        return str;
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        }
			        //for nested objects iteration if required
			        if (keyvalue instanceof JSONObject){
	
			           printJsonObject((JSONObject)keyvalue,gaEvents1);
			        }
			    }
				return str;
   }
   
   public static Map<String, String> getQueryMap(String query,HashMap<String, String> gaEvents1)
   {
       String[] params = query.split("&");
       Map<String, String> map = new HashMap<String, String>();
       for (String param : params)
       {
           String name = param.split("=")[0];
           String value="";
           if(name.equals("ea") || name.equals("el")){
        	   value = param.split("=")[1]; 
           
           
           map.put(name, value);
           gaEvents1.put(name, value);
          
           }
       }
       return map;
   }
	
	
	@AfterClass
	public static void endWebDriver() {
		driver.quit();
	}
}