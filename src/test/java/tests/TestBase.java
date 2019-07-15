package tests;

import app.WebDriverFactory;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.concurrent.TimeUnit;

public abstract class TestBase {

    protected static final Logger logger = LogManager.getLogger(TestBase.class);
    protected WebDriver wd;

    private ProxyServer proxy;

    @BeforeMethod
    public void setUpMethod() throws Exception {
        proxy = new ProxyServer(5555);
        proxy.start();

        ChromeOptions options = new ChromeOptions();
//        options.setCapability("proxy", proxy.seleniumProxy());

        wd = WebDriverFactory.createNewDriver(
                System.getProperty("browser", BrowserType.CHROME), options
        );

        wd.manage().timeouts()
                .implicitlyWait(5, TimeUnit.SECONDS)
                .pageLoadTimeout(15, TimeUnit.SECONDS)
                .setScriptTimeout(10, TimeUnit.SECONDS);

        proxy.newHar("new_har");
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() throws Exception {
        if (wd != null) wd.quit();
        if (proxy != null) {
            int i = 0;
            for(HarEntry entry : proxy.getHar().getLog().getEntries()){
                logger.info("request " + (++i) + ":");
                logger.info("URL: " + entry.getRequest().getUrl());
                logger.info("DateTime: " + entry.getStartedDateTime());
            }
            proxy.stop();
        }
    }

    protected void deleteAllCookies(WebDriver wd){
        wd.manage().deleteAllCookies();
    }
}