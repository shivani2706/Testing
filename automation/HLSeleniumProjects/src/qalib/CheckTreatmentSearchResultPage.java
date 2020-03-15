package qalib;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Dipti 04/27/2013 to 05/01/2013 - Check various elements of SxS Result page
 */

public class CheckTreatmentSearchResultPage {

  private WebDriver webDriver;
  private String baseURL;
  private List<String> messages = new ArrayList<String>();

  public CheckTreatmentSearchResultPage(WebDriver driver, String baseUrl) {
    baseURL = baseUrl;
    webDriver = driver;

  }

  public void checkNow() {
    try {
      WebElement pageContent = webDriver.findElement(By.id("pagecontent"));
      System.out.println("hh");
      if (pageContent != null) {
        testTitle();
        testBreadCrumbs();
        checkRelatedCount();
        checkFindDoctor();
        check3DVideo();
        checkOverview();
        checkTests();
        checkSurgicalProcedure();
        checkMedicalProcedure();
        checkPrescribedMedications();
        checkAlternativeProcedures();
        checkHerbsSupplements();
       // checkUpperAd();
       // checkRhsAdsMr1();
       // checkRhsAdsMr2();
       // testNetseerAd();
        testSocialButtons();
        //checkWelcomeSection(); //welcome section image click results in blank page. It's known bug so commented this method.
      }
    } catch (Exception e) {
      System.out.println(e);
      messages.add("Breast cancer treatment page content is missing");
    }
  }

  public void checkTreatment() {
    try {
      WebElement pageContent = webDriver.findElement(By.id("pagecontent"));
      List<WebElement> sections = webDriver.findElements(By
          .className("yellowBox"));
      boolean titleExist = false;
      boolean summaryExist = false;
      String style = "";
      for (WebElement section : sections) {
        style = section.getAttribute("style");

        if (style
            .equals("font-weight: bold; text-decoration: none; padding: 10px 0px 10px 15px;")) {

          titleExist = true;
        }
        if (style.equals("padding: 0px 0px 10px 15px;")) {
          if (section.getText().startsWith("Learn More")) {
            WebElement learnMore = section.findElement(By.tagName("a"));
            CheckHttpConnection httpConnection = new CheckHttpConnection(
                learnMore.getAttribute("href"));
            if (httpConnection.testHttpConn() != 200) {
              messages
                  .add("Learn More link is broken from TxS result page for treatment");
            }

            try {

              WebElement conditionLink = webDriver.findElement(By
                  .id("diseasesPopupLink"));
              conditionLink.click();
              Thread.sleep(3000);
              try {
                WebElement conditionPop = webDriver.findElement(By
                    .id("overlayDiseasesPopup"));
                try {
                  WebElement defTitle = conditionPop.findElement(By
                      .className("def-title"));
                } catch (Exception e) {
                  messages
                      .add("Condition Popup title is missing on TxS result page for treatment");
                }
                try {
                  List<WebElement> condLinks = conditionPop.findElements(By
                      .className("a"));
                  for (WebElement condLink : condLinks) {
                    String href = condLink.getAttribute("href");
                    if (href != null) {
                      CheckHttpConnection httpConn = new CheckHttpConnection(
                          href);
                      int status = httpConn.testHttpConn();

                      if (status != 200) {
                        messages
                            .add("link from Condition popup is broken from result page for treatment status = "
                                + status);
                      }
                    }
                  }
                } catch (Exception e) {
                  messages
                      .add("Any Links not listed in Condition Popup of TxS result page for treatment");
                }
              } catch (Exception e) {
                messages
                    .add("Condition Popup not display on TxS result page for treatment");
              }
            } catch (Exception e) {
              messages
                  .add("Condition Popup not display on TxS result page for treatment");
            }

          } else {
            summaryExist = true;
          }

        }
      }
      testTitle();
      testBreadCrumbs();
      //checkRhsAdsMr1();
      //checkRhsAdsMr2();
      //testNetseerAd();
      testSocialButtons();
      //checkWelcomeSection();//welcome section image click results in blank page. It's known bug so commented this method.
    } catch (Exception e) {
      System.out.println(e);
      messages.add("Content is missing on TxS result page for treatment");
    }
  }

  public void checkUpperAd() {
    try {
      CheckAds upperAds = new CheckAds(webDriver);
      String message = upperAds.checkUpperAd();
      if (message != null) {
        messages.add("On TxS landing page!" + message);
      } else {
      }
    } catch (Exception e) {
    }
  }

  public void checkRhsAdsMr1() {
    try {
      CheckAds mr1Ads = new CheckAds(webDriver);
      String message = mr1Ads.checkRhsAdsMr1();
      if (message != null) {
        messages.add("On TxS landing page!" + message);
      } else {
      }
    } catch (Exception e) {
    }
  }

  public void checkRhsAdsMr2() {
    try {
      CheckAds mr2Ads = new CheckAds(webDriver);
      String message = mr2Ads.checkRhsAdsMr2();
      if (message != null) {
        messages.add("On TxS landing page!" + message);
      } else {
      }
    } catch (Exception e) {
    }
  }

  public void testNetseerAd() {
    try {
      CheckAds netseerAds = new CheckAds(webDriver);
      String message = netseerAds.checkNetseerAds();
      if (message != null) {
        messages.add("On TxS landing page " + message);
      } else {
      }
    } catch (Exception e) {
    }
  }

  public void testTitle() {
    try {
      WebElement title = webDriver.findElement(By.className("bodyheading"))
          .findElement(By.tagName("h2"));
      if (title.getText().trim().equals("")) {
        messages.add("Title text missing for TxS result page");
      }

    } catch (Exception e) {
      messages.add("Title text missing for TxS result page");
    }

  }

  public void testBreadCrumbs() {
    try {
      WebElement breadCrumb = webDriver.findElement(By.className("breadcrumb"));
      if (breadCrumb.getText().trim().equals("")) {
        messages.add("Breadcrumbs missing for TxS result page");
      }

    } catch (Exception e) {
      messages.add("Breadcrumbs missing for TxS result page");
    }
  }

  public void testSocialButtons() {
    try {
      WebElement socialBtn = webDriver.findElement(By.className("hl-widget-sharebar"));
    } catch (Exception e) {
      messages.add("Social Buttons from TxS result page");
    }
  }

  public void checkRelatedCount() {
    try {
      WebElement relatedCount = webDriver.findElement(
          By.className("pagecontent"))
          .findElement(By.className("relatedCount"));
      if (relatedCount.getText().trim().equals("")) {
        messages.add("Related count text is missing on TxS result page");
      }
    } catch (Exception e) {
      messages.add("Related count is missing on TxS result page");
    }

  }

  public void checkRelatedItems() {
    try {
      WebElement relatedItems = webDriver.findElement(By
          .className("relatedItems"));
    } catch (Exception e) {
      messages.add("Related Items is missing on TxS result page");
    }
  }

  public void checkFindDoctor() {
    try {
      WebElement findDoctor = webDriver.findElement(By.id("doctorsPopupLink"));
      WebElement findDoctorImg = findDoctor.findElement(By.tagName("img"));
      CheckHttpConnection httpConnection = new CheckHttpConnection(
          findDoctorImg.getAttribute("src"));
      if (httpConnection.testHttpConn() != 200) {
        messages
            .add("FIND A DOCTOR button image is missing on TxS result page");
      }
      findDoctor.click();
      Thread.sleep(3000);
      WebElement docOverlay = webDriver.findElement(By
          .id("overlayDoctorsPopup"));

      if (docOverlay != null) {
        try {
          WebElement docTitle = docOverlay.findElement(By
              .className("def-title"));
          if (docTitle.getText().trim().equals("")) {
            messages
                .add("Title missing in Find a Doctor Overlay from TxS result page");
          }

        } catch (Exception e) {
          messages
              .add("Title missing in Find a Doctor Overlay from TxS result page");
        }
        try {
          List<WebElement> docLinks = docOverlay.findElement(
              By.className("scrollableFilter")).findElements(By.tagName("a"));
          if (docLinks.size() == 0) {
            messages
                .add("Any Doctor link is not listed in FIND A DOCTOR popup from TxS result page");
          }
          String oldURL = webDriver.getCurrentUrl();

          webDriver.get(docLinks.get(0).getAttribute("href"));
          Thread.sleep(3000);
          if (!webDriver.getCurrentUrl().contains("/doctors/")) {
            messages
                .add("Link from FIND A DOCTOR popup not leads to proper page");
          }
          webDriver.get(oldURL);
        } catch (Exception e) {
          // TODO: handle exception
        }
      }

    } catch (Exception e) {
      messages.add("FIND A DOCTOR button is missing on TxS result page");
    }
  }

  public void check3DVideo() {
    try {
      WebElement pick3Dvideo = webDriver.findElement(By.id("videosPopupLink"));
      WebElement findVideoImg = pick3Dvideo.findElement(By.tagName("img"));
      CheckHttpConnection httpConnection = new CheckHttpConnection(
          findVideoImg.getAttribute("src"));
      if (httpConnection.testHttpConn() != 200) {
        messages
            .add("FIND A DOCTOR button image is missing on TxS result page");
      }
      pick3Dvideo.click();
      Thread.sleep(3000);
      WebElement videoOverlay = webDriver.findElement(By
          .id("overlayVideosPopup"));
      if (videoOverlay != null) {
        WebElement defTitle = webDriver.findElement(By.id("relatedVideos"))
            .findElement(By.className("def-title"));
        if (defTitle.getText().trim().equals("")) {
          messages.add("Definition title missing in 3D Video popup");
        }

      }
      try {
        WebElement viewAll = webDriver.findElement(By.id("relatedVideos"))
            .findElement(By.id("viewAllVideos"));
      } catch (Exception e) {
        messages.add("View All link from 3D Video popup is missing");
      }
      /*
       * try { WebElement closeBtn =
       * videoOverlay.findElement(By.className("overlay-footer"
       * )).findElement(By.id("overlay-close")); } catch (Exception e) {
       * messages.add("Close button from 3D Video popup is missing"); }
       */
      List<WebElement> videos = webDriver.findElement(By.id("relatedVideos"))
          .findElement(By.id("scrollableDiv")).findElements(By.tagName("div"));
      for (WebElement video : videos) {
        if (video.getAttribute("style").startsWith(
            "float: left; cursor: pointer;")) {

          List<WebElement> bodyMapLinks = video.findElements(By.tagName("a"));
          CheckHttpConnection httpConnection1 = new CheckHttpConnection(
              bodyMapLinks.get(0).getAttribute("href"));
          if (httpConnection1.testHttpConn() != 200) {
            messages.add(bodyMapLinks.get(0).getAttribute("href")
                + " VP Video link is broken from 3D Videos popup "
                + httpConnection.testHttpConn());
          }
          WebElement bodyMapImage = video.findElement(By.tagName("img"));
          if (bodyMapImage.getAttribute("src") != null) {
            CheckHttpConnection httpConnection2 = new CheckHttpConnection(
                bodyMapImage.getAttribute("src"));
            if (httpConnection2.testHttpConn() != 200) {
              messages
                  .add(" VP Video thumbnail is broken from 3D Videos popup ");
            }

          }
        }

        if (video.getAttribute("style").startsWith("float: left; width: 80px;")) {

          List<WebElement> fiveminLinks = video.findElements(By.tagName("a"));
          for (WebElement fiveminLink : fiveminLinks) {
            CheckHttpConnection httpConnection3 = new CheckHttpConnection(
                fiveminLink.getAttribute("href"));
            if (httpConnection3.testHttpConn() != 200) {
              messages.add(fiveminLink.getAttribute("href")
                  + " 5 Minute Video link is broken from 3D Videos popup "
                  + httpConnection3.testHttpConn());
            }
          }
          List<WebElement> fiveminImages = video
              .findElements(By.tagName("img"));
          for (WebElement fiveminImage : fiveminImages) {
            String src = fiveminImage.getAttribute("src");
            if (src != null) {
              CheckHttpConnection httpConnection4 = new CheckHttpConnection(src);
              if (httpConnection4.testHttpConn() != 200) {
                messages
                    .add(fiveminImage.getAttribute("src")
                        + " 5 Minute Video thumbnail is broken from 3D Videos popup ");
              }

            }
          }

        }
      }

    } catch (Exception e) {
      messages.add("3D VIDEOS button is missing from TxS result page");
    }

  }

  public void checkOverview() {
    try {
      WebElement overview = webDriver.findElement(By.className("overview"));

      try {
        WebElement overviewText = webDriver.findElement(
            By.className("entryintro")).findElement(By.tagName("div"));
        if (overviewText.getText().trim().equals("")) {
          messages.add("Overview section text is missing in TxS result page");
        }
      } catch (Exception e) {
        messages.add("Overview section is missing in TxS result page");
      }
      WebElement readMore = webDriver
          .findElement(By.linkText("[read article]"));
      CheckHttpConnection httpConnection = new CheckHttpConnection(
          readMore.getAttribute("href"));
      if (httpConnection.testHttpConn() != 200) {
        messages
            .add("Read More link from the Overview section is broken from TxS result page");
      }
    } catch (Exception e) {
      messages.add("Overview section header missing in TxS result page");
    }
  }

  public void checkTests() {
    try {
      WebElement testHead = webDriver.findElement(By.id("diagnostic"));
      if (testHead.getText().trim().equals("")) {
        messages
            .add("section Header above Tests section is missing from TxS result page");
      }
      List<WebElement> entryList = webDriver.findElements(By
          .className("entryTable_box"));
      List<WebElement> testLinks = entryList.get(0).findElements(
          By.className("entryItem"));

      for (WebElement testLink : testLinks) {
        boolean isLink = false;

        try {
          WebElement testItemTitle = testLink.findElement(
              By.className("item_title")).findElement(By.tagName("a"));
          if (testItemTitle != null) {

            isLink = true;
            CheckHttpConnection httpConnection = new CheckHttpConnection(
                testItemTitle.getAttribute("href"));
            if (httpConnection.testHttpConn() != 200) {
              messages.add(testItemTitle.getAttribute("href")
                  + " link is broken from Test section of TxS Result page");
            }
          }
        } catch (Exception e) {
          // TODO: handle exception
        }
        if (isLink == true) {
          WebElement testItemDesc = testLink.findElement(By
              .className("item_description"));
          if (testItemDesc.getText().trim().equals("")) {
            messages
                .add("description text is missing in Test section of TxS result page");
          }
          try {
            if (testItemDesc.findElement(By.tagName("a")) != null) {
              CheckHttpConnection httpConnection = new CheckHttpConnection(
                  testItemDesc.findElement(By.tagName("a"))
                      .getAttribute("href"));
              if (httpConnection.testHttpConn() != 200) {
                messages.add(testItemDesc.findElement(By.tagName("a"))
                    .getAttribute("href")
                    + " link is broken from Test section of TxS Result page");
              }
            }
          } catch (Exception e) {
            // TODO: handle exception
          }
        }
      }

    } catch (Exception e) {

    }
  }

  public void checkSurgicalProcedure() {
    try {
      WebElement testHead = webDriver.findElement(By.id("surgical"));
      if (testHead.getText().trim().equals("")) {
        messages
            .add("section Header above Surgical section is missing from TxS result page");
      }
      List<WebElement> entryList = webDriver.findElements(By
          .className("entryTable_box"));
      List<WebElement> testLinks = entryList.get(0).findElements(
          By.className("entryItem"));

      for (WebElement testLink : testLinks) {
        boolean isLink = false;

        try {
          WebElement testItemTitle = testLink.findElement(
              By.className("item_title")).findElement(By.tagName("a"));
          if (testItemTitle != null) {

            isLink = true;
            CheckHttpConnection httpConnection = new CheckHttpConnection(
                testItemTitle.getAttribute("href"));
            if (httpConnection.testHttpConn() != 200) {
              messages.add(testItemTitle.getAttribute("href")
                  + " link is broken from Surgical section of TxS Result page");
            }
          }
        } catch (Exception e) {
          // TODO: handle exception
        }
        if (isLink == true) {
          WebElement testItemDesc = testLink.findElement(By
              .className("item_description"));
          if (testItemDesc.getText().trim().equals("")) {
            messages
                .add("description text is missing in Surgical section of TxS result page");
          }
          try {
            if (testItemDesc.findElement(By.tagName("a")) != null) {
              CheckHttpConnection httpConnection = new CheckHttpConnection(
                  testItemDesc.findElement(By.tagName("a"))
                      .getAttribute("href"));
              if (httpConnection.testHttpConn() != 200) {
                messages
                    .add(testItemDesc.findElement(By.tagName("a"))
                        .getAttribute("href")
                        + " link is broken from Surgical section of TxS Result page");
              }
            }
          } catch (Exception e) {
            // TODO: handle exception
          }
        }
      }

    } catch (Exception e) {

    }
  }

  public void checkMedicalProcedure() {
    try {
      WebElement testHead = webDriver.findElement(By.id("medical"));
      if (testHead.getText().trim().equals("")) {
        messages
            .add("section Header above Medical Procedures section is missing from TxS result page");
      }
      List<WebElement> entryList = webDriver.findElements(By
          .className("entryTable_box"));
      List<WebElement> testLinks = entryList.get(0).findElements(
          By.className("entryItem"));

      for (WebElement testLink : testLinks) {
        boolean isLink = false;

        try {
          WebElement testItemTitle = testLink.findElement(
              By.className("item_title")).findElement(By.tagName("a"));
          if (testItemTitle != null) {

            isLink = true;
            CheckHttpConnection httpConnection = new CheckHttpConnection(
                testItemTitle.getAttribute("href"));
            if (httpConnection.testHttpConn() != 200) {
              messages
                  .add(testItemTitle.getAttribute("href")
                      + " link is broken from Medical Procedures section of TxS Result page");
            }
          }
        } catch (Exception e) {
          // TODO: handle exception
        }
        if (isLink == true) {
          WebElement testItemDesc = testLink.findElement(By
              .className("item_description"));
          if (testItemDesc.getText().trim().equals("")) {
            messages
                .add("description text is missing in Medical Procedures section of TxS result page");
          }
          try {
            if (testItemDesc.findElement(By.tagName("a")) != null) {
              CheckHttpConnection httpConnection = new CheckHttpConnection(
                  testItemDesc.findElement(By.tagName("a"))
                      .getAttribute("href"));
              if (httpConnection.testHttpConn() != 200) {
                messages
                    .add(testItemDesc.findElement(By.tagName("a"))
                        .getAttribute("href")
                        + " link is broken from Medical Procedures section of TxS Result page");
              }
            }
          } catch (Exception e) {
            // TODO: handle exception
          }
        }
      }

    } catch (Exception e) {

    }
  }

  public void checkPrescribedMedications() {
    try {
      WebElement testHead = webDriver.findElement(By.id("prescriptions"));
      if (testHead.getText().trim().equals("")) {
        messages
            .add("section Header above Prescribed Medications section is missing from TxS result page");
      }
      List<WebElement> entryList = webDriver.findElements(By
          .className("entryTable_box"));
      List<WebElement> testLinks = entryList.get(0).findElements(
          By.className("entryItem"));

      for (WebElement testLink : testLinks) {
        boolean isLink = false;

        try {
          WebElement testItemTitle = testLink.findElement(
              By.className("item_title")).findElement(By.tagName("a"));
          if (testItemTitle != null) {

            isLink = true;
            CheckHttpConnection httpConnection = new CheckHttpConnection(
                testItemTitle.getAttribute("href"));
            if (httpConnection.testHttpConn() != 200) {
              messages
                  .add(testItemTitle.getAttribute("href")
                      + " link is broken from Prescribed Medications section of TxS Result page");
            }
          }
        } catch (Exception e) {
          // TODO: handle exception
        }
        if (isLink == true) {
          WebElement testItemDesc = testLink.findElement(By
              .className("item_description"));
          if (testItemDesc.getText().trim().equals("")) {
            messages
                .add("description text is missing in Prescribed Medications section of TxS result page");
          }
          try {
            if (testItemDesc.findElement(By.tagName("a")) != null) {
              CheckHttpConnection httpConnection = new CheckHttpConnection(
                  testItemDesc.findElement(By.tagName("a"))
                      .getAttribute("href"));
              if (httpConnection.testHttpConn() != 200) {
                messages
                    .add(testItemDesc.findElement(By.tagName("a"))
                        .getAttribute("href")
                        + " link is broken from Medical Procedures section of TxS Result page");
              }
            }
          } catch (Exception e) {
            // TODO: handle exception
          }
        }
      }

    } catch (Exception e) {

    }
  }

  public void checkAlternativeProcedures() {
    try {
      WebElement testHead = webDriver.findElement(By.id("alternative"));
      if (testHead.getText().trim().equals("")) {
        messages
            .add("section Header above Alternative Procedures section is missing from TxS result page");
      }
      List<WebElement> entryList = webDriver.findElements(By
          .className("entryTable_box"));
      List<WebElement> testLinks = entryList.get(0).findElements(
          By.className("entryItem"));

      for (WebElement testLink : testLinks) {
        boolean isLink = false;

        try {
          WebElement testItemTitle = testLink.findElement(
              By.className("item_title")).findElement(By.tagName("a"));
          if (testItemTitle != null) {

            isLink = true;
            CheckHttpConnection httpConnection = new CheckHttpConnection(
                testItemTitle.getAttribute("href"));
            if (httpConnection.testHttpConn() != 200) {
              messages
                  .add(testItemTitle.getAttribute("href")
                      + " link is broken from Alternative Procedures section of TxS Result page");
            }
          }
        } catch (Exception e) {
          // TODO: handle exception
        }
        if (isLink == true) {
          WebElement testItemDesc = testLink.findElement(By
              .className("item_description"));
          if (testItemDesc.getText().trim().equals("")) {
            messages
                .add("description text is missing in Alternative Procedures section of TxS result page");
          }
          try {
            if (testItemDesc.findElement(By.tagName("a")) != null) {
              CheckHttpConnection httpConnection = new CheckHttpConnection(
                  testItemDesc.findElement(By.tagName("a"))
                      .getAttribute("href"));
              if (httpConnection.testHttpConn() != 200) {
                messages
                    .add(testItemDesc.findElement(By.tagName("a"))
                        .getAttribute("href")
                        + " link is broken from Alternative Procedures section of TxS Result page");
              }
            }
          } catch (Exception e) {
            // TODO: handle exception
          }
        }
      }

    } catch (Exception e) {

    }
  }

  public void checkHerbsSupplements() {
    try {
      WebElement testHead = webDriver.findElement(By.id("supplements"));
      if (testHead.getText().trim().equals("")) {
        messages
            .add("section Header above Prescribed Medications section is missing from TxS result page");
      }
      List<WebElement> entryList = webDriver.findElements(By
          .className("entryTable_box"));
      List<WebElement> testLinks = entryList.get(0).findElements(
          By.className("entryItem"));

      for (WebElement testLink : testLinks) {
        boolean isLink = false;
        try {
          WebElement testItemTitle = testLink.findElement(
              By.className("item_title")).findElement(By.tagName("a"));
          if (testItemTitle != null) {

            isLink = true;
            CheckHttpConnection httpConnection = new CheckHttpConnection(
                testItemTitle.getAttribute("href"));
            if (httpConnection.testHttpConn() != 200) {
              messages
                  .add(testItemTitle.getAttribute("href")
                      + " link is broken from Herbs and Supplements section of TxS Result page");
            }
          }
        } catch (Exception e) {
          // TODO: handle exception
        }
        if (isLink == true) {
          WebElement testItemDesc = testLink.findElement(By
              .className("item_description"));
          if (testItemDesc.getText().trim().equals("")) {
            messages
                .add("description text is missing in Herbs and Supplements section of TxS result page");
          }
          try {
            if (testItemDesc.findElement(By.tagName("a")) != null) {
              CheckHttpConnection httpConnection = new CheckHttpConnection(
                  testItemDesc.findElement(By.tagName("a"))
                      .getAttribute("href"));
              if (httpConnection.testHttpConn() != 200) {
                messages
                    .add(testItemDesc.findElement(By.tagName("a"))
                        .getAttribute("href")
                        + " link is broken from Herbs and Supplements section of TxS Result page");
              }
            }
          } catch (Exception e) {
            // TODO: handle exception
          }
        }
      }

    } catch (Exception e) {

    }
  }

  public void checkWelcomeSection() {
    try {
      WebElement welcome = webDriver.findElement(By.className("ts_welcome"));
      try {
        WebElement homePageLink = webDriver.findElement(By
            .className("ts_homepage_link"));
        homePageLink.click();
        if (!webDriver.getCurrentUrl().equals(baseURL + "/treatments")) {
          messages
              .add("TxS Image in welcome section not linked to TxS Landing page");
        }
      } catch (Exception e) {
        messages
            .add("TxS Image in welcome section not linked to TxS Landing page");
      }
    } catch (Exception e) {
      messages.add("Welcome section on TxS Result page is missing");
    }

  }

  public List<String> getMessages() {
    return messages;
  }
}
