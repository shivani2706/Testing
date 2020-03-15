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

public class RequestUrl {

  // 1
	public static void main(String[] args) throws Exception {
		// try {
		 DesiredCapabilities caps = DesiredCapabilities.chrome(); 

		String cur_url;


		// Specify location of Selenium ChromDriver
		System.setProperty("webdriver.chrome.driver", "C:/Users/llederman/Downloads/chromedriver.exe");

		WebDriver driver;
		
		String baseUrl = "http://sfc-stage01.healthline.com/health/abdominal-tap?biddername=sovrn&biddercpm=20";
		
		
		driver = new ChromeDriver(caps);
		
	
	
		driver.get(baseUrl);
	
		driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
	

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

