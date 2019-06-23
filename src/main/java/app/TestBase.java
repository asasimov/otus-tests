package app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.concurrent.TimeUnit;

public abstract class TestBase {
    protected static final Logger logger = LogManager.getLogger(TestBase.class);

    protected WebDriver wd;

    @BeforeMethod
    public void setUpMethod() {
        wd = WebDriverFactory.createNewDriver(
                System.getProperty("browser", BrowserType.CHROME)
        );

        wd.manage().timeouts()
                .implicitlyWait(4, TimeUnit.SECONDS)
                .pageLoadTimeout(10, TimeUnit.SECONDS)
                .setScriptTimeout(10, TimeUnit.SECONDS);
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        if (wd != null)
            wd.quit();
    }
}
