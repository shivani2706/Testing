package qalib;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckSuperLearningCenter {

  private WebDriver webDriver;
  private List<String> messages = new ArrayList<String>();
  private String relatedImages;

  public CheckSuperLearningCenter(WebDriver driver) {
    webDriver = driver;
    webDriver.getCurrentUrl();
  }

  public void checkNow() {
    checkShareTools();
    checkHealthTools();
    checkLCTitle();
    checkRelatedTopics();
    checkFeaturedConditions();
    checkVideos();
    checkNewsFeatures();
    checkImages();
    if (relatedImages != null) {
      checkIMG("related Images", relatedImages);
    }
  }

  public void checkHealthTools() {
    WebElement healthTools = webDriver.findElement(By.id("aarp-tools-widget"));
    List<WebElement> heathToolLinks = healthTools.findElements(By.className("entry-title"));
    boolean found = false;
    for (int j = 0; j <= heathToolLinks.size() - 1; j++) {
      String Title = heathToolLinks.get(j).getText();
      if (Title == "Condition & Treatment Search") {
        found = true;
      }
      if (Title == "Symptom Search") {
        found = true;
      }
      if (Title != "Drug Search") {
        found = true;
      }
    }
    if (found != true) {
      messages.add("Health Tools are missing");
    }
  }

  public void checkLCTitle() {
    try {
      WebElement lcTitle = webDriver.findElement(By.className("contentHeader"));
      lcTitle.findElement(By.tagName("h1"));
    } catch (Exception e) {
      messages.add("Article Title is missing");
    }
  }

  public void checkRelatedTopics() {

    List<WebElement> Container = webDriver.findElements(By.xpath("//div[@class='parbase contentWrapper']"));
    for (int i = 0; i <= Container.size() - 1; i++) {
      WebElement section = null;
      for (int k = 0; k < 3; k++) {
        section = Container.get(i).findElement(By.tagName("h2"));
      }
      String sectionName = section.getText();
      if (sectionName.contains("TOPICS")) {
        List<WebElement> TopicLinks = Container.get(i).findElements(By.tagName("a"));
        for (WebElement topic : TopicLinks) {
          String Href = topic.getAttribute("href");
          CheckHttpConnection con = null;
          if (Href != null) {
            con = new CheckHttpConnection(Href);
            int status = con.testHttpConn();
            if (status != 200) {
              messages.add("Related topic link is broken : " + " " + Href + " failed : " + con.getMessage() + " " + con.getStatus());
            }
          }
        }
        break;
      }
    }
  }

  public void checkFeaturedConditions() {

    WebElement conditions = webDriver.findElement(By.id("aarp-super-lc"));
    List<WebElement> tagname = conditions.findElements(By.tagName("h3"));
    for (WebElement conditionLink : tagname) {
      String Href = conditionLink.findElement(By.tagName("a")).getAttribute("href");
      CheckHttpConnection con = null;
      if (Href != null) {
        con = new CheckHttpConnection(Href);
        int status = con.testHttpConn();

        if (status != 200) {
          messages.add("Featured condition link is broken : " + " " + Href + " failed : " + con.getMessage() + " " + con.getStatus());
        }
      }
    }
  }

  public void checkNewsFeatures() {

    List<WebElement> Container = webDriver.findElements(By.xpath("//div[@class='section parbase contentWrapper']"));
    WebElement section = null;
    for (int k = 0; k < 3; k++) {
      section = Container.get(3).findElement(By.tagName("h2"));
    }
    List<WebElement> FeaturesLinks = section.findElements(By.tagName("a"));
    for (WebElement features : FeaturesLinks) {
      String Href = features.getAttribute("href");
      CheckHttpConnection con = null;
      if (Href != null) {
        con = new CheckHttpConnection(Href);
        int status = con.testHttpConn();
        if (status != 200) {
          messages.add("News & Features link is broken : " + " " + Href + " failed : " + con.getMessage() + " " + con.getStatus());
        }
      }
    }
  }

  public void checkImages() {
    try {
      WebElement tabContainer = webDriver.findElement(By.id("aarp-lcTab"));
      List<WebElement> tabs = tabContainer.findElements(By.tagName("a"));
      for (WebElement tab : tabs) {
        String tabName = tab.getText();

        if (tabName.contains("IMAGES")) {
          WebElement data1 = webDriver.findElement(By.id("data1"));
          List<WebElement> links = data1.findElements(By.tagName("a"));
          relatedImages = links.get(0).getAttribute("href");
          break;
        }
      }
    } catch (Exception e) {
      //Do nothing
    }
  }

  public void checkVideos() {
    try {
      WebElement tabContainer = webDriver.findElement(By.id("aarp-lcTab"));
      List<WebElement> tabs = tabContainer.findElements(By.tagName("a"));
      for (WebElement tab : tabs) {
        String tabName = tab.getText();

        if (tabName.contains("VIDEOS")) {

          WebElement data0 = webDriver.findElement(By.id("data0"));
          List<WebElement> videoLinks = data0.findElements(By.tagName("a"));

          for (WebElement videos : videoLinks) {
            String Href = videos.getAttribute("href");
            CheckHttpConnection con = null;
            if (Href != null) {
              con = new CheckHttpConnection(Href);
              int status = con.testHttpConn();

              if (status != 200) {
                messages.add("Video link is broken : " + " " + Href + " failed : " + con.getMessage() + " " + con.getStatus());
              }
            }
          }
          break;
        }
      }
    } catch (Exception e) {
      //Do nothing
    }
  }

  public void checkIMG(String sectionName, String sectionLink) {
    webDriver.get(sectionLink);

    /*
     * Check 1st Image
     */

    try {
      WebElement image = webDriver.findElement(By.id("lightbox-container-image"));
      image.findElement(By.tagName("img")).getAttribute("src");
    } catch (Exception e) {
      messages.add("Image is missing");
    }

    /*
     * Check Image rail.
     */
    try {
      WebElement imagelinks = webDriver.findElement(By.id("carousel-box"));
      List<WebElement> img = imagelinks.findElements(By.tagName("li"));
      String urls = "";
      CheckHttpConnection conn = null;

      for (int j = 0; j <= img.size() - 1; j++) {
        urls = img.get(j).getAttribute("src");
        if (urls != null) {
          conn = new CheckHttpConnection(webDriver.getCurrentUrl());
          int status = conn.testHttpConn();
          if (status != 200) {
            messages.add("Image link is broken : " + " " + urls + " failed : " + conn.getMessage() + " " + conn.getStatus());
          }
        }
      }
    } catch (Exception e) {
      //Do nothing
    }
  }

  public void checkShareTools() {
    try {
      List<WebElement> shareTools = webDriver.findElements(By.className("articleShareTools"));
    } catch (Exception e) {
      messages.add("Share tools are missing");
    }
  }

  public List<String> getMessages() {
    return messages;
  }
}