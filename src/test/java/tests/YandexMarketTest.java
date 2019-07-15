package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class YandexMarketTest extends TestBase {

    private WebDriverWait wait;
    private Actions action;

    @BeforeMethod
    public void before(){
        action = new Actions(wd);
        wait = new WebDriverWait(wd, 5);
    }

    @Test
    public void compareMobilePhones() {
        wd.get("https://yandex.ru/");
        wd.get("https://market.yandex.ru/catalog--mobilnye-telefony/54726/");

        wd.findElement(By.xpath("//div[@class='LhMupC0dLR']/span[text()='Xiaomi']")).click();
        wd.findElement(By.xpath("//div[@class='LhMupC0dLR']/span[text()='Samsung']")).click();
        wd.findElement(By.linkText("по цене")).click();

        this.waitForElementLoader();
        WebElement xiaomiItem = wait.until(visibilityOfElementLocated(By.xpath("//img[@class='image' and contains(@title, 'Xiaomi')]")));
        String productName = xiaomiItem.getAttribute("title");
        logger.info(productName);

        action.moveToElement(xiaomiItem).build().perform();
        wait.until(elementToBeClickable(By.xpath("//div[contains(@data-bem, 'Xiaomi')]"))).click();

        assertThat(
                wait.until(visibilityOfElementLocated(By.xpath("//div[@class='popup-informer__title']"))).getText(),
                containsStringIgnoringCase(String.format("Товар %s добавлен к сравнению", productName))
        );

        this.waitForElementLoader();
        WebElement samsungItem = wait.until(visibilityOfElementLocated(By.xpath("//img[@class='image' and contains(@title, 'Samsung')]")));
        productName = samsungItem.getAttribute("title");
        logger.info(productName);

        action.moveToElement(samsungItem).build().perform();
        wait.until(elementToBeClickable(By.xpath("//div[contains(@data-bem, 'Samsung')]"))).click();

        assertThat(
                wait.until(visibilityOfElementLocated(By.xpath("//div[@class='popup-informer__title']"))).getText(),
                containsStringIgnoringCase(String.format("Товар %s добавлен к сравнению", productName))
        );

        //сравнение телефонов
        this.waitForElementLoader();
        wd.findElement(By.cssSelector("a[href='/compare?track=rmmbr']")).click();
        assertThat(
                wd.findElements(By.xpath("//div[@class='n-compare-head__image']")).size(),
                equalTo(2)
        );

        wd.findElement(By.xpath("//span[contains(text(), 'все характеристики')]")).click();
        this.waitForElementLoader();
        assertThat(characteristicIsPresent("операционная система"), is(true));

        wd.findElement(By.xpath("//span[contains(text(), 'различающиеся характеристики')]")).click();
        this.waitForElementLoader();
        assertThat(characteristicIsPresent("операционная система"), is(false));
    }

    private boolean characteristicIsPresent (final String name){
        List<WebElement> elements = wd.findElements(By.cssSelector("div.n-compare-row-name.i-bem"));
        for(WebElement element : elements){
            if(element.getText().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    private void waitForElementLoader(){
        wait.until(
                invisibilityOfElementLocated(By.cssSelector("div.preloadable__preloader.preloadable__preloader_visibility_visible.preloadable__paranja"))
        );
    }

}