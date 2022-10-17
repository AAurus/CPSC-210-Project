package ui;

import enums.*;
import model.CharBackground;
import model.CharClass;
import model.CharRace;
import model.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharacterCreator {
    /// A helper for main that handles creating characters.
    private InputHelper input = new InputHelper();

    //EFFECTS: creates a Character according to user input
    public Character createCharacter(List<CharClass> classList, List<CharBackground> backgroundList,
                                     List<CharRace> raceList) {
        String name = input.scanName("Character");
        if (name == null) {
            return null;
        }
        HashMap<ScoreType, Integer> baseScores = scanCharacterBaseScores();
        Character result = new Character(name, baseScores.get(ScoreType.STRENGTH), baseScores.get(ScoreType.DEXTERITY),
                                         baseScores.get(ScoreType.CONSTITUTION), baseScores.get(ScoreType.INTELLIGENCE),
                                         baseScores.get(ScoreType.WISDOM), baseScores.get(ScoreType.CHARISMA));
        if (input.scanYesNo("Would you like to add more information to this character?")) {
            return createCharacterExtended(result, backgroundList, raceList);
        }
        return result;
    }

    //EFFECTS: adds extra information to baseCharacter in the form of race and background, then returns it
    private Character createCharacterExtended(Character baseCharacter, List<CharBackground> backgroundList,
                                              List<CharRace> raceList) {
        Character result = baseCharacter;
        if (input.scanYesNo("Would you like to set the race of this character?")) {
            result = addCharacterRace(result, raceList);
            if (result == null) {
                return null;
            }
        }
        if (input.scanYesNo("Would you like to set the background of this character?")) {
            result = addCharacterBackground(result, backgroundList);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    //EFFECTS: returns hashMap of positive ints for character base ability scores
    private HashMap<ScoreType, Integer> scanCharacterBaseScores() {
        HashMap<ScoreType, Integer> result = new HashMap<>();
        for (ScoreType s : ScoreType.BASE_SCORES) {
            int score = input.scanPositiveIntWithExit("Please enter your " + s.name().toLowerCase() + " score");
            if (score < 0) {
                return null;
            }
            result.put(s, score);
        }
        return result;
    }

    //EFFECTS: sets Background of baseCharacter, then returns it
    private Character addCharacterBackground(Character baseCharacter, List<CharBackground> backgrounds) {
        if (backgrounds.isEmpty()) {
            System.out.println("Sorry, this functionality isn't available yet. Create some backgrounds first!");
            return null;
        }
        System.out.println("Backgrounds Available:");
        for (int i = 0; i < backgrounds.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + backgrounds.get(i).getName());
        }
        while (true) {
            int raceIndex = input.scanPositiveIntWithExit("Please choose a background by its index:");
            if (raceIndex < 0) {
                return null;
            }
            if (raceIndex > 0 && raceIndex <= backgrounds.size()) {
                baseCharacter.setBackground(backgrounds.get(raceIndex - 1));
                return baseCharacter;
            }
        }
    }
    
    //EFFECTS: sets Race of baseCharacter, then returns it
    private Character addCharacterRace(Character baseCharacter, List<CharRace> races) {
        if (races.isEmpty()) {
            System.out.println("Sorry, this functionality isn't available yet. Create some races first!");
            return null;
        }
        System.out.println("Races Available:");
        for (int i = 0; i < races.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + races.get(i).getName());
        }
        while (true) {
            int raceIndex = input.scanPositiveIntWithExit("Please choose a race by its index:");
            if (raceIndex < 0) {
                return null;
            }
            if (raceIndex > 0 && raceIndex <= races.size()) {
                baseCharacter.setRace(races.get(raceIndex - 1));
                if (!races.get(raceIndex - 1).getSubRaces().isEmpty()) {
                    if (input.scanYesNo("Would you also like to select a subrace?")) {
                        return addCharacterSubRace(baseCharacter);
                    }
                }
                return baseCharacter;
            }
        }
    }

    //EFFECTS: sets subrace on baseCharacter, then returns it
    private Character addCharacterSubRace(Character baseCharacter) {
        if (baseCharacter.getRace() == null) {
            System.out.println("Looks like you don't have a race yet! Set your race first!");
            return baseCharacter;
        }
        if (baseCharacter.getRace().getSubRaces().isEmpty()) {
            System.out.println("This character's race has no subraces!");
            return baseCharacter;
        }
        ArrayList<CharRace> races = baseCharacter.getRace().getSubRaces();
        System.out.println("Subraces Available:");
        for (int i = 0; i < races.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + races.get(i).getName());
        }
        while (true) {
            int raceIndex = input.scanPositiveIntWithExit("Please choose a subrace by its index:");
            if (raceIndex < 0) {
                return null;
            }
            if (raceIndex > 0 && raceIndex <= races.size()) {
                baseCharacter.getRace().selectSubRace(raceIndex - 1);
                return baseCharacter;
            }
        }
    }
}
