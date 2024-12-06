package org.crews.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MaskedNumber {
    public static String maskedAccountNumber(String accountNumber) {
        String maskingResult = "";

        if (accountNumber.length() >= 7) {
            maskingResult = accountNumber.replaceAll("(?<=.{4}).(?=.{2})", "*");
        } else {
            maskingResult = accountNumber;
        }

        return maskingResult;
    }

    // 카드번호 가운데 8자리 마스킹
    public static String cardMasking(String cardNo) {
        // 카드번호 16자리 또는 15자리 '-'포함/미포함 상관없음
        String regex = "(\\d{4})-?(\\d{4})-?(\\d{4})-?(\\d{3,4})$";

        Matcher matcher = Pattern.compile(regex).matcher(cardNo);
        if(matcher.find()) {
            String target = matcher.group(2) + matcher.group(3);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');

            return cardNo.replace(target, String.valueOf(c));
        }
        return cardNo;
    }
}
