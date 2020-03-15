package hlauto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.umass.cs.benchlab.har.*;
import edu.umass.cs.benchlab.har.tools.HarFileReader;
import edu.umass.cs.benchlab.har.tools.HarFileWriter;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Reporter;
import com.google.gson.JsonParseException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.MetaTag;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class hlUtils {
	static boolean debug = false;
	
	public void implicit_sleep(WebDriver driver, int timeout) {
		driver.manage().timeouts().implicitlyWait(timeout,TimeUnit.SECONDS);
	}
	public void clearDriver(WebDriver driver) {
		driver.manage().deleteAllCookies();
	}
	
	public void maximizeBrowser(WebDriver driver) {
		driver.manage().window().maximize();
	}
	
	public void openNewTab(WebDriver driver) {
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
	}
	
	public String setTestingEnv(String testing_env) {
		String baseUrl = null;
		
		switch (testing_env.toLowerCase()) {
		case "qa": case "stage02":
			baseUrl = "http://sfc-stage02.healthline.com";
			break;
		case "stage": case "stage01":
			baseUrl = "http://sfc-stage01.healthline.com";
			break;
		case "prod": 
			baseUrl = "http://www.healthline.com";
			break;
		case "sfc-web01":
			baseUrl = "http://sfc-web01.healthline.com";
			break;
		case "njc-web01":
			baseUrl = "http://njc-web01.healthline.com";
			break;
		}
		System.out.println("The baseUrl is: " + baseUrl);
		return baseUrl;
	}
	
	public void updateHostFile(String serverEntry) {
		// Update the hosts file with the testing ip
		String hostsPath = "C:\\Windows\\System32\\drivers\\etc\\";
		String domainUrl = "www.healthline.com";
		
		try{
	        String entry = serverEntry + "\t" + domainUrl;
	        File file = new File(hostsPath + "hosts");

	        FileWriter fw = new FileWriter(file.getAbsolutePath());
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(entry);
	        bw.close();

	        System.out.println("Done");
	    }catch(IOException e){
	        e.printStackTrace();
	    }
	}
	
	public void renameHarFile(File path, String newName) {
		// Get the name of the existing .har file.
		File folder = path;
		File [] fileList = folder.listFiles();
		for (File file : fileList) {
			if(debug) {
				System.out.println("Filename is: " + file);
			}
			// Rename the .har file and move it (newName includes the path).
			file.renameTo(new File(newName));
		}
	}
	
	public String getSailThruUrl(String testing_env) {
		String sailThruUrl = "https://my.sailthru.com/login";
		return sailThruUrl;
	}
	
	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss\n");	
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public List<String> getLinksOnPage(final String url) {
	    Parser htmlParser = null;
		try {
			htmlParser = new Parser(url);
		} catch (ParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    final List<String> result = new LinkedList<String>();

	    try {
	        final NodeList tagNodeList = htmlParser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
	        for (int j = 0; j < tagNodeList.size(); j++) {
	            final LinkTag loopLink = (LinkTag) tagNodeList.elementAt(j);
	            final String loopLinkStr = loopLink.getLink();
	            result.add(loopLinkStr);
	        }
	    } catch (ParserException e) {
	        e.printStackTrace(); // TODO handle error
	    }

	    return result;
	}
	
	public void patternMatchServer(String serverName, String htmlFile) {
		int patternCounter = 0;
		String pattern = serverName + ".\\w+.\\w+";
		
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(htmlFile);
		System.out.println("Now looking through html source for: " + serverName + "...");
		while(m.find()) {
			System.out.println("Found the entry: " + serverName);
			patternCounter++;
		}
		System.out.println("Now evaluating the search results...");
		switch(patternCounter) {
		case 0:
			System.out.println("No Match was Found");
			break;
		case 1:
			System.out.println("Static Resource Test passed for server: " + serverName);
			break;
		default:
			System.out.println("Static Resource Test failed for server: " + serverName);
			break;	
		}
	}
	
	public void parseHarFile(String fileName) {
		boolean showHar = false;
		boolean showEntries = false;
        File f = new File(fileName);
        HarFileReader r = new HarFileReader();
        
        try {
        	System.out.println("Reading file: " + fileName);
        	System.out.println("================================================");
        	// All violations of the specification generate warnings
        	List<HarWarning> warnings = new ArrayList<HarWarning>();
        	HarLog log = r.readHarFile(f, warnings);
        	if(debug) {
        		for (HarWarning w : warnings) {
        			System.out.println("File: " + fileName + " - Warning: " + w);
        		}
        	}
        	if(debug && showHar) {
        		System.out.println("Har file contents: " + log);
        	}
        	// Access all elements as objects
        	HarBrowser browser = log.getBrowser();
        	System.out.println("Browser: " + browser);
        	// Get the Article’s url.
        	// Get the Entries value (An array of json).
        	HarEntries entries = log.getEntries();
        	if(debug && showEntries) {
        		System.out.println("Entries are: " + entries);
        	}
        	List<HarEntry> hentry = entries.getEntries();   
        	// Parse the Har Entry List.
        	parseHarEntryList(hentry);
        }
        catch (JsonParseException e)
        {
          e.printStackTrace();
          System.out.println("Parsing error during test");
        }
        catch (IOException e)
        {
          e.printStackTrace();
          System.out.println("IO exception during test");
        }
    }
    
    public void parseHarEntryList(List<HarEntry> entries) {
        /* From the Har Entries List (which is quite large
         * get the list of queryStrings (which has the information
         * we’re most interested in. Then we parse each param name
         * and values using a switch to output our information.
         */
        String searchString1 = "prev_scp";
        String searchString2 = "tid";
        String searchString3 = "gtm";
        List <String> initialTIDList = new ArrayList<String>();
        List <String> initialGTMList = new ArrayList<String>();
        List <HarQueryString> prevScpList = new ArrayList<HarQueryString>();
        int lastIndex;
        String paramKey;
        String paramValue;
        
        for (HarEntry entry : entries) { 
            HarQueryString qs = entry.getRequest().getQueryString();
            if(debug) {
            	System.out.println(qs);
            }
            // Find the queryString containing params we’re interested in.
            if (qs.toString().contains(searchString1) || qs.toString().contains(searchString2) || qs.toString().contains(searchString3)) {
                prevScpList.add(qs);
            }
        }
        // System.out.println(“prevScpArray: ” + prevScpArray);
        for (HarQueryString params : prevScpList) {
            lastIndex = params.getQueryParams().size();
            for(int i = 0; i < lastIndex; i++) {
                paramKey = params.getQueryParams().get(i).getName();
                paramValue = params.getQueryParams().get(i).getValue();
                switch(paramKey) {
                case "tid": 
                	if(!initialTIDList.contains(paramValue)) {
                		System.out.println(paramKey + " : " + paramValue);
                		System.out.println("");
                		initialTIDList.add(paramValue);
                	}
                	break;
                case "gtm": 
                	if(!initialGTMList.contains(paramValue)) {
                    	System.out.println(paramKey + " : " + paramValue);
                    	System.out.println("");   
                    	initialGTMList.add(paramValue);
                	}
                    break;
                case "correlator": case "prev_iu_szs": case "prev_scp":
                    System.out.println(paramKey + " : " + paramValue);
                    System.out.println("");
                    break;
                }
            }
        }   
    }
    
    public void setDevices(String article, WebDriver driver, String mobileDevice, String tablet) throws InterruptedException {
    	Map<String, String> mobileEmulation = new HashMap<String, String>();
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		//List of devices to run the test on
		String [] devices = {"desktop", mobileDevice, tablet};
		int devicecount=0;
				
		for (devicecount = 0; devicecount < devices.length ; devicecount++) {	  
			switch (devicecount) {
			case 0:  
				driver = new ChromeDriver();
				break;
			case 1 : 
				mobileEmulation.put("deviceName", devices[devicecount]);
				driver = new ChromeDriver(capabilities);
				break;
			case 2:  
				mobileEmulation.put("deviceName", devices[devicecount]);
				driver = new ChromeDriver(capabilities);
				break;  
			}
			
			Reporter.log("DEVICE =" + devices[devicecount] + '\n');
			System.out.println("DEVICE =" + devices[devicecount] + '\n');
			driver.manage().window().maximize();
			driver.get(article);
			((JavascriptExecutor)driver).executeScript("scroll(0,document.body.scrollHeight)");
			Thread.sleep(3000);
		}
    }
    
    public HashMap<String, String> setHashMap(String[][] keyValues) {
    	// Pass in a two dimensional list of strings for the keys and values.
    	// ie: ["key1", "value1"], ["key2", "value2"]...
    	HashMap<String, String> hm = new HashMap<String, String>();
    	for(int i = 0; i < keyValues.length; i++) {
    		hm.put(keyValues[i][0], keyValues[i][1]);    	
    	}
    	return hm;
    } 
    
    public void getCSSElement(WebDriver driver, String element, String v) {
    	WebElement navlink = driver.findElement(By.className(v));
    	switch(element) {
    	case "color": 
    		String navfontColor = navlink.getCssValue("color");
			System.out.print("RGb Font Color -> " + navfontColor + " ; ");
			Reporter.log("RGb Font Color -> " + navfontColor + " ; ");
			String[] hexValue = navfontColor.replace("rgba(", "").replace(")", "").split(",");

			int hexValue1 = Integer.parseInt(hexValue[0]);
			hexValue[1] = hexValue[1].trim();
			int hexValue2 = Integer.parseInt(hexValue[1]);
			hexValue[2] = hexValue[2].trim();
			int hexValue3 = Integer.parseInt(hexValue[2]);

			String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
			System.out.println("Font Color -> " + actualColor);
			Reporter.log("Font Color -> " + actualColor);
    	default:
    		String CSSValue = navlink.getCssValue(element); 
    		System.out.println(element + " -> " + CSSValue);
    		Reporter.log(element + " -> " + CSSValue);
    	}
    }
    
/*    public void verifyCSSValue(String uiItem, WebElement uiElement, String expected) {
    	String CSSValue = (uiElement.getCssValue(uiItem));
    	if (AssertJUnit.assertEquals(CSSValue, expected)) {
    		System.out.println("UI");
    	}
    } */
	
	public void getScreenShot(WebDriver driver, String path, String filename) throws IOException {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //The below method will save the screen shot in c drive with name"
        FileUtils.copyFile(scrFile, new File(path + filename.trim() + ".png"));
	}
}