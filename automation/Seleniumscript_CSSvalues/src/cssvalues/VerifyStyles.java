package cssvalues;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class VerifyStyles {
	//public static WebElement navlink;
	public WebDriver driver;
	static hlUtils hlu = new hlUtils();
	Map<String, String> mobileEmulation = new HashMap<String, String>();
	Map<String, Object> chromeOptions = new HashMap<String, Object>();
	DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	Map<String, String> env;
	String articleUrl;
	String testUrl;
	String testingEnv;
	String mobileDevice;
	String tablet;
	
	
	@BeforeTest
	public void setUp() throws IOException {
		env = System.getenv();
		System.setProperty("webdriver.chrome.driver", "c:\\Chromedriver\\chromedriver.exe"); 
		chromeOptions.put("mobileEmulation", mobileEmulation);
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		//WebDriver driver = new ChromeDriver(capabilities);
		testUrl = env.get("testing_environment");
		articleUrl = env.get("article_url");
		testingEnv = hlu.setTestingEnv(testUrl);
		mobileDevice = env.get("mobile_device");
		tablet = env.get("tablet");
		System.out.print("Test Starting Teim: " + hlu.getDate());
		System.out.println("Now testing article: " + testingEnv + articleUrl);
	}
	

	@Test (priority=0)
	public void getCSSStyles() throws Exception {

		//List of devices to run the test on
		String [] devices = {"desktop", mobileDevice, tablet};
		int devicecount=0;
				
		for (devicecount=0; devicecount < devices.length ; devicecount++) {	  
			switch (devicecount) {
			case 0:   driver = new ChromeDriver();
			break;
			case 1 :  mobileEmulation.put("deviceName", devices[devicecount]);
			driver = new ChromeDriver(capabilities);
			break;
			case 2:  mobileEmulation.put("deviceName", devices[devicecount]);
			driver = new ChromeDriver(capabilities);
			break;  }
			
			Reporter.log("DEVICE="+devices[devicecount] + '\n');
			System.out.println("DEVICE="+devices[devicecount] + '\n');
			driver.manage().window().maximize();
			driver.get(testingEnv + articleUrl);
			((JavascriptExecutor)driver).executeScript("scroll(0,document.body.scrollHeight)");
			Thread.sleep(3000);
			
			//Footer Elements from web page
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("Hamburger menu", "nav__control-container");
			hm.put("Header Newsletter", "newsletter--header-link");
			hm.put("Header Logo", "header__logo");
			hm.put("Heading H1", "article--content-title");
			hm.put("Heading H2", "content_head");
			hm.put("Newsletter footer", "newsletter-signup__header");
			hm.put("Newsletter input box", "newsletter-signup__input");
			hm.put("Newsletter submit btn", "newsletter-signup__submit");
			hm.put("Copyright text", "footer__copyright");
			
			hm.forEach((k,v)-> {

			WebElement	navlink=driver.findElement(By.className(v)); 
			System.out.println("Styles for "+ k +" are ------------------------- ");
			Reporter.log("Styles for "+ k +" are ------------------------- ");
			String navlinkfontsize= navlink.getCssValue("font-size");
			System.out.println("Font Size -> "+navlinkfontsize);
			Reporter.log("Font Size -> "+navlinkfontsize);

			String navfontColor= navlink.getCssValue("color");
			System.out.print("RGb Font Color -> "+navfontColor + " ; ");
			Reporter.log("RGb Font Color -> "+navfontColor + " ; ");
			String[] hexValue = navfontColor.replace("rgba(", "").replace(")", "").split(",");

			int hexValue1=Integer.parseInt(hexValue[0]);
			hexValue[1] = hexValue[1].trim();
			int hexValue2=Integer.parseInt(hexValue[1]);
			hexValue[2] = hexValue[2].trim();
			int hexValue3=Integer.parseInt(hexValue[2]);

			String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
			System.out.println("Font Color -> "+actualColor);
			Reporter.log("Font Color -> "+actualColor);

			String navfontFamily= navlink.getCssValue("font-family");
			System.out.println("Font Family List -> "+navfontFamily);
			Reporter.log("Font Family List -> "+navfontFamily);
			String [] strarr = navfontFamily.split(",", 0);
			System.out.println("Used Font Family -> "+strarr[0]);
			Reporter.log("Used Font Family -> "+strarr[0]);


			String navfontweight= navlink.getCssValue("font-weight");
			System.out.print("font-weight -> "+navfontweight + " ; ");
			Reporter.log("font-weight -> "+navfontweight + " ; ");


			String navheight= navlink.getCssValue("height");
			System.out.print("height -> "+navheight+ " ; ");
			Reporter.log("height -> "+navheight+ " ; ");
			//String navminwidth= navlink.getCssValue("min-width");
			//System.out.println("min-width -> "+navminwidth);

			String navpadding= navlink.getCssValue("padding");
			System.out.println("padding -> "+navpadding);
			Reporter.log("padding -> "+navpadding);

			String navlineheight= navlink.getCssValue("line-height");
			System.out.println("Line-height -> "+navlineheight);
			Reporter.log("Line-height -> "+navlineheight);
					
			String navfonttxtAlign= navlink.getCssValue("text-align");
			System.out.println("Font Text Alignment -> "+navfonttxtAlign);
			Reporter.log("Font Text Alignment -> "+navfonttxtAlign);

			String navbackgroundcolor= navlink.getCssValue("background-color");
			System.out.println("background color -> "+navbackgroundcolor);
			Reporter.log("background color -> "+navbackgroundcolor);
			System.out.println("\n");
			Reporter.log("\n");
			driver.close();					
			});

			Reporter.log("----------------------------------------------------------------------------------------------------");
		}	
	}

	@AfterTest (alwaysRun=true)
	protected void tearDown() {
		System.out.println("Test Ending Time: " + hlu.getDate());
		driver.quit();
		driver = null;
	}
}
