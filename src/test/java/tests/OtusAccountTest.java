package tests;

import app.TestBase;
import enums.Country;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.otus.PersonalPage;
import pages.otus.StartPage;

import static enums.CommunicationMethod.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OtusAccountTest extends TestBase {

    @Test
    @Parameters({"login", "password"})
    public void testOtusAccount(String login, String password){
        final String lastName = "Тестов";
        final String date = "01.01.2000";
        final Country country = Country.RUSSIA;
        final String city = "Москва";
        final String facebook = "facebook.com";
        final String skype = "skype.com";

        PersonalPage personalPage = new StartPage(wd).logIn(login, password)
                .goToAccountPage().goToPersonalPage();

        personalPage.addLastName(lastName).addDateOfBirth(date)
                .addCountry(country).addCity(city).isReadyToRelocate(true)
                .addCommunicationMethod(FACEBOOK, 0, facebook)
                .addCommunicationMethod(SKYPE, 1, skype)
                .clickSaveButton();

        super.deleteAllCookies(wd);
        personalPage = new StartPage(wd).logIn(login, password)
                .goToAccountPage().goToPersonalPage();

        assertThat(personalPage.getLastName(), equalTo(lastName));
        assertThat(personalPage.getDateOfBirth(), equalTo(date));
        assertThat(personalPage.getCountry(), equalTo(country.getName()));
        assertThat(personalPage.getCity(), equalTo(city));
        assertThat(personalPage.getContactData(0), equalTo(facebook));
        assertThat(personalPage.getContactData(1), equalTo(skype));
    }
}