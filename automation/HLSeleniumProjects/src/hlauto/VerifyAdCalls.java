package hlauto;

import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.testng.AssertJUnit;
import org.testng.annotations.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

public class VerifyAdCalls extends hlauto.hlUtils {
	public WebDriver driver;
	static Map<String, String> env;
	public String test_environment = null;
	public String actual = null;
	static hlUtils hlu = new hlUtils();
	static int waitTime = 8000;
	public String baseUrl;
	private final static File HARDIR = new File("C:\\Users\\cchoy\\workspace\\CaptureNetworkTraffic\\");
	private final static File NEWHARDIR = new File("C:\\harFiles\\");
	private final static File SCREENSHOTSDIR = new File("C:\\screenshotsset2\\");
	static String newHarPath = "C:\\harFiles\\";
	File src = new File("C:\\testdata\\testdata.xlsx");
	FirefoxProfile profile = new FirefoxProfile();
	List <String> harFileList = new ArrayList<String>();
	
	@BeforeTest 
	public void setUp() throws IOException {
		File harExport = new File("C:\\Users\\cchoy\\workspace\\automation\\HLSeleniumProjects\\resources\\harexporttrigger-0.5.0-beta.10.xpi");
		profile.addExtension(harExport);
		profile.setPreference("devtools.netmonitor.enabled", true);
		profile.setPreference("devtools.netmonitor.har.enableAutoExportToFile", true);
		profile.setPreference("extensions.netmonitor.har.enableAutomation", true);
		profile.setPreference("extensions.netmonitor.har.contentAPIToken", "test");
		profile.setPreference("extensions.netmonitor.har.autoConnect", true);
		profile.setPreference("devtools.netmonitor.har.defaultLogDir", "C:\\Users\\cchoy\\workspace\\CaptureNetworkTraffic");
		driver = new FirefoxDriver(profile);
		// Some haus cleaning before starting.
		FileUtils.cleanDirectory(HARDIR);
		FileUtils.cleanDirectory(SCREENSHOTSDIR);
		FileUtils.cleanDirectory(NEWHARDIR);
		// Setup the Browser.
		env = System.getenv();
		test_environment = env.get("testing_environment");
		System.out.println("The test_environment is: " + test_environment);	
		baseUrl = hlu.setTestingEnv(test_environment);
		driver.manage().window().maximize();		
	}
	
	@Test (priority = 0)
	public void testTabArticle() throws InterruptedException, IOException {
		// Load the file
	    FileInputStream fis;
		fis = new FileInputStream(src);
	    // load the workbook
	    XSSFWorkbook wb = new XSSFWorkbook(fis);
	    // get the sheet which you want to modify or create
	    XSSFSheet urlslist = wb.getSheet("Sheet1");
	    // getRow specify which row we want to read and getCell which column
	    int urlslistrows = urlslist.getPhysicalNumberOfRows();
	    System.out.println ("Test urls are " + urlslistrows);
	    
		try {
			//for (int urlnum = 1; urlnum <= urlslistrows; urlnum++)
			for (int urlnum = 1; urlnum <= 1; urlnum++)
            {
                for (int count = 1; count <= 1; count++)
                {
                	driver.manage().deleteAllCookies();
                	String url=(urlslist.getRow(urlnum).getCell(0).getStringCellValue());
                	String pagename=(urlslist.getRow(urlnum).getCell(1).getStringCellValue());
                	driver.get(baseUrl + url);
                    Thread.sleep(2000); 
                    String pagetitle = driver.getTitle();
                    System.out.println ("pagetitle== " + pagetitle);
                    // Find the lb2 element.  
                    WebElement element = driver.findElement(By.id("dlb2__slot__inline"));
                   
                    Point location = element.getLocation();
                    System.out.println ("location== " + location);
              
                    int  x= ( (location.x)-150);
                    int  y = ((location.y)-350);
                    System.out.println ("location x== " + x);
                    System.out.println ("location y== " + y);  
                    ((JavascriptExecutor) driver).executeScript("scroll(" + x +","+ y +")");
                    Thread.sleep(8000); 
                    File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                    //The below method will save the screen shot in c drive with name"
                    FileUtils.copyFile(scrFile, new File("C:\\screenshotsset2\\" + pagename.trim() + "-" + count + ".png"));
                    Thread.sleep(6000); 
                    ((JavascriptExecutor)driver).executeScript("scroll(0,document.body.scrollHeight)");
                    hlu.renameHarFile(HARDIR, newHarPath + pagename.trim() + "-" + count + ".har");
                    harFileList.add(pagename.trim() + "-" + count + ".har");
                    Thread.sleep(10000); 
               }
            }
			Thread.sleep(waitTime);
		} catch (Exception e) {
			e.printStackTrace();	
		}
		wb.close();
	}
	
	@Test (priority = 1) 
	public void testParseHarFile() {
		for(String harFile : harFileList) {
			hlu.parseHarFile(harFile);
		}
	}

	@AfterTest (enabled = false)
	public void terminateBrower() {
		driver.close();
		driver.quit();
		driver = null;
	}
}