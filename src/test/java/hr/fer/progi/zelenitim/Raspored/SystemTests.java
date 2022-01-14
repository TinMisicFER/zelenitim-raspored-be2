package hr.fer.progi.zelenitim.Raspored;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.hibernate.dialect.Dialect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;


public class SystemTests {

	private WebDriver driver;
	private String startPage;
	
	@BeforeEach
	void driver() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\ChromeDriver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		startPage  = "http://localhost:3000";// Kada se stavi u pogon ver2 staviti: https://zelenitim-raspored-fe.herokuapp.com
	}
	
	@AfterEach
	void killDriver() {
		driver.quit();
	}
	
//	SYSTEM TESTS
	
	@Test
	void testSystemPortfolio() {
		driver.get(startPage+"/portfolio");
				
		try {
			driver.findElement(By.className("card-text"));
		} catch (NoSuchElementException e) {
			fail();
		}
		
	}
	
	@Test
	void testSystemSignInCorrect() {
		driver.get(startPage+"/signin");
				
		WebElement element = driver.findElement(By.id("floatingInput"));
		element.sendKeys("direktor");
		element = driver.findElement(By.id("floatingPassword"));
		element.sendKeys("superSecret");
		
		element.submit();
		try {
			Thread.sleep(1000); //loading
		} catch (InterruptedException e) {
		}
		//driver.findElement(By.cssSelector("button[type='submit']")).click();
		String redirURL = driver.getCurrentUrl();
		
		System.out.println("Current URL is: " + redirURL );
		
		if(redirURL.contains("intranet"))
			System.out.println("pass");
		else {
			System.out.println("fail");
			fail();
		}
		
	}
	
	@Test
	void testSystemSignInIncorrect() {
		driver.get(startPage+"/signin");
				
		WebElement element = driver.findElement(By.id("floatingInput"));
		element.sendKeys("direktor");
		element = driver.findElement(By.id("floatingPassword"));
		element.sendKeys("falsePass");
		
		element.submit();
		try {
			Thread.sleep(1000); //loading
		} catch (InterruptedException e) {
		}
		//driver.findElement(By.cssSelector("button[type='submit']")).click();
		String redirURL = driver.getCurrentUrl();
		
		System.out.println("Current URL is: " + redirURL );
		
		if(redirURL.contains("intranet")) {
			System.out.println("fail");
			fail();		
		}

	}
	@Test
	void testSystemCreateGroup() {
		driver.get(startPage+"/signin");
		WebElement element = driver.findElement(By.id("floatingInput"));
		element.sendKeys("direktor");
		element = driver.findElement(By.id("floatingPassword"));
		element.sendKeys("superSecret");
		element.submit();

		element = driver.findElement(By.xpath("//*[text()='Nova grupa']"));
		element.click();

		element = driver.findElement(By.id("floatingInput"));
		element.sendKeys("NovaGrupa");
		Select select = new Select(driver.findElement(By.id("exampleFormControlSelect1")));
		select.selectByVisibleText("BE Development");
		element = driver.findElement(By.xpath("//*[text()='Odaberi voditelja']"));
		element.click();
		element = driver.findElement(By.xpath("//*[text()='direktor']"));
		element.click();

		element.submit();
		try {
			Thread.sleep(1000); //loading
		} catch (InterruptedException e) {
		}
		element = driver.findElement(By.xpath("//*[text()='NovaGrupa']"));
		element.click();

		String redirURL = driver.getCurrentUrl();

		System.out.println("Current URL is: " + redirURL );
		if(redirURL.contains("grouppage"))
			System.out.println("pass");

		else {
			System.out.println("fail"); 
			fail();
		}



	}
	
	@Test
	public void testSystemCreateEmployee() {
		driver.get(startPage+"/signin");
		WebElement element = driver.findElement(By.id("floatingInput"));
		element.sendKeys("direktor");
		element = driver.findElement(By.id("floatingPassword"));
		element.sendKeys("superSecret");
		element.submit();
		
		element = driver.findElement(By.xpath("//*[text()='Novi korisnik']"));
		element.click();
		
		element = driver.findElement(By.cssSelector(("input[type='username']")));
		element.sendKeys("noviDjelatnik");
		
		element = driver.findElement(By.cssSelector(("input[type='name']")));
		element.sendKeys("Novi");
		
		element = driver.findElement(By.cssSelector(("input[type='lastName']")));
		element.sendKeys("Djelatnik");
		
		element = driver.findElement(By.cssSelector(("input[type='email']")));
		element.sendKeys("novi.djelatnik@gmail.com");
		
		element = driver.findElement(By.cssSelector(("input[type='password']")));
		element.sendKeys("novi123");

		element.submit();
		try {
			Thread.sleep(1000); //loading
		} catch (InterruptedException e) {
		}
		
		element = driver.findElement(By.xpath("//*[text()='Novi']"));
		element.click();

		String redirURL = driver.getCurrentUrl();

		System.out.println("Current URL is: " + redirURL );
		if(redirURL.contains("employeepage"))
			System.out.println("pass");

		else {
			System.out.println("fail");
			fail();
		}


	}

}
