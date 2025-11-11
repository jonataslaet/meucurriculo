package com.meucurriculo.testsupport.factories;

import com.meucurriculo.controllers.dtos.HardSkillInputDTO;

public final class HardSkillFactory {
    private HardSkillFactory() {}

    public static HardSkillInputDTO javaSkill() {
        return new HardSkillInputDTO("Java");
    }

    public static HardSkillInputDTO springSkill() {
        return new HardSkillInputDTO("Spring");
    }

    public static HardSkillInputDTO blankNameSkill() {
        return new HardSkillInputDTO("");
    }
}
