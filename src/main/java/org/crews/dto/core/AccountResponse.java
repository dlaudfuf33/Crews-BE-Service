package org.crews.dto.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.crews.dto.response.AccountsInfoResponse;
import org.crews.model.constants.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AccountResponse.class, name = "account"),
        @JsonSubTypes.Type(value = AttachResponse.class, name = "attach")
})
public class AccountResponse {
    private String customerName;
    private String bankCode;
    private String bankImage;
    private String productName;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private LocalDate createdAt;
    private LocalDate updatedAt;



    public AccountResponse(AccountsInfoResponse.AccountInfo accountInfo) {
        this.customerName = accountInfo.getCustomerName();
        this.bankCode = accountInfo.getBankCode();
        this.bankImage = accountInfo.getBankImage();
        this.productName = accountInfo.getProductName();
        this.accountNumber = accountInfo.getAccountNumber();
        this.accountType = accountInfo.getAccountType();
        this.balance = accountInfo.getBalance();
        this.createdAt = accountInfo.getCreatedAt();
        this.updatedAt = accountInfo.getUpdatedAt();
    }

}