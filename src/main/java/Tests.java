import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class Tests {

    public WebDriver driver;

    @BeforeAll
    public static void setDriver() {WebDriverManager.chromedriver().setup();}

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    public void quit() {
        driver.quit();
    }



    public void tests() {

        /* Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

        driver.get("https://miuz.ru");

        String searchRequest = "Кольцо";

        String title = driver.getTitle();
        assertEquals("Официальный интернет-магазин Московского ювелирного завода MIUZ Diamonds", title);

        WebElement searchInput = driver.findElement(By.cssSelector("#search_textbox_input_top"));
        searchInput.click();
        searchInput.sendKeys(searchRequest);
        WebElement searchContainer = driver.findElement(By.cssSelector(".title-search-result"));
        wait.until(ExpectedConditions.visibilityOf(searchContainer));

        List<WebElement> linksСount = driver.findElements(By.cssSelector(".search-result-grid .title-search-item a"));

        if (linksСount.size() != 8 ) {
            Assertions.fail("Нет значений в поиске");
        };

        for(WebElement link: linksСount) {

            String linkText = link.findElement(By.cssSelector("span.search-item-info")).getText();
            String regex = searchRequest;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(linkText);

            if(!matcher.find()) {
                fail("Нет совпадений по запросу");
                System.out.println(link);
            }


        }







        String cssValueSearchContainer = searchContainer.getCssValue("display");

        assertEquals(cssValueSearchContainer,"block");
*/
    }

}
