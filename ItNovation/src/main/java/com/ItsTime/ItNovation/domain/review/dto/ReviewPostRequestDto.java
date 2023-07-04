package com.ItsTime.ItNovation.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
@ToString
@Getter
public class ReviewPostRequestDto {
    private  Long movieId;
    private  Float star;
    private  String reviewTitle;
    private  String reviewMainText;
    private  Boolean hasGoodStory;

    private  Boolean hasGoodProduction;

    private  Boolean hasGoodScenario;

    private  Boolean hasGoodDirecting;

    private  Boolean hasGoodOst;

    private  Boolean hasGoodVisual;

    private  Boolean hasGoodActing;
    private  Boolean hasGoodCharterCharming;
    private  Boolean hasGoodDiction;
    private  Boolean hasCheckDate;
    private  Boolean hasSpoiler;

    private  String watchDate;


}
