package qalib;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Point;

/**
 * Dipti 04/27/2013 - Check various elements of SxS Result page
 */    

public class CheckSymptomSearchResultPage {

  private WebDriver webDriver;
  private String baseURL;
  private List<String> messages = new ArrayList<String>();

  public CheckSymptomSearchResultPage(WebDriver driver, String baseUrl) {
    baseURL = baseUrl;
    webDriver = driver;

  }

  public void checkNow() {
    if(webDriver.manage().window().getSize().width>=768){
    testTitle();
    testBreadCrumbs();
    }
    testReviewedBy();
    //checkMySymptomList();
    //checkMoreRelatedSymptom();
    checkResults();

   
    //checkPagging();

    //checkFilter();
    checkCauseTitle();
    checkAnotherSymptom();
    checkArticleBlock();
    
  }

  public void checkMySymptomList() {
    try {
      WebElement mySymptomList = webDriver.findElement(By.id("mySymptomsList"));
      List<WebElement> symptom = mySymptomList.findElements(By
          .className("mySymptom"));
      if (symptom.size() == 0) {
        messages.add("No Symptom listed in My Symptom section at LHS");
      }

      try {
        WebElement addSymptom = mySymptomList.findElement(By
            .className("addsymptom"));
      } catch (Exception e) {
        messages.add("Add Symptom link misisng in My Symptom section at LHS");
      }
      try {
        WebElement clearList = mySymptomList.findElement(By.tagName("input"));
      } catch (Exception e) {
        messages.add("Clear List button missing in My Symptom section at LHS");
      }
      
    } catch (Exception e) {
      messages.add("My Symptom List section is missing");
    }
    
  }

  public void checkArticleBlock(){
    try {
      WebElement articleBlock = webDriver.findElement(By.className("hl-box-symptom-article"));
      if(articleBlock.getText().trim()==""){
        messages.add("Content missing inside the article block on SxC result page for 'yellow eyes'");
      }
      try{
      WebElement readMore = webDriver.findElement(By.className("hl-box-top-article-read-more")).findElement(By.className("hl-link-top-article-read-more"));
      } catch (Exception e) {
        messages.add("Read More button missing in article block on SxC result page for 'yellow eyes'");
      }
    } catch (Exception e) {
      messages.add("Article block missing on SxC result page for 'yellow eyes'");
    }
  }
  
  public void checkMoreRelatedSymptom() {
    try {
      WebElement relatedSymptomLink = webDriver.findElement(By
          .className("relatedSymptomsSet"));
      List<WebElement> relatedSymptoms = relatedSymptomLink.findElements(By
          .className("ss_links"));

      if (relatedSymptoms.size() == 0) {
        messages
            .add("Related symptoms not listed in Related Symptoms section at LHS");
      }
      try {
        WebElement moreRelatedSymptoms = relatedSymptomLink.findElement(By
            .id("related-link"));
        moreRelatedSymptoms.click();
        Thread.sleep(3000);
        JavascriptExecutor js;
        /*
        if (webDriver instanceof JavascriptExecutor) {
          js = (JavascriptExecutor) webDriver;
          js.executeScript("return  addrelatedsymptom.click('',this);return false;");
        }*/
        try {
          WebElement relatedSymptomBox = webDriver.findElement(By
              .id("RELATEDsymList"));
        } catch (Exception e) {
          messages.add("Add a Symptom popup is missing");
        }
        webDriver.findElement(By.id("RELATEDsymptomnavigator")).findElement(By.className("box-title")).findElement(By.tagName("a")).click();
        /*
        if (webDriver instanceof JavascriptExecutor) {
          js = (JavascriptExecutor) webDriver;
          js.executeScript("addrelatedsymptom.closeit();return false;");
        }
        */
         
      } catch (Exception e) {
        messages.add("More Related symptom link is missing at LHS");
      }
     
    } catch (Exception e) {
      messages.add("Related Symptoms Section missing at LHS");
    }

  }

  public void checkResults() {
	    try {
	      WebElement sxSResults = webDriver.findElement(By.id("ts_result"));

	      List<WebElement> results = webDriver.findElements(By.className("hl-box-result-item"));
	      int firstCount=0;
	      int secondCount=0;
	      
	      if (results.size() <= 0) {
	        messages.add("Symptom search results are missing"+webDriver.getCurrentUrl());
	      }else{
	    	  Thread.sleep(7000);
	    	  firstCount=results.size();
	    	  Point subDrop=results.get(18).getLocation();
	          Thread.sleep(6000);
	          ((JavascriptExecutor)webDriver).executeScript("window.scrollBy(0,"+(subDrop.getY())+");"); 
	          Thread.sleep(6000);
	          List<WebElement> resultss = webDriver.findElements(By.className("hl-box-result-item"));
	          secondCount=resultss.size();
	          System.out.println(firstCount+"  "+secondCount);
	          if(secondCount>firstCount){
	        	  System.out.println("lazy load is working fine");
	          }else{
	        	  messages.add("lazy load is not working on "+webDriver.getCurrentUrl());
	          }
	      }
	            
	    } catch (Exception e) {
	      messages.add("Search Results are missing "+webDriver.getCurrentUrl());
	    }

	  }



  public void checkPagging() {
    try {
      WebElement pagging = webDriver.findElement(By
          .className("list-pagination"));
      
      List<WebElement> paggingLinks = pagging.findElements(
          By.tagName("a"));
      for (WebElement paggingLink : paggingLinks) {
        String href = paggingLink.getAttribute("href");
        if (href != null) {
          CheckHttpConnection httpConnection = new CheckHttpConnection(href);

          int status = httpConnection.testHttpConn();

          if (status != 200) {
            messages
                .add(href + " link from SxS pagination is broken " + status+webDriver.getCurrentUrl());
          }
        }
      }
     
    } catch (Exception e) {
      messages.add("pagination links missing "+webDriver.getCurrentUrl());
    }
  }

  public void checkFilter() {
    try {
      WebElement filter = webDriver.findElement(By.id("filterDiv"));
      filter.click();
      Thread.sleep(3000);
      try{
      List<WebElement> filterChecks=webDriver.findElement(By.id("layover-320")).findElements(By.className("filterCell"));
      if(filterChecks.size()==0){
        messages.add("Filter Check buttons missing from filter popup");
      }
      }catch (Exception e) {
       messages.add("Filter Overlay is missing on clicking the Filter button");
      }
      try{
        WebElement filterSubmit = webDriver.findElement(By.className("submitFilter"));
      }catch (Exception e) {
        messages.add("Submit button is missing from Filter Overlay");
      }
      
      try{
        WebElement filterCancel = webDriver.findElement(By.className("cancelFilter"));
      }catch (Exception e) {
        messages.add("Cancel button is missing from Filter Overlay");
      }    
     
    } catch (Exception e) {
      messages.add("Filter button is missing or not working on SxS result page");
    }
  }

  public void checkCauseTitle() {
    try {
      WebElement causeTitle = webDriver.findElement(By
          .tagName("h1"));
      if (causeTitle.getText().trim().equals("")) {
        messages.add("Cause title text missing above SxS results");
      }
      
    } catch (Exception e) {
      messages.add("Cause title text missing above SxS results");
    }
  }

  public void checkAnotherSymptom(){
  try{
    WebElement box=webDriver.findElement(By.className("hl-box-other-symptoms")).findElement(By.className("hl-box-other-message"));
    if(!box.isDisplayed()){
      messages.add("Are you experiencing other symptom... widget is missing");
    }else{
    try{
    WebElement searchBox=webDriver.findElement(By.className("hl-field-addterm"));
    if(!searchBox.getText().trim().equals("")){
      messages.add("text box missing in Do you have any other symptoms? section on SxS Result page");
    }
    }catch (Exception e) {
      messages.add("text box missing in Do you have any other symptoms? section on SxS Result page");
    } 
    try{
      WebElement helpMe=null;
      if(webDriver.manage().window().getSize().width>=768){
        helpMe=webDriver.findElement(By.className("hl-link-choose-symptom"));
      }else{
        helpMe=webDriver.findElement(By.className("hl-link-choose-symptom-expand"));
      }
      helpMe.click();
      Thread.sleep(3000);
      try{
      List<WebElement> syms=webDriver.findElements(By.className("hl-list-symptoms-p"));
      if(syms.size()<=0){
        messages.add("No symptoms listed after clicking Help Me Choose link in I'm experiencing widget on SxS Result page");
      }
      }catch (Exception e) {
        messages.add("Help Me Choose link not working in I'm experiencing widget on SxS Result page");
      }
    }catch (Exception e) {
      messages.add("Help Me Choose link missing in I'm experiencing widget on SxS Result page");
    }
    try{
      WebElement pillBox=webDriver.findElement(By.className("hl-pillbox-symptom"));
    }catch (Exception e) {
      messages.add("Symptom pillbox missing in I'm experiencing section on SxS Result page");
    }
    }
  }catch (Exception e) {
    messages.add("Are you experiencing other symptom... widget is missing");
  }
  }
  
  
  public void testTitle(){
    try{
      WebElement title=webDriver.findElement(By.className("navigation-title"));
        if(title.getText().trim().equals("")){
          messages.add("Title text missing for SxS result page");
        }
     
    }catch (Exception e) {
      messages.add("Title text missing for SxS result page");
    }
    
  }
  
  
  public void testBreadCrumbs(){
    try{
      WebElement breadCrumb = webDriver.findElement(By.className("breadcrumb"));
        if(breadCrumb.getText().trim().equals("")){
         messages.add("Breadcrumbs missing for SxS result page"); 
        }
      
    }catch (Exception e) {
      messages.add("Breadcrumbs missing for SxS result page"); 
    }
  }
  

  public void testReviewedBy(){
    try{
      WebElement reviewedBy=webDriver.findElement(By.className("byline"));
      if(reviewedBy.getText().trim().equals("")){
        messages.add("Reviewed By text missing for SxS result page");
      }
     
      try{
      WebElement reviewer=webDriver.findElement(By.className("byline")).findElement(By.tagName("a"));
      if(reviewer!=null){
        CheckHttpConnection httpConnection=new CheckHttpConnection(reviewer.getAttribute("href"));
        int status=httpConnection.testHttpConn();
        if(status!=200){
          messages.add("Reviewer link is broken from Reviewer Info section of SxS result page");
        }
        
      }
      }catch (Exception e) {
      }
    }catch (Exception e) {
      messages.add("Reviewed By text missing for SxS result page");
    }
  }

  
  public List<String> getMessages() {
    return messages;
  }
}
