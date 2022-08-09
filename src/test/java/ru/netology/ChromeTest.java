package ru.netology;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChromeTest {

    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

//    Требования к содержимому полей://
//    Поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы.
//    Поле телефон - только цифры (11 цифр), символ + (на первом месте).
//    Флажок согласия должен быть выставлен

    @Test
    void shouldHappyPath() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+79998889999");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id = order-success]")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldHappyPath1() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Иванова Анна-Мария");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+79998889999");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id = order-success]")).getText();
        assertEquals(expected, actual.trim());
    }

//    Условия: если какое-то поле не заполнено, или заполнено неверно, то при нажатии на кнопку "Продолжить"
//    должны появляться сообщения об ошибке (будет подсвечено только первое неправильно заполненное поле)
    @Test
    void shouldErrorInTheNameInLatin() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Ivanov");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+79998889999");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldErrorInTheNameWithSymbol() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Иванов+");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+79998889999");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldErrorInTheNameIsNotFilled() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+79998889999");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldErrorInThePhoneIsNotFilled() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldErrorInThePhoneFilledIn10Digits() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+7999888998");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldErrorInThePhoneFilledIn12Digits() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Иванов");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+799988899889");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldErrorInThePhoneFilledInLatin() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("hjkh");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldErrorInThePhoneFilledIsNotPlus() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("89998889988");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldErrorInThePhoneFilledWhenPlusInMiddle() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("7+9998889988");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }
    @Test
    void shouldErrorInTheNameAndThePhoneIsNotFilled() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.cssSelector("[type = button]")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

}
