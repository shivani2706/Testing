package replatformProject;

import static org.testng.AssertJUnit.assertEquals;

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

import org.apache.commons.collections.ClosureUtils;
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

import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.webkitRTCPeerConnection;

import qalib.CheckAds;
import qalib.CheckHttpConnection;
import qalib.SelectRandomURL;
import org.openqa.selenium.WebDriver;

public class ReplatformHealthNewsArticle {
	static Map<String, String> env;
	static String driverType;
	private static String baseURL;
	private static String newsarticle;
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
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		domain=baseURL;
		newsarticle=env.get("newsarticle");

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
	public void checkColorOnMouseHoverHealthNews(WebElement elem, String Componant,
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
		if(elem.getCssValue("color").indexOf("rgba(180, 32, 114")<0){
			messages.add(Componant+" color not changed to pink on mouse hover on "+ driver.getCurrentUrl());	
		}
		
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
		if(elem.getCssValue("color").indexOf("rgba(236, 28, 130")<0){
			messages.add(Componant+" color not changed to pink on mouse hover on "+ driver.getCurrentUrl());	
		}
		
	}
	public void checkColorOnMouseHoverforFact(WebElement elem, String Componant,
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
				+ elem.getCssValue("background-color"));
		if(elem.getCssValue("background-color").indexOf("rgba(236, 28, 130")<0){
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

	
	@Test(enabled = true, priority = 1, description = "In this test we are checking 'HEALTH NEWS' Branding exist"
			+ "\nColor of 'HEALTH NEWS' Branding before & after mouse hover"
			+ "\nClick 'HEALTH NEWS' Branding and back to News Article")
	public void ReplatformNewsBranding() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		try {
			newsarticle = "newsarticle.txt";
			 SelectRandomURL s=new SelectRandomURL();
			 String url= s.choose(new File(newsarticle));
			 driver.get(baseURL+url);
			 Thread.sleep(2000);   
		System.out
				.println("=================='HEALTH NEWS' Branding Test Case Start==================");
		// Check 'HEALTHLINE NEWS' branding
		try {
			WebElement topbranding = locateElement('s', ".css-115ig6b .css-1c8s6kl h4");
			isElementDisplayed(topbranding, "'HEALTH NEWS' Branding",
					messages);

			// Check 'HEALTHLINE NEWS' branding color
			checkColorOnMouseHoverHealthNews(topbranding, "'HEALTH NEWS' Branding", messages);
			Thread.sleep(1000);
			
			// Click 'HEALTHLINE NEWS' branding
			topbranding.click();
				System.out.println("'HEALTH NEWS' branding is clickbale "+ driver.getCurrentUrl());
				try {
					WebElement articleContent = locateElement('s', ".css-d36iui");
					if (articleContent.getText() == "") {
						messages.add(" Content missing on "
								+ driver.getCurrentUrl());
					}
					System.out.println("Content exist on Replatform News Article "
							+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add(" Content missing on "
							+ driver.getCurrentUrl() + e.toString());
				}
			
			driver.navigate().back();
			Thread.sleep(waittime);
		} catch (Exception e) {
			messages.add("'HEALTH NEWS' Branding missing on Replatform News Article "
					+ e.toString());
		}
		}
		catch (Exception e) {
			messages.add(" Problem on Replatform News Article "
					+ driver.getCurrentUrl() + e.toString());
		}
		finalAssert("'HEALTH NEWS' Branding", messages);
	}

	@Test(enabled = true, priority = 2, description = "In this test we are checking 'Fact Checked' exist"
			+ "\nColor of 'Fact Checked' Badge before & after mouse hover"
			+ "\nClick 'Fact Checked' Badge and check content and close overlay")
	public void ReplatformNewsFactCheckedBadge() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Fact Checked Branding Badge Test Case Start==================");
		// Check 'Fact Checked' branding
		try {
			WebElement topbranding = locateElement('s', ".css-b3a5f9");
			isElementDisplayed(topbranding, "'Fact Checked' Badge", messages);

			// Check 'Fact Checked' branding Badge color
			checkColorOnMouseHoverforFact(topbranding, "'Fact Checked'", messages);
						// Click 'Fact Checked' branding Badge
			try {
				
				topbranding.click();
				WebElement overlay = locateElement('s', ".css-mjgn4d");
				if (overlay.isDisplayed()) {

					WebElement articleContent = locateElement('s',
							".css-19lgokg");
					if (articleContent.getText() == "") {
						messages.add(" Content missing on "
								+ driver.getCurrentUrl());
					}
					System.out.println("Content exist in Fact Overlay "
							+ driver.getCurrentUrl());
					WebElement Close = overlay.findElement(By
							.className("icon-hl-close"));
					Close.click();
				}
			} catch (Exception e) {
				messages.add("'Fact Checked' Badge is not clickable "
						+ driver.getCurrentUrl());
			}

			Thread.sleep(waittime);

		} catch (Exception e) {
			messages.add("'Fact Checked' Badge missing on Replatform News Article "
					+ e.toString());
		}
		finalAssert("'Fact Checked' Badge", messages);
			}

	@Test(enabled = true, priority = 3, description = "In this test we are checking H1 tag(title) exist")
	public void ReplatformNewsTitle() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Title Test Case Start==================");
		// Check Title
		try {
			WebElement title = locateElement('s', ".css-115ig6b h1");
			isElementDisplayed(title, "Title", messages);
		} catch (Exception e) {
			messages.add("Title missing on Replatform News Article "
					+ e.toString());
		}
		finalAssert("Title", messages);
	}

	@Test(enabled = true, priority = 4, description = "In this test we are checking Deck exist")
	public void ReplatformNewsDeck() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Deck Test Case Start==================");
		// Check Deck
		try {
			WebElement deck = locateElement('s', ".css-1wip6xr");
			isElementDisplayed(deck, "Deck", messages);
		} catch (Exception e) {
			messages.add("Deck missing on Replatform News Article "
					+ e.toString());
		}
		finalAssert("Deck", messages);
	}
	@Test(enabled = true, priority = 5, description = "In this test we are checking Landscape Image")
	public void ReplatformNewsLanscapeImg() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Landscape Image Test Case Start==================");
		// Check Landscape Image
		try {
			WebElement lndimg = driver.findElement(By.xpath(".//*[@class='css-115ig6b']/div[2]/span"));
			isElementDisplayed(lndimg, "Lndimg", messages);
		} catch (Exception e) {
			messages.add("Landscape Image missing on Replatform News Article "
					+ e.toString());
		}
		finalAssert("Landscape Image", messages);
	}
	@Test(enabled = false, priority = 6, description = "In this test we are checking Image" + "Click Pin icon on mouse hovering image and click icon.")
	public void ReplatformNewsPininImage() throws InterruptedException {
		// Right now pin is not available so it's false
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		System.out
				.println("=================Pinterest Icon on Image Test Case Start==================");
		try {
			// Check Center Content
			
			WebElement content1 = locateElement('s', ".css-p9in6m");
			isElementDisplayed(content1, "Landscape Image",messages);
			
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", content1);
			jse.executeScript("window.scrollBy(0,-150)", "");
			Thread.sleep(5000);
			
			WebElement clickiconarea = driver.findElement(By.xpath("//span[@class='css-187all6']"));
		
      		Actions action = new Actions(driver);
			Thread.sleep(5000);
			action.moveToElement(clickiconarea).perform();
			Thread.sleep(5000);
			if (clickiconarea.isDisplayed()) {
				System.out
						.println("Pin icon displayed when clicking on Image.");
			}
			else {
					messages.add("Pin icon Not displayed when clicking image "
						+ driver.getCurrentUrl());
			}
			try{	
				String parentWindow = driver.getWindowHandle();
			WebElement clickicon = driver.findElement(By.xpath("//span[@class='css-187all6']/span[@class='css-1nkpegb']/div[@class='css-1p4gj3n']/a"));
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", clickicon);
			ArrayList<String> tabs = new ArrayList<String>(
					driver.getWindowHandles());

			// Switch to new window
			driver.switchTo().window(tabs.get(1));
			Thread.sleep(5000);
			
				System.out
						.println("Pin icon is clicked and open in new tab.");
				driver.close();// do some action in new window(2nd tab)
				driver.switchTo().window(parentWindow);
			}
			catch (Exception e) {
				messages.add("Pin icon is not clicked and open in new tab. "
						+ driver.getCurrentUrl());
			} 
		} catch (Exception e) {
			messages.add("Landscape Image is missing on Replatform News Article "
					+ e.toString());
		}
		finalAssert("Pinterest Icon on Image", messages);
	}
	@Test(enabled = true, priority = 7, description = "In this test we are checking Center Content exist Below landscape Image")
	public void ReplatformNewsCenterContent() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Center Content Test Case Start==================");
		// Check Center Content
		try {
			WebElement content = locateElement('s', ".css-1tkhpnj");
			isElementDisplayed(content,
					"Center Content Below landscape Image ", messages);
		} catch (Exception e) {
			messages.add("Center Content missing Below landscape Image on Replatform News Article "
					+ e.toString());
		}
		finalAssert("Center Content", messages);
	}

	
	@Test(enabled = true, priority = 8, description = "In this test we are checking Sharebar exist"
			+ "\nColor of FB, Twitter, Print, feedback smile & frown icons before & after mouse hover"
			+ "\nClick FB, Twitter & Email icons"+ "\nClick feedback smile & frown icons and check sublinks from overlay.Click 1st link from Smile overlay and closed it.")
	public void ReplatformNewsShareTools() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Sharebar Test Case Start==================");
		// Check Share Tools
		

		WebElement sharebar = new WebDriverWait(driver, 25).until(ExpectedConditions.visibilityOfElementLocated(By
				.cssSelector(".css-xsfqmw")));
		
		((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", locateElement('s',
					".css-10wvu0d"));
			jse.executeScript("window.scrollBy(0,-255)", "");
			Thread.sleep(3000);
			
					if (!(sharebar.isDisplayed())) {
				messages.add("Sharebar & Feedback section is missing on Replatform News Article "
						+ driver.getCurrentUrl());
				
			} else {
				//Print Icon
				try {
				
					WebElement printicon = locateElement('s',
							".css-ea7map .print a");
										((JavascriptExecutor) driver).executeScript(
							"arguments[0].scrollIntoView(true);", printicon);
					jse.executeScript("window.scrollBy(0,-150)", "");
					
					isElementDisplayed(printicon, "Print Icon", messages);
				
				} catch (Exception e) {
					messages.add("Print Icon is missing on Replatform News Article, "
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
					if (!driver.getCurrentUrl()
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
					messages.add("FB Icon is missing Replatform News Article, "
							+ driver.getCurrentUrl());
				}

				// Twitter Icon
				try {
					WebElement twittericon = locateElement('s',
							".css-ea7map .twitter a");
					isElementDisplayed(twittericon, "Twitter Icon", messages);
					checkColorOnMouseHover(twittericon, "Twitter Icon", messages);
					Thread.sleep(5000);
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
									"https://twitter.com/intent/tweet?text=")) {
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
					messages.add("Twitter Icon is missing on Replatform News Article, "
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
					messages.add("Email Icon is missing on Replatform News Article, "
							+ driver.getCurrentUrl());
				}
				// Sharebar End

				// Feedback Smile icon
				try {
					WebElement smileicon = locateElement('s',
							".css-98bjb0 .icon-hl-smile");
					isElementDisplayed(smileicon, "Feedback Smile Icon",
							messages);
										// Check Feedback Smile Icon color
					checkColorOnMouseHover(smileicon, "Feedback Smile Icon",
							messages);
					((JavascriptExecutor) driver).executeScript(
							"arguments[0].scrollIntoView(true);", smileicon);

					jse.executeScript("window.scrollBy(0,-150)", "");
					String parentWindowHandler = driver.getWindowHandle();
					String subWindowHandler = null;
					smileicon.click();
					System.out.println("Smile icon clicked ");
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
							messages.add("'This article changed my life!' overlay title is missing on Replatform News Article "
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
					checkColorOnMouseHover(frownicon, "Feedback Frown Icon", messages);
					Thread.sleep(3000);
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
		
		finalAssert("Sharebar & Feedback", messages);
	}

	@Test(enabled = true, priority = 9, description = "In this test we are checking existance 'Read This Next' section title"
			+ "\nChecked All Links from Read Next section is not broken"
			+ "\nColor of a Read This Next section Article Title before & after mouse hover")
	public void ReplatformNewsReadNext() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Read This Next Test Case Start==================");
		// Checked Read Next Section title

		try {
			WebElement readnext = locateElement('s',
			".css-10ib5jr h3");
			isElementDisplayed(readnext, "'Read This Next' section title",
					messages);

			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", readnext);
			jse.executeScript("window.scrollBy(0,-200)", "");

		} catch (Exception e) {
			messages.add("'Read This Next' section title is missing on Replatform News Article "
					+ e.toString());
		}

		String href = null;
		try {
			List<WebElement> readnextlists = driver.findElement(
					By.className("css-1l95nvm")).findElements(
					By.className("css-s6ma3s"));
			for (WebElement readnextlist : readnextlists) {
				WebElement lnk = locateElement('s', ".css-egoa7 .css-kkxwou .css-u80ajq a");
			
				checkHttpStatus(lnk, "'Read Next' section", messages);
			}
			System.out
					.println("All Links from Read Next section from Replatform News Article are not broken "
							+ driver.getCurrentUrl());
		} catch (Exception e) {
			messages.add("Read Next section not found or problem with Read Next section links on Replatform News Article ");
		}
		// Check link color for Article Title from Read This Next Section
		WebElement colorforarticletitle = locateElement('s', ".css-u80ajq a");
			// Mouse hover
		checkColorOnMouseHover(colorforarticletitle,"Article Title", messages);
		// Check link color for Article 'READ MORE' link 
		// Section
		WebElement readmorelink = locateElement('s', ".css-u80ajq .css-120snmw a");
		checkColorOnMouseHover(readmorelink,
				"''READ MORE' link'", messages);
		finalAssert("Read This Next", messages);
	}

	@Test(enabled = true, priority = 10, description = "In This test we are checking Byline exist at RHS")
	public void ReplatformNewsByline() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		System.out
				.println("==================Byline Test Case Start==================");
		// Check Byline at RHS
		try {
			
			WebElement byline = locateElement('s', ".css-1kx3y31 .css-uyum6x");
			isElementDisplayed(byline, "Byline at RHS", messages);
		} catch (Exception e) {
			messages.add("Byline is missing at RHS on Replatform News Article "
					+ driver.getCurrentUrl());
		}
		finalAssert("Byline", messages);
	}

	@Test(enabled = true, priority = 11, description = "In this test we are checking 'Top Stories' title and content")
	public void ReplatformNewsTopStroies() throws InterruptedException {
		List<String> messages = new ArrayList<String>();

		System.out
				.println("==================Top Stories Test Case Start==================");
		// Checked Top Stories widget title at RHS
		try {
			WebElement topstories = locateElement('s', ".css-1kynjxe h2");
			isElementDisplayed(topstories, "Top Stories widget title at RHS",
					messages);

		} catch (Exception e) {
			messages.add("'Top Stories' title is missing at RHS on Replatform News Article "
					+ driver.getCurrentUrl());
		}
		// Checked 'Top Stories' section
		try {
			WebElement topstorie = locateElement('s', ".css-17mrx6g");
			isElementDisplayed(topstorie, "'Top Stories' Content", messages);
		
		} catch (Exception e) {
			messages.add("Top Stories Content missing at RHS on Replatform News Article Page "
					+ e.toString());
		}
		finalAssert("'Top Stories' section", messages);
	}

	@Test(enabled = true, priority = 12, description = "In this test we are checking Ads like Top LB, Inline LB, INA, RNA, DMR, DCMR, Footer LB")
	public void ReplatformNewsAds() throws InterruptedException {
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
				messages.add("Problem with Top dlb Ad... on Replatform News Article "
						+ e.toString());
			}

			// Inline DLBs
			Thread.sleep(2000);
			((JavascriptExecutor) driver)
					.executeScript("window.scrollTo(0,document.body.scrollHeight);");
			Thread.sleep(2000);

			try {
				
				String xpath = ".//article[@class='css-1tkhpnj']/p";
						
				List<WebElement> tag = driver.findElements(By.xpath(xpath));
								Thread.sleep(3000);
				// set counter to check inline dlb 5th and 15th para
				
				int adcounter = 2;
				int j = 0;
				if (tag.size() > 5) {
					j = 1;
					if (tag.size() > 15) {
						j = 2;
						} else {

					}
				} else {

				}
				int adcount = 1;
				for (int i = 1; i <= j;) {

					// String
					if (i == 1) {
						adcount = 5;

					} else {
						if (i == 2) {
							adcount = 15;

						} 
					}
					xpath = ".//article[@class='css-1tkhpnj']/p[" + adcount + " ]/following-sibling::div/div[@class='css-0']";
					Thread.sleep(3000);
					try {
						WebElement topAd = driver.findElement(By.xpath(xpath));
						WebElement advt = topAd.findElement(By.className("css-pu7wh4"));
						if (advt.getAttribute("id").equalsIgnoreCase(
								"dlb" + adcounter + "__slot")) {

							String inlineLB = advt
									.getAttribute("data-google-query-id");
							String tpath= ".//article[@class='css-1tkhpnj']/p[" + adcount + " ]/following-sibling::div/div[@class='css-0']/div/div/div[@class='css-1im4ljs']";
							String gogleItemId = driver.findElement(By.xpath(tpath)).getAttribute("data-line-item-id");
							String googleCreativeId = driver.findElement(By.xpath(tpath)).getAttribute("data-creative-id");
							if (inlineLB.isEmpty()) {
								messages.add("Inline DLB"
										+ adcounter
										+ " is not fired...Replatform News Article "
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
					adcounter++;

					i = i + 1;
				}

			} catch (Exception E) {
				messages.add("Problem with inline dlb Ad on News Article Page"
						+ E.toString());
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
					.println("INA Ad is fire ...Advertisement id is --> "+googleQueryId);
					
				}
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());
			}
			
			// Check DMRs
			try {
			
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
						if (dmr.isEmpty()) {
							messages.add("DMR"
									+ i
									+ " Ad at right rail on Replatform News Article is not fired.."
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
								+ " Ad is missing on Replatform News Article ");
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			
			//Read Next DCMRs
			try {
				
				WebElement readnxtContent = driver.findElement(By
						.className("css-1l95nvm"));
				List<WebElement> dcmrs = readnxtContent.findElements(By.className("css-s6ma3s"));
				for (int i = 1; i <= 5; i++) {
					try {
						String dcmr = driver.findElement(
								By.id("dcmr" + i + "__slot")).getAttribute(
								"data-google-query-id");
						String gogleItemId = readnxtContent.findElement(By.className("css-1ca3fuk")).findElement(By.className("css-1im4ljs")).getAttribute("data-line-item-id");
						String googleCreativeId = readnxtContent.findElement(By.className("css-1ca3fuk")).findElement(By.className("css-1im4ljs")).getAttribute("data-creative-id");
						if (dcmr.isEmpty()) {
							messages.add("DCMR"
									+ i
									+ " Ad at right rail on Replatform Tabbed Article is not fired.."
									+ driver.getCurrentUrl());
						} else {
							System.out
							.println("DCMR"
									+ i
									+ " ad is fired ...google query id is--> "
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
								+ " Ad is missing on Replatform Tabbed Article ");
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
			messages.add("Replatform News Article Ads test case exception ..."
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
