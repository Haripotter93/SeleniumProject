package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DemoPage;
public class DemoTest {

	@BeforeClass
	@Parameters({"browser"})
	void setup(String browser) {
		System.out.println(browser);
	}

	@Test(groups = {"testgroup"})
	void demoTest() {
			try {
				WebDriver driver = new ChromeDriver();
				DemoPage demoPage = new DemoPage(driver);
				driver.manage().window().maximize();
				driver.get("https://playwright.dev/");
				driver.getTitle().equals("Fast and reliable end-to-end testing for modern web apps | Playwright");
				demoPage.search("Page object models");
				Assert.assertEquals(driver.getCurrentUrl(), "https://playwright.dev/docs/pom");
				WebElement link = demoPage.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[normalize-space()='Screenshots'])[1]")));
				driver.findElement(By.linkText("Screenshots"));
				Actions actions = new Actions(driver);
				actions.moveToElement(link).perform();
				link.click();
				driver.getCurrentUrl().contains("/screenshots");
				driver.quit();
//				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//				driver.findElement(By.className("DocSearch-Button-Placeholder")).click();
//				WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docsearch-input")));
//				searchBox.sendKeys("Page object models");
//				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".DocSearch-Hit")));
//				searchBox.sendKeys(Keys.ENTER);
//				driver.findElement(By.tagName("h1")).getText().equals("Page object models");
//				WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[normalize-space()='Screenshots'])[1]")));
//				WebElement element = driver.findElement(By.linkText("Screenshots"));
//				Actions actions = new Actions(driver);
//				actions.moveToElement(link).perform();
//				link.click();
			} catch(Exception e) {
				System.out.println(e);
			}
	}
	
	@Test(dependsOnMethods = {"demoTest"})
	void demoTest1() {
		System.out.println("test");
	}
}
