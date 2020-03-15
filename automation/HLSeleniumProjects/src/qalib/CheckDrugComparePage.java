package qalib;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import qalib.CheckAds;

public class CheckDrugComparePage {
  private WebDriver webDriver;
  private String baseURL;
  private String currentURL;
  private String msgAds;
  
  private List<String> messages = new ArrayList<String>();
  
  public CheckDrugComparePage(WebDriver driver, String baseUrl) {
    webDriver = driver;
    currentURL = webDriver.getCurrentUrl();
    baseURL = baseUrl;
  }
  
  public void checkNow() {
    DrugPageContents(webDriver,messages,baseURL);
  }
  
  public void checkDrugOneInput(WebDriver driver, List<String> messages) {
    try {
      WebElement drug1input = driver.findElement(By.id("div-twocol2-lc")).findElement(By.id("drug1input"));
    } catch (Exception e) {
      messages.add(driver.getCurrentUrl()+"First Drug Input is missing : " + e.toString());
    }
  }
  
  
  public void checkDrugTwoInput(WebDriver driver, List<String> messages) {
    try {
      WebElement drug2input = driver.findElement(By.id("div-twocol2-lc")).findElement(By.id("drug2input"));
    } catch (Exception e) {
      messages.add(driver.getCurrentUrl()+"Second Drug Input is missing : " + e.toString());
    }
  }
  
  
  public void checkTOC(WebDriver driver, List<String> messages) {
    try {
      WebElement toc = driver.findElement(By.className("tableofcontents"));
    } catch (Exception e) {
      messages.add(driver.getCurrentUrl()+"Table of Contents is missing : " + e.toString());
    }
  }
  
  
  public void checkTopDrugs(WebDriver driver, List<String> messages) {
    try {
      WebElement topDrugs = driver.findElement(By.xpath("//div[contains(@class, 'related-content')]"));
    } catch (Exception e) {
      messages.add(driver.getCurrentUrl()+"Top Rx Drugs is missing : " + e.toString());
    }
  }
  
  
  public void checkLicense(WebDriver driver, List<String> messages)  {
    try {
      WebElement searchField = driver.findElement(By.className("licensed-tab"));
    } catch (Exception e) {
      messages.add(driver.getCurrentUrl()+"'License attribution is missing : " + e.toString());
    }
  }
  
  
  public void checkClinicalTab(WebDriver driver, List<String> messages, String baseURL) {
    List<WebElement> pages = null;
    List<String> pageLinks = new ArrayList<String>();
    
    try {
      String href = driver.findElement(By.id("drugtab")).findElement(By.tagName("a")).getAttribute("href");
      driver.get(baseURL+href);
      CheckAds checkAds = new CheckAds(driver);
      msgAds = checkAds.checkUpperAd();
      if (msgAds != null) {
        messages.add(msgAds);
      }
      
      msgAds = checkAds.checkRhsAdsWsr();
      if (msgAds != null) {
        messages.add(msgAds);
      }

      msgAds = checkAds.checkLowerAd2();
      if (msgAds != null) {
        messages.add(msgAds);
      }
      
      checkDrugOneInput(driver,messages);
      checkDrugTwoInput(driver,messages);
      checkTOC(driver,messages);
      checkTopDrugs(driver,messages);
      checkLicense(driver,messages);
      
      WebElement paging = driver.findElement(By.id("div-twocol2-lc")).findElement(By.className("paging"));
      if (paging != null) {
        pages = paging.findElements(By.tagName("a"));
        pages.remove(pages.size()-1);
      
        //Somehow single loop will cause web driver error out
        //workaround is to loop in 2 steps
        for (WebElement page : pages) {
          //Test each page
          href = page.getAttribute("href");
          pageLinks.add(baseURL + href);
        }
      
        for (String pageLink : pageLinks) {
          driver.get(pageLink);
          CheckHttpConnection checkStatus = new CheckHttpConnection(pageLink);
          if(checkStatus.testHttpConn() != 200)   messages.add(pageLink+" Page load failed");
        }
      }
    } catch (Exception e) {
      messages.add(driver.getCurrentUrl()+" Error : " + e.toString());
    }
  }
  
  
  public void DrugPageContents(WebDriver driver, List<String> messages, String baseURL) {
    List<WebElement> pages = null;
    List<String> pageLinks = new ArrayList<String>();
    
    try {
      CheckAds checkAds = new CheckAds(driver);
      msgAds = checkAds.checkUpperAd();
      if (msgAds != null) {
        messages.add(msgAds);
      }
      
      msgAds = checkAds.checkRhsAdsWsr();
      if (msgAds != null) {
        messages.add(msgAds);
      }

      msgAds = checkAds.checkLowerAd2();
      if (msgAds != null) {
        messages.add(msgAds);
      }
    
      checkDrugOneInput(driver,messages);
      checkDrugTwoInput(driver,messages);
      checkTOC(driver,messages);
      checkTopDrugs(driver,messages);
      checkLicense(driver,messages);
    
      String drugTab = driver.findElement(By.id("drugtab")).findElement(By.tagName("a")).getText().trim();
    
      WebElement paging = driver.findElement(By.id("div-twocol2-lc")).findElement(By.className("paging"));
      if (paging != null) {
        pages = paging.findElements(By.tagName("a"));
        pages.remove(pages.size()-1);
    
        //Somehow single loop will cause web driver error out
        //workaround is to loop in 2 steps
        for (WebElement page : pages) {
          //Test each page
          String href = page.getAttribute("href");
          pageLinks.add(baseURL + href);
        }
    
        for (String pageLink : pageLinks) {
          driver.get(pageLink);
          CheckHttpConnection checkStatus = new CheckHttpConnection(pageLink);
          if(checkStatus.testHttpConn() != 200)   messages.add(pageLink+" Page load failed");
        }
      }
    
      if (drugTab.equals("Clinical")) {
        checkClinicalTab(driver,messages,baseURL);
      }
    } catch (Exception e) {
      messages.add(driver.getCurrentUrl()+" Error : " + e.toString());
    }
  }
  

  public List<String> getMessages() {
    return messages;
  }
}

