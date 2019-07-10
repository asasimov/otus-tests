package pages.otus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import pages.Page;

public class StartPage extends Page {

    private static final Logger logger = LogManager.getLogger(AccountPage.class);

    public StartPage(WebDriver wd) {
        super(wd);
        wd.navigate().to("https://otus.ru/");

        if (!wd.getCurrentUrl().contains("otus.ru")) {
            logger.error("Current URL: {}", wd.getCurrentUrl());
            throw new IllegalStateException("This is not otus.ru page.");
        }
    }

    private By loginButton = By.xpath("//button[@data-modal-id='new-log-reg']");
    private By emailInput = By.xpath("//form/div[2]/input[@name='email' and @placeholder='Электронная почта']");
    private By passwordInput = By.name("password");
    private By signInButton = By.xpath("//form/div[4]/button");
    private By personalAccountIcon = By.cssSelector("div:nth-child(2) > div > div.header2-menu__icon > div");
    private By personalAccountLink = By.xpath("//a[@href='/learning/']");

    public StartPage logIn(String email, String password){
        click(loginButton);
        type(emailInput, email);
        type(passwordInput, password);
        click(signInButton);
        return this;
    }

    public AccountPage goToAccountPage(){
        new Actions(wd).moveToElement(wd.findElement(personalAccountIcon)).build().perform();
        click(personalAccountLink);
        return new AccountPage(wd);
    }
}