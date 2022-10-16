package model;

import enums.ScoreType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.ListOfHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharBackgroundTest {
    /// A series of tests for the CharBackground class.
    private CharBackground testEmptyBackground;
    private CharBackground testFullBackground;
    private InventoryItem itemTest1 = new InventoryItem("Short Sword");
    private InventoryItem itemTest2 = new InventoryItem("Small Flask");

    private Feature featureTest1 = new Feature("Test Feature 1", "Test 1");
    private Feature featureTest2 = new Feature("Test Feature 2", "Test 2");

    private Proficiency proficiencyTest1 = new Proficiency(ScoreType.CON_SAVE, new BigDecimal("0.2"));
    private Proficiency proficiencyTest2 = new Proficiency(ScoreType.DEX_SAVE, new BigDecimal("4.5"));
    private Proficiency proficiencyTest3 = new Proficiency("Short Swords", new BigDecimal("2"));

    private ArrayList<Proficiency> proficiencyList = new ArrayList<>(ListOfHelper.listOf(proficiencyTest1,
                                                                             proficiencyTest2,
                                                                             proficiencyTest3));
    private ArrayList<String> languageList = new ArrayList<>(ListOfHelper.listOf("Common", "TestLang"));
    private ArrayList<Feature> featureList = new ArrayList<>(ListOfHelper.listOf(featureTest1, featureTest2));
    private ArrayList<InventoryItem> itemList = new ArrayList<>(ListOfHelper.listOf(itemTest1, itemTest2));

    @BeforeEach
    void runBefore() {
        testEmptyBackground = new CharBackground("Test Empty Background");
        testFullBackground = new CharBackground("Test Full Background", proficiencyList,
                                                languageList, featureList, itemList, "Test 2");
    }

    @Test
    void testConstructor() {
        Assertions.assertEquals("Test Empty Background", testEmptyBackground.getName());
        Assertions.assertEquals("", testEmptyBackground.getDescription());
        Assertions.assertTrue(testEmptyBackground.getFeatures().isEmpty());
        Assertions.assertTrue(testEmptyBackground.getProficiencies().isEmpty());
        Assertions.assertTrue(testEmptyBackground.getLanguages().isEmpty());
        Assertions.assertTrue(testEmptyBackground.getEquipment().isEmpty());
    }

    @Test
    void testFullConstructor() {
        Assertions.assertEquals("Test Full Background", testFullBackground.getName());
        Assertions.assertEquals("Test 2", testFullBackground.getDescription());
        Assertions.assertEquals(proficiencyList, testFullBackground.getProficiencies());
        Assertions.assertEquals(languageList, testFullBackground.getLanguages());
        Assertions.assertEquals(featureList, testFullBackground.getFeatures());
        Assertions.assertEquals(itemList, testFullBackground.getEquipment());
    }

    @Test
    void testAddOneProficiency() {
        testEmptyBackground.addProficiency(proficiencyTest1);
        Assertions.assertTrue(testEmptyBackground.getProficiencies().contains(proficiencyTest1));
    }

    @Test
    void testAddManyProficiency() {
        testEmptyBackground.addProficiency(proficiencyTest1);
        testEmptyBackground.addProficiency(proficiencyTest2);
        Assertions.assertTrue(testEmptyBackground.getProficiencies().contains(proficiencyTest1));
        Assertions.assertTrue(testEmptyBackground.getProficiencies().contains(proficiencyTest2));
    }

    @Test
    void testRemoveScoreProficiency() {
        testFullBackground.removeProficiency("CON_SAVE");
        Assertions.assertFalse(testFullBackground.getProficiencies().contains(proficiencyTest1));
        Assertions.assertTrue(testFullBackground.getProficiencies().contains(proficiencyTest2));
        Assertions.assertTrue(testFullBackground.getProficiencies().contains(proficiencyTest3));
    }

    @Test
    void testRemoveStringProficiency() {
        testFullBackground.removeProficiency("Short Swords");
        Assertions.assertFalse(testFullBackground.getProficiencies().contains(proficiencyTest3));
        Assertions.assertTrue(testFullBackground.getProficiencies().contains(proficiencyTest1));
        Assertions.assertTrue(testFullBackground.getProficiencies().contains(proficiencyTest2));
    }

    @Test
    void testRemoveDuplicateScoreProficiency() {
        Proficiency proficiencyTest4 = new Proficiency(ScoreType.CON_SAVE, new BigDecimal("4"));
        testFullBackground.addProficiency(proficiencyTest4);
        testFullBackground.removeProficiency("CON_SAVE");
        Assertions.assertFalse(testFullBackground.getProficiencies().contains(proficiencyTest1));
        Assertions.assertTrue(testFullBackground.getProficiencies().contains(proficiencyTest2));
        Assertions.assertTrue(testFullBackground.getProficiencies().contains(proficiencyTest4));
    }

    @Test
    void testRemoveDuplicateObjectProficiency() {
        testFullBackground.addProficiency(proficiencyTest1);
        testFullBackground.removeProficiency("CON_SAVE");
        Assertions.assertTrue(testFullBackground.getProficiencies().contains(proficiencyTest1));
        Assertions.assertNotEquals(proficiencyTest1, testFullBackground.getProficiencies().get(0));
    }

    @Test
    void testAddOneLanguage() {
        testEmptyBackground.addLanguage("Elvish");
        Assertions.assertTrue(testEmptyBackground.getLanguages().contains("Elvish"));
    }

    @Test
    void testAddManyLanguage() {
        testEmptyBackground.addLanguage("Elvish");
        testEmptyBackground.addLanguage("Java");
        Assertions.assertTrue(testEmptyBackground.getLanguages().contains("Elvish"));
        Assertions.assertTrue(testEmptyBackground.getLanguages().contains("Java"));
    }

    @Test
    void testRemoveLanguage() {
        testFullBackground.removeLanguage("Common");
        Assertions.assertFalse(testFullBackground.getLanguages().contains("Common"));
        Assertions.assertTrue(testFullBackground.getLanguages().contains("TestLang"));
    }

    @Test
    void testRemoveDuplicateLanguage() {
        testFullBackground.addLanguage("Common");
        Assertions.assertEquals("Common", testFullBackground.getLanguages().get(0));
        testFullBackground.removeLanguage("Common");
        Assertions.assertTrue(testFullBackground.getLanguages().contains("Common"));
        Assertions.assertNotEquals("Common", testFullBackground.getLanguages().get(0));
    }

    @Test
    void testAddOneItem() {
        testEmptyBackground.addEquipment(itemTest1);
        Assertions.assertTrue(testEmptyBackground.getEquipment().contains(itemTest1));
    }

    @Test
    void testAddManyItem() {
        testEmptyBackground.addEquipment(itemTest1);
        testEmptyBackground.addEquipment(itemTest2);
        Assertions.assertTrue(testEmptyBackground.getEquipment().contains(itemTest1));
        Assertions.assertTrue(testEmptyBackground.getEquipment().contains(itemTest2));
    }

    @Test
    void testRemoveItem() {
        testFullBackground.removeEquipment("Short Sword");
        Assertions.assertFalse(testFullBackground.getEquipment().contains(itemTest1));
        Assertions.assertTrue(testFullBackground.getEquipment().contains(itemTest2));
    }

    @Test
    void testRemoveDuplicateNameItem() {
        InventoryItem itemTest4 = new InventoryItem("Short Sword");
        testFullBackground.addEquipment(itemTest4);
        testFullBackground.removeEquipment("Short Sword");
        Assertions.assertFalse(testFullBackground.getEquipment().contains(itemTest1));
        Assertions.assertTrue(testFullBackground.getEquipment().contains(itemTest2));
        Assertions.assertTrue(testFullBackground.getEquipment().contains(itemTest4));
    }

    @Test
    void testRemoveDuplicateObjectItem() {
        testFullBackground.addEquipment(itemTest1);
        testFullBackground.removeEquipment("Short Sword");
        Assertions.assertTrue(testFullBackground.getEquipment().contains(itemTest1));
        Assertions.assertNotEquals(itemTest1, testFullBackground.getEquipment().get(0));
    }

    @Test
    void testGetAllProficienciesApplied() {
        ArrayList<HashMap<ScoreType, Modifier>> testApplyList =
                testFullBackground.getAllScoreProficienciesApplied(new BigDecimal("1"));
        Assertions.assertEquals(proficiencyTest1.generateScoreMap(new BigDecimal("1")),
                testApplyList.get(0));
        Assertions.assertEquals(proficiencyTest2.generateScoreMap(new BigDecimal("1")),
                testApplyList.get(1));
    }
}
