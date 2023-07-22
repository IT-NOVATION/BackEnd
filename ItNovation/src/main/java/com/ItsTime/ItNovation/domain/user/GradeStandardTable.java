package com.ItsTime.ItNovation.domain.user;


import java.util.Arrays;
import lombok.Getter;

@Getter
public enum GradeStandardTable {
    STANDARD(0,0, 0, Grade.STANDARD),
    PREMIUM(5, 10, 1, Grade.PREMIUM),
    VIP(10, 20, 2, Grade.VIP),
    SPECIAL(30, 30, 3, Grade.SPECIAL);

    private final int reviewCount;
    private final int commentCount;
    private final int level;
    private final Grade grade;

    GradeStandardTable(int reviewCount, int commentCount, int level, Grade grade) {
        this.reviewCount = reviewCount;
        this.commentCount = commentCount;
        this.level = level;
        this.grade = grade;
    }

    public static GradeStandardTable getByLevel(int level){
        return Arrays.stream(values())
            .filter(table -> table.level==level)
            .findFirst()
            .orElse(null);
    }


}
