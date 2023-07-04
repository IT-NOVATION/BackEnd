package com.ItsTime.ItNovation.domain.review.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ReviewInfoDto {

    private Long reviewId;
    private String reviewTitle;
    private String reviewMainText;
    private Boolean hasGoodProduction;
    private Boolean hasGoodScenario;
    private Boolean hasGoodDirecting;
    private Boolean hasGoodOst;
    private Boolean hasGoodVisual;
    private Boolean hasGoodActing;
    private Boolean hasGoodCharterCharming;
    private Boolean hasGoodDiction;
    private Boolean hasCheckDate;
    private Boolean hasSpoiler;
    private String watchDate;


    @Builder
    public ReviewInfoDto(Long reviewId, String reviewTitle, String reviewMainText,
        Boolean hasGoodProduction, Boolean hasGoodScenario, Boolean hasGoodDirecting,
        Boolean hasGoodOst, Boolean hasGoodVisual, Boolean hasGoodActing,
        Boolean hasGoodCharterCharming, Boolean hasGoodDiction, Boolean hasCheckDate,
        Boolean hasSpoiler, String watchDate) {
        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.reviewMainText = reviewMainText;
        this.hasGoodProduction = hasGoodProduction;
        this.hasGoodScenario = hasGoodScenario;
        this.hasGoodDirecting = hasGoodDirecting;
        this.hasGoodOst = hasGoodOst;
        this.hasGoodVisual = hasGoodVisual;
        this.hasGoodActing = hasGoodActing;
        this.hasGoodCharterCharming = hasGoodCharterCharming;
        this.hasGoodDiction = hasGoodDiction;
        this.hasCheckDate = hasCheckDate;
        this.hasSpoiler = hasSpoiler;
        this.watchDate = watchDate;
    }
}
