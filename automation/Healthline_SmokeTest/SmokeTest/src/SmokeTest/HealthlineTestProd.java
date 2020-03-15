package SmokeTest;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.BeforeClass;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.openqa.selenium.Keys;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;

import qalib.CheckHttpConnection;
import qalib.SelectRandomURL;

public class HealthlineTestProd {

	/* To Disable HtmlUnit Driver verbos */
	static Map<String, String> env;
	static String driverType;
	private static String baseURL;
	private static WebDriver driver;
	public static int waittime = 2000;
	static int dayOfWeek;
	static int dayOfMonth;
	public static boolean newHeader = true;
	public static boolean newFooter = true;

	@BeforeTest
	public static void startWebDriver() {

		env = System.getenv();
		driverType = env.get("DRIVER");
		baseURL = env.get("URL");

		if (driverType.equals("firefox")) {
			driver = new FirefoxDriver();
		} else if (driverType.equals("ie11")) {
			System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		} else if (driverType.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

			driver = new ChromeDriver();
		} else {
			System.out.println("Unknown driver");
		}
		driver.manage().window().maximize();
	}

	public void clearDriver() {
		driver.manage().deleteAllCookies();

	}

	@Test(enabled = true, priority = 1, description = "In this test we are taking list of health tool links from top right section of home page. We are checking their status. Test fail if status of any link is not 200 or 301.")
	public void HOMEPageTest() throws InterruptedException {
		List<String> messages = new ArrayList<String>();

		String href = null;
		System.out
				.println("==================HOMEPageTest start==================");

		try {
			driver.get(baseURL);
			Thread.sleep(waittime);

			List<WebElement> featuredTools = driver.findElements(By
					.xpath("//*[@class='top_cont']//*[@class='top_list']//li"));
			for (WebElement featuredTool : featuredTools) {
				WebElement lnk = featuredTool.findElement(By.tagName("a"));
				href = lnk.getAttribute("href");
				if (href != null) {
					CheckHttpConnection urlHttpConn = new CheckHttpConnection(
							href);
					int status = urlHttpConn.testHttpConn();
					if (status != 200 && status != 301) {
						messages.add(href
								+ " Problem with Health tool section link"
								+ status);
					}
				}
			}
			System.out
					.println("All Links from Health Tools section from HL home page are not broken "
							+ driver.getCurrentUrl());
		} catch (Exception e) {
			messages.add("Health tool section not found or problem with Health tool section links on HL home page ");
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================HOMEPageTest end==================");
	}

	@Test(enabled = true, priority = 2, description = "In This test we are checking different elements of old gloabal header & redesigned global header."
			+ "\nLogo - It Locates logo, click it and check h2 on HL home page "
			+ "\nTopics & Tools - Locate Topics & Tools button, click it, check for drop down and links inside it. Check status of each links. Click on 3rd link i.e ADHD from drop down"
			+ "\nADHD Topic center - Check title & contents, click back arrow of browser "
			+ "\nIt click 34th link of dropdown i.e pill identifier"
			+ "\nPill Identifier page: Check title is 'Pill Identifier'"
			+ "\nCheck newsletter button/link, existance of overlay , form submit & thank you message"
			+ "\nCheck existance of Search textbox, fill it with search term, submit form and check result on search result page")
	public void Header() throws InterruptedException {

		List<String> messages = new ArrayList<String>();
		// String newheader = true;
		System.out
				.println("==================Header test case start - Old header==================");
		try {
			driver.get(baseURL + "/directory/topics");
			String title = null;
			try {
				WebElement header = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector("#hl_global_header")));

				try {

					WebElement logo = new WebDriverWait(driver, 15)
							.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.cssSelector(".logo img")));
					if (!(logo.isDisplayed())) {
						messages.add("HL logo not displayed from Global Header HL home page"
								+ driver.getCurrentUrl());
					}
					System.out.println("HL logo displayed in Global Header"
							+ driver.getCurrentUrl());

				} catch (Exception e) {
					messages.add("HL logo not displayed from Global Header."
							+ driver.getCurrentUrl());
				}

			} catch (Exception e) {
				messages.add("Some issue with HL Global Header "
						+ driver.getCurrentUrl());
			}

			// Test Topic and Tools nav button
			try {
				driver.navigate().refresh();
				WebElement Topicsandtools = new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector("#hl_global_header #topic_center li a")));

				Topicsandtools.click();
				Thread.sleep(waittime);
				List<WebElement> dropDownLinks = driver.findElement(
						By.className("container-fluid")).findElements(
						By.tagName("a"));

				if (dropDownLinks.size() <= 0) {
					messages.add("No links listed in Health Topics Nav Dropdown List ");
				}
				System.out
						.println("Topic and Tools nav button exist on Global Header "
								+ driver.getCurrentUrl());
				System.out
						.println(dropDownLinks.size()
								+ " links listed in Global Header Health Topics Nav Dropdown List "
								+ driver.getCurrentUrl());
				for (WebElement topicLink : dropDownLinks) {
					String link = topicLink.getAttribute("href");

					CheckHttpConnection httpConn = new CheckHttpConnection(link);
					int status = httpConn.testHttpConn();
					if (status != 200 && status != 301) {
						messages.add(link
								+ " - Broken Page - Status= "
								+ status
								+ " from Topics & Tools Global Header dropdown ");
					}
				}
				dropDownLinks.get(19).click();
				Thread.sleep(waittime);

				try {
					driver.navigate().refresh();

					WebElement title1 = new WebDriverWait(driver, 25)
							.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.cssSelector(".page-title")));
					if (title1.getText().equals("")) {
						messages.add("Title missing for topic center is wrong ");
					}
					System.out.println("Title exist on Health Topic page "
							+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add(" Title missing on " + driver.getCurrentUrl()
							+ e.toString());
				}
				try {

					WebElement articleContent = driver.findElement(By
							.className("article-body"));
					if (articleContent.getText() == "") {
						messages.add(" Content missing on "
								+ driver.getCurrentUrl());
					}
					System.out.println("Content exist on Health " + " page "
							+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add(" Content missing on "
							+ driver.getCurrentUrl() + e.toString());
				}

				driver.navigate().back();
			} catch (Exception e) {
				messages.add("Topic and Tools nav button is missing "
						+ e.toString() + driver.getCurrentUrl());
			}

			// Test Newsletter

			try {
				String parentWindow = driver.getWindowHandle();
				WebElement Newsletter = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector("#hl_global_header #topic_center .newsletterLink a")));

				if (Newsletter.getAttribute("href").contains(
						"#overlayToptocPopup")) {
					System.out
							.println("Newsletter button exist on Global header");
					Newsletter.click();

					WebElement overlay = new WebDriverWait(driver, 15)
							.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.cssSelector(".hl-overlay")));

					if (overlay.isDisplayed()) {
						System.out
								.println("Newsletter overlay displayed on clicking Newsletter button from global header");

						WebElement email = new WebDriverWait(driver, 15)
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector(".hl-email-toptoc-field #hl-nw-toptocpopup-email")));
						email.clear();
						email.sendKeys("prodigyinfosoft.p.ltd@gmail.com");
						Thread.sleep(waittime);
						WebElement send = overlay.findElement(By
								.id("hl-toptocFormSubmission"));
						send.click();

						WebElement responceText = new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector("#hl-response-block .hl-res-text")));
						if (responceText.getText() == "") {
							messages.add("Thank you message not display after newsletter form submit from Newsletter Overlay.");
						}
						System.out
								.println("Thank you message display after newsletter form submit from Newsletter Overlay.");
						WebElement Close = overlay.findElement(
								By.className("overlay-close-container"))
								.findElement(By.className("hl-icon-X"));
						if (!(Close.isEnabled())) {
							messages.add("Close icon not displayed at Newsletter Overlay.");
						}
					}

				} else {
					System.out.println("Newsletter Hyperlink Present...");
					Newsletter.click();
					Thread.sleep(waittime);
					ArrayList<String> tabs = new ArrayList<String>(
							driver.getWindowHandles());

					// Switch to new window
					driver.switchTo().window(tabs.get(1));
					System.out
							.println("Newsletter entrypoint exist in global header "
									+ driver.getCurrentUrl());
					Thread.sleep(waittime);
					try {
						WebElement nwtit = driver.findElement(
								By.className("hl-first-step-container"))
								.findElement(By.tagName("h3"));
						if (nwtit.getText().indexOf(
								"Get our flagship newsletter,") != -1) {
							System.out
									.println("On clicking newsletter button Newsletter page display "
											+ driver.getCurrentUrl());
						} else {

							messages.add("On clicking newsletter button Newsletter page doesn't display "
									+ driver.getCurrentUrl());
							System.out
									.println("On clicking newsletter button Newsletter page doesn't display "
											+ driver.getCurrentUrl());
						}
					} catch (Exception e) {
						messages.add("On clicking newsletter button Newsletter page doesn't display "
								+ e.toString());
						System.out
								.println("On clicking newsletter button Newsletter page doesn't display "
										+ e.toString());
					}
					driver.close();// do some action in new window(2nd tab)
					// Switch to main/parent window
					driver.switchTo().window(parentWindow);
				}
			} catch (Exception e) {
				messages.add("Newsletter button is missing " + e.toString());

			}
			// Test Search field
			driver.navigate().refresh();
			Thread.sleep(waittime);

			try {

				driver.navigate().refresh();
				WebElement Search = driver
						.findElement(By.id("hl_global_header"))
						.findElement(By.id("topic_center"))
						.findElement(By.className("search"))
						.findElement(By.className("headSearchBox"))
						.findElement(By.id("___gcse_2"))
						.findElement(By.id("gsc-i-id3"));
				System.out.println("Search textbox exist in global header "
						+ driver.getCurrentUrl());
				Search.clear();
				Search.sendKeys("knee");
				Search.sendKeys(Keys.ENTER);
				Thread.sleep(waittime);

				try {
					WebElement Searchrs = driver
							.findElement(By.id("___gcse_1"));

					System.out
							.println("On submitting search form , Search result page display "
									+ driver.getCurrentUrl());

				} catch (Exception e) {
					messages.add("On submitting search form , Search result page doesn't display "
							+ e.toString());
				}

			} catch (Exception e) {
				messages.add("Search textbox is missing" + e.toString());
			}

		} catch (Exception e) {
			messages.add("Page loading problem for" + e.toString());
		}
		System.out
				.println("==================Header test case end - Old header==================");
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");

		// ******** New Header from Redesign Article************
		// Check for Global Header

		if (newHeader) {
			messages = new ArrayList<String>();
			System.out
					.println("==================Header test case start - New header==================");
			try {
				driver.get(baseURL + "/health/a1c");
				try {
					WebElement header = new WebDriverWait(driver, 15)
							.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.cssSelector(".header")));
					Thread.sleep(waittime);
					System.out
							.println("Global Header displayed on Redesign Tabbed Article "
									+ driver.getCurrentUrl());
					if (!(header.isDisplayed())) {
						messages.add("Global Header is not displayed on Redesign Tabbed Article "
								+ driver.getCurrentUrl());

					}

					try {
						// Check for Hamburger Menu from Header
						WebElement menu = header.findElement(
								By.className("nav__control-container"))
								.findElement(By.id("mainnav-link"));
						Thread.sleep(waittime);
						if (!(menu.isDisplayed())) {
							messages.add("Hamburger Menu not displayed in Redesign Header "
									+ driver.getCurrentUrl());
						}
						System.out
								.println("Hamburger Menu displayed in Redesign Header "
										+ driver.getCurrentUrl());
					} catch (Exception e) {
						messages.add("Hamburger Menu not displayed from Redesign Header "
								+ driver.getCurrentUrl());
					}

					// Check for Newsletter button from Header
					try {
						WebElement newsletter = header.findElement(
								By.className("newsletter--header-link"))
								.findElement(By.tagName("a"));
						Thread.sleep(waittime);
						if (!(newsletter.isDisplayed())) {
							messages.add("Newsletter button/link not displayed from Redesign Header "
									+ driver.getCurrentUrl());
						}
						System.out
								.println("Newsletter button/link displayed from Redesign Header "
										+ driver.getCurrentUrl());
					} catch (Exception e) {
						messages.add("Newsletter button/link not displayed from Redesign Header.."
								+ driver.getCurrentUrl());
					}
					// Check for HL Logo from Header
					try {
						WebElement logo = header
								.findElement(
										By.className("header__center-block"))
								.findElement(By.className("header__logo"))
								.findElement(By.tagName("img"));

						Thread.sleep(waittime);
						if (!(logo.isDisplayed())) {
							messages.add("HL logo not displayed from Redesign Header "
									+ driver.getCurrentUrl());
						}
						System.out
								.println("HL logo displayed in Redesign Header "
										+ driver.getCurrentUrl());
					} catch (Exception e) {
						messages.add("HL logo not displayed from Redesign Header."
								+ driver.getCurrentUrl());
					}
					// Check for Search from Header
					try {
						WebElement search = header.findElement(
								By.className("search__container")).findElement(
								By.className("gsc-control-searchbox-only"));
						Thread.sleep(waittime);
						if (!(search.isDisplayed())) {
							messages.add("Search text box not displayed in Redesign Header "
									+ driver.getCurrentUrl());
						}
						System.out
								.println("Search text box displayed in Redesign Header "
										+ driver.getCurrentUrl());
					} catch (Exception e) {
						messages.add("Search not displayed from Redesign Header."
								+ driver.getCurrentUrl());
					}
				} catch (Exception e) {
					messages.add("Global Header is not displayed on Redesign Tabbed Article."
							+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				messages.add("Page loading problem or Header elements missing on "
						+ e.toString());
			}

			dispMessage = "";
			if (messages.size() > 0) {
				for (String msg : messages) {
					dispMessage = dispMessage + msg + "\n";
				}
			}
			AssertJUnit.assertTrue(dispMessage, dispMessage == "");
			System.out
					.println("==================Header test case end - New header==================");
		}

	}

	@Test(enabled = true, priority = 3, description = "In this test we are checking title & breadcrumb exist on Health Topics Directory page"
			+ "\nChecking existance, status and order of LHS Filters"
			+ "\nTaking list of center well links and click 2nd link"
			+ "\nCheck title & contents on topic center page and click back arrow of browser"
			+ "\nCheck Box Index section , take list of all box pagination link and check their status ")
	public void TopicDirectory() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		String href = null;
		System.out
				.println("==================TopicDirectory test case start==================");
		try {
			driver.get(baseURL + "/directory/topics");
			// Check title
			try {
				WebElement title = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath("//*[@class='bodyheading']//h2")));
				if (title.getText().trim().equals("")) {
					messages.add("Title text missing on Health Topics Directory page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Title exist on Health Topics Directory page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on Health Topics Directory page "
						+ driver.getCurrentUrl());
			}
			// Check Bread Crumbs
			try {

				WebElement breadCrumb = driver.findElement(By
						.xpath("//*[@class='breadcrumb']"));

				if (breadCrumb.getText().trim().equals("")) {
					messages.add("Breadcrumbs missing on Health Topics Directory page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Breadcrumbs exist on Health Topics Directory page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Breadcrumbs missing on Health Topics Directory page "
						+ driver.getCurrentUrl());
			}

			// Check Filters
			String message = "";
			try {
				List<WebElement> filterLinks = driver.findElements(By
						.xpath("//*[@class='filter']//a"));
				List<String> desiredList = asList("Symptoms", "Drugs",
						"Videos", "Slideshows", "Health Blogs", "Health News",
						"Reference Library");
				int counter = 0;
				for (WebElement filterLink : filterLinks) {
					href = filterLink.getAttribute("href");
					if (!filterLink.getText().equals(desiredList.get(counter))) {
						messages.add(" Some problem in filter section at LHS on Health Topics directory page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println(filterLink.getText()
									+ " Filter exist on LHS of Health Topics Directory page "
									+ driver.getCurrentUrl());
					CheckHttpConnection httpConn = new CheckHttpConnection(href);
					if (httpConn.testHttpConn() != 200) {
						messages.add("On " + driver.getCurrentUrl()
								+ " Broken link inside filter section " + href
								+ " status = " + httpConn.testHttpConn());
					}
					counter++;
				}
			} catch (Exception e) {
				messages.add("Filters missing at LHS on Health Topics directory page "
						+ driver.getCurrentUrl());
			}
			if (message != "") {
				messages.add(message);
			}

			// Check center well links for Common TAB
			try {
				driver.navigate().refresh();
				/*
				 * List<WebElement> centerLinks = new WebDriverWait(driver, 15)
				 * .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
				 * .xpath("//*[@class='box-directory-list']//a")));
				 */

				WebElement centerLinks = new WebDriverWait(driver, 15).until(
						ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".box-directory-list")))
						.findElement(By.linkText("Atrial Fibrillation"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", centerLinks);
				jse.executeScript("window.scrollBy(0,-150)", "");
				centerLinks.click();

				try {
					WebElement title = new WebDriverWait(driver, 15)
							.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.xpath("//*[@class='page-title']")));
					System.out.println("Title exist on Health Topic page "
							+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add(" Title missing on " + driver.getCurrentUrl()
							+ e.toString());
				}
				try {
					WebElement articleContent = driver.findElement(By
							.xpath("//*[@class='textBlock c']"));
					if (articleContent.getText() == "") {
						messages.add(" Content missing on "
								+ driver.getCurrentUrl());
					}
					System.out.println("Content exist on Health Topic page "
							+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add(" Content missing on "
							+ driver.getCurrentUrl() + e.toString());
				}
				driver.navigate().back();
				driver.navigate().refresh();
				System.out
						.println("Center well links  exist on Health Topics directory page Common TAB "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Center well links missing for 'Common' TAB on Health Topics Directory page "
						+ driver.getCurrentUrl());
			}

			// Check Box Pagination
			message = "";
			try {
				List<String> bxLinks = new ArrayList<String>();
				List<WebElement> boxLinks = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
								.xpath("//*[@class='list-pagination c']//a")));
				for (WebElement boxLink : boxLinks) {
					if (boxLink.getAttribute("href") != null) {
						CheckHttpConnection httpConnection = new CheckHttpConnection(
								boxLink.getAttribute("href"));
						if (httpConnection.testHttpConn() != 200) {
							messages.add("Link " + boxLink.getAttribute("href")
									+ " is broken status = "
									+ httpConnection.testHttpConn());
						} else {
							bxLinks.add(boxLink.getAttribute("href"));
						}
					}
				}
				System.out
						.println("Box Pagination links exist on Health Topic page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Box Pagination section missing on Health Topics directory page "
						+ driver.getCurrentUrl() + e.toString());
			}

		} catch (Exception e) {
			messages.add("page loading problem TopicDirectory test"
					+ driver.getCurrentUrl());
		}

		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================TopicDirectory test case end==================");
	}

	@Test(enabled = true, priority = 4, description = "Taking list of center well links and click 1st link"
			+ "\nOn SxC result page check for number of results")
	public void symptomsdirectory() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================symptomsdirectory test case start==================");
		try {

			driver.get(baseURL + "/directory/symptoms");
			// Check center well links for Common TAB
			try {
				List<WebElement> centerLinks2 = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
								.xpath("//*[@class='box-directory-list']//a")));
				System.out
						.println("Center well links exist on Common TAB of Health Symptoms Directory page "
								+ driver.getCurrentUrl());

				centerLinks2.get(0).click();
				// check number of result blocks on SxC result page
				List<WebElement> results = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
								.xpath(".//*[@class='hl-box-result-item']")));
				if (results.size() <= 0) {
					messages.add("Symptom search results are missing on "
							+ driver.getCurrentUrl());
				} else {
					System.out
							.println("symptom search results exist SxC result page "
									+ driver.getCurrentUrl());
				}

			} catch (Exception e) {

				messages.add("Center well links missing for Common TAB on Health Symptom Directory page "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Exception in Health Symptoms Directory page "
					+ driver.getCurrentUrl());
		}

		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================symptomsdirectory test case end==================");
	}

	// for drugs
	@Test(enabled = true, priority = 5, description = "Taking list of center well links and click 3rd link"
			+ "\nOn resulting drug page check for brans name"
			+ "\nOn Hybrid drug page & Licensed drug page checking that 'What does the pill look like.' section exist"
			+ "\nOn Generic & Branded drug page checking that 'What does the pill look like.' section does not exist ")
	public void drugsdirectory() throws InterruptedException {

		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================drugDirectory test case start==================");
		try {
			driver.get(baseURL + "/directory/drugs");

			List<WebElement> centerLinks = new WebDriverWait(driver, 15)
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
							.xpath("//*[@class='box-directory-list']//a")));
			System.out
					.println("Center well links exist on Common TAB of Health Drugs Directory page "
							+ driver.getCurrentUrl());

			// center link clicking
			try {
				centerLinks.get(2).click();
				Thread.sleep(waittime);
				WebElement generic = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath("//*[@class='hl-primary-heading hl-capitalize']//*[@class='hl-brand-title']//a")));
				if (generic.getText().trim().equals("")) {
					messages.add("Brand name missing on drugs page "
							+ driver.getCurrentUrl());
				} else {
					System.out.println("Brand name exist on drugs page "
							+ driver.getCurrentUrl());
				}

			} catch (Exception e) {
				messages.add(" Brand name missing on drugs page "
						+ driver.getCurrentUrl());
			}

		} catch (Exception e) {
			messages.add("Center well links missing for Common TAB on Health Drugs Directory page "
					+ driver.getCurrentUrl());
		}
		// /
		// Check "What does the pill look like." section should be on Hybrid and
		// licensed pages
		ArrayList<String> link1 = new ArrayList<String>();

		link1.add(baseURL + "/drugs/lorazepam/oral-tablet"); // Hybrid link
		link1.add(baseURL + "/drugs/erythromycin/oral-tablet"); // licensed link
		for (int i = 0; i < link1.size(); i++) {
			try {
				driver.get(link1.get(i));

				WebElement drugHead = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath("//*[@class='hl-drug-alternatives']//h2")));
				String heading = drugHead.getText();
				WebElement pillimage = driver.findElement(By
						.xpath("//*[@class='item']"));
				System.out
						.println("'What does the pill look like.' section is displayed on page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add(" 'What does the pill look like.' section is not displayed on page "
						+ driver.getCurrentUrl() + e.toString());

			}
		}

		// Check "What does the pill look like." section should not on Generic
		// and Branded pages
		ArrayList<String> links1 = new ArrayList<String>();

		links1.add(baseURL + "/drugs/prednisone/oral-tablet"); // Generic link
		links1.add(baseURL
				+ "/drugs/lisdexamfetamine/oral-capsule?brand=vyvanse"); // Branded
																			// link
		for (int i = 0; i < links1.size(); i++) {
			try {
				driver.get(links1.get(i));
				WebElement drugHead = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath("//*[@class='hl-drug-alternatives']//h2")));
				String heading = drugHead.getText();
				if (heading.equals("What does the pill look like")) {
					messages.add(" 'What does the pill look like.' section should not displayed "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("'What does the pill look like.' section is missing and it is as expected on page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				System.out
						.println("'What does the pill look like.' section is missing and it is as expected on page "
								+ driver.getCurrentUrl());
			}
		}

		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================drugDirectory test case end==================");
	}

	// video page
	@Test(enabled = true, priority = 6, description = "Check existance and numbers of center well links on video directory page, click on 2nd link."
			+ "\nOn resulting vedio page checking for contents")
	public void videodirectory() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================videoDirectory test case start==================");
		try {
			driver.get(baseURL + "/directory/videos");
			// Check center well links for Common TAB
			try {

				List<WebElement> centerLinks = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
								.xpath("//*[@class='box-directory-list']//a")));
				if (centerLinks.size() <= 0) {
					messages.add("Center well links missing on Health Video Directory page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Center well links exist on Health Video Directory page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Center well links missing on Health Video Directory page "
						+ driver.getCurrentUrl());
			}

			List<WebElement> centerLinks5 = driver.findElements(By
					.xpath("//*[@class='box-directory-list']//a"));

			centerLinks5.get(1).click();
			Thread.sleep(waittime);
			try {

				WebElement tit = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath("//*[@class='article__content-body']")));
				System.out.println("Content exist on Health Video page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Content missing on Health Video page "
						+ driver.getCurrentUrl());
			}
			// driver.navigate().back();

		} catch (Exception e) {
			messages.add("Exception in Health Video Directory page "
					+ driver.getCurrentUrl());
		}

		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================videoirectory test case end==================");
	}

	// health-slideshow

	@Test(enabled = true, priority = 7, description = "In this test we are checking center well links for 'All' tab on Health Slideshow Directory page and click on 3rd link"
			+ "\nOn resulting slideshow page checking that slideshow container exist")
	public void slideshowdirectory() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================SlideshowDirectory test case start==================");
		try {
			driver.get(baseURL + "/health-slideshow");
			Thread.sleep(waittime);

			// Check center well links for Common TAB
			try {

				List<WebElement> centerLinks = driver.findElements(By
						.xpath("//*[@class='box-directory-list']//a"));

				System.out
						.println("Center well links exist for 'All' TAB on Health Slideshow Directory page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Center well links missing for 'All' TAB on Health Slideshow Directory page "
						+ driver.getCurrentUrl());
			}

			List<WebElement> centerLinks6 = driver.findElements(By
					.xpath("//*[@class='box-directory-list']//a"));

			centerLinks6.get(2).click();
			Thread.sleep(waittime);

			try {
				WebElement tit = driver.findElement(By
						.xpath("//*[@class='span12 container-slideshow']"));

				System.out
						.println("Slideshow container exist on Slideshow page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add(" Slideshow container missing on Slideshow page "
						+ driver.getCurrentUrl());
			}
			driver.navigate().back();
			Thread.sleep(waittime);
			// Check Box Pagination
		} catch (Exception e) {
			messages.add("Exception in Health Slideshow Directory page "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================SlideshowDirectory test case end==================");
	}

	// News directory

	@Test(enabled = true, priority = 8, description = "In this test we are checking that center well links exist for 'Recent' TAB and click on 5th link"
			+ "\nOn Resulting Health News page checking Heading of the page")
	public void newsdirectory() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================Newsdirectory test case start==================");
		try {
			driver.get(baseURL + "/directory/news");
			// Check center well links for Common TAB
			try {
				WebElement centerLinks = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".grid648 .box-directory-list")));
				// Thread.sleep(waittime);
				System.out
						.println("Center well links exist for 'Recent' TAB on Health News Directory page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Center well links missing for 'Recent' TAB on Health News Directory page "
						+ driver.getCurrentUrl());
			}

			try {
				driver.get(baseURL
						+ "/health-news/yoga-can-help-girls-with-trauma");

				WebElement title1 = driver.findElement(
						By.className("content-header")).findElement(
						By.className("article--content-title"));
				Thread.sleep(waittime);
				if (!(title1.getText().trim() != null)) {
					messages.add("Title missing on Redesign News Article Page, "
							+ driver.getCurrentUrl());

				}
				System.out
						.println("Title is dislpayed on Redesign News Article Page"
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add(" Title missing on Redesign News Article Page, "
						+ driver.getCurrentUrl() + e.toString());
			}
			try {
				// check content
				WebElement content1 = driver.findElement(By
						.className("article__content-body"));
				Thread.sleep(waittime);
				if (!(content1.isDisplayed())) {
					messages.add("Content is missing on Redesign News Article Page, "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Content is displayed on Redesign News Article Page"
								+ driver.getCurrentUrl());
				try {
					WebElement topstories1 = driver.findElement(By
							.className("top-stories"));
					Thread.sleep(waittime);
					if (!(topstories1.isDisplayed())) {
						messages.add("Top Stories section is missing at RHS on Redesign News Article Page, "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Top Stories section is displayed at RHS on Redesign News Article Page"
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Top Stories section is missing at RHS on Redesign News Article Page, "
							+ driver.getCurrentUrl());
				}

				// Checked read next section
				try {
					WebElement readnext = driver.findElement(By
							.className("read-next"));
					Thread.sleep(waittime);
					if (!(readnext.isDisplayed())) {
						messages.add("read next section is missing on Redesign News Article Page, "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("read next section is dispalyed on Redesign News Article Page"
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("read next section is missing on Redesign News Article Page, "
							+ driver.getCurrentUrl());
				}

			} catch (Exception e) {
				messages.add("Content missing on Redesign News Article Page, "
						+ driver.getCurrentUrl());
			}

			Thread.sleep(waittime);
		} catch (Exception e) {
			messages.add("page loading problem for Health News Directory page "
					+ driver.getCurrentUrl());

		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================NewsDirectory test case end==================");
	}

	@Test(enabled = true, priority = 9, description = "In this test we are checking existance of copy text. Take list of all footer links, except twitter and check it's status"
			+ "\n Check existance of newsletter sign up widget,sharebar & copyright text.")
	public void footer() throws InterruptedException {
		List<String> messages = new ArrayList<String>();

		String href = null;
		System.out
				.println("==================Footer test case start - Old footer==================");
		// check footer
		try {
			driver.get(baseURL + "/symptom-checker");
			WebElement footer = new WebDriverWait(driver, 15)
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.cssSelector(".hl-box-container-footer .grid978")));
			try {
				WebElement footerCopy = driver.findElement(By
						.className("hl-box-footer-1"));
				if (footerCopy.getText().trim() == "") {
					messages.add("Footer Copy text missing on "
							+ driver.getCurrentUrl());
				} else {
					System.out.println("Footer Copy text exist "
							+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				messages.add("Footer Copy text missing on "
						+ driver.getCurrentUrl() + e.toString());
			}
			try {
				List<WebElement> footerLinks = footer.findElement(
						By.className("hl-box-footer-3")).findElements(
						By.tagName("a"));
				for (WebElement footerLink : footerLinks) {
					String href2 = footerLink.getAttribute("href");
					if (href != null && !href.contains("twitter")) {
						CheckHttpConnection httpConn = new CheckHttpConnection(
								href);
						int status = httpConn.testHttpConn();
						if (status != 200) {
							messages.add(href + " link broken  - Status= "
									+ status + " from Footer of "
									+ driver.getCurrentUrl());
						}
					}

				}
				System.out.println("Footer Links exist "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Links missing in footer of "
						+ driver.getCurrentUrl());
			}

			try {
				WebElement newWidget = driver.findElement(By
						.className("newsletterbox"));
				System.out
						.println("Newsletter sign up widget exist on the footer of "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Newsletter sign up widget is missing from the footer of "
						+ driver.getCurrentUrl() + e.toString());
			}

			try {
				WebElement sharebar = driver.findElement(By
						.className("sharebar"));
				System.out
						.println("Sharebar below Newsletter sign up widget exist on the footer of "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Sharebar below Newsletter sign up widget is missing from the footer of "
						+ driver.getCurrentUrl() + e.toString());
			}

			try {
				WebElement footerCopy = driver.findElement(By
						.className("copyright"));
				if (footerCopy.getText().trim() == "") {
					messages.add("Footer Copyright text missing on "
							+ driver.getCurrentUrl());
				} else {
					System.out.println("Footer Copyright text exist on "
							+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				messages.add("Footer Copyright text section missing on "
						+ driver.getCurrentUrl() + e.toString());
			}
		} catch (Exception e) {
			messages.add("Footer missing on " + driver.getCurrentUrl()
					+ e.toString());
		}

		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================Footer test case end - Old footer==================");

		// ******** New Footer from Redesign Article************
		// Check for Global Footer

		if (newFooter) {
			System.out
					.println("==================Footer test case start - New footer==================");
			try {
				driver.get(baseURL + "/health/a1c");
				WebElement footer = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".container-fluid")));
				if (!(footer.isDisplayed())) {
					System.out
							.println("Redesign Global Footer is not displayed on Tabbed Article "
									+ driver.getCurrentUrl());
					messages.add("Redesign Global Footer is not displayed on Tabbed Article "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Redesign Global Footer is displayed on Tabbed Article "
								+ driver.getCurrentUrl());
				try {
					WebElement sharebar = new WebDriverWait(driver, 15)
							.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.cssSelector(".social__links")));
					if (!(sharebar.isDisplayed())) {

						messages.add("Share bar section not displayed from Redesign Global Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Share bar section displayed from Redesign Global Footer "
									+ driver.getCurrentUrl());
					// Check facebook
					if (!(sharebar.findElement(By
							.className("icon-circle-facebook")).isDisplayed())) {
						messages.add("Facebook icon is not displayed in Sharbar setion at Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Facebook icon is displayed in Sharbar setion at Footer "
									+ driver.getCurrentUrl());
					// Check pinterest
					if (!(sharebar.findElement(By
							.className("icon-circle-pinterest")).isDisplayed())) {
						messages.add("Pinterest icon is not displayed in Sharbar setion at Footer "
								+ driver.getCurrentUrl());

					}
					System.out
							.println("Pinterest icon is displayed in Sharbar setion at Footer "
									+ driver.getCurrentUrl());
					// Check twitter
					if (!(sharebar.findElement(By
							.className("icon-circle-twitter")).isDisplayed())) {
						messages.add("Twitter icon is not displayed in Sharbar setion at Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Twitter icon is  displayed in Sharbar setion at Footer "
									+ driver.getCurrentUrl());
					// Check google plus
					if (!(sharebar.findElement(By
							.className("icon-circle-google")).isDisplayed())) {
						messages.add("Google plus icon is not displayed in Sharbar setion at Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Google plus icon is displayed in Sharbar setion at Footer "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Problem in Sharebar section in Redesign Footer..."
							+ e.toString());
				}
				System.out
						.println("Sharebar section exists in Redesign Footer..."
								+ driver.getCurrentUrl());
				// Check Nav links section from Footer
				try {
					WebElement navlinks = driver.findElement(By
							.className("left-block__us"));
					Thread.sleep(waittime);
					if (!(navlinks.isDisplayed())) {
						messages.add("Nav links Text not displayed in Redesign Global Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Nav links Text displayed in Redesign Global Footer "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Problem in Nav link section in Redesign Global Footer..."
							+ e.toString());
				}
				System.out
						.println("Nav link section exists in Redesign Global Footer..."
								+ driver.getCurrentUrl());
				// Check for Newsletter Widget from Footer
				try {
					WebElement newsletter = driver.findElement(By
							.className("newsletter-signup__container"));
					Thread.sleep(waittime);
					if (!(newsletter.isDisplayed())) {
						messages.add("Newsletter Widget not displayed in Redesign Global Footer "
								+ driver.getCurrentUrl());

					}
					System.out
							.println("Newsletter Widget displayed in Redesign Global Footer "
									+ driver.getCurrentUrl());
					// Check newsletter title
					WebElement ntitle = newsletter.findElement(By
							.className("newsletter-signup__header"));
					if (!(ntitle.isDisplayed())) {
						messages.add("Newsletter Widget's Title not displayed in Redesign Global Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Newsletter Widget's Title displayed in Redesign Global Footer "
									+ driver.getCurrentUrl());
					// Check newsletter summary
					WebElement nsummary = newsletter.findElement(By
							.className("newsletter-signup__subheader"));
					Thread.sleep(waittime);
					if (!(nsummary.isDisplayed())) {
						messages.add("Newsletter Widget's Description not displayed in Redesign Global Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Newsletter Description displayed in Redesign Global Footer "
									+ driver.getCurrentUrl());
					// Check Subscribe button
					WebElement nbutton = newsletter.findElement(By
							.className("newsletter-signup__submit"));
					Thread.sleep(waittime);
					if (!(nbutton.isDisplayed())) {
						messages.add("Subscribe button is not displayed in Redesign Global Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Subscribe button is displayed in Redesign Global Footer "
									+ driver.getCurrentUrl());
					// Check Email field
					WebElement nfield = newsletter.findElement(By
							.className("newsletter-signup__input"));
					Thread.sleep(waittime);
					if (!(nfield.isDisplayed())) {
						messages.add("Email field is not displayed in Redesign Global Footer."
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Email field is displayed in Redesign Global Footer "
									+ driver.getCurrentUrl());
					// Enter email id
					nfield.sendKeys("kinjal@healthline.com");
					nbutton.click();
					Thread.sleep(9000);
					WebElement nconfirm = newsletter.findElement(By
							.className("newsletter-signup__success"));
					if (!(nconfirm.isDisplayed())) {
						messages.add("Confirmation message is not displayed in Redesign Global Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Confirmation message is displayed in Redesign Global Footer "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Problem in Newsletter Widget in Redesign Footer..."
							+ e.toString());
				}
				System.out
						.println("Newsletter Widget exits in Redesign Footer..."
								+ driver.getCurrentUrl());
				// Check for Honcode Logo and Copyright Text from Footer
				try {
					WebElement honlogo = driver.findElement(By
							.className("footer__hon-image"));
					Thread.sleep(waittime);
					if (!(honlogo.isDisplayed())) {
						messages.add("Honcode logo not displayed from Redesign Global Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Honcode logo displayed in Redesign Global Footer."
									+ driver.getCurrentUrl());
					WebElement copytext = driver.findElement(By
							.className("footer__copyright"));
					Thread.sleep(waittime);
					if (!(copytext.isDisplayed())) {
						messages.add("Copyright Text not displayed in Redesign Global Footer "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Copyright Text displayed in Redesign Global Footer "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Problem in Copyright Text in Redesign Footer..."
							+ e.toString());
				}
				System.out
						.println("Copyright Text exists in Redesign Footer..."

						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Problem in Redesign Footer..." + e.toString());
			}
			System.out.println("Redesign Footer exists..."
					+ driver.getCurrentUrl());
			dispMessage = "";
			if (messages.size() > 0) {
				for (String msg : messages) {
					dispMessage = dispMessage + msg + "\n";
				}
			}
			AssertJUnit.assertTrue(dispMessage, dispMessage == "");
			System.out
					.println("==================Footer test case end - New footer==================");
		}
	}

	@Test(enabled = true, priority = 10, description = "In this test we check existance of sharebar & facebook, twitter, email, print icon inside it.")
	public void sharebar() throws InterruptedException {

		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================Sharebar test case start==================");
		try {
			driver.get(baseURL
					+ "/health/meningitis-awareness/tips-for-keeping-your-teen-healthy-at-camp-and-college");
			try {
				WebElement title = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".hl-sharebar-global .hl-share-button-facebook")));
				System.out.println("On sharebar Facebook icon exist "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("On sharebar Facebook icon missing "
						+ driver.getCurrentUrl());
			}
			// Thread.sleep(2000);
			try {
				WebElement twitter = driver.findElement(
						By.className("hl-sharebar-global")).findElement(
						By.className("hl-share-button-twitter"));
				System.out.println("On sharebar Twitter icon exist "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("On sharebar Twitter icon missing "
						+ driver.getCurrentUrl());
			}
			try {
				WebElement email = driver.findElement(
						By.className("hl-sharebar-global")).findElement(
						By.className("hl-share-button-envelope"));

				System.out.println("On sharebar Email icon exist "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("On sharebar Email icon missing "
						+ driver.getCurrentUrl());
			}
			try {
				WebElement print = driver.findElement(
						By.className("hl-sharebar-global")).findElement(
						By.className("hl-share-button-envelope"));
				System.out.println("On sharebar Print icon exist "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("On sharebar Print icon missing "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Clinical sharebar section missing on "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================Sharebar test case end==================");
	}

	@Test(enabled = true, priority = 11, description = "In this test we are checking FDA Article page title"
			+ "\n checking center content"
			+ "\n checking Top Stories section at RHS"
			+ "\n checking 'back to top' link ")
	public void FDAArticle() throws InterruptedException {

		List<String> messages = new ArrayList<String>();

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		System.out
				.println("==================FDA Article test case start==================");
		try {
			driver.get(baseURL
					+ "/health/fda/what-to-ask-your-doctor-before-taking-opioids");
			WebElement title1 = driver.findElement(By
					.className("hl-title-area"));
			Thread.sleep(waittime);
			if (!(title1.getText().trim() != null)) {
				messages.add("Title missing on FDA Article Page, "
						+ driver.getCurrentUrl());
			}
			System.out.println("Title is dislpayed on FDA Article Page "
					+ driver.getCurrentUrl());
		} catch (Exception e) {
			messages.add(" Title missing on FDA Article Page "
					+ driver.getCurrentUrl() + e.toString());
		}
		try {
			// check content
			WebElement content1 = driver.findElement(By
					.className("article-body"));
			if (!(content1.isDisplayed())) {
				messages.add("Content is missing on FDA Article Page "
						+ driver.getCurrentUrl());
			}
			System.out.println("Content is displayed on FDA Article Page "
					+ driver.getCurrentUrl());
			try {
				WebElement bacttotop = driver.findElement(By
						.className("registrationCondition"));
				if (!(bacttotop.isDisplayed())) {
					messages.add("'Get FDA Consumer Updates' Newsletter widget is missing on FDA Article Page at RHS "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("'Get FDA Consumer Updates' Newsletter widget is displayed on FDA Article Page at RHS "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("'Get FDA Consumer Updates' Newsletter widget is missing on FDA Article Page at RHS "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Content missing on FDA Article Page, "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================FDA Article test case end==================");
	}

	@Test(enabled = true, priority = 12, description = "In this test we are taking list of all links from This Week's post"
			+ "\nsection and check their status. scroll to This Week's post section & Click"
			+ "\non 1st 'Read More' link inside 1st result block. On resulting DM article page checing breadcrumb, title & content")
	public void testDM() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		try {
			driver.get(baseURL + "/diabetesmine");
			Thread.sleep(waittime);
			System.out
					.println("==================testDM test case start==================");
			// On DM Landing page get list of all links from This Week's post
			// section and check their status
			List<WebElement> Links = driver.findElement(By.id("container"))
					.findElement(By.id("resultbody"))
					.findElement(By.id("ts_result"))
					.findElement(By.id("rss-div"))
					.findElements(By.className("hl-box-result-item"));
			System.out
					.println(Links.size()
							+ " links present in This Week's post section on DM landing page ");

			for (WebElement Link : Links) {
				if (Link.findElement(By.tagName("a")).getAttribute("href") != null) {
					CheckHttpConnection httpConnection = new CheckHttpConnection(
							Link.findElement(By.tagName("a")).getAttribute(
									"href"));
					if (httpConnection.testHttpConn() != 200
							&& httpConnection.testHttpConn() != 301) {
						messages.add("Link "
								+ Link.findElement(By.tagName("a"))
										.getAttribute("href")
								+ " is broken status = "
								+ httpConnection.testHttpConn());
					}
				}
			}
			// /On DM landing page , scroll to This Week's post section & Click
			// on 1st "Read More" link inside 1st result block
			try {
				WebElement subdrop1 = Links.get(0)
						.findElement(By.className("hl-box-article-summary"))
						.findElement(By.className("hl-box-content-column"))
						.findElement(By.className("hl-link-read-more"));
				Thread.sleep(waittime);
				((JavascriptExecutor) driver)
						.executeScript("window.scrollTo(0,"
								+ subdrop1.getLocation().y + ")");
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].click();", subdrop1);
				Thread.sleep(waittime);
			} catch (Exception e) {
				messages.add("Problem in locating Read More link inside 1st result block of This Week's post section on DM landing page "
						+ e.toString());
				System.out
						.println("Problem in locating Read More link inside 1st result block of This Week's post section on DM landing page "
								+ e.toString());
			}
			// Checked for few elements present on DM page
			try {
				WebElement breadcrumb = driver.findElement(By
						.className("breadcrumb"));
				System.out.println("Breadcrumbs exist on DM article page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Breadcrumbs are missing on DM article page "
						+ driver.getCurrentUrl());

			}
			try {
				WebElement latestEnt = driver.findElement(
						By.className("article-header")).findElement(
						By.tagName("h1"));
				if (latestEnt.getText().equals("")) {
					messages.add("Title missing on DM article page "
							+ driver.getCurrentUrl());

				}
				System.out.println("Title exist on DM article page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on DM article page "
						+ driver.getCurrentUrl());
			}
			try {
				WebElement content = driver.findElement(By
						.className("article-body"));
				if (content.getText().trim().equals("")) {
					messages.add("Contents missing on DM article page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Contents exist on DM article page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Contents missing on DM article page "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Loading problem for DM landing page or links missing on This Week's post section of DM Landing page "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================testDM test case end==================");
	}

	// Feedback widget
	@Test(enabled = true, priority = 13, description = "In this test we are checking existance of feedback widget, click on yes link."
			+ "\nCheck that two links present on overlay for positive feedback. Click on 1st link"
			+ "\nOn resulting overlay feelup the feedback form, click done button and check Confirmation text display"
			+ "\nClose overlay, click no link and check two links exist for negative feedback.")
	public void feedbackWidget() throws InterruptedException {
		WebElement yeslink;

		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================feedbackWidget test case start==================");
		driver.get(baseURL + "/drugs/anagrelide/oral-capsule#Interactions3");

		ArrayList<String> tabs = new ArrayList<String>(
				driver.getWindowHandles());

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		// scrolling the page and click on Yes link
		try {
			yeslink = new WebDriverWait(driver, 15)
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.cssSelector(".box-link-feedback #feedbackLink-positive")));
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,"
					+ yeslink.getLocation().y + ")");
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].click();", yeslink);
			System.out.println("Feedback widgey yes link clicked ");
			Thread.sleep(waittime);
			// check title from Yes overlay
			try {
				WebElement yesoverlayHeading = driver.findElement(
						By.id("overlayFeedback")).findElement(
						By.className("positiveFeedback"));
				if (yesoverlayHeading.getText().trim().equals("")) {
					messages.add("Title missing in Yes overlay "
							+ driver.getCurrentUrl());
					System.out.println("Title missing on Yes overlay ");
				} else {
					System.out.println("Title exist on Yes overlay ");
				}
			} catch (Exception e) {
				messages.add("Title missing on Yes overlay "
						+ driver.getCurrentUrl());
				System.out.println("Title missing on Yes overlay "
						+ e.toString());
			}
			// check 3 links of Yes overlay using findelements
			try {
				List<WebElement> linksCont = driver
						.findElement(By.id("overlayFeedback"))
						.findElement(By.className("feedback-options"))
						.findElements(By.className("positiveFeedback"));

				for (int i = 0; i < linksCont.size(); i++) {
					WebElement comomnlink = linksCont.get(i).findElement(
							By.className("feedback-option-text"));
					if (comomnlink.getText() == "") {
						System.out
								.println(" hyperlink missing on 'Yes' overlay ");
					} else {
						System.out.println(linksCont.get(i).getText()
								+ " hyperlink exist on 'Yes' overlay ");
					}
				}

			} catch (Exception e) {
				messages.add("Links missing on 'Yes' overlay "
						+ driver.getCurrentUrl());
			}
			// click 1st link from Yes overlay
			try {
				WebElement clcik1stlink = driver
						.findElement(By.id("overlayFeedback"))
						.findElement(By.className("feedback-options"))
						.findElement(By.className("hl-icon-heart"));
				clcik1stlink.click();
				Thread.sleep(waittime);
				System.out
						.println("'This article changed my life!' link is clickable from 'Yes' overlay "
								+ driver.getCurrentUrl());
				List<WebElement> overlaytitle = driver
						.findElement(By.id("overlayFeedback"))
						.findElement(By.id("feedback-owl-carousel"))
						.findElement(By.className("owl-wrapper"))
						.findElements(By.className("owl-item"));

				WebElement firstform = overlaytitle.get(1).findElement(
						By.className("item"));

				// Fill required fields in overlay. Enter msg
				WebElement entermsg = firstform
						.findElement(By.className("overlay-content"))
						.findElement(By.id("feedbackformdiv"))
						.findElement(By.id("feedbackform"))
						.findElement(By.className("box-textarea"))
						.findElement(By.id("feedbacktextarea"));
				entermsg.sendKeys("Test...");
				Thread.sleep(waittime);
				// enter email
				WebElement entermailid = firstform
						.findElement(By.className("overlay-content"))
						.findElement(By.id("feedbackformdiv"))
						.findElement(By.id("feedbackform"))
						.findElement(By.id("feedbackemail"));
				entermailid.sendKeys("prodigyinfosoft.p.ltd@gmail.com");
				// enter name
				WebElement entername = firstform
						.findElement(By.className("overlay-content"))
						.findElement(By.id("feedbackformdiv"))
						.findElement(By.id("feedbackform"))
						.findElement(By.id("nametext"));
				entername.sendKeys("Prod Tester");
				// click Done btn
				WebElement donebtn = overlaytitle.get(1)
						.findElement(By.className("item"))
						.findElement(By.className("overlay-content"))
						.findElement(By.id("feedbackformdiv"))
						.findElement(By.id("feedbackform"))
						.findElement(By.className("box-buttons"));
				donebtn.click();
				Thread.sleep(9000);
				System.out
						.println("Done button clickable on feedback overlay with 'Yes' option and 1st link clicked ");
				// Confirmation Overlay
				WebElement confirmoverlay = overlaytitle.get(2)
						.findElement(By.className("item"))
						.findElement(By.className("feedback-facebook"))
						.findElement(By.className("final-feedback-header"));
				String confirmtext = confirmoverlay.getText();
				System.out
						.println("Title of confirmation overlay after form submission is: "
								+ confirmtext);

				// Click on 'Close' icon from Thanks overlay.
				WebElement closeicon = driver.findElement(
						By.id("overlayFeedback")).findElement(
						By.className("hl-icon-X"));
				((JavascriptExecutor) driver)
						.executeScript("window.scrollTo(0,"
								+ closeicon.getLocation().y + ")");
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].click();", closeicon);
				// closeicon.click();

			} catch (Exception e) {
				messages.add("'This article changed my life!' link is missing on 'Yes' overlay "
						+ driver.getCurrentUrl());
				System.out
						.println("'This article changed my life!' link is missing on 'Yes' overlay "
								+ e.toString());
			}

		} catch (Exception e) {
			messages.add("Feedback widget missing from the page or Yes link is not clickable "
					+ driver.getCurrentUrl());
		}
		// Click No link from the widget
		try {
			WebElement nolink1 = new WebDriverWait(driver, 15)
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.cssSelector(".box-link-feedback .link-feedback .hl-yes-no-feedback #feedbackLink-negative")));
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,"
					+ nolink1.getLocation().y + ")");
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].click();", nolink1);
			Thread.sleep(waittime);
			System.out.println("NO link from feedback widget is clicked ");

			// Check 3 links of No overlay using findelements
			try {
				List<WebElement> NolinksCont = driver
						.findElement(By.id("overlayFeedback"))
						.findElement(By.className("feedback-options"))
						.findElements(By.className("negativeFeedback"));

				for (int i = 0; i < NolinksCont.size(); i++) {
					WebElement comomnlink = NolinksCont.get(i).findElement(
							By.className("feedback-option-text"));
					if (comomnlink.getText() == "") {
						System.out
								.println(" hyperlink missing on 'No' overlay ");
					} else {
						System.out.println(NolinksCont.get(i).getText()
								+ " hyperlink exist on 'No' overlay ");
					}
				}

			} catch (Exception e) {
				messages.add("Hyperlinks missing on No overlay "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("'No' link missing on feedback widget "
					+ driver.getCurrentUrl());
		}

		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================feedbackWidget test case end==================");
	}

	@Test(enabled = true, priority = 14, description = "In this test we are checking existance of elements breadcrumb, title, disclaimer, 'Start Here' button and progress bar")
	public void Assessment() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		System.out
				.println("==================Assessment test case start==================");
		try {
			driver.get(baseURL + "/health-assessments/knee-pain");
			// Test BreadCrumb
			try {
				WebElement breadCrumb = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".persist-area .persist-header .box-outer-sticky .box-inner-sticky .breadcrumb")));
				if (breadCrumb.getText().trim().equals("")) {
					messages.add("Breadcrumbs missing on Assessment Page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Breadcrumbs exist on Assessment Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Breadcrumbs missing on Assessment Page "
						+ driver.getCurrentUrl());
			}

			// Test Disclaimer
			try {
				WebElement title = driver.findElement(
						By.className("hl-sponsor-disclaimer")).findElement(
						By.className("hl-disclaimer-full"));
				if (title.getText().trim().equals("")) {
					messages.add("Disclaimer missing on Assessment Page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Disclaimer exist on Assessment Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Disclaimer missing on Assessment Page "
						+ driver.getCurrentUrl());
			}

			// Test Title
			try {
				WebElement title = driver.findElement(By.id("hl"))
						.findElement(By.className("bodycontainer"))
						.findElement(By.className("innerbodybox"))
						.findElement(By.id("box-full-scenes"))
						.findElement(By.className("fullwidthblock"))
						.findElement(By.tagName("h1"));
				if (title.getText().trim().equals("")) {
					messages.add("Title missing on Assessment Page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Title exist on Assessment Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				System.out.println("Title missing on Assessment Page "
						+ driver.getCurrentUrl());
			}

			// Checked Thumbnail
			try {

				WebElement container = driver.findElement(By.id("scene1Video"));
				if (!(container.isDisplayed())) {
					driver.navigate().refresh();
					Thread.sleep(waittime);
				}
				WebElement thumbnail = driver.findElement(By.id("scene1Video"));
				if (thumbnail.isDisplayed()) {
					System.out
							.println("Video thumbanail is displayed on Assessment page"
									+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				System.out
						.println("Video thumbanail is missing on Assessment Page "
								+ driver.getCurrentUrl());
			}
			// check Start Here button
			try {
				WebElement button = driver.findElement(
						By.className("box-leftside-navigation")).findElement(
						By.tagName("button"));
				if (button.getText().equalsIgnoreCase("start here")) {
					System.out
							.println("Start Here button is displayed on Assessment page "
									+ driver.getCurrentUrl());
				} else {
					messages.add("Start Here button is missing on Assessment Page "
							+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				messages.add("Start Here button is missing on Assessment Page "
						+ driver.getCurrentUrl());
			}
			// Check view table of contents
			try {
				WebElement tablecontent = driver.findElement(
						By.className("box-chrome-toolbar")).findElement(
						By.id("tocButton"));
				if (tablecontent.isDisplayed()) {
					System.out
							.println("View table of contents is displayed on Assessment Page "
									+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				messages.add("View table of contents is missing on Assessment Page "
						+ driver.getCurrentUrl());
			}

			// check progress bar
			try {
				WebElement progressbar = driver.findElement(
						By.className("box-complete-progressbar")).findElement(
						By.className("box-progressbar"));
				if (progressbar.isDisplayed()) {
					System.out
							.println("progress bar is displayed on Assessment Page "
									+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				messages.add("Progress bar is missing on Assessment Page "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Assessment Page is not working properly "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================Assessment test case end==================");
	}

	@Test(enabled = true, priority = 15, description = "In this test we are cheking existance of title, LHS filters on Reference Directory page, Taking list of"
			+ "\nCenter well links and click on1st link. On resulting redesigned TAB article page checking existance of title, tab navigation, sharebar, content, section titles, wishper text, "
			+ "\nArticle resources, top stories. Click back arrow of browser & click 2nd link. On resulting standard article page check for existance of title, content, article resorces"
			+ "\ntop stories,read this next section \nChecked New Sharebar and Feedback icons with clicking.")
	public void ReferenceDirectory() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		String href = null;
		System.out
				.println("==================ReferenceDirectory test case start==================");
		try {
			driver.get(baseURL + "/directory/reference-library-a");
			// Check title
			try {
				WebElement title = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".bodyheading h2")));
				if (title.getText().trim().equals("")) {
					messages.add("Title text missing on Reference Directory page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Title exist on Reference Directory page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on Reference Directory page "
						+ driver.getCurrentUrl());
			}

			// Check Filters
			String message = "";
			try {
				WebElement filterSec = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".filter")));
				Thread.sleep(waittime);
				List<WebElement> filterLinks = filterSec.findElements(By
						.tagName("a"));
				List<String> desiredList = asList("Health Topics", "Symptoms",
						"Drugs", "Videos", "Slideshows", "Health Blogs",
						"Health News");
				int counter = 0;
				for (WebElement filterLink : filterLinks) {
					href = filterLink.getAttribute("href");
					if (!filterLink.getText().equals(desiredList.get(counter))) {
						messages.add(" Some problem in filter section at LHS on Reference directory page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println(filterLink.getText()
									+ " Filter link exist on LHS of Reference Directory page "
									+ driver.getCurrentUrl());
					CheckHttpConnection httpConn = new CheckHttpConnection(href);
					if (httpConn.testHttpConn() != 200
							&& httpConn.testHttpConn() != 301) {
						messages.add("On " + driver.getCurrentUrl()
								+ " Broken link inside filter section " + href
								+ " status = " + httpConn.testHttpConn());
					}
					counter++;
				}
			} catch (Exception e) {
				messages.add("Filters missing at LHS on Reference directory page "
						+ driver.getCurrentUrl());
			}
			if (message != "") {
				messages.add(message);
			}
			// Redesign Tabbed Article
			try {
				driver.navigate().refresh();
				driver.get(baseURL + "/directory/reference-library-a");
				Thread.sleep(waittime);

				List<WebElement> centerLinks = driver.findElement(
						By.className("box-directory-list")).findElements(
						By.tagName("a"));
				WebElement tabArt = driver.findElement(
						By.className("box-directory-list")).findElement(
						By.linkText("Aarskog Syndrome"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", tabArt);
				jse.executeScript("window.scrollBy(0,-150)", "");
				tabArt.click();

				System.out
						.println("Center well links  exist on Reference directory page "
								+ driver.getCurrentUrl());
				try {
					WebElement title = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector(".content-header .article--content-title")));
					Thread.sleep(waittime);
					if (!(title.getText().trim() != null)) {
						messages.add("Title missing on Tabbed Article Page "
								+ driver.getCurrentUrl());

					}
					System.out
							.println("Title is displayed on Tabbed Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add(" Title missing on on Tabbed Article Page"
							+ driver.getCurrentUrl() + e.toString());
				}
				// Share tools
				try {
					WebElement sharetools = new WebDriverWait(driver, 15)
							.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.cssSelector(".sharebar")));
					if (!(sharetools.isDisplayed())) {
						messages.add("Share Tools is NOT displayed on Tabbed Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Share Tools is displayed on Tabbed Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {

					messages.add("Problem in Share Tools on Tabbed Article Page, "
							+ e.toString());
				}
				// Tab Navigation
				try {
					WebElement tabnav = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector(".tabbed-article__tabs")));
					if (!(tabnav.isDisplayed())) {
						messages.add("Tab Navigation is NOT displayed on Tabbed Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Tab Navigation is displayed on Tabbed Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Tab Navigation missing on Tabbed Article Page "
							+ e.toString());
				}

				try {
					// check content
					WebElement content = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector(".article__content-body")));
					if (!(content.isDisplayed())) {
						messages.add("Content is missing on Tabbed Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Content is displayed on Tabbed Article Page "
									+ driver.getCurrentUrl());
					// check section title
					List<WebElement> sectiontitle = driver.findElements(By
							.className("content_head"));
					if (sectiontitle.size() <= 0) {
						messages.add("Section titles missing on Tabbed Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Section titles is displayed on Tabbed Article Page "
									+ driver.getCurrentUrl());
					// check section content

					WebElement sectioncontent = driver.findElement(By
							.className("content_body"));
					if (!(sectioncontent.isDisplayed())) {
						messages.add("Section content is missing on Tabbed Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Section content is displayed on Tabbed Article Page "
									+ driver.getCurrentUrl());
					// check whisper text

					List<WebElement> whispertexts = driver.findElements(By
							.className("tabbed-article__whisper"));
					if (whispertexts.size() <= 0) {
						messages.add("Whisper text missing on Tabbed Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Whisper text is displyed on Tabbed Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Problem in Center content, " + e.toString());
				}
				try {
					WebElement articlesource = new WebDriverWait(driver, 15)
							.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.cssSelector("#sourcesLink")));
					if (!(articlesource.isDisplayed())) {
						messages.add("Article Resources link is Not displayed on Tabbed Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Article Resources link is displayed on Tabbed Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Article Resources link is Not displayed on Tabbed Article Page "
							+ driver.getCurrentUrl());
				}
				try {
					WebElement readnext = driver.findElement(
							By.className("read-next")).findElement(
							By.tagName("h3"));
					if (!(readnext.isDisplayed())) {
						messages.add("read next section is missing on Tabbed Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("read next section is displayed on Tabbed Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("read next section is missing on Tabbed Article Page "
							+ driver.getCurrentUrl());
				}
				try {

					WebElement topstories = driver.findElement(By
							.className("top-stories"));
					if (!(topstories.isDisplayed())) {
						messages.add("Top Stories section is missing at RHS on Tabbed Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Top Stories section is dispalyed at RHS on Tabbed Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Top Stories section is missing at RHS on Tabbed Article Page "
							+ driver.getCurrentUrl());
				}

			} catch (Exception e) {
				messages.add("Center well links missing for Reference Directory page "
						+ driver.getCurrentUrl());
			}
			driver.navigate().back();
			Thread.sleep(waittime);

			// ***********************************
			// Redesign Standard Article
			WebElement standardArt = new WebDriverWait(driver, 15).until(
					ExpectedConditions.visibilityOfElementLocated(By
							.cssSelector(".box-directory-list"))).findElement(
					By.linkText("Acute Gastritis"));
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", standardArt);
			jse.executeScript("window.scrollBy(0,-150)", "");
			standardArt.click();
			Thread.sleep(waittime);
			System.out
					.println("Center well links exist on Reference directory page, "
							+ driver.getCurrentUrl());
			try {

				WebElement title1 = driver.findElement(
						By.className("content-header")).findElement(
						By.className("article--content-title"));
				Thread.sleep(waittime);
				if (!(title1.getText().trim() != null)) {
					messages.add("Title missing on Standard Article Page, "
							+ driver.getCurrentUrl());

				}
				System.out
						.println("Title is displayed on Standard Article Page, "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add(" Title missing on Standard Article Page "
						+ driver.getCurrentUrl() + e.toString());
			}

			try {
				// check content
				WebElement content1 = driver.findElement(By
						.className("article__content-body"));
				if (!(content1.isDisplayed())) {
					messages.add("content is missing on Standard Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("content is displayed on Standard Article Page "
								+ driver.getCurrentUrl());
				// Check Deck: Deck is optional on standard article
				// Article Resources
				try {

					WebElement articlesource1 = driver.findElement(By
							.id("sourcesLink"));
					if (!(articlesource1.isDisplayed())) {
						messages.add("Article Resources link is Not displayed on Standard Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Article Resources link is displayed on Standard Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Article Resources link is Not displayed on Standard Article Page "
							+ driver.getCurrentUrl());
				}

				try {

					WebElement topstories1 = driver.findElement(By
							.className("top-stories"));
					if (!(topstories1.isDisplayed())) {
						messages.add("Top Stories section is missing at RHS on Standard Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("Top Stories section is displayed at RHS on Standard Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("Top Stories section is missing at RHS on Standard Article Page "
							+ driver.getCurrentUrl());
				}
				try {

					WebElement readnext = driver.findElement(By
							.className("read-next"));
					if (!(readnext.isDisplayed())) {
						messages.add("read next section is missing on Standard Article Page "
								+ driver.getCurrentUrl());
					}
					System.out
							.println("read next section is displayed on Standard Article Page "
									+ driver.getCurrentUrl());
				} catch (Exception e) {
					messages.add("read next section is missing on Standard Article Page "
							+ driver.getCurrentUrl());
				}

				// Redesign Sharebar Start

				try {
					WebElement shareandfeedbar = new WebDriverWait(driver, 15)
							.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.id("share-and-feed-bar")));

					if (!(shareandfeedbar.isDisplayed())) {
						messages.add("Sharebar Feedback section is missing on Standard Article Page "
								+ driver.getCurrentUrl());
					} else {
						WebElement printicon = new WebDriverWait(driver, 15)
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector("#share-and-feed-bar .sharebar--bottom .print-btn-icon")));
						String back_prn_icon = printicon
								.getCssValue("background-image");
						if (back_prn_icon
								.contains("/resources/base/images/print@2x.png")) {
							System.out
									.println("Sharebar print icon src is "
											+ printicon
													.getCssValue("background-image"));
							String parentWindowHandler = driver
									.getWindowHandle(); // Store your parent
														// window
							String subWindowHandler = null;
							((JavascriptExecutor) driver).executeScript(
									"arguments[0].scrollIntoView(true);",
									printicon);
							jse.executeScript("window.scrollBy(0,-250)", "");
							printicon.click();
							Thread.sleep(3000);

							Set<String> handles = driver.getWindowHandles(); // get
																				// all
																				// window
																				// handles
							Iterator<String> iterator = handles.iterator();
							while (iterator.hasNext()) {
								subWindowHandler = iterator.next();
							}
							driver.switchTo().window(subWindowHandler);
							if (driver.getCurrentUrl().contains("?print=true")) {
								System.out.println("print window URL is "
										+ driver.getCurrentUrl());
							} else {
								System.out
										.println("print window not open on clicking print icon from sharbar on "
												+ driver.getCurrentUrl());
							}
							driver.switchTo().window(subWindowHandler).close();
							driver.switchTo().window(parentWindowHandler);
						} else {
							System.out
									.println("Sharebar print icon not changed as per spec on "
											+ driver.getCurrentUrl());
						}

						// FB Icon
						WebElement fbicon = new WebDriverWait(driver, 15)
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector("#share-and-feed-bar .sharebar--bottom .fb-btn-icon")));

						String back_fb_icon = fbicon
								.getCssValue("background-image");
						if (back_fb_icon
								.contains("/resources/base/images/fb@2x.png")) {
							System.out.println("Sharebar facebook icon src is "
									+ fbicon.getCssValue("background-image"));
							((JavascriptExecutor) driver).executeScript(
									"arguments[0].scrollIntoView(true);",
									fbicon);

							jse.executeScript("window.scrollBy(0,-150)", "");
							String parentWindowHandler = driver
									.getWindowHandle(); // Store your parent
														// window
							String subWindowHandler = null;
							fbicon.click();
							Thread.sleep(3000);

							Set<String> handles = driver.getWindowHandles(); // get
																				// all
																				// window
																				// handles

							Iterator<String> iterator = handles.iterator();
							while (iterator.hasNext()) {
								subWindowHandler = iterator.next();
							}
							driver.switchTo().window(subWindowHandler);
							if (driver
									.getCurrentUrl()
									.contains(
											"www.facebook.com/v2.3/dialog/feed?app_id=239371929577489")) {
								System.out.println("FB window URL is "
										+ driver.getCurrentUrl());
							} else {
								System.out
										.println("FB window not open on clicking FB icon from sharbar on "
												+ driver.getCurrentUrl());
							}
							driver.switchTo().window(subWindowHandler).close();
							driver.switchTo().window(parentWindowHandler);
						} else {
							System.out
									.println("Sharebar facebook icon not changed as per spec on "
											+ driver.getCurrentUrl());
						}

						WebElement twicon = new WebDriverWait(driver, 15)
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector("#share-and-feed-bar .sharebar--bottom .twitter-btn-icon")));

						String back_tw_icon = twicon
								.getCssValue("background-image");
						if (back_tw_icon
								.contains("/resources/base/images/twitter@2x.png")) {
							System.out.println("Sharebar Twitter icon src is "
									+ twicon.getCssValue("background-image"));
							((JavascriptExecutor) driver).executeScript(
									"arguments[0].scrollIntoView(true);",
									twicon);
							jse.executeScript("window.scrollBy(0,-250)", "");
							String parentWindowHandler = driver
									.getWindowHandle(); // Store your parent
														// window
							String subWindowHandler = null;
							twicon.click();
							Thread.sleep(3000);

							Set<String> handles = driver.getWindowHandles(); // get
																				// all
																				// window
																				// handles
							Iterator<String> iterator = handles.iterator();
							while (iterator.hasNext()) {
								subWindowHandler = iterator.next();
							}
							driver.switchTo().window(subWindowHandler);
							if (driver.getCurrentUrl().contains(
									"https://twitter.com/intent/tweet?text=")) {
								System.out.println("Twitter window URL is "
										+ driver.getCurrentUrl());
							} else {
								System.out
										.println("Twitter window not open on clicking Twitter icon from sharbar on "
												+ driver.getCurrentUrl());
							}
							driver.switchTo().window(subWindowHandler).close();
							driver.switchTo().window(parentWindowHandler);

						} else {
							System.out
									.println("Sharebar Twitter icon not changed as per spec on "
											+ driver.getCurrentUrl());
						}

						// Feedback Smile icon
						WebElement smileicon = new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector("#share-and-feed-bar .feedback .smile-btn-icon")));

						String back_smile_icon = smileicon
								.getCssValue("background-image");
						if (back_smile_icon
								.contains("/resources/base/images/smile@2x.png")) {
							System.out
									.println("Feedback Smile icon src is "
											+ smileicon
													.getCssValue("background-image"));
							String parentWindowHandler = driver
									.getWindowHandle(); // Store your parent
														// window
							String subWindowHandler = null;

							smileicon.click();
							Thread.sleep(3000);
							WebElement smileh3 = driver.findElement(By
									.id("positiveFeedbackModalTitle"));
							if (!smileh3.isDisplayed()) {
								System.out
										.println("Feedback Smile window not open on clicking smile icon from feedback on "
												+ driver.getCurrentUrl());
							} else {
								System.out
										.println("Feedback Smile POPUP H3 title text is "
												+ smileh3.getText());
								WebElement hds = driver
										.findElement(
												By.className("icon-close-menu"))
										.findElement(By.className("icon-close"));

								((JavascriptExecutor) driver)
										.executeScript("window.scrollTo(0,"
												+ hds.getLocation().y + ")");
								((JavascriptExecutor) driver).executeScript(
										"arguments[0].click();", hds);
							}

						} else {
							System.out
									.println("Feedback smile icon not changed as per spec on "
											+ driver.getCurrentUrl());
						}

						// Feedback Frown icon
						WebElement frownicon = new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector("#share-and-feed-bar .feedback .frown-btn-icon")));

						String back_frown_icon = frownicon
								.getCssValue("background-image");
						if (back_frown_icon
								.contains("/resources/base/images/frown@2x.png")) {
							System.out
									.println("Feedback Frown icon src is "
											+ frownicon
													.getCssValue("background-image"));

							String parentWindowHandler = driver
									.getWindowHandle(); // Store your parent
														// window
							String subWindowHandler = null;
							Thread.sleep(3000);
							frownicon.click();
							Thread.sleep(3000);
							WebElement frownh3 = driver.findElement(By
									.id("negativeFeedbackModalTitle"));
							if (!frownh3.isDisplayed()) {
								System.out
										.println("Feedback Frown window not open on clicking Frown icon from feedback on "
												+ driver.getCurrentUrl());
							} else {
								System.out
										.println("Feedback Frown POPUP H3 title text is "
												+ frownh3.getText());
								WebElement hds1 = driver
										.findElement(
												By.className("icon-close-menu"))
										.findElement(By.className("icon-close"));
								((JavascriptExecutor) driver)
										.executeScript("window.scrollTo(0,"
												+ hds1.getLocation().y + ")");
								((JavascriptExecutor) driver).executeScript(
										"arguments[0].click();", hds1);

							}

						} else {
							messages.add("Feedback frown icon not changed as per spec on "
											+ driver.getCurrentUrl());
						}

						// Emailicon
						WebElement emailicon = new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By
										.cssSelector("#share-and-feed-bar .sharebar--bottom .email-btn-icon")));

						String back_email_icon = emailicon
								.getCssValue("background-image");
						if (back_email_icon
								.contains("/resources/base/images/email@2x.png")) {
							System.out
									.println("Sharebar email icon src is "
											+ emailicon
													.getCssValue("background-image"));
							String parentWindowHandler = driver
									.getWindowHandle(); // Store your parent
														// window
							String subWindowHandler = null;
							((JavascriptExecutor) driver).executeScript(
									"arguments[0].scrollIntoView(true);",
									emailicon);
							jse.executeScript("window.scrollBy(0,-250)", "");
							Thread.sleep(3000);
							emailicon.click();
							Thread.sleep(3000);
							Set<String> handles = driver.getWindowHandles(); // get
																				// all
																				// window
																				// handles
							Iterator<String> iterator = handles.iterator();
							while (iterator.hasNext()) {
								subWindowHandler = iterator.next();
							}
							driver.switchTo().window(subWindowHandler);
							WebElement emailh3 = driver.findElement(By
									.id("sharebarEmailModalTitle"));
							if (!emailh3.isDisplayed()) {
								messages.add("EMAIL window not open on clicking email icon from sharbar on "
												+ driver.getCurrentUrl());
							} else {
								System.out
										.println("EMAIL POPUP H3 title text is "
												+ emailh3.getText());
								WebElement hds2 = driver.findElement(By
										.className("btn--secondary"));

								((JavascriptExecutor) driver)
										.executeScript("window.scrollTo(0,"
												+ hds2.getLocation().y + ")");
								((JavascriptExecutor) driver).executeScript(
										"arguments[0].click();", hds2);
							}
							driver.switchTo().window(parentWindowHandler);
						} else {
							System.out
									.println("Sharebar email icon not changed as per spec on "
											+ driver.getCurrentUrl());
						}
					}
				} catch (Exception e) {
					messages.add("Sharebar Feedback section is missing on Standard Article Page "
							+ driver.getCurrentUrl() + " " + e.toString());
				}

				// Redesign Sharebar End

			} catch (Exception e) {
				messages.add("Content missing on Standard Article Page "
						+ driver.getCurrentUrl());
			}

		} catch (Exception e) {
			messages.add("page loading problem or element missing on Reference Directory page "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================ReferenceDirectory test case end==================");

	}

	@Test(enabled = true, priority = 16, description = "In this test we are checking Breadcrumbs on Blog Article page"
			+ "\n checking Blog Type Name"
			+ "\n checking blog page title"
			+ "\n checking center content "
			+ "\n Recent Blog Posts section at RHS")
	public void blogArticle() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		int waittime = 2000;

		System.out
				.println("==================Blog Article test case start==================");
		try {

			driver.get(baseURL
					+ "/health-blogs/diabetes-still-isnt-easy/all-about-appointments");
			// Test BreadCrumb
			try {
				WebElement breadCrumb = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".persist-area .persist-header .box-outer-sticky .box-inner-sticky .breadcrumb")));
				if (breadCrumb.getText().trim().equals("")) {
					messages.add("Breadcrumbs missing on Blog Article Page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Breadcrumbs exist on Blog Article Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Breadcrumbs missing on Blog Article Page "
						+ driver.getCurrentUrl());
			}

			// Test Blog Type Name
			try {
				WebElement blogtype = driver.findElement(By
						.className("box-blog-header-name"));
				if (blogtype.getText().trim().equals("")) {
					messages.add("Blog Name missing on Blog Article Page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Blog Name exist on Blog Article Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Blog Name missing on Blog Article Page "
						+ driver.getCurrentUrl());
			}

			// Test Title
			try {
				WebElement title = driver.findElement(
						By.className("box-page-blog")).findElement(
						By.tagName("h1"));
				if (title.getText().trim().equals("")) {
					messages.add("Title missing on Blog Article Page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Title exist on Blog Article Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on Blog Article Page "
						+ driver.getCurrentUrl());
			}

			// Test center content
			try {
				WebElement content = driver.findElement(
						By.className("textBlock"));
				if (!(content.isDisplayed())) {
					messages.add("Content is missing on Blog Article Page "
							+ driver.getCurrentUrl());
				}

				System.out.println("Content is displayed on Blog Article Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Content missing on Blog Article Page "
						+ driver.getCurrentUrl());
			}
			// Check Recent Blog Posts section at RHS
			try {
				WebElement recentpost = driver
						.findElement(By.className("grid300"))
						.findElement(By.className("box-blog-recent-posts"))
						.findElement(By.tagName("h3"));
				if (!(recentpost.isDisplayed())) {
					messages.add("Recent Blog Posts section is missing at RHS on Blog Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Recent Blog Posts section is dispalyed at RHS on Blog Article Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Recent Blog Posts section is missing at RHS on Blog Article Page "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Problem in loading Blog Article page "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================Blog Article test case end==================");
	}

	@Test(enabled = true, priority = 17, description = "In this test we are checking Title on Pill Identifier page"
			+ "\n checking 'Do you see the imprint on the pill?' section and title")
	public void PillIdentifier() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		int waittime = 2000;

		System.out
				.println("==================Pill Identifier test case start==================");
		try {

			driver.get(baseURL + "/pill-identifier?ref=global");
			// Test Title
			try {
				WebElement title = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".bodyheading h2")));
				if (title.getText().trim().equals("")) {
					messages.add("Title missing on Pill Identifier Page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Title exist on Pill Identifier Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on Pill Identifier Page "
						+ driver.getCurrentUrl());
			}

			// Test "Do you see the imprint on the pill?" section and title
			try {
				WebElement content = driver.findElement(
						By.className("hl-pill-id-module")).findElement(
						By.className("hl-pi-instruct"));
				if (!(content.isDisplayed())) {
					messages.add("'Do you see the imprint on the pill?' section title is missing on Pill Identifier Page "
							+ driver.getCurrentUrl());
				}

				System.out
						.println("'Do you see the imprint on the pill?' section title is displayed on Pill Identifier Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("'Do you see the imprint on the pill?' section title missing on Pill Identifier Page "
								+ driver.getCurrentUrl());
			}
			// Check Recent Blog Posts section at RHS

		} catch (Exception e) {
			messages.add("Problem in loading Pill Identifier page "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================Pill Identifier test case end==================");
	}

	@Test(enabled = true, priority = 18, description = "In this test we are checking Title on AN Landing page"
			+ "\n checking 'Share tools'"
			+ "'THE LATEST' section"
			+ "Check THE LATEST section title"
			+ "Check Hero Image in THE LATEST section"
			+ "Check Read Next section")
	public void ANLandigPage() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		String href = null;
		System.out
				.println("==================Authority Nutrition Landing Page test case start==================");
		try {
			driver.get(baseURL + "/nutrition");
			// Check title
			try {
				WebElement title = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".hl-section1 h1")));
				if (title.getText().trim().equals("")) {
					messages.add("Title text missing on AN Landing Page "
							+ driver.getCurrentUrl());
				}
				System.out.println("Title exist on AN Landing Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on AN Landing Page "
						+ driver.getCurrentUrl());
			}
			// Share tools
			try {
				WebElement sharetools = driver.findElement(
						By.className("hl-section1")).findElement(
						By.className("sharebar"));
				if (!(sharetools.isDisplayed())) {
					messages.add("Share Tools is NOT displayed on AN Landing Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Share Tools is displayed on AN Landing Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {

				messages.add("Problem in Share Tools on AN Landing Page, "
						+ e.toString());
			}
			// 'THE LATEST' section

			try {
				WebElement latestsection = driver.findElement(By
						.className("hl-section2"));
				if (!(latestsection.isDisplayed())) {
					messages.add("'THE LATEST' section is NOT displayed on AN Landing Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("'THE LATEST' section is displayed on AN Landing Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("'THE LATEST' section is missing on AN Landing Page "
						+ e.toString());
			}
			// Check 'THE LATEST' section title
			try {

				WebElement latestsectiontitle = driver.findElement(
						By.className("col-right")).findElement(
						By.className("latest"));
				if (!(latestsectiontitle.isDisplayed())) {
					messages.add("'THE LATEST' section title is NOT displayed on AN Landing Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("'THE LATEST' section title is displayed on AN Landing Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("'THE LATEST' section title missing on AN Landing Page "
						+ e.toString());
			}
			// Check Hero Image in 'THE LATEST' section title
			try {

				WebElement heroimage = driver.findElement(
						By.className("hero-box-article")).findElement(
						By.className("hero-image"));
				if (!(heroimage.isDisplayed())) {
					messages.add("Hero image is NOT displayed in THE LATEST section on AN Landing Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Hero image is displayed in THE LATEST section on AN Landing Page "
								+ driver.getCurrentUrl());
				// check connection
				WebElement lnk = heroimage.findElement(By.tagName("a"));
				href = lnk.getAttribute("href");
				CheckHttpConnection httpConn = new CheckHttpConnection(href);
				int status = httpConn.testHttpConn();
				if (status != 200 && status != 301) {
					messages.add("On " + driver.getCurrentUrl()
							+ " Broken link for Hero Image " + href
							+ " status = " + httpConn.testHttpConn());
				}
			} catch (Exception e) {
				messages.add("Hero image is missing in THE LATEST section on AN Landing Page "
						+ e.toString());
			}
			// Check Read Next section
			try {
				List<WebElement> readnextlists = driver.findElements(By
						.className("hl-left-article"));
				for (WebElement readnextlist : readnextlists) {
					WebElement lnk = readnextlist.findElement(By.tagName("a"));
					href = lnk.getAttribute("href");
					if (href != null) {
						CheckHttpConnection urlHttpConn = new CheckHttpConnection(
								href);
						int status = urlHttpConn.testHttpConn();

						if (status != 200 && status != 301) {
							messages.add(href
									+ " Problem with Read Next section link"
									+ status);
						}
					}
				}
				System.out
						.println("All Links from Read Next section from AN Landing Page are not broken "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Read Next section not found or problem with Read Next section links on AN Landing Page ");
			}

		} catch (Exception e) {
			messages.add("Page loading problem or element missing on AN Landing Page "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================Authority Nutrition Landing Page test case end==================");

	}

	@Test(enabled = true, priority = 19, description = "In this test we are Check 'AUTHORITY NUTRITION' top branding link on AN Article page"
			+ "\n checking Check 'Evidence Based' branding badge besides AUTHORITY NUTRITION link "
			+ " Check Article Title"
			+ "Check Center content"
			+ "Check Bottom Line"
			+ "Check Byline at RHS"
			+ "Check Top Story"
			+ "Check Bottom Branding"
			+ "Authority Nutrition link in Bottom Branding"
			+ "Share tools"
			+ "Check Read Next Section")
	public void ANArticlePage() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		String href = null;
		System.out
				.println("==================Authority Nutrition Article Page test case start==================");
		try {
			driver.get(baseURL
					+ "/nutrition/10-things-dietitians-say-about-low-carb-diets");
			// Check 'AUTHORITY NUTRITION' top branding link
			try {
				WebElement topbranding = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector(".branding .branding__name")));
				if (!(topbranding.isDisplayed())) {
					messages.add("'AUTHORITY NUTRITION' top branding link missing on AN Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("'AUTHORITY NUTRITION' top branding link exist on AN Article Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("'AUTHORITY NUTRITION' top branding link missing on AN Article Page "
						+ driver.getCurrentUrl());
			}
			// Check 'Evidence Based' branding badge besides AUTHORITY NUTRITION
			// link
			try {
				WebElement brandingbadge = driver.findElement(
						By.className("branding")).findElement(
						By.className("branding__badge"));
				if (!(brandingbadge.isDisplayed())) {
					messages.add("'Evidence Based' badge is missing on AN Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("'Evidence Based' badge is exist on AN Article Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("'Evidence Based' missing badge is on AN Article Page "
						+ driver.getCurrentUrl());
			}
			// Check Article Title
			try {
				WebElement title1 = driver.findElement(
						By.className("content-header")).findElement(
						By.className("article--content-title"));
				Thread.sleep(waittime);
				if (!(title1.getText().trim() != null)) {
					messages.add("Title missing on AN Article Page, "
							+ driver.getCurrentUrl());
				}
				System.out.println("Title is displayed on AN Article Page "
						+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add(" Title missing on AN Article Page "
						+ driver.getCurrentUrl() + e.toString());
			}
			// Check Center content
			WebElement content1 = driver.findElement(By
					.className("article__content-body"));
			if (!(content1.isDisplayed())) {
				messages.add("content is missing on AN Article Page "
						+ driver.getCurrentUrl());
			}
			System.out.println("content is displayed on AN Article Page"
					+ driver.getCurrentUrl());
			// Check Bottom Line
			WebElement bottomline = driver.findElement(By.tagName("strong"));
			if (!(bottomline.isDisplayed())) {
				messages.add("Bottom Line is missing at Center on AN Article Page "
						+ driver.getCurrentUrl());
			}
			System.out
					.println("Bottom Line is displayed at Center on AN Article Page "
							+ driver.getCurrentUrl());

			// Check Byline at RHS
			try {
				WebElement byline = driver.findElement(
						By.className("body__col-fixed-right")).findElement(
						By.className("byline"));
				if (!(byline.isDisplayed())) {
					messages.add("Byline is missing at RHS on AN Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Byline is displayed at RHS on AN Article Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Byline is missing at RHS on AN Article Page "
						+ driver.getCurrentUrl());
			}
			// Check Top Story
			try {
				WebElement topstories1 = driver.findElement(By
						.className("top-stories"));
				if (!(topstories1.isDisplayed())) {
					messages.add("Top Stories Section is missing at RHS on AN Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Top Stories Section is displayed at RHS on AN Article Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Top Stories Section is missing at RHS on AN Article Page "
						+ driver.getCurrentUrl());
			}

			// Check Bottom Branding
			try {
				WebElement bottombranding = driver.findElement(By
						.className("branding"));
				if (!(bottombranding.isDisplayed())) {
					messages.add("Bottom Branding is missing on AN Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Bottom Branding is displayed on AN Article Page "
								+ driver.getCurrentUrl());
				// Authority Nutrition link in Bottom Branding
				WebElement bottombrandinglink = driver.findElement(By
						.className("branding"));
				if (!(bottombrandinglink.isDisplayed())) {
					messages.add("Authority Nutrition link missing in Bottom Branding on AN Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Authority Nutrition link is displayed in Bottom Branding on AN Article Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Authority Nutrition link is missing in Bottom Branding on AN Article Page "
						+ driver.getCurrentUrl());
			}
			// Share Tools
			try {
				WebElement sharetools = driver.findElement(
						By.className("share-feed-desktop")).findElement(
						By.className("sharebar"));
				if (!(sharetools.isDisplayed())) {
					messages.add("Share Tools is NOT displayed on AN Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("Share Tools is displayed on AN Article Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {

				messages.add("Problem in Share Tools on AN Article Page "
						+ e.toString());
			}
			// Check Read Next Section
			try {
				WebElement readnext = driver.findElement(By
						.className("read-next"));
				if (!(readnext.isDisplayed())) {
					messages.add("read next section is missing on AN Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("read next section is displayed on AN Article Page "
								+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("read next section is missing on AN Article Page "
						+ driver.getCurrentUrl());
			}

		} catch (Exception e) {
			messages.add("page loading problem or element missing on AN Article Page "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
		System.out
				.println("==================Authority Nutrition Article Page test case end==================");

	}

	// For 05/23 release CORE-1509. Pinterest sharing for image widget on a
	// page.
	@Test(enabled = false)
	public void CORE1509() throws InterruptedException {
		List<String> messages = new ArrayList<String>();

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		try {
			driver.get(baseURL + "/health/parenting/what-is-a-baby-box");

			WebElement imagecontainer = new WebDriverWait(driver, 15)
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.cssSelector(".widget__image")));
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", imagecontainer);
			jse.executeScript("window.scrollBy(0,-50)", "");
			Thread.sleep(waittime);
			WebElement mousehover = driver.findElement(By
					.className("social-share__overlay"));

			if (mousehover.isDisplayed()) {
				System.out
						.println("Pin icon displayed when mouse hovring on Image.");
				mousehover.findElement(By.className("social-share__buttons"))
						.findElement(By.className("pinterest")).click();

				// Switch to new window
				String winHandleBefore = driver.getWindowHandle();
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
				}
				Thread.sleep(waittime);

				// Enter Email
				WebElement entermailid = driver.findElement(
						By.className("fieldset")).findElement(
						By.id("userEmail"));
				entermailid.sendKeys("prodigyinfosoft.p.ltd@gmail.com");
				// Enter Password
				WebElement pwd = driver.findElement(
						By.className("passwordFieldRedesign")).findElement(
						By.className("password"));
				pwd.sendKeys("h3althlin3");
				// Click Sign Up button
				WebElement sendBtn = driver.findElement(By
						.className("signupButton"));
				sendBtn.click();
				System.out.println("Sign Up Button is clickable for Pin.");
				Thread.sleep(waittime);
				try {
					// check image on the page
					WebElement chkimge = driver
							.findElement(By.className("pinToBoard"))
							.findElement(By.className("_1X82l"))
							.findElement(By.tagName("img"));
					System.out.println("image src"
							+ chkimge.getAttribute("src"));
					if (chkimge.isDisplayed()) {
						System.out.println("Pin image displayed in pin window "
								+ driver.getCurrentUrl());
					} else {
						System.out
								.println("Pin image Not displayed in pin window "
										+ driver.getCurrentUrl());
					}
				} catch (Exception e) {
					messages.add("Problem in Pin window, " + e.toString());
				}
				Thread.sleep(waittime);
				driver.close();
				driver.switchTo().window(winHandleBefore);
			} else {
				System.out.println("Pin icon Not displayed on image hover "
						+ driver.getCurrentUrl());
				messages.add("Pin icon Not displayed on image hover "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Problem displaying pinterest icon on image hover "
					+ e.toString());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		AssertJUnit.assertTrue(dispMessage, dispMessage == "");
	}

	@AfterTest
	public static void endWebDriver() {
		driver.close();
	}
}
