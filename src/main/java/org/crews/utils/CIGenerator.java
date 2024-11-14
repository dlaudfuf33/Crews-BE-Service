package org.crews.utils;

import org.crews.excaption.HMACGenerationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class CIGenerator {
    private static String algorithm;
    private static String sa;
    private static String sk;

    @Value("${hash.algorithm}")
    public void setAlgorithm(String algorithm) {
        CIGenerator.algorithm = algorithm;
    }

    @Value("${hash.sa}")
    public void setSa(String sa) {
        CIGenerator.sa = sa;
    }

    @Value("${hash.sk}")
    public void setSk(String sk) {
        CIGenerator.sk = sk;
    }

    // HMAC-SHA512 알고리즘을 사용해 해시 생성
    public static byte[] generateHMAC(byte[] key, byte[] data) throws Exception {
        try {
            Mac mac = Mac.getInstance("HmacSHA512"); // 알고리즘 이름이 올바른지 확인
            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA512"); // 알고리즘 일치 확인
            mac.init(keySpec);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // 커스텀 예외로 포장하여 던지기
            throw new HMACGenerationException("암호화 과정에서 오류가 발생하여 HMAC 생성에 실패했습니다.", e);
        }
    }

    // 주민등록번호(RN) + Padding + Sa 준비
    public static byte[] prepareData(String rn, byte[] sa) {
        byte[] rnBytes = rn.getBytes(StandardCharsets.UTF_8);
        byte[] padding = new byte[51]; // 408비트(51바이트)를 0x00으로 채움

        byte[] data = new byte[64]; // 64바이트로 구성 (13바이트 + 51바이트)
        System.arraycopy(rnBytes, 0, data, 0, rnBytes.length);
        System.arraycopy(padding, 0, data, rnBytes.length, padding.length);

        // Sa 추가 (이후에 해시 계산 시 사용)
        byte[] combinedData = new byte[data.length + sa.length];
        System.arraycopy(data, 0, combinedData, 0, data.length);
        System.arraycopy(sa, 0, combinedData, data.length, sa.length);

        return combinedData;
    }

    // CI 생성
    public static String generateCI(String rn) throws Exception {
        // 데이터 준비
        byte[] data = prepareData(rn, sa.getBytes());

        // HMAC-SHA512 해시 생성
        byte[] hmacResult = generateHMAC(sk.getBytes(), data);

        // Base64 인코딩하여 88바이트 문자열 생성
        return Base64.getEncoder().encodeToString(hmacResult);
    }

}