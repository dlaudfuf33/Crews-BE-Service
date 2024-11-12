package org.crews.utils;

import org.crews.config.AESConfig;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // GCM 태그 길이 (비트 단위)

    private static AESConfig aesConfig;

    public AESUtil(AESConfig aesConfig) {
        AESUtil.aesConfig = aesConfig;
    }

    public static String encrypt(String data) throws AESUtilException {
        try {
            if (aesConfig.getSecretKey() == null || aesConfig.getIv() == null) {
                throw new AESUtilException("SecretKey 또는 IV가 설정되지 않았습니다.");
            }

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(aesConfig.getSecretKey().getBytes(), "AES");

            // GCMParameterSpec 사용 (IV와 태그 길이 필요)
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, aesConfig.getIv().getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new AESUtilException("데이터 암호화 중 오류 발생", e);
        }
    }

    public static String decrypt(String encryptedData) throws AESUtilException {
        try {
            if (aesConfig.getSecretKey() == null || aesConfig.getIv() == null) {
                throw new AESUtilException("SecretKey 또는 IV가 설정되지 않았습니다.");
            }

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(aesConfig.getSecretKey().getBytes(), "AES");

            // GCMParameterSpec 사용 (IV와 태그 길이 필요)
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, aesConfig.getIv().getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            return new String(cipher.doFinal(decodedBytes));
        } catch (Exception e) {
            throw new AESUtilException("데이터 복호화 중 오류 발생", e);
        }
    }

    // 사용자 정의 예외 클래스 정의
    public static class AESUtilException extends RuntimeException {
        public AESUtilException(String message) {
            super(message);
        }

        public AESUtilException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
