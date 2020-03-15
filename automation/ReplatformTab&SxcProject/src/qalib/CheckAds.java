package qalib;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckAds {

  private WebDriver webDriver;
  
  public CheckAds(WebDriver driver) {
    webDriver = driver;
  }
  
  /**
   * Check upper Ad DFPAD_L
   * @return
   */
  public String checkUpperAd() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {

    	  //WebElement topAd = webDriver.findElement(By.id("gpt-ad-lb"));
    	  
    	  WebElement topAd = new WebDriverWait(webDriver, 15)
			.until(ExpectedConditions.visibilityOfElementLocated(By.id("gpt-ad-lb")));    	  
//    	  if(!topAd.isDisplayed()){
//    		  message = "Cannot see upper Ad";
//    	  }

    	  //WebElement topAd = webDriver.findElement(By.id("gpt-ad-lb"));
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see upper Ad";
          break;
        } else {
          try {
            Thread.sleep(5000);
          } catch (Exception te) {
            //do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }
  
  /**
   * Check lower Ad ad-belowarticle
   * @return
   */
  public String checkLowerAd() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {
        WebElement lowerAd = webDriver.findElement(By.className("ad-belowarticle"));
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see lower Ad ad-belowarticle";
          break;
        } else {
          try {
            Thread.sleep(5000);
          } catch (Exception te) {
            // do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }

  /**
   * Check lower Ad DFPAD_L2
   * @return
   */
  public String checkLowerAd2() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {
        WebElement lowerAd2 = webDriver.findElement(By.id("DFPAD_L2"));
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see lower Ad DFPAD_L2";
          break;
        } else {
          try {
            Thread.currentThread().sleep(5000);
          } catch (Exception te) {
            // do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }
  
  public String checkRedesignStickyLB() {
	    int i;
	    String message = null;
	    for (i = 0; i < 3; i++) {
	      try {
	        WebElement lowerAd2 = webDriver.findElement(By.className("mlb")).findElement(By.id("mlb1__slot"));
	      } catch (Exception e) {
	        if (i == 2) {
	          message = "Cannot see Sticky LB";
	          break;
	        } else {
	          try {
	            Thread.currentThread().sleep(5000);
	          } catch (Exception te) {
	            // do nothing, just sleep for 5 seconds
	          }
	        }
	      }
	    }
	    return message;
	  }
  public String checkStickyLB() {
	    int i;
	    String message = null;
	    for (i = 0; i < 3; i++) {
	      try {
	        WebElement lowerAd2 = webDriver.findElement(By.className("ad-lb-filler"));
	      } catch (Exception e) {
	        if (i == 2) {
	          message = "Cannot see Sticky LB";
	          break;
	        } else {
	          try {
	            Thread.currentThread().sleep(5000);
	          } catch (Exception te) {
	            // do nothing, just sleep for 5 seconds
	          }
	        }
	      }
	    }
	    return message;
	  }

  public String checkTopDlb() {
	    int i;
	    String message = null;
	    for (i = 0; i < 3; i++) {
	      try {
	    	  WebElement topdlb = webDriver.findElement(By.id("dlb1__slot"));
	      } catch (Exception e) {
	        if (i == 2) {
	          message = "Cannot see top DLB Ad";
	          break;
	        } else {
	          try {
	            Thread.sleep(5000);
	          } catch (Exception te) {
	            //do nothing, just sleep for 5 seconds
	          }
	        }
	      }
	    }
	    return message;
	  }


  /**
   * Check LHS Ad DFPAD_WSL
   * @return
   */
  public String checkLhsAd() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {
    	  WebElement lowerAd2 = webDriver.findElement(By.id("gpt-ad-wsl"));
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see LHS Ad gpt-ad-wsl";
          break;
        } else {
          try {
            Thread.sleep(5000);
          } catch (Exception te) {
            // do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }

  /**
   * Check RHS Ad MR1 DFPAD_MR
   * @return
   */
  public String checkRhsAdsMr1() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {
        WebElement rhsAdAnchor = webDriver.findElement(By.className("box-ad-mr1"));

        WebElement MR1 = rhsAdAnchor.findElement(By.id("gpt-ad-mr1"));
  
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see RHS Ad MR1 gpt-ad-mr1";
          break;
        } else {
          try {
            Thread.sleep(5000);
          } catch (Exception te) {
            // do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }
  
  /**
   * Check RHS Ad MR2
   * @return
   */
  public String checkRhsAdsMr2() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {

        WebElement rhsAdAnchor = webDriver.findElement(By.className("box-ad-mr2")).findElement(By.id("gpt-ad-mr2"));
        //System.out.println(rhsAdAnchor);
       // WebElement mr2 = rhsAdAnchor.findElement(By.xpath("//div[@class='hl-ad-mr2']"));

       // WebElement rhsAdAnchor = webDriver.findElement(By.className("box-ad-mr2"));
       // WebElement mr2 = rhsAdAnchor.findElement(By.xpath("//div[@class='hl-ad-mr2']"));
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see RHS Ad MR2";
          break;
        } else {
          try {
            Thread.sleep(5000);
          } catch (Exception te) {
            // do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }
  
  /**
   * Check RHS Ad DFPAD_WSR
   * @return
   */
  public String checkRhsAdsWsr() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {
        WebElement rhsAdWsr = webDriver.findElement(By.id("DFPAD_WSR"));
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see RHS Ad DFPAD_WSR";
          break;
        } else {
          try {
            Thread.sleep(5000);
          } catch (Exception te) {
            // do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }
  
  /**
   * Check LRHS Ad DFPAD_WSR
   * @return
   */
  public String checkLHSAdsWsl() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {
    	  WebElement lhsAdWsl = webDriver.findElement(By.id("gpt-ad-wsl"));
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see LHS Ad gpt-ad-wsl";
          break;
        } else {
          try {
            Thread.sleep(5000);
          } catch (Exception te) {
            // do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }
  
  
  
  
  /**
   * Check RHS Ad Marketplace
   * @return
   */
  public String checkRhsAdsMarketPlace() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {
        WebElement rhsAdAnchor = webDriver.findElement(By.id("ad-anchor"));
        WebElement marketPlace = rhsAdAnchor.findElement(By.className("ads-block-marketplace-container"));
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see RHS Ad Market place";
          break;
        } else {
          try {
            Thread.sleep(5000);
          } catch (Exception te) {
            // do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }
  
  /**
   * Check RHS Ad MR1 DFPAD_MR
   * @return
   */
  public String checkNetseerAds() {
    int i;
    String message = null;
    for (i = 0; i < 3; i++) {
      try {
    	  WebElement netseerAd = webDriver.findElement(By.className("ad-pb")).findElement(By.id("gpt-ad-pb"));
      } catch (Exception e) {
        if (i == 2) {
          message = "Cannot see Netseer Ad above footer";
          break;
        } else {
          try {
            Thread.sleep(5000);
          } catch (Exception te) {
            // do nothing, just sleep for 5 seconds
          }
        }
      }
    }
    return message;
  }
  
  public String checkTLAd() {
	    int i;
	    String message = null;
	    for (i = 0; i < 3; i++) {
	      try {

	    	  WebElement TLAd = webDriver.findElement(By.id("gpt-ad-tl"));
	    	  if(!TLAd.isDisplayed()){
	    		  message = "Cannot see TLAd Ad";
	    	  }

	    	  //WebElement topAd = webDriver.findElement(By.id("gpt-ad-lb"));
	      } catch (Exception e) {
	        if (i == 2) {
	          message = "Cannot see TL Ad";
	          break;
	        } else {
	          try {
	            Thread.sleep(5000);
	          } catch (Exception te) {
	            //do nothing, just sleep for 5 seconds
	          }
	        }
	      }
	    }
	    return message;
	  }
  public String checkSwoopAd() {
	    int i;
	    String message = null;
	    for (i = 0; i < 3; i++) {
	      try {

	    	  WebElement SwoopAd = webDriver.findElement(By.id("swoopAd"));
	    	  if(!SwoopAd.isDisplayed()){
	    		  message = "Cannot see SwoopAd Ad";
	    	  }

	    	  //WebElement topAd = webDriver.findElement(By.id("gpt-ad-lb"));
	      } catch (Exception e) {
	        if (i == 2) {
	          message = "Cannot see Swoop Ad";
	          break;
	        } else {
	          try {
	            Thread.sleep(5000);
	          } catch (Exception te) {
	            //do nothing, just sleep for 5 seconds
	          }
	        }
	      }
	    }
	    return message;
	  }

}
