package tests;

import app.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class TestLinkAppTests extends TestBase {

    private final String SUITE_NAME = "Test suite #" + System.currentTimeMillis();
    private final String APP_URL = "http://localhost/TestLink/";
    private WebDriverWait wait;
    private Actions action;

    @BeforeMethod
    public void before(){
        action = new Actions(wd);
        wait = new WebDriverWait(wd, 5);
    }

    @Test
    public void createNewTestSuite(){
        wd.navigate().to(APP_URL);
        this.login("admin", "admin");
        this.goToMenuItem("Test Specification");
        this.createTestSuite(SUITE_NAME);
        this.addCase(SUITE_NAME, "Case#1");
        this.openSuiteFolder(SUITE_NAME);
        this.addStep("Case#1");
        this.addStep("Case#1");
        this.addStep("Case#1");
        this.addCase(SUITE_NAME, "Case#2");
        this.addStep("Case#2");
        this.addStep("Case#2");
        this.addStep("Case#2");
    }

    @Test
    public void createNewProject() {
        wd.navigate().to(APP_URL);
        this.login("admin", "admin");
        this.createProject("Tests","T");
    }

    @Test
    public void executeTests() {
        final String blackColor = "rgba(0, 0, 0, 1)";
        final String greenColor = "rgba(0, 100, 0, 1)";
        final String redColor = "rgba(178, 34, 34, 1)";
        final String lightPassedClassColor = "rgba(213, 238, 213, 1)";
        final String lightFailedClassColor = "rgba(238, 213, 213, 1)";

        wd.navigate().to(APP_URL);
        this.login("admin", "admin");
        this.goToMenuItem("Execute Tests");
        this.goToTreeFrame();
        this.openSuiteFolder("Test suite #1560463287493");

        this.clickOnTestCase("case1");
        this.goToWorkFrame();

        //проверяем, что первый заголовок черного цвета
        assertThat(
                wd.findElement(By.xpath("//div[@id='execution_history']/div[2]")).getCssValue("background-color"),
                equalTo(blackColor)
        );

        //отмечаем шаги первого кейса как успешные
        List<WebElement> selects = wd.findElements(By.tagName("select"));
        for(WebElement element : selects){
            Select s = new Select(element);
            s.selectByValue("p");
        }
        wd.findElement(By.cssSelector("img[src$='test_status_passed.png']")).click();

        this.goToTreeFrame();
        this.clickOnTestCase("case2");
        this.goToWorkFrame();

        //отмечаем шаги второго кейса как неуспешные
        List<WebElement> select = wd.findElements(By.tagName("select"));
        for(WebElement element : select){
            Select s = new Select(element);
            s.selectByValue("f");
        }
        wd.findElement(By.cssSelector("img[src$='test_status_failed.png']")).click();

        //проверяем цвета в левой панеле
        this.goToTreeFrame();
        assertThat(
                wait.until(elementToBeClickable(By.xpath("//span[contains(text(), 'case1')]"))).getCssValue("background-color"),
                equalTo(lightPassedClassColor)
        );
        assertThat(
                wait.until(elementToBeClickable(By.xpath("//span[contains(text(), 'case2')]"))).getCssValue("background-color"),
                equalTo(lightFailedClassColor)
        );

        //проверяем цвета заголовков в правой панели
        this.clickOnTestCase("case1");
        this.goToWorkFrame();
        assertThat(
                wd.findElement(By.xpath("//div[@id='execution_history']/div[2]")).getCssValue("background-color"),
                equalTo(greenColor)
        );

        this.goToTreeFrame();
        this.clickOnTestCase("case2");
        this.goToWorkFrame();
        assertThat(
                wd.findElement(By.xpath("//div[@id='execution_history']/div[2]")).getCssValue("background-color"),
                equalTo(redColor)
        );
    }

    private void login(String login, String password){
        wd.findElement(By.id("tl_login")).clear();
        wd.findElement(By.id("tl_login")).sendKeys(login);
        wd.findElement(By.id("tl_password")).clear();
        wd.findElement(By.id("tl_password")).sendKeys(password);
        wd.findElement(By.xpath("//div[@class='form__field']/input[@type='submit']")).click();
    }

    private void goToWorkFrame(){
        wd.switchTo().defaultContent()
                .switchTo().frame(wd.findElement(By.name("mainframe")))
                .switchTo().frame(wd.findElement(By.name("workframe")));
    }

    private void goToTreeFrame(){
        wd.switchTo().defaultContent()
                .switchTo().frame(wd.findElement(By.name("mainframe")))
                .switchTo().frame(wd.findElement(By.name("treeframe")));
    }

    private void addCase(String suiteName, String caseName){
        wd.navigate().refresh();
        this.goToMenuItem("Test Specification");

        this.goToTreeFrame();
        wait.until(elementToBeClickable(By.xpath(String.format("//span[contains(text(), '%s')]", suiteName)))).click();

        this.goToWorkFrame();
        wd.findElement(By.xpath("//div[@class='workBack']/img[@title='Actions']")).click();
        wd.findElement(By.id("create_tc")).click();
        wd.findElement(By.id("testcase_name")).sendKeys(caseName);

        wd.switchTo().frame(wd.findElements(By.cssSelector("iframe.cke_wysiwyg_frame.cke_reset")).get(0));
        wd.findElement(By.cssSelector("body > p")).click();
        action.sendKeys("Add summary").build().perform();

        this.goToWorkFrame();
        wd.switchTo().frame(wd.findElements(By.cssSelector("iframe.cke_wysiwyg_frame.cke_reset")).get(1));
        wd.findElement(By.cssSelector("body > p")).click();
        action.sendKeys("Add preconditions").build().perform();

        this.goToWorkFrame();
        wd.findElement(By.id("do_create_button_2")).click();
    }

    private void addStep(String caseName){
        wd.navigate().refresh();
        this.goToMenuItem("Test Specification");

        this.goToTreeFrame();
        this.clickOnTestCase(caseName);

        this.goToWorkFrame();
        wd.findElement(By.name("create_step")).click();
        wd.switchTo().frame(
                wait.until(elementToBeClickable(By.cssSelector("iframe.cke_wysiwyg_frame.cke_reset")))
        );

        wd.findElement(By.cssSelector("body > p")).click();
        action.sendKeys("Add step actions").build().perform();

        this.goToWorkFrame();
        wd.switchTo().frame(wd.findElements(By.cssSelector("iframe.cke_wysiwyg_frame.cke_reset")).get(1));
        wd.findElement(By.cssSelector("body > p")).click();
        action.sendKeys("Add expected results").build().perform();

        this.goToWorkFrame();
        wd.findElement(By.name("do_update_step")).click();
    }

    private void openSuiteFolder(String suiteName){
        this.goToTreeFrame();
        WebElement element = wait.until(elementToBeClickable(By.xpath(String.format("//span[contains(text(), '%s')]", suiteName))));
        action.doubleClick(element).perform();
    }

    private void createTestSuite(String suiteName){
        wd.switchTo().frame(wd.findElement(By.name("workframe")));
        wd.findElement(By.xpath("//div[@class='workBack']/img[@title='Actions']")).click();
        wd.findElement(By.id("new_testsuite")).click();
        wd.findElement(By.xpath("//input[@id='name']")).sendKeys(suiteName);

        wd.switchTo().frame(wd.findElement(By.cssSelector("iframe.cke_wysiwyg_frame.cke_reset")));
        wd.findElement(By.cssSelector("body > p")).click();
        action.sendKeys("Add details").build().perform();

        this.goToWorkFrame();
        wd.findElement(By.name("add_testsuite_button")).click();
    }

    private void createProject(String projectName, String casePrefix){
        this.goToMenuItem("Test Project Management");
        wd.findElement(By.id("create")).click();
        wd.findElement(By.name("tprojectName")).clear();
        wd.findElement(By.name("tprojectName")).sendKeys(projectName);
        wd.findElement(By.name("tcasePrefix")).clear();
        wd.findElement(By.name("tcasePrefix")).sendKeys(casePrefix);
        wd.findElement(By.name("doActionButton")).click();
    }

    private void goToMenuItem(String textLink){
        wd.switchTo().frame(wd.findElement(By.name("mainframe")));
        wd.findElement(By.linkText(textLink)).click();
    }

    private void clickOnTestCase(String caseName){
        wait.until(elementToBeClickable(By.xpath(String.format("//span[contains(text(), '%s')]", caseName)))).click();
    }
}