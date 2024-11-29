package org.crews.utils;

import org.springframework.stereotype.Component;

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
}
