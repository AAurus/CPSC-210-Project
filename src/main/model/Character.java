package model;

import enums.ModifierType;
import enums.ScoreType;

import java.math.BigDecimal;
import java.util.*;

public class Character {
    /// A representation of the various information recorded for a D&D Character.
    private int[] rolledStats = new int[6];
    private HashMap<ScoreType, BigDecimal> scores = new HashMap<>();
    private CharRace race;
    private CharBackground background;
    private ArrayList<CharClass> classes = new ArrayList<>();
    private String name;
    private String description;

    public Character(String name, int[] rolledStats, CharRace race, CharBackground background,
                     CharClass startingClass) {
        this.name = name;
        for (int i = 0; i < 6; i++) {
            this.rolledStats[i] = rolledStats[i];
        }
        this.race = race;
        this.background = background;
        this.classes.add(startingClass);
        initStats();
        updateScores();
        this.description = "";
    }

    public Character(String name, int[] rolledStats, CharRace race, CharBackground background, CharClass startingClass,
                     String description) {
        this.name = name;
        for (int i = 0; i < 6; i++) {
            this.rolledStats[i] = rolledStats[i];
        }
        this.race = race;
        this.background = background;
        this.classes.add(startingClass);
        initStats();
        updateScores();
        this.description = description;
    }

    //MODIFIES: this
    //EFFECTS: initializes scores from rolled stats
    private void initStats() {
        for (ScoreType st : ScoreType.values()) {
            String stGroup = st.name().substring(0,2);
            switch (stGroup) {
                case "STR":
                    scores.put(st, new BigDecimal(rolledStats[0]));
                    break;
                case "DEX":
                    scores.put(st, new BigDecimal(rolledStats[1]));
                    break;
                case "CON":
                    scores.put(st, new BigDecimal(rolledStats[2]));
                    break;
                case "INT":
                    scores.put(st, new BigDecimal(rolledStats[3]));
                    break;
                case "WIS":
                    scores.put(st, new BigDecimal(rolledStats[4]));
                    break;
                case "CHR":
                    scores.put(st, new BigDecimal(rolledStats[5]));
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: applies all score changes from race and classes.
    private void updateScores() {
        HashMap<ScoreType, Modifier> newScores = new HashMap<>();
        ArrayList<HashMap<ScoreType, Modifier>> addScores = new ArrayList<>();
        for (ScoreType st : ScoreType.values()) {
            newScores.put(st, new Modifier(ModifierType.BASE, scores.get(st)));
        }
        addScores.addAll(CharacterModifier.getAllScores(race));
        addScores.addAll(CharacterModifier.getAllScores(background));
        for (CharClass dc : classes) {
            addScores.addAll(CharacterModifier.getAllScores(dc));
        }

        newScores = applyAllScores(addScores, newScores);
        for (ScoreType st : ScoreType.values()) {
            scores.put(st, newScores.get(st).getValue());
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    //MODIFIES: this
    //EFFECTS: adds a DnD class to this and updates scores, keeping earlier classes at end of list
    public void addClass(CharClass dndClass) {
        classes.add(0, dndClass);
        updateScores();
    }

    //REQUIRES: dndClass (the actual object itself, not just an identical object) must exist in classes
    //MODIFIES: this
    //EFFECTS: removes a CharClass to this and updates scores
    public void removeClass(CharClass dndClass) {
        classes.remove(dndClass);
        updateScores();
    }

    //MODIFIES: this
    //EFFECTS: removes the first (latest) CharClass this has that matches the name provided and updates scores
    public void removeClass(String className) {
        for (CharClass dc : classes) {
            if (dc.getName().equals(className)) {
                classes.remove(dc);
                break;
            }
        }
        updateScores();
    }

    //MODIFIES: this
    //EFFECTS: changes this character's race and updates scores to match
    public void changeRace(CharRace race) {
        this.race = race;
        updateScores();
    }

    //MODIFIES: this
    //EFFECTS: changes this character's background and updates scores to match
    public void changeBackground(CharBackground background) {
        this.background = background;
        updateScores();
    }

    //REQUIRES: all modifiers in base should be of type BASE, and base should at least have all pairings that
    //          all elements of apply have
    //EFFECTS: applies all score changes from ArrayList of HashMap<ScoreType, Modifier> to one base HashMap
    private static HashMap<ScoreType, Modifier> applyAllScores(ArrayList<HashMap<ScoreType, Modifier>> apply,
                                                               HashMap<ScoreType, Modifier> base) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        for (ScoreType st : ScoreType.values()) {
            result.put(st, base.get(st));
        }

        for (HashMap<ScoreType, Modifier> score : apply) {
            result = applyScores(result, score, ModifierType.BASE);
        }

        for (HashMap<ScoreType, Modifier> score : apply) {
            result = applyScores(result, score, ModifierType.ADD);
        }

        for (HashMap<ScoreType, Modifier> score : apply) {
            result = applyScores(result, score, ModifierType.MULTIPLY);
        }

        for (HashMap<ScoreType, Modifier> score : apply) {
            result = applyScores(result, score, ModifierType.MIN);
        }

        for (HashMap<ScoreType, Modifier> score : apply) {
            result = applyScores(result, score, ModifierType.MAX);
        }
        return result;
    }

    //REQUIRES: all modifiers in base should be of type BASE, and base should at least have all pairings that apply has
    //EFFECTS: applies score changes from one HashMap<ScoreType, Modifier> to another
    private static HashMap<ScoreType, Modifier> applyScores(HashMap<ScoreType, Modifier> base,
                                                            HashMap<ScoreType, Modifier> apply) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        for (ScoreType st : ScoreType.values()) {
            if (base.containsKey(st)) {
                if (apply.containsKey(st)) {
                    result.put(st, base.get(st).apply(apply.get(st)));
                } else {
                    result.put(st, base.get(st));
                }
            }
        }
        return result;
    }

    //REQUIRES: all modifiers in base should be of type BASE, and base and apply should have 1:1 match of keys
    //EFFECTS: applies score changes from one HashMap<ScoreType, Modifier> to another, as long as the modifier is of a
    //         certain type
    private static HashMap<ScoreType, Modifier> applyScores(HashMap<ScoreType, Modifier> base,
                                                            HashMap<ScoreType, Modifier> apply,
                                                            ModifierType applyType) {
        HashMap<ScoreType, Modifier> result = new HashMap<>();
        for (ScoreType st : ScoreType.values()) {
            if (base.containsKey(st)) {
                if (apply.containsKey(st) && apply.get(st).getType().equals(applyType)) {
                    result.put(st, base.get(st).apply(apply.get(st)));
                } else {
                    result.put(st, base.get(st));
                }
            }
        }
        return result;
    }
}
