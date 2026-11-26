package ua.kpi.tzyapz.personal.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ui")
@DisplayName("Персонал+ (OrangeHRM Demo): UI-автотести (TC-01 … TC-08)")
class OrangeHrmUiTest {

    private static final String BASE_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,900", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(BASE_URL);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void loginAsAdmin() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/dashboard"));
    }

    @Test
    @DisplayName("TC-01: Успішний вхід Admin відкриває Dashboard")
    void validLoginOpensDashboard() {
        loginAsAdmin();
        WebElement profileName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("oxd-userdropdown-name")));
        assertTrue(profileName.isDisplayed(), "Ім'я користувача має відображатися на панелі");
    }

    @Test
    @DisplayName("TC-02: Помилка входу — невірний пароль")
    void invalidPasswordShowsError() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("wrong_pass");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String errorText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".oxd-alert-content-text"))).getText();

        assertAll("Помилка входу",
                () -> assertEquals("Invalid credentials", errorText),
                () -> assertTrue(driver.getCurrentUrl().contains("/auth/login"))
        );
    }

    @Test
    @DisplayName("TC-03: Помилка входу — порожні обов'язкові поля")
    void emptyCredentialsShowRequired() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

        List<WebElement> errors = wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector(".oxd-input-field-error-message"), 2));

        assertAll("Обов'язкові поля",
                () -> assertEquals("Required", errors.get(0).getText()),
                () -> assertEquals("Required", errors.get(1).getText())
        );
    }

    @Test
    @DisplayName("TC-04: Вихід із системи (Logout)")
    void logoutRedirectsToLogin() {
        loginAsAdmin();
        wait.until(ExpectedConditions.elementToBeClickable(By.className("oxd-userdropdown-tab"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Logout']"))).click();

        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button[type='submit']")));
        assertTrue(loginBtn.isDisplayed());
    }

    @Test
    @DisplayName("TC-05: Пошук працівника в модулі PIM")
    void searchEmployeeInPim() {
        loginAsAdmin();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='viewPimModule']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//input[@placeholder='Type for hints...'])[1]"))).sendKeys("a");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement recordsLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(), 'Record')]")));
        assertTrue(recordsLabel.isDisplayed() && !recordsLabel.getText().contains("No Records Found"));
    }

    @Test
    @DisplayName("TC-06: Додавання нового працівника (PIM -> Add Employee)")
    void addEmployeeSuccessfully() {
        loginAsAdmin();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='viewPimModule']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Add')]"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstName"))).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("Testenko");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='Personal Details']")));
        assertTrue(header.isDisplayed());
    }

    @Test
    @DisplayName("TC-07: Відкриття форми заявки на відпустку (Leave -> Apply)")
    void openApplyLeaveForm() {
        loginAsAdmin();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='viewLeaveModule']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Apply']"))).click();

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='Apply Leave']")));
        assertTrue(header.isDisplayed());
    }

    @Test
    @DisplayName("TC-08: Перегляд списку вакансій (Recruitment -> Vacancies)")
    void viewVacanciesList() {
        loginAsAdmin();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='viewRecruitmentModule']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Vacancies']"))).click();

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h5[text()='Vacancies']")));
        assertTrue(header.isDisplayed());
    }
}
