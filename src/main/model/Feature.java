package model;

import enums.FeatureType;
import enums.ScoreType;
import enums.StatType;

import java.math.BigDecimal;
import java.util.*;

public class Feature {
    ///A representation of a D&D Character Feature, that may produce various effects.
    public static final int DEFAULT_MAXIMUM_CHOICES = 1;

    private final String name;
    private final FeatureType type;
    private final HashMap<ScoreType, Modifier> scoreMod;
    private final HashMap<StatType, Modifier> statMod;
    private final Proficiency proficiency;
    private final Attack attack;
    private final List<Feature> features;
    private final boolean choice;
    private final int choiceCount;
    private List<Integer> choices;
    private String description;

    public Feature(String name, ScoreType slot, Modifier mod) {
        this.name = name;
        this.type = FeatureType.SCORE;
        this.scoreMod = new HashMap<>();
        scoreMod.put(slot, mod);
        this.statMod = null;
        this.proficiency = null;
        this.attack = null;
        this.features = null;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = "";
    }

    public Feature(String name, StatType slot, Modifier mod) {
        this.name = name;
        this.type = FeatureType.STAT;
        this.scoreMod = null;
        this.statMod = new HashMap<>();
        statMod.put(slot, mod);
        this.proficiency = null;
        this.attack = null;
        this.features = null;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = "";
    }

    public Feature(String name, Proficiency proficiency) {
        this.name = name;
        this.type = FeatureType.PROFICIENCY;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = proficiency;
        this.attack = null;
        this.features = null;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = "";
    }

    public Feature(String name, Attack attack) {
        this.name = name;
        this.type = FeatureType.ATTACK;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.attack = attack;
        this.features = null;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = "";
    }

    public Feature(String name, List<Feature> features) {
        this.name = name;
        this.type = FeatureType.MULTI;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.attack = null;
        this.features = features;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = "";
    }

    public Feature(String name, List<Feature> features, boolean choice) {
        this.name = name;
        this.type = FeatureType.MULTI;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.attack = null;
        this.features = features;
        this.choice = choice;
        this.choiceCount = DEFAULT_MAXIMUM_CHOICES;
        this.choices = new ArrayList<>();
        this.description = "";
    }

    public Feature(String name, List<Feature> features, boolean choice, int choiceCount) {
        this.name = name;
        this.type = FeatureType.MULTI;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.attack = null;
        this.features = features;
        this.choice = choice;
        this.choiceCount = choiceCount;
        this.choices = new ArrayList<>();
        this.description = "";
    }

    public Feature(String name, String desc) {
        this.name = name;
        this.type = FeatureType.ETC;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.attack = null;
        this.features = null;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = desc;
    }

    public Feature(String name, ScoreType slot, Modifier val, String desc) {
        this(name, slot, val);
        this.description = desc;
    }

    public Feature(String name, StatType slot, Modifier val, String desc) {
        this(name, slot, val);
        this.description = desc;
    }

    public Feature(String name, Proficiency proficiency, String desc) {
        this(name, proficiency);
        this.description = desc;
    }

    public Feature(String name, Attack attack, String desc) {
        this(name, attack);
        this.description = desc;
    }

    public Feature(String name, List<Feature> features, String desc) {
        this(name, features);
        this.description = desc;
    }

    public Feature(String name, List<Feature> features, boolean choice, String desc) {
        this(name, features, choice);
        this.description = desc;
    }

    public String getName() {
        return name;
    }

    public FeatureType getType() {
        return type;
    }

    public HashMap<ScoreType, Modifier> getScoreMod() {
        return scoreMod;
    }

    public HashMap<StatType, Modifier> getStatMod() {
        return statMod;
    }

    public Proficiency getProficiency() {
        return proficiency;
    }

    public Attack getAttack() {
        return attack;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public int getChoiceCount() {
        return choiceCount;
    }

    public ArrayList<Feature> getChosenFeatures() {
        ArrayList<Feature> result = new ArrayList<>();
        if (choice) {
            for (int i : choices) {
                result.add(features.get(i));
            }
        }
        return result;
    }

    public boolean isChoice() {
        return choice;
    }

    public List<Integer> getChoices() {
        return choices;
    }

    public String getDescription() {
        return description;
    }



    //REQUIRES: choiceIndex is within range for features, this is of type MULTI, choice is true, choices.size() less
    //          than choiceCount
    //MODIFIES: this
    //EFFECTS: chooses a feature from the available choices, removing oldest choice if not enough space
    public void chooseFeature(int choiceIndex) {
        if (type == FeatureType.MULTI
                && choice
                && choiceIndex >= 0
                && choiceIndex < features.size()) {
            if (choices.size() < choiceCount) {
                choices.add(choiceIndex);
            } else {
                choices.add(choiceIndex);
                choices.remove(0);
            }
        }
    }



    //EFFECTS: compiles all reachable score features into one big list of hashmaps
    public List<HashMap<ScoreType, Modifier>> getAllScoreModifiers() {
        List<Feature> scoreFeatures = getAllReachableFeaturesOfType(List.of(this), FeatureType.SCORE);
        List<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Feature f : scoreFeatures) {
            result.add(f.getScoreMod());
        }
        return result;
    }

    //EFFECTS: compiles all reachable proficiency features into one big list of hashmaps
    public List<HashMap<ScoreType, Modifier>> getAllProficiencyModifiers(BigDecimal profBonus) {
        List<Feature> profFeatures = getAllReachableFeaturesOfType(List.of(this), FeatureType.PROFICIENCY);
        List<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Feature f : profFeatures) {
            result.add(f.getProficiency().generateScoreMap(profBonus));
        }
        return result;
    }

    //EFFECTS: compiles all reachable stat features into one big list of hashmaps
    public List<HashMap<StatType, Modifier>> getAllStatModifiers() {
        List<Feature> scoreFeatures = getAllReachableFeaturesOfType(List.of(this), FeatureType.STAT);
        List<HashMap<StatType, Modifier>> result = new ArrayList<>();
        for (Feature f : scoreFeatures) {
            result.add(f.getStatMod());
        }
        return result;
    }

    //EFFECTS: searches through an array and returns all features that match a certain type, excluding branches
    public static List<Feature> getAllSurfaceFeaturesOfType(List<Feature> features, FeatureType type) {
        List<Feature> result = new ArrayList<>();
        for (Feature f : features) {
            if (f.getType().equals(type)) {
                result.add(f);
            }
        }
        return result;
    }

    //EFFECTS: searches through a list and returns all features that match a certain type, including all branches
    public static List<Feature> getAllFeaturesOfType(List<Feature> features, FeatureType type) {
        List<Feature> result = new ArrayList<>();
        for (Feature f : features) {
            if (f.getType().equals(type)) {
                result.add(f);
            } else if (f.getType().equals(FeatureType.MULTI)) {
                result.addAll(getAllFeaturesOfType(f.getFeatures(), type));
            }
        }
        return result;
    }

    //EFFECTS: searches through a list and returns all features that match a certain type, including only the branches
    //         that can be accessed
    public static List<Feature> getAllReachableFeaturesOfType(List<Feature> features, FeatureType type) {
        List<Feature> result = new ArrayList<>();
        for (Feature f : features) {
            if (f.getType().equals(type)) {
                result.add(f);
            } else if (f.getType().equals(FeatureType.MULTI)) {
                if (f.isChoice()) {
                    for (int choice : f.getChoices()) {
                        result.addAll(getAllReachableFeaturesOfType(List.of(f.getFeatures().get(choice)), type));
                    }
                } else {
                    result.addAll(getAllReachableFeaturesOfType(f.getFeatures(), type));
                }
            }
        }
        return result;
    }

    //EFFECTS: searches through a list and returns all features that offer choices between multiple other features
    public static List<Feature> getAllSurfaceFeaturesWithChoice(List<Feature> features) {
        List<Feature> result = new ArrayList<>();
        for (Feature f : features) {
            if (f.getType().equals(FeatureType.MULTI)) {
                if (f.isChoice()) {
                    result.add(f);
                } else {
                    result.addAll(getAllSurfaceFeaturesWithChoice(f.getFeatures()));
                }
            }
        }
        return result;
    }

    //EFFECTS: searches through a list and returns all features that offer choices between multiple other features
    public static List<Feature> getAllFeaturesWithChoice(List<Feature> features) {
        List<Feature> result = new ArrayList<>();
        for (Feature f : features) {
            if (f.getType().equals(FeatureType.MULTI)) {
                if (f.isChoice()) {
                    result.add(f);
                    result.addAll(f.getFeatures());
                } else {
                    result.addAll(getAllSurfaceFeaturesWithChoice(f.getFeatures()));
                }
            }
        }
        return result;
    }

    //EFFECTS: searches through a list and returns all features that offer choices between multiple other features
    public static List<Feature> getAllReachableFeaturesWithChoice(List<Feature> features) {
        List<Feature> result = new ArrayList<>();
        for (Feature f : features) {
            if (f.getType().equals(FeatureType.MULTI)) {
                if (f.isChoice()) {
                    result.add(f);
                    result.addAll(getAllReachableFeaturesWithChoice(f.getChosenFeatures()));
                } else {
                    result.addAll(getAllSurfaceFeaturesWithChoice(f.getFeatures()));
                }
            }
        }
        return result;
    }


    //EFFECTS: returns true if this has same values as other
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Feature other = (Feature) o;
        boolean nameEqual = (this.getName().equals(other.getName()));
        boolean typeEqual = (this.getType().equals(other.getType()));
        boolean containerEqual = (this.containersEqual(other));
        boolean isChoiceEqual = (this.isChoice() == other.isChoice());
        boolean choiceCountEqual = (this.getChoiceCount() == other.getChoiceCount());
        boolean choicesEqual = (this.getChosenFeatures().equals(other.getChosenFeatures()));

        return (nameEqual && typeEqual && containerEqual && isChoiceEqual && choiceCountEqual && choicesEqual);
    }

    //EFFECTS: helper for equals(), true if both features share the same values according to their type (since off-type
    //         fields are ignored
    private boolean containersEqual(Feature other) {
        boolean result = false;
        switch (this.getType()) {
            case SCORE:
                result = (this.getScoreMod().equals(other.getScoreMod()));
                break;
            case STAT:
                result = (this.getStatMod().equals(other.getStatMod()));
                break;
            case PROFICIENCY:
                result = (this.getProficiency().equals(other.getProficiency()));
                break;
            case ATTACK:
                result = (this.getAttack().equals(other.getAttack()));
                break;
            case MULTI:
                result = (this.getFeatures().equals(other.getFeatures()));
                break;
            case ETC:
                result = (this.getDescription().equals(other.getDescription()));
                break;
        }
        return result;
    }
}
