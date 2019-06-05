package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.MatcherAssert.assertThat;

public class OpenOtusPageTest {

    private static final Logger logger = LogManager.getLogger(OpenOtusPageTest.class);

    private WebDriver wd;

    @BeforeClass
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setUpMethod() {
        wd = new ChromeDriver();
    }

    @Test
    public void testOpenOtusPage(){
        logger.info("Test started.");

        wd.navigate().to("https://otus.ru");
        assertThat(wd.getTitle(), containsStringIgnoringCase("otus"));

        logger.info("Test completed.");
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        if (wd != null)
            wd.quit();
    }
}
