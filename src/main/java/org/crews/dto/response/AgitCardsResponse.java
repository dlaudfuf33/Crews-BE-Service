package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Agit;
import org.crews.model.Card;

@Getter
@Builder
public class AgitCardsResponse {
    private Long cardId;
    private String cardImage;
    private String cardName;
    private String maskedCardNum;
    private String agitName;

    public AgitCardsResponse(Long cardId, String cardImage, String cardName, String maskedCardNum, String agitName) {
        this.cardId = cardId;
        this.cardImage = cardImage;
        this.cardName = cardName;
        this.maskedCardNum = maskedCardNum;
        this.agitName = agitName;
    }

    public static AgitCardsResponse of(Card card, Agit agit) {
        return AgitCardsResponse.builder()
                .cardImage(card.getCardImage())
                .cardName(card.getCardName())
                .maskedCardNum(card.getMaskedCardNumber().substring(0, 4))
                .agitName(agit.getAgitName())
                .build();
    }

}
