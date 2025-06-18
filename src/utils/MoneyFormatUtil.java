package utils;

import java.text.NumberFormat;
import java.util.Locale;

public class MoneyFormatUtil {
    private static final Locale vietnamLocale = new Locale("vi", "VN");
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnamLocale);

    public static String format(double amount) {
        return currencyFormatter.format(amount);
    }
}
