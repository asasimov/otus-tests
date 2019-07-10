package pages.otus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.Page;

public class AccountPage extends Page {

    private static final Logger logger = LogManager.getLogger(AccountPage.class);

    public AccountPage(WebDriver wd) {
        super(wd);
        logger.info("URL: {}", wd.getCurrentUrl());
    }

    private By personalDataButton = By.linkText("О себе");

    public PersonalPage goToPersonalPage(){
        click(personalDataButton);
        return new PersonalPage(wd);
    }
}