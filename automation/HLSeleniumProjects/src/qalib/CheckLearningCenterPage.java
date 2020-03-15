package qalib;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class CheckLearningCenterPage {

  private WebDriver webDriver;
  private String baseURL;
  private String msgAds;
  private List<String> messages = new ArrayList<String>();

  public CheckLearningCenterPage(WebDriver driver, String Url) {
    webDriver = driver;
    baseURL = Url;
  }

  public void checkNow() {
    checkPageType();
  }

 
  public void checkPageType() {
    
    try {
      boolean hrl=false;
      try{
        WebElement lhsDivs = webDriver.findElement(By.className("toggle-accordion"));
        hrl=true;
      }catch (Exception e) {
        // TODO: handle exception
      }
      
      if(hrl){
       System.out.println(webDriver.getCurrentUrl()+"----->HRL");
        checkHRL();
      }else{          
        System.out.println(webDriver.getCurrentUrl()+"----->topic center");
          checkLearningCenter();
        }
     
      } catch (Exception e) {
        System.out.println(e.toString());
      messages.add("Some problem on "+webDriver.getCurrentUrl());
    }
  }

  
  public void checkLearningCenter() {
    
    checkHead();
    //checkBreadCrumb();
    checkHeading();
    checkTopicReviewerInfo();
    checkTopicTextSize();
    List<String> tcLinks = new ArrayList<String>();
    System.out.println("test");
    try{
      List<WebElement> tocLinks=webDriver.findElement(By.className("fullwidthblock")).findElements(By.tagName("a"));
      for(int i=0;i<=tocLinks.size()-1;i++){
        tcLinks.add(tocLinks.get(i).getText());
        //System.out.println(tocLinks.get(i).getText());
      }
      
      for(int i=0;i<=tcLinks.size()-1;i++){
        try{
        webDriver.findElement(By.linkText(tcLinks.get(i))).click();
        Thread.sleep(2000);
        try{
          WebElement articleBody=webDriver.findElement(By.className("introduction"));
        }catch (Exception e) {
          try{
            WebElement content=webDriver.findElement(By.className("article-body"));
            if(content.getText().trim().equals("")){
              messages.add("Content missing on "+webDriver.getCurrentUrl());
            }
          }catch (Exception e1) {
            messages.add("Content Below TOC missing "+webDriver.getCurrentUrl());
          }
         
        }
        }catch(Exception e){
          messages.add("Problem with TOC on "+webDriver.getCurrentUrl()+" for TAB "+tcLinks.get(i));
          
        }
      }
    }catch(Exception e){
      System.out.println(e.toString());
      
    }
    //box-article-bottom-section
     
    
    //checkThreeColImageBlock();
    //checkTwoColImageBlock();
    //checkOneColumnImageBlock();
   // checkMR1();
   // checkMR2();
    
  }

  public void checkHRL() {
    
    //checkNavigator();
    //checkBreadCrumb();
    checkHeading();
    checkReviewerInfo();
    checkTextSize();
    WebElement toogleBtn=null; 
    try{
      toogleBtn=webDriver.findElement(By.className("toggle-accordion"));
    }catch (Exception e) {
      messages.add("Expand button is missing on "+webDriver.getCurrentUrl());
    }
    try{
      WebElement articleBody=webDriver.findElement(By.className("article-body"));
    }catch (Exception e) {
      messages.add("Article contents missing on "+webDriver.getCurrentUrl());
    }
    try{
      WebElement fbShare=webDriver.findElement(By.className("shmod-social_b"));
      }catch (Exception e) {
      messages.add("FB share bar missing below article contents on "+webDriver.getCurrentUrl());
    }
  
    if(toogleBtn!=null){
    try{
      WebElement articleResource=webDriver.findElement(By.className("box-article-source"));
      if(articleResource.getText().trim().equals("")){
        messages.add("Article Sources section is empty on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      messages.add("Article Sources section missing on "+webDriver.getCurrentUrl());
    }
    try{
      WebElement hrlLogo=webDriver.findElement(By.className("hrl-logo"));
    }catch (Exception e) {
      messages.add("HRL logo missing on "+webDriver.getCurrentUrl());
    }
    }
       
    }

  public void checkNavigator(){
    List<WebElement> navLinks=webDriver.findElement(By.id("toptoc-scroll-menu")).findElements(By.tagName("a"));
    int counter=1;
    for(WebElement link:navLinks){
      link.click();
      WebElement articleContent=webDriver.findElement(By.id("TOC_TITLE_"+counter));
      System.out.println(articleContent.getAttribute("style"));
    }
     
  }
  
  
  public void checkBreadCrumb(){
    try{
      WebElement breadCrumb = webDriver.findElement(By.className("breadcrumb"));
      if(breadCrumb.getText().trim().equals("")){
        messages.add("Bread crumb section empty on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      messages.add("Bread crumb missing on "+webDriver.getCurrentUrl());
    }
  }
  
  public void checkHeading(){
    try{
      WebElement heading = webDriver.findElement(By.className("article-header")).findElement(By.tagName("h1"));
      if(heading.getText().trim().equals("")){
        messages.add("Headding section empty on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      messages.add("Headding is missing on "+webDriver.getCurrentUrl());
    }
  }
  
  public void checkSocialButtons(){
    try {
      WebElement socialBtn = null;
      for (int i = 0; i < 3; i++) {
        try {
          socialBtn = webDriver.findElement(By.id("socialIcons"));
          break;
        } catch (Exception e) {
          try {
            Thread.sleep(3000);
          } catch (Exception te) {
          }
        }
      }      
      try{
        WebElement fb = null;
        for (int i = 0; i < 3; i++) {
          try {
            fb=socialBtn.findElement(By.id("facebookIcon"));
            break;
          } catch (Exception e) {
            try {
              Thread.sleep(3000);
            } catch (Exception te) {
            }
          }
        }  
      }catch (Exception e) {
        messages.add("FB Button missing from "+webDriver.getCurrentUrl());
      }
      try{
        WebElement tweet = null;
        for (int i = 0; i < 3; i++) {
          try {
            tweet=socialBtn.findElement(By.className("addthis_button_tweet"));
            break;
          } catch (Exception e) {
            try {
              Thread.sleep(3000);
            } catch (Exception te) {
            }
          }
        }      
          
      }catch (Exception e) {
        messages.add("Tweeter Button missing from "+webDriver.getCurrentUrl());
      } 
      try{
        WebElement google = null;
        for (int i = 0; i < 3; i++) {
          try {
            google=socialBtn.findElement(By.className("addthis_button_google_plusone"));
            break;
          } catch (Exception e) {
            try {
              Thread.sleep(3000);
            } catch (Exception te) {
            }
          }
        } 
      }catch (Exception e) {
        messages.add("Google Plus Button missing from "+webDriver.getCurrentUrl());
      } 
      try{
        WebElement print = null;
        for (int i = 0; i < 3; i++) {
          try {
            print=socialBtn.findElement(By.className("addthis_button_print"));
            break;
          } catch (Exception e) {
            try {
              Thread.sleep(3000);
            } catch (Exception te) {
            }
          }
        }         
      }catch (Exception e) {
        messages.add("Print Button missing from "+webDriver.getCurrentUrl());
      }         
      try{
        WebElement email = null;
        for (int i = 0; i < 3; i++) {
          try {
            email=socialBtn.findElement(By.className("addthis_button_email"));
            break;
          } catch (Exception e) {
            try {
              Thread.sleep(3000);
            } catch (Exception te) {
            }
          }
        }         
      }catch (Exception e) {
        messages.add("Email Button missing from "+webDriver.getCurrentUrl());
      }         
    } catch (Exception e) {
      messages.add("Social Buttons missing from "+webDriver.getCurrentUrl());
    }
  }
  
  public void checkReviewerInfo(){
    try{
      WebElement ReviewerInfo = webDriver.findElement(By.className("byline"));
      if(ReviewerInfo.getText().trim().equals("")){
        messages.add("Reviewer Info section is empty on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      messages.add("Reviewer Info section is missing on "+webDriver.getCurrentUrl());
    }
  }
  
  public void checkTextSize(){
    try{
      WebElement textSize = webDriver.findElement(By.className("text-resize"));
      if(textSize.getText().trim().equals("")){
        messages.add("Text size section besides Reviewer Info section is missing on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      messages.add("Text size section besides Reviewer Info section is missing on "+webDriver.getCurrentUrl());
    }
  }
  
  public void checkTopicReviewerInfo(){
    try{
      WebElement ReviewerInfo = webDriver.findElement(By.className("box-byblock-tc")).findElement(By.className("byline-tc"));
      if(ReviewerInfo.getText().trim().equals("")){
        messages.add("Reviewer Info section is empty on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      messages.add("Reviewer Info section is missing on "+webDriver.getCurrentUrl());
    }
  }
  
  public void checkTopicTextSize(){
    try{
      WebElement textSize = webDriver.findElement(By.className("text-resize"));
      if(textSize.getText().trim().equals("")){
        messages.add("Text size section besides Reviewer Info section is missing on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      messages.add("Text size section besides Reviewer Info section is missing on "+webDriver.getCurrentUrl());
    }
  }
  
  public void checkHead(){
    try{
      WebElement heading = webDriver.findElement(By.className("bodyheading")).findElement(By.tagName("h2"));
      if(heading.getText().trim().equals("")){
        messages.add("Topic Center Headding empty on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      messages.add("Topic Center Headding is missing on "+webDriver.getCurrentUrl());
    }
  }
  
  public void checkThreeColImageBlock(){
    try{
      WebElement colSection = webDriver.findElement(By.className("threecolimage"));
      List<WebElement> sectionImages=colSection.findElements(By.tagName("img"));
      for(WebElement sectionImage:sectionImages){
        if(sectionImage.getAttribute("src")!=null){
          CheckHttpConnection httpConnection=new CheckHttpConnection(sectionImage.getAttribute("src"));
          if(httpConnection.testHttpConn()!=200){
            messages.add(sectionImage.getAttribute("src") +" - Broken Image on "+webDriver.getCurrentUrl());
          }
        }
      }
       
      List<WebElement> sectionBoxes=colSection.findElements(By.className("box-summary"));
      for(WebElement sectionBox:sectionBoxes){
        try{
        WebElement boxTitle=sectionBox.findElement(By.tagName("h3"));
        String href=boxTitle.findElement(By.tagName("a")).getAttribute("href");
        if(href!=null){
          CheckHttpConnection httpConnection=new CheckHttpConnection(href);
          if(httpConnection.testHttpConn()!=200){
            messages.add(href +" - Box title link leads to broken page from "+webDriver.getCurrentUrl());
          }         
        }
        try{
          WebElement boxSummary=sectionBox.findElement(By.className("summary"));
          if(boxSummary.getText().trim().equals("")){
            messages.add("Box summary text missing on "+webDriver.getCurrentUrl());
          }
        }catch (Exception e) {
          messages.add("Box summary missing on "+webDriver.getCurrentUrl());
        }
        try{
          WebElement boxLink=sectionBox.findElement(By.className("learnmorelink")).findElement(By.tagName("a"));
          if(boxLink.getAttribute("href")!=null){
            CheckHttpConnection httpConnection=new CheckHttpConnection(boxLink.getAttribute("href"));
            if(httpConnection.testHttpConn()!=200){
              messages.add(href +" - Box Learn more link leads to broken page from "+webDriver.getCurrentUrl());
            }               
          }
        }catch (Exception e) {
          messages.add("Learn More link from Box missing on "+webDriver.getCurrentUrl());
        }
        
        }catch (Exception e) {
          messages.add("Box title missing on "+webDriver.getCurrentUrl());
        }
      }
    }catch (Exception e) {
       
    }
  }
  
  public void checkTwoColImageBlock(){
    try{
      WebElement colSection = webDriver.findElement(By.className("twocolist"));
      try{
      WebElement title=colSection.findElement(By.className("row12")).findElement(By.className("span12")).findElement(By.tagName("h2"));
      }catch (Exception e) {
        messages.add("Two column list section title missing on "+webDriver.getCurrentUrl());
      }
      List<WebElement> subSections=colSection.findElements(By.className("span6"));
      for(WebElement subSection:subSections){
        try{
        WebElement subTitle=subSection.findElement(By.tagName("h3"));
        }catch (Exception e) {
          messages.add("In two column list section sub title missing on "+webDriver.getCurrentUrl());
        }
        List<WebElement> sectionLinks=subSection.findElements(By.tagName("a"));
        for(WebElement sectionLink:sectionLinks){
          String href=sectionLink.getAttribute("href");
          CheckHttpConnection httpConnection=new CheckHttpConnection(href);
          if(httpConnection.testHttpConn()!=200){
            messages.add(href +" - Broken link in two column list section from "+webDriver.getCurrentUrl());
          }           
        }
      }
    }catch (Exception e) {
      // TODO: handle exception
    }
  }
  
  
  public void checkOneColumnImageBlock(){
    try{
      WebElement colSection = webDriver.findElement(By.className("onecolimage"));
      List<WebElement> sectionImages=colSection.findElements(By.tagName("img"));
      for(WebElement sectionImage:sectionImages){
        if(sectionImage.getAttribute("src")!=null){
          CheckHttpConnection httpConnection=new CheckHttpConnection(sectionImage.getAttribute("src"));
          if(httpConnection.testHttpConn()!=200){
            messages.add(sectionImage.getAttribute("src") +" - Broken Image on "+webDriver.getCurrentUrl());
          }
        }
      }
      List<WebElement> sectionBoxes=colSection.findElements(By.className("box-summary"));
      for(WebElement sectionBox:sectionBoxes){
        try{
        WebElement boxTitle=sectionBox.findElement(By.tagName("h3"));
        String href=boxTitle.findElement(By.tagName("a")).getAttribute("href");
        if(href!=null){
          CheckHttpConnection httpConnection=new CheckHttpConnection(href);
          if(httpConnection.testHttpConn()!=200){
            messages.add(href +" - Box title link leads to broken page from "+webDriver.getCurrentUrl());
          }         
        }
        try{
          WebElement boxSummary=sectionBox.findElement(By.className("summary"));
          if(boxSummary.getText().trim().equals("")){
            messages.add("Box summary text missing on "+webDriver.getCurrentUrl());
          }
        }catch (Exception e) {
          messages.add("Box summary missing on "+webDriver.getCurrentUrl());
        }
        try{
          WebElement boxLink=sectionBox.findElement(By.className("learnmorelink")).findElement(By.tagName("a"));
          if(boxLink.getAttribute("href")!=null){
            CheckHttpConnection httpConnection=new CheckHttpConnection(boxLink.getAttribute("href"));
            if(httpConnection.testHttpConn()!=200){
              messages.add(href +" - Box Learn more link leads to broken page from "+webDriver.getCurrentUrl());
            }               
          }
        }catch (Exception e) {
          messages.add("Learn More link from Box missing on "+webDriver.getCurrentUrl());
        }
        
        }catch (Exception e) {
          messages.add("Box title missing on "+webDriver.getCurrentUrl());
        }
      }
    }catch (Exception e) {
      // TODO: handle exception
    }
  }
  
  public void checkIntroduction(){
    try{
      WebElement intro=webDriver.findElement(By.id("introduction"));
      if(intro.getText().trim().equals("")){
        messages.add("Text missing in introduction section on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      // TODO: handle exception
    }
  }
  
  public void checkCarousal(){
    try{
      WebElement carousal=webDriver.findElement(By.className("flexslider"));
      List<WebElement> slides=carousal.findElements(By.className("slides"));
      for(WebElement slide:slides){
        WebElement slideImage=slide.findElement(By.tagName("img"));
        if(slideImage.getAttribute("src")!=null){
          CheckHttpConnection httpConnection=new CheckHttpConnection(slideImage.getAttribute("src"));
          if(httpConnection.testHttpConn()!=200){
            messages.add(slideImage.getAttribute("src") +" - Broken Carousal Slide Image on "+webDriver.getCurrentUrl());
          }
        }
        try{
        WebElement slideBoxTitle=slide.findElement(By.className("box-summary")).findElement(By.tagName("h3"));
        if(slideBoxTitle.getText().trim().equals("")){
          messages.add("Summary Box heading is missing in Carousal Slide on "+webDriver.getCurrentUrl());
        }
        try{        
          WebElement slideBoxLink=slide.findElement(By.className("box-summary")).findElement(By.tagName("h3")).findElement(By.tagName("a"));
          if(slideBoxLink.getAttribute("href")!=null){
            CheckHttpConnection httpConnection=new CheckHttpConnection(slideBoxLink.getAttribute("href"));
            if(httpConnection.testHttpConn()!=200){
              messages.add(slideBoxLink.getAttribute("href") +" - is Broken Carousal Slide title-link on "+webDriver.getCurrentUrl());
            }
             
          }
          }catch (Exception e) {
            messages.add("link is missing in Carousal Slide on "+webDriver.getCurrentUrl());
          } 
        
        }catch (Exception e) {
          messages.add("Summary Box heading is missing in Carousal Slide on "+webDriver.getCurrentUrl());
        }
        try{        
        WebElement slideBoxSummary=slide.findElement(By.className("box-summary")).findElement(By.tagName("p"));
        if(slideBoxSummary.getText().trim().equals("")){
          messages.add("Summary is missing in Carousal Slide on "+webDriver.getCurrentUrl());
        }
        }catch (Exception e) {
          messages.add("Summary is missing in Carousal Slide on "+webDriver.getCurrentUrl());
        }        
        try{        
          WebElement slideBoxLink=slide.findElement(By.className("box-summary")).findElement(By.tagName("p")).findElement(By.tagName("a"));
          if(slideBoxLink.getAttribute("href")!=null){
            CheckHttpConnection httpConnection=new CheckHttpConnection(slideBoxLink.getAttribute("href"));
            if(httpConnection.testHttpConn()!=200){
              messages.add(slideBoxLink.getAttribute("href") +" - is Broken Carousal Slide link on "+webDriver.getCurrentUrl());
            }
             
          }
          }catch (Exception e) {
            messages.add("link is missing in Carousal Slide on "+webDriver.getCurrentUrl());
          }          
      }
    try{
      WebElement navControl=carousal.findElement(By.className("flex-control-nav"));  
    }catch (Exception e) {
      messages.add("Navigator control buttons missing from Carousal Slide on "+webDriver.getCurrentUrl());
    }
     
      
    }catch (Exception e) {
      // TODO: handle exception
    }
  }
  
  public void checkBlogSection(){
    try{
      List<WebElement> blogBoxes=webDriver.findElements(By.className("box-blog"));
      for(WebElement blogBox:blogBoxes){
        try{
          WebElement blogSecTitle=blogBox.findElement(By.tagName("h2"));
        if(blogSecTitle.getText().trim().equals("")){
          messages.add("Title missing for Blog section on "+webDriver.getCurrentUrl());
        }
        }catch (Exception e) {
          messages.add("Title missing for Blog section on "+webDriver.getCurrentUrl());
        }
        try{
          WebElement blogImage=blogBox.findElement(By.tagName("img"));
          if(blogImage.getAttribute("src")!=null){
            CheckHttpConnection httpConnection=new CheckHttpConnection(blogImage.getAttribute("src"));
            if(httpConnection.testHttpConn()!=200){
              messages.add(blogImage.getAttribute("src") +" - Broken Blogger Image on "+webDriver.getCurrentUrl());
            }
          }
        }catch (Exception e) {
          messages.add("Blogger Image is missing on "+webDriver.getCurrentUrl());
        }
        List<WebElement> blogLinks=blogBox.findElements(By.tagName("a"));
        for(WebElement blogLink:blogLinks){
          if(blogLink.getAttribute("href")!=null){
            CheckHttpConnection httpConnection=new CheckHttpConnection(blogLink.getAttribute("href"));
            if(httpConnection.testHttpConn()!=200){
              messages.add(blogLink.getAttribute("href") +" - Broken link from Blog section on "+webDriver.getCurrentUrl());
            }
          }
        }
      }
    }catch (Exception e) {
      // TODO: handle exception
    }
  }
  
  public void checkNavDropdown(){
    try{
      WebElement healthTopicsTab=webDriver.findElement(By.id("tGActiveTc"));
      Actions actions = new Actions(webDriver);
      actions.moveToElement(healthTopicsTab).release(healthTopicsTab).build()
          .perform();
      List<WebElement> dropDownLinks = webDriver.findElement(
          By.className("innersubmenubox")).findElements(By.tagName("a"));
      if (dropDownLinks.size() <= 0) {
        messages.add("No links listed in Health Topics Nav Dropdown List");
      }

      for (WebElement dropDownLink : dropDownLinks) {
        if (dropDownLink.getAttribute("href") != null) {
          CheckHttpConnection urlHttpConn = new CheckHttpConnection(
              dropDownLink.getAttribute("href"));
          int status = urlHttpConn.testHttpConn();
          if (status != 200) {
            messages
                .add(dropDownLink.getText()
                    + " link from Health Topics Nav TAB dropdown leads to 'Oops!Page'");
          }
        }
      }

    }catch (Exception e) {
      messages.add("Topic center TAB is misisng on Global Navigator header"+webDriver.getCurrentUrl());
    }
  }
  
  public void checkWSR(){ 
    try{
      WebElement mr1=webDriver.findElement(By.className("wsrbox"));
      CheckAds checkAds=new CheckAds(webDriver);
      msgAds = checkAds.checkRhsAdsWsr();
      if (msgAds != null) {
        messages.add(msgAds);
      }
    }catch (Exception e) {
      // TODO: handle exception
    }   
  }
  
  public void checkMR1(){
    try{
      WebElement mr1=webDriver.findElement(By.className("mr1block"));
      CheckAds checkAds=new CheckAds(webDriver);
      msgAds = checkAds.checkRhsAdsMr1();
      if (msgAds != null) {
        messages.add(msgAds);
      }
    }catch (Exception e) {
      // TODO: handle exception
    }
  }

  public void checkMR2(){
    try{
      WebElement mr2=webDriver.findElement(By.className("mr1block"));
      CheckAds checkAds=new CheckAds(webDriver);
      msgAds = checkAds.checkRhsAdsMr2();
      if (msgAds != null) {
        messages.add(msgAds);
      }
    }catch (Exception e) {
      // TODO: handle exception
    }  
  }

  public void checkRegistration(){
    try{
      WebElement regWidget=webDriver.findElement(By.className("registration"));
      WebElement emailBox=regWidget.findElement(By.id("email"));
      emailBox.sendKeys("tester@healthline.com");
      emailBox.submit();
      Thread.sleep(3000);
      WebElement boxText=regWidget.findElement(By.id("signupBody"));
      if(!boxText.getText().contentEquals("Thanks for signing up. You will receive a confirmation email soon.")){
        messages.add("Newsletter widget functionality broken on "+webDriver.getCurrentUrl());
      }
    }catch (Exception e) {
      // TODO: handle exception
    }
  }
  
  public void checkTopStories(){
    try{
      WebElement topStories=webDriver.findElement(By.className("topstories"));
      List<WebElement> topStoryImages=topStories.findElements(By.tagName("img"));
      for(WebElement topStoryImage:topStoryImages){
        if(topStoryImage.getAttribute("src")!=null){
          CheckHttpConnection httpConnection=new CheckHttpConnection(topStoryImage.getAttribute("src"));
          if(httpConnection.testHttpConn()!=200){
            messages.add(topStoryImage.getAttribute("src") +" - Broken Image in Top Stories section on "+webDriver.getCurrentUrl());
          }
        }
      }
      List<WebElement> topStoryLinks=topStories.findElements(By.tagName("a"));
      for(WebElement topStoryLink:topStoryLinks){
        if(topStoryLink.getAttribute("href")!=null){
          CheckHttpConnection httpConnection=new CheckHttpConnection(topStoryLink.getAttribute("href"));
          if(httpConnection.testHttpConn()!=200){
            messages.add(topStoryLink.getAttribute("href") +" - Broken link in Top Stories section on "+webDriver.getCurrentUrl());
          }
        }
      }
    }catch (Exception e) {
      // TODO: handle exception
    }
  }
  
  public List<String> getMessages() {
    return messages;
  }
}

