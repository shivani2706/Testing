// This scrip is created by Luda Lederman to verify that static resources point to www on all sfc- and njc- servers //

package releasesourceresource;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SourceResourceLinks {
      
	public static String sourceText;
	public static int counter = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		WebDriver driver;
		//String baseUrl = "http://www.healthline.com/health/do-you-need-take-vitamins";
		
	    driver = new FirefoxDriver();
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	    
	   
	    String[] listUrls = {"http://sfc-web01.healthline.com", "http://sfc-web02.healthline.com", "http://sfc-web03.healthline.com", "http://sfc-web04.healthline.com", "http://sfc-web05.healthline.com", "http://sfc-web07.healthline.com", "http://sfc-web08.healthline.com", "http://sfc-web09.healthline.com", "http://njc-web01.healthline.com", "http://njc-web02.healthline.com", "http://njc-web03.healthline.com", "http://njc-web04.healthline.com", "http://njc-web05.healthline.com", "http://njc-web06.healthline.com", "http://njc-web07.healthline.com", "http://njc-web08.healthline.com"}; 	        
   
	    for(int i =0; i < listUrls.length; i++)
	    {
	    	  driver.get(listUrls[i]);
	    	  sourceText = driver.getPageSource(); 
	    	  
	    	  if( contains( sourceText, "sfc" ) || contains( sourceText, "njc" ) ) {
	    		  counter = counter + 1;
	    		  System.out.println( "Found " + "local server " + " within " + "view source" + "." );
	    	  }else{
	    		  System.out.println( "Not found " + "local server " + " within " + "view source of " + listUrls[i] + "." );
	    		}
	    	  
	//    	System.out.println(listUrls[i]);
	    }
	  
  	  if (counter >= 1) {
		  System.out.println("Test Failed");
	  }else{
		  System.out.println("Test Passed");
	  }     
		driver.quit();

	}

	private static boolean contains(String sourceText2, String string) {
		// TODO Auto-generated method stub
		return false;
	}

}
