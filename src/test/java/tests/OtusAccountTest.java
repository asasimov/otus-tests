package tests;

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
    @Parameters({"login", "password", "lastName", "date", "country", "city", "facebook", "skype"})
    public void testOtusAccount(String login, String password, String lastName, String date, String country, String city, String facebook, String skype){
        PersonalPage personalPage = new StartPage(wd).logIn(login, password)
                .goToAccountPage().goToPersonalPage();

        personalPage.addLastName(lastName).addDateOfBirth(date)
                .addCountry(Country.valueOf(country)).addCity(city).isReadyToRelocate(true)
                .addCommunicationMethod(FACEBOOK, 0, facebook)
                .addCommunicationMethod(SKYPE, 1, skype)
                .clickSaveButton();

        super.deleteAllCookies(wd);
        personalPage = new StartPage(wd).logIn(login, password)
                .goToAccountPage().goToPersonalPage();

        assertThat(personalPage.getLastName(), equalTo(lastName));
        assertThat(personalPage.getDateOfBirth(), equalTo(date));
        assertThat(personalPage.getCountry(), equalTo(Country.valueOf(country).getName()));
        assertThat(personalPage.getCity(), equalTo(city));
        assertThat(personalPage.getContactData(0), equalTo(facebook));
        assertThat(personalPage.getContactData(1), equalTo(skype));
    }
}