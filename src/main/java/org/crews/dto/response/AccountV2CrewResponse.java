package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.model.Agit;
import org.crews.utils.AESUtil;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountV2CrewResponse {

    private Long agitId;
    private String agitName;
    private String bankCode;
    private String bankName;
    private String accountNumber;
    private String productName;
    private BigDecimal balance;

    public static AccountV2CrewResponse from(Agit agit){
        return AccountV2CrewResponse
                .builder()
                .agitId(agit.getId())
                .agitName(agit.getAgitName())
                .bankCode(agit.getAgitAndAccount().getAccount().getBank().getBankCode())
                .bankName(agit.getAgitAndAccount().getAccount().getBank().getBankName())
                .accountNumber(AESUtil.decrypt(agit.getAgitAndAccount().getAccount().getAccountNumber()))
                .productName(agit.getAgitAndAccount().getAccount().getProductName())
                .balance(agit.getAgitAndAccount().getAccount().getBalance())
                .build();

    }
}
