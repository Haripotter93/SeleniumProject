package test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class Dropdown {
	
	@Test(priority = 1)
	public void simpleDropDownSelect() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get("https://testautomationcentral.com/demo/dropdown.html");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Dropdowns"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-target='simple-dropdown']"))).click();
		Thread.sleep(2000); // Wait for 2 seconds to observe the selection
		Select options = new Select(
			    driver.findElement(By.cssSelector("div#simple-dropdown select"))
			);
		options.selectByVisibleText("Option 2");
		Thread.sleep(2000); // Wait for 2 seconds to observe the selection
		driver.quit();
	}
	
	@Test(priority = 2)
	public void styledDropDownSelect() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get("https://testautomationcentral.com/demo/dropdown.html");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Dropdowns"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-target='styled-dropdown']"))).click();
		Thread.sleep(2000); // Wait for 2 seconds to observe the selection
		Select options = new Select(
			    driver.findElement(By.cssSelector("div#styled-dropdown select"))
			);
		options.selectByVisibleText("Styled Option 2");
		Thread.sleep(2000); // Wait for 2 seconds to observe the selection
		driver.quit();
	}
	
	@Test(priority = 3)
	public void multiSelectDropDownSelect() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get("https://testautomationcentral.com/demo/dropdown.html");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Dropdowns"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-target='multi-select-dropdown']"))).click();
		Thread.sleep(2000); // Wait for 2 seconds to observe the selection
		Select options = new Select(
			    driver.findElement(By.cssSelector("div#multi-select-dropdown select"))
			);
		options.selectByVisibleText("Option 1");
		options.selectByVisibleText("Option 2");
		Thread.sleep(2000); // Wait for 2 seconds to observe the selection
		driver.quit();
	}

}
