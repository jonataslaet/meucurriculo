package com.meucurriculo.testsupport.factories;

import com.meucurriculo.controllers.dtos.AwardInputDTO;

public final class AwardFactory {
    private AwardFactory() {}

    public static AwardInputDTO awardBestDeveloper2025() {
        return new AwardInputDTO(
                "Best Developer",
                "Awarded for outstanding contributions",
                2025
        );
    }

    public static AwardInputDTO awardCommunityStar2024() {
        return new AwardInputDTO(
                "Community Star",
                "Helping the community",
                2024
        );
    }

    public static AwardInputDTO awardWithBlankTitle() {
        return new AwardInputDTO(
                "",
                "Helping the community",
                2024
        );
    }

    public static AwardInputDTO awardWithBlankDescription() {
        return new AwardInputDTO(
                "Test",
                "  ",
                2024
        );
    }

    public static AwardInputDTO awardWithNullDescription() {
        return new AwardInputDTO(
                "Test",
                null,
                2024
        );
    }

}
