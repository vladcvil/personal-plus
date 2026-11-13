package ua.kpi.tzyapz.personal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class LeaveCalculatorTest {

    private final LeaveCalculator calc = new LeaveCalculator();

    // 1. Нарахування 2 дні за місяць (рядок 6 у таблиці)
    @Test
    public void calculateLeave_OneMonth_ReturnsTwoDays() {
        assertEquals(2, calc.accrued(1));
    }

    // 2. Межа річного ліміту відпустки: максимум 24 дні (рядок 7 у таблиці)
    @Test
    public void calculateLeave_ExceedsYearlyLimit_ReturnsTwentyFour() {
        // За 15 місяців має нарахувати максимум 24 дні (а не 30)
        assertEquals(24, calc.accrued(15));
    }

    // 3. Від'ємна кількість місяців -> виняток (рядок 8 у таблиці)
    @Test
    public void calculateLeave_NegativeMonths_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> calc.accrued(-1));
    }

    // 4. Перенесення невикористаних днів: не більше 5 (рядок 9 у таблиці)
    @Test
    public void transferUnusedLeave_ExceedsLimit_TransfersFiveDays() {
        // Якщо залишилось 10 днів, перенестися має лише 5
        assertEquals(5, calc.carryOver(10));
    }







    
    // 1. Робочі дні між датами без вихідних (рядок 2 у таблиці)
    @Test
    @DisplayName("BL-30: Робочі дні між датами без вихідних (норма)")
    public void durationDays_standardWorkWeek_returnsFiveDays() {
        LocalDate start = LocalDate.of(2026, 9, 7); // Понеділок
        LocalDate end = LocalDate.of(2026, 9, 11);  // П'ятниця
        assertEquals(5, calc.durationDays(start, end));
    }

    // 2. Період включає суботу й неділю: вихідні відкидаються (рядок 3 у таблиці)
    @Test
    @DisplayName("BL-30: Період включає суботу й неділю (вихідні відкидаються)")
    public void durationDays_periodWithWeekend_excludesSaturdaySunday() {
        LocalDate start = LocalDate.of(2026, 9, 7);  // Понеділок
        LocalDate end = LocalDate.of(2026, 9, 13);   // Неділя
        assertEquals(5, calc.durationDays(start, end));
    }

    // 3. Граничне значення: один і той самий робочий день (рядок 4 у таблиці)
    @Test
    @DisplayName("BL-30: Граничне значення: один і той самий робочий день")
    public void durationDays_sameDay_returnsOneDay() {
        LocalDate sameDay = LocalDate.of(2026, 9, 9); // Середа
        assertEquals(1, calc.durationDays(sameDay, sameDay));
    }

    // 4. Дата початку пізніша за кінець або null -> виняток (рядок 5 у таблиці)
    @Test
    @DisplayName("BL-30: Дата початку пізніша за кінець або null -> виняток")
    public void durationDays_startAfterEndOrNull_throwsException() {
        LocalDate start = LocalDate.of(2026, 9, 11);
        LocalDate end = LocalDate.of(2026, 9, 7);

        // Початок пізніше кінця має кидати IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> calc.durationDays(start, end));
        // Дата null має кидати IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> calc.durationDays(null, end));
    }
}
