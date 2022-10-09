package model;

import enums.FeatureType;
import enums.ScoreType;

import java.util.*;

public class Feature {
    ///A representation of a D&D Character Feature, that may produce various effects.
    private final String name;
    private final FeatureType type;
    private final ScoreType scoreSlot;
    private final Modifier scoreMod;
    private final Attack attack;
    private final ArrayList<Feature> features;
    private final String description;

    public Feature(String name, ScoreType slot, Modifier val) {
        this.name = name;
        this.type = FeatureType.SCORE;
        this.scoreSlot = slot;
        this.scoreMod = val;
        this.attack = null;
        this.features = null;
        this.description = "";
    }

    public Feature(String name, Attack attack) {
        this.name = name;
        this.type = FeatureType.ATTACK;
        this.scoreSlot = null;
        this.scoreMod = null;
        this.attack = attack;
        this.features = null;
        this.description = "";
    }

    public Feature(String name, ArrayList<Feature> features) {
        this.name = name;
        this.type = FeatureType.MULTI;
        this.scoreSlot = null;
        this.scoreMod = null;
        this.attack = null;
        this.features = features;
        this.description = "";
    }

    public Feature(String name, ScoreType slot, Modifier val, String desc) {
        this.name = name;
        this.type = FeatureType.SCORE;
        this.scoreSlot = slot;
        this.scoreMod = val;
        this.attack = null;
        this.features = null;
        this.description = desc;
    }

    public Feature(String name, Attack attack, String desc) {
        this.name = name;
        this.type = FeatureType.ATTACK;
        this.scoreSlot = null;
        this.scoreMod = null;
        this.attack = attack;
        this.features = null;
        this.description = desc;
    }

    public Feature(String name, ArrayList<Feature> features, String desc) {
        this.name = name;
        this.type = FeatureType.MULTI;
        this.scoreSlot = null;
        this.scoreMod = null;
        this.attack = null;
        this.features = features;
        this.description = desc;
    }

    public Feature(String name, String desc) {
        this.name = name;
        this.type = FeatureType.ETC;
        this.scoreSlot = null;
        this.scoreMod = null;
        this.attack = null;
        this.features = null;
        this.description = desc;
    }

    public String getName() {
        return name;
    }

    public FeatureType getType() {
        return type;
    }

    public ScoreType getScoreSlot() {
        return scoreSlot;
    }

    public Modifier getScoreMod() {
        return scoreMod;
    }

    public Attack getAttack() {
        return attack;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public String getDescription() {
        return description;
    }

    //EFFECTS: compiles all score features into one big list of hashmaps
    public ArrayList<HashMap<ScoreType, Modifier>> getAllModifiersWithSlots() {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        if (getType().equals(FeatureType.MULTI)) {
            for (Feature f : features) {
                result.addAll(f.getAllModifiersWithSlots());
            }
        } else if (getType().equals(FeatureType.SCORE)) {
            HashMap<ScoreType, Modifier> singleMod = new HashMap<>();
            singleMod.put(scoreSlot, scoreMod);
            result.add(singleMod);
        }
        return result;
    }

    //EFFECTS: searches through an array and returns all features that match a certain type
    public static ArrayList<Feature> allFeaturesOfType(ArrayList<Feature> features, FeatureType type) {
        ArrayList<Feature> result = new ArrayList<>();
        for (Feature f : features) {
            if (f.getType().equals(type)) {
                result.add(f);
            }
        }
        return result;
    }
}
