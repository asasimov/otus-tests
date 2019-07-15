package tests;

import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.MatcherAssert.assertThat;

public class OpenOtusPageTest extends TestBase {

    @Test
    public void testOpenOtusPage(){
        logger.info("Test started.");

        wd.navigate().to("https://otus.ru");
        assertThat(wd.getTitle(), containsStringIgnoringCase("otus"));

        logger.info("Test completed.");
    }
}
