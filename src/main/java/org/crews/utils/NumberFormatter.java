package org.crews.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {
	public static String formatCurrency(BigDecimal amount) {
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA); // 한국식 숫자 포맷
		return numberFormat.format(amount);
	}

}