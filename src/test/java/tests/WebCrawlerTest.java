package tests;

import app.TestBase;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WebCrawlerTest extends TestBase {

    private Actions actions;

    @BeforeMethod
    public void before(){
        actions = new Actions(wd);
    }

    @Test
    public void testWebCrawler(){
        wd.get("https://www.mann-ivanov-ferber.ru/books/allbooks/?booktype=audiobook");

        int productCount = 0;
        int retryCount = 0;
        while (true) {
            WebElement element = wd.findElement(By.cssSelector("div.js-page-loader.page-loader"));
            actions.moveToElement(element).build().perform();
            ((JavascriptExecutor) wd).executeScript("window.scrollBy(0, -100);");

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

        //получаем ссылки на все продукты
        List<String> links = wd.findElements(By.xpath("//div[contains(@class, 'lego-book')]/a")).stream()
                .map(e -> e.getAttribute("href"))
                .collect(Collectors.toList());

        //данные продуктов
        List<List<String>> rows = new ArrayList<>();

        for (String url : links){
            wd.navigate().to(url);

            //название
            String productName = wd.findElement(By.xpath("//h1[@class='header active p-sky-title']/span")).getText();

            //автор
            String authorName = wd.findElement(By.cssSelector("div.authors")).findElements(By.cssSelector("a > span"))
                    .stream().map(WebElement::getText).collect(Collectors.toList())
                    .stream().collect(Collectors.joining(" "));

            //цена
            String price = wd.findElement(By.xpath("//div[@data-type='audiobook']")).getAttribute("data-price");

            //ссылка для скачивания фрагмента
            String link = "";
            try {
                link = wd.findElement(By.cssSelector("a.nkk-file-download__link[href$='.pdf']")).getAttribute("href");
            } catch (NoSuchElementException ex){}

            //сохраняем данные
            rows.add(Arrays.asList(url, productName, authorName, price, link));
        }

        //запись в файл
        final String delimiter = ";";
        try(FileWriter csv = new FileWriter("src/test/resources/new.csv")){
            csv.append("url");
            csv.append(delimiter);
            csv.append("productName");
            csv.append(delimiter);
            csv.append("authorName");
            csv.append(delimiter);
            csv.append("price");
            csv.append(delimiter);
            csv.append("link");
            csv.append("\n");

            for (List<String> rowData : rows) {
                csv.append(String.join(delimiter, rowData));
                csv.append("\n");
            }
        } catch (IOException e){}
    }
}