package ua.kpi.tzyapz.personal;

import org.junit.jupiter.api.Test;
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
}
