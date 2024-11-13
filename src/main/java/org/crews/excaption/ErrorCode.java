package org.crews.excaption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    BANK_NOT_FOUND("해당하는 은행을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("해당하는 고객을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    NAUTHORIZED_ACCOUNT_CREATION("통장을 생성할 권한이 없습니다.",HttpStatus.BAD_REQUEST),
    HASH_ALGORITHM_NOT_FOUND("해시 알고리즘을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    WRONG_BANKCODE("잘못된 뱅크코드 입니다.",HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_FOUND_BY_FINNUM("해당 핀테크에 해당하는 계좌를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    AGIT_NOT_FOUND("해당하는 번호의 아지트를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    AGIT_ACCOUNT_NOT_FOUND("해당하는 아지트의 모임통장이 없습니다.",HttpStatus.NOT_FOUND);
    private final String message;
    private final HttpStatus httpStatus;

}
