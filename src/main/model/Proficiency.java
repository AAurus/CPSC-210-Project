package model;

import enums.*;
import utility.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class Proficiency {
    ///A representation of a D&D Proficiency.
    public static final int HASH_MULTIPLIER = 157;

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
        type = ProficiencyType.SCORE;
        this.score = score;
        this.multiplier = multiplier;
        this.item = null;
    }

    //REQUIRES: type is not ITEM, proficiencyScore > 0
    //EFFECTS: returns a HashMap compatible with Character score handling
    public HashMap<ScoreType, Modifier> generateScoreMap(BigDecimal proficiencyScore) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        if (type.equals(ProficiencyType.SCORE)) {
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

    public String getProficiencyName() {
        switch (type) {
            case SCORE:
                return score.name();
            case ITEM:
            default:
                return item;
        }
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

    @Override
    public int hashCode() {
        ArrayList<Integer> hashComponents = new ArrayList<>();
        hashComponents.add(type.hashCode());
        hashComponents.add(Utility.hashIfAble(score));
        hashComponents.add(Utility.hashIfAble(item));
        hashComponents.add(multiplier.hashCode());
        return Utility.hashCodeHelper(hashComponents, HASH_MULTIPLIER);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Proficiency other = (Proficiency) obj;
        return (this.hashCode() == other.hashCode());
    }
}
