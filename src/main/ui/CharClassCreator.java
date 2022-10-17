package ui;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class CharClassCreator extends AcceptsInput {
    /// A helper for main that handles creating CharClasses.

    //EFFECTS: creates a new CharRace from user input
    public CharClass createClass(List<Feature> otherFeatures, List<CharClass> otherClasses,
                                 List<InventoryItem> otherItems) {
        String name = input.scanName("Race");
        if (name == null) {
            return null;
        }
        while (true) {
            String classType = input.scanWithExit("Is this class intended to be a subclass, or a main class?"
                    + "(type 'subclass' for subclass, 'main' for main class");
            if (classType == null) {
                return null;
            } else if (classType.trim().toLowerCase().equals("subclass")) {
                return createSubClass(name, otherFeatures);
            } else if (classType.trim().toLowerCase().equals("main")) {
                return createMainClass(name, otherFeatures, otherClasses, otherItems);
            }
        }
    }

    private CharClass createSubClass(String name, List<Feature> otherFeatures) {
        System.out.println("Please add some Level 1 features.");
        CharClass result = addLevelOneFeatures(new CharClass(name, new ArrayList<>()), otherFeatures);
        if (input.scanYesNo("Would you like to add extra information about this class?")) {
            return createSubClassExtended(result, otherFeatures);
        }
        return result;
    }

    private CharClass createMainClass(String name, List<Feature> otherFeatures, List<CharClass> otherClasses,
                                      List<InventoryItem> otherItems) {
        int hitDie = input.scanPositiveIntWithExit("Please enter the number of sides for this class' hit die:");
        if (hitDie < 0) {
            return null;
        }
        int subClassLevel = input.scanPositiveIntWithExit("Please enter the level threshold needed to select a"
                + "subclass for this class:");
        if (subClassLevel < 0) {
            return null;
        }
        CharClass result = new CharClass(name, new ArrayList<>(),
                            new ArrayList<>(), new ArrayList<>(),
                            new ArrayList<>(), new ArrayList<>(),
                            subClassLevel, hitDie);
        result = addLevelOneFeatures(result, otherFeatures);
        if (input.scanYesNo("Would you like to add extra information about this class?")) {
            return createMainClassExtended(result, otherFeatures, otherClasses, otherItems);
        }
        return result;
    }

    //EFFECTS: adds more information to a main class, then returns it
    private CharClass createMainClassExtended(CharClass baseClass, List<Feature> otherFeatures,
                                              List<CharClass> otherClasses, List<InventoryItem> otherItems) {
        CharClass result = addClassBaseProficiencies(baseClass);
        result = addClassMultiClassProficiencies(result);
        result = addClassFeatures(result, otherFeatures);
        result = addClassSubClasses(result, otherClasses);
        result = addClassEquipment(result, otherItems);
        result = addClassDescription(result);
        return result;
    }


    private CharClass createSubClassExtended(CharClass baseClass, List<Feature> otherFeatures) {
        CharClass result = addClassFeatures(baseClass, otherFeatures);
        result = addClassDescription(result);
        return result;
    }

    //EFFECTS: adds proficiencies to baseClass if the user wishes, then returns it
    private CharClass addClassBaseProficiencies(CharClass baseClass) {
        if (baseClass == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add base proficiencies to this class?")) {
            while (true) {
                Proficiency proficiency = input.scanProficiency();
                if (proficiency == null) {
                    return null;
                }
                baseClass.addBaseProficiency(proficiency);
                if (!input.scanYesNo("Would you like to add more proficiencies?")) {
                    break;
                }
            }
        }
        return baseClass;
    }

    //EFFECTS: adds proficiencies to baseClass if the user wishes, then returns it
    private CharClass addClassMultiClassProficiencies(CharClass baseClass) {
        if (baseClass == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add multiclass proficiencies to this class?")) {
            while (true) {
                Proficiency proficiency = input.scanProficiency();
                if (proficiency == null) {
                    return null;
                }
                baseClass.addMultiClassProficiency(proficiency);
                if (!input.scanYesNo("Would you like to add more proficiencies?")) {
                    break;
                }
            }
        }
        return baseClass;
    }

    //EFFECTS: adds features to baseClass if the user wishes, then returns it
    private CharClass addClassFeatures(CharClass baseClass, List<Feature> otherFeatures) {
        if (baseClass == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add features to this class?")) {
            if (otherFeatures.isEmpty()) {
                System.out.println("Sorry, this functionality isn't available yet. Make some features first!");
            } else {
                while (true) {
                    baseClass = addOneFeatureToCharClass(baseClass, otherFeatures);
                    if (!input.scanYesNo("Would you like to add more features?")) {
                        break;
                    }
                }
            }
        }
        return baseClass;
    }

    //REQUIRES: otherFeatures is not empty
    //EFFECTS: helper for addFeaturesToClass, adds a single feature
    private CharClass addOneFeatureToCharClass(CharClass baseClass, List<Feature> otherFeatures) {
        if (baseClass == null) {
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
                while (true) {
                    int level = input.scanPositiveIntWithExit("Please enter the level this feature should be"
                            + "placed in");
                    if (index < 0) {
                        return null;
                    }
                    baseClass.addFeature(otherFeatures.get(index), level);
                    return baseClass;
                }
            }
        }
    }

    //EFFECTS: adds level 1 features to baseClass, no choice; then returns it
    private CharClass addLevelOneFeatures(CharClass baseClass, List<Feature> otherFeatures) {
        if (baseClass == null) {
            return null;
        }
        if (otherFeatures.isEmpty()) {
            System.out.println("Sorry, this functionality isn't available yet. Make some features first!");
            return null;
        } else {
            while (true) {
                baseClass = addOneLevelOneFeatureToCharClass(baseClass, otherFeatures);
                if (!input.scanYesNo("Would you like to add more Level 1 features?")) {
                    break;
                }
            }
        }
        return baseClass;
    }

    //REQUIRES: otherFeatures is not empty
    //EFFECTS: helper for addFeaturesToClass, adds a single feature
    private CharClass addOneLevelOneFeatureToCharClass(CharClass baseClass, List<Feature> otherFeatures) {
        if (baseClass == null) {
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
                baseClass.addFeature(otherFeatures.get(index));
                return baseClass;
            }
        }
    }

    //EFFECTS: adds subclasss to baseClass if the user wishes, then returns it
    private CharClass addClassSubClasses(CharClass baseClass, List<CharClass> otherClasses) {
        if (baseClass == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add subclasss to this class?")) {
            if (otherClasses.isEmpty()) {
                System.out.println("Sorry, this functionality isn't available yet. Make some other classs first!");
            } else {
                while (true) {
                    baseClass = addOneSubClassToCharClass(baseClass, otherClasses);
                    if (!input.scanYesNo("Would you like to add more subclasss?")) {
                        break;
                    }
                }
            }
        }
        return baseClass;
    }

    //REQUIRES: otherClasses is not empty
    //EFFECTS: helper for addFeaturesToClass, adds a single subclass
    private CharClass addOneSubClassToCharClass(CharClass baseClass, List<CharClass> otherClasses) {
        if (baseClass == null) {
            return null;
        }
        System.out.println("The classs you can choose to add to this feature are:");
        for (int i = 0; i < otherClasses.size(); i++) {
            System.out.println("[" + i + "] " + otherClasses.get(i).getName());
        }
        while (true) {
            int index = input.scanPositiveIntWithExit("Please enter the index of your selected feature");
            if (index < 0) {
                return null;
            } else if (index < otherClasses.size()) {
                baseClass.addSubClass(otherClasses.get(index));
                return baseClass;
            }
        }
    }

    //EFFECTS: adds starting equipment to baseClass if the user wishes, then returns it
    private CharClass addClassEquipment(CharClass baseClass, List<InventoryItem> otherItems) {
        if (baseClass == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add starting equipment to this class?")) {
            if (otherItems.isEmpty()) {
                System.out.println("Sorry, this functionality isn't available yet. Make some items first!");
            } else {
                while (true) {
                    baseClass = addOneItemToCharClass(baseClass, otherItems);
                    if (!input.scanYesNo("Would you like to add more equipment?")) {
                        break;
                    }
                }
            }
        }
        return baseClass;
    }

    //REQUIRES: otherItems is not empty
    //EFFECTS: helper for addClassEquipment, adds a single inventory item
    private CharClass addOneItemToCharClass(CharClass baseClass, List<InventoryItem> otherItems) {
        if (baseClass == null) {
            return null;
        }
        System.out.println("The items you can choose to add to this class are:");
        for (int i = 0; i < otherItems.size(); i++) {
            System.out.println("[" + i + "] " + otherItems.get(i).getName());
        }
        while (true) {
            int index = input.scanPositiveIntWithExit("Please enter the index of your selected item");
            if (index < 0) {
                return null;
            } else if (index < otherItems.size()) {
                baseClass.addEquipment(otherItems.get(index));
                return baseClass;
            }
        }
    }
    
    //EFFECTS: adds description to item if user wishes, and returns it
    private CharClass addClassDescription(CharClass baseClass) {
        if (baseClass == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add a description to this class?")) {
            String description = input.scanWithExit("Please enter a description of your class here");
            if (description == null) {
                return null;
            }
            baseClass.setDescription(description);
        }
        return baseClass;
    }
}
