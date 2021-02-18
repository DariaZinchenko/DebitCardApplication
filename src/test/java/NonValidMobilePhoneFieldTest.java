import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonValidMobilePhoneFieldTest {

    private static ChromeOptions options;
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
        options = new ChromeOptions();
        options.addArguments("--headless");
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void setEmptyPhoneNumberFieldTest(){
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid")).getText().replaceAll("\\s\\s*", " ");
        assertEquals("Мобильный телефон Поле обязательно для заполнения", text);
    }

    @Test
    void setSpacesPhoneNumberFieldTest(){
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("          ");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid")).getText().replaceAll("\\s\\s*", " ");
        assertEquals("Мобильный телефон Поле обязательно для заполнения", text);
    }

    @ParameterizedTest(name = "{index} {0}")
    @CsvSource({"set one number, 1",
            "set 12 numbers, 123456789012",
            "set cyrillic symbols, Телефон",
            "set latin symbols, Telephone",
            "set other symbols, !\"№;%:?*()_+{}[]<>/\\|"})
    void setNotEmptyPhoneNumberFieldTest(String testName, String phone){
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid")).getText().replaceAll("\\s\\s*", " ");
        assertEquals("Мобильный телефон Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }
}