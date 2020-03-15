package smoketest;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;

import org.junit.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.openqa.selenium.Keys;
import qalib.CheckHttpConnection;
import org.junit.Test;
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
import qalib.SelectRandomURL;
import bsh.commands.dir;

public class LivestrongTest {
	static Map<String, String> env;
	static String driverType;

	// private static String baseURL;
	private static String assessment;
	private static String slideshow;
	private static String standardarticle;
	private static String tabbedarticle;
	private static String topiccenter;
	private static String video;
	private static String domain;
	List<String> messages = new ArrayList<String>();
	SelectRandomURL s = new SelectRandomURL();
	String url = null;

	// static String driverType;
	private static String baseURL;
	private static WebDriver driver;
	public static int waittime = 8000;

	@BeforeClass
	public static void startWebDriver() {

		env = System.getenv();
		driverType = env.get("DRIVER");
		baseURL = env.get("URL");
		domain = baseURL;

		if (driverType.equals("firefox")) {
			driver = new FirefoxDriver();
		} else if (driverType.equals("ie11")) {
			System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		} else if (driverType.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"E:\\chromedriver.exe");
			driver = new ChromeDriver();
		} else {
			System.out.println("Unknown driver");
		}
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage()
				.window()
				.setSize(
						new Dimension(
								Integer.parseInt(env.get("BROWSER_WIDTH")),
								Integer.parseInt(env.get("BROWSER_HEIGHT"))));
		if (env.containsValue("http://staging.livestrong.com/healthline")) {
			baseURL = "http://trs:radioshack@staging.livestrong.com/healthline";
		}
	}

	@Before
	public void clearDriver() {
		driver.manage().deleteAllCookies();
	}

	@Test
	public void Assessment() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		try {
			assessment = "E:/Livestrong/assessment.txt";
			s = new SelectRandomURL();
			url = s.choose(new File(assessment));
			driver.get(baseURL + url);
			Thread.sleep(waittime);
			System.out.println("Assessment Page...");

			// Test Disclaimer
			try {
				List<WebElement> title2 = driver
						.findElement(By.className("hl")).findElements(
								By.className("innerbodybox"));

				WebElement title = title2.get(0)
						.findElement(By.className("hl-disclaimer-container"))
						.findElement(By.className("span12"))
						.findElement(By.className("hl-sponsor-disclaimer"))
						.findElement(By.className("hl-disclaimer-full"));
				if (title.getText().trim().equals("")) {
					messages.add("Disclaimer missing on Assessment Page "
							+ driver.getCurrentUrl());
				} else {
					System.out.println("Disclaimer exist on Assessment Page "
							+ driver.getCurrentUrl());
				}

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
				} driver.navigate().refresh();
					System.out.println("Title exist on Assessment Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				System.out.println("Title missing on Assessment Page "
						+ driver.getCurrentUrl());
			}

			// Checked Thumbnail
			try {

				WebElement container = driver.findElement(By
						.className("box-scenes"));
				if (!(container.isDisplayed())) {
					driver.navigate().refresh();
					Thread.sleep(3000);
				}
				WebElement thumbnail = driver.findElement(By
						.className("box-quest-image"));
				if (thumbnail.isDisplayed()) {
					System.out
							.println("Video thumbanail is displayed on Assessment page"
									+ driver.getCurrentUrl());
				} else
					messages.add("Video thumbanail is missing on Assessment Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Video thumbanail is missing on Assessment Page "
						+ driver.getCurrentUrl());
			}

			// check Next button
			try {
				WebElement button = driver.findElement(
						By.className("box-question-buttons")).findElement(
						By.tagName("button"));
				if (button.getText().equalsIgnoreCase("Next")) {
					System.out
							.println("Nextbutton is displayed on Assessment page"
									+ driver.getCurrentUrl());
				} else {
					messages.add("Next button is missing on Assessment Page "
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
							.println("view table of contents is displayed on Assessment Page"
									+ driver.getCurrentUrl());
				} else
					messages.add("view table of contents is missing on Assessment Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("view table of contents is missing on Assessment Page "
						+ driver.getCurrentUrl());
			}

			// check progress bar
			try {
				WebElement progressbar = driver.findElement(
						By.className("box-complete-progressbar")).findElement(
						By.className("box-progressbar"));
				if (progressbar.isDisplayed()) {
					System.out
							.println("progress bar is displayed on Assessment Page"
									+ driver.getCurrentUrl());
				} else
					messages.add("progress bar is missing on Assessment Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("progress bar is missing on Assessment Page "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Assessment Page is not working properly:  "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		assertTrue(dispMessage, dispMessage == "");
	}

	@Test
	public void VideoPage() throws InterruptedException {
		List<String> messages = new ArrayList<String>();

		try {
			video = "E:/Livestrong/video.txt";
			s = new SelectRandomURL();
			url = s.choose(new File(video));
			driver.get(baseURL + url);
			Thread.sleep(waittime);
			System.out.println("Video Page...");

			// Test Title
			try {
				WebElement title = driver.findElement(By.id("hl"))
						.findElement(By.className("container"))
						.findElement(By.className("article-header"))
						.findElement(By.tagName("h1"));

				if (title.getText().trim().equals("")) {
					messages.add("Title missing on Video Article Page "
							+ driver.getCurrentUrl());
				} else
					System.out.println("Title exist on Video Article Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on Video Article Page "
						+ driver.getCurrentUrl());
			}

	/*		// Test Written Info
			try {
				WebElement winfo = driver.findElement(By.id("hl"))
						.findElement(By.className("container"))
						.findElement(By.className("container-article"))
						.findElement(By.className("box-byblock"))
						.findElement(By.className("byline"));

				if (winfo.getText().trim().equals("")) {
					messages.add("Written Info missing on Video Article Page "
							+ driver.getCurrentUrl());
				} else
					System.out
							.println("Written Info exist on Video Article Page "
									+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Written Info missing on Video Article Page "
						+ driver.getCurrentUrl());
			}*/

			// Test Video Container
			try {
				WebElement tit = driver.findElement(
						By.className("article-body")).findElement(
						By.className("video-container"));

				if (tit.isDisplayed())
					System.out
							.println("Content exist on Health Video Article page "
									+ driver.getCurrentUrl());

				else
					messages.add("Content missing on Health Video Article page "
							+ driver.getCurrentUrl());

			} catch (Exception e) {
				messages.add("Content missing on Health Video Article page "
						+ driver.getCurrentUrl());
			}

		} catch (Exception e) {
			messages.add("Video Page is not working properly:  "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		assertTrue(dispMessage, dispMessage == "");
	}

	@Test
	public void Slideshow() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		try {
			slideshow = "E:/Livestrong/slideshow.txt";
			s = new SelectRandomURL();
			url = s.choose(new File(slideshow));
			driver.get(baseURL + url);
			Thread.sleep(waittime);

			System.out.println("Slideshow Page...");

			// Test Title
			try {
				List<WebElement> title1 = driver.findElements(By
						.className("grid978"));
				WebElement title = title1.get(1).findElement(
						By.className("page-title"));
				if (title.getText().trim().equals("")) {
					messages.add("Title missing on Slideshow Page "
							+ driver.getCurrentUrl());
				} else
					System.out.println("Title exist on Slideshow Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on Slideshow Page "
						+ driver.getCurrentUrl());
			}

			// Test Disclaimer
			try {
				List<WebElement> title2 = driver
						.findElement(By.className("hl")).findElements(
								By.className("innerbodybox"));

				WebElement title = title2.get(0)
						.findElement(By.className("hl-disclaimer-container"))
						.findElement(By.className("span12"))
						.findElement(By.className("hl-sponsor-disclaimer"))
						.findElement(By.className("hl-disclaimer-full"));
				if (title.getText().trim().equals("")) {
					messages.add("Disclaimer missing on Slideshow Page "
							+ driver.getCurrentUrl());
				} else
					System.out.println("Disclaimer exist on Slideshow Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Disclaimer missing on Slideshow Page "
						+ driver.getCurrentUrl());
			}

			// check slide ribbon and container
			try {
				WebElement slidecontainer = driver.findElement(By
						.className("slidecontainer"));
				if (!(slidecontainer.isDisplayed())) {
					messages.add("Slideshow container is missing on Slideshow Page "
							+ driver.getCurrentUrl());
				}
				WebElement ribbon = driver.findElement(
						By.className("container-slideshow")).findElement(
						By.className("box-slide-progress"));
				if (!(ribbon.isDisplayed())) {
					messages.add("Ribbon missing on Slideshow Page "
							+ driver.getCurrentUrl());
				}

			} catch (Exception e) {
				messages.add("Slideshow container is missing on Slideshow Page "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("Slideshow Page is not working properly: "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		assertTrue(dispMessage, dispMessage == "");
	}

	@Test
	public void TOCarticle() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		try {
			topiccenter = "E:/Livestrong/topiccenter.txt";
			s = new SelectRandomURL();
			url = s.choose(new File(topiccenter));
			driver.get(baseURL + url);
			System.out.println("TOC Page...");
			Thread.sleep(waittime);

			// Test Desclaimer Widget
			try {

				WebElement desclaimer = driver.findElement(By.id("hl"))
						.findElement(By.className("hl-disclaimer-full"));
				if (desclaimer.getText().trim().equals("")) {
					messages.add("Desclaimer Widget missing on TOC Page "
							+ driver.getCurrentUrl());
				} else
					System.out.println("Desclaimer Widget exist on TOC Page "
							+ driver.getCurrentUrl());

			} catch (Exception e) {
				messages.add("Desclaimer Widget missing on TOC Page "
						+ driver.getCurrentUrl());
			}

			// Test Title
			try {
				Thread.sleep(waittime);
				WebElement title = driver.findElement(By.id("hl"))
						.findElement(By.className("container"))
						.findElement(By.id("introduction"))
						.findElement(By.tagName("h2"));
				if (title.getText().trim().equals("")) {
					messages.add("Title missing on TOC Page "
							+ driver.getCurrentUrl());
				} else
					System.out.println("Title exist on TOC Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on TOC Page "
						+ driver.getCurrentUrl());
			}

			// Centerwell Links
			try {
				Thread.sleep(waittime);
				List<WebElement> centerlinks = driver.findElement(
						By.className("innerbodybox")).findElements(
						By.className("onecolimage"));
				centerlinks.get(1);
				for (WebElement boxContent : centerlinks) {
					String src = boxContent.findElement(By.tagName("img"))
							.getAttribute("src");
					System.out.println(src);
					if (src != null) {
						CheckHttpConnection httpConnection = new CheckHttpConnection(
								src);
						if (httpConnection.testHttpConn() != 200) {
							messages.add(src
									+ " Image broken in Read this next section on "
									+ driver.getCurrentUrl());
						}
					}
					String href = boxContent
							.findElement(
									By.className("hl-content-listing-title"))
							.findElement(By.tagName("a")).getAttribute("href");
					if (href != null) {
						CheckHttpConnection httpConnection = new CheckHttpConnection(
								href);
						if (httpConnection.testHttpConn() != 200) {
							messages.add(href
									+ " Link broken in Read this next section on "
									+ driver.getCurrentUrl());
						}
					}
				}
			} catch (Exception e) {
				messages.add("Centerwell links not working properly on TOC Page: "
						+ driver.getCurrentUrl());
			}
		} catch (Exception e) {
			messages.add("TOC Page is not working properly:  "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		assertTrue(dispMessage, dispMessage == "");
	}

	@Test
	public void Standardarticle() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		
		try {
			standardarticle = "E:/Livestrong/standardarticle.txt";
			s = new SelectRandomURL();
			url = s.choose(new File(standardarticle));
			driver.get(baseURL + url);
			System.out.println("Standard Article...");
			Thread.sleep(waittime);

			// Test Desclaimer Widget
			try {

				WebElement desclaimer = driver.findElement(By.id("hl"))
						.findElement(By.className("hl-disclaimer-full"));
				if (desclaimer.getText().trim().equals("")) {
					messages.add("Desclaimer Widget missing on Standard Article Page "
							+ driver.getCurrentUrl());
				} else
					System.out
							.println("Desclaimer Widget exist on Standard Article Page "
									+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Desclaimer Widget missing on Standard Article Page "
						+ driver.getCurrentUrl());
			}

			// Test Title
			try {
				WebElement title = driver.findElement(By.id("hl"))
						.findElement(By.className("container"))
						.findElement(By.className("article-header"))
						.findElement(By.tagName("h1"));
				if (title.getText().trim().equals("")) {
					messages.add("Title missing on Standard Article Page "
							+ driver.getCurrentUrl());
				} else
					System.out.println("Title exist on Standard Article Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on Standard Article Page "
						+ driver.getCurrentUrl());
			}

			// Test Written Info
			try {
				WebElement winfo = driver.findElement(By.id("hl"))
						.findElement(By.className("container"))
						.findElement(By.className("byline"));
				if (winfo.getText().trim().equals("")) {
					messages.add("Written Info missing on Standard Article Page "
							+ driver.getCurrentUrl());
				} else
					System.out
							.println("Written Info exist on Standard Article Page "
									+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Written Info missing on Standard Article Page "
						+ driver.getCurrentUrl());
			}

			// Article Resource Overlay
			try {
				WebElement Resourcelink = driver.findElement(
						By.className("box-link-sources")).findElement(
						By.id("sourcesLink"));

				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", Resourcelink);

				Thread.sleep(waittime);
				jse.executeScript("window.scrollBy(0,-50)", "");
				Thread.sleep(waittime);
				Resourcelink.click();
				 Thread.sleep(waittime);
				WebElement overlay = driver
						.findElement(By.id("overlaySources"));
				if (overlay.isDisplayed()) {
					System.out.println("Article Resource Overlay is opened");
					WebElement Close = driver.findElement(
							By.id("overlaySources")).findElement(
							By.className("overlay-close"));
					Close.click();
					System.out.println("Article Resource Overlay is closed");
				}
			} catch (Exception e) {
				messages.add("Article Resource Overlay missing on Standard Article Page"
						+ driver.getCurrentUrl());
			}

			// Read This Next Section
			try {
				WebElement readnext = driver.findElement(By
						.className("article-tcblocks")).findElement(By
						.className("onecolimage")).findElement(By.className("row12")).findElement(By.tagName("h2"));
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", readnext);
				jse.executeScript("window.scrollBy(0,-200)", "");

				if (!(readnext.isDisplayed())) {
					messages.add("'Read This Next' section title is missing on Standard Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("'Read This Next' section title is displayed on Standard Article Page "
								+ driver.getCurrentUrl());
				
				
		} catch (Exception e) {
			messages.add("'Read This Next' section title is missing on Standard Article Page "
					+ e.toString());
		}
		
		
		} catch (Exception e) {
			messages.add("Standard Article Page is not working properly:  "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		assertTrue(dispMessage, dispMessage == "");
	}

	@Test
	public void Tabbedarticle() throws InterruptedException {
		List<String> messages = new ArrayList<String>();
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		try {
			tabbedarticle = "E:/Livestrong/tabbedarticle.txt";
			s = new SelectRandomURL();
			url = s.choose(new File(tabbedarticle));
			driver.get(baseURL + url);

			System.out.println("Tabbed Article...");
			Thread.sleep(waittime);

			// Test Desclaimer Widget
			try {

				WebElement desclaimer = driver.findElement(By.className("hl-disclaimer-container"))
						.findElement(By.className("hl-disclaimer-full"));
				if (desclaimer.getText().trim().equals("")) {
					messages.add("Desclaimer Widget missing on Tabbed Article Page "
							+ driver.getCurrentUrl());
				} else
					System.out
							.println("Desclaimer Widget exist on Tabbed Article Page "
									+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Desclaimer Widget missing on Tabbed Article Page "
						+ driver.getCurrentUrl());
			}
			// Test Title
			try {
				WebElement title = driver.findElement(By.id("hl"))
						.findElement(By.className("container"))
						.findElement(By.className("article-header"))
						.findElement(By.tagName("h1"));
				if (title.getText().trim().equals("")) {
					messages.add("Title missing on Tabbed Article Page "
							+ driver.getCurrentUrl());
				} else
					System.out.println("Title exist on Tabbed Article Page "
							+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Title missing on Tabbed Article Page "
						+ driver.getCurrentUrl());
			}

			// Test Written Info
			try {
				WebElement winfo = driver.findElement(By.id("hl"))
						.findElement(By.className("container"))
						.findElement(By.id("byline"));
				if (winfo.getText().trim().equals("")) {
					messages.add("Written Info missing on Tabbed Article Page "
							+ driver.getCurrentUrl());
				} else
					System.out
							.println("Written Info exist on Tabbed Article Page "
									+ driver.getCurrentUrl());
			} catch (Exception e) {
				messages.add("Written Info missing on Tabbed Article Page "
						+ driver.getCurrentUrl());
			}

			// Test Tab Navbar
			try {
				Thread.sleep(waittime);
				WebElement navbar = driver.findElement(By
						.className("hl-tabs-tabnav"));
				if (!(navbar.isDisplayed())) {
					messages.add("navbar is missing on TAB article");
				}
				List<WebElement> navLinks = driver.findElement(
						By.className("hl-tabs-tabnav")).findElements(
						By.tagName("a"));
				int counter = 1;
				System.out.println(navLinks.size());
				Thread.sleep(waittime);
				if (navLinks.size() <= 2) {
					messages.add("TABs missing in TOC on TAB article "
							+ driver.getCurrentUrl());
				}
			} catch (Exception e) {
				messages.add("Navbar not Working properly on Tabbed Article Page "
						+ driver.getCurrentUrl());
			}
		
			
			// Read This Next Section
			try {
				WebElement readnext = driver.findElement(By
						.className("article-tcblocks")).findElement(By
						.className("onecolimage")).findElement(By.className("row12")).findElement(By.tagName("h2"));
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", readnext);
				jse.executeScript("window.scrollBy(0,-200)", "");

				if (!(readnext.isDisplayed())) {
					messages.add("'Read This Next' section title is missing on Tabbed Article Page "
							+ driver.getCurrentUrl());
				}
				System.out
						.println("'Read This Next' section title is displayed on Tabbed Article Page "
								+ driver.getCurrentUrl());
				
				
		} catch (Exception e) {
			messages.add("'Read This Next' section title is missing on Tabbed Article Page "
					+ e.toString());
		}
		} catch (Exception e) {
			messages.add("Tabbed Article Page is not working properly:  "
					+ driver.getCurrentUrl());
		}
		String dispMessage = "";
		if (messages.size() > 0) {
			for (String msg : messages) {
				dispMessage = dispMessage + msg + "\n";
			}
		}
		assertTrue(dispMessage, dispMessage == "");
	}

	@AfterClass
	public static void endWebDriver() {
		driver.quit();
	}
}
