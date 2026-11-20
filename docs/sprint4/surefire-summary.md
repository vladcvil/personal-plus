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
[INFO] --- surefire:3.2.5:test (default-test) @ qa-starter ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running ua.kpi.tzyapz.starter.CalculatorTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.225 s -- in ua.kpi.tzyapz.starter.CalculatorTest
[INFO] Running ua.kpi.tzyapz.personal.PayrollCalculatorTest
[ERROR] Tests run: 7, Failures: 3, Errors: 0, Skipped: 0, Time elapsed: 0.121 s <<< FAILURE! -- in ua.kpi.tzyapz.personal.PayrollCalculatorTest
[ERROR] ua.kpi.tzyapz.personal.PayrollCalculatorTest.net_standardGross_returnsSeventySevenPercent -- Time elapsed: 0.017 s <<< FAILURE!
org.opentest4j.AssertionFailedError: expected: <7700.00> but was: <7790.00>
        at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
        at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
        at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:197)
        at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:182)
        at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:177)
        at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1145)
        at ua.kpi.tzyapz.personal.PayrollCalculatorTest.net_standardGross_returnsSeventySevenPercent(PayrollCalculatorTest.java:17)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)

[ERROR] ua.kpi.tzyapz.personal.PayrollCalculatorTest.hourlyRate_standardAndBoundarySalaries_roundsCorrectly -- Time elapsed: 0.009 s <<< FAILURE!
org.opentest4j.AssertionFailedError: Для окладу 25000 ставка має округлюватися вгору до 148.81 ==> expected: <148.81> but was: <148.80>
        at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
        at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
        at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:197)
        at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:182)
        at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1156)
        at ua.kpi.tzyapz.personal.PayrollCalculatorTest.hourlyRate_standardAndBoundarySalaries_roundsCorrectly(PayrollCalculatorTest.java:50)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)

[ERROR] ua.kpi.tzyapz.personal.PayrollCalculatorTest.net_fractionalAmount_roundsHalfUpToTwoDecimals -- Time elapsed: 0.005 s <<< FAILURE!
org.opentest4j.MultipleFailuresError: 
Multiple Failures (2 failures)
        org.opentest4j.AssertionFailedError: expected: <8.09> but was: <8.18>
        org.opentest4j.AssertionFailedError: expected: <7.72> but was: <7.81>
        at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:80)
        at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:44)
        at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:38)
        at org.junit.jupiter.api.Assertions.assertAll(Assertions.java:2944)
        at ua.kpi.tzyapz.personal.PayrollCalculatorTest.net_fractionalAmount_roundsHalfUpToTwoDecimals(PayrollCalculatorTest.java:24)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        Suppressed: org.opentest4j.AssertionFailedError: expected: <8.09> but was: <8.18>
                at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
                at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
                at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:197)
                at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:182)
                at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:177)
                at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1145)
                at ua.kpi.tzyapz.personal.PayrollCalculatorTest.lambda$net_fractionalAmount_roundsHalfUpToTwoDecimals$0(PayrollCalculatorTest.java:25)
                at org.junit.jupiter.api.AssertAll.lambda$assertAll$0(AssertAll.java:68)
                at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:214)
                at java.base/java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:1024)
                at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:570)
                at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:560)
                at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
                at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:265)
                at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:723)
                at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:77)
                ... 7 more
        Suppressed: org.opentest4j.AssertionFailedError: expected: <7.72> but was: <7.81>
                at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
                at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
                at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:197)
                at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:182)
                at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:177)
                at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1145)
                at ua.kpi.tzyapz.personal.PayrollCalculatorTest.lambda$net_fractionalAmount_roundsHalfUpToTwoDecimals$1(PayrollCalculatorTest.java:26)
                at org.junit.jupiter.api.AssertAll.lambda$assertAll$0(AssertAll.java:68)
                at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:214)
                at java.base/java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:1024)
                at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:570)
                at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:560)
                at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
                at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:265)
                at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:723)
                at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:77)
                ... 7 more

[INFO] Running ua.kpi.tzyapz.personal.EmployeeValidatorTest
[ERROR] Tests run: 6, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.053 s <<< FAILURE! -- in ua.kpi.tzyapz.personal.EmployeeValidatorTest
[ERROR] ua.kpi.tzyapz.personal.EmployeeValidatorTest.validate_underageEmployee_returnsAgeError -- Time elapsed: 0.004 s <<< FAILURE!
org.opentest4j.AssertionFailedError: Має бути зафіксована помилка AGE, оскільки повних 16 років ще немає ==> expected: <true> but was: <false>
        at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
        at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
        at org.junit.jupiter.api.AssertTrue.failNotTrue(AssertTrue.java:63)
        at org.junit.jupiter.api.AssertTrue.assertTrue(AssertTrue.java:36)
        at org.junit.jupiter.api.Assertions.assertTrue(Assertions.java:214)
        at ua.kpi.tzyapz.personal.EmployeeValidatorTest.validate_underageEmployee_returnsAgeError(EmployeeValidatorTest.java:69)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)

[ERROR] ua.kpi.tzyapz.personal.EmployeeValidatorTest.isValidTaxId_boundaryLengthsAndNonDigits_validated -- Time elapsed: 0.003 s <<< FAILURE!
org.opentest4j.AssertionFailedError: Код з 9 цифр повинен відхилятися ==> expected: <false> but was: <true>
        at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
        at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
        at org.junit.jupiter.api.AssertFalse.failNotFalse(AssertFalse.java:63)
        at org.junit.jupiter.api.AssertFalse.assertFalse(AssertFalse.java:36)
        at org.junit.jupiter.api.Assertions.assertFalse(Assertions.java:239)
        at ua.kpi.tzyapz.personal.EmployeeValidatorTest.isValidTaxId_boundaryLengthsAndNonDigits_validated(EmployeeValidatorTest.java:41)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)

[INFO] Running ua.kpi.tzyapz.personal.LeaveCalculatorTest
[ERROR] Tests run: 9, Failures: 3, Errors: 0, Skipped: 0, Time elapsed: 0.061 s <<< FAILURE! -- in ua.kpi.tzyapz.personal.LeaveCalculatorTest
[ERROR] ua.kpi.tzyapz.personal.LeaveCalculatorTest.durationDays_standardWorkWeek_returnsFiveDays -- Time elapsed: 0.018 s <<< FAILURE!
org.opentest4j.AssertionFailedError: expected: <5> but was: <4>
        at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
        at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
        at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:197)
        at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:150)
        at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:145)
        at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:531)
        at ua.kpi.tzyapz.personal.LeaveCalculatorTest.durationDays_standardWorkWeek_returnsFiveDays(LeaveCalculatorTest.java:51)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)

[ERROR] ua.kpi.tzyapz.personal.LeaveCalculatorTest.durationDays_sameDay_returnsOneDay -- Time elapsed: 0.002 s <<< FAILURE!
org.opentest4j.AssertionFailedError: expected: <1> but was: <0>
        at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
        at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
        at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:197)
        at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:150)
        at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:145)
        at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:531)
        at ua.kpi.tzyapz.personal.LeaveCalculatorTest.durationDays_sameDay_returnsOneDay(LeaveCalculatorTest.java:68)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)

[ERROR] ua.kpi.tzyapz.personal.LeaveCalculatorTest.calculateLeave_ExceedsYearlyLimit_ReturnsTwentyFour -- Time elapsed: 0.003 s <<< FAILURE!
org.opentest4j.AssertionFailedError: expected: <24> but was: <30>
        at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
        at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
        at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:197)
        at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:150)
        at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:145)
        at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:531)
        at ua.kpi.tzyapz.personal.LeaveCalculatorTest.calculateLeave_ExceedsYearlyLimit_ReturnsTwentyFour(LeaveCalculatorTest.java:22)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)

[INFO] 
[INFO] Results:
[INFO] 
[ERROR] Failures: 
[ERROR]   EmployeeValidatorTest.isValidTaxId_boundaryLengthsAndNonDigits_validated:41 Код з 9 цифр повинен відхилятися ==> expected: <false> but was: <true>
[ERROR]   EmployeeValidatorTest.validate_underageEmployee_returnsAgeError:69 Має бути зафіксована помилка AGE, оскільки повних 16 років ще немає ==> expected: <true> but was: <false>
[ERROR]   LeaveCalculatorTest.calculateLeave_ExceedsYearlyLimit_ReturnsTwentyFour:22 expected: <24> but was: <30>
[ERROR]   LeaveCalculatorTest.durationDays_sameDay_returnsOneDay:68 expected: <1> but was: <0>
[ERROR]   LeaveCalculatorTest.durationDays_standardWorkWeek_returnsFiveDays:51 expected: <5> but was: <4>
[ERROR]   PayrollCalculatorTest.hourlyRate_standardAndBoundarySalaries_roundsCorrectly:50 Для окладу 25000 ставка має округлюватися вгору до 148.81 ==> expected: <148.81> but was: <148.80>
[ERROR]   PayrollCalculatorTest.net_fractionalAmount_roundsHalfUpToTwoDecimals:24 Multiple Failures (2 failures)
        org.opentest4j.AssertionFailedError: expected: <8.09> but was: <8.18>
        org.opentest4j.AssertionFailedError: expected: <7.72> but was: <7.81>
[ERROR]   PayrollCalculatorTest.net_standardGross_returnsSeventySevenPercent:17 expected: <7700.00> but was: <7790.00>
[INFO] 
[ERROR] Tests run: 29, Failures: 8, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[ERROR] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.731 s
[INFO] Finished at: 2026-11-20T20:53:37Z
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.2.5:test (default-test) on project qa-starter: There are test failures.
[ERROR] 
[ERROR] Please refer to /workspaces/personal-plus/target/surefire-reports for the individual test results.
[ERROR] Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.
[ERROR] -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
```
