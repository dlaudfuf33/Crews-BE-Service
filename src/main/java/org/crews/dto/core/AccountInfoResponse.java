package org.crews.dto.core;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class AccountInfoResponse {
    private List<AccountIssuedResponse> accountList = new ArrayList<>();
    private List<CardList> cardList = new ArrayList<>();
}
