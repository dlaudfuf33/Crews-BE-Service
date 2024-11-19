package org.crews.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 404 NOT_FOUND
    BANK_NOT_FOUND("해당하는 은행을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INTERESTS_NOT_FOUND("관심사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_MATCHED("해당하는 번호의 멤버가 없습니다.", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_MATCHED_FINNUM("해당 핀테크에 해당하는 계좌를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AGIT_NOT_FOUND("아지트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AGIT_NOT_MATCHED("해당하는 번호의 아지트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AGIT_ACCOUNT_NOT_FOUND("해당하는 아지트의 모임통장이 없습니다.", HttpStatus.NOT_FOUND),
    MEMBERSHIP_NOT_FOUND("해당 회원이 모임에 참여하고 있지 않습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("이 이메일로 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 400 BAD_REQUEST
    AUTHORIZED_ACCOUNT_CREATION("통장을 생성할 권한이 없습니다.", HttpStatus.BAD_REQUEST),
    WRONG_BANKCODE("잘못된 은행코드 입니다.", HttpStatus.BAD_REQUEST),
    NOT_MATCHED_MEMBER("해당하는 아지트의 멤버가 아닙니다.", HttpStatus.BAD_REQUEST),
    REQUIRED_NOT_NULL("필수 값이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    WRONG_RESPONSE("잘못된 응답을 수신했습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST),
    CI_CODE_SEND_ERROR("CI 코드 전송 오류. 다시 회원가입을 진행 해 주세요.", HttpStatus.BAD_REQUEST),


    // 403 FORBIDDEN
    CREW_ROLE_NOT_AUTHORIZED("모임장이나 공동 모임장만 권한이 있습니다.", HttpStatus.FORBIDDEN),

    // 409 CONFLICT
    CARD_ALREADY_EXISTS("카드가 이미 존재하여 추가 발급할 수 없습니다.", HttpStatus.CONFLICT),

    // 500 SERVER_ERROR
    DATABASE_ACCESS_FAILED("데이터베이스 접근 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ENCRYPTION_FAILED("이메일 암호화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

}
