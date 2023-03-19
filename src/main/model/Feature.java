package model;

import enums.FeatureType;
import enums.ScoreType;
import enums.StatType;
import utility.Utility;

import java.math.BigDecimal;
import java.util.*;

public class Feature {
    ///A representation of a D&D Character Feature, that may produce various effects.
    public static final int HASH_MULTIPLIER = 73;
    public static final int DEFAULT_MAXIMUM_CHOICES = 1;

    private final String name;
    private final FeatureType type;
    private final HashMap<ScoreType, Modifier> scoreMod;
    private final HashMap<StatType, Modifier> statMod;
    private final Proficiency proficiency;
    private final List<Feature> features;
    private final boolean choice;
    private final int choiceCount;
    private ArrayList<Integer> choices;
    private String description;

    public Feature(Feature feature) {
        this.name = feature.getName();
        this.type = feature.getType();
        this.scoreMod = cloneScoreMod(feature.getScoreMod());
        this.statMod = cloneStatMod(feature.getStatMod());
        this.proficiency = feature.getProficiency();
        if (feature.getFeatures() != null) {
            this.features = new ArrayList<>();
            for (Feature f : feature.getFeatures()) {
                this.features.add(new Feature(f));
            }
        } else {
            this.features = null;
        }
        this.choice = feature.isChoice();
        this.choiceCount = feature.getChoiceCount();
        if (feature.isChoice()) {
            this.choices = new ArrayList();
        } else {
            this.choices = null;
        }
        this.description = feature.getDescription();

        Utility.logEvent("Feature duplicated from " + feature.getName());
    }

    public Feature(String name, ScoreType slot, Modifier mod) {
        this.name = name;
        this.type = FeatureType.SCORE;
        this.scoreMod = new HashMap<>();
        scoreMod.put(slot, mod);
        this.statMod = null;
        this.proficiency = null;
        this.features = null;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = "";

        Utility.logEvent("New Score feature created: " + name);
    }

    public Feature(String name, StatType slot, Modifier mod) {
        this.name = name;
        this.type = FeatureType.STAT;
        this.scoreMod = null;
        this.statMod = new HashMap<>();
        statMod.put(slot, mod);
        this.proficiency = null;
        this.features = null;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = "";

        Utility.logEvent("New Stat feature created: " + name);
    }

    public Feature(String name, Proficiency proficiency) {
        this.name = name;
        this.type = FeatureType.PROFICIENCY;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = proficiency;
        this.features = null;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = "";

        Utility.logEvent("New Proficiency feature created: " + name);
    }

    public Feature(String name, List<Feature> features) {
        this.name = name;
        this.type = FeatureType.MULTI;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.features = features;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = "";

        Utility.logEvent("New Multi-Feature feature created: " + name);
    }

    public Feature(String name, List<Feature> features, boolean choice) {
        this.name = name;
        this.type = FeatureType.MULTI;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.features = features;
        this.choice = choice;
        this.choiceCount = DEFAULT_MAXIMUM_CHOICES;
        this.choices = new ArrayList<>();
        this.description = "";

        Utility.logEvent("New Single-Choice feature created: " + name);
    }

    public Feature(String name, List<Feature> features, boolean choice, int choiceCount) {
        this.name = name;
        this.type = FeatureType.MULTI;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.features = features;
        this.choice = choice;
        this.choiceCount = choiceCount;
        this.choices = new ArrayList<>();
        this.description = "";

        Utility.logEvent("New " + choiceCount + "-Choice feature created: " + name);
    }

    public Feature(String name, List<Feature> features, boolean choice, int choiceCount, ArrayList<Integer> choices) {
        this.name = name;
        this.type = FeatureType.MULTI;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.features = features;
        this.choice = choice;
        this.choiceCount = choiceCount;
        this.choices = choices;
        this.description = "";

        Utility.logEvent("New " + choiceCount + "-Choice feature created: " + name + " with pre-loaded choices");
    }

    public Feature(String name, String desc) {
        this.name = name;
        this.type = FeatureType.ETC;
        this.scoreMod = null;
        this.statMod = null;
        this.proficiency = null;
        this.features = null;
        this.choice = false;
        this.choiceCount = 0;
        this.choices = null;
        this.description = desc;

        Utility.logEvent("New description-only feature created: " + name);
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

    public Feature(String name, List<Feature> features, String desc) {
        this(name, features);
        this.description = desc;
    }

    public Feature(String name, List<Feature> features, boolean choice, String desc) {
        this(name, features, choice);
        this.description = desc;
    }

    public Feature(String name, List<Feature> features, boolean choice, int choiceCount, String desc) {
        this(name, features, choice, choiceCount);
        this.description = desc;
    }

    public Feature(String name, List<Feature> features, boolean choice, int choiceCount,
                   ArrayList<Integer> choices, String desc) {
        this(name, features, choice, choiceCount, choices);
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

            Utility.logEvent("Feature " + features.get(choiceIndex).getName() + " chosen from feature "
                             + name);
        }
    }



    //EFFECTS: deep copies a HashMap<ScoreType, Modifier> and returns it
    private HashMap<ScoreType, Modifier> cloneScoreMod(HashMap<ScoreType, Modifier> source) {
        if (source != null) {
            HashMap<ScoreType, Modifier> result = new HashMap<>();
            for (ScoreType s : source.keySet()) {
                result.put(s, source.get(s).clone());
            }
            return result;
        } else {
            return null;
        }
    }

    //EFFECTS: deep copies a HashMap<ScoreType, Modifier> and returns it
    private HashMap<StatType, Modifier> cloneStatMod(HashMap<StatType, Modifier> source) {
        if (source != null) {
            HashMap<StatType, Modifier> result = new HashMap<>();
            for (StatType s : source.keySet()) {
                result.put(s, source.get(s).clone());
            }
            return result;
        } else {
            return null;
        }
    }


    //EFFECTS: compiles all reachable score features into one big list of hashmaps
    public List<HashMap<ScoreType, Modifier>> getAllScoreModifiers() {
        List<Feature> scoreFeatures = getAllReachableFeaturesOfType(Utility.listOf(this), FeatureType.SCORE);
        List<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Feature f : scoreFeatures) {
            result.add(f.getScoreMod());
        }
        return result;
    }

    //EFFECTS: compiles all reachable proficiency features into one big list of hashmaps
    public List<HashMap<ScoreType, Modifier>> getAllProficiencyModifiers(BigDecimal profBonus) {
        List<Feature> profFeatures = getAllReachableFeaturesOfType(Utility.listOf(this), FeatureType.PROFICIENCY);
        List<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Feature f : profFeatures) {
            result.add(f.getProficiency().generateScoreMap(profBonus));
        }
        return result;
    }

    //EFFECTS: compiles all reachable stat features into one big list of hashmaps
    public List<HashMap<StatType, Modifier>> getAllStatModifiers() {
        List<Feature> statFeatures = getAllReachableFeaturesOfType(Utility.listOf(this), FeatureType.STAT);
        List<HashMap<StatType, Modifier>> result = new ArrayList<>();
        for (Feature f : statFeatures) {
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
                        result.addAll(getAllReachableFeaturesOfType(Utility.listOf(f.getFeatures().get(choice)),
                                                                    type));
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
                    result.addAll(getAllFeaturesWithChoice(f.getFeatures()));
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
                    result.addAll(getAllReachableFeaturesWithChoice(f.getFeatures()));
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

        return (nameEqual && typeEqual && containerEqual && isChoiceEqual && choiceCountEqual);
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
            case MULTI:
                result = (this.getFeatures().equals(other.getFeatures()));
                break;
            case ETC:
                result = (this.getDescription().equals(other.getDescription()));
                break;
        }
        return result;
    }

    @Override
    public int hashCode() {
        ArrayList<Integer> hashComponents = new ArrayList<>();
        hashComponents.add(name.hashCode());
        hashComponents.add(type.hashCode());
        hashComponents.add(description.hashCode());
        hashComponents.add(Utility.hashIfAble(scoreMod));
        hashComponents.add(Utility.hashIfAble(statMod));
        hashComponents.add(Utility.hashIfAble(proficiency));
        hashComponents.add(Utility.hashIfAble(features));
        hashComponents.add(Boolean.hashCode(choice));
        hashComponents.add(choiceCount);
        hashComponents.add(Utility.hashIfAble(choices));
        return Utility.hashCodeHelper(hashComponents, HASH_MULTIPLIER);
    }
}
