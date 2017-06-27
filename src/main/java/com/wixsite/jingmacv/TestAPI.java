package com.wixsite.jingmacv;

import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.net.HttpURLConnection; 
import java.net.MalformedURLException; 
import java.net.URL; 

public class TestAPI {
 
    public static void main(String[] args) { 
        try {
            URL url = new URL("https://api.bmreports.com/BMRS/B1510/V1?APIKey=69qnl68twxam19w&StartDate=2014-12-31&EndDate=2014-12-31&StartTime=14:00:00 ZZ&EndTime=15:00:00 ZZ&ServiceType=xml");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");
 
            if (conn.getResponseCode() != 200) { 
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode()); 
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()))); 
            String output; 
            System.out.println("Output from Server .... \n"); 
            while ((output = br.readLine()) != null) { 
                System.out.println(output); 
            } 
            conn.disconnect(); 
        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    } 
}
