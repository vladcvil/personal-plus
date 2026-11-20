# Протокол запуску автотестів (Surefire Summary) — Версія модуля v1

**Продукт:** «Персонал+» (пакет `ua.kpi.tzyapz.personal`)  
**Дата прогону:** 08.11.2026  
**Команда запуску:** `mvn test -Dtest='*Test'`  
**Середовище:** OpenJDK 17 (Temurin), Apache Maven 3.9, JUnit Jupiter 5.10  
**Відповідальні за прогін:** Цвілій Влада (КТ), Блащак Петро (ІТ)  

---

## 1. Підсумок виконання тестів модуля бізнес-логіки

| Параметр | Значення | Коментар |
| :--- | :--- | :--- |
| **Tests run** | **20** | Покрито 100% нетривіальних методів (BL-10 … BL-80) |
| **Passed** | **12** | Тести, де логіка модуля v1 працює коректно |
| **Failures** | **8** | Тести, що впали через закладені викладачем дефекти |
| **Errors** | **0** | Непередбачених винятків або помилок компіляції немає |
| **Skipped** | **0** | Усі заплановані тести були виконані |

---

## 2. Список дефектів модуля v1, виявлених автотестами

Усі 8 падінь (`Failures`) відповідають закладеним дефектам бізнес-логіки та оформлені як звіти в GitHub Issues (знайдено 6 унікальних дефектів):

1. **`LeaveCalculatorTest#calculateLeave_ExceedsYearlyLimit_ReturnsTwentyFour`**
   * **Результат:** `Failure` (очікувалося 24, повернуто 30)
   * **Дефект:** [BUG-BL10-1] Відсутність обмеження річного ліміту відпустки при нарахуванні

2. **`LeaveCalculatorTest#durationDays_standardWorkWeek_returnsFiveDays`**
   * **Результат:** `Failure` (очікувалося 5, повернуто 4)
   * **Дефект:** [BUG-BL30-1] Метод durationDays не враховує останній день відпустки

3. **`LeaveCalculatorTest#durationDays_sameDay_returnsOneDay`**
   * **Результат:** `Failure` (очікувалося 1, повернуто 0)
   * **Дефект:** [BUG-BL30-1] Метод durationDays не враховує останній день відпустки

4. **`PayrollCalculatorTest#net_standardGross_returnsSeventySevenPercent`**
   * **Результат:** `Failure`
   * **Дефект:** [BUG-BL50-1] Помилка у розрахунку військового збору

5. **`PayrollCalculatorTest#net_fractionalAmount_roundsHalfUpToTwoDecimals`**
   * **Результат:** `Failure`
   * **Дефект:** [BUG-BL50-1] Помилка у розрахунку військового збору

6. **`PayrollCalculatorTest#hourlyRate_standardAndBoundarySalaries_roundsCorrectly`**
   * **Результат:** `Failure` (очікувалося 148.81, повернуто 148.80)
   * **Дефект:** [BUG-BL60-1] Метод hourlyRate використовує RoundingMode.DOWN замість HALF_UP

7. **`EmployeeValidatorTest#isValidTaxId_boundaryLengthsAndNonDigits_validated`**
   * **Результат:** `Failure` (код з 9 цифр повернув true)
   * **Дефект:** [BUG-BL80-1] Метод isValidTaxId вважає валідним РНОКПП довжиною менше 10 цифр

8. **`EmployeeValidatorTest#validate_underageEmployee_returnsAgeError`**
   * **Результат:** `Failure` (не зафіксовано помилку AGE для працівника < 16 років)
   * **Дефект:** [BUG-BL80-2] Метод isOldEnough не враховує день народження, а лише різницю років

---

## 3. Консольний підсумок Maven Surefire

```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running ua.kpi.tzyapz.personal.LeaveCalculatorTest
[ERROR] Tests run: 9, Failures: 3, Errors: 0, Skipped: 0
[INFO] Running ua.kpi.tzyapz.personal.PayrollCalculatorTest
[ERROR] Tests run: 7, Failures: 3, Errors: 0, Skipped: 0
[INFO] Running ua.kpi.tzyapz.personal.EmployeeValidatorTest
[ERROR] Tests run: 4, Failures: 2, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[ERROR] Failures: 8, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.140 s
[INFO] Finished at: 2026-11-08T16:30:00+02:00
[INFO] ------------------------------------------------------------------------
```
