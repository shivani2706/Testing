package qalib;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckPillIdentifierPage {
  private WebDriver webDriver;
  private String baseURL;
  @SuppressWarnings("unused")
  private String currentURL;
  private String msgAds;
  private List<String> messages = new ArrayList<String>();
  
  public CheckPillIdentifierPage(WebDriver driver, String baseUrl) {
    webDriver = driver;
    currentURL = webDriver.getCurrentUrl();
    baseURL = baseUrl;
  }
  
  public void checkNow() {
    CheckAds checkAds = new CheckAds(webDriver);
    
    msgAds = checkAds.checkUpperAd();
    if (msgAds != null) {
      messages.add(msgAds);
    }
    
    /**msgAds = checkAds.checkLowerAd();
    if (msgAds != null) {
      messages.add(msgAds);
    }**/
    
    msgAds = checkAds.checkRhsAdsMr1();
    if (msgAds != null) {
      messages.add(msgAds);
    }
    checkPageElements();
  } 
  
  private void checkPageElements()  {
    try {
      WebElement contentLicense = webDriver.findElement(By.className("licensed-tab"));
      String src = contentLicense.findElement(By.tagName("img")).getAttribute("src");
      CheckHttpConnection checkStatus = new CheckHttpConnection(baseURL.concat(src));
      
      if(checkStatus.testHttpConn() != 200)  {
        messages.add(webDriver.getCurrentUrl()+" License Attribution is not working");
      }
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"License Attribution is missing : " + e.toString());
    }
    
    try {
      WebElement removeLink = webDriver.findElement(By.className("reset"));
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"'Reset Button is missing : " + e.toString());
    }
    
    try {
      WebElement clinicalAppLink = webDriver.findElement(By.id("clinicalAppCallout"));
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"'Clinical App Callout is missing : " + e.toString());
    }
    
    try {
      WebElement results = webDriver.findElement(By.className("results"));
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"Pill Identifier Results are missing : " + e.toString());
    }
    
    try {
      List<WebElement> pillImages = webDriver.findElements(By.className("pill-thumbnail"));
      
      for(int i=0;i<pillImages.size();i++ )  {
        List<WebElement> images = pillImages.get(i).findElements(By.tagName("img"));
        String src = images.get(0).getAttribute("src");
        CheckHttpConnection checkStatus = new CheckHttpConnection(baseURL.concat(src));
        if(checkStatus.testHttpConn() != 200)  {
          messages.add(baseURL.concat(src)+" Image is broken");
        }
      }
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"'Pill Thumbnails are missing : " + e.toString());
    }
    
    try {
      WebElement drugLink = webDriver.findElement(By.linkText("Acyclovir"));
      String href = drugLink.getAttribute("href");

      CheckHttpConnection checkStatus = new CheckHttpConnection(baseURL.concat(href));
      if(checkStatus.testHttpConn() != 200)  {
        messages.add(baseURL.concat(href)+" Drug Acyclovir is broken");
      }
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"Drug Acyclovir is missing : " + e.toString());
    }
  }
  

  public List<String> getMessages() {
    return messages;
  }
}

