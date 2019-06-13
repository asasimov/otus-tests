package app;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class TestBase {
    protected static final Logger logger = LogManager.getLogger(TestBase.class);

    protected WebDriver wd;

    @BeforeClass
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setUpMethod() {
        wd = new ChromeDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        if (wd != null)
            wd.quit();
    }
}
