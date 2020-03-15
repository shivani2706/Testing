package qalib;

import java.net.HttpURLConnection;
import java.net.URL;

public class CheckHttpConnection {
  private String errorMessage;
  private int status;
  private String url2Test;
  
  public CheckHttpConnection(String url) {
    url2Test = url;
  }
  public String getMessage() {
    return errorMessage;
  }
  
  public int getStatus() {
    return status;
  }
    
  public int testHttpConn() {

    try {
    	System.setProperty("jsse.enableSNIExtension", "false");
      URL url = new URL(url2Test);
      HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
      httpConn.setRequestMethod("GET");
      httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      httpConn.connect();
      httpConn.connect();
      status = httpConn.getResponseCode();

      /*
      if(status==401){
        String authString = "healthline:h3althlin3";
        String auth = "Basic " +
        new sun.misc.BASE64Encoder().encode(authString.getBytes()); 
        httpConn.setRequestProperty("Authorization", auth);
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        status = httpConn.getResponseCode();
      }*/
    } catch (Exception e) {
      errorMessage = e.toString();
      System.out.println(e.toString());
    }
    return status;
  }
}

