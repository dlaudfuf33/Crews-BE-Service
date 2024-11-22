package org.crews.utils;

import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;

import java.util.List;
import java.util.Random;

public final class NicknameGenerator {
    private static final List<String> COLORS = List.of(
            "빨간", "파란", "초록", "노란", "보라", "검은", "흰", "분홍", "연두", "연보라", "주황",
            "하늘색", "자주색", "청록", "금색", "은색", "갈색", "회색", "민트색", "버건디", "옥색", "산호색"
    );
    private static final List<String> OBJECTS = List.of(
            "모자", "양말", "갑판장", "선장", "구명조끼", "돛", "배꼽종", "객실", "구명보트", "정박지", "도크",
            "등대", "앵커", "조타수", "갑판", "망원경", "항해일지", "나침반", "비상구", "부표", "해달", "갈매기",
            "거북이", "구명튜브", "수평선", "비둘기", "선실", "바람개비", "돛단배", "나팔", "해적기", "선창",
            "연회장", "식당", "바", "칵테일", "바텐더", "수영장", "자쿠지", "워터슬라이드", "침대", "베개", "이불",
            "구명줄", "산책로", "태양광선", "썬베드", "갑판의자", "등받이", "파도", "선실번호판", "무전기", "레스토랑",
            "피아노", "탁자", "의자", "오션뷰", "창문", "유리잔", "야경", "태양", "달빛", "별자리", "항구", "네비게이션",
            "라운지", "무대", "극장", "연기자", "스포트라이트", "댄스홀", "헬스장", "러닝머신", "요트", "스쿠버장비",
            "산호초", "해양생물", "프런트데스크", "조명", "운전대", "레이다", "선박호출기", "구명밧줄", "정박장치"
    );
    private static final Random RANDOM = new Random();
    private NicknameGenerator() {
        throw new CustomException(ErrorCode.IS_UTILITY_CLASS);
    }

    public static String generateRandomNickname() {
        String randomColor = COLORS.get(RANDOM.nextInt(COLORS.size()));
        String randomObject = OBJECTS.get(RANDOM.nextInt(OBJECTS.size()));
        return randomColor + randomObject;
    }
}
