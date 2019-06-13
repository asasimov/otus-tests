package tests;

import app.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestLinkAppTests extends TestBase {

    private final String SUITE_NAME = "Test suite #" + System.currentTimeMillis();
    private Actions action;

    @BeforeMethod
    public void before(){
        action = new Actions(wd);
    }

    @Test
    public void createNewTestSuite(){
        wd.navigate().to("http://localhost/TestLink/");

        //логин
        wd.findElement(By.id("tl_login")).sendKeys("admin");
        wd.findElement(By.id("tl_password")).sendKeys("admin");
        wd.findElement(By.xpath("//div[@class='form__field']/input[@type='submit']")).click();

        //левое меню дашборда
        wd.switchTo().frame(wd.findElement(By.name("mainframe")));
        wd.findElement(By.cssSelector("a.list-group-item[href$='feature=editTc']")).click();

        //создаем ноый тест-сьюит
        wd.switchTo().frame(wd.findElement(By.name("workframe")));
        wd.findElement(By.xpath("//div[@class='workBack']/img[@title='Actions']")).click();
        wd.findElement(By.id("new_testsuite")).click();
        wd.findElement(By.xpath("//input[@id='name']")).sendKeys(SUITE_NAME);

        wd.switchTo().frame(wd.findElement(By.cssSelector("iframe.cke_wysiwyg_frame.cke_reset")));
        wd.findElement(By.cssSelector("body > p")).click();
        action.sendKeys("Add details").build().perform();

        this.goToWorkFrame();
        wd.findElement(By.name("add_testsuite_button")).click();

        this.addCase("Case#1");
        this.openSuiteFolder();
        this.addStep("Case#1");
        this.addStep("Case#1");
        this.addStep("Case#1");

        this.addCase("Case#2");
        this.addStep("Case#2");
        this.addStep("Case#2");
        this.addStep("Case#2");
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

    private void addCase(String caseName){
        wd.navigate().refresh();
        wd.switchTo().frame(wd.findElement(By.name("mainframe")));
        wd.findElement(By.cssSelector("a.list-group-item[href$='feature=editTc']")).click();

        this.goToTreeFrame();
        (new WebDriverWait(wd, 3))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//span[contains(text(), '%s')]", SUITE_NAME)))).click();

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
        wd.switchTo().frame(wd.findElement(By.name("mainframe")));
        wd.findElement(By.cssSelector("a.list-group-item[href$='feature=editTc']")).click();

        this.goToTreeFrame();
        (new WebDriverWait(wd, 3))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//span[contains(text(), '%s')]", caseName)))).click();

        this.goToWorkFrame();
        wd.findElement(By.name("create_step")).click();
        wd.switchTo().frame((new WebDriverWait(wd, 3))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("iframe.cke_wysiwyg_frame.cke_reset"))));

        wd.findElement(By.cssSelector("body > p")).click();
        action.sendKeys("Add step actions").build().perform();

        this.goToWorkFrame();
        wd.switchTo().frame(wd.findElements(By.cssSelector("iframe.cke_wysiwyg_frame.cke_reset")).get(1));
        wd.findElement(By.cssSelector("body > p")).click();
        action.sendKeys("Add expected results").build().perform();

        this.goToWorkFrame();
        wd.findElement(By.name("do_update_step")).click();
    }

    private void openSuiteFolder(){
        this.goToTreeFrame();
        WebElement until = (new WebDriverWait(wd, 3))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//span[contains(text(), '%s')]", SUITE_NAME))));
        action.doubleClick(until).perform();
    }
}
