package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class Page {

    protected final String PATTERN = "${}";

    protected WebDriver wd;

    public Page(WebDriver wd) {
        this.wd = wd;
    }

    protected By getSelectorByPattern(String locatorTemplate, Object... params){
        String[] s = locatorTemplate.split(":");
        String prefix = s[0];
        String template = s[1];
        StringBuilder sb = new StringBuilder(template);
        for(Object param : params){
            int start = sb.indexOf(PATTERN);
            int end = start + PATTERN.length();
            sb.replace(start, end, param.toString());
        }

        if (prefix.equalsIgnoreCase("xpath")) {
            return By.xpath(sb.toString());
        } else if (prefix.equalsIgnoreCase("css")) {
            return By.cssSelector(sb.toString());
        } else if (prefix.equalsIgnoreCase("name")) {
            return By.name(sb.toString());
        } else if (prefix.equalsIgnoreCase("id")) {
            return By.id(sb.toString());
        } else {
            throw new IllegalStateException("Unable to determine locator prefix.");
        }
    }

    protected void type(By locator, String text){
        wd.findElement(locator).clear();
        wd.findElement(locator).sendKeys(text);
    }

    protected void click(By locator){
        wd.findElement(locator).click();
    }

    protected void click(WebElement element){
        element.click();
    }

    protected List<WebElement> getElementsByLocator(By locator){
        return wd.findElements(locator);
    }

    protected String getValue(By locator){
        return wd.findElement(locator).getAttribute("value");
    }

    protected String getText(By locator){
        return wd.findElement(locator).getText();
    }
}