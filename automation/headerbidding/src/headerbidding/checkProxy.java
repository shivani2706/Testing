package headerbidding;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
//import org.testng.collections.CollectionUtils;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import net.lightbody.bmp.*;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.*;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.proxy.jetty.http.HttpRequest;
import net.lightbody.bmp.proxy.jetty.http.HttpResponse;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;


public class checkProxy {

  // 1
	public static void main(String[] args) throws Exception {
		// try {
		 DesiredCapabilities caps = DesiredCapabilities.chrome(); 
		
		 BrowserMobProxy bmproxy = new BrowserMobProxyServer();
		 bmproxy.start();
		 System.out.println("bmproxy= " + bmproxy);
		    // get the JVM-assigned port and get to work!
		 int port = bmproxy.getPort();		 
             //System.out.println(port);
         String proxyString = "--proxy-server=http://localhost:" + port;
         //System.out.println(proxyString);
		 caps.setCapability("chrome.switches", Arrays.asList(proxyString));
		  // bmproxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
		 bmproxy.enableHarCaptureTypes(CaptureType.RESPONSE_CONTENT); 

         Proxy seleniumProxy = ClientUtil.createSeleniumProxy(bmproxy);
         caps.setCapability(CapabilityType.PROXY, seleniumProxy);

		//String cur_url;
		ChromeOptions options = new ChromeOptions();

		//options.addArguments("-incognito");
		caps.setCapability(ChromeOptions.CAPABILITY, options);

		// Specify location of Selenium ChromDriver
		System.setProperty("webdriver.chrome.driver", "C:/Users/llederman/Downloads/chromedriver.exe");

		WebDriver driver;
		System.out.println("Before loop");
		//String[] bidders = { "rubicon",  "sonobi", "appnexus", "yieldbot" };
		String[] bidders = { "sonobi" };
		driver = new ChromeDriver(caps);
		for (String bidder: bidders) {
		
			//String baseUrl = "http://sfc-stage02.healthline.com/health/bad-breath?biddername=sonobi&biddercpm=20";
		/*	String baseUrl = String.format(
					"http://www.healthline.com/health/non-surgical-treatment-for-osteoarthritis-knee-pain?biddername=%s&biddercpm=20",
					bidder); */
					
			String baseUrl = String.format(
				//	"http://www.healthline.com/health/abdominal-tap?biddername=%s&biddercpm=20", bidder);
			        "http://www.healthline.com/health/copd/breathlessness-explained?forcebidder=sonobi" );  
			System.out.println("baseUrl = " + baseUrl);

			//System.out.println("Chrome is up");
		    
		//	driver.manage().window().maximize();
		//	driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
	
			bmproxy.blacklistRequests("https://www\\.honcode\\.ch/.*", 200);
			
			ArrayList <String> wl = new ArrayList <String>();
			wl.add("https://securepubads.*");
			wl.add(".*healthline.*");
			
//			bmproxy.whitelistRequests(wl, 200);
			Set <CaptureType> captureTypes = new HashSet <CaptureType>(); 
			captureTypes.add(CaptureType.REQUEST_HEADERS);
			captureTypes.add(CaptureType.REQUEST_COOKIES);
		    bmproxy.newHar();
		    System.out.println("step after bmproxy.newHar();");
		    
		    driver.get(baseUrl);
		  
		/*	try{
				driver.get(baseUrl);
			} catch (TimeoutException e) {
				System.out.println("Webpage timed out. Continuing anyway...");
			} */
			
			new PerformanceTiming((JavascriptExecutor) driver, bmproxy.getHar());
		    System.out.println("step after calling PerformanceTiming");
			//System.out.println("Done Perofrmance");
			// get the HAR data
		    FileOutputStream fos = new FileOutputStream("C:\\Temp\\output_6.txt");
		    bmproxy.getHar().writeTo(fos);      
		 
		    System.out.println("txt file is ready");
		    
	    	 for (HarEntry entry : bmproxy.getHar().getLog().getEntries()) {
	    		//  System.out.println("inside of first for loop");
		    		    		 
	    		//System.out.println("request url: " + entry.getRequest().getUrl() );
	    		 
	    		if (entry.getRequest().getUrl().contains("doubleclick")) {
	    			System.out.println("qs: " + entry.getRequest().getQueryString());
	    			System.out.println("redir: " + entry.getResponse().getRedirectURL());
	    			
	    		}
		    	List<HarNameValuePair> nameValList = (entry.getRequest().getQueryString());
		    	
		    	for(HarNameValuePair pair : nameValList) {
		    		//  System.out.println("inside of second for loop");
		    		
		    		//System.out.println(pair.getName() + " -- " + pair.getValue());
		    		
		    		if(pair.getName().equals("prev_scp")) {
		    			
		    		
		    			String prev_scp = pair.getValue();
		    			System.out.println("Found parameter 'prev_scp': " + prev_scp);
		    			
		    			List<String> pos = new ArrayList<String>();
		    			if(prev_scp.contains(bidder)) {
		    				
		    				System.out.println(bidder + " won");
		    					    				
		    				if(prev_scp.contains("pos=lb")) {
		    					pos.add("lb");
		    				}
		    				if(prev_scp.contains("pos=mr1")) {
		    					pos.add("mr1");
		    				}
		    				/*
		    				if(prev_scp.contains("pos=mr2")) {
		    					pos.add("mr2");
		    				}
		    				if(prev_scp.contains("pos=wsl")) {
		    					pos.add("wsl");
		    				}
		    				*/
		    				
		    				if(!pos.isEmpty()) {
		    					System.out.println(bidder + " won the following Ad positions " + pos);
		    				} else {
		    					System.out.println(bidder + " didn't win any pos");
		    				}
		    				System.out.println( "Test with "+ bidder + " PASSED");
		    			}else {
		    				System.out.println( "Test with "+ bidder + " FAILED");	
		    		     }
		    			
		    			for (String bidder2: bidders) {
		    				if(bidder == bidder2) {
		    					continue;
		    				}
		    				
		    				if(prev_scp.contains(bidder2)) {
			    				System.out.println("FAIL: Contains wrong bidder: " + bidder2 + " found in " + bidder);
			    			}
		    			}
		    						    		
		    		}
		    	}
		    }
		    

		}
        System.out.println("Script done");
	    //driver.close();
	    bmproxy.stop();
	    
	    
	}
}

