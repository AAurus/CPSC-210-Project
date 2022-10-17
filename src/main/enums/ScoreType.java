package enums;

import model.Modifier;
import utility.ListOfHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum ScoreType {
    /// An enumeration to capture the six main ability scores.
    STRENGTH,
    DEXTERITY,
    CONSTITUTION,
    INTELLIGENCE,
    WISDOM,
    CHARISMA,

    STR_CHECK,
    DEX_CHECK,
    CON_CHECK,
    INT_CHECK,
    WIS_CHECK,
    CHR_CHECK,

    STR_SAVE,
    DEX_SAVE,
    CON_SAVE,
    INT_SAVE,
    WIS_SAVE,

    STR_ATHLETICS,
    DEX_ACROBATICS,
    DEX_SLEIGHT_OF_HAND,
    DEX_STEALTH,
    INT_ARCANA,
    INT_HISTORY,
    INT_INVESTIGATION,
    INT_NATURE,
    INT_RELIGION,
    WIS_ANIMAL_HANDLING,
    WIS_INSIGHT,
    WIS_MEDICINE,
    WIS_SURVIVAL,
    CHR_DECEPTION,
    CHR_INTIMIDATION,
    CHR_PERFORMANCE,
    CHR_PERSUASION,
    ETC;

    //EFFECTS: replicates ListOfHelper.listOf(E... elements) for ScoreType
    public static List<ScoreType> listOf(ScoreType... scoreTypes) {
        ArrayList<ScoreType> result = new ArrayList<>();
        for (ScoreType s : scoreTypes) {
            result.add(s);
        }
        return result;
    }

    //REQUIRES: STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA are all keyed to values in baseScores
    //EFFECTS: initializes a check modifier HashMap using another HashMap containing base ability scores
    public static HashMap<ScoreType, Modifier> initCheckScoreMods(HashMap<ScoreType, Integer> baseScores) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        for (ScoreType s : ScoreType.CHECK_SCORES) {
            String stGroup = s.name().substring(0,4);
            result.put(s, new Modifier(Math.floorDiv((baseScores.get(getBaseScoreFromHeader(stGroup)) - 10), 2)));
        }
        return result;
    }

    //REQUIRES: STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA are all keyed to values in baseScores
    //EFFECTS: initializes a check modifier HashMap using another HashMap containing base ability scores
    public static HashMap<ScoreType, Modifier> initBaseScoreMods(HashMap<ScoreType, Integer> baseScores) {
        HashMap<ScoreType, Modifier> result = generateEmptyScoreMap();
        for (ScoreType s : BASE_SCORES) {
            result.put(s, new Modifier(baseScores.get(s)));
        }
        return result;
    }

    //REQUIRES: all Modifiers in modifierScores should be of type BASE
    //EFFECTS: returns a HashMap of the floored integer result of all modifiers in modifierScores keyed to the
    //         same ScoreTypes
    public static HashMap<ScoreType, Integer> finalizeScoreMods(HashMap<ScoreType, Modifier> modifierScores) {
        HashMap<ScoreType, Integer> result = new HashMap<>();
        for (ScoreType s : modifierScores.keySet()) {
            result.put(s, modifierScores.get(s).getValue().setScale(0, RoundingMode.FLOOR).intValue());
        }
        return result;
    }

    //REQUIRES: header is "STR_", "DEX_", "CON_", "INT_", "WIS_", or "CHR_"
    //EFFECTS: returns base ability score type from skill/save type header
    private static ScoreType getBaseScoreFromHeader(String header) {
        switch (header) {
            case "STR_":
                return ScoreType.STRENGTH;
            case "DEX_":
                return ScoreType.DEXTERITY;
            case "CON_":
                return ScoreType.CONSTITUTION;
            case "INT_":
                return ScoreType.INTELLIGENCE;
            case "WIS_":
                return ScoreType.WISDOM;
            case "CHR_":
                return ScoreType.CHARISMA;
            default:
                return ScoreType.ETC;
        }
    }

    public static final List<ScoreType> BASE_SCORES = ListOfHelper.listOf(STRENGTH,     DEXTERITY,
                                                                          CONSTITUTION, INTELLIGENCE,
                                                                          WISDOM,       CHARISMA);

    public static final List<ScoreType> CHECK_SCORES = ListOfHelper.listOf(ScoreType.values()).subList(6,
                                                               ScoreType.values().length - 1);

    public static HashMap<ScoreType, Modifier> generateEmptyScoreMap() {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        for (ScoreType s : ScoreType.values()) {
            result.put(s, new Modifier(ModifierType.BASE, BigDecimal.ZERO));
        }
        return result;
    }

    //REQUIRES: all modifiers in base should be of type BASE, and every filled key in apply should also have a value
    //          in base
    //EFFECTS: applies all score changes from ArrayList of HashMap<ScoreType, Modifier> to one base HashMap
    public static HashMap<ScoreType, Modifier> applyAllScoresToAll(HashMap<ScoreType, Modifier> base,
                                                                   List<HashMap<ScoreType, Modifier>> apply) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        for (ScoreType s : ScoreType.values()) {
            result.put(s, base.get(s));
        }
        for (int i = 0; i < Modifier.OPERATIONS_ORDER.length; i++) {
            for (HashMap<ScoreType, Modifier> score : apply) {
                result = applyScores(result, score, Modifier.OPERATIONS_ORDER[i]);
            }
        }
        return result;
    }

    //REQUIRES: all modifiers in base should be of type BASE, and every filled key in apply should also have a value
    //          in base
    //EFFECTS: applies all score changes from ArrayList of HashMap<ScoreType, Modifier> to one base HashMap
    public static HashMap<ScoreType, Modifier> applyAllScoresToList(HashMap<ScoreType, Modifier> base,
                                                                    List<HashMap<ScoreType, Modifier>> apply,
                                                                    List<ScoreType> scoreTypes) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        for (ScoreType s : scoreTypes) {
            result.put(s, base.get(s));
        }
        for (int i = 0; i < Modifier.OPERATIONS_ORDER.length; i++) {
            for (HashMap<ScoreType, Modifier> score : apply) {
                result = applyScores(result, score, Modifier.OPERATIONS_ORDER[i]);
            }
        }
        return result;
    }

    //REQUIRES: all modifiers in base should be of type BASE, and every filled key in apply should also have a value
    //          in base
    //EFFECTS: applies score changes from one HashMap<ScoreType, Modifier> to another
    public static HashMap<ScoreType, Modifier> applyScoresToAll(HashMap<ScoreType, Modifier> base,
                                                                HashMap<ScoreType, Modifier> apply) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        for (ScoreType s : ScoreType.values()) {
            if (base.containsKey(s)) {
                if (apply.containsKey(s)) {
                    result.put(s, base.get(s).apply(apply.get(s)));
                } else {
                    result.put(s, base.get(s));
                }
            }
        }
        return result;
    }

    //REQUIRES: all modifiers in base should be of type BASE, and every filled key in apply should also have a value
    //          in base
    //EFFECTS: applies score changes from one HashMap<ScoreType, Modifier> to another, as long as the modifier is of a
    //         certain type
    public static HashMap<ScoreType, Modifier> applyScores(HashMap<ScoreType, Modifier> base,
                                                           HashMap<ScoreType, Modifier> apply,
                                                           ModifierType applyType) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        for (ScoreType s : ScoreType.values()) {
            if (base.containsKey(s)) {
                if (apply.containsKey(s) && apply.get(s).getType().equals(applyType)) {
                    result.put(s, base.get(s).apply(apply.get(s)));
                } else {
                    result.put(s, base.get(s));
                }
            }
        }
        return result;
    }
}
