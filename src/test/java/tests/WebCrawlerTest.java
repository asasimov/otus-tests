package tests;

import app.TestBase;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.*;

public class WebCrawlerTest extends TestBase {

    private Actions actions;

    @BeforeMethod
    public void before(){
        actions = new Actions(wd);
    }

    @Test
    public void testWebCrawler(){
        final String fileName = "src/test/resources/new.csv";
        final String delimiter = ";";

        this.createCSVFile(fileName, delimiter, "url", "productName", "authorName", "price", "link");

        wd.get("https://www.mann-ivanov-ferber.ru/books/allbooks/?booktype=audiobook");

        int productCount = 0;
        int retryCount = 0;
        while (true) {
            actions.moveToElement(wd.findElement(By.cssSelector("div.js-page-loader.page-loader"))).build().perform();
            ((JavascriptExecutor) wd).executeScript("window.scrollBy(0, -250);");

            int size = wd.findElements(By.cssSelector("img.lego-book__cover-image")).size();
            if(size != productCount){
                productCount = size;
                retryCount = 0;
            } else {
                retryCount++;
                if(retryCount > 10) break;
            }
        }
        logger.info("Product count = " + productCount);

        List<String> links = this.getProductList();
        for (String url : links) {
            wd.navigate().to(url);
            List<String> rowData = Arrays.asList(url, getProductName(), getAuthorsName(), getPrice(), getDownloadLink());
            this.addRowToFile(fileName, delimiter, rowData);
        }
    }

    private void createCSVFile(String pathToFile, String delimiter, String... fieldNames){
        try(FileWriter csv = new FileWriter(pathToFile, true)) {
            csv.append(String.join(delimiter, fieldNames));
            csv.append("\n");
        } catch (IOException ex) {
            logger.error("Could not create file - " + pathToFile);
        }
    }

    private void addRowToFile(String pathToFile, String delimiter, List<String> rows){
        try(FileWriter csv = new FileWriter(pathToFile, true)){
            csv.append(String.join(delimiter, rows));
            csv.append("\n");
        } catch (IOException e){
            logger.warn("Failed to add line to file.");
        }
    }

    private List<String> getProductList(){
        return wd.findElements(By.xpath("//div[contains(@class, 'lego-book')]/a")).stream()
                .map(e -> e.getAttribute("href")).collect(toList());
    }

    private String getAuthorsName() {
        return wd.findElement(By.cssSelector("div.authors")).findElements(By.cssSelector("a > span"))
                .stream().map(WebElement::getText).collect(toList())
                .stream().collect(joining(" "));
    }

    private String getProductName(){
        return wd.findElement(By.xpath("//h1[@class='header active p-sky-title']/span")).getText();
    }

    private String getPrice(){
        return wd.findElement(By.xpath("//div[@data-type='audiobook']")).getAttribute("data-price");
    }

    private String getDownloadLink(){
        try {
            return wd.findElement(By.cssSelector("a.nkk-file-download__link[href$='.pdf']")).getAttribute("href");
        } catch (NoSuchElementException ex){
            return "";
        }
    }
}