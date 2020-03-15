package qalib;

import java.io.*;
import java.util.*;

public class SelectRandomURL {
 
  public String choose(File f) throws FileNotFoundException
  {
     String result = null;
     Random rand = new Random();
     int n = 0;
     for(Scanner sc = new Scanner(f); sc.hasNext(); )
     {
        ++n;
        String line = sc.nextLine();
        if(rand.nextInt(n) == 0)
           result = line;         
     }

     return result;      
  } 
}
