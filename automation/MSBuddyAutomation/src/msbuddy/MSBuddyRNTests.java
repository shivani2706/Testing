/* Author: chaz 
 * Date: 05.30.17
 * Automation for MS Buddy react-native, currently works for iOS.
 * Must use Accessibility Labels, until Appium catches up and support Resource IDs from rn.
 * Base Stage: A fresh install of MS Buddy app.
 */

package msbuddy;

import static org.testng.AssertJUnit.assertTrue;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.testng.annotations.BeforeTest;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;


public class MSBuddyRNTests {
	private static IOSDriver<MobileElement> wd;
	static ProcessBuilder processBuilder;
	static Process process;
	static MAUtils mau = new MAUtils();
	static final LinkedHashMap<String, String> msScreens = createMap();
	private static LinkedHashMap<String, String> createMap() {
		LinkedHashMap<String, String> tmpMap = new LinkedHashMap<String, String>();
		tmpMap.put("Messages", "tabs.messages");
		tmpMap.put("Discover", "tabs.discover");
		tmpMap.put("Your Profile", "tabs.profile");
		tmpMap.put("Today's Match", "tabs.match");
		return tmpMap;	
	}
	
	@BeforeTest
	public void setup() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, "1.7.1");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.0.1");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone X");
		capabilities.setCapability(MobileCapabilityType.UDID, "E6C689DD-946D-49F2-B473-00D9CEC5D07C");
		capabilities.setCapability(MobileCapabilityType.APP, "/Users/cchoy/Documents/workspace/buddy-front/msbuddy/ios/build/Build/Products/Debug-iphonesimulator/MSBuddy.app");
		capabilities.setCapability("bundleId", "com.healthline.msbuddy-qa");
		capabilities.setCapability(IOSMobileCapabilityType.LAUNCH_TIMEOUT, 500000);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
		capabilities.setCapability("autoAcceptAlerts", true);
		wd = new IOSDriver<>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
		wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}
	
	@Test (priority = 0, enabled = true)
	public void testWelcomeScreen() {
		final String welcomeTxt = "Join our growing community of over 3,500";
		final String tosPpTxt = "By continuing you agree to the Terms of use and Privacy policy.";
		
		mau.log("[INFO]: Now testing the Welcome Screen...");
		// Verify that all 3 buttons: Email, Facebook and Google (on Android) are displayed.
		AssertJUnit.assertTrue(wd.findElementByAccessibilityId("sign-in.facebook-btn").isDisplayed());
		AssertJUnit.assertTrue(wd.findElementByAccessibilityId("welcome.email-sign-up-btn").isDisplayed());
		AssertJUnit.assertTrue(wd.findElementByAccessibilityId("onboarding.sign-in-btn").isDisplayed());
		AssertJUnit.assertTrue(wd.findElementByName(tosPpTxt).isDisplayed());	
	}
	
	@Test (priority = 1, enabled = false)
	public void testEmailSignUp() {
		mau.log("[INFO]: Now testing the Email Signup Screen...");
		// Tap on the Email Sign Up button.
		wd.findElementByAccessibilityId("welcome.email-sign-up-btn").click();
		// Verify the 3 textfields: Email, Password and Password Confirm.
		AssertJUnit.assertTrue(wd.findElementByAccessibilityId("sign-up.email-txt-fld").isDisplayed());
		AssertJUnit.assertTrue(wd.findElementByName("Password (8+ characters").isDisplayed());
		AssertJUnit.assertTrue(wd.findElementByAccessibilityId("sign-up.reenterpassword-txt-fld").isDisplayed());
		wd.findElementByName("Back").click();
	}
	
	@Test (priority = 2, enabled = true)
	public void testFacebookSignIn() throws InterruptedException {
		mau.log("[INFO]: Now testing the Facebook Sign In...");
		// Tap on the Facebook button.
		wd.findElementByAccessibilityId("sign-in.facebook-btn").click();
		mau.sleep(5000);
		wd.findElementByName("Email or Phone").sendKeys("cchoy@healthline.com");
		mau.sleep(2000);
		wd.findElementByName("Facebook Password").sendKeys("H3althlin3");
		wd.findElementByAccessibilityId("Log In").click();
		mau.sleep(2000);
		wd.findElementByName("Continue").click();
		// Allow time for the Opt In Modal to appear.
		mau.sleep(10000);
		// The opt in for Push Notifications
		// mau.log("[INFO]: Now checking for the Opt In Dialog...");
		// AssertJUnit.assertTrue(wd.findElementById("Don't miss out").isDisplayed());
		mau.log("[INFO]: Now opting in to Push Notifications...");
		wd.findElementByAccessibilityId("I'm in").click();
		// Auto accept the system alert.
		mau.log("[INFO]: Now auto accepting the system dialog...");
		wd.switchTo().alert().accept();
		mau.sleep(4000);
		mau.log("[INFO]: Now verifying the Today's Match Screen...");
		AssertJUnit.assertTrue(wd.findElementByName("Today's Match").isDisplayed());
		// AssertJUnit.assertTrue(wd.findElementByName("Say hello").isEnabled());
	}
	
	@Test (priority = 3, enabled = true)
	public void testNavBar() throws InterruptedException {
		mau.log("[INFO]: Now testing the Nav Bar...");
		msScreens.forEach((k,v)-> {
			mau.log("INFO]: Now checking the " + k + " icon on the Nav Bar...");
			AssertJUnit.assertTrue(wd.findElementByAccessibilityId(v).isDisplayed());	
			wd.findElementByAccessibilityId(v).click();
			mau.sleep(1000);
			mau.log("[INFO]: Verifying the screen name: " + k + "...");
			AssertJUnit.assertTrue(wd.findElementByName(k).isDisplayed());	
		});			
	}
	
	@Test (priority = 4, enabled = false)
	public void testEmailSignIn() throws InterruptedException {
		AssertJUnit.assertTrue(wd.findElementByName("Already a memeber?").isDisplayed());
		wd.findElementByName("Sign In").click();
		// Verify the 2 text fields: Email and Password.
		AssertJUnit.assertTrue(wd.findElementByName("Email").isDisplayed());
		AssertJUnit.assertTrue(wd.findElementByName("Password").isDisplayed());
		wd.findElementByName("Email").sendKeys("cchoy@healthline.com");
		Thread.sleep(2000);
		wd.findElementByName("Password").sendKeys("11111111");
		AssertJUnit.assertTrue(wd.findElementByName("Next").isEnabled());
		wd.findElementByName("Next").click();
		Thread.sleep(2000);
		AssertJUnit.assertTrue(wd.findElementByName("Today's Match").isDisplayed());
		AssertJUnit.assertTrue(wd.findElementByName("Say Hello").isEnabled());
	}
	
	@AfterTest
	public void quit() {
		wd.quit();
	}
	
	


}
