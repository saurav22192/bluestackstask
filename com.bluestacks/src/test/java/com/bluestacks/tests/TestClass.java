package com.bluestacks.tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;

public class TestClass {
  WebDriver driver;
  @FindBy(xpath="//*[@id=\"game_list\"]")
  WebElement firstel;
  @FindBy(className ="games-item")
  List<WebElement> allgames;
  
  @BeforeTest
  public void beforeTest() {
  System.setProperty("webdriver.chrome.driver","src\\main\\resources\\drivers\\chromedriver.exe");
  System.setProperty("webdriver.chrome.silentOutput", "true"); //THIS will surpress all logs expect INFO


  java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF); 
  driver=new ChromeDriver();
	  driver.get("https://www.game.tv/");
driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }

	@Test
  public void test() throws MalformedURLException, IOException {
		PageFactory.initElements(driver, this);
		JavascriptExecutor jex= (JavascriptExecutor)driver;
		jex.executeScript("arguments[0].scrollIntoView();",firstel );
		///html/body/div/section[2]/div/div[1]/h2/span
	  System.out.println("#  GameName  PageUrl  PageStatus  TournamentCount");
	  for(WebElement game: allgames) {
		  //a/figcaption
		  System.out.print(allgames.indexOf(game)+1);
		  System.out.print("  ");
		  System.out.print(game.findElement(By.className("game-name")).getText());
		  System.out.print("  ");
		  System.out.print(game.findElement(By.tagName("a")).getAttribute("href"));
		 String gameurl=game.findElement(By.tagName("a")).getAttribute("href");
		  System.out.print("  ");
		  HttpURLConnection huc = (HttpURLConnection)(new URL(gameurl).openConnection());
          
          huc.setRequestMethod("HEAD");
          
          huc.connect();
          
          int respCode = huc.getResponseCode();
          System.out.print("  ");
          if(respCode >= 400){
              System.out.println("it is a broken link");
          }
          else{
        	  System.out.print(respCode);
        	  
          }
          System.out.print("  ");
          
          if(respCode < 400) {
        	  String parentwindow=driver.getWindowHandle();
        	  ((JavascriptExecutor)driver).executeScript("window.open()");
        	 for(String handle:driver.getWindowHandles()) {
        		 if(!(driver.switchTo().window(handle).getTitle().contains("Game.tv")))
        		 {
        			 driver.switchTo().window(handle);
        			 
        			 driver.get(gameurl);
                 	  System.out.println(driver.findElement(By.xpath("/html/body/div/section[2]/div/div[1]/h2/span")).getText());
                 	  driver.close();
                 	  driver.switchTo().window(parentwindow);
                 	  break;
        		 }
        		 
        	 }
        	  
        	 
        	 
          	 
        	   
        	  
          }
	  }
		
  }
 
  @AfterTest
  public void afterTest() {
	  driver.quit();
  }

}
