package pages.otus;

import enums.CommunicationMethod;
import enums.Country;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.Page;

public class PersonalPage extends Page {

    private static final Logger logger = LogManager.getLogger(PersonalPage.class);

    public PersonalPage(WebDriver wd) {
        super(wd);
        logger.info("URL: {}", wd.getCurrentUrl());
    }

    private By lastNameInput = By.name("lname");
    private By dateOfBirthInput = By.name("date_of_birth");
    private By countryDiv = By.xpath("//input[@name='country']/../div");
    private By cityDiv = By.xpath("//input[@name='city']/../div");
    private By readyToRelocateIsTrue = By.xpath("//input[@id='id_ready_to_relocate_1']/..");
    private By readyToRelocateIsFalse = By.xpath("//input[@id='id_ready_to_relocate_0']/..");
    private By addContactDataButton = By.xpath("//button[text()='Добавить']");
    private By saveButton = By.xpath("//button[@title='Сохранить']");

    private final String buttonTemplate  = "xpath://button[@title='"+ PATTERN +"']";
    private final String communicationMethodDivTemplate = "xpath://input[@name='contact-"+ PATTERN +"-service']/../..";
    private final String contactInputTemplate = "xpath://*[@id='id_contact-"+ PATTERN +"-value']";

    public PersonalPage addLastName(String lastName){
        type(lastNameInput, lastName);
        return this;
    }

    public PersonalPage addDateOfBirth(String date){
        type(dateOfBirthInput, date);
        return this;
    }

    public PersonalPage addCountry(Country country){
        click(countryDiv);
        click(getSelectorByPattern(buttonTemplate, country.getName()));
        return this;
    }

    public PersonalPage addCity(String city){
        click(cityDiv);
        click(getSelectorByPattern(buttonTemplate, city));
        return this;
    }

    public PersonalPage isReadyToRelocate(boolean flag){
        click(flag ? readyToRelocateIsTrue : readyToRelocateIsFalse);
        return this;
    }

    public PersonalPage addCommunicationMethod(CommunicationMethod method, Integer contactId, String text){
        if (contactId == 0){
            this.chooseCommunicationMethod(method, contactId);
            type(getSelectorByPattern(contactInputTemplate, contactId), text);
        } else {
            click(addContactDataButton);
            this.chooseCommunicationMethod(method, contactId);
            type(getSelectorByPattern(contactInputTemplate, contactId), text);
        }
        return this;
    }

    public void clickSaveButton(){
        click(saveButton);
    }

    private void chooseCommunicationMethod(CommunicationMethod method, Integer contactId){
        click(getSelectorByPattern(communicationMethodDivTemplate, contactId));
        By selector = getSelectorByPattern(buttonTemplate, method.getName());
        click(getElementsByLocator(selector).get(getElementsByLocator(selector).size() - 1));
    }

    public String getLastName(){
        return getValue(lastNameInput).trim();
    }

    public String getDateOfBirth(){
        return getValue(dateOfBirthInput).trim();
    }

    public String getCountry(){
        return getText(countryDiv).trim();
    }

    public String getCity(){
        return getText(cityDiv).trim();
    }

    public String getContactData(int contactId){
        return getValue(getSelectorByPattern(contactInputTemplate, contactId)).trim();
    }
}