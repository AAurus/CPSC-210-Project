package ui;

import enums.*;
import model.CharBackground;
import model.CharClass;
import model.CharRace;
import model.Character;

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
        return new Character(name, baseScores.get(ScoreType.STRENGTH), baseScores.get(ScoreType.DEXTERITY),
                baseScores.get(ScoreType.CONSTITUTION), baseScores.get(ScoreType.INTELLIGENCE),
                baseScores.get(ScoreType.WISDOM), baseScores.get(ScoreType.CHARISMA));
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
}
