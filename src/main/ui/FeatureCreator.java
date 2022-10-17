package ui;

import enums.*;
import model.Feature;
import model.Modifier;
import model.Proficiency;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FeatureCreator extends AcceptsInput {
    /// A helper for main that handles creating features.

    //EFFECTS: creates a new feature from input (otherFeatures is for access for MULTI feature)
    public Feature createFeature(List<Feature> otherFeatures) {
        return createFeatureStart(otherFeatures);
    }

    //EFFECTS: starts the process to create a new feature
    private Feature createFeatureStart(List<Feature> otherFeatures) {
        String name = input.scanName("Feature");
        if (name == null) {
            return null;
        }
        FeatureType type = input.scanFeatureType();
        if (type == null) {
            return null;
        }
        return createFeatureContent(name, type, otherFeatures);
    }

    //EFFECTS: handles the feature type branching of the feature creation process,
    //         taking name and type from createFeatureStart
    private Feature createFeatureContent(String name, FeatureType type, List<Feature> otherFeatures) {
        switch (type) {
            case SCORE:
                return createScoreFeature(name);
            case STAT:
                return createStatFeature(name);
            case PROFICIENCY:
                return createProficiencyFeature(name);
            case ATTACK:
                System.out.println("Sorry, that functionality hasn't been added yet!");
                return null;
            case MULTI:
                return createMultiFeature(name, otherFeatures);
            case ETC:
                return createEtcFeature(name);
        }
        return null;
    }

    //EFFECTS: creates a new feature of type ETC
    private Feature createEtcFeature(String name) {
        String description = input.scanWithExit("Please enter a description of your feature here");
        if (description == null) {
            return null;
        }
        return new Feature(name, description);
    }

    //EFFECTS: creates a new feature of type SCORE
    private Feature createScoreFeature(String name) {
        ScoreType scoreType = input.scanScoreType();
        if (scoreType == null) {
            return null;
        }
        Modifier modifier = input.scanModifier();
        if (modifier == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add a description?")) {
            String description = input.scanWithExit("Please enter a description of your feature here");
            if (description == null) {
                return null;
            }
            return new Feature(name, scoreType, modifier, description);
        }
        return new Feature(name, scoreType, modifier);
    }
    
    //EFFECTS: creates a new feature of type STAT
    private Feature createStatFeature(String name) {
        StatType statType = input.scanStatType();
        if (statType == null) {
            return null;
        }
        Modifier modifier = input.scanModifier();
        if (modifier == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add a description?")) {
            String description = input.scanWithExit("Please enter a description of your feature here");
            if (description == null) {
                return null;
            }
            return new Feature(name, statType, modifier, description);
        }
        return new Feature(name, statType, modifier);
    }

    //EFFECTS: creates a new feature of type PROFICIENCY
    private Feature createProficiencyFeature(String name) {
        Proficiency proficiency = input.scanProficiency();
        if (proficiency == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add a description?")) {
            String description = input.scanWithExit("Please enter a description of your feature here");
            if (description == null) {
                return null;
            }
            return new Feature(name, proficiency, description);
        }
        return new Feature(name, proficiency);
    }

    //EFFECTS: creates a new feature of type MULTI
    private Feature createMultiFeature(String name, List<Feature> otherFeatures) {
        if (otherFeatures.isEmpty()) {
            System.out.println("Sorry, this functionality isn't available yet. Make some other features first!");
            return null;
        } else {
            System.out.println("The features you can choose to add to this feature are:");
            for (int i = 0; i < otherFeatures.size(); i++) {
                System.out.println("[" + i + "] " + otherFeatures.get(i).getName());
            }
            while (true) {
                String listInput = input.scanWithExit("Please select a number of features to add by their index");
                if (listInput == null) {
                    return null;
                }
                if (listInput.trim().matches("(\\d+\\s*)*(\\d+)")) {
                    return createMultiFeatureAddFeatures(name, otherFeatures, listInput);
                }
            }
        }
    }

    //EFFECTS: continuation of createMultiFeature handling feature adding and choices
    private Feature createMultiFeatureAddFeatures(String name, List<Feature> otherFeatures, String listInput) {
        ArrayList<Feature> features = new ArrayList<>();
        Scanner listInputScanner = new Scanner(listInput);
        boolean choice = false;
        int choiceCount = 0;
        while (listInputScanner.hasNextInt()) {
            int scannedInt = listInputScanner.nextInt();
            if (scannedInt >= 0 && scannedInt < otherFeatures.size()) {
                features.add(otherFeatures.get(scannedInt));
            }
        }
        if (input.scanYesNo("Would you like the user to choose between feature options?")) {
            choice = true;
            String choices = input.scanWithExit("Please select the amount of features one can choose");
            if (choices == null) {
                return null;
            }
            if (choices.trim().matches("\\d+")) {
                choiceCount = Integer.parseInt(choices);
            }
        }
        return createMultiFeatureFinalize(name, features, choice, choiceCount);
    }

    //EFFECTS: final continuation of createMultiFeature handling description
    private Feature createMultiFeatureFinalize(String name, List<Feature> features, boolean choice, int choiceCount) {
        if (input.scanYesNo("Would you like to add a description?")) {
            String description = input.scanWithExit("Please enter a description of your feature here");
            if (description == null) {
                return null;
            }
            return new Feature(name, features, choice, choiceCount, description);
        }
        return new Feature(name, features, choice, choiceCount);
    }
}
