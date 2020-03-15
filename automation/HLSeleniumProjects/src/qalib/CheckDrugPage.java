package qalib;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckDrugPage {
  private WebDriver webDriver;
  private String baseURL;
  private String msgAds;
  private List<String> messages = new ArrayList<String>();

  public CheckDrugPage(WebDriver driver, String baseUrl) {
    webDriver = driver;
    baseURL = baseUrl;
  }

  public void checkNow() {
    checkAds();
    checkLicense();
    checkLeftRail();
    checkCenterWell();
    checkMediaWidget();
    checkLicense();
    checkPagging();
    checkDisclaimer();
  }

  public void checkAds(){
    CheckAds checkAds = new CheckAds(webDriver);
    msgAds = checkAds.checkUpperAd();
    if (msgAds != null) {
      messages.add(msgAds);
    }

    msgAds = checkAds.checkLhsAd();
    if (msgAds != null) {
      messages.add(msgAds);
    }
    msgAds = checkAds.checkRhsAdsMr1();
    if (msgAds != null) {
      messages.add(msgAds);
    }
    msgAds = checkAds.checkNetseerAds();
    if (msgAds != null) {
      messages.add(msgAds);
    }        
  }

  public void checkLeftRail() {
    try{
      WebElement leftRail = webDriver.findElement(By.id("leftRail"));


      try{
        WebElement lhsNav = leftRail.findElement(By.id("lhsNav"));
        //Check content header
        WebElement contentHeader = webDriver.findElement(By.className("lhsNavTitle"));
        String header = contentHeader.findElement(By.tagName("h3")).getText();
        if (header.isEmpty()) {
          messages.add("Left rail header is empty on drug notebook page.");
        }

        //Check lhsNav links
        List<WebElement> anchors = lhsNav.findElements(By.tagName("a"));
       
        for (WebElement anchor : anchors) {
          String href = anchor.getAttribute("href");
          String title = anchor.getText();

          if (href.startsWith("http")) {
            if (!title.contentEquals("Clinical Information")) {
              CheckHttpConnection httpConn = new CheckHttpConnection(href);
              int httpStatus = httpConn.testHttpConn();
              if (httpStatus != 200) {
                messages.add(href + " is broken from drug notebook page and http status = " + httpStatus);
              }
            }
          } else { continue; }

        }
      }catch (Exception e) {
        messages.add("Left rail header is missing on drug notebook page.");
      }
      
      try{
      WebElement lhsNavDrugTools = leftRail.findElement(By.id("lhsNavDrugTools"));
      String lhsNavTitle = lhsNavDrugTools.findElement(By.tagName("h3")).getText();
      if (! lhsNavTitle.equals("Drug Info Tools")) {
        messages.add("Header missing for Drug Info Tools box on drug notebook page");
      }

      List<WebElement> drugInfoHrefs = lhsNavDrugTools.findElements(By.tagName("a"));
      for (WebElement drugInfoLink : drugInfoHrefs) {
        String href = drugInfoLink.getAttribute("href");
        CheckHttpConnection httpConn = new CheckHttpConnection(href);
        int httpStatus = httpConn.testHttpConn();
        if (httpStatus != 200) {
          messages.add(href + " is broken and http status = " + httpStatus);
        }
      }
      }catch (Exception e) {
        messages.add("Drug Info Tools box is missing at Left Rail on drug notebook page");
      }

    }catch (Exception e) {
      messages.add("Left Rail is missing from Drug Notebook Page");
    }
  }

  public void checkCenterWell(){
    try{
      WebElement centerWell=webDriver.findElement(By.id("div-threecol-mc"));
      //check Header
      try{
        WebElement contentHead=centerWell.findElement(By.className("contentHeader"));
        if(contentHead.findElement(By.tagName("h1")).getText().isEmpty()){
         messages.add("Heading misisng in Center well of Drug Notebook page"); 
        }
        if(contentHead.findElement(By.id("definition")).getText().isEmpty()){
          messages.add("Definition misisng in Center well of Drug Notebook page"); 
         }        
      }catch (Exception e) {
        messages.add("Header section above social buttons missing on Drug Notrbook Page");
      }
      
      //check social buttons
      try{
        WebElement shareBar = webDriver.findElement(By.className("hl-widget-sharebar"));
        List<WebElement> shareBarLinks = shareBar.findElements(By.className("sb-icons"));

        //Check facebook
        String faceBookTitle = shareBarLinks.get(0).getAttribute("data-action");
        if (! faceBookTitle.contentEquals("facebook")) {
          messages.add("facebook icon is missing.");
        }

        //Check Tweeter
        String tweeterTitle = shareBarLinks.get(1).getAttribute("data-action");
        if (! tweeterTitle.contentEquals("twitter")) {
          messages.add("Tweeter icon is missing.");
        }

        //Check Google plusone
        String googlePlusOneTitle = shareBarLinks.get(2).getAttribute("data-action");
        if (! googlePlusOneTitle.contentEquals("googleplus")) {
          messages.add("Google plusone icon is missing.");
        }

        //Check pinterest
        String printTitle = shareBarLinks.get(3).getAttribute("data-action");
        if (! printTitle.contentEquals("pinterest")) {
          messages.add("Pinterest icon is missing.");
        }

        //Check print
        String emailTitle = shareBarLinks.get(4).getAttribute("data-action");
        if (! emailTitle.contentEquals("email")) {
          messages.add("Email icon is missing.");
        }
        
      }catch (Exception e) {
        messages.add("Social buttons missing on drug notebook page");
      }
      //check content
      try{
        WebElement pageContent=webDriver.findElement(By.id("page_1"));
        if(pageContent.getText().isEmpty()){
          messages.add("Center well content is missing on Drug Notebook page");
        }
      }catch (Exception e) {
        
      }
      
    }catch (Exception e) {
      messages.add("Center well is missing on Drug Notebook page");
    }
  }
  
  public void checkMediaWidget() {
    
    try {
    
      WebElement mediaWidget = webDriver.findElement(By.className("grid300")).findElement(By.className("right")).findElement(By.tagName("div"));
      System.out.println(mediaWidget.getAttribute("style"));
      if(mediaWidget.getAttribute("style").equals("margin-top: 5px;")){
       try{
        WebElement title1=webDriver.findElement(By.id("browsable")).findElement(By.className("groupImageTitle")).findElement(By.tagName("strong"));
       }catch (Exception e) {
         messages.add("Title missing in media widget on drug notebook page");
      }
        
        try{
        WebElement seeAllLink=webDriver.findElement(By.className("groupImageLink")).findElement(By.tagName("a"));
        CheckHttpConnection httpConnection=new CheckHttpConnection(seeAllLink.getAttribute("href"));
        if(httpConnection.testHttpConn()!=200){
          messages.add("See All Images link broken from media widget at RHS on drug notebook page");
        }
        }catch (Exception e) {
           messages.add("See All Images link missing from media widget at RHS on drug notebook page");
        }
        try{
        List<WebElement> pillImages=webDriver.findElement(By.id("browsable")).findElement(By.tagName("table")).findElements(By.tagName("img"));
        for(WebElement pillImage:pillImages){
          String src=pillImage.getAttribute("src");
          if(src!=null){
            CheckHttpConnection httpConnection=new CheckHttpConnection(src);
            if(httpConnection.testHttpConn()!=200){
              messages.add("Pill thumbnail is broken from media widget at RHS on drug notebook page");
            }
          }
        }
        List<WebElement> pillLinks=webDriver.findElement(By.id("browsable")).findElement(By.tagName("table")).findElements(By.tagName("a"));
        for(WebElement pillLink:pillLinks){
          String href=pillLink.getAttribute("href");
          if(href!=null){
            CheckHttpConnection httpConnection=new CheckHttpConnection(href);
            if(httpConnection.testHttpConn()!=200){
              messages.add("Pill image link is broken from media widget at RHS on drug notebook page");
            }
          }
        }        
        
        }catch (Exception e) {
          messages.add("Pill Images list missing in media widget at RHS on drug notebook page");
        }
      }
    } catch (Exception e) {

    }
  }


  public void checkLicense() {
    try{
      WebElement license = webDriver.findElement(By.xpath("//div[contains(@class, 'licensed licensed-tab')]"));

      WebElement anchor = license.findElement(By.tagName("a"));
      String licenseLink = anchor.getAttribute("href");

      WebElement img = license.findElement(By.tagName("img"));
      String imgLink = img.getAttribute("src");

      if (! licenseLink.startsWith("http")) {
        licenseLink = baseURL + licenseLink;
      }

      if (! imgLink.startsWith("http")) {
        imgLink = baseURL + imgLink;
      }

      CheckHttpConnection httpConn = new CheckHttpConnection(licenseLink);
      if (httpConn.testHttpConn() != 200) {
        messages.add("Lincense link is broken on drug notebook page " + " " + licenseLink + ", status = " + httpConn.getStatus());
      }

      httpConn = new CheckHttpConnection(imgLink);
      if (httpConn.testHttpConn() != 200) {
        messages.add("Lincense image link is broken on drug notebook page " + imgLink + ", status = " + httpConn.getStatus());
      }
    }catch (Exception e) {
      messages.add("Attribution missing on Drug Notebook page "+webDriver.getCurrentUrl());
    }
  }
  public void checkPagging(){
    try{
      WebElement pagging=null;
      pagging=webDriver.findElement(By.className("pagination"));
      if(pagging!=null){
        List<WebElement> pages=pagging.findElements(By.tagName("a"));
        
        pages.remove(pages.size()-1);
        int sizes=pages.size();
        int pageNum=2;
        for(int j=0;j<=sizes-1;j++){
          webDriver.findElement(By.className("pagination")).findElement(By.linkText(Integer.toString(pageNum))).click();
          Thread.sleep(3000);
          WebElement currentPage=null;
          String path="//div[contains(@id,'page_') and @style='display: block;']";
          currentPage=webDriver.findElement(By.xpath(path));
          String  currentPageNum=currentPage.getAttribute("id");
          if(!currentPageNum.equals("page_"+pageNum)){
            messages.add("Problem with pagination link on "+webDriver.getCurrentUrl());
          }
          pageNum++;
        }
      }
    }catch (Exception e) {
      
    }
  }

  public void checkDisclaimer(){
    try{
      WebElement disclaimer=webDriver.findElement(By.className("disclaim"));
      if(disclaimer.getText().isEmpty()){
        messages.add("Disclaimer text missing on drug notebook page");
      }
    }catch (Exception e) {
     messages.add("Disclaimer text missing on drug notebook page");
    }
  }
  
  public List<String> getMessages() {
    return messages;
  }
}

