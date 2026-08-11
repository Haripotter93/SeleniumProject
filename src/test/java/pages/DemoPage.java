package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DemoPage {

	WebDriver driver;
	public WebDriverWait wait;
	Actions actions;

	// Constructor
	public DemoPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements (driver, this);
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		actions = new Actions(driver);
	}

	//Locators
	@FindBy(how=How.CLASS_NAME, using="DocSearch-Button-Placeholder") 
	WebElement btn_searchButton;
	
	@FindBy(id="docsearch-input")
	WebElement txt_searchBox;

	By options = By.cssSelector("a[href='/docs/pom']");

	@FindBy(how=How.XPATH, using="(//div[normalize-space()='Screenshots'])[1]") 
	WebElement screenshot_link;

	// Actions
	public void search(String value) throws InterruptedException {
	    wait.until(ExpectedConditions.elementToBeClickable(btn_searchButton))
	            .click();
	    wait.until(ExpectedConditions.visibilityOf(txt_searchBox))
	            .sendKeys(value);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(options));
	    wait.until(ExpectedConditions.elementToBeClickable(options)).click();
	    wait.until(ExpectedConditions.urlToBe(
	            "https://playwright.dev/docs/pom"
	    ));
	    Thread.sleep(2000); // Wait for the page to load completely
	    WebElement heading = wait.until(
	            ExpectedConditions.refreshed(
	                    ExpectedConditions.visibilityOfElementLocated(By.tagName("h1"))
	            )
	    );
	    String headingText = heading.getText();
	    System.out.println("headingText" + headingText);
	    Assert.assertEquals(headingText, value);
	}

	
	public void clickScreenshotsLink() {
		WebElement link = wait.until(ExpectedConditions.visibilityOf(screenshot_link));
		actions.moveToElement(link).perform();
		link.click();
	}

}
