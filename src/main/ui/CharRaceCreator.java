package ui;

import enums.ScoreType;
import model.*;

import java.util.List;

public class CharRaceCreator extends AcceptsInput {
    /// A helper for main that handles creating CharRaces.

    //EFFECTS: creates a new CharRace from user input
    public CharRace createRace(List<Feature> otherFeatures, List<CharRace> otherRaces) {
        String name = input.scanName("Race");
        if (name == null) {
            return null;
        }
        CharRace baseRace = new CharRace(name);
        if (input.scanYesNo("Would you like to add extra information about this race?")) {
            return createCharRaceExtended(baseRace, otherFeatures, otherRaces);
        }
        return baseRace;
    }

    //EFFECTS: adds extra information to baseRace from user input, then returns it
    private CharRace createCharRaceExtended(CharRace baseRace, List<Feature> otherFeatures, List<CharRace> otherRaces) {
        CharRace result = addRaceScores(baseRace);
        result = addRaceProficiencies(result);
        result = addRaceLanguages(result);
        result = addRaceFeatures(result, otherFeatures);
        result = addRaceSubRaces(result, otherRaces);
        result = addRaceDescription(result);
        return result;
    }

    //EFFECTS: adds score modifiers to baseRace if the user wishes, then returns it
    private CharRace addRaceScores(CharRace baseRace) {
        if (baseRace == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add score modifiers to this race?")) {
            while (true) {
                ScoreType scoreType = input.scanScoreType();
                if (scoreType == null) {
                    return null;
                }
                Modifier modifier = input.scanModifier();
                if (modifier == null) {
                    return null;
                }
                baseRace.setScoreMod(scoreType, modifier);
                if (!input.scanYesNo("Would you like to add more score modifiers?")) {
                    break;
                }
            }
        }
        return baseRace;
    }

    //EFFECTS: adds proficiencies to baseRace if the user wishes, then returns it
    private CharRace addRaceProficiencies(CharRace baseRace) {
        if (baseRace == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add proficiencies to this race?")) {
            while (true) {
                Proficiency proficiency = input.scanProficiency();
                if (proficiency == null) {
                    return null;
                }
                baseRace.addProficiency(proficiency);
                if (!input.scanYesNo("Would you like to add more proficiencies?")) {
                    break;
                }
            }
        }
        return baseRace;
    }

    //EFFECTS: adds languages to baseRace if the user wishes, then returns it
    private CharRace addRaceLanguages(CharRace baseRace) {
        if (baseRace == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add proficiencies to this race?")) {
            while (true) {
                String language = input.scanWithExit("Please enter the name of the language you would like to add");
                if (language == null) {
                    return null;
                }
                baseRace.addLanguage(language);
                if (!input.scanYesNo("Would you like to add more languages?")) {
                    break;
                }
            }
        }
        return baseRace;
    }

    //EFFECTS: adds features to baseRace if the user wishes, then returns it
    private CharRace addRaceFeatures(CharRace baseRace, List<Feature> otherFeatures) {
        if (baseRace == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add features to this race?")) {
            if (otherFeatures.isEmpty()) {
                System.out.println("Sorry, this functionality isn't available yet. Make some features first!");
            } else {
                while (true) {
                    baseRace = addOneFeatureToCharRace(baseRace, otherFeatures);
                    if (!input.scanYesNo("Would you like to add more features?")) {
                        break;
                    }
                }
            }
        }
        return baseRace;
    }

    //REQUIRES: otherFeatures is not empty
    //EFFECTS: helper for addFeaturesToRace, adds a single feature
    private CharRace addOneFeatureToCharRace(CharRace baseRace, List<Feature> otherFeatures) {
        if (baseRace == null) {
            return null;
        }
        System.out.println("The features you can choose to add to this feature are:");
        for (int i = 0; i < otherFeatures.size(); i++) {
            System.out.println("[" + i + "] " + otherFeatures.get(i).getName());
        }
        while (true) {
            int index = input.scanPositiveIntWithExit("Please enter the index of your selected feature");
            if (index < 0) {
                return null;
            } else if (index < otherFeatures.size()) {
                baseRace.addFeature(otherFeatures.get(index));
                return baseRace;
            }
        }
    }

    //EFFECTS: adds subraces to baseRace if the user wishes, then returns it
    private CharRace addRaceSubRaces(CharRace baseRace, List<CharRace> otherRaces) {
        if (baseRace == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add subraces to this race?")) {
            if (otherRaces.isEmpty()) {
                System.out.println("Sorry, this functionality isn't available yet. Make some other races first!");
            } else {
                while (true) {
                    baseRace = addOneSubRaceToCharRace(baseRace, otherRaces);
                    if (!input.scanYesNo("Would you like to add more subraces?")) {
                        break;
                    }
                }
            }
        }
        return baseRace;
    }

    //REQUIRES: otherRaces is not empty
    //EFFECTS: helper for addFeaturesToRace, adds a single subrace
    private CharRace addOneSubRaceToCharRace(CharRace baseRace, List<CharRace> otherRaces) {
        if (baseRace == null) {
            return null;
        }
        System.out.println("The subraces you can choose to add to this race are:");
        for (int i = 0; i < otherRaces.size(); i++) {
            System.out.println("[" + i + "] " + otherRaces.get(i).getName());
        }
        while (true) {
            int index = input.scanPositiveIntWithExit("Please enter the index of your selected subrace");
            if (index < 0) {
                return null;
            } else if (index < otherRaces.size()) {
                baseRace.addSubRace(otherRaces.get(index));
                return baseRace;
            }
        }
    }

    //EFFECTS: adds description to item if user wishes, and returns it
    private CharRace addRaceDescription(CharRace baseRace) {
        if (baseRace == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add a description to this race?")) {
            String description = input.scanWithExit("Please enter a description of your race here");
            if (description == null) {
                return null;
            }
            baseRace.setDescription(description);
        }
        return baseRace;
    }
}
