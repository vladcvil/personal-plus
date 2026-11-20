package ua.kpi.tzyapz.personal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PayrollCalculatorTest {

    private final PayrollCalculator calculator = new PayrollCalculator();

    // BL-50: Розрахунок «на руки» (ставка 18% ПДФО + 5% ВЗ = gross * 0.77)
    @Test
    @DisplayName("BL-50: Розрахунок на руки зі стандартної суми (0.77)")
    void net_standardGross_returnsSeventySevenPercent() {
        BigDecimal result = calculator.net(new BigDecimal("10000.00"));
        assertEquals(new BigDecimal("7700.00"), result);
    }

    // BL-50: Округлення HALF_UP до копійок
    @Test
    @DisplayName("BL-50: Округлення сум з копійками за HALF_UP")
    void net_fractionalAmount_roundsHalfUpToTwoDecimals() {
        assertAll(
            () -> assertEquals(new BigDecimal("8.09"), calculator.net(new BigDecimal("10.50"))),
            () -> assertEquals(new BigDecimal("7.72"), calculator.net(new BigDecimal("10.03")))
        );
    }

    // BL-50: Від'ємна або null сума нарахування -> виняток
    @Test
    @DisplayName("BL-50: Від'ємна або null сума gross кидає виняток")
    void net_negativeOrNullGross_throwsException() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> calculator.net(new BigDecimal("-100.00"))),
            () -> assertThrows(IllegalArgumentException.class,
                () -> calculator.net(null))
        );
    }

    // 1. Погодинна ставка: оклад / 168 з округленням HALF_UP (рядок 14 у таблиці)
    @Test
    @DisplayName("BL-60: Погодинна ставка з округленням HALF_UP (приклад з SRS)")
    void hourlyRate_standardAndBoundarySalaries_roundsCorrectly() {
        // Оклад 10 000 -> 59.52
        assertEquals(new BigDecimal("59.52"), calculator.hourlyRate(new BigDecimal("10000.00")));
        // Оклад 25 000 -> 148.8095 -> за правилом HALF_UP має бути 148.81 (падає через DOWN!)
        assertEquals(new BigDecimal("148.81"), calculator.hourlyRate(new BigDecimal("25000.00")),
                "Для окладу 25000 ставка має округлюватися вгору до 148.81");
    }

    // 2. Від'ємний оклад або null -> виняток (рядок 15 у таблиці)
    @Test
    @DisplayName("BL-60: Від'ємний оклад або null кидає IllegalArgumentException")
    void hourlyRate_negativeOrNullSalary_throwsException() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> calculator.hourlyRate(new BigDecimal("-500.00"))),
            () -> assertThrows(IllegalArgumentException.class,
                () -> calculator.hourlyRate(null))
        );
    }

    // 3. Оплата надурочних: ставка * 1.5 * години (рядок 16 у таблиці)
    @Test
    @DisplayName("BL-70: Оплата надурочних годин (коефіцієнт 1.5, дробові години)")
    void overtimePay_fractionalHours_calculatesCorrectly() {
        // Оклад 16 800 (ставка 100.00), години 2.5 -> 100 * 1.5 * 2.5 = 375.00
        BigDecimal monthly = new BigDecimal("16800.00");
        BigDecimal hours = new BigDecimal("2.5");
        assertEquals(new BigDecimal("375.00"), calculator.overtimePay(monthly, hours));
    }

    // 4. Граничні години: 0 -> 0.00 грн, від'ємні -> виняток (рядок 17 у таблиці)
    @Test
    @DisplayName("BL-70: 0 годин дає 0.00 грн, від'ємні години або null кидають виняток")
    void overtimePay_zeroAndNegativeHours_handledCorrectly() {
        BigDecimal monthly = new BigDecimal("16800.00");
        assertAll(
            () -> assertEquals(new BigDecimal("0.00"), calculator.overtimePay(monthly, BigDecimal.ZERO)),
            () -> assertThrows(IllegalArgumentException.class,
                () -> calculator.overtimePay(monthly, new BigDecimal("-1.0"))),
            () -> assertThrows(IllegalArgumentException.class,
                () -> calculator.overtimePay(monthly, null))
        );
    }
}
