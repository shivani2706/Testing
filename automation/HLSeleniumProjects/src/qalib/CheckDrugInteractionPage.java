package qalib;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckDrugInteractionPage {
  private WebDriver webDriver;
  private String baseURL;
  private String currentURL;
  private String msgAds;
  private List<String> messages = new ArrayList<String>();
  
  public CheckDrugInteractionPage(WebDriver driver, String baseUrl) {
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
    
    msgAds = checkAds.checkLowerAd2();
    if (msgAds != null) {
      messages.add(msgAds);
    }
    
    msgAds = checkAds.checkRhsAdsMr1();
    if (msgAds != null) {
      messages.add(msgAds);
    }
    
    msgAds = checkAds.checkRhsAdsMr2();
    if (msgAds != null) {
      messages.add(msgAds);
    }
    
    msgAds = checkAds.checkRhsAdsMarketPlace();
    if (msgAds != null) {
      messages.add(msgAds);
    }
    
    checkPageElements();
  }
  
  private void checkPageElements()  {
    try {
      WebElement contentLicense = webDriver.findElement(By.xpath("//div[contains(@class, 'contentlicense')]"));
      String src = contentLicense.findElement(By.tagName("img")).getAttribute("src");
      CheckHttpConnection checkStatus = new CheckHttpConnection(baseURL.concat(src));
      
      if(checkStatus.testHttpConn() != 200)  {
        messages.add(webDriver.getCurrentUrl()+" License Attribution is not working");
      }
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"License Attribution is missing : " + e.toString());
    }
    
    try {
      WebElement removeLink = webDriver.findElement(By.id("removeAllLink"));
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"'Remove All/Start Over' is missing : " + e.toString());
    }
    
    try {
      WebElement searchField = webDriver.findElement(By.id("ysearchinput"));
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"Textbox to enter second drug is missing : " + e.toString());
    }
    
    try {
      WebElement searchField = webDriver.findElement(By.id("pickdrug"));
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"'Or choose from a list' Link is missing : " + e.toString());
    }
    
    try {
      WebElement filter = webDriver.findElement(By.className("intFilter"));
      List<WebElement> filterOptions = filter.findElements(By.tagName("input"));
      
      for(int i=0;i<filterOptions.size();i++)  {
        String filterValue = filterOptions.get(i).getAttribute("value").trim();
        if(!filterValue.equalsIgnoreCase("all") && !filterValue.equalsIgnoreCase("Severe") && !filterValue.equalsIgnoreCase("Moderate") && !filterValue.equalsIgnoreCase("Minor"))  {
          messages.add(webDriver.getCurrentUrl()+" One of the filters is missing");
        }
      }
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+"Severity Filters are missing : " + e.toString());
    }
    
    try {
      WebElement searchField = webDriver.findElement(By.xpath("//div[contains(@class, 'interaction')]"));
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+" Drug Interactions are missing : " + e.toString());
    }
    
    try {
      WebElement interaction = webDriver.findElement(By.className("interactionfieldset")).findElement(By.linkText("Show details"));
      interaction.click();
      WebElement interaction1 = webDriver.findElement(By.className("interactionfieldset")).findElement(By.tagName("div"));
      String intText = interaction1.getText();
      
      if(intText == null)  {
        messages.add(webDriver.getCurrentUrl()+" Drug Interaction Detailed Text is missing");
      }
      
    } catch (Exception e) {
      messages.add(webDriver.getCurrentUrl()+" Drug Interactions are missing : " + e.toString());
    }
  } 
  

  public List<String> getMessages() {
    return messages;
  }
}

