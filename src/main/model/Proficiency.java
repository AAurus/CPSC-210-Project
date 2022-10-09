package model;

import enums.ModifierType;
import enums.ProficiencyType;
import enums.ScoreType;

import java.math.BigDecimal;
import java.util.HashMap;

public class Proficiency {
    private final ProficiencyType type;
    private final ScoreType score;
    private final String item;
    private final BigDecimal multiplier;

    public Proficiency(String item, BigDecimal multiplier) {
        type = ProficiencyType.ITEM;
        this.item = item;
        this.multiplier = multiplier;
        this.score = null;
    }

    public Proficiency(ScoreType score, BigDecimal multiplier) {
        type = ProficiencyType.SKILL;
        this.score = score;
        this.multiplier = multiplier;
        this.item = null;
    }

    //REQUIRES: type is not ITEM, proficiencyScore > 0
    //EFFECTS: returns a HashMap compatible with Character score handling
    public HashMap<ScoreType, Modifier> generateScoreMap(BigDecimal proficiencyScore) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        if (type.equals(ProficiencyType.SKILL)) {
            result.put(score, new Modifier(ModifierType.ADD, proficiencyScore.multiply(multiplier)));
        }
        return result;
    }

    //REQUIRES: proficiencyScore > 0
    //EFFECTS: returns a Modifier that represents the bonus gained from this proficiency
    public Modifier generateModifier(BigDecimal proficiencyScore) {
        return new Modifier(ModifierType.ADD, proficiencyScore.multiply(multiplier));
    }

    public ProficiencyType getType() {
        return type;
    }

    public ScoreType getScore() {
        return score;
    }

    public String getItem() {
        return item;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }
}