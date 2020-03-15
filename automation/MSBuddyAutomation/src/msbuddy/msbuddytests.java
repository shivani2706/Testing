package msbuddy;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.AssertJUnit;
import org.testng.annotations.*;


public class msbuddytests {
	
	AndroidDriver<AndroidElement> dr;
	static MAUtils mau = new MAUtils();
	
	@Test (priority = 0)
	public void testInstallAppLaunch() throws MalformedURLException, InterruptedException {
		
		File app = new File(System.getProperty("user.dir") + "/apks/android-debug.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		// capabilities.setCapability("deviceName","motorola-xt1575-TA39400SMM");
		capabilities.setCapability("deviceName","samsung-sm_g930v-22759c9f");
		capabilities.setCapability("platformVersion", "6.0.1");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("app", app.getAbsolutePath());
		dr = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		dr.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		mau.sleep(5000);
	}
	
	@SuppressWarnings("deprecation")
	@Test (priority = 1)
	public void testWelcomeScreens() {
		// Should be on the Welcome screen.
		
		// Get the width and height of the mobile screen.
		int x_max = dr.manage().window().getSize().width;
		int y_max = dr.manage().window().getSize().height;
		
		dr.swipe(x_max - 75, y_max/2, 5, y_max/2, 5000);
		mau.sleep(2000);
		dr.tap(1, 894, 2147, 1000);
		mau.sleep(2000);
	}
	
	@SuppressWarnings("deprecation")
	@Test (priority = 2)
	public void testEmailLogin() {
		// Login with an existing email address and password
		String email = "maggie@healthline.com";
		String password = "h3althlin3";
		List<AndroidElement> textFields = dr.findElementsByAndroidUIAutomator("UiSelector().className(\"android.widget.EditText\")");
        // System.out.println("Total - " + textFields);
        textFields.get(0).sendKeys(email);
		textFields.get(1).sendKeys(password);
		dr.tap(1, 79, 936, 2000);
		mau.sleep(5000);
	}
	
	@Test (priority = 3)
	public void testTodaysMatch() {
		String todayDate = mau.getMonthAndDate();
		// Verify the Today's Match Screen.
		List<AndroidElement> views = dr.findElementsByAndroidUIAutomator("UiSelector().className(\"android.view.View\")");
		System.out.println("Total Views: " + views);
		AssertJUnit.assertEquals(todayDate, views.get(0).getText());
	}
	
	@SuppressWarnings("deprecation")
	@Test (enabled = false)
	public void testLogout() {
		int y_max = dr.manage().window().getSize().height;
		
		// Tap the Hamburger menu.
		dr.tap(1, 19, 152, 2000);
		mau.sleep(5000);
		// Tap the Logout link.
		dr.tap(1, 75, y_max/2 - 20, 1000);
		mau.sleep(5000);
	}
	
	@AfterTest
	public void quit() {
		dr.quit();
	}
	
	
	
}