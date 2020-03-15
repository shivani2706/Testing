package headerbidding;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.Arrays;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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

public class checkNetwork {

  // 1
	public static void main(String[] args) throws Exception {
		// try {
		 DesiredCapabilities caps = DesiredCapabilities.chrome(); 
		 caps.setCapability("chrome.switches", Arrays.asList("--proxy-server=127.0.0.1:8888")); 

	//	Proxy proxy = new Proxy();
		
		String cur_url;


		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.ALL);
		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

		Map<String, Object> prefs = new HashMap<>();
		prefs.put("enableNetwork", true);
		prefs.put("enablePage", true);

		
		prefs.put("traceCategories", "browser,devtools.timeline,devtools"); // comma-separated trace categories
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("perfLoggingPrefs", prefs);
		options.addArguments("-incognito");
		caps.setCapability(ChromeOptions.CAPABILITY, options);

		//caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
		//caps.setCapability(CapabilityType.PROXY, proxy);

		// Specify location of Selenium ChromDriver
		System.setProperty("webdriver.chrome.driver", "C:/Users/llederman/Downloads/chromedriver.exe");

		WebDriver driver;
		
		String baseUrl = "http://sfc-stage02.healthline.com/health/dental-and-oral-health/mountain-dew-mouth?pbjs_debug=true";
		
		
		driver = new ChromeDriver(caps);
		
	    
		driver.manage().window().maximize();

	
		driver.get(baseUrl);
		LogEntries logEntries = driver.manage().logs().get(LogType.PERFORMANCE);
		List<LogEntry> logs = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
				
	 System.out.println("logs = " + logs);
	 
		driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
		
	//	cur_url = driver.getCurrentUrl();

		
		for (LogEntry entry : logs) {
			System.out.println("entry = " + entry.getMessage().toString());

			if (entry.getMessage().contains("prev_scp") && entry.getMessage().contains("\"status\":200")) {
				System.out.println("Enter into if ststement");
			//if (entry.getMessage().contains("prev_scp")) {
			////if (entry.getMessage().contains("pubads")) {
				System.out.println("entry prevscp = " + entry.getMessage().toString());
				 //System.out.println("entry = " + entry.toString());
				 System.out.println("Entered into entry.getMessage()");
			    	String scp = getPrevScpData(entry.toString());
							
				//System.out.println("Result: " + scp);

			/*	String out[] = scp.split("%7Cpos%3D");
				
				for(int i=0; i<out.length; i++) {
				//	byte[] outBytes = out[i].getBytes("UTF-8");
				//	System.out.println("ad=" + new String(outBytes, Charset.forName("UTF-8")));
					if(out[i].isEmpty())
						continue;
				    String adName;
				    if(out[i].startsWith("lb")) {
				    	adName = "lb";
				    }
				    else {
					    adName = out[i].substring(0, 3);
				    }
					if(out[i].contains("rubicon")) {						
						System.out.println("Advertisement " + adName + " is bidding for rubicon" );
					}
					else if(out[i].contains("sonobi")) {
						System.out.println("Advertisement " + adName + " is bidding for sonobi" );
					}
					else if(out[i].contains("appnexus")) {
						System.out.println("Advertisement " + adName + " is bidding for appnexus" );
					}
					else {
						System.out.println("Advertisement " + adName + " - no any bidding" );
					}
				} */

			}

		}

		driver.close();

	}

	// 2
	private static String getPrevScpData(String str) throws Exception {
		// if (StringUtils.isBlank(str))
		// return "";
		String retVal = null;
		str = str.trim();
		URI uri = new URI("https", str, null);
		URL url = uri.toURL();
		// System.out.println("url = " + url.toString());

		Map<String, List<String>> map = splitQuery(url);
		// System.out.println((map == null) ? "true" : "false");
		if (MapUtils.isNotEmpty(map) && CollectionUtils.isNotEmpty(map.get("prev_scp"))) {
			retVal = map.get("prev_scp").get(0);
			System.out.println("retVal_1 = " +retVal);

			retVal = URLDecoder.decode(retVal,"UTF-8");
			
			System.out.println("retVal_2 = " + retVal);
		}
		return retVal;
	}

	// 3
	private static Map<String, List<String>> splitQuery(URL url)
			throws UnsupportedEncodingException, MalformedURLException, URISyntaxException {

		final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
		final String[] pairs = url.getQuery().split("&");
		for (String pair : pairs) {
			final int idx = pair.indexOf("=");
			final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
			if (!query_pairs.containsKey(key)) {
				query_pairs.put(key, new LinkedList<String>());
			}
			final String value = idx > 0 && pair.length() > idx + 1
					? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
			query_pairs.get(key).add(value);
		}
		return query_pairs;
	}
}

