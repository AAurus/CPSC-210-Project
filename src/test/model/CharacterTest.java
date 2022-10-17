package model;

import enums.ModifierType;
import enums.ScoreType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

class CharacterTest {
    /// A series of tests for the Character class.
    private Feature testFeature = new Feature("Test Feature", ScoreType.STRENGTH, new Modifier(ModifierType.ADD,
                                                                                               new BigDecimal(1)));
    private CharClass testMainClass = new CharClass("Test Main Class", new ArrayList<>(), new ArrayList<>(),
                                                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 3, 6);
    private CharClass testSubClass = new CharClass("Test Sub Class", new ArrayList<>());
    private CharRace testRace = new CharRace("Test Race");
    private CharBackground testBackground = new CharBackground("Test Background");

    private Character testCharacter;

    @BeforeEach
    void setUp() {
        testMainClass.addFeature(testFeature);
        testSubClass.addFeature(testFeature);
        testRace.addFeature(testFeature);
        testBackground.addFeature(testFeature);

        testCharacter = new Character("Test Character", 10,11,12,13,14,15);
    }

    @Test
    void testConstructor() {
        HashMap<ScoreType, Integer> baseScoreMap = new HashMap<>();
        Assertions.assertTrue(testCharacter.getClasses().isEmpty());
        Assertions.assertEquals(10, testCharacter.getRolledAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(11, testCharacter.getRolledAbilityScores().get(ScoreType.DEXTERITY));
        Assertions.assertEquals(12, testCharacter.getRolledAbilityScores().get(ScoreType.CONSTITUTION));
        Assertions.assertEquals(13, testCharacter.getRolledAbilityScores().get(ScoreType.INTELLIGENCE));
        Assertions.assertEquals(14, testCharacter.getRolledAbilityScores().get(ScoreType.WISDOM));
        Assertions.assertEquals(15, testCharacter.getRolledAbilityScores().get(ScoreType.CHARISMA));
        Assertions.assertNull(testCharacter.getRace());
        Assertions.assertNull(testCharacter.getBackground());
        Assertions.assertTrue(testCharacter.getEquippedItems().isEmpty());
        Assertions.assertTrue(testCharacter.getCarriedItems().isEmpty());
        Assertions.assertTrue(testCharacter.getInventoryItems().isEmpty());
    }

    @Test
    void testGetCharacterLevel() {
        Assertions.assertEquals(0, testCharacter.getCharacterLevel());
        testCharacter.addClass(testMainClass);
        Assertions.assertEquals(1, testCharacter.getCharacterLevel());
        testMainClass.levelUp();
        Assertions.assertEquals(2, testCharacter.getCharacterLevel());
        testCharacter.addClass(testMainClass);
        Assertions.assertEquals(4, testCharacter.getCharacterLevel());
    }

    @Test
    void testAddClass() {
        Assertions.assertEquals(10, testCharacter.getAbilityScores().get(ScoreType.STRENGTH));
        testCharacter.addClass(testMainClass);
        Assertions.assertEquals(11, testCharacter.getAbilityScores().get(ScoreType.STRENGTH));
    }
}