package ua.kpi.tzyapz.personal;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PayrollCalculatorTest {

    private final PayrollCalculator calculator = new PayrollCalculator();

    // BL-50: Розрахунок «на руки» (ставка 18% ПДФО + 5% ВЗ = gross * 0.77)
    @Test
    void net_standardGross_returnsSeventySevenPercent() {
        BigDecimal result = calculator.net(new BigDecimal("10000.00"));
        assertEquals(new BigDecimal("7700.00"), result);
    }

    // BL-50: Округлення HALF_UP до копійок
    // 10.50 * 0.77 = 8.085 -> HALF_UP дає 8.09 (відсіює HALF_EVEN/HALF_DOWN/DOWN)
    // 10.03 * 0.77 = 7.7231 -> HALF_UP дає 7.72 (відсіює UP/CEILING)
    @Test
    void net_fractionalAmount_roundsHalfUpToTwoDecimals() {
        assertAll(
            () -> assertEquals(new BigDecimal("8.09"), calculator.net(new BigDecimal("10.50"))),
            () -> assertEquals(new BigDecimal("7.72"), calculator.net(new BigDecimal("10.03")))
        );
    }

    // BL-50: Від'ємна або null сума нарахування -> виняток
    @Test
    void net_negativeOrNullGross_throwsException() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> calculator.net(new BigDecimal("-100.00"))),
            () -> assertThrows(IllegalArgumentException.class,
                () -> calculator.net(null))
        );
    }
}
