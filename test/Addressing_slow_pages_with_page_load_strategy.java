import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Addressing_slow_pages_with_page_load_strategy {
    static WebDriver webDriver;
    static final String slowPageUrl = "http://www.spiegel.de";
    static final String textOfLink = "Schlagzeilen";
    static final String titleOfLinkedPage = "Schlagzeilen - SPIEGEL ONLINE";

    @BeforeAll
    public static void setupDrivers() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/lib/selenium/chromedriver");
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/lib/selenium/geckodriver");
    }
    @AfterEach
    public void closeDriver() {
        webDriver.quit();
    }
    @Test
    public void Standard_firefox_driver_waits_for_all_page_contents() {
        webDriver = new FirefoxDriver();
        webDriver.get(slowPageUrl);
        webDriver.findElement(By.linkText(textOfLink)).click();

        new WebDriverWait(webDriver, 5).until(ExpectedConditions.titleContains(titleOfLinkedPage));

        assertEquals(titleOfLinkedPage, webDriver.getTitle());
    }
    @Test
    public void Eager_firefox_driver_waits_for_necessary_contents() {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        webDriver = new FirefoxDriver(options);
        webDriver.get(slowPageUrl);

        new WebDriverWait(webDriver, 10).until(ExpectedConditions.elementToBeClickable(By.linkText(textOfLink)));

        webDriver.findElement(By.linkText(textOfLink)).click();

        new WebDriverWait(webDriver, 5).until(ExpectedConditions.titleContains(titleOfLinkedPage));

        assertEquals(titleOfLinkedPage, webDriver.getTitle());
    }
    @Test
    public void Eager_html_unit_driver_is_fastest() {
        webDriver = new HtmlUnitDriver();
        webDriver.get(slowPageUrl);

        new WebDriverWait(webDriver, 10).until(ExpectedConditions.elementToBeClickable(By.linkText(textOfLink)));

        webDriver.findElement(By.linkText(textOfLink)).click();

        new WebDriverWait(webDriver, 5).until(ExpectedConditions.titleContains(titleOfLinkedPage));

        assertEquals(titleOfLinkedPage, webDriver.getTitle());
    }
}
