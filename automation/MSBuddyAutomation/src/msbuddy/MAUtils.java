package msbuddy;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Dimension;

public class MAUtils {
	public void sleep(int time_ms) {
		try {
			Thread.sleep(time_ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getMonthAndDate() {
		Format formatter = new SimpleDateFormat("MMMM d"); 
	    String date = formatter.format(new Date());
	    return date;
	}
	
	public void log(String msg) {
		System.out.println(msg);
	}
	
	
/*	public void swipe(IOSDriver<MobileElement> driver,String direction) throws InterruptedException {
		Dimension size;
		
		//Get the size of screen.
		size = driver.manage().window().getSize();
		System.out.println(size);
		  
		//Find swipe start and end point from screen's with and height.
		//Find startx point which is at right side of screen.
		int startx = (int) (size.width * 0.70);
		//Find endx point which is at left side of screen.
		int endx = (int) (size.width * 0.30);
		  
		//Find vertical point where you wants to swipe. It is in middle of screen height.
		int starty = size.height / 2;
		System.out.println("startx = " + startx + " ,endx = " + endx + " , starty = " + starty);
		
		switch(direction) {
		case "Right":
			//Swipe from Right to Left.
			driver.swipe(startx, starty, endx, starty, 3000);
			Thread.sleep(2000);
			break;
		case "Left":
			//Swipe from Left to Right.
			driver.swipe(endx, starty, startx, starty, 3000);
			Thread.sleep(2000);
			break;
		}
	} */
}
