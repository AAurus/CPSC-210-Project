package ui;

import enums.FeatureType;
import enums.ScoreType;
import model.*;
import model.Character;

import java.util.ArrayList;

public class Main extends AcceptsInput {
    /// The main UI class of this project. handles or delegates all user interaction.

    private CharacterCreator characterCreator = new CharacterCreator();
    private CharClassCreator classCreator = new CharClassCreator();
    private CharRaceCreator raceCreator = new CharRaceCreator();
    private CharBackgroundCreator backgroundCreator = new CharBackgroundCreator();
    private InventoryItemCreator itemCreator = new InventoryItemCreator();
    private FeatureCreator featureCreator = new FeatureCreator();

    private CharacterEditor characterEditor = new CharacterEditor();

    private ArrayList<Character> characters;
    private ArrayList<CharClass> classes;
    private ArrayList<CharRace> races;
    private ArrayList<CharBackground> backgrounds;
    private ArrayList<InventoryItem> items;
    private ArrayList<Feature> features;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        characters = new ArrayList<>();
        classes = new ArrayList<>();
        races = new ArrayList<>();
        backgrounds = new ArrayList<>();
        items = new ArrayList<>();
        features = new ArrayList<>();

        mainMenu();
    }

    //MODIFIES: this
    //EFFECTS: creates and manages the main menu for users to navigate
    private void mainMenu() {
        System.out.println("WELCOME TO TINYSRVNT");
        while (true) {
            System.out.println("--Main Menu--\nPlease select an option:\n"
                               + "[1] Create an Asset\n[2] Edit a Character\n[3] Exit");
            int mainSelect = input.scanPositiveIntWithExit("Please select an option using its index.");
            if (mainSelect < 0) {
                return;
            }
            switch (mainSelect) {
                case (1):
                    assetCreateMenu();
                    break;
                case (2):
                    characterEditMenu();
                    break;
                case (3):
                    if (input.scanYesNo("Are you sure you want to exit?")) {
                        return;
                    }
                    break;
            }
            System.out.println("\n");
        }
    }

    private void characterEditMenu() {
        while (true) {
            System.out.println("--Character Edit Menu--");
            if (characters.isEmpty()) {
                System.out.println("It seems like no characters are saved. Try creating a character using the\n"
                                   + "Asset Creation Menu!\n\n");
                return;
            }
            System.out.println("Please select a character to edit:");
            for (int i = 0; i < characters.size(); i++) {
                System.out.println("[" + Integer.toString(i + 1) + "] " + characters.get(i).getName());
            }
            int characterSelect = input.scanPositiveIntWithExit("Please select an option using its index.");
            if (characterSelect < 0) {
                return;
            }
            if (characterSelect <= characters.size()) {
                characters.set(characterSelect - 1, characterEditor.edit(characters.get(characterSelect - 1),
                                                                         classes, races, backgrounds, items));
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: creates and manages asset creation menu
    private void assetCreateMenu() {
        while (true) {
            System.out.println("--Asset Creation Menu--\nPlease select an asset type to create:\n"
                               + "[1] Character\n[2] Class\n[3] Race\n"
                               + "[4] Background\n[5] Item\n[6] Feature\n[7] Return to Main Menu");
            int createSelect = input.scanPositiveIntWithExit("Please select an option using its index.");
            if (createSelect < 0) {
                return;
            }
            if (!decideCreate(createSelect)) {
                return;
            }
            System.out.println("\n");
        }
    }

    //REQUIRES: createSelect > 0
    //MODIFIES: this
    //EFFECTS: helper method for assetCreateMenu, contains switch statement for asset creation; returns false to return
    //         to main menu
    private boolean decideCreate(int createSelect) {
        switch (createSelect) {
            case (1):
                createCharacter();
                break;
            case (2):
                createClass();
                break;
            case (3):
                createRace();
                break;
            case (4):
                createBackground();
                break;
            case (5):
                createItem();
                break;
            case (6):
                createFeature();
                break;
            case (7):
                return false;
        }
        return true;
    }


    //MODIFIES: this
    //EFFECTS: creates a new character to add to this
    private void createCharacter() {
        Character character = characterCreator.createCharacter(classes, backgrounds, races);
        if (character == null) {
            return;
        }
        characters.add(character);
        System.out.println("Character created! Edit this character by using the Edit menu.");
    }

    //MODIFIES: this
    //EFFECTS: creates a new class to add to this
    private void createClass() {
        CharClass charClass = classCreator.createClass(features, classes, items);
        if (charClass == null) {
            return;
        }
        classes.add(charClass);
        System.out.println("Class created!");
    }

    //MODIFIES: this
    //EFFECTS: creates a new race to add to this
    private void createRace() {
        CharRace race = raceCreator.createRace(features, races);
        if (race == null) {
            return;
        }
        races.add(race);
        System.out.println("Race created!");
    }

    //MODIFIES: this
    //EFFECTS: creates a new background to add to this
    private void createBackground() {
        CharBackground background = backgroundCreator.createBackground(features, items);
        if (background == null) {
            return;
        }
        backgrounds.add(background);
        System.out.println("Background created!");
    }

    //MODIFIES: this
    //EFFECTS: creates a new item to add to this
    private void createItem() {
        InventoryItem item = itemCreator.createInventoryItem(features);
        if (item == null) {
            return;
        }
        items.add(item);
        System.out.println("Item created!");
    }

    //MODIFIES: this
    //EFFECTS: creates a new feature to add to this
    private void createFeature() {
        Feature feature = featureCreator.createFeature(features);
        if (feature == null) {
            return;
        }
        features.add(feature);
        System.out.println("Feature created!");
    }
}
