package org.crews.dto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.model.Account;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceRequest {
    private List<String> fintechNum;

    public static BalanceRequest from(List<Account> accountList) {
        List<String> fintechNumList = accountList.stream()
                .map(Account::getFintecNumber)
                .toList();
        return new BalanceRequest(fintechNumList);
    }
}
