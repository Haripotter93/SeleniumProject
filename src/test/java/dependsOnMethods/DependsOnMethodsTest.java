package dependsOnMethods;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.DemoPage;

public class DependsOnMethodsTest {

	@Test
	void dependsOnMethod1() {
			try {
				WebDriver driver = new ChromeDriver();
				DemoPage demoPage = new DemoPage(driver);
				driver.manage().window().maximize();
				driver.get("https://playwright.dev/");
				driver.getTitle().equals("Fast and reliable end-to-end testing for modern web apps | Playwright");
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				demoPage.search("Page object models");
				WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docsearch-input")));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".DocSearch-Hit")));
				searchBox.sendKeys(Keys.ENTER);
				driver.findElement(By.tagName("h1")).getText().equals("Page object models");
				Assert.assertEquals(driver.getCurrentUrl(), "https://playwright.dev/docs/pom");
				WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[normalize-space()='Screenshots'])[1]")));
				Actions actions = new Actions(driver);
				actions.moveToElement(link).perform();
				link.click();
				driver.getCurrentUrl().contains("/screenshots");
				driver.quit();
			} catch(Exception e) {
				System.out.println(e);
			}
	}
	
	@Test(dependsOnMethods = {"dependsOnMethod1"})
	void dependsOnMethod2() {
		System.out.println("test");
	}

}
