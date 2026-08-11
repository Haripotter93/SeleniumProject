package testCases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.DemoPage;
import testBase.BaseClass;

public class DemoTest extends BaseClass {

    @Test
    public void demoTest() {

        logger.info("----Demo test start----");

        try {

            // Use driver from BaseClass
            DemoPage demoPage = new DemoPage(driver);

            driver.manage().window().maximize();

            driver.get("https://playwright.dev/");

            Assert.assertEquals(
                    driver.getTitle(),
                    "Fast and reliable end-to-end testing for modern web apps | Playwright"
            );

            demoPage.search("Page object models");

            Assert.assertEquals(
                    driver.getCurrentUrl(),
                    "https://playwright.dev/docs/pom"
            );

            WebElement link = demoPage.wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("(//div[normalize-space()='Screenshots'])[1]")
                    )
            );

            Actions actions = new Actions(driver);

            actions.moveToElement(link).perform();

            link.click();

            demoPage.wait.until(ExpectedConditions.urlContains("/screenshots"));

            Assert.assertTrue(
                    driver.getCurrentUrl().contains("/screenshots")
            );

        } catch (Exception e) {

            logger.error("Demo test failed", e);

            Assert.fail("Demo test failed", e);

        } finally {

            logger.info("----Demo test end----");
        }
    }
}