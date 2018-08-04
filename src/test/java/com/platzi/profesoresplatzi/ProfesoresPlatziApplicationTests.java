package com.platzi.profesoresplatzi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfesoresPlatziApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	
}
/*
package com.ui.interconnet.test;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

/**
*
* @author Edgar Heredia emh5
*/
public class SearchTest {

    private static WebDriver driver = null;
	
	@BeforeClass
	public static void inicializarDriver() {
	    	/*Inicialización  de Mozilla Firefox*/
	    	System.setProperty ("webdriver.gecko.driver", "C:\\geckodriver.exe");
	    	System.setProperty("webdriver.firefox.bin","C:\\Program Files\\Mozilla Firefox\\firefox.exe");
	        driver = new FirefoxDriver();
	}
	
	@Test
	public void test() {
	   	 Actions builder = new Actions(driver);
	
	    // driver.get("http://localhost:8080/ui-thymeleaf/login");https://validator.w3.org/
        //  driver.get("http://localhost:8080/ui-interconnet/login");
          driver.get("https://validator.w3.org/");
	      WebElement origenElem = driver.findElement(By.id("organizacion"));
	      origenElem.sendKeys("vidasecurity");  
	      WebElement destinoElem = driver.findElement(By.id("username"));
	      destinoElem.sendKeys("eheredia");
	      WebElement importeElem = driver.findElement(By.id("contrasena"));
	      importeElem.sendKeys("password");
	  
	      WebElement cmdAceptar = driver.findElement(By.id("loginButton"));
	      cmdAceptar.click();
	      /*Posicionar Puntero del mouse sobre el menu solicitud*/
          WebElement link_home= driver.findElement(By.linkText("Solicitud"));
          Action mouseOvermenu= builder.moveToElement(link_home).build();
          mouseOvermenu.perform(); 
          /**/
          WebElement menuSelect= driver.findElement(By.xpath("/html/body/div[2]/div/table/tbody/tr/td/div/div[1]/div/ul/li[1]/ul/li[2]/a"));
          menuSelect.click();
          WebElement cmdBuscar = driver.findElement(By.id("commands:searchButton"));
          cmdBuscar.click();
	}
	
    @AfterClass
    public static void liquidarDriver() {

    	driver.quit();
        
    }
    

}






*/
