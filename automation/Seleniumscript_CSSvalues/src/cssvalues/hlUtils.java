package cssvalues;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;
import com.google.gson.JsonParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class hlUtils {
	
	public String setTestingEnv(String testing_env) {
		String baseUrl = null;
		
		switch (testing_env) {
		case "stage": case "Stage":
			baseUrl = "http://sfc-stage02.healthline.com";
			break;
		case "qa": case "QA":
			baseUrl = "http://sfc-stage01.healthline.com";
			break;
		case "Prod": case "prod":
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
	
	public String getSailThruUrl(String testing_env) {
		String sailThruUrl = "https://my.sailthru.com/login";
		return sailThruUrl;
	}
	
	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss\n");	
		Date date = new Date();
		return dateFormat.format(date);
	}
}