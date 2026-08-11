package listeners;

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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

//@Listeners(listeners.MyListener.class)
public class listenerTest {

	WebDriver driver;
	WebDriverWait wait;

	@BeforeClass
	void setup() throws InterruptedException {
		driver = new ChromeDriver();
		driver.get("https://playwright.dev/");
		driver.getTitle().equals("Fast and reliable end-to-end testing for modern web apps | Playwright");
	}

	@Test(priority = 1)
	void navigateToPOMPage() {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.findElement(By.className("DocSearch-Button-Placeholder")).click();
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docsearch-input")));
		searchBox.sendKeys("Page object models");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".DocSearch-Hit")));
		searchBox.sendKeys(Keys.ENTER);
		driver.findElement(By.tagName("h1")).getText().equals("Page object models");
		Assert.assertEquals(driver.getCurrentUrl(), "https://playwright.dev/docs/pom");
	}

	@Test(priority=2)
	void navigateToScreenshotsPage()
	{
		WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[normalize-space()='Screenshots'])[1]")));
		//WebElement element = driver.findElement(By.linkText("Screenshots"));
		Actions actions = new Actions(driver);
		actions.moveToElement(link).perform();
		link.click();
		driver.getCurrentUrl().contains("/screenshots");
	}

	@Test(priority=3, dependsOnMethods= {"navigateToPOMPage"})
	@AfterClass
	void tearDown()
	{
		driver.quit();
	}

}
