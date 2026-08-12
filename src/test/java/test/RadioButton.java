package test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;

public class RadioButton {

	@Test
	public void checkandUnCheckRadioButton() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get("https://testautomationcentral.com/demo/radiobuttons.html");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Radio Buttons (Single Selection)"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='mb-6']//label[1]//input[1]"))).click();
		Thread.sleep(2000); // Wait for 2 seconds to observe the selection
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='mb-6']//label[2]//input[1]"))).click();
		Thread.sleep(2000); // Wait for 2 seconds to observe the selection
		driver.quit();
	}
}
