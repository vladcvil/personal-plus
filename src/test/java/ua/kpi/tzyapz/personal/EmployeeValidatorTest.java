package ua.kpi.tzyapz.personal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Тести валідації даних працівника (EmployeeValidator)")
public class EmployeeValidatorTest {

    private EmployeeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmployeeValidator();
    }

    // 1. Валідація форматів Email (рядок 18 у таблиці)
    @ParameterizedTest
    @ValueSource(strings = {"valid.user@example.com", "user_12@sub.domain.org", "hr+test@company.ua"})
    @DisplayName("BL-80: Валідні адреси email повертають true")
    public void isValidEmail_variousFormats_validated(String email) {
        assertTrue(validator.isValidEmail(email));
    }

    // 2. РНОКПП: рівно 10 цифр без інших символів (рядок 19 у таблиці)
    @Test
    @DisplayName("BL-80: РНОКПП довжиною 9 цифр має відхилятися")
    public void isValidTaxId_boundaryLengthsAndNonDigits_validated() {
        // Рівно 10 цифр - валідно
        assertTrue(validator.isValidTaxId("1234567890"));
        // Граничне значення: 9 цифр - має бути false (падає через закладений баг <= 10)
        assertFalse(validator.isValidTaxId("123456789"), "Код з 9 цифр повинен відхилятися");
    }

    // 3. Перевірка дати найму (рядок 20 у таблиці)
    @Test
    @DisplayName("BL-80: Дата найму: поточна дозволена, майбутня — ні")
    public void isHireDateValid_pastTodayAndFuture_validated() {
        LocalDate today = LocalDate.of(2026, 9, 8);
        LocalDate pastDate = LocalDate.of(2026, 9, 1);
        LocalDate futureDate = LocalDate.of(2026, 9, 9);

        assertTrue(validator.isHireDateValid(today, today), "Поточна дата дозволена");
        assertTrue(validator.isHireDateValid(pastDate, today), "Минула дата дозволена");
        assertFalse(validator.isHireDateValid(futureDate, today), "Майбутня дата заборонена");
    }

    // 4. Комплексна валідація віку: не менше 16 повних років (рядок 21 у таблиці)
    @Test
    @DisplayName("BL-80: Працівник віком 15 років і 11 місяців отримує помилку AGE")
    public void validate_underageEmployee_returnsAgeError() {
        LocalDate today = LocalDate.of(2026, 9, 8);
        LocalDate hireDate = today;
        // Народився в грудні 2010 -> у вересні 2026 виповнилося лише 15 років і 9 місяців!
        LocalDate birthDate = LocalDate.of(2010, 12, 1);

        List<String> errors = validator.validate("test@example.com", "1234567890", birthDate, hireDate, today);

        // Падає через закладений баг (код віднімає лише роки 2026 - 2010 = 16)
        assertTrue(errors.contains("AGE"), "Має бути зафіксована помилка AGE, оскільки повних 16 років ще немає");
    }
}
