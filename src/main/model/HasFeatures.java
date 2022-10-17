package model;

import java.util.*;

import enums.FeatureType;
import enums.ScoreType;

public abstract class HasFeatures {
    protected ArrayList<Feature> features;

    //EFFECTS: returns list of hashmaps of all features that provide score modifiers
    public ArrayList<HashMap<ScoreType, Modifier>> getAllFeatureScoreMods() {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Feature f : Feature.getAllReachableFeaturesOfType(features, FeatureType.SCORE)) {
            result.add(f.getScoreMod());
        }
        return result;
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    //EFFECTS: removes the first feature that matches
    public void removeFeature(String featureName) {
        for (Feature f : features) {
            if (f.getName().equals(featureName)) {
                features.remove(f);
                break;
            }
        }
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
