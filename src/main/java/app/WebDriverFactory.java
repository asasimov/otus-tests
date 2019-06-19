package app;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;

public class WebDriverFactory {

    public static WebDriver createNewDriver(String name, MutableCapabilities options){
        if (name.equalsIgnoreCase(BrowserType.CHROME)){
            WebDriverManager.chromedriver().setup();
            if (options != null) return new ChromeDriver((ChromeOptions) options);
            else return new ChromeDriver();
        } else if (name.equalsIgnoreCase(BrowserType.FIREFOX)) {
            WebDriverManager.firefoxdriver().setup();
            if (options != null) return new FirefoxDriver((FirefoxOptions) options);
            else return new FirefoxDriver();
        } else if (name.equalsIgnoreCase(BrowserType.EDGE)) {
            WebDriverManager.edgedriver().setup();
            if (options != null) return new EdgeDriver((EdgeOptions) options);
            else return new EdgeDriver();
        } else {
            throw new WebDriverException(String.format("Browser '%s' is not currently supported.", name));
        }
    }

    public static WebDriver createNewDriver(String name){
        return createNewDriver(name, null);
    }
}

