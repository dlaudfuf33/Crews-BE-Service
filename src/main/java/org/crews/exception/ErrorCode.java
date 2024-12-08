package org.crews.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 400 BAD_REQUEST
    NO_ASSOCIATED_GROUP("소속된 모임이 없습니다.", HttpStatus.BAD_REQUEST),
    WRONG_BANKCODE("잘못된 은행코드 입니다.", HttpStatus.BAD_REQUEST),
    NOT_MATCHED_MEMBER("해당하는 아지트의 멤버가 아닙니다.", HttpStatus.BAD_REQUEST),
    REQUIRED_NOT_NULL("필수 값이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    WRONG_RESPONSE("잘못된 응답을 수신했습니다.", HttpStatus.BAD_REQUEST),
    AGIT_NOT_MATCHED("해당하는 번호의 아지트를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_MATCHED("해당하는 번호의 멤버가 없습니다.", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_MATCHED_FINNUM("해당 핀테크에 해당하는 계좌를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    CARD_NOT_MATCHED_MEMBER("해당 회원에 해당하는 카드를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_INTEREST_ID("유효하지 않은 관심사 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_INTERESTS_COUNT("관심사는 1개 이상, 3개 이하로 설정해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_PAGE_NUMBER("유효하지 않은 페이지 범위입니다. 0 이상의 정수로 입력하세요.", HttpStatus.BAD_REQUEST),
    VERIFY_NUMBER_MISMATCH("인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    VERIFY_NUMBER_EXPIRED("인증번호가 만료되었습니다. 다시 요청해주세요.", HttpStatus.BAD_REQUEST),
    VERIFY_PIN_MISMATCH("PIN번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    DATE_AFTER_NOW("지정한 날짜가 현재 날짜보다 더 이후의 날짜입니다.",HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_ALLOWED("멤버 권한 설정을 할 수 없습니다.",HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE("잔액이 부족합니다.",HttpStatus.BAD_REQUEST),

    // 403 FORBIDDEN
    AUTHORIZED_CAPTAIN_ONLY("모임장이나 공동 모임장만 권한이 있습니다.", HttpStatus.FORBIDDEN),
    AUTHORIZED_MEETING_CREATION("모임을 생성할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    AUTHORIZED_ACCOUNT_CREATION("통장을 생성할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    AUTHORIZED_INTRODUCING_UPDATE("모임소개를 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    AUTHORIZED_FEED_UPDATE("기록을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    AUTHORIZED_FEED_DELETE("기록을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_OLD_PASSWORD("비밀번호가 틀렸습니다.", HttpStatus.FORBIDDEN),
    AUTHORIZED_CARD_DELETE("카드 삭제권한이 없습니다.", HttpStatus.FORBIDDEN),
    PINNUMBER_AND_ID_NOT_MATCH("핀번호와 멤버id가 맞지 않습니다.",HttpStatus.FORBIDDEN),

    // 404 NOT FOUND
    AGIT_ACCOUNT_NOT_FOUND("해당 모임에 가입하지 않았거나 권한이 없습니다.", HttpStatus.NOT_FOUND),
    MEMBERSHIP_NOT_FOUND("해당 회원이 모임에 참여하고 있지 않습니다.", HttpStatus.NOT_FOUND),
    BANK_NOT_FOUND("해당하는 은행을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INTERESTS_NOT_FOUND("관심사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AGIT_NOT_FOUND("아지트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("이 이메일로 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SUBJECT_NOT_FOUND("주제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EVENT_NOT_FOUND("해당하는 번호의 정기모임이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    COMMON_DUES_NOT_FOUND("공통회비가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ACCOUNT_ID_NOT_FOUND("계좌에 해당하는 id가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    FEED_NOT_FOUND("해당하는 번호의 기록이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ADDRESS_NOT_FOUND("해당 회원의 주소를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MESSAGE_NOT_FOUND("해당 번호로 발송된 메세지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NO_ACCOUNTS_RETURNED("계좌가 없습니다.", HttpStatus.NOT_FOUND),

    // 409 CONFLICT
    CARD_ALREADY_EXISTS("카드가 이미 존재하여 추가 발급할 수 없습니다.", HttpStatus.CONFLICT),
    PRESENT_AGIT_AND_ACCOUNT("AgitAndAccount가 이미 존재합니다.", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    ACCOUNT_ALREADY_EXISTS("이미 존재하는 계좌입니다.", HttpStatus.CONFLICT),
    ALREADY_REPORTED_FEED("이미 신고한 피드입니다.",HttpStatus.CONFLICT),
    AGIT_ALREADY_JOINED("이미 가입한 아지트입니다.",HttpStatus.CONFLICT),

    // 410 삭제된 데이터
    DELETED_MEETING("이미 삭제된 정기모임 입니다.", HttpStatus.GONE),
    DELETED_FEED("이미 삭제된 기록 입니다.", HttpStatus.GONE),
    DELETED_MEMBER("이미 탈퇴한 회원입니다.", HttpStatus.GONE),

    // 422 유효성 검사 실패
    PASSWORD_CONFIRMATION_MISMATCH("변경할 비밀번호와 일치하지 않습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_NICKNAME("닉네임은 공백 없이 16자 이하로 작성해야 합니다.", HttpStatus.UNPROCESSABLE_ENTITY),

    // 500 SERVER_ERROR
    CI_CODE_SEND_ERROR("CI 코드 전송 오류. 다시 회원가입을 진행 해 주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ACCESS_FAILED("데이터베이스 접근 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_EXCEPTION("예상 외 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ENCRYPTION_FAILED("이메일 암호화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CORE_RESPONSE_ERROR("CORE_RESPONSE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    SEND_MESSAGE_FAILED("메세지 발송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    AGIT_APPLY_ERROR("아지트 가입신청을 하는 도중 문제가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 501
    IS_UTILITY_CLASS("인스턴화 할 수 없는 유틸클래스 입니다.", HttpStatus.NOT_IMPLEMENTED);


    private final String message;
    private final HttpStatus httpStatus;

}
