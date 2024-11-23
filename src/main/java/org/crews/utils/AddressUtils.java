package org.crews.utils;


import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;

public class AddressUtils {
    private AddressUtils() {
        throw new CustomException(ErrorCode.IS_UTILITY_CLASS);
    }
    /**
     * 주소의 각 구성 요소를 결합하여 고유 키를 생성합니다.
     *
     * @param addressDo    도
     * @param addressSi    시
     * @param addressGuGun 구/군
     * @param addressDong  동
     * @return 고유 키 문자열
     */
    public static String generateUniqueAddressKey(String addressDo, String addressSi, String addressGuGun, String addressDong) {
        return addressDo.trim().toLowerCase() + "-" +
                addressSi.trim().toLowerCase() + "-" +
                addressGuGun.trim().toLowerCase() + "-" +
                addressDong.trim().toLowerCase();
    }
}
