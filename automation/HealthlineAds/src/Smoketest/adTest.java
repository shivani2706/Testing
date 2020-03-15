package Smoketest;

import org.testng.annotations.Test;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.testng.annotations.Test;
import org.apache.commons.lang3.time.FastDateFormat;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import qalib.CheckAds;
import qalib.SelectRandomURL;

public class adTest {

	
	static Map<String, String> env;
	static String driverType;
	private static String baseURL;

	private static String domain;
	public WebDriver driver;
	static int dayOfWeek;
	static int dayOfMonth;
	
	public static void createReport(List<String> messages) {
		try {
			File myFile = null;
			try {
				String date = FastDateFormat.getInstance("dd-MM-yyyy").format(
						System.currentTimeMillis());
				myFile = new File("mobileautomation" + date + ".txt");
				if (myFile.exists()) {
				} else {
					myFile.createNewFile();
				}

			} catch (Exception e) {
				System.out.println("Problem in creating report file "
						+ e.toString());
			}
			try {
				FileWriter fstream = null;
				if (myFile.exists()) {
					fstream = new FileWriter(myFile, false);
				} else {
					fstream = new FileWriter(myFile, true);
				}
				BufferedWriter fbw = new BufferedWriter(fstream);
				if (messages.size() > 0) {
					for (String msg : messages) {
						fbw.write(msg);
						fbw.newLine();
					}
				}
				fbw.close();
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
	public void testHomePage() throws IOException, InterruptedException {
		baseURL = "https://sfc-stage01.healthline.com";

		//driver = setDeviceOrientation("iPad Mini", 1024, 768);

		String message = "";
		List<String> messages = new ArrayList<String>();
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		messages.add("Current Date & Time: " + df.format(dateobj));

		

		// ----------------Redesign TAB Article---------------------

		messages.add("       ");

		messages.add("Redesign TAB Article");
		messages.add("===========");
		messages.add(" ");

		try {
			driver = setDevice("");
			driver.manage().window().maximize();
			messages.add("--Desktop BPs--");
			driver.get(baseURL + "/health/benefits-of-crying");
			messages.add("Test Url : " + driver.getCurrentUrl());

			// Check Top DLB Ad & Stikiness for 3 seconds
			try {
				CheckAds TopDlb = new CheckAds(driver);
				message = TopDlb.checkTopDlb();
				if (message != null) {
				
					messages.add("Top dlb Ad is missing..."
							+ driver.getCurrentUrl());
				} else {
					// Check label ADVERTISEMENT
					WebElement adLb1 = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector("#dlb1__container .dlb__text")));
					// Scroll page 155px
					messages.add("Top dlb Ad exist");
					try{
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
						if(googleQueryId.isEmpty()){
							messages.add("Top dlb Ad is not fired..."
									+ driver.getCurrentUrl());
						}else{
							messages.add("Top dlb Ad google query id is --> "+googleQueryId);
							messages.add("Top dlb Ad google item id is --> "+gogleItemId);
							messages.add("Top dlb Ad google creative id is --> "+googleCreativeId);
						}
					}catch (Exception e) {
						messages.add("Top dlb Ad is not fired..."
								+ driver.getCurrentUrl());
					}
					
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,155)");

					// Check stickiness - 1st check that DLB is visible before
					// check 3sec of scrolling 150px
					Thread.sleep(2900);
					WebElement stickyLayer = driver.findElement(By
							.className("dlb__sticky-placeholder"));
					try {
						String styl = stickyLayer.getAttribute("style");
						try{
							stickyLayer = driver.findElement(By
									.className("dlb__sticky-placeholder"));
							messages.add("Top dlb Ad disappeared after 3 seconds of scrolling 150px");
							}catch (Exception e) {
								messages.add("Top dlb Ad not disappeared after 3 seconds of scrolling 150px"
										+ driver.getCurrentUrl());
							}

					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				messages.add("Problem with Top dlb Ad... on Redesign Tabbed Article Page"
						+ e.toString());
			}
			Thread.sleep(2000);
			((JavascriptExecutor) driver)
					.executeScript("window.scrollTo(0,document.body.scrollHeight);");
			Thread.sleep(2000);

			try {
				List<WebElement> tag = driver.findElement(
						By.className("article__content-body")).findElements(
						By.className("content_body"));

				// set counter to check inline dlb after every 2nd section for
				// sections <=6 and after every 3rd section if sections > 6
				int adcounter = 2;
				int j = 1;
				if (tag.size() > 6) {
					j = 2;
				}

				for (int i = j; i < tag.size() - 1;) {
					String txt = tag.get(i).findElement(By.tagName("p"))
							.getText();
					// String
					String xpath = "//div[@id='TOC_TITLE_"
							+ i
							+ "']/following-sibling::div[@class='dlb--inline-container']";

					try {
						WebElement topAd = driver.findElement(By.xpath(xpath));
						WebElement advt = topAd.findElement(By.className("dlb__slot"));
										
						if (advt.getAttribute("id").equalsIgnoreCase(
								"dlb" + adcounter + "__slot__inline")) {
								messages.add("Inline DLB Ad exist after section "
									+ (i + 1));
								String googleQueryId = advt.getAttribute("data-google-query-id");
								String gogleItemId = advt.getAttribute("data-line-item-id");
								String googleCreativeId = advt.getAttribute("data-creative-id");
								
								if(googleQueryId.isEmpty()){
									messages.add("Inline DLB is not fired..." + i + " on Tabbed Article" + driver.getCurrentUrl());
								}else{
									messages.add("Inline DLB Ad google query id is --> "+googleQueryId);
									messages.add("Inline DLB Ad google item id is --> "+gogleItemId);
									messages.add("Inline DLB Ad google creative id is --> "+googleCreativeId);
								}	
								
						}
					} catch (Exception e) {
						messages.add("Inline dlb Ad is missing after section "
								+ (i + 1) + e.toString());
					}
					adcounter++;
					if (tag.size() > 6) {
						i = i + 3;
					} else {
						i = i + 2;
					}
				}

			} catch (Exception E) {
				messages.add("Problem with Inline DLB Ad on Redesign Tabbed Article "
						+ E.toString());
			}

			// Check DMR
			// Calculated number of expected DMR using (height of article
			// section/563)
			try {
				int wdth;
				WebElement articleContent = driver.findElement(By
						.className("tabbed-article"));
				WebElement topstories = driver.findElement(By
						.className("top-stories"));
				WebElement byline = driver.findElement(By.className("byline"));

				wdth = (articleContent.getSize().getHeight()
						- topstories.getSize().getHeight() - byline.getSize()
						.getHeight()) / 563;

				// wdth is number of DMR that should fire
				List<WebElement> dmrs = driver.findElement(
						By.className("body__col-fixed-right")).findElements(
						By.className("dmr--show"));

				for (int i = 1; i <= dmrs.size(); i++) {
				
						WebElement dmr = driver.findElement(By.id("dmr" + i
								+ "__slot"));
						try {
							//WebElement ifrm = dmr.findElement(By.tagName("iframe"));
							messages.add("DMR" + i + " ad exist");
							String googleQueryId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute(
									"data-google-query-id");
							String gogleItemId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-line-item-id");
							String googleCreativeId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-creative-id");
							if (googleQueryId.isEmpty()) {
								messages.add("DMR"
										+ i
										+ " Ad at right rail on Replatform Tabbed Article is not fired.."
										+ driver.getCurrentUrl());
							} else {
								messages.add("DMR Ad google query id is --> "+googleQueryId);
								messages.add("DMR Ad google item id is --> "+gogleItemId);
								messages.add("DMR Ad google creative id is --> "+googleCreativeId);
							}
						} catch (Exception e) {
							messages.add("Rendering problem with DMR"
									+ i
									+ " Ad at right rail on Redesign Tabbed Article");
						}
					
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}
			
			//Read Next DMRs
			
			try {
				
				WebElement readnxtContent = driver.findElement(By
						.className("read-next"));
				List<WebElement> dmrs = readnxtContent.findElements(By.className("content-promotion-story"));
				int j = 11 ;
				int arrLength = dmrs.size();
				for ( int i = 1 ; i <= arrLength-1;) {
					WebElement dmr = driver.findElement(By.id("dmr" + j
								+ "__slot-contentpromo"));
						try {
							//WebElement ifrm = dmr.findElement(By.tagName("iframe"));
							messages.add("DMR" + j + " ad exist");
							String googleQueryId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute(
									"data-google-query-id");
							String gogleItemId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-line-item-id");
							String googleCreativeId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-creative-id");
							if (googleQueryId.isEmpty()) {
								messages.add("DMR"
										+ j
										+ " Ad at Right rail in Read Next on Redesign Tabbed Article is not fired.."
										+ driver.getCurrentUrl());
							} else {
								messages.add("DMR Ad google query id is --> "+googleQueryId);
								messages.add("DMR Ad google item id is --> "+gogleItemId);
								messages.add("DMR Ad google creative id is --> "+googleCreativeId);
							}
							Thread.sleep(10000);
						} catch (Exception e) {
							messages.add("Rendering problem with DMR"
									+ j
									+ " Ad at right rail on Redesign Tabbed Article");
						}
						i=i+2;
						j++;
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}
			// INA ad
			try {

				WebElement inaAdSlot = driver
						.findElement(By.className("article__content-body"))
						.findElement(By.className("ina"))
						.findElement(By.className("ina__slot"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", inaAdSlot);
				jse.executeScript("window.scrollBy(0,-150)", "");
				if (!(inaAdSlot.isDisplayed())) {
					messages.add("INA Ad is Not displayed  "
							+ message);
				}
				messages.add("INA Ad is displayed  "
								);
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("INA Ad is not firing - Desktop breakpoint ");
				}else{
					messages.add("INA Ad is firing - Desktop breakpoint ");
					messages.add("INA Ad google query id is --> "+googleQueryId);
					messages.add("INA Ad google item id is --> "+gogleItemId);
					messages.add("INA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());
			}
			
			// RNA ad
			try {
				WebElement rnaAdSlot = driver
						.findElement(By.className("top-stories"))
						.findElement(By.className("rna"))
						.findElement(By.className("rna__slot"));
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("RNA Ad is not firing - Desktop breakpoint ");
				}else{
					messages.add("RNA Ad is firing - Desktop breakpoint ");
					messages.add("RNA Ad google query id is --> "+googleQueryId);
					messages.add("RNA Ad google item id is --> "+gogleItemId);
					messages.add("RNA Ad google creative id is --> "+googleCreativeId);
				}
								
			} catch (Exception e) {
				messages.add("RNA Ad is Not displayed "
						+ driver.getCurrentUrl());

			}
			// Footer advertisement
			try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Problem in Ads Widget on Redesign Tabbed Article..."
					+ e.toString());
		}

		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			
			driver.get(baseURL + "/health/benefits-of-crying");
			Thread.sleep(3000);
			messages.add(" ");
			messages.add("--1024 BPs--");
			messages.add("Test URL : " + driver.getCurrentUrl());
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkTopDlb();
				if (message != null) {
					messages.add("Top dlb Ad is missing "
							+ message);
				} else {
									
					// Check label ADVERTISEMENT
					WebElement adLb1 = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector("#dlb1__container .dlb__text")));
					// Scroll page 155px
					messages.add("Top dlb Ad exist");
					try{
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
						if(googleQueryId.isEmpty()){
							messages.add("Top dlb Ad is not fired..."
									+ driver.getCurrentUrl());
						}else{
							messages.add("Top dlb Ad google query id is --> "+googleQueryId);
							messages.add("Top dlb Ad google item id is --> "+gogleItemId);
							messages.add("Top dlb Ad google creative id is --> "+googleCreativeId);
						}
					}catch (Exception e) {
						messages.add("Top dlb Ad is not fired..."
								+ driver.getCurrentUrl());
					}
					
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,155)");

					// Check stickiness - 1st check that DLB is visible before
					// check 3sec of scrolling 150px
					Thread.sleep(2900);
					WebElement stickyLayer = driver.findElement(By
							.className("dlb__sticky-placeholder"));
					try {
						String styl = stickyLayer.getAttribute("style");
						try{
							stickyLayer = driver.findElement(By
									.className("dlb__sticky-placeholder"));
							messages.add("Top dlb Ad disappeared after 3 seconds of scrolling 150px");
							}catch (Exception e) {
								messages.add("Top dlb Ad not disappeared after 3 seconds of scrolling 150px"
										+ driver.getCurrentUrl());
							}

					} catch (Exception e) {

					}
	}
			} catch (Exception e) {
				messages.add("Problem with Top dlb Ad... on Redesign Tabbed Article Page"
						+ e.toString());
			}
			// check DMR ad

			try {
				int wdth;
				WebElement articleContent = driver.findElement(By
						.className("tabbed-article"));
				WebElement topstories = driver.findElement(By
						.className("top-stories"));
				WebElement byline = driver.findElement(By.className("byline"));

				wdth = (articleContent.getSize().getHeight()
						- topstories.getSize().getHeight() - byline.getSize()
						.getHeight()) / 563;
				((JavascriptExecutor) driver)
						.executeScript("window.scrollTo(0,document.body.scrollHeight);");

				// wdth is number of DMR that should fire
				List<WebElement> dmrs = driver.findElement(
						By.className("body__col-fixed-right")).findElements(
						By.className("dmr--show"));

				for (int i = 1; i <= dmrs.size(); i++) {
					try {
						WebElement dmr = driver.findElement(By.id("dmr" + i
								+ "__slot"));
						messages.add("DMR" + i + " ad exist");
						String googleQueryId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute(
								"data-google-query-id");
						String gogleItemId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-creative-id");
						if (googleQueryId.isEmpty()) {
							messages.add("DMR"
									+ i
									+ " Ad at right rail on Replatform Tabbed Article is not fired.."
									+ driver.getCurrentUrl());
						} else {
							messages.add("DMR Ad google query id is --> "+googleQueryId);
							messages.add("DMR Ad google item id is --> "+gogleItemId);
							messages.add("DMR Ad google creative id is --> "+googleCreativeId);
						}
					} catch (Exception e) {
						messages.add("DMR" + i
								+ " Ad is missing on Redesign Tabbed Article Page ");
					}
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}
			
			//Read Next DMRs
			
			try {
				
				((JavascriptExecutor) driver)
				.executeScript("window.scrollTo(0,document.body.scrollHeight);");
				WebElement readnxtContent = driver.findElement(By
						.className("read-next"));
				List<WebElement> dmrs = readnxtContent.findElements(By.className("content-promotion-story"));
				int j = 11 ;
				int arrLength = dmrs.size();
				for ( int i = 1 ; i <= arrLength-1;) {
					WebElement dmr = driver.findElement(By.id("dmr" + j
								+ "__slot-contentpromo"));
						try {
							//WebElement ifrm = dmr.findElement(By.tagName("iframe"));
							messages.add("DMR" + j + " ad exist");
							String googleQueryId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute(
									"data-google-query-id");
							String gogleItemId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-line-item-id");
							String googleCreativeId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-creative-id");
							if (googleQueryId.isEmpty()) {
								messages.add("DMR"
										+ j
										+ " Ad at Right rail in Read Next on Redesign Tabbed Article is not fired.."
										+ driver.getCurrentUrl());
							} else {
								messages.add("DMR Ad google query id is --> "+googleQueryId);
								messages.add("DMR Ad google item id is --> "+gogleItemId);
								messages.add("DMR Ad google creative id is --> "+googleCreativeId);
							}
							Thread.sleep(10000);
						} catch (Exception e) {
							messages.add("Rendering problem with DMR"
									+ j
									+ " Ad at right rail on Redesign Tabbed Article");
						}
						i=i+2;
						j++;
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// INA ad
			try {

				WebElement inaAdSlot = driver
						.findElement(By.className("article__content-body"))
						.findElement(By.className("ina"))
						.findElement(By.className("ina__slot"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", inaAdSlot);
				jse.executeScript("window.scrollBy(0,-150)", "");
				if (!(inaAdSlot.isDisplayed())) {
					messages.add("INA Ad is Not displayed "
							+ message);
				}
				messages.add("INA Ad is displayed "
								);
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("INA Ad is not firing - 1024 breakpoint ");
				}else{
					messages.add("INA Ad is firing - 1024 breakpoint ");
					messages.add("INA Ad google query id is --> "+googleQueryId);
					messages.add("INA Ad google item id is --> "+gogleItemId);
					messages.add("INA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());

			}
			// RNA ad
			try {
				WebElement rnaAdSlot = driver
						.findElement(By.className("top-stories"))
						.findElement(By.className("rna"))
						.findElement(By.className("rna__slot"));

				
				 JavascriptExecutor jse = (JavascriptExecutor) driver;
				 ((JavascriptExecutor) driver).executeScript(
				 "arguments[0].scrollIntoView(true);", rnaAdSlot);
				  jse.executeScript("window.scrollBy(0,-150)", "");
				 
				if (!(rnaAdSlot.isDisplayed())) {
					messages.add("RNA Ad is Not displayed "
							+ message);
				}
				messages.add("RNA Ad is displayed "
								);
				  String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("RNA Ad is not firing - 1024 breakpoint ");
					}else{
						messages.add("RNA Ad is firing - 1024 breakpoint ");
						messages.add("RNA Ad google query id is --> "+googleQueryId);
						messages.add("RNA Ad google item id is --> "+gogleItemId);
						messages.add("RNA Ad google creative id is --> "+googleCreativeId);
					}
			} catch (Exception e) {
				messages.add("RNA Ad is Not displayed  "
						+ driver.getCurrentUrl());

			}
			// Footer advertisement
			try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
					driver.close();
		} catch (Exception e) {
			messages.add("Exception in Redesign Tabbed Article Page  "
					+ e.toString());
		}

		// 768 BP
		try {
			driver = setDeviceOrientation("iPad Mini", 768, 1024);
			messages.add(" ");
			messages.add("--768 BPs--");
			driver.get(baseURL + "/health/benefits-of-crying");
			Thread.sleep(3000);
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkTopDlb();
				if (message != null) {
					messages.add("DLB Ad is missing "
							+ message);
				} else {
									
					// Check label ADVERTISEMENT
					WebElement adLb1 = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector("#dlb1__container .dlb__text")));
					// Scroll page 155px
					messages.add("Top dlb Ad exist");
					try{
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
						if(googleQueryId.isEmpty()){
							messages.add("Top dlb Ad is not fired..."
									+ driver.getCurrentUrl());
						}else{
							messages.add("Top dlb Ad google query id is --> "+googleQueryId);
							messages.add("Top dlb Ad google item id is --> "+gogleItemId);
							messages.add("Top dlb Ad google creative id is --> "+googleCreativeId);
						}
					}catch (Exception e) {
						messages.add("Top dlb Ad is not fired..."
								+ driver.getCurrentUrl());
					}
					
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,155)");

					// Check stickiness - 1st check that DLB is visible before
					// check 3sec of scrolling 150px
					Thread.sleep(2900);
					WebElement stickyLayer = driver.findElement(By
							.className("dlb__sticky-placeholder"));
					try {
						String styl = stickyLayer.getAttribute("style");
						try{
							stickyLayer = driver.findElement(By
									.className("dlb__sticky-placeholder"));
							messages.add("Top dlb Ad disappeared after 3 seconds of scrolling 150px");
							}catch (Exception e) {
								messages.add("Top dlb Ad not disappeared after 3 seconds of scrolling 150px"
										+ driver.getCurrentUrl());
							}

					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				messages.add("DLB Ad is missing "
						+ driver.getCurrentUrl());
			}
			// check MR ad
			try {
				List<WebElement> tag = driver.findElement(
						By.className("article__content-body")).findElements(
						By.className("content_body"));

				// set counter to check inline dlb after every 2nd section for
				// sections <=6 and after every 3rd section if sections > 6
				int adcounter = 1;
				int j = 1;

				for (int i = j; i < tag.size() - 1;) {
					String txt = tag.get(i).findElement(By.tagName("p"))
							.getText();
					// String
					String xpath = "//div[@id='TOC_TITLE_"
							+ i
							+ "']/following-sibling::div[@class='dmr row dmr--tablet']";

					try {
						WebElement topAd = driver.findElement(By.xpath(xpath));
						WebElement advt = topAd.findElement(By
								.className("dmr__slot"));
						// System.out.println(advt.getAttribute("id"));
						if (advt.getAttribute("id").equalsIgnoreCase(
								"dmr" + adcounter + "__slot-tablet")) {
							// System.out.println(advt.getAttribute("id"));
							messages.add("DMR Ad exist after section "
									+ (i + 1));
					String googleQueryId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-google-query-id");
					String gogleItemId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-line-item-id");
					String googleCreativeId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-creative-id");
					if (googleQueryId.isEmpty()) {
						messages.add("DMR"
								+ i
								+ " Ad at right rail on Replatform Tabbed Article is not fired.."
								+ driver.getCurrentUrl());
					} else {
						messages.add("DMR Ad google query id is --> "+googleQueryId);
						messages.add("DMR Ad google item id is --> "+gogleItemId);
						messages.add("DMR Ad google creative id is --> "+googleCreativeId);
					}
						}
					} catch (Exception e) {
						messages.add("DMR Ad is missing after section "
								+ (i + 1) + e.toString());
					}
					adcounter++;

					i = i + 2;
				}
			} catch (Exception e) {
				messages.add("DMR Ad is missing "
						+ driver.getCurrentUrl());
			}
			// INA ad
			try {

				WebElement inaAdSlot = driver
						.findElement(By.className("article__content-body"))
						.findElement(By.className("ina"))
						.findElement(By.className("ina__slot"));

				if (!(inaAdSlot.isDisplayed())) {
					messages.add("INA Ad is Not displayed "
							+ message);
				}
				messages.add("INA Ad is displayed  "
								);
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("INA Ad is not firing - 768 breakpoint ");
				}else{
					messages.add("INA Ad is firing - 768 breakpoint ");
					messages.add("INA Ad google query id is --> "+googleQueryId);
					messages.add("INA Ad google item id is --> "+gogleItemId);
					messages.add("INA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());
			}

			// Footer advertisement
			try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Redesign Tabbed Article Page - 768 BP  "
					+ e.toString());
		}

		try {
			driver = setDevice("iPhone 5");
			messages.add(" ");
			messages.add("--320 BPs--");
			driver.get(baseURL + "/health/alp");
			driver.navigate().refresh();
			Thread.sleep(4000);
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkRedesignStickyLB();
				if (message != null) {
					messages.add("MLB Ad is missing "
							+ message);
				} else {
					messages.add("MLB Ad is present "
							);
					
					try{
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-creative-id");
						if(googleQueryId.isEmpty()){
							messages.add("MLB Ad is not fired..."
									+ driver.getCurrentUrl());
						}else{
							messages.add("MLB Ad google query id is --> "+googleQueryId);
							messages.add("MLB Ad google item id is --> "+gogleItemId);
							messages.add("MLB Ad google creative id is --> "+googleCreativeId);
						}
					}catch (Exception e) {
						messages.add("MLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}
				}
			} catch (Exception e) {
				messages.add("MLB Ad is missing  "
						+ driver.getCurrentUrl());
			}
			// check MMR ad
			try {
				List<WebElement> tag = driver.findElement(
						By.className("article__content-body")).findElements(
						By.className("content_body"));

				// set counter to check inline dlb after every 2nd section for
				// sections <=6 and after every 3rd section if sections > 6
				int adcounter = 1;
				int j = 1;

				for (int i = j; i < tag.size()-1;) {
					String txt = tag.get(i).findElement(By.tagName("p"))
							.getText();
					// String
					String xpath = "//div[@id='TOC_TITLE_" + i
							+ "']/following-sibling::div[@class='mmr row']";

					try {
						WebElement topAd = driver.findElement(By.xpath(xpath));
						WebElement advt = topAd.findElement(By
								.className("mmr__slot"));
						// System.out.println(advt.getAttribute("id"));
						if (advt.getAttribute("id").equalsIgnoreCase(
								"mmr" + adcounter + "__slot")) {
							// System.out.println(advt.getAttribute("id"));
							messages.add("MMR Ad exist after section "
									+ (i + 1));
					String googleQueryId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-google-query-id");
					String gogleItemId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-line-item-id");
					String googleCreativeId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-creative-id");
					if (googleQueryId.isEmpty()) {
						messages.add("MMR"
								+ i
								+ " Ad at right rail on Replatform Tabbed Article is not fired.."
								+ driver.getCurrentUrl());
					} else {
						messages.add("MMR Ad google query id is --> "+googleQueryId);
						messages.add("MMR Ad google item id is --> "+gogleItemId);
						messages.add("MMR Ad google creative id is --> "+googleCreativeId);
					}
						}
					} catch (Exception e) {
						messages.add("MMR Ad is missing after section "
								+ (i + 1) + e.toString());
					}
					adcounter++;

					i = i + 2;

				}

			} catch (Exception E) {
				messages.add("Problem with inline MMR Ad on Redesign Tabbed Article "
						+ E.toString());
			}

			// INA ad
			try {

				WebElement inaAdSlot = driver
						.findElement(By.className("article__content-body"))
						.findElement(By.className("ina"))
						.findElement(By.className("ina__slot"));

				if (!(inaAdSlot.isDisplayed())) {
					messages.add("INA Ad is Not displayed "
							+ message);
				}
				messages.add("INA Ad is displayed "
					);
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("INA Ad is not firing - 320 BP ");
				}else{
					messages.add("INA Ad is firing - 320 BP ");
					messages.add("INA Ad google query id is --> "+googleQueryId);
					messages.add("INA Ad google item id is --> "+gogleItemId);
					messages.add("INA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());
			}

			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Redesign Tabbed Article- 320 BP test "
					+ e.toString());
		}

		// -------------------------------------Redesign Standard
		// Article-------------------------------------------------

		messages.add("       ");

		messages.add("Redesign Standard Article");
		messages.add("===========");
		messages.add(" ");
		// Check advertisements in standard article redesign
		try {
			driver = setDevice("");

			driver.manage().window().maximize();

			driver.get(baseURL + "/health/alcoholism/withdrawal");
			messages.add("--Desktop BPs--");
			messages.add("Test Url : " + driver.getCurrentUrl());

			// Check Top DLB Ad & Stikiness for 3 seconds
			try {
				CheckAds TopDlb = new CheckAds(driver);
				message = TopDlb.checkTopDlb();
				if (message != null) {
					// System.out.println("Top LB Ad is missing...");
					messages.add("Top dlb Ad is missing in Redesign Standard Article..."
							+ message);
				} else {
					// Check label ADVERTISEMENT
					WebElement adLb1 = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector("#dlb1__container .dlb__text")));
					// Scroll page 155px
					messages.add("Top dlb Ad exist");
					try{
					//	String adLbid = driver.findElement(By.id("dlb1__slot")).getAttribute("data-google-query-id");
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
						if(googleQueryId.isEmpty()){
							messages.add("Top dlb Ad is not fired..."
									+ driver.getCurrentUrl());
						}else{
							messages.add("Top dlb Ad google query id is --> "+googleQueryId);
							messages.add("Top dlb Ad google item id is --> "+gogleItemId);
							messages.add("Top dlb Ad google creative id is --> "+googleCreativeId);
						}
					}catch (Exception e) {
						messages.add("Top dlb Ad is not fired..."
								+ driver.getCurrentUrl());
					}
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,155)");

					// Check stickiness - 1st check that DLB is visible before
					// 3sec of scrolling 150px
					Thread.sleep(2900);
					WebElement stickyLayer = driver.findElement(By
							.className("dlb__sticky-placeholder"));
					try {
						String styl = stickyLayer.getAttribute("style");
						if (styl.indexOf("display: none;") >= 0) {
							messages.add("Problem with Top DLB Ad stickiness after scrolling 150px"
									+ driver.getCurrentUrl());
						} else {
							// Check stickiness - if DLB is visible for 3seconds
							// than after 1 second check that it disappeared
							Thread.sleep(2000);
							stickyLayer = driver.findElement(By
									.className("dlb__sticky-placeholder"));
							styl = stickyLayer.getAttribute("style");
							if (styl.indexOf("display: none;") < 0) {
								messages.add("Top DLB Ad not disappeared after 3 seconds of scrolling 150px"
										+ driver.getCurrentUrl());
							}
						}

					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				messages.add("Problem with Top DLB Ad... on Redesign Standard Article"
						+ e.toString());
			}
			Thread.sleep(2000);
			((JavascriptExecutor) driver)
					.executeScript("window.scrollTo(0,document.body.scrollHeight);");
			Thread.sleep(2000);

			try {
				List<WebElement> tag = driver.findElements(By
						.cssSelector(".article__content-body p"));
				Thread.sleep(3000);
				// set counter to check inline dlb after every 2nd section for
				// sections <=6 and after every 3rd section if sections > 6
				int adcounter = 2;
				int j = 0;
				if (tag.size() > 5) {
					j = 1;
					if (tag.size() > 15) {
						j = 2;
						if (tag.size() > 20) {
							j = 3;
						}
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

						} else {
							if (i == 3) {
								adcount = 20;
							}
						}
					}				
					String xpath = "//div[@class='article__content-body']/p["
							+ adcount
							+ "]/following-sibling::div[@class='dlb--inline-container']";
					Thread.sleep(3000);
					try {
						WebElement topAd = driver.findElement(By.xpath(xpath));
						WebElement advt = topAd.findElement(By
								.className("dlb__slot"));
						if (advt.getAttribute("id").equalsIgnoreCase(
								"dlb" + (i + 1) + "__slot__inline")) {
							messages.add("Inline DLB Ad exist after Para "
									+ (adcount));
							String googleQueryId = advt.getAttribute("data-google-query-id");
							String gogleItemId = advt.getAttribute("data-line-item-id");
							String googleCreativeId = advt.getAttribute("data-creative-id");
							if(googleQueryId.isEmpty()){
								messages.add("Inline DLB is not fired..." + i + " on Standard Article" + driver.getCurrentUrl());
							}else{
							//	System.out.println("Inline DLB Ad is fired..." + i + " Advertisement id is --> " + googleQueryId);
								messages.add("Inline DLB Ad is firing on Tabbed Article - Desktop breakpoint ");
								messages.add("Inline DLB Ad google query id is --> "+googleQueryId);
								messages.add("Inline DLB Ad google item id is --> "+gogleItemId);
								messages.add("Inline DLB Ad google creative id is --> "+googleCreativeId);
							}
						}
					} catch (Exception e) {
						messages.add("Inline DLB Ad is missing after Para "
								+ (i + 1) + e.toString());
					}
					adcounter++;

					i = i + 1;
				}

			} catch (Exception E) {
				messages.add("Problem with inline DLB Ad on Redesign Standard Article "
						+ E.toString());
			}

			// Check DMR
			// Calculated number of expected DMR using (height of article
			// section/563)
			try {
				int wdth;
				WebElement articleContent = driver.findElement(By
						.className("standard-article"));
				WebElement topstories = driver.findElement(By
						.className("top-stories"));
				WebElement byline = driver.findElement(By.className("byline"));

				wdth = (articleContent.getSize().getHeight()
						- topstories.getSize().getHeight() - byline.getSize()
						.getHeight()) / 563;

				// wdth is number of DMR that should fire
				List<WebElement> dmrs = driver.findElement(
						By.className("body__col-fixed-right")).findElements(
						By.className("dmr--show"));

				for (int i = 1; i <= dmrs.size(); i++) {
					
						WebElement dmr = driver.findElement(By.id("dmr" + i
								+ "__slot"));
						try {
							messages.add("DMR" + i + " ad exist");
							String googleQueryId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute(
							"data-google-query-id");
					String gogleItemId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-line-item-id");
					String googleCreativeId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-creative-id");
					if (googleQueryId.isEmpty()) {
						messages.add("DMR"
								+ i
								+ " Ad at right rail on Redesign Standard Article is not fired.."
								+ driver.getCurrentUrl());
					} else {
						messages.add("DMR Ad google query id is --> "+googleQueryId);
						messages.add("DMR Ad google item id is --> "+gogleItemId);
						messages.add("DMR Ad google creative id is --> "+googleCreativeId);
					}
						} catch (Exception e) {
							messages.add("Rendering problem with DMR"
									+ i
									+ " Ad at right rail on Redesign Standard Article");
						}
					
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}

//Read Next DMRs
			
			try {
				
				WebElement readnxtContent = driver.findElement(By
						.className("read-next"));
				List<WebElement> dmrs = readnxtContent.findElements(By.className("content-promotion-story"));
				int j = 11 ;
				int arrLength = dmrs.size();
				for ( int i = 1 ; i <= arrLength-1;) {
					WebElement dmr = driver.findElement(By.id("dmr" + j
								+ "__slot-contentpromo"));
						try {
							//WebElement ifrm = dmr.findElement(By.tagName("iframe"));
							messages.add("DMR" + j + " ad exist");
							String googleQueryId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute(
									"data-google-query-id");
							String gogleItemId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-line-item-id");
							String googleCreativeId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-creative-id");
							if (googleQueryId.isEmpty()) {
								messages.add("DMR"
										+ j
										+ " Ad at Right rail in Read Next on Redesign Standard Article is not fired.."
										+ driver.getCurrentUrl());
							} else {
								messages.add("DMR Ad google query id is --> "+googleQueryId);
								messages.add("DMR Ad google item id is --> "+gogleItemId);
								messages.add("DMR Ad google creative id is --> "+googleCreativeId);
							}
							Thread.sleep(10000);
						} catch (Exception e) {
							messages.add("Rendering problem with DMR"
									+ j
									+ " Ad at right rail on Redesign Standard Article");
						}
						i=i+2;
						j++;
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}


			// INA ad
			try {

				WebElement inaAdSlot = driver
						.findElement(By.className("article__content-body"))
						.findElement(By.className("ina"))
						.findElement(By.className("ina__slot"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", inaAdSlot);
				jse.executeScript("window.scrollBy(0,-150)", "");
				if (!(inaAdSlot.isDisplayed())) {
					messages.add("INA Ad is Not displayed "
							+ message);
				}
				messages.add("INA Ad is displayed "
								);
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("INA Ad is not firing - Desktop breakpoint ");
				}else{
					messages.add("INA Ad is firing - Desktop breakpoint ");
					messages.add("INA Ad google query id is --> "+googleQueryId);
					messages.add("INA Ad google item id is --> "+gogleItemId);
					messages.add("INA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());

			}
			// RNA ad
			try {
				WebElement rnaAdSlot = driver
						.findElement(By.className("top-stories"))
						.findElement(By.className("rna"))
						.findElement(By.className("rna__slot"));
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("RNA Ad is not firing - Desktop breakpoint ");
				}else{
					messages.add("RNA Ad is firing - Desktop breakpoint ");
					messages.add("RNA Ad google query id is --> "+googleQueryId);
					messages.add("RNA Ad google item id is --> "+gogleItemId);
					messages.add("RNA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("RNA Ad is Not displayed "
						+ driver.getCurrentUrl());

			}
			// Footer advertisement
			try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}

			driver.close();
		} catch (Exception e) {
			messages.add("Problem in Ads Widget on Redesign Standard Article..."
					+ e.toString());
		}

		try{
			driver = setDeviceOrientation("iPad Mini",1024,768);
			driver.get(baseURL + "/health/alcoholism/withdrawal");
			Thread.sleep(3000);
			messages.add(" ");
			messages.add("--1024 BPs--");
			messages.add("Test URL : " + driver.getCurrentUrl());
		try {
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkTopDlb();
			if (message != null) {
				messages.add("DLB Ad is missing "
						+ message);
			} else {
				// Check label ADVERTISEMENT
				WebElement adLb1 = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector("#dlb1__container .dlb__text")));
				// Scroll page 155px
				messages.add("Top DLB Ad exist");
				try{
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("Top DLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}else{
						messages.add("Top DLB Ad google query id is --> "+googleQueryId);
						messages.add("Top DLB Ad google item id is --> "+gogleItemId);
						messages.add("Top DLB Ad google creative id is --> "+googleCreativeId);
					}
				}catch (Exception e) {
					messages.add("Top DLB Ad is missing fired..."
							+ driver.getCurrentUrl());
				}
				((JavascriptExecutor) driver)
				.executeScript("window.scrollBy(0,155)");
				// Check stickiness - 1st check that DLB is visible before
				// check 3sec of scrolling 150px
				Thread.sleep(2900);
				WebElement stickyLayer = driver.findElement(By
						.className("dlb__sticky-placeholder"));
				try {
					String styl = stickyLayer.getAttribute("style");
					try{
						stickyLayer = driver.findElement(By
								.className("dlb__sticky-placeholder"));
						messages.add("Top DLB Ad disappeared after 3 seconds of scrolling 150px");
						}catch (Exception e) {
							messages.add("Top DLB Ad not disappeared after 3 seconds of scrolling 150px"
									+ driver.getCurrentUrl());
						}

				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			messages.add("DLB Ad is missing "
					+ driver.getCurrentUrl());
		}
		// check DMR ad
		
		try {
			int wdth;
			WebElement articleContent = driver.findElement(By
					.className("standard-article"));
			WebElement topstories = driver.findElement(By
					.className("top-stories"));
			WebElement byline = driver.findElement(By.className("byline"));

			wdth = (articleContent.getSize().getHeight()
					- topstories.getSize().getHeight() - byline.getSize()
					.getHeight()) / 563;

			// wdth is number of DMR that should fire
			List<WebElement> dmrs = driver.findElement(
					By.className("body__col-fixed-right")).findElements(
					By.className("dmr--show"));

			for (int i = 1; i <= dmrs.size(); i++) {
				
					WebElement dmr = driver.findElement(By.id("dmr" + i
							+ "__slot"));
					try {
						messages.add("DMR" + i + " ad exist");
						String googleQueryId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute(
						"data-google-query-id");
				String gogleItemId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-line-item-id");
				String googleCreativeId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-creative-id");
				if (googleQueryId.isEmpty()) {
					messages.add("DMR"
							+ i
							+ " Ad at right rail on Redesign Standard Article is not fired.."
							+ driver.getCurrentUrl());
				} else {
					messages.add("DMR Ad google query id is --> "+googleQueryId);
					messages.add("DMR Ad google item id is --> "+gogleItemId);
					messages.add("DMR Ad google creative id is --> "+googleCreativeId);
				}
					} catch (Exception e) {
						messages.add("Rendering problem with DMR"
								+ i
								+ " Ad at right rail on Redesign Standard Article");
					}
				
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		//Read Next DMRs
		
		try {
			
			WebElement readnxtContent = driver.findElement(By
					.className("read-next"));
			List<WebElement> dmrs = readnxtContent.findElements(By.className("content-promotion-story"));
			int j = 11 ;
			int arrLength = dmrs.size();
			for ( int i = 1 ; i <= arrLength-1;) {
				WebElement dmr = driver.findElement(By.id("dmr" + j
							+ "__slot-contentpromo"));
					try {
						//WebElement ifrm = dmr.findElement(By.tagName("iframe"));
						messages.add("DMR" + j + " ad exist");
						String googleQueryId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute(
								"data-google-query-id");
						String gogleItemId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-creative-id");
						if (googleQueryId.isEmpty()) {
							messages.add("DMR"
									+ j
									+ " Ad at Right rail in Read Next on Redesign Standard Article is not fired.."
									+ driver.getCurrentUrl());
						} else {
							messages.add("DMR Ad google query id is --> "+googleQueryId);
							messages.add("DMR Ad google item id is --> "+gogleItemId);
							messages.add("DMR Ad google creative id is --> "+googleCreativeId);
						}
						Thread.sleep(10000);
					} catch (Exception e) {
						messages.add("Rendering problem with DMR"
								+ j
								+ " Ad at right rail on Redesign Standard Article");
					}
					i=i+2;
					j++;
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}


		//INA ad
		try {
			
					WebElement inaAdSlot = driver.findElement(
							By.className("article__content-body")).findElement(By.className("ina")).findElement(By.className("ina__slot"));
					JavascriptExecutor jse = (JavascriptExecutor) driver;
					((JavascriptExecutor) driver).executeScript(
							"arguments[0].scrollIntoView(true);", inaAdSlot);
					jse.executeScript("window.scrollBy(0,-150)", "");
					if (!(inaAdSlot.isDisplayed())) {
						messages.add("INA Ad is Not displayed "
								+ message);
					}
					messages.add("INA Ad is displayed "
									);
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("INA Ad is not firing - 1024 breakpoint ");
					}else{
						messages.add("INA Ad is firing - 1024 breakpoint ");
						messages.add("INA Ad google query id is --> "+googleQueryId);
						messages.add("INA Ad google item id is --> "+gogleItemId);
						messages.add("INA Ad google creative id is --> "+googleCreativeId);
					}
				} catch (Exception e) {
					messages.add("INA Ad is Not displayed "
							+ driver.getCurrentUrl());

			}
				//RNA ad
				try {
					WebElement rnaAdSlot = driver.findElement(By.className("top-stories")).findElement(By.className("rna")).findElement(By.className("rna__slot"));
				
					if (!(rnaAdSlot.isDisplayed())) {
						messages.add("RNA Ad is Not displayed "
								+ message);
					}
					messages.add("RNA Ad is displayed "
									);
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("RNA Ad is not firing - 1024 breakpoint ");
					}else{
						messages.add("RNA Ad is firing - 1024 breakpoint ");
						messages.add("RNA Ad google query id is --> "+googleQueryId);
						messages.add("RNA Ad google item id is --> "+gogleItemId);
						messages.add("RNA Ad google creative id is --> "+googleCreativeId);
					}
				} catch (Exception e) {
					messages.add("RNA Ad is Not displayed "
							+ driver.getCurrentUrl());

					}
		// Footer advertisement
		try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
		messages.add("       ");
		driver.close();
		} catch (Exception e) {
			messages.add("Exception in Redesign Standard Article Page - 1024 BP test  " + e.toString());
		}
	// 768 BP
		try{
			driver = setDeviceOrientation("iPad Mini",768,1024);
			messages.add(" ");
			messages.add("--768 BPs--");
			driver.get(baseURL + "/health/alcoholism/withdrawal");
			Thread.sleep(3000);
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkTopDlb();
				if (message != null) {
					messages.add("DLB Ad is missing "
							+ message);
				} else {
									
					// Check label ADVERTISEMENT
					WebElement adLb1 = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector("#dlb1__container .dlb__text")));
					// Scroll page 155px
					messages.add("Top DLB Ad exist");
					try{
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
						if(googleQueryId.isEmpty()){
							messages.add("Top DLB Ad is not fired..."
									+ driver.getCurrentUrl());
						}else{
							messages.add("Top DLB Ad google query id is --> "+googleQueryId);
							messages.add("Top DLB Ad google item id is --> "+gogleItemId);
							messages.add("Top DLB Ad google creative id is --> "+googleCreativeId);
						}
					}catch (Exception e) {
						messages.add("Top DLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}
					
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,155)");

					// Check stickiness - 1st check that DLB is visible before
					// check 3sec of scrolling 150px
					Thread.sleep(2900);
					WebElement stickyLayer = driver.findElement(By
							.className("dlb__sticky-placeholder"));
					try {
						String styl = stickyLayer.getAttribute("style");
						try{
							stickyLayer = driver.findElement(By
									.className("dlb__sticky-placeholder"));
							messages.add("Top DLB Ad disappeared after 3 seconds of scrolling 150px");
							}catch (Exception e) {
								messages.add("Top DLB Ad not disappeared after 3 seconds of scrolling 150px"
										+ driver.getCurrentUrl());
							}

					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				messages.add("DLB Ad is missing "
						+ driver.getCurrentUrl());
			}
		// check DMR ad
			try {
				 List<WebElement> dmrs = driver.findElement(
	                     By.className("article__content-body")).findElements(     
	                     By.className("dmr--tablet"));
				
				// set counter to check inline dlb after every 2nd section for
				// sections <=6 and after every 3rd section if sections > 6
						
			
				 for (int i = 1; i <= dmrs.size(); i++){
					

					 try {
	                     WebElement dmr = driver.findElement(By.id("dmr" + i
	                                     + "__slot-tablet"));
	                     try {
	                             WebElement ifrm = dmr.findElement(By
	                                             .tagName("iframe"));
	                             messages.add("DMR" + i + " ad exist");
	                             
	                             try{
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dmr__slot"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dmr__slot"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dmr__slot"))).getAttribute("data-creative-id");
						if(googleQueryId.isEmpty()){
							messages.add("Top DMR Ad is not fired..."
									+ driver.getCurrentUrl());
						}else{
							messages.add("Top DMR Ad google query id is --> "+googleQueryId);
							messages.add("Top DMR Ad google item id is --> "+gogleItemId);
							messages.add("Top DMR Ad google creative id is --> "+googleCreativeId);
						}
					}catch (Exception e) {
						messages.add("Top DMR Ad is not fired..."
								+ driver.getCurrentUrl());
					}
	                     } catch (Exception e) {
	                         messages.add("DMR" + i + " Ad is missing ");
	                     }
	             }
	     catch (Exception e) {
						messages.add("DMR Ad is missing after section "
								+ (i + 1)+e.toString());
					}
					
				}
			} catch (Exception e) {
				messages.add("DMR Ad is missing on Redesign Standard Article Page - 768 breakpoint "
						+ driver.getCurrentUrl());
			}
		//INA ad
		try {
			
					WebElement inaAdSlot = driver.findElement(
							By.className("article__content-body")).findElement(By.className("ina")).findElement(By.className("ina__slot"));
				
					if (!(inaAdSlot.isDisplayed())) {
						messages.add("INA Ad is Not displayed "
								+ message);
					}
					messages.add("INA Ad is displayed "
									);
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("INA Ad is not firing - 768 breakpoint ");
					}else{
						messages.add("INA Ad is firing - 768 breakpoint ");
						messages.add("INA Ad google query id is --> "+googleQueryId);
						messages.add("INA Ad google item id is --> "+gogleItemId);
						messages.add("INA Ad google creative id is --> "+googleCreativeId);
					}
				} catch (Exception e) {
					messages.add("INA Ad is Not displayed "
							+ driver.getCurrentUrl());
	}
		
		// Footer advertisement
		try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
		driver.close();
		} catch (Exception e) {
			messages.add("Exception in Redesign Standard Page - 768 BP test  " + e.toString());
		}
	
		try{
			driver = setDevice("iPhone 5");
			messages.add(" ");
			messages.add("--320 BPs--");
			driver.get(baseURL + "/health/alcoholism/withdrawal");
			driver.navigate().refresh();
			Thread.sleep(4000);
		try {
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkRedesignStickyLB();
			if (message != null) {
				messages.add("MLB Ad is missing "
						+ message);
			} else {
				messages.add("MLB Ad is present " );
				
				try{
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("MLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}else{
						messages.add("MLB Ad google query id is --> "+googleQueryId);
						messages.add("MLB Ad google item id is --> "+gogleItemId);
						messages.add("MLB Ad google creative id is --> "+googleCreativeId);
					}
				}catch (Exception e) {
					messages.add("MLB Ad is not fired..."
							+ driver.getCurrentUrl());
				}
			}
		} catch (Exception e) {
			messages.add("MLB Ad is missing "
					+ driver.getCurrentUrl());
		}
		// check MR1 ad
		try {
			List<WebElement> mmrs = driver.findElement(
                    By.className("article__content-body")).findElements(     
                    By.className("mmr"));
			
			// set counter to check inline dlb after every 2nd section for
			// sections <=6 and after every 3rd section if sections > 6
	
			for (int i = 1; i <= mmrs.size(); i++) {
				
				try {
					 WebElement mmr = driver.findElement(By.id("mmr" + i
                             + "__slot"));
					//System.out.println(advt.getAttribute("id"));
					 messages.add("MMR Ad exist after section "
								+ (i + 1));
				String googleQueryId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-google-query-id");
				String gogleItemId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-line-item-id");
				String googleCreativeId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-creative-id");
				if (googleQueryId.isEmpty()) {
					messages.add("MMR"
							+ i
							+ " Ad at right rail on Redesign Standard Article is not fired.."
							+ driver.getCurrentUrl());
				} else {
					messages.add("MMR Ad google query id is --> "+googleQueryId);
					messages.add("MMR Ad google item id is --> "+gogleItemId);
					messages.add("MMR Ad google creative id is --> "+googleCreativeId);
				}
					try {
                        WebElement ifrm = mmr.findElement(By
                                .tagName("iframe"));
                messages.add("MMR" + i + " ad exist");
        } catch (Exception e) {
        	 messages.add("Rendering problem with MMR" + i
                     + " Ad at center content");
        }
				} catch (Exception e) {
					 messages.add("MMR" + i + " Ad is missing ");
				}
								
			}

		} catch (Exception E) {
			messages.add("Problem with MMR Ad on Redesign Standard Article Page " + E.toString());
		}
		
		//INA ad
		try {
			
					WebElement inaAdSlot = driver.findElement(
							By.className("article__content-body")).findElement(By.className("ina")).findElement(By.className("ina__slot"));
				
					if (!(inaAdSlot.isDisplayed())) {
						messages.add("INA Ad is Not displayed "
								+ message);
					}
					messages.add("INA Ad is displayed "
									);
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("INA Ad is not firing - 320 BP ");
					}else{
						messages.add("INA Ad is firing - 320 BP ");
						messages.add("INA Ad google query id is --> "+googleQueryId);
						messages.add("INA Ad google item id is --> "+gogleItemId);
						messages.add("INA Ad google creative id is --> "+googleCreativeId);
					}
				} catch (Exception e) {
					messages.add("INA Ad is Not displayed "
							+ driver.getCurrentUrl());
	}
		
		driver.close();
		} catch (Exception e) {
			messages.add("Exception in Redesign Standard Article Page - 320  BP test  " + e.toString());
		}
	
		// -------------------------------------Redesign News
		// Article-----------------------------------------------------
		messages.add("       ");

		messages.add("Redesign News Article");
		messages.add("===========");
		messages.add(" ");
		// Check advertisements in News article redesign
		try {
			driver = setDevice("");
			driver.manage().window().maximize();

			driver.get(baseURL
					+ "/health-news/you-probably-dont-need-extra-protein-in-your-diet-heres-why");
			messages.add("--Desktop BPs--");
			messages.add("Test Url : " + driver.getCurrentUrl());

			// Check Top DLB Ad & Stikiness for 3 seconds
			try {
				CheckAds TopDlb = new CheckAds(driver);
				message = TopDlb.checkTopDlb();
				if (message != null) {
					// System.out.println("Top LB Ad is missing...");
					messages.add("Top DLB Ad is missing in News Article..."
							+ driver.getCurrentUrl());
				} else {
					// Check label ADVERTISEMENT
					WebElement adLb1 = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector("#dlb1__container .dlb__text")));
					// Scroll page 155px
					messages.add("Top DLB Ad exist");
					
					try{
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
						if(googleQueryId.isEmpty()){
							messages.add("Top DLB Ad is not fired..."
									+ driver.getCurrentUrl());
						}else{
							messages.add("Top DLB Ad google query id is --> "+googleQueryId);
							messages.add("Top DLB Ad google item id is --> "+gogleItemId);
							messages.add("Top DLB Ad google creative id is --> "+googleCreativeId);
						}
					}catch (Exception e) {
						messages.add("Top DLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}
					
					
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,155)");

					// Check stickiness - 1st check that DLB is visible before
					// 3sec of scrolling 150px
					Thread.sleep(2900);
					WebElement stickyLayer = driver.findElement(By
							.className("dlb__sticky-placeholder"));
					try {
						String styl = stickyLayer.getAttribute("style");
						// System.out.println(styl.indexOf("display: none;")+" Style attribute: "+styl);
						if (styl.indexOf("display: none;") >= 0) {
							messages.add("Problem with Top dlb Ad stickiness after scrolling 150px"
									+ driver.getCurrentUrl());
						} else {
							// Check stickiness - if DLB is visible for 3seconds
							// than after 1 second check that it disappeared
							Thread.sleep(2000);
							stickyLayer = driver.findElement(By
									.className("dlb__sticky-placeholder"));
							styl = stickyLayer.getAttribute("style");
							if (styl.indexOf("display: none;") < 0) {
								messages.add("Top DLB Ad not disappeared after 3 seconds of scrolling 150px"
										+ driver.getCurrentUrl());
							}
						}

					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				messages.add("Problem with Top DLB Ad... on News Article Page"
						+ e.toString());
			}
			Thread.sleep(2000);
			((JavascriptExecutor) driver)
					.executeScript("window.scrollTo(0,document.body.scrollHeight);");
			Thread.sleep(2000);

			try {
				List<WebElement> tag = driver.findElements(By
						.cssSelector(".article__content-body p"));
				Thread.sleep(3000);
				// set counter to check inline dlb after every 2nd section for
				// sections <=6 and after every 3rd section if sections > 6
				int adcounter = 2;
				int j = 0;
				if (tag.size() > 5) {
					j = 1;
					if (tag.size() > 15) {
						j = 2;
						if (tag.size() > 20) {
							j = 3;
						}
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

						} else {
							if (i == 3) {
								adcount = 20;
							}
						}
					}
					String xpath = "//div[@class='article__content-body']/p["
							+ adcount
							+ "]/following-sibling::div[@class='dlb--inline-container']";

					Thread.sleep(3000);
					try {
						WebElement topAd = driver.findElement(By.xpath(xpath));
						WebElement advt = topAd.findElement(By
								.className("dlb__slot"));
						if (advt.getAttribute("id").equalsIgnoreCase(
								"dlb" + (i + 1) + "__slot__inline")) {

							messages.add("Inline dlb Ad exist after Para "
									+ (adcount));
							String googleQueryId = advt.getAttribute("data-google-query-id");
							String gogleItemId = advt.getAttribute("data-line-item-id");
							String googleCreativeId = advt.getAttribute("data-creative-id");
							
							if(googleQueryId.isEmpty()){
								messages.add("Inline DLB is not fired..." + i + " on Tabbed Article" + driver.getCurrentUrl());
							}else{
								messages.add("Inline DLB Ad google query id is --> "+googleQueryId);
								messages.add("Inline DLB Ad google item id is --> "+gogleItemId);
								messages.add("Inline DLB Ad google creative id is --> "+googleCreativeId);
							}	
							
						}
					} catch (Exception e) {
						messages.add("Inline DLB Ad is missing after Para "
								+ (i + 1) + e.toString());
					}
					adcounter++;

					i = i + 1;
				}

			} catch (Exception E) {
				messages.add("Problem with Inline DLB Ad on News Article Page"
						+ E.toString());
			}
			// Check DMR
			// Calculated number of expected DMR using (height of article
			// section/563)
			try {
				int wdth;
				WebElement articleContent = driver.findElement(By
						.className("standard-article"));
				WebElement topstories = driver.findElement(By
						.className("top-stories"));
				WebElement byline = driver.findElement(By.className("byline"));

				wdth = (articleContent.getSize().getHeight()
						- topstories.getSize().getHeight() - byline.getSize()
						.getHeight()) / 563;

				// wdth is number of DMR that should fire
				List<WebElement> dmrs = driver.findElement(
						By.className("body__col-fixed-right")).findElements(
						By.className("dmr--show"));

				for (int i = 1; i <= dmrs.size(); i++) {
					try {
						WebElement dmr = driver.findElement(By.id("dmr" + i
								+ "__slot"));
						try {
							WebElement ifrm = dmr.findElement(By
									.tagName("iframe"));
							messages.add("DMR" + i + " ad exist");
							
							String googleQueryId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute(
							"data-google-query-id");
					String gogleItemId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-line-item-id");
					String googleCreativeId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-creative-id");
					if (googleQueryId.isEmpty()) {
						messages.add("DMR"
								+ i
								+ " Ad at right rail on News Article is not fired.."
								+ driver.getCurrentUrl());
					} else {
						messages.add("DMR Ad google query id is --> "+googleQueryId);
						messages.add("DMR Ad google item id is --> "+gogleItemId);
						messages.add("DMR Ad google creative id is --> "+googleCreativeId);
					}
						} catch (Exception e) {
							messages.add("Rendering problem with DMR" + i
									+ " Ad at right rail on News Article");
						}
					} catch (Exception e) {
						messages.add("DMR" + i
								+ " Ad is missing on News Article");
					}
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}

//Read Next DMRs
			
			try {
				
				WebElement readnxtContent = driver.findElement(By
						.className("read-next"));
				List<WebElement> dmrs = readnxtContent.findElements(By.className("content-promotion-story"));
				int j = 11 ;
				int arrLength = dmrs.size();
				for ( int i = 1 ; i <= arrLength-1;) {
					WebElement dmr = driver.findElement(By.id("dmr" + j
								+ "__slot-contentpromo"));
						try {
							//WebElement ifrm = dmr.findElement(By.tagName("iframe"));
							messages.add("DMR" + j + " ad exist");
							String googleQueryId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute(
									"data-google-query-id");
							String gogleItemId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-line-item-id");
							String googleCreativeId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-creative-id");
							if (googleQueryId.isEmpty()) {
								messages.add("DMR"
										+ j
										+ " Ad at Right rail in Read Next on Redesign News Article is not fired.."
										+ driver.getCurrentUrl());
							} else {
								messages.add("DMR Ad google query id is --> "+googleQueryId);
								messages.add("DMR Ad google item id is --> "+gogleItemId);
								messages.add("DMR Ad google creative id is --> "+googleCreativeId);
							}
							Thread.sleep(10000);
						} catch (Exception e) {
							messages.add("Rendering problem with DMR"
									+ j
									+ " Ad at right rail on Redesign News Article");
						}
						i=i+2;
						j++;
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}


			// INA ad
			try {

				WebElement inaAdSlot = driver
						.findElement(By.className("article__content-body"))
						.findElement(By.className("ina"))
						.findElement(By.className("ina__slot"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", inaAdSlot);
				jse.executeScript("window.scrollBy(0,-150)", "");
				if (!(inaAdSlot.isDisplayed())) {
					messages.add("INA Ad is Not displayed "
							+ message);
				}
				messages.add("INA Ad is displayed "
								);
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("INA Ad is not firing - Desktop breakpoint ");
				}else{
					messages.add("INA Ad is firing - Desktop breakpoint ");
					messages.add("INA Ad google query id is --> "+googleQueryId);
					messages.add("INA Ad google item id is --> "+gogleItemId);
					messages.add("INA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());

			}
			// RNA ad
			try {
				WebElement rnaAdSlot = driver
						.findElement(By.className("top-stories"))
						.findElement(By.className("rna"))
						.findElement(By.className("rna__slot"));

				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("RNA Ad is not firing - Desktop breakpoint ");
				}else{
					messages.add("RNA Ad is firing - Desktop breakpoint ");
					messages.add("RNA Ad google query id is --> "+googleQueryId);
					messages.add("RNA Ad google item id is --> "+gogleItemId);
					messages.add("RNA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("RNA Ad is Not displayed "
						+ driver.getCurrentUrl());

			}

			// Footer advertisement
		try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}

			driver.close();
		} catch (Exception e) {
			messages.add("Problem in Ads Widget on News Article..."
					+ e.toString());
		}
		
		try{
			driver = setDeviceOrientation("iPad Mini",1024,768);
			driver.get(baseURL + "/health-news/you-probably-dont-need-extra-protein-in-your-diet-heres-why");
			Thread.sleep(3000);
			messages.add(" ");
			messages.add("--1024 BPs--");
			messages.add("Test URL : " + driver.getCurrentUrl());
		try {
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkTopDlb();
			if (message != null) {
				messages.add("DLB Ad is missing "
						+ message);
			} else {
				// Check label ADVERTISEMENT
				WebElement adLb1 = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector("#dlb1__container .dlb__text")));
				// Scroll page 155px
				messages.add("Top DLB Ad exist");
				try{
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("Top DLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}else{
						messages.add("Top DLB Ad google query id is --> "+googleQueryId);
						messages.add("Top DLB Ad google item id is --> "+gogleItemId);
						messages.add("Top DLB Ad google creative id is --> "+googleCreativeId);
					}
				}catch (Exception e) {
					messages.add("Top DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				}
				
				((JavascriptExecutor) driver)
						.executeScript("window.scrollBy(0,155)");

				// Check stickiness - 1st check that DLB is visible before
				// check 3sec of scrolling 150px
				Thread.sleep(2900);
				WebElement stickyLayer = driver.findElement(By
						.className("dlb__sticky-placeholder"));
				try {
					String styl = stickyLayer.getAttribute("style");
					try{
						stickyLayer = driver.findElement(By
								.className("dlb__sticky-placeholder"));
						messages.add("Top DLB Ad disappeared after 3 seconds of scrolling 150px");
						}catch (Exception e) {
							messages.add("Top DLB Ad not disappeared after 3 seconds of scrolling 150px"
									+ driver.getCurrentUrl());
						}

				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			messages.add("DLB Ad is missing "
					+ driver.getCurrentUrl());
		}
		// check DMR ad
		
		try {
			int wdth;
			WebElement articleContent = driver.findElement(By
					.className("standard-article"));
			WebElement topstories = driver.findElement(By
					.className("top-stories"));
			WebElement byline = driver.findElement(By
					.className("byline"));
			
			wdth = (articleContent.getSize().getHeight() - topstories
					.getSize().getHeight()-byline.getSize().getHeight()) / 563;
			((JavascriptExecutor) driver)
			.executeScript("window.scrollTo(0,document.body.scrollHeight);");

			// wdth is number of DMR that should fire
			List<WebElement> dmrs = driver.findElement(
					By.className("body__col-fixed-right")).findElements(     
					By.className("dmr--show"));

			for (int i = 1; i <= dmrs.size(); i++) {
				try {
					WebElement dmr = driver.findElement(By.id("dmr" + i
							+ "__slot"));
					try {
						WebElement ifrm = dmr.findElement(By
								.tagName("iframe"));
						messages.add("DMR" + i + " ad exist");
						String googleQueryId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute(
						"data-google-query-id");
				String gogleItemId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-line-item-id");
				String googleCreativeId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-creative-id");
				if (googleQueryId.isEmpty()) {
					messages.add("DMR"
							+ i
							+ " Ad at right rail on Replatform News Article is not fired.."
							+ driver.getCurrentUrl());
				} else {
					messages.add("DMR Ad google query id is --> "+googleQueryId);
					messages.add("DMR Ad google item id is --> "+gogleItemId);
					messages.add("DMR Ad google creative id is --> "+googleCreativeId);
				}
					} catch (Exception e) {
						messages.add("Rendering problem with DMR" + i
								+ " Ad at right rail ");
					}
				} catch (Exception e) {
					messages.add("DMR" + i + " Ad is missing on News Article Page ");
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}		
		//Read Next DMRs
		
		try {
			
			WebElement readnxtContent = driver.findElement(By
					.className("read-next"));
			List<WebElement> dmrs = readnxtContent.findElements(By.className("content-promotion-story"));
			int j = 11 ;
			int arrLength = dmrs.size();
			for ( int i = 1 ; i <= arrLength-1;) {
				WebElement dmr = driver.findElement(By.id("dmr" + j
							+ "__slot-contentpromo"));
					try {
						//WebElement ifrm = dmr.findElement(By.tagName("iframe"));
						messages.add("DMR" + j + " ad exist");
						String googleQueryId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute(
								"data-google-query-id");
						String gogleItemId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-line-item-id");
						String googleCreativeId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-creative-id");
						if (googleQueryId.isEmpty()) {
							messages.add("DMR"
									+ j
									+ " Ad at Right rail in Read Next on Redesign News Article is not fired.."
									+ driver.getCurrentUrl());
						} else {
							messages.add("DMR Ad google query id is --> "+googleQueryId);
							messages.add("DMR Ad google item id is --> "+gogleItemId);
							messages.add("DMR Ad google creative id is --> "+googleCreativeId);
						}
						Thread.sleep(10000);
					} catch (Exception e) {
						messages.add("Rendering problem with DMR"
								+ j
								+ " Ad at right rail on Redesign News Article");
					}
					i=i+2;
					j++;
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}



		//INA ad
		try {
			
					WebElement inaAdSlot = driver.findElement(
							By.className("article__content-body")).findElement(By.className("ina")).findElement(By.className("ina__slot"));
					JavascriptExecutor jse = (JavascriptExecutor) driver;
					((JavascriptExecutor) driver).executeScript(
							"arguments[0].scrollIntoView(true);", inaAdSlot);
					jse.executeScript("window.scrollBy(0,-150)", "");
					if (!(inaAdSlot.isDisplayed())) {
						messages.add("INA Ad is Not displayed "
								+ message);
						String googleQueryId=new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
								String gogleItemId=new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
								String googleCreativeId=new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
								if(googleQueryId.isEmpty()){
									messages.add("INA Ad is not firing - Desktop breakpoint ");
								}else{
									messages.add("INA Ad is firing - Desktop breakpoint ");
									messages.add("INA Ad google query id is --> "+googleQueryId);
									messages.add("INA Ad google item id is --> "+gogleItemId);
									messages.add("INA Ad google creative id is --> "+googleCreativeId);
								}					}
					
					
				} catch (Exception e) {
					messages.add("INA Ad is Not displayed "
							+ driver.getCurrentUrl());

			}
				//RNA ad
				try {
					WebElement rnaAdSlot = driver.findElement(By.className("top-stories")).findElement(By.className("rna")).findElement(By.className("rna__slot"));
					if (!(rnaAdSlot.isDisplayed())) {
						messages.add("RNA Ad is Not displayed  "
								+ message);
					}
					messages.add("RNA Ad is displayed  "
									);
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("RNA Ad is not firing - 1024 breakpoint ");
					}else{
						messages.add("RNA Ad is firing - Desktop breakpoint ");
						messages.add("RNA Ad google query id is --> "+googleQueryId);
						messages.add("RNA Ad google item id is --> "+gogleItemId);
						messages.add("RNA Ad google creative id is --> "+googleCreativeId);
					}
				} catch (Exception e) {
					messages.add("RNA Ad is Not displayed "
							+ driver.getCurrentUrl());

					}
		// Footer advertisement
		try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
		messages.add("       ");
		driver.close();
		} catch (Exception e) {
			messages.add("Exception in News Article Page - 1024 BP test  " + e.toString());
		}
		
	// 768 BP
		try{
            driver = setDeviceOrientation("iPad Mini",768,1024);
            messages.add(" ");
            messages.add("--768 BPs--");
            driver.get(baseURL +
"/health-news/you-probably-dont-need-extra-protein-in-your-diet-heres-why");
            Thread.sleep(3000);
    try {
            CheckAds Lbads = new CheckAds(driver);
            message = Lbads.checkTopDlb();
            if (message != null) {
                    messages.add("DLB Ad is missing "
                                    + message);
            } else {
            	// Check label ADVERTISEMENT
				WebElement adLb1 = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector("#dlb1__container .dlb__text")));
				// Scroll page 155px
				messages.add("Top DLB Ad exist");
				try{
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("Top DLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}else{
						messages.add("Top DLB Ad google query id is --> "+googleQueryId);
						messages.add("Top DLB Ad google item id is --> "+gogleItemId);
						messages.add("Top DLB Ad google creative id is --> "+googleCreativeId);
					}
				}catch (Exception e) {
					messages.add("Top dlb Ad is not fired..."
							+ driver.getCurrentUrl());
				}
				
				((JavascriptExecutor) driver)
						.executeScript("window.scrollBy(0,155)");

				// Check stickiness - 1st check that DLB is visible before
				// check 3sec of scrolling 150px
				Thread.sleep(2900);
				WebElement stickyLayer = driver.findElement(By
						.className("dlb__sticky-placeholder"));
				try {
					String styl = stickyLayer.getAttribute("style");
					try{
						stickyLayer = driver.findElement(By
								.className("dlb__sticky-placeholder"));
						messages.add("Top DLB Ad disappeared after 3 seconds of scrolling 150px");
						}catch (Exception e) {
							messages.add("Top DLB Ad not disappeared after 3 seconds of scrolling 150px"
									+ driver.getCurrentUrl());
						}

				} catch (Exception e) {

				}            }
    } catch (Exception e) {
            messages.add("DLB Ad is missing "
                            + driver.getCurrentUrl());
    }
    // check DMR ad
    try {
            List<WebElement> dmrs = driver.findElement(
                            By.className("article__content-body")).findElements(     
                            By.className("dmr--tablet"));

            for (int i = 1; i <= dmrs.size(); i++) {
                    try {
                            WebElement dmr = driver.findElement(By.id("dmr" + i
                                            + "__slot-tablet"));
                            try {
                                    WebElement ifrm = dmr.findElement(By
                                                    .tagName("iframe"));
                                    messages.add("DMR" + i + " ad exist");
                                    try{
                						String googleQueryId=new WebDriverWait(driver, 25)
                						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dmr__slot"))).getAttribute("data-google-query-id");
                						String gogleItemId=new WebDriverWait(driver, 25)
                						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dmr__slot"))).getAttribute("data-line-item-id");
                						String googleCreativeId=new WebDriverWait(driver, 25)
                						.until(ExpectedConditions.visibilityOfElementLocated(By.className("dmr__slot"))).getAttribute("data-creative-id");
                						if(googleQueryId.isEmpty()){
                							messages.add("Top DMR Ad is not fired..."
                									+ driver.getCurrentUrl());
                						}else{
                							messages.add("Top DMR Ad google query id is --> "+googleQueryId);
                							messages.add("Top DMR Ad google item id is --> "+gogleItemId);
                							messages.add("Top DMR Ad google creative id is --> "+googleCreativeId);
                						}
                					}catch (Exception e) {
                						messages.add("Top DMR Ad is not fired..."
                								+ driver.getCurrentUrl());
                					}
                            } catch (Exception e) {
                                    messages.add("Rendering problem with DMR" + i
                                                    + " Ad at center content");
                            }
                    } catch (Exception e) {
                            messages.add("DMR" + i + " Ad is missing ");
                    }
            }
    } catch (Exception e) {
            messages.add("DMR Ad is missing on News Article Page - 768 breakpoint "
                            + driver.getCurrentUrl());
    }
    //INA ad
    try {
            
                            WebElement inaAdSlot = driver.findElement(
                                            By.className("article__content-body")).findElement(By.className("ina")).findElement(By.className("ina__slot"));
                    
                            if (!(inaAdSlot.isDisplayed())) {
                                    messages.add("INA Ad is Not displayed "
                                                    + message);
                            }
                            messages.add("INA Ad is displayed  "
                                                            );
                            String googleQueryId=new WebDriverWait(driver, 25)
        					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
        					String gogleItemId=new WebDriverWait(driver, 25)
        					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
        					String googleCreativeId=new WebDriverWait(driver, 25)
        					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
        					if(googleQueryId.isEmpty()){
        						messages.add("INA Ad is not firing - 768 breakpoint ");
        					}else{
        						messages.add("INA Ad is firing - 768 breakpoint ");
        						messages.add("INA Ad google query id is --> "+googleQueryId);
        						messages.add("INA Ad google item id is --> "+gogleItemId);
        						messages.add("INA Ad google creative id is --> "+googleCreativeId);
        					}
                    } catch (Exception e) {
                            messages.add("INA Ad is Not displayed "
                                            + driver.getCurrentUrl());
}
    
    // Footer advertisement
  try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
    driver.close();
    } catch (Exception e) {
            messages.add("Exception in News Article Page - 768 BP test " + e.toString());
    }
		
		
		try{
            driver = setDevice("iPhone 5");
            messages.add(" ");
            messages.add("--320 BPs--");
            driver.get(baseURL +
"/health-news/you-probably-dont-need-extra-protein-in-your-diet-heres-why");
            driver.navigate().refresh();
            Thread.sleep(4000);
    try {
            CheckAds Lbads = new CheckAds(driver);
            message = Lbads.checkRedesignStickyLB();
            if (message != null) {
                    messages.add("MLB Ad is missing "
                                    + message);
            } else {
                    messages.add("MLB Ad is present " );
                    try{
    					String googleQueryId=new WebDriverWait(driver, 25)
    					.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-google-query-id");
    					String gogleItemId=new WebDriverWait(driver, 25)
    					.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-line-item-id");
    					String googleCreativeId=new WebDriverWait(driver, 25)
    					.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-creative-id");
    					if(googleQueryId.isEmpty()){
    						messages.add("MLB Ad is not fired..."
    								+ driver.getCurrentUrl());
    					}else{
    						messages.add("MLB Ad google query id is --> "+googleQueryId);
    						messages.add("MLB Ad google item id is --> "+gogleItemId);
    						messages.add("MLB Ad google creative id is --> "+googleCreativeId);
    					}
    				}catch (Exception e) {
    					messages.add("MLB Ad is not fired..."
    							+ driver.getCurrentUrl());
    				}
            }
    } catch (Exception e) {
            messages.add("MLB Ad is missing "
                            + driver.getCurrentUrl());
    }
    // check MR1 ad
    try {
            List<WebElement> mmrs = driver.findElement(
                            By.className("article__content-body")).findElements(     
                            By.className("mmr"));

            for (int i = 1; i <= mmrs.size(); i++) {
                    try {
                            WebElement mmr = driver.findElement(By.id("mmr" + i
                                            + "__slot"));
                           
                                    //WebElement ifrm = mmr.findElement(By.tagName("iframe"));
                                    messages.add("MMR" + i + " ad exist" + (i+1));
                                    String googleQueryId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-google-query-id");
                    				String gogleItemId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-line-item-id");
                    				String googleCreativeId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-creative-id");
                    				if (googleQueryId.isEmpty()) {
                    					messages.add("MMR"
                    							+ i
                    							+ " Ad at right rail on Replatform Tabbed Article is not fired.."
                    							+ driver.getCurrentUrl());
                    				} else {
                    					messages.add("MMR Ad google query id is --> "+googleQueryId);
                    					messages.add("MMR Ad google item id is --> "+gogleItemId);
                    					messages.add("MMR Ad google creative id is --> "+googleCreativeId);
                    				}
                           
                    } catch (Exception e) {
                            messages.add("MMR" + i + " Ad is missing ");
                    }
            }
    } catch (Exception E) {
            messages.add("Problem with MMR Ad on News Article Page " + E.toString());
    }
    
    //INA ad
    try {
                            WebElement inaAdSlot = driver.findElement(
                                            By.className("article__content-body")).findElement(By.className("ina")).findElement(By.className("ina__slot"));
                    
                            if (!(inaAdSlot.isDisplayed())) {
                                    messages.add("INA Ad is Not displayed "
                                                    + message);
                            }
                            messages.add("INA Ad is displayed "
                                                            );
                            String googleQueryId=new WebDriverWait(driver, 25)
        					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
        					String gogleItemId=new WebDriverWait(driver, 25)
        					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
        					String googleCreativeId=new WebDriverWait(driver, 25)
        					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
        					if(googleQueryId.isEmpty()){
        						messages.add("INA Ad is not firing - 320 BP ");
        					}else{
        						messages.add("INA Ad is firing - 320 BP ");
        						messages.add("INA Ad google query id is --> "+googleQueryId);
        						messages.add("INA Ad google item id is --> "+gogleItemId);
        						messages.add("INA Ad google creative id is --> "+googleCreativeId);
        					}
                    } catch (Exception e) {
                            messages.add("INA Ad is Not displayed "
                                            + driver.getCurrentUrl());
}
		
		driver.close();
		} catch (Exception e) {
			messages.add("Exception in News Article Page - 320  BP test  " + e.toString());
		}
	

		// --------------------------------Nutrition
		// Article----------------------------------------------
		messages.add("       ");

		messages.add("Redesign Nutrition Article");
		messages.add("===========");
		messages.add(" ");
		try {
			driver = setDevice("");

			driver.manage().window().maximize();

			driver.get(baseURL + "/nutrition/happiness-and-health");
			messages.add("--Desktop BPs--");
			messages.add("Test Url : " + driver.getCurrentUrl());

			// Check Top DLB Ad & Stikiness for 3 seconds
			try {
				CheckAds TopDlb = new CheckAds(driver);
				message = TopDlb.checkTopDlb();
				if (message != null) {
					// System.out.println("Top LB Ad is missing...");
					messages.add("Top DLB Ad is missing..."
							+ message);
				} else {
					// Check label ADVERTISEMENT
					WebElement adLb1 = new WebDriverWait(driver, 15)
							.until(ExpectedConditions.visibilityOfElementLocated(By
									.cssSelector("#dlb1__container .dlb__text")));
					// Scroll page 155px
					messages.add("Top DLB Ad exist");
					try{
						//	String adLbid = driver.findElement(By.id("dlb1__slot")).getAttribute("data-google-query-id");
							String googleQueryId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
							String gogleItemId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
							String googleCreativeId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
							if(googleQueryId.isEmpty()){
								messages.add("Top DLB Ad is not fired..."
										+ driver.getCurrentUrl());
							}else{
								messages.add("Top DLB Ad google query id is --> "+googleQueryId);
								messages.add("Top DLB Ad google item id is --> "+gogleItemId);
								messages.add("Top DLB Ad google creative id is --> "+googleCreativeId);
							}
						}catch (Exception e) {
							messages.add("Top DLB Ad is not fired..."
									+ driver.getCurrentUrl());
						}
					((JavascriptExecutor) driver)
							.executeScript("window.scrollBy(0,155)");

					// Check stickiness - 1st check that DLB is visible before
					// 3sec of scrolling 150px
					Thread.sleep(2900);
					WebElement stickyLayer = driver.findElement(By
							.className("dlb__sticky-placeholder"));
					try {
						String styl = stickyLayer.getAttribute("style");
						// System.out.println(styl.indexOf("display: none;")+" Style attribute: "+styl);
						if (styl.indexOf("display: none;") >= 0) {
							messages.add("Problem with Top DLB Ad stickiness after scrolling 150px"
									+ driver.getCurrentUrl());
						} else {
							// Check stickiness - if DLB is visible for 3seconds
							// than after 1 second check that it disappeared
							Thread.sleep(1000);
							stickyLayer = driver.findElement(By
									.className("dlb__sticky-placeholder"));
							styl = stickyLayer.getAttribute("style");
							if (styl.indexOf("display: none;") < 0) {
								messages.add("Top DLB Ad not disappeared after 3 seconds of scrolling 150px"
										+ driver.getCurrentUrl());
							}
						}

					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				messages.add("Problem with Top DLB Ad... on Nutrtion Article"
						+ e.toString());
			}
			Thread.sleep(2000);
			((JavascriptExecutor) driver)
					.executeScript("window.scrollTo(0,document.body.scrollHeight);");
			Thread.sleep(2000);

			try {
				List<WebElement> tag = driver.findElement(
						By.className("article__content-body")).findElements(
						By.className("content_body"));

				// set counter to check inline dlb after every 2nd section for
				// sections <=6 and after every 3rd section if sections > 6
				int adcounter = 2;
				int j = 1;
				if (tag.size() > 6) {
					j = 2;
				}

				for (int i = j; i < tag.size() - 1;) {
					String txt = tag.get(i).findElement(By.tagName("p"))
							.getText();
					// String
					String xpath = "//div[@id='TOC_TITLE_"
							+ i
							+ "']/following-sibling::div[@class='dlb--inline-container']";

					try {
						WebElement topAd = driver.findElement(By.xpath(xpath));
						WebElement advt = topAd.findElement(By
								.className("dlb__slot"));
					
						if (advt.getAttribute("id").equalsIgnoreCase(
								"dlb" + adcounter + "__slot__inline")) {
						
							messages.add("Inline dlb Ad exist after section "
									+ (i + 1));
							String googleQueryId = advt.getAttribute("data-google-query-id");
							String gogleItemId = advt.getAttribute("data-line-item-id");
							String googleCreativeId = advt.getAttribute("data-creative-id");
							if(googleQueryId.isEmpty()){
								messages.add("Inline DLB is not fired..." + i + " on Standard Article" + driver.getCurrentUrl());
							}else{
							
								messages.add("Inline DLB Ad is firing - Desktop breakpoint ");
								messages.add("Inline DLB Ad google query id is --> "+googleQueryId);
								messages.add("Inline DLB Ad google item id is --> "+gogleItemId);
								messages.add("Inline DLB Ad google creative id is --> "+googleCreativeId);
							}
						}
					} catch (Exception e) {
						messages.add("Inline DLB Ad is missing after section "
								+ (i + 1) + e.toString());
					}
					adcounter++;
					if (tag.size() > 6) {
						i = i + 3;
					} else {
						i = i + 2;
					}
				}

			} catch (Exception E) {
				messages.add("Problem with inline dlb Ad on Nutrition Article "
						+ E.toString());
			}

			// Check DMR
			// Calculated number of expected DMR using (height of article
			// section/563)
			try {
				int wdth;
				WebElement articleContent = driver.findElement(By
						.className("an-article"));
				WebElement topstories = driver.findElement(By
						.className("top-stories"));
				WebElement byline = driver.findElement(By.className("byline"));

				wdth = (articleContent.getSize().getHeight()
						- topstories.getSize().getHeight() - byline.getSize()
						.getHeight()) / 563;

				// wdth is number of DMR that should fire
				List<WebElement> dmrs = driver.findElement(
						By.className("body__col-fixed-right")).findElements(
						By.className("dmr--show"));

				for (int i = 1; i <= dmrs.size(); i++) {
					
						WebElement dmr = driver.findElement(By.id("dmr" + i
								+ "__slot"));
						try {
							messages.add("DMR" + i + " ad exist");
							String googleQueryId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute(
							"data-google-query-id");
					String gogleItemId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-line-item-id");
					String googleCreativeId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-creative-id");
					if (googleQueryId.isEmpty()) {
						messages.add("DMR"
								+ i
								+ " Ad at right rail on Nutrition Article is not fired.."
								+ driver.getCurrentUrl());
					} else {
						messages.add("DMR Ad google query id is --> "+googleQueryId);
						messages.add("DMR Ad google item id is --> "+gogleItemId);
						messages.add("DMR Ad google creative id is --> "+googleCreativeId);
					}
						
					} catch (Exception e) {
						messages.add("DMR" + i
								+ "Ad is missing on Nutrition Article");
					}
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}
//Read Next DMRs
			
			try {
				
				WebElement readnxtContent = driver.findElement(By
						.className("read-next"));
				List<WebElement> dmrs = readnxtContent.findElements(By.className("content-promotion-story"));
				int j = 11 ;
				int arrLength = dmrs.size();
				for ( int i = 1 ; i <= arrLength-1;) {
					WebElement dmr = driver.findElement(By.id("dmr" + j
								+ "__slot-contentpromo"));
						try {
							//WebElement ifrm = dmr.findElement(By.tagName("iframe"));
							messages.add("DMR" + j + " ad exist");
							String googleQueryId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute(
									"data-google-query-id");
							String gogleItemId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-line-item-id");
							String googleCreativeId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-creative-id");
							if (googleQueryId.isEmpty()) {
								messages.add("DMR"
										+ j
										+ " Ad at Right rail in Read Next on Redesign Nutrition Article is not fired.."
										+ driver.getCurrentUrl());
							} else {
								messages.add("DMR Ad google query id is --> "+googleQueryId);
								messages.add("DMR Ad google item id is --> "+gogleItemId);
								messages.add("DMR Ad google creative id is --> "+googleCreativeId);
							}
							Thread.sleep(10000);
						} catch (Exception e) {
							messages.add("Rendering problem with DMR"
									+ j
									+ " Ad at right rail on Redesign Nutrition Article");
						}
						i=i+2;
						j++;
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}


			// INA ad
			try {

				WebElement inaAdSlot = driver
						.findElement(By.className("article__content-body"))
						.findElement(By.className("ina"))
						.findElement(By.className("ina__slot"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", inaAdSlot);
				jse.executeScript("window.scrollBy(0,-150)", "");
				if (!(inaAdSlot.isDisplayed())) {
					messages.add("INA Ad is Not displayed "
							+ message);
				}
				messages.add("INA Ad is displayed "
								);
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("INA Ad is not firing - Desktop breakpoint ");
				}else{
					messages.add("INA Ad is firing - Desktop breakpoint ");
					messages.add("INA Ad google query id is --> "+googleQueryId);
					messages.add("INA Ad google item id is --> "+gogleItemId);
					messages.add("INA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());

			}
			// RNA ad
			try {
				WebElement rnaAdSlot = driver
						.findElement(By.className("top-stories"))
						.findElement(By.className("rna"))
						.findElement(By.className("rna__slot"));
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("RNA Ad is not firing - Desktop breakpoint ");
				}else{
					messages.add("RNA Ad is firing - Desktop breakpoint ");
					messages.add("RNA Ad google query id is --> "+googleQueryId);
					messages.add("RNA Ad google item id is --> "+gogleItemId);
					messages.add("RNA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("RNA Ad is Not displayed "
						+ driver.getCurrentUrl());
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
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ adfooetrLbid);
				}
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}

			driver.close();
		} catch (Exception e) {
			messages.add("Problem in Ads Widget on Nutrition Article..."
					+ e.toString());
		}
		
		// 1024 BP
		try{
		driver = setDeviceOrientation("iPad Mini", 1024, 768);
		driver.get(baseURL + "/nutrition/happiness-and-health");
		Thread.sleep(3000);
		messages.add(" ");
		messages.add("--1024 BPs--");
		messages.add("Test URL : " + driver.getCurrentUrl());
	try {
		CheckAds Lbads = new CheckAds(driver);
		message = Lbads.checkTopDlb();
		if (message != null) {
			messages.add("DLB Ad is missing "
					+ message);
		} else {
			// Check label ADVERTISEMENT
			WebElement adLb1 = new WebDriverWait(driver, 15)
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.cssSelector("#dlb1__container .dlb__text")));
			// Scroll page 155px
			messages.add("Top DLB Ad exist");
			try{
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("Top DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				}else{
					messages.add("Top DLB Ad google query id is --> "+googleQueryId);
					messages.add("Top DLB Ad google item id is --> "+gogleItemId);
					messages.add("Top DLB Ad google creative id is --> "+googleCreativeId);
				}
			}catch (Exception e) {
				messages.add("Top DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
			
			((JavascriptExecutor) driver)
					.executeScript("window.scrollBy(0,155)");

			// Check stickiness - 1st check that DLB is visible before
			// check 3sec of scrolling 150px
			Thread.sleep(2900);
			WebElement stickyLayer = driver.findElement(By
					.className("dlb__sticky-placeholder"));
			try {
				String styl = stickyLayer.getAttribute("style");
				try{
					stickyLayer = driver.findElement(By
							.className("dlb__sticky-placeholder"));
					messages.add("Top DLB Ad disappeared after 3 seconds of scrolling 150px");
					}catch (Exception e) {
						messages.add("Top dDLBlb Ad not disappeared after 3 seconds of scrolling 150px"
								+ driver.getCurrentUrl());
					}

			} catch (Exception e) {

			}
		}
	} catch (Exception e) {
		messages.add("DLB Ad is missing "
				+ driver.getCurrentUrl());
	}
	// check DMR ad
	
	try {
		int wdth;
		WebElement articleContent = driver.findElement(By
				.className("an-article"));
		WebElement topstories = driver.findElement(By
				.className("top-stories"));
		WebElement byline = driver.findElement(By
				.className("byline"));
		
		wdth = (articleContent.getSize().getHeight() - topstories
				.getSize().getHeight()-byline.getSize().getHeight()) / 563;
		((JavascriptExecutor) driver)
		.executeScript("window.scrollTo(0,document.body.scrollHeight);");

		// wdth is number of DMR that should fire
		List<WebElement> dmrs = driver.findElement(
				By.className("body__col-fixed-right")).findElements(     
				By.className("dmr--show"));

		for (int i = 1; i <= dmrs.size(); i++) {
			try {
				WebElement dmr = driver.findElement(By.id("dmr" + i
						+ "__slot"));
				try {
					WebElement ifrm = dmr.findElement(By
							.tagName("iframe"));
					messages.add("DMR" + i + " ad exist");
					String googleQueryId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute(
					"data-google-query-id");
			String gogleItemId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-line-item-id");
			String googleCreativeId = driver.findElement(By.id("dmr" + i + "__slot")).getAttribute("data-creative-id");
			if (googleQueryId.isEmpty()) {
				messages.add("DMR"
						+ i
						+ " Ad at right rail on Nutrition Article is not fired.."
						+ driver.getCurrentUrl());
			} else {
				messages.add("DMR Ad google query id is --> "+googleQueryId);
				messages.add("DMR Ad google item id is --> "+gogleItemId);
				messages.add("DMR Ad google creative id is --> "+googleCreativeId);
			}
				} catch (Exception e) {
					messages.add("Rendering problem with DMR" + i
							+ " Ad at right rail ");
				}
			} catch (Exception e) {
				messages.add("DMR" + i + " Ad is missing on Nutrition Article Page ");
			}
		}

	} catch (Exception e) {
		System.out.println(e.toString());
	}		

	//Read Next DMRs
	
	try {
		
		WebElement readnxtContent = driver.findElement(By
				.className("read-next"));
		List<WebElement> dmrs = readnxtContent.findElements(By.className("content-promotion-story"));
		int j = 11 ;
		int arrLength = dmrs.size();
		for ( int i = 1 ; i <= arrLength-1;) {
			WebElement dmr = driver.findElement(By.id("dmr" + j
						+ "__slot-contentpromo"));
				try {
					//WebElement ifrm = dmr.findElement(By.tagName("iframe"));
					messages.add("DMR" + j + " ad exist");
					String googleQueryId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute(
							"data-google-query-id");
					String gogleItemId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-line-item-id");
					String googleCreativeId = driver.findElement(By.id("dmr" + j + "__slot-contentpromo")).getAttribute("data-creative-id");
					if (googleQueryId.isEmpty()) {
						messages.add("DMR"
								+ j
								+ " Ad at Right rail in Read Next on Redesign Nutrition Article is not fired.."
								+ driver.getCurrentUrl());
					} else {
						messages.add("DMR Ad google query id is --> "+googleQueryId);
						messages.add("DMR Ad google item id is --> "+gogleItemId);
						messages.add("DMR Ad google creative id is --> "+googleCreativeId);
					}
					Thread.sleep(10000);
				} catch (Exception e) {
					messages.add("Rendering problem with DMR"
							+ j
							+ " Ad at right rail on Redesign Nutrition Article");
				}
				i=i+2;
				j++;
		}

	} catch (Exception e) {
		System.out.println(e.toString());
	}


	//INA ad
	try {
		
				WebElement inaAdSlot = driver.findElement(
						By.className("article__content-body")).findElement(By.className("ina")).findElement(By.className("ina__slot"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollIntoView(true);", inaAdSlot);
				jse.executeScript("window.scrollBy(0,-150)", "");
				if (!(inaAdSlot.isDisplayed())) {
					messages.add("INA Ad is Not displayed "
							+ message);
					String googleQueryId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
							String gogleItemId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
							String googleCreativeId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
							if(googleQueryId.isEmpty()){
								messages.add("INA Ad is not firing - Desktop breakpoint ");
							}else{
								messages.add("INA Ad is firing - Desktop breakpoint ");
								messages.add("INA Ad google query id is --> "+googleQueryId);
								messages.add("INA Ad google item id is --> "+gogleItemId);
								messages.add("INA Ad google creative id is --> "+googleCreativeId);
							}					}
				
				
			} catch (Exception e) {
				messages.add("INA Ad is Not displayed "
						+ driver.getCurrentUrl());

		}
			//RNA ad
			try {
				WebElement rnaAdSlot = driver.findElement(By.className("top-stories")).findElement(By.className("rna")).findElement(By.className("rna__slot"));
				if (!(rnaAdSlot.isDisplayed())) {
					messages.add("RNA Ad is Not displayed  "
							+ message);
				}
				messages.add("RNA Ad is displayed  "
								);
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("rna__slot"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("RNA Ad is not firing - 1024 breakpoint ");
				}else{
					messages.add("RNA Ad is firing - Desktop breakpoint ");
					messages.add("RNA Ad google query id is --> "+googleQueryId);
					messages.add("RNA Ad google item id is --> "+gogleItemId);
					messages.add("RNA Ad google creative id is --> "+googleCreativeId);
				}
			} catch (Exception e) {
				messages.add("RNA Ad is Not displayed "
						+ driver.getCurrentUrl());

				}
	// Footer advertisement
	try {
			String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
			
					
			if (footerAd.isEmpty()) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			} else {
				messages.add("Footer DLB Ad is fired...Advertisement id is --> "
								+ footerAd);
			}
			
		} catch (Exception e) {
			messages.add("Footer DLB Ad is not fired..."
					+ driver.getCurrentUrl());
		}
	messages.add("       ");
	driver.close();
	} catch (Exception e) {
		messages.add("Exception in Nutrition Article Page - 1024 BP test  " + e.toString());
	}
	
	// 	768 BP
	try{
			driver = setDeviceOrientation("iPad Mini", 768, 1024);
			messages.add(" ");
			messages.add("--768 BPs--");
			driver.get(baseURL + "/nutrition/happiness-and-health");
			Thread.sleep(3000);
		try {
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkTopDlb();
			if (message != null) {
				messages.add("DLB Ad is missing  "
						+ message);
			} else {
				// Check label ADVERTISEMENT
				WebElement adLb1 = new WebDriverWait(driver, 15)
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.cssSelector("#dlb1__container .dlb__text")));
				// Scroll page 155px
				messages.add("Top DLB Ad exist");
				try{
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("dlb__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("Top DLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}else{
						messages.add("Top DLB Ad google query id is --> "+googleQueryId);
						messages.add("Top DLB Ad google item id is --> "+gogleItemId);
						messages.add("Top DLB Ad google creative id is --> "+googleCreativeId);
					}
				}catch (Exception e) {
					messages.add("Top DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				}
				
				((JavascriptExecutor) driver)
						.executeScript("window.scrollBy(0,155)");

				// Check stickiness - 1st check that DLB is visible before
				// check 3sec of scrolling 150px
				Thread.sleep(2900);
				WebElement stickyLayer = driver.findElement(By
						.className("dlb__sticky-placeholder"));
				try {
					String styl = stickyLayer.getAttribute("style");
					try{
						stickyLayer = driver.findElement(By
								.className("dlb__sticky-placeholder"));
						messages.add("Top DLB Ad disappeared after 3 seconds of scrolling 150px");
						}catch (Exception e) {
							messages.add("Top DLB Ad not disappeared after 3 seconds of scrolling 150px"
									+ driver.getCurrentUrl());
						}

				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			messages.add("DLB Ad is missing "
					+ driver.getCurrentUrl());
		}
		// check MR ad
		try {
			List<WebElement> tag = driver.findElement(
					By.className("article__content-body")).findElements(
					By.className("content_body"));
			
			// set counter to check inline dlb after every 2nd section for
			// sections <=6 and after every 3rd section if sections > 6
			int adcounter = 1;
			int j = 1;
			
		
			for (int i = j; i < tag.size()-1;) {
				String txt = tag.get(i).findElement(By.tagName("p"))
						.getText();
				// String	
				String xpath = "//div[@id='TOC_TITLE_"
						+ i
						+ "']/following-sibling::div[@class='dmr row dmr--tablet']";

				try {
					WebElement topAd = driver.findElement(By.xpath(xpath));
					WebElement advt = topAd.findElement(By
							.className("dmr__slot"));
					//System.out.println(advt.getAttribute("id"));
					if (advt.getAttribute("id").equalsIgnoreCase(
							"dmr" + adcounter + "__slot-tablet")) {
						//System.out.println(advt.getAttribute("id"));
						messages.add("DMR Ad exist after section "
								+ (i + 1));
						 try{
								String googleQueryId=new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By.className("dmr__slot"))).getAttribute("data-google-query-id");
								String gogleItemId=new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By.className("dmr__slot"))).getAttribute("data-line-item-id");
								String googleCreativeId=new WebDriverWait(driver, 25)
								.until(ExpectedConditions.visibilityOfElementLocated(By.className("dmr__slot"))).getAttribute("data-creative-id");
								if(googleQueryId.isEmpty()){
									messages.add("Top DMR Ad is not fired..."
											+ driver.getCurrentUrl());
								}else{
									messages.add("Top DMR Ad google query id is --> "+googleQueryId);
									messages.add("Top DMR Ad google item id is --> "+gogleItemId);
									messages.add("Top DMR Ad google creative id is --> "+googleCreativeId);
								}
							}catch (Exception e) {
								messages.add("Top DMR Ad is not fired..."
										+ driver.getCurrentUrl());
							}
					} 
				} catch (Exception e) {
					messages.add("DMR Ad is missing after section "
							+ (i + 1)+e.toString());
				}
				adcounter++;
				
					i = i + 2;
			}
		} catch (Exception e) {
			messages.add("DMR Ad is missing on Nutrition Article Page - 768 breakpoint "
					+ driver.getCurrentUrl());
		}
		//INA ad
		try {
			
					WebElement inaAdSlot = driver.findElement(
							By.className("article__content-body")).findElement(By.className("ina")).findElement(By.className("ina__slot"));
				
					if (!(inaAdSlot.isDisplayed())) {
						messages.add("INA Ad is Not displayed "
								+ message);
					}
					messages.add("INA Ad is displayed  "
									);
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("INA Ad is not firing - 768 breakpoint ");
					}else{
						messages.add("INA Ad is firing - 768 breakpoint ");
						messages.add("INA Ad google query id is --> "+googleQueryId);
						messages.add("INA Ad google item id is --> "+gogleItemId);
						messages.add("INA Ad google creative id is --> "+googleCreativeId);
					}
				} catch (Exception e) {
					messages.add("INA Ad is Not displayed "
							+ driver.getCurrentUrl());
	}
		
		// Footer advertisement
	try {
				String footerAd = driver.findElement(By.id("ad-pb-by-google")).getAttribute("data-ad-client");
				
						
				if (footerAd.isEmpty()) {
					messages.add("Footer DLB Ad is not fired..."
							+ driver.getCurrentUrl());
				} else {
					messages.add("Footer DLB Ad is fired...Advertisement id is --> "
									+ footerAd);
				}
				
			} catch (Exception e) {
				messages.add("Footer DLB Ad is not fired..."
						+ driver.getCurrentUrl());
			}
		driver.close();
		} catch (Exception e) {
			messages.add("Exception in Nutrition Article Page - 768 BP test  " + e.toString());
		}

		try{
			driver = setDevice("iPhone 5");
			messages.add(" ");
			messages.add("--320 BPs--");
			driver.get(baseURL + "/nutrition/happiness-and-health");
			driver.navigate().refresh();
			Thread.sleep(4000);
		try {
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkRedesignStickyLB();
			if (message != null) {
				messages.add("MLB Ad is missing "
						+ message);
			} else {
				messages.add("MLB Ad is present ");
				try{
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("mmr__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("MLB Ad is not fired..."
								+ driver.getCurrentUrl());
					}else{
						messages.add("MLB Ad google query id is --> "+googleQueryId);
						messages.add("MLB Ad google item id is --> "+gogleItemId);
						messages.add("MLB Ad google creative id is --> "+googleCreativeId);
					}
				}catch (Exception e) {
					messages.add("MLB Ad is not fired..."
							+ driver.getCurrentUrl());
			}
			}
		} catch (Exception e) {
			messages.add("MLB Ad is missing  "
					+ driver.getCurrentUrl());
		}
		// check DMR Ad
		try {
			List<WebElement> tag = driver.findElement(
					By.className("article__content-body")).findElements(
					By.className("mmr"));
			
			// set counter to check inline dlb after every 2nd section for
			// sections <=6 and after every 3rd section if sections > 6
	for (int i = 1; i <= tag.size(); i++) {
				
				try {
					 WebElement mmr = driver.findElement(By.id("mmr" + i
                             + "__slot"));
					//System.out.println(advt.getAttribute("id"));
					 messages.add("MMR Ad exist after section "
								+ (i + 1));
				String googleQueryId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-google-query-id");
				String gogleItemId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-line-item-id");
				String googleCreativeId = driver.findElement(By.id("mmr" + i + "__slot")).getAttribute("data-creative-id");
				if (googleQueryId.isEmpty()) {
					messages.add("MMR"
							+ i
							+ " Ad at right rail on Replatform Nutrition Article is not fired.."
							+ driver.getCurrentUrl());
				} else {
					messages.add("MMR Ad google query id is --> "+googleQueryId);
					messages.add("MMR Ad google item id is --> "+gogleItemId);
					messages.add("MMR Ad google creative id is --> "+googleCreativeId);
				}
				
				} catch (Exception e) {
					 messages.add("MMR" + i + " Ad is missing ");
				}
								
			}
		} catch (Exception E) {
			messages.add("Problem with MMR Ad on Nutrition Article Page " + E.toString());
		}
		
		//INA ad
		try {
			
					WebElement inaAdSlot = driver.findElement(
							By.className("article__content-body")).findElement(By.className("ina")).findElement(By.className("ina__slot"));
				
					if (!(inaAdSlot.isDisplayed())) {
						messages.add("INA Ad is Not displayed "
								+ message);
					}
					messages.add("INA Ad is displayed "
									);
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("ina__slot"))).getAttribute("data-creative-id");
					if(googleQueryId.isEmpty()){
						messages.add("INA Ad is not firing - 320 BP ");
					}else{
						messages.add("INA Ad google query id is --> "+googleQueryId);
						messages.add("INA Ad google item id is --> "+gogleItemId);
						messages.add("INA Ad google creative id is --> "+googleCreativeId);
					}
				} catch (Exception e) {
					messages.add("INA Ad is Not displayed "
							+ driver.getCurrentUrl());
	}
		
		driver.close();
		} catch (Exception e) {
			messages.add("Exception in Nutrition Article Page - 320  BP test  " + e.toString());
		}

		
		// Legacy Standard Article
			driver.get(baseURL
				+ "/health/10-habits-to-ensure-better-health-in-10-years");
		Thread.sleep(3000);
		messages.add("       ");

		messages.add("Legacy Standard Article");
		messages.add("===============");
		messages.add(" ");
		messages.add("Test URL : " + driver.getCurrentUrl());
		try {
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkUpperAd();
			if (message != null) {
				messages.add("LB Ad is missing on Legacy Standard Article page - 1024 breakpoint "
						+ message);
			} else {
				messages.add("LB Ad is present on Legacy Standard Article - 1024 breakpoint ");
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on Legacy Standard Article - 1024 breakpoint ");
				}else{
					messages.add("LB Ad is firing on Legacy Standard Article - 1024 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
			
			}
		} catch (Exception e) {
			messages.add("LB Ad is missing on Legacy Standard Article - 1024 breakpoint"
					+ driver.getCurrentUrl());
		}
		// check MR1 ad
		try {
			CheckAds MR1Ads = new CheckAds(driver);
			message = MR1Ads.checkRhsAdsMr1();
			if (message != null) {
				messages.add("MR1 Ad is missing on Legacy Standard Article - 1024 breakpoint! "
						+ message);
			} else {
				messages.add("MR1 Ad is present on Legacy Standard Article - 1024 breakpoint! ");
			
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Legacy Standard Article - 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Legacy Standard Article - 1024 breakpoint ");
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
			}
		} catch (Exception e) {
			messages.add("MR1 Ad is missing on Legacy Standard Article - 1024 breakpoint"
					+ driver.getCurrentUrl()+e.toString());
		}
		// check MR2 ad
		try {
			CheckAds MR1Ads = new CheckAds(driver);
			message = MR1Ads.checkRhsAdsMr2();
			if (message != null) {
				messages.add("MR2 Ad is missing on Legacy Standard Article - 1024 breakpoint!"
						+ message);
			} else {
				messages.add("MR2 Ad is present on Legacy Standard Article - 1024 breakpoint!");
				
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR2 Ad is not firing on Legacy Standard Article - 1024 breakpoint ");
				}else{
					messages.add("MR2 Ad is firing on Legacy Standard Article - 1024 breakpoint ");
					messages.add("MR2 Ad google query id is --> "+googleQueryId);
					messages.add("MR2 Ad google item id is --> "+gogleItemId);
					messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
				}
			}
		} catch (Exception e) {
			messages.add("MR2 Ad is missing on Legacy Standard Article - 1024 breakpoint"
					+ driver.getCurrentUrl());
		}
		// check for WSL AD
		try {
			CheckAds wslAds = new CheckAds(driver);
			message = wslAds.checkLHSAdsWsl();
			if (message != null) {
				messages.add("WSL Ad is missing on Legacy Standard Article - 1024 breakpoint!"
						+ message);
			} else {
				messages.add("WSL Ad is present on Legacy Standard Article - 1024 breakpoint!");
				
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-wsl"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-wsl"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-wsl"))).getAttribute("data-creative-id");
			
				if(googleQueryId.isEmpty()){
					messages.add("WSL Ad is not firing on Legacy Standard Article - 1024 breakpoint ");
				}else{
					messages.add("WSL Ad is firing on Legacy Standard Article - 1024 breakpoint ");
					messages.add("WSL Ad google query id is --> "+googleQueryId);
					messages.add("WSL Ad google item id is --> "+gogleItemId);
					messages.add("WSL Ad google creative id is --> "+googleCreativeId);
				}
			}
		} catch (Exception e) {
			messages.add("WSL Ad is missing on Legacy Standard Article - 1024 breakpoint "
					+ driver.getCurrentUrl());
		}
		// Check Netseer Ad
		try {
			CheckAds netseerAds = new CheckAds(driver);
			message = netseerAds.checkNetseerAds();
			if (message != null) {
				messages.add("Netseer Ad is missing on Legacy Standard Article - 1024 breakpoint!"
						+ message);
			} else {
				messages.add("Netseer Ad is present on Legacy Standard Article - 1024 breakpoint!");
		}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Legacy Standard Article - 1024 BP test  "
					+ e.toString());
		}
		try {

			driver = setDevice("Nexus 7");
			driver.get(baseURL
					+ "/health/10-habits-to-ensure-better-health-in-10-years");
			driver.navigate().refresh();
			Thread.sleep(3000);
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on Legacy Standard Article - 600 breakpoint "
							+ message);
				} else {
					messages.add("LB Ad is present on Legacy Standard Article - 600 breakpoint ");
					
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Legacy Standard Article - 600 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Legacy Standard Article - 600 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
					}
				
				
			} catch (Exception ee) {
				messages.add("LB ad is not display on Legacy Standard Article 600*1024"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on Legacy Standard Article - 600 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on Legacy Standard Article - 600 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on Legacy Standard Article - 600 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on Legacy Standard Article - 600 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e1) {
				messages.add("MR1 ad is not display on Legacy Standard Article 600 BP "
						+ driver.getCurrentUrl());
			}
			// check MR2 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr2();
				if (message != null) {
					messages.add("MR2 Ad is missing on Legacy Standard Article - 600 breakpoint!"
							+ message);
				} else {
					messages.add("MR2 Ad is present on Legacy Standard Article - 600 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR2 Ad is not firing on Legacy Standard Article - 600 breakpoint ");
					}else{
						messages.add("MR2 Ad is firing on Legacy Standard Article - 600 breakpoint ");
						
						messages.add("MR2 Ad google query id is --> "+googleQueryId);
						messages.add("MR2 Ad google item id is --> "+gogleItemId);
						messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e2) {
				messages.add("MR2 ad is not display on Legacy Standard Article 600 BP "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e3) {
			messages.add("Exception in Legacy Standard Article - 600 BP test  "
					+ e3.toString());
		}
		try {

			driver = setDevice("iPhone 5");
			driver.get(baseURL
					+ "/health/10-habits-to-ensure-better-health-in-10-years");
			Thread.sleep(3000);
			// check LB ad
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on Legacy Standard Article - 320 breakpoint"
							+ message);
				} else {
					messages.add("LB Ad is present on Legacy Standard Article - 320 breakpoint");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Legacy Standard Article - 320 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Legacy Standard Article - 320 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e7) {
				messages.add("LB ad is not display on Legacy Standard Article - 320 breakpoint"
						+ driver.getCurrentUrl());
			}
			// Check MR3 ad
			try {
				Thread.sleep(3000);
				List<WebElement> tag = driver.findElement(
						By.className("article-body")).findElements(
						By.tagName("p"));
				int adscount = 0;
				if (tag.size() > 3) {
					adscount = (tag.size() - 2) / 5 + 1;
				}
				System.out.println("adcount  --- " + adscount);
				int counter = 1;
				for (int i = 10; i < adscount + 10; i++) {
					try {
						WebElement mr3Ad = driver.findElement(By
								.className("mr3-widget-" + i));
						messages.add(ordinal(counter)
								+ " MR3 ad is visible on Legacy Standard Article - 320 breakpoint ");
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-creative-id");
					
						
						if(googleQueryId.isEmpty()){
							messages.add("MR3 is not fired..." + i + " on Legacy Standard Article " + driver.getCurrentUrl());
						}else{
							messages.add("MR3 Ad is firing on Legacy Standard Article - 320 breakpoint ");
							
							messages.add("MR3 Ad google query id is --> "+googleQueryId);
							messages.add("MR3 Ad google item id is --> "+gogleItemId);
							messages.add("MR3 Ad google creative id is --> "+googleCreativeId);
							
							
						}
					} catch (Exception e) {
						messages.add(ordinal(counter)
								+ " MR3 ad is missing on Legacy Standard Article - 320 breakpoint"
								+ e.toString());
					}
					counter++;
				}

			} catch (Exception E) {
				System.out.println(E.toString());
			}
			driver.close();
		} catch (Exception e5) {
			System.out
					.println("Problem in iphone size on Legacy Standard Article");
		}
		// -------------------------------Legcay TAB
		// Article---------------------------------------
	
		 messages.add("       ");

		messages.add("Legcay TAB Article");
		messages.add("===========");
		messages.add(" ");

		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			Thread.sleep(3000);
			// driver.get(baseURL + "/health/meningitis");
			driver.get(baseURL + "/health/depression/melancholic-depression");
			messages.add("Test Url : " + driver.getCurrentUrl());
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkUpperAd();
			if (message != null) {
				messages.add("LB Ad is missing on Legcay TAB Article -  1024 breakpoint!"
						+ message);
			} else {
				messages.add("LB Ad is present on Legcay TAB Article -  1024 breakpoint!");
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
			
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on Legacy TAB Article - 1024 breakpoint ");
				}else{
					messages.add("LB Ad is firing on Legacy TAB Article - 1024 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
			}
		} catch (Exception e) {
			messages.add("LB ad is not display" + driver.getCurrentUrl());
		}
		// check MR1 ad
		try {
			CheckAds MR1Ads = new CheckAds(driver);
			message = MR1Ads.checkRhsAdsMr1();
			if (message != null) {
				messages.add("MR1 Ad is missing on Legcay TAB Article -  1024 breakpoint!"
						+ message);
			} else {
				messages.add("MR1 Ad is present on Legcay TAB Article -  1024 breakpoint!");
			
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
			
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Legacy TAB Article - 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Legacy TAB Article - 1024 breakpoint ");
					
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
			}
		} catch (Exception e) {
			messages.add("MR1 ad is not display on Legcay TAB Article -  1024 BP "
					+ driver.getCurrentUrl());
		}
		// check MR2 ad
		try {
			CheckAds MR1Ads = new CheckAds(driver);
			message = MR1Ads.checkRhsAdsMr2();
			if (message != null) {
				messages.add("MR2 Ad is missing on Legcay TAB Article -  1024 breakpoint!"
						+ message);
			} else {
				messages.add("MR2 Ad is present on Legcay TAB Article -  1024 breakpoint!");
				
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR2 Ad is not firing on Legacy TAB Article - 1024 breakpoint ");
				}else{
					messages.add("MR2 Ad is firing on Legacy TAB Article - 1024 breakpoint ");
					messages.add("MR2 Ad google query id is --> "+googleQueryId);
					messages.add("MR2 Ad google item id is --> "+gogleItemId);
					messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
				}
			}
		} catch (Exception e) {
			messages.add("MR2 ad is not display on Legcay TAB Article -  1024 BP "
					+ driver.getCurrentUrl());
		}
		// Check Netseer Ad
		try {
			CheckAds netseerAds = new CheckAds(driver);
			message = netseerAds.checkNetseerAds();
			if (message != null) {
				messages.add("Netseer Ad is missing on Legcay TAB Article -  1024 breakpoint!"
						+ message);
			} else {
				messages.add("Netseer Ad is present on Legcay TAB Article -  1024 breakpoint!");
			}
		} catch (Exception e) {
		}
		driver.close();
		// check for 600*1024 size and also 768*1024 size half worked
		try {
			driver = setDevice("Nexus 7");
			Thread.sleep(3000);
			//driver.get(baseURL + "/health/meningitis");
			driver.get(baseURL + "/health/depression/melancholic-depression");
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on Legcay TAB Article -  600 breakpoint!"
							+ message);
				} else {
					messages.add("LB Ad is present on Legcay TAB Article -  600 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Legacy TAB Article - 600 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Legacy TAB Article - 600 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on Legcay TAB Article -  600 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				List<WebElement> tag = driver.findElement(
						By.className("hl-article-toc")).findElements(
						By.className("content_body"));
				//System.out.println("ad12 count" + tag.size());
				// if (tag.size()%2 == 1 ){
				int counter = 10;
				for (int i = 1; i <= tag.size();) {
					String txt = tag.get(i).findElement(By.tagName("p")).getText();
				//	System.out.println("texting  " + txt);
					// String
					String xpath = "//div[@id='TOC_TITLE_"
							+ i
							+ "']/following-sibling::div[@class='mr3block c mr3-widget-"
							+ counter + "']";
					// String xpath =
					// "//p[contains(@class, 'hl-section-disclaimer') and text()=txt]/preceding-sibling::div[@class='mr3block c']";
					// String xpath =
					// "/preceding-sibling::div[@class='mr3block c']";
					try {
						WebElement topAd = driver.findElement(By.xpath(xpath));
						String pass = topAd.findElement(
								By.className("ads-box-header")).getText();

						//System.out.println(pass);
						if (pass.equalsIgnoreCase("advertisement")) {
							messages.add("MR3 ad is present after section " + i
									+ " on TAB Article -  600 breakpoint");
							System.out.println("MM3 Ad exist after section "
									+ (i));
							
							String googleQueryId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-google-query-id");
							String gogleItemId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-line-item-id");
							String googleCreativeId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-creative-id");
							
							if(googleQueryId.isEmpty()){
								messages.add("Inline DLB is not fired..." + i + " on Replatform Tabbed Article" + driver.getCurrentUrl());
							}else{
								messages.add("MR3 Ad is firing on Legacy Standard Article - 320 breakpoint ");
								
							//	System.out.println("MR3 DLB Ad is fired..." + i + " Advertisement id is --> " + googleQueryId);
								messages.add("MR3 Ad google query id is --> "+googleQueryId);
								messages.add("MR3 Ad google item id is --> "+gogleItemId);
								messages.add("MR3 Ad google creative id is --> "+googleCreativeId);
							}	
						}
					} catch (Exception e) {
						messages.add("MR3 ad is missing on Legcay TAB Article -  600 breakpoint"
								+ e.toString());

					}
					i = i + 2;
					counter++;
				}
				// messages.add("MR3 ad is present on TAB Article -  320 breakpoint");

			} catch (Exception E) {
				messages.add("Exception in Legacy TAB Article - 320 BP test  "
						+ E.toString());
			}
			// check MR2 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr2();
				if (message != null) {
					messages.add("MR2 Ad is missing on Legcay TAB Article -  600 breakpoint!"
							+ message);
				} else {
					messages.add("MR2 Ad is present on Legcay TAB Article -  600 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("hl-ad-mr3"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("hl-ad-mr3"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("hl-ad-mr3"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR2 Ad is not firing on Legacy TAB Article - 600 breakpoint ");
					}else{
						messages.add("MR2 Ad is firing on Legacy TAB Article - 600 breakpoint ");
						
						messages.add("MR2 Ad google query id is --> "+googleQueryId);
						messages.add("MR2 Ad google item id is --> "+gogleItemId);
						messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR2 ad is not display on Legcay TAB Article -  600 BP "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add(e.toString());
		}

		driver = setDevice("iPhone 5");
		Thread.sleep(3000);
		//driver.get(baseURL + "/health/meningitis");
		driver.get(baseURL + "/health/depression/melancholic-depression");
		// check LB ad
		try {
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkStickyLB();
			if (message != null) {
				messages.add("LB Ad is missing on Legcay TAB Article -  320 breakpoint!"
						+ message);
			} else {
				messages.add("LB Ad is present on Legcay TAB Article -  320 breakpoint!");
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
			
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on Legacy TAB Article - 320 breakpoint ");
				}else{
					messages.add("LB Ad is firing on Legacy TAB Article - 320 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
			}
		} catch (Exception e) {
			messages.add("LB ad is not display on Legcay TAB Article -  320 breakpoint"
					+ driver.getCurrentUrl());
		}

		// Check MR3 ad
		try {
			List<WebElement> tag = driver.findElement(
					By.className("hl-article-toc")).findElements(
					By.className("content_body"));
			System.out.println("ad12 count" + tag.size());
			// if (tag.size()%2 == 1 ){
			int counter = 10;
			for (int i = 1; i <= tag.size();) {
				String txt = tag.get(i).findElement(By.tagName("p")).getText();
			//	System.out.println("texting  " + txt);
				// String
				String xpath = "//div[@id='TOC_TITLE_"
						+ i
						+ "']/following-sibling::div[@class='mr3block c mr3-widget-"
						+ counter + "']";
				// String xpath =
				// "//p[contains(@class, 'hl-section-disclaimer') and text()=txt]/preceding-sibling::div[@class='mr3block c']";
				// String xpath =
				// "/preceding-sibling::div[@class='mr3block c']";
				try {
					WebElement topAd = driver.findElement(By.xpath(xpath));
					String pass = topAd.findElement(
							By.className("ads-box-header")).getText();

				//	System.out.println(pass);
					if (pass.equalsIgnoreCase("advertisement")) {
						messages.add("MR3 ad is present after section " + i
								+ " on TAB Article -  320 breakpoint");
						System.out.println("MM3 Ad exist after section "
								+ (i));
						
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-creative-id");
						
						if(googleQueryId.isEmpty()){
							messages.add("Inline DLB is not fired..." + i + " on Replatform Tabbed Article" + driver.getCurrentUrl());
						}else{
							messages.add("MR3 Ad is firing on Legacy Standard Article - 320 breakpoint ");
							
						//	System.out.println("MR3 DLB Ad is fired..." + i + " Advertisement id is --> " + googleQueryId);
							messages.add("MR3 Ad google query id is --> "+googleQueryId);
							messages.add("MR3 Ad google item id is --> "+gogleItemId);
							messages.add("MR3 Ad google creative id is --> "+googleCreativeId);
						}	
					}
				} catch (Exception e) {
					messages.add("MR3 ad is missing on Legcay TAB Article -  320 breakpoint"
							+ e.toString());

				}
				i = i + 2;
				counter++;
			}
			// messages.add("MR3 ad is present on TAB Article -  320 breakpoint");

		} catch (Exception E) {
			messages.add("Exception in Legacy TAB Article - 320 BP test  "
					+ E.toString());
		}
	driver.close();

				
		 
		// -----------------------------Health
		// Slideshow----------------------------------
		messages.add(" ");
		messages.add("Health Slideshow");
		messages.add("================");
		messages.add(" ");

		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			driver.get(baseURL
					+ "/health-slideshow/10-tips-get-your-kids-sleep");
			messages.add("Test Url : " + driver.getCurrentUrl());
			Thread.sleep(3000);
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on slideshow page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("LB Ad is present on slideshow page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on slideshow page - 1024 breakpoint ");
				}else{
					messages.add("LB Ad is firing on slideshow page- 1024 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on slideshow page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on slideshow page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on slideshow page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on slideshow page - 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on slideshow page - 1024 breakpoint ");
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("MR1 ad is not display on slideshow page - 1024 breakpoint "
						+ driver.getCurrentUrl());
			}
			// check MR2 ad
			try {
				CheckAds MR2Ads = new CheckAds(driver);
				message = MR2Ads.checkRhsAdsMr2();
				if (message != null) {
					messages.add("MR2 Ad is missing on slideshow page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR2 Ad is present on slideshow page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR2 Ad is not firing on slideshow page - 1024 breakpoint ");
				}else{
					messages.add("MR2 Ad is firing on slideshow page - 1024 breakpoint ");
					messages.add("MR2 Ad google query id is --> "+googleQueryId);
					messages.add("MR2 Ad google item id is --> "+gogleItemId);
					messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("MR2 ad is not display on slideshow page - 1024 breakpoint "
						+ driver.getCurrentUrl());
			}

			// Check netsser ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("PB Ad is missing on slideshow page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("PB Ad is present on slideshow page - 1024 breakpoint!");
				}
			} catch (Exception e) {
				messages.add("PB ad is not display on slideshow page - 1024 breakpoint "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Slideshow Page - 1024 BP test  "
					+ e.toString());
			System.out.println(e.toString());
		}

		try {
			driver = setDeviceOrientation("iPad Mini", 768, 1024);
			driver.get(baseURL
					+ "/health-slideshow/10-tips-get-your-kids-sleep");
			Thread.sleep(7000);
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on slideshow page - 768 breakpoint! "
							+ message);
				} else {
					messages.add("LB Ad is present on slideshow page - 768 breakpoint!");
					
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on slideshow page - 768 breakpoint ");
					}else{
						messages.add("LB Ad is firing on slideshow page - 768 breakpoint");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("LB ad is missing on slideshow page - 768 breakpoint"
						+ driver.getCurrentUrl());
			}

			try {
				Thread.sleep(3000);
				WebElement sliderContainer = driver
						.findElement(By.className("flex-container"))
						.findElement(By.className("flexslider"))
						.findElement(By.className("flex-direction-nav"))
						.findElement(By.className("flex-next"));
				WebElement totSld = driver.findElement(
						By.className("flex-container")).findElement(
						By.className("total-slides"));
				int totSl = Integer.parseInt(totSld.getText()) - 1;
				System.out.println(totSl);
				for (int i = 1; i <= totSl; i++) {
					if (i % 5 == 0) {
						Thread.sleep(3000);
						try {
							WebElement mr15 = driver
									.findElement(
											By.className("ad-placeholder" + i))
									.findElement(By.id("gpt-ad-mr1"))
									.findElement(
											By.xpath("//div[contains(@id,'google_ads_iframe_')]"));
							// .findElement(By.id("DFPAD_MR"));
							System.out.println(mr15.getText() + "   "
									+ mr15.isEnabled() + "   "
									+ mr15.isDisplayed());
							
							String googleQueryId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
							String gogleItemId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
							String googleCreativeId=new WebDriverWait(driver, 25)
							.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
						
							if(googleQueryId.isEmpty()){
								messages.add("MR1 Ad is not firing on slideshow page - 768 breakpoint ");
							}else{
								messages.add("MR1 Ad is firing on slideshow page - 768 breakpoint");
								
								messages.add("MR1 Ad google query id is --> "+googleQueryId);
								messages.add("MR1 Ad google item id is --> "+gogleItemId);
								messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
							}
							
						} catch (Exception e) {
							messages.add("MR1 Ad is missing on "
									+ i
									+ " slide of slideshow page - 768 breakpoint!");
						}
						
					messages.add("MR1 Ad is present on " + i
								+ " slide of slideshow page - 768 breakpoint!");
					}
					sliderContainer.click();
					Thread.sleep(1000);

				}

			} catch (Exception e) {
				System.out.println(e.toString());
				messages.add("MR1 Ad not display on slideshow page - 768 breakpoint"
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Slideshow Page - 768 BP test  "
					+ e.toString());
		}

		try {
			driver = setDevice("iPhone 5");
			driver.get(baseURL
					+ "/health-slideshow/10-tips-get-your-kids-sleep");
			Thread.sleep(3000);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0,250)", "");
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on slideshow page - 320 breakpoint!"
							+ message);
				} else {
					messages.add("LB Ad is present on slideshow page - 320 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on slideshow page - 320 breakpoint ");
					}else{
						messages.add("LB Ad is firing on slideshow page - 320 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on slideshow page - 320 breakpoint"
						+ driver.getCurrentUrl());
			}

			try {

				List<WebElement> totSlds = driver
						.findElement(By.className("flex-container"))
						.findElement(By.className("slides"))
						.findElements(By.tagName("li"));

				int totSl = totSlds.size() - 1;
				if (totSl >= 9) {
					totSl = 9;
				}
				System.out.println(totSl);
				int counter = 10;
				for (int i = 0; i <= totSl + 1; i++) {
					if ((i + 1) % 2 == 0) {
						Thread.sleep(3000);
						System.out.println(totSlds.get(i).getText());

						WebElement mr15 = totSlds.get(i).findElement(
								By.className("mr3-widget-" + counter));
						// .findElement(By.id("DFPAD_MR"));
						counter++;
						System.out.println(mr15.getText());
						if (mr15.isEnabled()) {
							messages.add("MR3 Ad is present after "
									+ (i + 1)
									+ " slide of slideshow page - 320 breakpoint!");
						} else {
							messages.add("MR3 Ad is missing after"
									+ (i + 1)
									+ " slide of slideshow page - 320 breakpoint!");
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-creative-id");
					
						
						if(googleQueryId.isEmpty()){
							messages.add("MR3 is not fired..." + i + " on slideshow page " + driver.getCurrentUrl());
						}else{
							messages.add("MR3 Ad is firing on slideshow page - 320 breakpoint ");
							
							messages.add("MR3 Ad google query id is --> "+googleQueryId);
							messages.add("MR3 Ad google item id is --> "+gogleItemId);
							messages.add("MR3 Ad google creative id is --> "+googleCreativeId);
							
							
						}
						}
					}

					Thread.sleep(1000);

				}

			} catch (Exception e) {
				System.out.println(e.toString());
				messages.add("Contents not display on slideshow page - 320 breakpoint"
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			 messages.add("page is not loaded"+driver.getCurrentUrl());
		}

		// --------------------------Topic
		// Center---------------------------------------
		messages.add(" ");
		messages.add("Topic Center");
		messages.add("============");

		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			driver.get(baseURL + "/health/depression");
			Thread.sleep(3000);
			// Check LB ads
			messages.add(" ");
			messages.add("Test Url : " + driver.getCurrentUrl());

			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on topic center page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("LB Ad is present on topic center page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on topic center page - 1024 breakpoint ");
				}else{
					messages.add("LB Ad is firing on topic center page - 1024 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on topic center page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on topic center page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on topic center page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on topic center page - 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on topic center page - 1024 breakpoint ");
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("MR1 ad is not display on topic center page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR2 ad
			try {
				CheckAds MR2Ads = new CheckAds(driver);
				message = MR2Ads.checkRhsAdsMr2();
				if (message != null) {
					messages.add("MR2 Ad is missing on topic center page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR2 Ad is present on topic center page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR2 Ad is not firing on topic center page - 1024 breakpoint ");
				}else{
					messages.add("MR2 Ad is firing on topic center page - 1024 breakpoint ");
					messages.add("MR2 Ad google query id is --> "+googleQueryId);
					messages.add("MR2 Ad google item id is --> "+gogleItemId);
					messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("MR2 ad is not display on topic center page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}

			// Check netsser ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on topic center page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("Netseer Ad is present on topic center page - 1024 breakpoint!");
				}
			} catch (Exception e) {
				messages.add("Netseer ad is not display on topic center page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			messages.add("       ");
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Topic Center Page - 1024 BP test  "
					+ e.toString());
		}
		try {
			driver = setDeviceOrientation("iPad Mini", 768, 1024);
			driver.get(baseURL + "/health/depression");
			Thread.sleep(3000);
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on topic center page - 768 breakpoint!"
							+ message);
				} else {
					messages.add("LB Ad is present on topic center page - 768 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on topic center page - 768 breakpoint! ");
					}else{
						messages.add("LB Ad is firing on topic center page - 768 breakpoint! ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on topic center page - 768 breakpoint"
						+ driver.getCurrentUrl());
			}

			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on topic center page - 768 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on topic center page - 768 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on topic center page - 768 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on topic center page - 768 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR1 ad is not display on topic center page - 768 breakpoint"
						+ driver.getCurrentUrl());
			}
			messages.add("       ");
			driver.close();
		} catch (Exception e) {
			messages.add("Page is not loaded for Topic Center Page 768 BP "
					+ driver.getCurrentUrl() + e.toString());
		}

		try {
			driver = setDevice("iPhone 5");
			driver.get(baseURL + "/health/depression");
			Thread.sleep(3000);
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on topic center page - 320 breakpoint!"
							+ message);
				} else {
					messages.add("LB Ad is present on topic center page - 320 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on topic center page - 320 breakpoint ");
					}else{
						messages.add("LB Ad is firing on topic center page - 320 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on topic center page - 320 breakpoint"
						+ driver.getCurrentUrl());
			}

			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on topic center page - 320 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on topic center page - 320 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on topic center page - 320 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on topic center page - 320 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR1 ad is not display on topic center page - 320 breakpoint"
						+ driver.getCurrentUrl());
			}
			messages.add("       ");
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Topic Center Page - 320 BP test  "
					+ e.toString());
		}

		// --------------------------------Single Video
		// Page---------------------------------------
		messages.add("       ");
		messages.add("Single Video Page");
		messages.add("============");
		// messages.add(" ");
		// messages.add("Test Url : " + driver.getCurrentUrl());

		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			driver.get(baseURL + "/video/high-cholesterol-treatments-and-management");
			Thread.sleep(3000);
			// Check LB ads
			messages.add(" ");
			messages.add("Test Url : " + driver.getCurrentUrl());
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on Single video page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("LB Ad is present on Single video page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on Single video page - 1024 breakpoint ");
				}else{
					messages.add("LB Ad is firing on Single video page - 1024 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on Single video page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on Single video page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on Single video page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Single video page - 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Single video page - 1024 breakpoint ");
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("MR1 ad is not display on Single video page - 1024 breakpoint "
						+ driver.getCurrentUrl());
			}
			messages.add("       ");
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Single video page - 1024 BP test  "
					+ e.toString());
		}
		try {
			driver = setDeviceOrientation("iPad Mini", 768, 1024);
			driver.get(baseURL + "/video/high-cholesterol-treatments-and-management");
			Thread.sleep(3000);
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on Single video page - 768 breakpoint!"
							+ message);
				} else {
					messages.add("LB Ad is present on Single video page - 768 breakpoint!");
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on Single video page - 768 breakpoint"
						+ driver.getCurrentUrl());
			}
			try {
					WebElement topAd = driver.findElement(By
							.className("box-ad-mr1"));
					System.out.println(topAd.getText());
					String pass = topAd.findElement(
							By.className("ads-box-header")).getText();
					
					if (!(topAd.isDisplayed())) {
						messages.add("MR1 ad is Not displayed  - 768 breakpoint");
					} else {
						messages.add("MR1 ad is present - 768 breakpoint");
						String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR2 Ad is not firing on Single video page - 7680 breakpoint ");
					}else{
						messages.add("MR2 Ad is firing on Single video page - 768 breakpoint ");
						
						messages.add("MR2 Ad google query id is --> "+googleQueryId);
						messages.add("MR2 Ad google item id is --> "+gogleItemId);
						messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
					}
					}

				} catch (Exception e) {
					messages.add("MR1 ad is missing on Single video article page - 768 breakpoint");
				}

			
			messages.add("       ");
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Single video page - 768  BP test  "
					+ e.toString());
		}

		try {
			driver = setDevice("iPhone 5");
			driver.get(baseURL + "/video/high-cholesterol-treatments-and-management");
			Thread.sleep(3000);
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on Single video page - 320 breakpoint!"
							+ message);
				} else {
					messages.add("LB Ad is present on Single video page - 320 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Single video page - 320 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Single video page - 320 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on Single video page - 320 breakpoint "
						+ driver.getCurrentUrl());
			}
			messages.add("       ");
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Single video page - 320 BP test  "
					+ e.toString());
		}

		// ------------------------------Health Assessment
		// page---------------------------------------
		messages.add("       ");
		messages.add("Health Assessment Page");
		messages.add("======================");
		messages.add(" ");
		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			driver.get(baseURL + "/health-assessments/knee-pain");
			messages.add("Test URL : " + driver.getCurrentUrl());
			Thread.sleep(6000);
			driver.navigate().refresh();
			// Check LB ads // Removed and it's requirement
			try {

				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on Assessment page & it's requirement - 1024 breakpoint!"
							+ message);
				} else {
					
				}
			} catch (Exception e) {
				messages.add("LB ad is not display on Assessment page & it's requirement - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				WebElement MR1AdsUp = driver
						.findElement(By.id("scene1"))
						.findElement(By.className("box-info-mr1"))
						.findElement(By.id("gpt-ad-mr1"))
						.findElement(
								By.xpath("//div[contains(@id,'google_ads_iframe_')]"));
				// WebElement MR1AdsUp = new WebDriverWait(driver,
				// 15).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#scene1 .box-info-mr1 .gpt-ad-mr1")));
				messages.add("MR1 Ad is present on 1st page of Assessment page - 1024 breakpoint!");
				
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Assessment page below the slide  - 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Assessment page below the slide  - 1024 breakpoint ");
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
				
				driver.findElement(By.className("next-scene")).click();
				Thread.sleep(3000);
				try {
					WebElement MR1AdsDown = driver.findElement(
							By.className("box-other-mr1")).findElement(
							By.id("gpt-ad-mr1"));
					messages.add("MR1 Ad is present on 2nd slide of Assessment page below the slide - 1024 breakpoint! ");
				} catch (Exception e) {
					messages.add("MR1 Ad is missing on 2nd page of Assessment page below the slide - 1024 breakpoint! ");
					
				String googleQueryId1=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId1=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId1=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Assessment page below the slide  - 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Assessment page below the slide  - 1024 breakpoint ");
					messages.add("MR1 Ad google query id is --> "+googleQueryId1);
					messages.add("MR1 Ad google item id is --> "+gogleItemId1);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId1);
				}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on 1st page of Assessment page - 1024 breakpoint!"
						+ e.toString());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Problem in loading Assessment page - 1024 breakpoint!"
					+ e.toString());
		}

		// ----------------------------Diabetesmine
		// Article--------------------------------
		Thread.sleep(3000);
		messages.add("       ");

		messages.add("Diabetesmine Article");
		messages.add("====================");

		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			driver.get(baseURL
					+ "/diabetesmine/new-research-ada-scisessions-2017");
			Thread.sleep(3000);
			messages.add(" ");
			messages.add("Test URL : " + driver.getCurrentUrl());
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on Diabetesmine article page - 1024 breakpoint"
							+ message);
				} else {
					messages.add("LB Ad is present on Diabetesmine article page - 1024 breakpoint");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on Diabetesmine article page - 1024 breakpoint ");
				}else{
					messages.add("LB Ad is firing on Diabetesmine article page - 1024 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("LB Ad is missing on Diabetesmine article page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on Diabetesmine article page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on Diabetesmine article page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Diabetesmine article page - 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Diabetesmine article page - 1024 breakpoint ");
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on Diabetesmine article page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR2 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr2();
				if (message != null) {
					messages.add("MR2 Ad is missing on Diabetesmine article page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR2 Ad is present on Diabetesmine article page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR2 Ad is not firing on Diabetesmine article page - 1024 breakpoint ");
				}else{
					messages.add("MR2 Ad is firing on Diabetesmine article page - 1024 breakpoint ");
					messages.add("MR2 Ad google query id is --> "+googleQueryId);
					messages.add("MR2 Ad google item id is --> "+gogleItemId);
					messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("MR2 Ad is missing on Diabetesmine article page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}

			// Check Netseer Ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on Diabetesmine article page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("Netseer Ad is present on Diabetesmine article page - 1024 breakpoint!");
				}
			} catch (Exception e) {
				messages.add("Netseer Ad is missing on Diabetesmine article page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			messages.add("       ");
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Diabetesmine article page - 1024 BP test  "
					+ e.toString());
		}
		try {
		
			driver = setDevice("Nexus 7");
			driver.get(baseURL
					+ "/diabetesmine/new-research-ada-scisessions-2017");
			Thread.sleep(3000);
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on Diabetesmine article page - 600 breakpoint "
							+ message);
				} else {
					messages.add("LB Ad is present on Diabetesmine article page - 600 breakpoint ");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Diabetesmine article page - 600 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Diabetesmine article page - 600 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception ee) {
				messages.add("LB ad is not display on Diabetesmine article page 600*1024"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on Diabetesmine article page - 600 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on Diabetesmine article page - 600 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on Diabetesmine article page - 600 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on Diabetesmine article page - 600 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e1) {
				messages.add("MR1 ad is not display on Diabetesmine article page 600"
						+ driver.getCurrentUrl());
			}
			// check MR2 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr2();
				if (message != null) {
					messages.add("MR2 Ad is missing on Diabetesmine article page - 600 breakpoint!"
							+ message);
				} else {
					messages.add("MR2 Ad is present on Diabetesmine article page - 600 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR2 Ad is not firing on Diabetesmine article page - 600 breakpoint ");
					}else{
						messages.add("MR2 Ad is firing on Diabetesmine article page - 600 breakpoint ");
						
						messages.add("MR2 Ad google query id is --> "+googleQueryId);
						messages.add("MR2 Ad google item id is --> "+gogleItemId);
						messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e2) {
				messages.add("MR2 ad is not display on Diabetesmine article page 600"
						+ driver.getCurrentUrl());
			}
			messages.add("       ");
			driver.close();
		} catch (Exception e3) {
			messages.add("Exception in Diabetesmine article page 600 BP test  "
					+ e3.toString());
		}
		try {
			driver = setDevice("iPhone 5");
			driver.get(baseURL
					+ "/diabetesmine/new-research-ada-scisessions-2017");
			Thread.sleep(3000);
			// check LB ad
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on Diabetesmine article page - 320 breakpoint"
							+ message);
				} else {
					messages.add("LB Ad is present on Diabetesmine article page - 320 breakpoint");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Diabetesmine article page - 320 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Diabetesmine article page - 320 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e7) {
				messages.add("LB ad is not display on Diabetesmine article page - 320 breakpoint"
						+ driver.getCurrentUrl());
			}
			// Check MR3 ad
			try {
				List<WebElement> tag = driver.findElements(By
						.className("hl-article-page"));
				int adscount = 0;
				if (tag.size() > 3) {
					adscount = (tag.size() - 2) / 5 + 1;
				}
				System.out.println("adcount  --- " + adscount);
				if (adscount > 4) {
					adscount = 4;
				}

				int counter = 1;
				for (int i = 10; i < adscount + 10; i++) {
					try {
						WebElement mr3Ad = driver.findElement(By
								.id("gpt-ad-mr3-" + i));
						messages.add(ordinal(counter)
								+ " MR3 ad is visible on Diabetesmine article page - 320 breakpoint");
						
						String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-creative-id");
					
						
						if(googleQueryId.isEmpty()){
							messages.add("MR3 is not fired..." + i + " on Diabetesmine article page " + driver.getCurrentUrl());
						}else{
							messages.add("MR3 Ad is firing on Diabetesmine article page - 320 breakpoint ");
							
							messages.add("MR3 Ad google query id is --> "+googleQueryId);
							messages.add("MR3 Ad google item id is --> "+gogleItemId);
							messages.add("MR3 Ad google creative id is --> "+googleCreativeId);
						}
								
					} catch (Exception e) {
						messages.add(ordinal(counter)
								+ " MR3 ad is missing on Diabetesmine article page - 320 breakpoint"
								+ e.toString());
					}
					counter++;
				}

			} catch (Exception e) {
				messages.add("MR3 ad is missing on Diabetesmine article page - 320 breakpoint");
			}
			messages.add("       ");
			driver.close();
		} catch (Exception e5) {
			messages.add("Exception in Diabetesmine article page - 320 BP test  "
					+ e5.toString());
		}

		// -------------------------------Bodymap
		// Page------------------------------------
	Thread.sleep(3000);
		messages.add("       ");

		messages.add("Bodymap Page");
		messages.add("============");
		driver = new ChromeDriver();
		try {

			// WebDriver driver =
			// setDeviceOrientation("Apple iPad Mini",1024,768);
			driver.get(baseURL + "/human-body-maps");
			messages.add(" ");
			messages.add("Test URL : " + driver.getCurrentUrl());
			Thread.sleep(3000);
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on Bodymap page - 1024 breakpoint "
							+ message);
				} else {
					messages.add("LB Ad is present on Bodymap page - 1024 breakpoint");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on Bodymap page - 1024 breakpoint ");
				}else{
					messages.add("LB Ad is firing on Bodymap page - 1024 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("LB Ad is missing on Bodymap page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on Bodymap page - 1024 breakpoint! "
							+ message);
				} else {
					messages.add("MR1 Ad is present on Bodymap page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Bodymap page - 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Bodymap page - 1024 breakpoint ");
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on Bodymap page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}

			// Check Netseer Ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on Bodymap page - 1024 breakpoint! "
							+ message);
				} else {
					messages.add("Netseer Ad is present on Bodymap page - 1024 breakpoint!");
				}
			} catch (Exception e) {
				messages.add("Netseer Ad is missing on Bodymap page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			messages.add(" ");
		} catch (Exception e) {
			messages.add("Exception in Bodymap page - 1024 BP test  "
					+ e.toString());
		}

		// --------------------------Directory
		// Page--------------------------------
	driver.get(baseURL + "/directory/topics");
		Thread.sleep(3000);
		messages.add("       ");

		messages.add("Directory Page");
		messages.add("==============");
		messages.add(" ");
		messages.add("Test URL : " + driver.getCurrentUrl());
		try {
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkUpperAd();
			if (message != null) {
				messages.add("LB Ad is missing on Directory page - 1024 breakpoint "
						+ message);
			} else {
				messages.add("LB Ad is present on Directory page - 1024 breakpoint");
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on Directory page - 1024 breakpoint ");
				}else{
					messages.add("LB Ad is firing on Directory page - 1024 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
			}
		} catch (Exception e) {
			messages.add("LB Ad is missing on Directory page - 1024 breakpoint"
					+ driver.getCurrentUrl());
		}
		// check MR1 ad
		try {
			CheckAds MR1Ads = new CheckAds(driver);
			message = MR1Ads.checkRhsAdsMr1();
			if (message != null) {
				messages.add("MR1 Ad is missing on Directory page - 1024 breakpoint! "
						+ message);
			} else {
				messages.add("MR1 Ad is present on Directory page - 1024 breakpoint!");
				
				String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Directory page- 1024 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Directory page - 1024 breakpoint ");
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
			}
		} catch (Exception e) {
			messages.add("Mr1 Ad is missing on Directory page - 1024 breakpoint"
					+ driver.getCurrentUrl());
		}

		// Check Netseer Ad
		try {
			CheckAds netseerAds = new CheckAds(driver);
			message = netseerAds.checkNetseerAds();
			if (message != null) {
				messages.add("Netseer Ad is missing on Directory page - 1024 breakpoint! "
						+ message);
			} else {
				messages.add("Netseer Ad is present on Directory page - 1024 breakpoint!");
			}
		} catch (Exception e) {
			messages.add("Netseer Ad is missing on Directory page - 1024 breakpoint"
					+ driver.getCurrentUrl());
		}

		// ----------------------------Blog Article
		// Page-----------------------------------
		driver = setDeviceOrientation("iPad Mini", 1024, 768);
		driver.get(baseURL + "/health-blogs/diet-diva/pumpkin-power");
		Thread.sleep(3000);
		messages.add("       ");
		
		messages.add("Blog Article Page");
		messages.add("=========");
		messages.add(" ");
		messages.add("Test URL : " + driver.getCurrentUrl());
		try {
			CheckAds Lbads = new CheckAds(driver);
			message = Lbads.checkUpperAd();
			if (message != null) {
				messages.add("LB Ad is missing on Blog page - 1024 breakpoint "
						+ message);
			} else {
				messages.add("LB Ad is present on Blog page - 1024 breakpoint ");
				String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Blog page - 1024 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Blog page - 1024 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
			}
		} catch (Exception e) {
			messages.add("LB Ad is missing on Blog page - 1024 breakpoint "
					+ driver.getCurrentUrl());
		}
		// check MR1 ad
		try {
			CheckAds MR1Ads = new CheckAds(driver);
			message = MR1Ads.checkRhsAdsMr1();
			if (message != null) {
				messages.add("MR1 Ad is missing on Blog page - 1024 breakpoint! "
						+ message);
			} else {
				messages.add("MR1 Ad is present on Blog page - 1024 breakpoint!");
				String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on Blog page - 1024 breakpoint! ");
					}else{
						messages.add("MR1 Ad is firing on Blog page - 1024 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
			}
		} catch (Exception e) {
			messages.add("MR1 Ad is missing on Blog page - 1024 breakpoint "
					+ driver.getCurrentUrl());
		}

		// Check Netseer Ad
		try {
			CheckAds netseerAds = new CheckAds(driver);
			message = netseerAds.checkNetseerAds();
			if (message != null) {
				messages.add("Netseer Ad is missing on Blog page - 1024 breakpoint! "
						+ message);
			} else {
				messages.add("Netseer Ad is present on Blog page - 1024 breakpoint! ");
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Netseer Ad is missing on Blog page - 1024 breakpoint! "
					+ driver.getCurrentUrl());
		}

		// ----------------------------Drug
		// Article------------------------------
		messages.add("       ");

		messages.add("Drug Article");
		messages.add("====================");
		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			driver.get(baseURL + "/drugs/daclatasvir/oral-tablet");
			Thread.sleep(3000);
			messages.add(" ");
			messages.add("Test URL : " + driver.getCurrentUrl());

			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on Drug article page - 1024 breakpoint"
							+ message);
				} else {
					messages.add("LB Ad is present on Drug article page - 1024 breakpoint");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Drug article page - 1024 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Drug article page - 1024 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("LB Ad is missing on Drug article page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on Drug article page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on Drug article page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on Drug article page - 1024 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on Drug article page - 1024 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on Drug article page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			// check MR2 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr2();
				if (message != null) {
					messages.add("MR2 Ad is missing on Drug article page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR2 Ad is present on Drug article page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr2"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR2 Ad is not firing on Drug article page - 1024 breakpoint ");
					}else{
						messages.add("MR2 Ad is firing on Drug article page - 1024 breakpoint ");
						
						messages.add("MR2 Ad google query id is --> "+googleQueryId);
						messages.add("MR2 Ad google item id is --> "+gogleItemId);
						messages.add("MR2 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR2 Ad is missing on Drug article page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}

			// Check Netseer Ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on Drug article page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("Netseer Ad is present on Drug article page - 1024 breakpoint!");
				}
			} catch (Exception e) {
				messages.add("Netseer Ad is missing on Drug article page - 1024 breakpoint"
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Drug Article - 1024 BP test  "
					+ e.toString());
		}
		try {
			driver = setDeviceOrientation("iPad Mini", 768, 1024);
			driver.get(baseURL + "/drugs/daclatasvir/oral-tablet");
			Thread.sleep(3000);
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on Drug article page - 768 breakpoint "
							+ message);
				} else {
					messages.add("LB Ad present on Drug article  page - 768 breakpoint");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Drug article page - 768 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Drug article page - 768 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("LB Ad is missing on Drug article page - 768 breakpoint "
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on Drug article page - 768 breakpoint! "
							+ message);
				} else {
					messages.add("MR1 Ad is present on Drug article page - 768 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-creative-id");
			
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Drug article - 768 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Drug article - 768 breakpoint ");
					
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on Drug article page - 768 breakpoint "
						+ driver.getCurrentUrl());
			}

			// Check Netseer Ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on Drug article page - 768 breakpoint! "
							+ message);
				} else {
					messages.add("Netseer Ad is present on Drug article page - 768 breakpoint!");
					
			}

			} catch (Exception e) {
				messages.add("Netseer Ad is missing on Drug article page - 768 breakpoint "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Drug article page - 768 BP test  "
					+ e.toString());
		}
		try {
			driver = setDevice("iPhone 5");
			driver.get(baseURL + "/drugs/daclatasvir/oral-tablet");
			driver.navigate().refresh();
			Thread.sleep(3000);
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on Drug article page - 320 breakpoint "
							+ message);
				} else {
					messages.add("LB Ad is present on Drug article page - 320 breakpoint");
					
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
			
				if(googleQueryId.isEmpty()){
					messages.add("LB Ad is not firing on Drug article - 320 breakpoint ");
				}else{
					messages.add("LB Ad is firing on Drug article - 320 breakpoint ");
					
					messages.add("LB Ad google query id is --> "+googleQueryId);
					messages.add("LB Ad google item id is --> "+gogleItemId);
					messages.add("LB Ad google creative id is --> "+googleCreativeId);
				}
				}
			} catch (Exception e) {
				messages.add("LB Ad is missing on Drug article page  - 320 breakpoint "
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on Drug article page   - 320 breakpoint! "
							+ message);
				} else {
					messages.add("MR1 Ad is present on  Drug article page - 320 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-creative-id");
			
				if(googleQueryId.isEmpty()){
					messages.add("MR1 Ad is not firing on Drug article - 320 breakpoint ");
				}else{
					messages.add("MR1 Ad is firing on Drug article - 320 breakpoint ");
					
					messages.add("MR1 Ad google query id is --> "+googleQueryId);
					messages.add("MR1 Ad google item id is --> "+gogleItemId);
					messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
				}
					
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on Drug article page  - 320 breakpoint "
						+ driver.getCurrentUrl());
			}

			// Check Netseer Ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on Drug article page  & it's as expected  - 320 breakpoint! "
							+ message);
				} else {
					WebElement netseerAd = driver.findElement(By
							.className("ad-pb"));
					if (!netseerAd.getCssValue("display").equalsIgnoreCase(
							"none")) {
						messages.add("Netseer Ad should not present on Drug artilce  page - 320 breakpoint!");
					} else {
						messages.add("Netseer Ad is missing on Drug article page  & it's as expected  - 320 breakpoint! ");
					}
				}
			} catch (Exception e) {

			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Drug article page - 320  BP test  "
					+ e.toString());
		}

		// -------------------------------News Landing
		// Page-------------------------------------
	messages.add("       ");

		messages.add("News Landing Page");
		messages.add("==================");
		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			driver.get(baseURL + "/health-news");
			Thread.sleep(3000);
			messages.add(" ");
			messages.add("Test URL : " + driver.getCurrentUrl());
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad should not present on News Landing page  -  1024 breakpoint!");
				} else {

					messages.add("LB Ad is missing on News landing page as it's expected-  1024 breakpoint!"
							+ message);

				}
			} catch (Exception e) {
				messages.add("LB ad is not display" + driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on News Landing page -  1024 breakpoint!"
							+ message);
				} else {
					messages.add("MR1 Ad is present on News Landing page -  1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on News Landing page - 1024 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on News Landing page - 1024 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on News Landing page -  1024 breakpoint! "
						+ driver.getCurrentUrl());
			}

			// Check Netseer Ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on News Landing page - 1024 breakpoint! "+ message);
				} else {
					messages.add("Netseer Ad is present on News Landing page - 1024 breakpoint! ");
					
				}
			} catch (Exception e) {
				messages.add("Netseer Ad is missing on News Landing page -  1024 breakpoint "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
		}
		try {
			driver = setDeviceOrientation("iPad Mini", 768, 1024);
			driver.get(baseURL + "/health-news");
			Thread.sleep(3000);
			messages.add(" ");
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on News Landing page - 768 breakpoint "
							+ message);
				} else {
					messages.add("LB Ad present on News Landing page - 768 breakpoint");
					
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Legacy TAB Article - 768 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Legacy TAB Article - 768 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
					
				}
			} catch (Exception e) {
				messages.add("LB Ad is missing on News Landing page - 768 breakpoint "
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on News Landing page - 768 breakpoint! "
							+ message);
				} else {
					messages.add("MR1 Ad is present on News Landing page - 768 breakpoint! ");
					
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on Legacy TAB Article - 768 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on Legacy TAB Article - 768 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
					
					
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on News Landing page - 768 breakpoint! "
						+ driver.getCurrentUrl());
			}

			// Check Netseer Ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on News Landing page - 768 breakpoint! "
							+ message);
				} else {
					messages.add("Netseer Ad is present on News Landing page- 768 breakpoint!");
				}
			} catch (Exception e) {
				messages.add("Netseer Ad is missing on News Landing page -  768 breakpoint "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in News Landing page -  768 BP test  "
					+ e.toString());
		}
		try {
			driver = setDevice("iPhone 5");
			driver.get(baseURL + "/health-news");
			Thread.sleep(3000);
			messages.add(" ");

			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on News Landing page - 320 breakpoint "
							+ message);
				} else {
					messages.add("LB Ad is present on News Landing page - 320 breakpoint");
					
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on Legacy TAB Article - 320 breakpoint ");
					}else{
						messages.add("LB Ad is firing on Legacy TAB Article - 320 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
					
				}
			} catch (Exception e) {
				messages.add("LB Ad is missing on News Landing page  - 320 breakpoint "
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on News Landing page - 320 breakpoint! "
							+ message);
				} else {
					messages.add("MR1 Ad is present on  News Landing page - 320 breakpoint!");
					
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on News Landing page - 320 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on News Landing page - 320 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on News Landing page - 320 breakpoint "
						+ driver.getCurrentUrl());
			}

			// Check Netseer Ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on News Landing page  & it's as expected  - 320 breakpoint! "
							+ message);
				} else {
					WebElement netseerAd = driver.findElement(By
							.className("ad-pb"));
					if (!netseerAd.getCssValue("display").equalsIgnoreCase(
							"none")) {
						messages.add("Netseer Ad should not present on News Landing page - 320 breakpoint!");
					} else {
						messages.add("Netseer Ad is missing on News Landing page & it's as expected  - 320 breakpoint! ");
					}
				}
			} catch (Exception e) {
				// tablet
			}
			driver.close();
		} catch (Exception e) {
			// tablet
		}

		// -----------------------------SxC Landing
		// Page----------------------------------
		messages.add("       ");

		messages.add("SxC Landing Page");
		messages.add("================");

		// check MR1 ad
		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			driver.get(baseURL + "/symptom-checker");
			Thread.sleep(3000);
			messages.add(" ");
			messages.add("Test URL : " + driver.getCurrentUrl());
			messages.add(" ");
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on SxC Landing page - 1024 breakpoint! "
							+ message);
				} else {
					messages.add("MR1 Ad is present on SxC Landing page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on SxC Landing page - 1024 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on SxC Landing page - 1024 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on SxC Landing page  - 1024 breakpoint "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in Legacy SxC Landing page - 1024 BP test  "
					+ e.toString());
		}
		try {
			driver = setDeviceOrientation("iPad Mini", 768, 1024);
			driver.get(baseURL + "/symptom-checker");
			Thread.sleep(3000);
			messages.add(" ");
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on SxC Landing page - 768 breakpoint! "
							+ message);
				} else {
					messages.add("MR1 Ad is present on SxC Landing page - 768 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on SxC Landing page - 768 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on SxC Landing page - 768 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
					
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on SxC Landing page  - 768 breakpoint "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in SxC Landing page  - 768 BP test  "
					+ e.toString());
		}
		try {
			driver = setDevice("iPhone 5");
			driver.get(baseURL + "/symptom-checker");
			Thread.sleep(3000);
			messages.add(" ");
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on SxC Landing page - 320 breakpoint! "
							+ message);
				} else {
					messages.add("MR1 Ad is present on SxC Landing page - 320 breakpoint!");
					
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on SxC Landing page - 320 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on SxC Landing page - 320 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on SxC Landing page  - 320 breakpoint "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in SxC Landing page  - 320  BP test  "
					+ e.toString());
		}

		// ------------------------------SxC Result
		// Page---------------------------------------
		messages.add("       ");

		messages.add("SxC Result Page");
		messages.add("===============");
		try {
			driver = setDeviceOrientation("iPad Mini", 1024, 768);
			driver.get(baseURL + "/symptom/neck-pain");
			Thread.sleep(3000);
			messages.add(" ");
			messages.add("Test URL : " + driver.getCurrentUrl());
			messages.add(" ");

			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on SxC Result Page - 1024 breakpoint "
							+ message);
				} else {
					messages.add("LB Ad is present on SxC Result Page - 1024 breakpoint");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on SxC Result Page - 1024 breakpoint ");
					}else{
						messages.add("LB Ad is firing on SxC Result Page - 1024 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("LB Ad is missing on SxC Result Page - 1024 breakpoint "
						+ driver.getCurrentUrl());
			}
			// check MR1 ad
			try {
				CheckAds MR1Ads = new CheckAds(driver);
				message = MR1Ads.checkRhsAdsMr1();
				if (message != null) {
					messages.add("MR1 Ad is missing on SxC Result Page - 1024 breakpoint! "
							+ message);
				} else {
					messages.add("MR1 Ad is present on SxC Result Page - 1024 breakpoint!");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-mr1"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("MR1 Ad is not firing on SxC Result Page - 1024 breakpoint ");
					}else{
						messages.add("MR1 Ad is firing on SxC Result Page - 1024 breakpoint ");
						
						messages.add("MR1 Ad google query id is --> "+googleQueryId);
						messages.add("MR1 Ad google item id is --> "+gogleItemId);
						messages.add("MR1 Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e) {
				messages.add("MR1 Ad is missing on SxC Result Page - 1024 breakpoint "
						+ driver.getCurrentUrl());
			}

			// Check Netseer Ad
			try {
				CheckAds netseerAds = new CheckAds(driver);
				message = netseerAds.checkNetseerAds();
				if (message != null) {
					messages.add("Netseer Ad is missing on SxC Result Page - 1024 breakpoint!"
							+ message);
				} else {
					messages.add("Netseer Ad is present on SxC Result Page - 1024 breakpoint!");
				}
			} catch (Exception e) {
				messages.add("Netseer Ad is missing on SxC Result Page - 1024 breakpoint "
						+ driver.getCurrentUrl());
			}
			driver.close();
		} catch (Exception e) {
			messages.add("Exception in SxC Result Page  - 1024 BP test  "
					+ e.toString());
		}
		try {
			// System.out.println("line1");
			driver = setDeviceOrientation("iPad Mini", 768, 1024);
			driver.get(baseURL + "/symptom/neck-pain");
			Thread.sleep(3000);
			messages.add(" ");
			// Check LB ads
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkUpperAd();
				if (message != null) {
					messages.add("LB Ad is missing on SxC Result Page - 768 breakpoint "
							+ message);
				} else {
					messages.add("LB Ad is present on SxC Result Page - 768 breakpoint ");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on SxC Result Page - 768 breakpoint ");
					}else{
						messages.add("LB Ad is firing on SxC Result Page - 768 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception ee) {
				messages.add("LB ad is not display on SxC Result Page 768 BPs "
						+ driver.getCurrentUrl());
			}
			// check MR3 ad

			try {
				List<WebElement> tag = driver.findElement(By.id("ts_result"))
				.findElements(By.className("hl-box-result-item"));
		int adscount = 0;
		if (tag.size() > 3) {
			adscount = tag.size() / 6;
		}
		System.out.println("adcount  --- " + adscount);
		int counter = 1;
		for (int i = 10; i < adscount + 10; i++) {
			try {
				WebElement mr3Ad = driver.findElement(By
						.id("gpt-ad-mr3-" + i));
				messages.add(ordinal(counter)
						+ "  SxC Result Page - 320 breakpoint");
						String googleQueryId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-google-query-id");
				String gogleItemId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-line-item-id");
				String googleCreativeId=new WebDriverWait(driver, 25)
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-creative-id");
			
				
				if(googleQueryId.isEmpty()){
					messages.add("MR3 is not fired..." + i + " on SxC Result Page " + driver.getCurrentUrl());
				}else{
					messages.add("MR3 Ad is firing on SxC Result Page - 768 breakpoint ");
					
					messages.add("MR3 Ad google query id is --> "+googleQueryId);
					messages.add("MR3 Ad google item id is --> "+gogleItemId);
					messages.add("MR3 Ad google creative id is --> "+googleCreativeId);
					
					
				}
			} catch (Exception e) {
				messages.add(ordinal(counter)
						+ "  SxC Result Page - 320 breakpoint"
						+ e.toString());
			}
			counter++;
		}
			} catch (Exception e1) {
				messages.add("MR3 ad is not display on SxC Result Page 768*1024"
						+ driver.getCurrentUrl());
			}

			
				driver.close();
		} catch (Exception e) {
			messages.add("Exception in on SxC Result Page 768*1024 BP test  "
					+ e.toString());
		}
		try {
			driver = setDevice("iPhone 5");
			driver.get(baseURL + "/symptom/neck-pain");
			Thread.sleep(3000);
			messages.add(" ");
			// check LB ad
			try {
				CheckAds Lbads = new CheckAds(driver);
				message = Lbads.checkStickyLB();
				if (message != null) {
					messages.add("LB Ad is missing on SxC Result Page - 320 breakpoint"
							+ message);
				} else {
					messages.add("LB Ad is present on SxC Result Page - 320 breakpoint");
					String googleQueryId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-google-query-id");
					String gogleItemId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-line-item-id");
					String googleCreativeId=new WebDriverWait(driver, 25)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb"))).getAttribute("data-creative-id");
				
					if(googleQueryId.isEmpty()){
						messages.add("LB Ad is not firing on SxC Result Page - 320 breakpoint ");
					}else{
						messages.add("LB Ad is firing on SxC Result Page - 320 breakpoint ");
						
						messages.add("LB Ad google query id is --> "+googleQueryId);
						messages.add("LB Ad google item id is --> "+gogleItemId);
						messages.add("LB Ad google creative id is --> "+googleCreativeId);
					}
				}
			} catch (Exception e7) {
				messages.add("LB ad is not display on SxC Result Page - 320 breakpoint"
						+ driver.getCurrentUrl());
			}
			// Check MR3 ad
			try {
				List<WebElement> tag = driver.findElement(By.id("ts_result"))
						.findElements(By.className("hl-box-result-item"));
				int adscount = 0;
				if (tag.size() > 3) {
					adscount = tag.size() / 6;
				}
				System.out.println("adcount  --- " + adscount);
				int counter = 1;
				for (int i = 10; i < adscount + 10; i++) {
					try {
						WebElement mr3Ad = driver.findElement(By
								.id("gpt-ad-mr3-" + i));
						messages.add(ordinal(counter)
								+ "  SxC Result Page - 320 breakpoint");
								String googleQueryId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-google-query-id");
						String gogleItemId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-line-item-id");
						String googleCreativeId=new WebDriverWait(driver, 25)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("hl-ad-mr3"))).getAttribute("data-creative-id");
					
						
						if(googleQueryId.isEmpty()){
							messages.add("MR3 is not fired..." + i + " on SxC Result Page " + driver.getCurrentUrl());
						}else{
							messages.add("MR3 Ad is firing on SxC Result Page - 320 breakpoint ");
							
							messages.add("MR3 Ad google query id is --> "+googleQueryId);
							messages.add("MR3 Ad google item id is --> "+gogleItemId);
							messages.add("MR3 Ad google creative id is --> "+googleCreativeId);
							
							
						}
					} catch (Exception e) {
						messages.add(ordinal(counter)
								+ "  SxC Result Page - 320 breakpoint"
								+ e.toString());
					}
					counter++;
				}

			} catch (Exception E) {
				System.out.println(E.toString());
			}
			driver.close();
		} catch (Exception e5) {
			messages.add("Exception in SxC Result Page - 320 BP test  "
					+ e5.toString());
		}
		
		createReport(messages);
	}

    public void getGoogleIds(String id,String Msg,String Msg1,List<String> messages){
    	
    	String googleQueryId=new WebDriverWait(driver, 25)
		.until(ExpectedConditions.visibilityOfElementLocated(By.id(id))).getAttribute("data-google-query-id");
		//String googleQueryId=driver.findElement(By.id(id)).getAttribute("data-google-query-id");
        String gogleItemId=new WebDriverWait(driver, 25)
		.until(ExpectedConditions.visibilityOfElementLocated(By.id(id))).getAttribute("data-line-item-id");
		String googleCreativeId=new WebDriverWait(driver, 25)
		.until(ExpectedConditions.visibilityOfElementLocated(By.id(id))).getAttribute("data-creative-id");
		
		if(googleQueryId.isEmpty()){
			messages.add(Msg+" "+ driver.getCurrentUrl());
		}else{
			messages.add(Msg1+ driver.getCurrentUrl());
			
			messages.add("LB Ad google query id is "+googleQueryId);
			messages.add("LB Ad google item id is "+gogleItemId);
			messages.add("LB Ad google creative id is "+googleCreativeId);
		}
    }
	public static WebDriver setDeviceOrientation(String deviceType, int width,
			int height) {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		// Map<String, String> mobileEmulation = new HashMap<String, String>();
		Map<String, Object> mobileEmulation = new HashMap<String, Object>();

		// mobileEmulation.put("deviceName", deviceType);
		Map<String, Object> deviceMetrics = new HashMap<String, Object>();
		// here creating the second map with key mobileEmulation
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		deviceMetrics.put("width", width);
		deviceMetrics.put("height", height);
		deviceMetrics.put("pixelRatio", 3.0);
		// setting DesiredCapabilities for chrome
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		mobileEmulation.put("deviceMetrics", deviceMetrics);
		mobileEmulation
				.put("userAgent",
						"Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25");
		WebDriver driver = new ChromeDriver(capabilities);
		return driver;

	}

	public static WebDriver setDevice(String deviceType) {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		if (deviceType.equals("")) {

		} else {
			mobileEmulation.put("deviceName", deviceType);

		}

		// here creating the second map with key mobileEmulation
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		// setting DesiredCapabilities for chrome
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

		WebDriver driver = new ChromeDriver(capabilities);
		return driver;

	}

	public static String ordinal(int i) {
		int mod100 = i % 100;
		int mod10 = i % 10;
		if (mod10 == 1 && mod100 != 11) {
			return i + "st";
		} else if (mod10 == 2 && mod100 != 12) {
			return i + "nd";
		} else if (mod10 == 3 && mod100 != 13) {
			return i + "rd";
		} else {
			return i + "th";
		}
	}
}
