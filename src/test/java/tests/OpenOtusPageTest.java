package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.MatcherAssert.assertThat;

public class OpenOtusPageTest extends TestBase {

    @Test
    @Description("Открыть страницу Otus.ru")
    public void testOpenOtusPage(){
        logger.info("Test started.");

        wd.navigate().to("https://otus.ru");
        this.checkTitle("otus");

        logger.info("Test completed.");
    }

    @Step("Проверяем, что заголовок содержит {expTitle}")
    private void checkTitle(String expTitle){
        assertThat(wd.getTitle(), containsStringIgnoringCase(expTitle));
    }
}