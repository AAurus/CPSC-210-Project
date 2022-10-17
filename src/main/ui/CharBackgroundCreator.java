package ui;

import model.*;

import java.util.List;

public class CharBackgroundCreator extends AcceptsInput {
    /// A helper for main that handles creating CharBackgrounds.
    
    //EFFECTS: creates a new CharBackground from user input
    public CharBackground createBackground(List<Feature> otherFeatures, List<InventoryItem> otherItems) {
        String name = input.scanName("Background");
        if (name == null) {
            return null;
        }
        CharBackground baseBackground = new CharBackground(name);
        if (input.scanYesNo("Would you like to add extra information about this background?")) {
            return createCharBackgroundExtended(baseBackground, otherFeatures, otherItems);
        }
        return baseBackground;
    }

    //EFFECTS: adds extra information to baseBackground from user input, then returns it
    private CharBackground createCharBackgroundExtended(CharBackground baseBackground, List<Feature> otherFeatures,
                                                        List<InventoryItem> otherItems) {
        CharBackground result = addBackgroundProficiencies(baseBackground);
        result = addBackgroundLanguages(result);
        result = addBackgroundEquipment(result, otherItems);
        result = addBackgroundFeatures(result, otherFeatures);
        result = addBackgroundDescription(result);
        return result;
    }

    //EFFECTS: adds proficiencies to baseBackground if the user wishes, then returns it
    private CharBackground addBackgroundProficiencies(CharBackground baseBackground) {
        if (baseBackground == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add proficiencies to this bacgkround?")) {
            while (true) {
                Proficiency proficiency = input.scanProficiency();
                if (proficiency == null) {
                    return null;
                }
                baseBackground.addProficiency(proficiency);
                if (!input.scanYesNo("Would you like to add more proficiencies?")) {
                    break;
                }
            }
        }
        return baseBackground;
    }

    //EFFECTS: adds languages to baseBackground if the user wishes, then returns it
    private CharBackground addBackgroundLanguages(CharBackground baseBackground) {
        if (baseBackground == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add proficiencies to this bacgkround?")) {
            while (true) {
                String language = input.scanWithExit("Please enter the name of the language you would like to add");
                if (language == null) {
                    return null;
                }
                baseBackground.addLanguage(language);
                if (!input.scanYesNo("Would you like to add more languages?")) {
                    break;
                }
            }
        }
        return baseBackground;
    }

    //EFFECTS: adds starting equipment to baseBackground if the user wishes, then returns it
    private CharBackground addBackgroundEquipment(CharBackground baseBackground, List<InventoryItem> otherItems) {
        if (baseBackground == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add starting equipment to this background?")) {
            if (otherItems.isEmpty()) {
                System.out.println("Sorry, this functionality isn't available yet. Make some items first!");
            } else {
                while (true) {
                    baseBackground = addOneItemToCharBackground(baseBackground, otherItems);
                    if (!input.scanYesNo("Would you like to add more equipment?")) {
                        break;
                    }
                }
            }
        }
        return baseBackground;
    }

    //REQUIRES: otherItems is not empty
    //EFFECTS: helper for addFeaturesToBackground, adds a single inventory item
    private CharBackground addOneItemToCharBackground(CharBackground baseBackground, List<InventoryItem> otherItems) {
        if (baseBackground == null) {
            return null;
        }
        System.out.println("The items you can choose to add to this background are:");
        for (int i = 0; i < otherItems.size(); i++) {
            System.out.println("[" + i + "] " + otherItems.get(i).getName());
        }
        while (true) {
            int index = input.scanPositiveIntWithExit("Please enter the index of your selected item");
            if (index < 0) {
                return null;
            } else if (index < otherItems.size()) {
                baseBackground.addEquipment(otherItems.get(index));
                return baseBackground;
            }
        }
    }

    //EFFECTS: adds features to baseBackground if the user wishes, then returns it
    private CharBackground addBackgroundFeatures(CharBackground baseBackground, List<Feature> otherFeatures) {
        if (baseBackground == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add features to this bacgkround?")) {
            if (otherFeatures.isEmpty()) {
                System.out.println("Sorry, this functionality isn't available yet. Make some features first!");
            } else {
                while (true) {
                    baseBackground = addOneFeatureToCharBackground(baseBackground, otherFeatures);
                    if (!input.scanYesNo("Would you like to add more features?")) {
                        break;
                    }
                }
            }
        }
        return baseBackground;
    }

    //REQUIRES: otherFeatures is not empty
    //EFFECTS: helper for addFeaturesToBackground, adds a single feature
    private CharBackground addOneFeatureToCharBackground(CharBackground baseBackground, List<Feature> otherFeatures) {
        if (baseBackground == null) {
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
                baseBackground.addFeature(otherFeatures.get(index));
                return baseBackground;
            }
        }
    }

    //EFFECTS: adds description to item if user wishes, and returns it
    private CharBackground addBackgroundDescription(CharBackground baseBackground) {
        if (baseBackground == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add a description to this background?")) {
            String description = input.scanWithExit("Please enter a description of your background here");
            if (description == null) {
                return null;
            }
            baseBackground.setDescription(description);
        }
        return baseBackground;
    }
}
