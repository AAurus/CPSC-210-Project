package model;

import enums.ModifierType;
import enums.ScoreType;
import enums.StatType;
import exceptions.ClassNotFoundException;
import exceptions.FeatureNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class CharacterTest {
    /// A series of tests for the Character class.
    private Feature testFeature = new Feature("Test Feature", ScoreType.STRENGTH, new Modifier(ModifierType.ADD,
                                                                                               new BigDecimal(2)));
    private Character testCharacter;

    @BeforeEach
    void setUp() {
        testCharacter = new Character("Test Character", 10,11,12,13,14,15);
    }

    @AfterEach
    void clearLog() {
        EventLog.getInstance().clear();
    }

    @Test
    void testConstructorEmpties() {
        Assertions.assertTrue(testCharacter.getClasses().isEmpty());
        Assertions.assertTrue(testCharacter.getClassLevels().isEmpty());
        Assertions.assertEquals(0, testCharacter.getCharacterLevel());
        Assertions.assertEquals(1, testCharacter.calculateProficiencyBonus());

        Assertions.assertNull(testCharacter.getRace());
        Assertions.assertNull(testCharacter.getBackground());

        Assertions.assertTrue(testCharacter.getFeatures().isEmpty());

        Assertions.assertTrue(testCharacter.getEquippedItems().isEmpty());
        Assertions.assertTrue(testCharacter.getCarriedItems().isEmpty());
        Assertions.assertTrue(testCharacter.getInventoryItems().isEmpty());
    }

    @Test
    void testConstructorScores() {
        Assertions.assertEquals(10, testCharacter.getRolledAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(11, testCharacter.getRolledAbilityScores().get(ScoreType.DEXTERITY));
        Assertions.assertEquals(12, testCharacter.getRolledAbilityScores().get(ScoreType.CONSTITUTION));
        Assertions.assertEquals(13, testCharacter.getRolledAbilityScores().get(ScoreType.INTELLIGENCE));
        Assertions.assertEquals(14, testCharacter.getRolledAbilityScores().get(ScoreType.WISDOM));
        Assertions.assertEquals(15, testCharacter.getRolledAbilityScores().get(ScoreType.CHARISMA));

        Assertions.assertEquals(10, testCharacter.getAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(11, testCharacter.getAbilityScores().get(ScoreType.DEXTERITY));
        Assertions.assertEquals(12, testCharacter.getAbilityScores().get(ScoreType.CONSTITUTION));
        Assertions.assertEquals(13, testCharacter.getAbilityScores().get(ScoreType.INTELLIGENCE));
        Assertions.assertEquals(14, testCharacter.getAbilityScores().get(ScoreType.WISDOM));
        Assertions.assertEquals(15, testCharacter.getAbilityScores().get(ScoreType.CHARISMA));
    }

    @Test
    void testConstructorStats() {
        Assertions.assertEquals(1, testCharacter.getStats().get(StatType.PROFICIENCY_BONUS));

        Assertions.assertEquals(1, testCharacter.getStats().get(StatType.HIT_POINT_CON_PER_LEVEL));
        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.MAX_HIT_POINTS));
        Assertions.assertEquals(testCharacter.getStats().get(StatType.MAX_HIT_POINTS),
                                testCharacter.getStats().get(StatType.CURRENT_HIT_POINTS));
        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.TEMPORARY_HIT_POINTS));

        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.INITIATIVE_BONUS));
        Assertions.assertEquals(10, testCharacter.getStats().get(StatType.INITIATIVE));

        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.WALK_SPEED));
        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.CLIMB_SPEED));
        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.SWIM_SPEED));
        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.FLY_SPEED));

        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.ARMOR_CLASS));
        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.DEXTERITY_ARMOR_BONUS));
        Assertions.assertEquals(150, testCharacter.getStats().get(StatType.CARRY_CAPACITY));
    }

    @Test
    void testConstructorEvent() {
        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = (containsEvent || e.getDescription().equals("New character created: Test Character"));
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testSetName() {
        testCharacter.setName("New Name");
        Assertions.assertEquals("New Name", testCharacter.getName());
    }

    @Test
    void testSetRace() {
        testCharacter.setRace("New Race");
        Assertions.assertEquals("New Race", testCharacter.getRace());
    }

    @Test
    void testSetBackground() {
        testCharacter.setBackground("New Background");
        Assertions.assertEquals("New Background", testCharacter.getBackground());
    }

    @Test
    void testAddClass() {
        testCharacter.addClass("Test Class");
        Assertions.assertEquals(1, testCharacter.getCharacterLevel());
        Assertions.assertTrue(testCharacter.getClasses().contains("Test Class"));
        Assertions.assertTrue(testCharacter.getClassLevels().containsKey("Test Class0"));
        Assertions.assertTrue(testCharacter.getClassLevels().get("Test Class0").equals(1));
    }

    @Test
    void testAddManyClass() {
        testCharacter.addClass("Test Class");
        testCharacter.addClass("");
        Assertions.assertEquals(2, testCharacter.getCharacterLevel());
        Assertions.assertTrue(testCharacter.getClasses().contains("Test Class"));
        Assertions.assertTrue(testCharacter.getClasses().contains(""));
        Assertions.assertTrue(testCharacter.getClassLevels().containsKey("Test Class0"));
        Assertions.assertTrue(testCharacter.getClassLevels().containsKey("1"));
    }

    @Test
    void testRemoveClassIndexFailure() {
        try {
            testCharacter.removeClass(0);
            Assertions.fail("Unexpected Success");
        } catch (IndexOutOfBoundsException ie) {
            Assertions.assertTrue(testCharacter.getClasses().isEmpty());
        }

        testCharacter.addClass("Test Class");
        try {
            testCharacter.removeClass(1);
            Assertions.fail("Unexpected Success");
        } catch (IndexOutOfBoundsException ie) {
            Assertions.assertTrue(testCharacter.getClasses().contains("Test Class"));
        }
    }

    @Test
    void testRemoveClassIndexSuccess() {
        testCharacter.addClass("Test 1");
        testCharacter.addClass("Test 2");

        try {
            testCharacter.removeClass(1);
            Assertions.assertEquals(1, testCharacter.getCharacterLevel());
            Assertions.assertFalse(testCharacter.getClassLevels().containsKey("Test 21"));
            Assertions.assertTrue(testCharacter.getClassLevels().containsKey("Test 10"));
            Assertions.assertFalse(testCharacter.getClasses().contains("Test 2"));
            Assertions.assertTrue(testCharacter.getClasses().contains("Test 1"));
        } catch (IndexOutOfBoundsException ie) {
            Assertions.fail("Unexpected IndexOutOfBounds Exception");
        }
    }

    @Test
    void testRemoveClassNameFailure() {
        try {
            testCharacter.removeClass("Test");
            Assertions.fail("Unexpected Success");
        } catch (ClassNotFoundException ce) {
            Assertions.assertTrue(testCharacter.getClasses().isEmpty());
        }

        testCharacter.addClass("Test Class");
        try {
            testCharacter.removeClass("Test");
            Assertions.fail("Unexpected Success");
        } catch (ClassNotFoundException ce) {
            Assertions.assertTrue(testCharacter.getClasses().contains("Test Class"));
        }
    }

    @Test
    void testRemoveClassNameSuccess() {
        testCharacter.addClass("Test 1");
        testCharacter.addClass("Test 2");

        try {
            testCharacter.removeClass("Test 2");
            Assertions.assertEquals(1, testCharacter.getCharacterLevel());
            Assertions.assertFalse(testCharacter.getClassLevels().containsKey("Test 21"));
            Assertions.assertTrue(testCharacter.getClassLevels().containsKey("Test 10"));
            Assertions.assertFalse(testCharacter.getClasses().contains("Test 2"));
            Assertions.assertTrue(testCharacter.getClasses().contains("Test 1"));
        } catch (ClassNotFoundException ie) {
            Assertions.fail("Unexpected ClassNotFound Exception");
        }
    }

    @Test
    void testAddFeatureScoreMod() {
        testCharacter.addFeature(testFeature);

         Assertions.assertEquals(10, testCharacter.getRolledAbilityScores().get(ScoreType.STRENGTH));
         Assertions.assertEquals(12, testCharacter.getAbilityScores().get(ScoreType.STRENGTH));
         Assertions.assertEquals(1, testCharacter.getSkillThrowBonuses().get(ScoreType.STR_CHECK));
    }

    @Test
    void testAddFeatureStatMod() {
        testFeature = new Feature("Test Feature", StatType.HIT_POINT_CON_PER_LEVEL,
                                  new Modifier(ModifierType.ADD, new BigDecimal(2)));
        Feature testFeature2 = new Feature("Walk Feature", StatType.WALK_SPEED,
                                           new Modifier(ModifierType.ADD, new BigDecimal(30)));
        testCharacter.addFeature(testFeature);
        testCharacter.addFeature(testFeature2);
        Assertions.assertFalse(testCharacter.getFeatures().isEmpty());
        Assertions.assertTrue(testCharacter.getFeatures().contains(testFeature));
        Assertions.assertTrue(testCharacter.getFeatures().contains(testFeature2));

        Assertions.assertEquals(3, testCharacter.getStats().get(StatType.HIT_POINT_CON_PER_LEVEL));
        Assertions.assertEquals(30, testCharacter.getStats().get(StatType.WALK_SPEED));
        Assertions.assertEquals(15, testCharacter.getStats().get(StatType.CLIMB_SPEED));
        Assertions.assertEquals(15, testCharacter.getStats().get(StatType.SWIM_SPEED));
        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.FLY_SPEED));

        testCharacter.addClass("Test Level Up");
        Assertions.assertEquals(3, testCharacter.getStats().get(StatType.MAX_HIT_POINTS));
        Assertions.assertEquals(30, testCharacter.getStats().get(StatType.WALK_SPEED));
        Assertions.assertEquals(15, testCharacter.getStats().get(StatType.CLIMB_SPEED));
        Assertions.assertEquals(15, testCharacter.getStats().get(StatType.SWIM_SPEED));
        Assertions.assertEquals(0, testCharacter.getStats().get(StatType.FLY_SPEED));
    }

    @Test
    void testAddFeatureProficiency() {
        testFeature = new Feature("Proficiency Feature", new Proficiency(ScoreType.STRENGTH, new BigDecimal(2)));
        testCharacter.addFeature(testFeature);

        Assertions.assertEquals(10, testCharacter.getRolledAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(12, testCharacter.getAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(1, testCharacter.getSkillThrowBonuses().get(ScoreType.STR_CHECK));

        testCharacter.addClass("Test Level Up");
        Assertions.assertEquals(10, testCharacter.getRolledAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(14,
                                testCharacter.getAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(2,
                                testCharacter.getSkillThrowBonuses().get(ScoreType.STR_CHECK));
    }

    @Test
    void testAddFeatureLog() {
        testFeature = new Feature("Test Feature", "Test Feature");
        testCharacter.addFeature(testFeature);

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = (containsEvent || e.getDescription().equals("Feature Test Feature added to character "
                                                                        + "Test Character"));
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testRemoveFeatureIndexFail() {
        try {
            testCharacter.removeFeature(0);
            Assertions.fail("Unexpected Success");
        } catch (IndexOutOfBoundsException ie) {
            Assertions.assertTrue(testCharacter.getFeatures().isEmpty());
        }

        testCharacter.addFeature(testFeature);
        try {
            testCharacter.removeFeature(1);
            Assertions.fail("Unexpected Success");
        } catch (IndexOutOfBoundsException ie) {
            Assertions.assertEquals(1, testCharacter.getFeatures().size());
            Assertions.assertTrue(testCharacter.getFeatures().contains(testFeature));
        }
    }

    @Test
    void testRemoveFeatureIndexSuccess() {
        Feature testFeature2 = new Feature("Test Feature 2", "Test 2");
        testCharacter.addFeature(testFeature2);
        testCharacter.addFeature(testFeature);

        try {
            testCharacter.removeFeature(1);
            Assertions.assertFalse(testCharacter.getFeatures().contains(testFeature));
            Assertions.assertTrue(testCharacter.getFeatures().contains(testFeature2));
        } catch (IndexOutOfBoundsException ie) {
            Assertions.fail("Unexpected IndexOutOfBounds Exception");
        }
    }

    @Test
    void testRemoveFeatureNameFailure() {
        try {
            testCharacter.removeFeature("Test");
            Assertions.fail("Unexpected Success");
        } catch (FeatureNotFoundException ce) {
            Assertions.assertTrue(testCharacter.getFeatures().isEmpty());
        }

        testCharacter.addFeature(testFeature);
        try {
            testCharacter.removeFeature("Test");
            Assertions.fail("Unexpected Success");
        } catch (FeatureNotFoundException ce) {
            Assertions.assertTrue(testCharacter.getFeatures().contains(testFeature));
        }
    }

    @Test
    void testRemoveFeatureNameSuccess() {
        Feature testFeature2 = new Feature("Test Feature 2", "Test 2");
        testCharacter.addFeature(testFeature2);
        testCharacter.addFeature(testFeature);

        try {
            testCharacter.removeFeature("Test Feature");
            Assertions.assertFalse(testCharacter.getFeatures().contains(testFeature));
            Assertions.assertTrue(testCharacter.getFeatures().contains(testFeature2));
        } catch (FeatureNotFoundException ie) {
            Assertions.fail("Unexpected FeatureNotFound Exception");
        }
    }

    @Test
    void testAddEquippedItem() {
        InventoryItem testItem = new InventoryItem("Test Item");
        testItem.setFeature(testFeature);

        testCharacter.addEquippedItem(testItem);

        Assertions.assertTrue(testCharacter.getEquippedItems().contains(testItem));
        Assertions.assertFalse(testCharacter.getCarriedItems().contains(testItem));
        Assertions.assertFalse(testCharacter.getInventoryItems().contains(testItem));

        Assertions.assertEquals(10, testCharacter.getRolledAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(12, testCharacter.getAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(1, testCharacter.getSkillThrowBonuses().get(ScoreType.STR_CHECK));

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = (containsEvent || e.getDescription().equals("Item Test Item added to character "
                                                                        + "Test Character's equipment"));
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testAddCarriedItem() {
        InventoryItem testItem = new InventoryItem("Test Item");
        testItem.setFeature(testFeature);

        testCharacter.addCarriedItem(testItem);

        Assertions.assertTrue(testCharacter.getCarriedItems().contains(testItem));
        Assertions.assertFalse(testCharacter.getEquippedItems().contains(testItem));
        Assertions.assertFalse(testCharacter.getInventoryItems().contains(testItem));

        Assertions.assertEquals(10, testCharacter.getRolledAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(10, testCharacter.getAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(0, testCharacter.getSkillThrowBonuses().get(ScoreType.STR_CHECK));
    }

    @Test
    void testAddInventoryItem() {
        InventoryItem testItem = new InventoryItem("Test Item");
        testItem.setFeature(testFeature);

        testCharacter.addInventoryItem(testItem);

        Assertions.assertTrue(testCharacter.getInventoryItems().contains(testItem));
        Assertions.assertFalse(testCharacter.getCarriedItems().contains(testItem));
        Assertions.assertFalse(testCharacter.getEquippedItems().contains(testItem));

        Assertions.assertEquals(10, testCharacter.getRolledAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(10, testCharacter.getAbilityScores().get(ScoreType.STRENGTH));
        Assertions.assertEquals(0, testCharacter.getSkillThrowBonuses().get(ScoreType.STR_CHECK));
    }

    @Test
    void testLevelUpIndexFail() {
        try {
            testCharacter.levelUp(0);
            Assertions.fail("Unexpected success");
        } catch (IndexOutOfBoundsException ioe) {
            Assertions.assertEquals(0, testCharacter.getCharacterLevel());
        }
    }

    @Test
    void testLevelUpIndex() {
        testCharacter.addClass("Test Class");
        Assertions.assertEquals(1, testCharacter.getClassLevels().get("Test Class0"));

        testCharacter.levelUp(0);
        Assertions.assertEquals(2, testCharacter.getClassLevels().get("Test Class0"));

        testCharacter.levelUp(0);
        testCharacter.levelUp(0);
        Assertions.assertEquals(4, testCharacter.getClassLevels().get("Test Class0"));
    }

    @Test
    void testLevelUpNameFail() {
        try {
            testCharacter.levelUp("Test Class");
            Assertions.fail("Unexpected success");
        } catch (ClassNotFoundException ce) {
            Assertions.assertEquals(0, testCharacter.getCharacterLevel());
        }
    }

    @Test
    void testLevelUpName() {
        try {
            testCharacter.addClass("Test Class");
            Assertions.assertEquals(1, testCharacter.getClassLevels().get("Test Class0"));

            testCharacter.levelUp("Test Class");
            Assertions.assertEquals(2, testCharacter.getClassLevels().get("Test Class0"));

            testCharacter.levelUp("Test Class");
            testCharacter.levelUp("Test Class");
            Assertions.assertEquals(4, testCharacter.getClassLevels().get("Test Class0"));
        } catch (ClassNotFoundException ce) {
            Assertions.fail("Unexpected ClassNotFound Exception");
        }
    }

    @Test
    void testSetLevelIndexFail() {
        try {
            testCharacter.setLevel(0, 10);
            Assertions.fail("Unexpected success");
        } catch (IndexOutOfBoundsException ioe) {
            Assertions.assertEquals(0, testCharacter.getCharacterLevel());
        }
    }

    @Test
    void testSetLevelIndex() {
        testCharacter.addClass("Test Class");
        Assertions.assertEquals(1, testCharacter.getClassLevels().get("Test Class0"));

        testCharacter.setLevel(0, 10);
        Assertions.assertEquals(10, testCharacter.getClassLevels().get("Test Class0"));
    }

    @Test
    void testSetLevelNameFail() {
        try {
            testCharacter.setLevel("Test Class", 10);
            Assertions.fail("Unexpected success");
        } catch (ClassNotFoundException ce) {
            Assertions.assertEquals(0, testCharacter.getCharacterLevel());
        }
    }

    @Test
    void testSetLevelName() {
        try {
            testCharacter.addClass("Test Class");
            Assertions.assertEquals(1, testCharacter.getClassLevels().get("Test Class0"));

            testCharacter.setLevel("Test Class", 10);
            Assertions.assertEquals(10, testCharacter.getClassLevels().get("Test Class0"));
        } catch (ClassNotFoundException ce) {
            Assertions.fail("Unexpected ClassNotFound Exception");
        }
    }
    
    @Test
    void testEquals() {
        Assertions.assertTrue(testCharacter.equals(testCharacter));

        Character testCharacter2 = new Character("Test 2", 10, 11, 12, 13, 14, 15);
        testCharacter2.setBackground("Apprentice");
        testCharacter2.setRace("Elf");
        testCharacter2.addClass("Wizard");

        Character testCharacter3 = new Character("Test Character", 10, 11, 12, 13, 14 ,15);

        Assertions.assertFalse(testCharacter.equals(null));
        Assertions.assertFalse(testCharacter.equals(new BigDecimal(2)));
        Assertions.assertFalse(testCharacter.equals(testCharacter2));
        Assertions.assertTrue(testCharacter.equals(testCharacter3));

        testCharacter.setBackground("The");
        testCharacter.setRace("Test");
        testCharacter.addClass("Check");
        Assertions.assertFalse(testCharacter.equals(testCharacter2));
        Assertions.assertFalse(testCharacter.equals(testCharacter3));

        testCharacter.removeClass(0);
        testCharacter.setBackground("Apprentice");
        testCharacter.setRace("Elf");
        testCharacter.addClass("Wizard");
        testCharacter.setName("Test 2");
        Assertions.assertTrue(testCharacter.equals(testCharacter2));
        Assertions.assertFalse(testCharacter.equals(testCharacter3));
    }
}
