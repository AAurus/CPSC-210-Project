package enums;

import model.Modifier;
import utility.ListOfHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum StatType {
    MAX_HIT_POINTS,
    CURRENT_HIT_POINTS,
    TEMPORARY_HIT_POINTS,
    HIT_POINT_CON_PER_LEVEL,

    INITIATIVE_BONUS,
    INITIATIVE,

    WALK_SPEED,
    CLIMB_SPEED,
    SWIM_SPEED,
    FLY_SPEED,

    ARMOR_CLASS,
    DEXTERITY_ARMOR_BONUS,
    PROFICIENCY_BONUS,

    CARRY_CAPACITY;

    //REQUIRES: all Modifiers in modifierStats should be of type BASE
    //EFFECTS: creates integer HashMap from Modifier HashMap for use in Character
    public static HashMap<StatType, Integer> finalizeStats(HashMap<StatType, Modifier> modifierStats) {
        HashMap<StatType, Integer> result = new HashMap<>();
        for (StatType s : modifierStats.keySet()) {
            result.put(s, modifierStats.get(s).getValue().setScale(0, RoundingMode.FLOOR).intValue());
        }
        return result;
    }

    //EFFECTS: creates basic hashmap of stats
    public static HashMap<StatType, Integer> initBasicStats() {
        HashMap<StatType, Integer> result = new HashMap<>();
        result.put(MAX_HIT_POINTS, 0);
        result.put(CURRENT_HIT_POINTS, 0);
        result.put(TEMPORARY_HIT_POINTS, 0);
        result.put(HIT_POINT_CON_PER_LEVEL, 1);
        result.put(INITIATIVE_BONUS, 0);
        result.put(INITIATIVE, 0);
        result.put(WALK_SPEED, 0);
        result.put(CLIMB_SPEED, 0);
        result.put(SWIM_SPEED, 0);
        result.put(FLY_SPEED, 0);
        result.put(ARMOR_CLASS, 0);
        result.put(DEXTERITY_ARMOR_BONUS, 0);
        result.put(PROFICIENCY_BONUS, 0);
        result.put(CARRY_CAPACITY, 0);
        return result;
    }

    //EFFECTS: returns a Modifier version of an integer stat map
    public static HashMap<StatType, Modifier> convertToModStats(HashMap<StatType, Integer> baseMap) {
        HashMap<StatType, Modifier> result = new HashMap<>();
        for (StatType s : baseMap.keySet()) {
            result.put(s, new Modifier(baseMap.get(s)));
        }
        return result;
    }

    //REQUIRES: all Modifiers in baseMap should be of type BASE; MAX_HIT_POINTS, INITIATIVE_BONUS, WALK_SPEED,
    //          DEXTERITY_ARMOR_BONUS, CARRY_CAPACITY should be keyed to values in baseMap
    //EFFECTS: derives certain stats from other stats to return a full HashMap
    public static HashMap<StatType, Modifier> deriveStats(HashMap<StatType, Modifier> baseMap) {
        HashMap<StatType, Modifier> result = new HashMap<>();
        result.put(MAX_HIT_POINTS, baseMap.get(MAX_HIT_POINTS));
        result.put(CURRENT_HIT_POINTS, baseMap.get(MAX_HIT_POINTS));
        result.put(TEMPORARY_HIT_POINTS, new Modifier(0));
        result.put(HIT_POINT_CON_PER_LEVEL, new Modifier(1));
        result.put(INITIATIVE_BONUS, baseMap.get(INITIATIVE_BONUS));
        result.put(INITIATIVE, new Modifier(ModifierType.BASE, BigDecimal.TEN.add(
                baseMap.get(INITIATIVE_BONUS).getValue())));
        result.put(WALK_SPEED, baseMap.get(WALK_SPEED));
        result.put(CLIMB_SPEED, new Modifier(ModifierType.BASE, baseMap.get(WALK_SPEED).getValue().divide(
                new BigDecimal(2))));
        result.put(SWIM_SPEED, new Modifier(ModifierType.BASE, baseMap.get(WALK_SPEED).getValue().divide(
                new BigDecimal(2))));
        result.put(FLY_SPEED, new Modifier(0));
        result.put(ARMOR_CLASS, baseMap.get(DEXTERITY_ARMOR_BONUS));
        result.put(DEXTERITY_ARMOR_BONUS, baseMap.get(DEXTERITY_ARMOR_BONUS));
        result.put(PROFICIENCY_BONUS, baseMap.get(PROFICIENCY_BONUS));
        result.put(CARRY_CAPACITY, baseMap.get(CARRY_CAPACITY));

        return result;
    }

    //REQUIRES: all modifiers in base should be of type BASE, and every filled key in apply should also have a value
    //          in base
    //EFFECTS: applies all stat changes from ArrayList of HashMap<StatType, Modifier> to one base HashMap
    public static HashMap<StatType, Modifier> applyAllStatsToAll(HashMap<StatType, Modifier> base,
                                                                   List<HashMap<StatType, Modifier>> apply) {
        HashMap<StatType, Modifier> result = new HashMap<>();
        for (StatType s : StatType.values()) {
            result.put(s, base.get(s));
        }
        for (int i = 0; i < Modifier.OPERATIONS_ORDER.length; i++) {
            for (HashMap<StatType, Modifier> stat : apply) {
                result = applyStats(result, stat, Modifier.OPERATIONS_ORDER[i]);
            }
        }
        return result;
    }

    //REQUIRES: all modifiers in base should be of type BASE, and every filled key in apply should also have a value
    //          in base
    //EFFECTS: applies all stat changes from ArrayList of HashMap<StatType, Modifier> to one base HashMap
    public static HashMap<StatType, Modifier> applyAllStatsToList(HashMap<StatType, Modifier> base,
                                                                    List<HashMap<StatType, Modifier>> apply,
                                                                    List<StatType> statTypes) {
        HashMap<StatType, Modifier> result = new HashMap<>();
        for (StatType s : statTypes) {
            result.put(s, base.get(s));
        }
        for (int i = 0; i < Modifier.OPERATIONS_ORDER.length; i++) {
            for (HashMap<StatType, Modifier> stat : apply) {
                result = applyStats(result, stat, Modifier.OPERATIONS_ORDER[i]);
            }
        }
        return result;
    }

    //REQUIRES: all modifiers in base should be of type BASE, and every filled key in apply should also have a value
    //          in base
    //EFFECTS: applies stat changes from one HashMap<StatType, Modifier> to another
    public static HashMap<StatType, Modifier> applyStatsToAll(HashMap<StatType, Modifier> base,
                                                                HashMap<StatType, Modifier> apply) {
        HashMap<StatType, Modifier> result = new HashMap<>();
        for (StatType s : StatType.values()) {
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
    //EFFECTS: applies stat changes from one HashMap<StatType, Modifier> to another, as long as the modifier is of a
    //         certain type
    public static HashMap<StatType, Modifier> applyStats(HashMap<StatType, Modifier> base,
                                                           HashMap<StatType, Modifier> apply,
                                                           ModifierType applyType) {
        HashMap<StatType, Modifier> result = new HashMap<>();
        for (StatType s : StatType.values()) {
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

    //EFFECTS: returns all stat types that are not used in deriveStats
    public static List<StatType> getNonDerivingStats() {
        ArrayList<StatType> result = new ArrayList<>(ListOfHelper.listOf(StatType.values()));
        result.removeAll(ListOfHelper.listOf(MAX_HIT_POINTS, INITIATIVE_BONUS, WALK_SPEED,
                DEXTERITY_ARMOR_BONUS, CARRY_CAPACITY));
        return result;
    }
}
