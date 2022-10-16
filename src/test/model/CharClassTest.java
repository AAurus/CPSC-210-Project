package model;

import enums.ModifierType;
import enums.ScoreType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharClassTest {
    /// A series of tests for the CharClass class.
    private CharClass testEmptyClass;
    private CharClass testFullClass;
    private CharClass testSubClass1;
    private CharClass testSubClass2;

    private InventoryItem testItem1 = new InventoryItem("Short Sword");
    private InventoryItem testItem2 = new InventoryItem("Small Flask");

    private Feature testFeature1 = new Feature("Test Feature 1", "Test 1");
    private Feature testFeature2 = new Feature("Test Feature 2", "Test 2");

    private Proficiency testProficiency1 = new Proficiency(ScoreType.CON_SAVE, new BigDecimal("0.2"));
    private Proficiency testProficiency2 = new Proficiency(ScoreType.DEX_SAVE, new BigDecimal("4.5"));
    private Proficiency testProficiency3 = new Proficiency("Short Swords", new BigDecimal("2"));

    private ArrayList<Proficiency> proficiencyList = new ArrayList<>(List.of(testProficiency1,
            testProficiency2,
            testProficiency3));
    private ArrayList<Feature> featureList = new ArrayList<>(List.of(testFeature1, testFeature2));
    private ArrayList<InventoryItem> itemList = new ArrayList<>(List.of(testItem1, testItem2));

    @BeforeEach
    void runBefore() {
        testSubClass1 = new CharClass("Test Subclass 1", new ArrayList<>());
        testSubClass2 = new CharClass("Test Subclass 2", featureList);
        testEmptyClass = new CharClass("Test Empty Class", new ArrayList<>(), new ArrayList<>(),
                                                      new ArrayList<>(), new ArrayList<>(),
                                                      new ArrayList<>(), 3, 6);
        testFullClass = new CharClass("Test Full Class", proficiencyList, proficiencyList,
                featureList, itemList, new ArrayList<>(List.of(testSubClass1, testSubClass2)), 3, 6);
    }

    @Test
    void testConstructor() {
        Assertions.assertNotNull(testEmptyClass.getName());
        Assertions.assertNotNull(testEmptyClass.getFeatures());
        Assertions.assertNotNull(testEmptyClass.getBaseProficiencies());
        Assertions.assertEquals(1, testEmptyClass.getLevel());
        Assertions.assertEquals(3, testEmptyClass.getSubClassLevel());
    }

    @Test
    void testLevelUpOnce() {
        testEmptyClass.levelUp();
        Assertions.assertEquals(2, testEmptyClass.getLevel());
    }

    @Test
    void testLevelUpMultiple() {
        testEmptyClass.levelUp();
        testEmptyClass.levelUp();
        Assertions.assertEquals(3, testEmptyClass.getLevel());
    }

    @Test
    void testAddOneItem() {
        testEmptyClass.addEquipment(testItem1);
        Assertions.assertTrue(testEmptyClass.getEquipment().contains(testItem1));
    }

    @Test
    void testAddManyItem() {
        testEmptyClass.addEquipment(testItem1);
        testEmptyClass.addEquipment(testItem2);
        Assertions.assertTrue(testEmptyClass.getEquipment().contains(testItem1));
        Assertions.assertTrue(testEmptyClass.getEquipment().contains(testItem2));
    }

    @Test
    void testRemoveItem() {
        testFullClass.removeEquipment("Short Sword");
        Assertions.assertFalse(testFullClass.getEquipment().contains(testItem1));
        Assertions.assertTrue(testFullClass.getEquipment().contains(testItem2));
    }

    @Test
    void testRemoveDuplicateNameItem() {
        InventoryItem itemTest4 = new InventoryItem("Short Sword");
        testFullClass.addEquipment(itemTest4);
        testFullClass.removeEquipment("Short Sword");
        Assertions.assertFalse(testFullClass.getEquipment().contains(testItem1));
        Assertions.assertTrue(testFullClass.getEquipment().contains(testItem2));
        Assertions.assertTrue(testFullClass.getEquipment().contains(itemTest4));
    }

    @Test
    void testRemoveDuplicateObjectItem() {
        testFullClass.addEquipment(testItem1);
        testFullClass.removeEquipment("Short Sword");
        Assertions.assertTrue(testFullClass.getEquipment().contains(testItem1));
        Assertions.assertNotEquals(testItem1, testFullClass.getEquipment().get(0));
    }

    @Test
    void testAddOneBaseProficiency() {
        testEmptyClass.addBaseProficiency(testProficiency1);
        Assertions.assertTrue(testEmptyClass.getBaseProficiencies().contains(testProficiency1));
    }

    @Test
    void testAddManyBaseProficiency() {
        testEmptyClass.addBaseProficiency(testProficiency1);
        testEmptyClass.addBaseProficiency(testProficiency2);
        Assertions.assertTrue(testEmptyClass.getBaseProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testEmptyClass.getBaseProficiencies().contains(testProficiency2));
    }

    @Test
    void testRemoveScoreBaseProficiency() {
        testFullClass.removeBaseProficiency("CON_SAVE");
        Assertions.assertFalse(testFullClass.getBaseProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testFullClass.getBaseProficiencies().contains(testProficiency2));
        Assertions.assertTrue(testFullClass.getBaseProficiencies().contains(testProficiency3));
    }

    @Test
    void testRemoveStringBaseProficiency() {
        testFullClass.removeBaseProficiency("Short Swords");
        Assertions.assertFalse(testFullClass.getBaseProficiencies().contains(testProficiency3));
        Assertions.assertTrue(testFullClass.getBaseProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testFullClass.getBaseProficiencies().contains(testProficiency2));
    }

    @Test
    void testRemoveDuplicateScoreBaseProficiency() {
        Proficiency proficiencyTest4 = new Proficiency(ScoreType.CON_SAVE, new BigDecimal("4"));
        testFullClass.addBaseProficiency(proficiencyTest4);
        testFullClass.removeBaseProficiency("CON_SAVE");
        Assertions.assertFalse(testFullClass.getBaseProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testFullClass.getBaseProficiencies().contains(testProficiency2));
        Assertions.assertTrue(testFullClass.getBaseProficiencies().contains(proficiencyTest4));
    }

    @Test
    void testRemoveDuplicateObjectBaseProficiency() {
        testFullClass.addBaseProficiency(testProficiency1);
        testFullClass.removeBaseProficiency("CON_SAVE");
        Assertions.assertTrue(testFullClass.getBaseProficiencies().contains(testProficiency1));
        Assertions.assertNotEquals(testProficiency1, testFullClass.getBaseProficiencies().get(0));
    }

    @Test
    void testAddOneMultiClassProficiency() {
        testEmptyClass.addMultiClassProficiency(testProficiency1);
        Assertions.assertTrue(testEmptyClass.getMultiClassProficiencies().contains(testProficiency1));
    }

    @Test
    void testAddManyMultiClassProficiency() {
        testEmptyClass.addMultiClassProficiency(testProficiency1);
        testEmptyClass.addMultiClassProficiency(testProficiency2);
        Assertions.assertTrue(testEmptyClass.getMultiClassProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testEmptyClass.getMultiClassProficiencies().contains(testProficiency2));
    }

    @Test
    void testRemoveScoreMultiClassProficiency() {
        testFullClass.removeMultiClassProficiency("CON_SAVE");
        Assertions.assertFalse(testFullClass.getMultiClassProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testFullClass.getMultiClassProficiencies().contains(testProficiency2));
        Assertions.assertTrue(testFullClass.getMultiClassProficiencies().contains(testProficiency3));
    }

    @Test
    void testRemoveStringMultiClassProficiency() {
        testFullClass.removeMultiClassProficiency("Short Swords");
        Assertions.assertFalse(testFullClass.getMultiClassProficiencies().contains(testProficiency3));
        Assertions.assertTrue(testFullClass.getMultiClassProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testFullClass.getMultiClassProficiencies().contains(testProficiency2));
    }

    @Test
    void testRemoveDuplicateScoreMultiClassProficiency() {
        Proficiency proficiencyTest4 = new Proficiency(ScoreType.CON_SAVE, new BigDecimal("4"));
        testFullClass.addMultiClassProficiency(proficiencyTest4);
        testFullClass.removeMultiClassProficiency("CON_SAVE");
        Assertions.assertFalse(testFullClass.getMultiClassProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testFullClass.getMultiClassProficiencies().contains(testProficiency2));
        Assertions.assertTrue(testFullClass.getMultiClassProficiencies().contains(proficiencyTest4));
    }

    @Test
    void testRemoveDuplicateObjectMultiClassProficiency() {
        testFullClass.addMultiClassProficiency(testProficiency1);
        testFullClass.removeMultiClassProficiency("CON_SAVE");
        Assertions.assertTrue(testFullClass.getMultiClassProficiencies().contains(testProficiency1));
        Assertions.assertNotEquals(testProficiency1, testFullClass.getMultiClassProficiencies().get(0));
    }

    @Test
    void testAddOneFeature() {
        testEmptyClass.addFeature(testFeature1);
        Assertions.assertEquals(1, testEmptyClass.getFeatures().size());
        Assertions.assertEquals(1, testEmptyClass.getFeatures().get(0).getFeatures().size());
        Assertions.assertTrue(testEmptyClass.getFeatures().get(0).getFeatures().contains(testFeature1));
    }

    @Test
    void testAddOneFeatureSelectLevel() {
        testEmptyClass.addFeature(testFeature1, 2);
        Assertions.assertEquals(2, testEmptyClass.getFeatures().size());
        Assertions.assertTrue(testEmptyClass.getFeatures().get(0).getFeatures().isEmpty());
        Assertions.assertEquals(1, testEmptyClass.getFeatures().get(1).getFeatures().size());
        Assertions.assertTrue(testEmptyClass.getFeatures().get(1).getFeatures().contains(testFeature1));
    }

    @Test
    void testAddManyFeatureOneLevel() {
        testEmptyClass.addFeature(testFeature1, 1);
        testEmptyClass.addFeature(testFeature2, 1);
        Assertions.assertEquals(1, testEmptyClass.getFeatures().size());
        Assertions.assertEquals(2, testEmptyClass.getFeatures().get(0).getFeatures().size());
        Assertions.assertTrue(testEmptyClass.getFeatures().get(0).getFeatures().contains(testFeature1));
        Assertions.assertTrue(testEmptyClass.getFeatures().get(0).getFeatures().contains(testFeature2));
    }

    @Test
    void testAddManyFeatureDifferentLevel() {
        testEmptyClass.addFeature(testFeature1, 1);
        testEmptyClass.addFeature(testFeature2, 2);
        Assertions.assertEquals(2, testEmptyClass.getFeatures().size());
        Assertions.assertEquals(1, testEmptyClass.getFeatures().get(0).getFeatures().size());
        Assertions.assertEquals(1, testEmptyClass.getFeatures().get(1).getFeatures().size());
        Assertions.assertTrue(testEmptyClass.getFeatures().get(0).getFeatures().contains(testFeature1));
        Assertions.assertTrue(testEmptyClass.getFeatures().get(1).getFeatures().contains(testFeature2));
    }

    @Test
    void testRemoveFeature() {
        testFullClass.removeFeature("Test Feature 1");
        Assertions.assertEquals(1, testFullClass.getFeatures().size());
        Assertions.assertEquals(1, testFullClass.getFeatures().get(0).getFeatures().size());
        Assertions.assertFalse(testFullClass.getFeatures().get(0).getFeatures().contains(testFeature1));
        Assertions.assertTrue(testFullClass.getFeatures().get(0).getFeatures().contains(testFeature2));
    }

    @Test
    void testRemoveFeatureSelectLevel() {
        testFullClass.addFeature(testFeature1, 2);
        testFullClass.addFeature(testFeature2, 2);
        Assertions.assertEquals(2, testFullClass.getFeatures().get(1).getFeatures().size());
        testFullClass.removeFeature("Test Feature 2", 2);
        Assertions.assertEquals(2, testFullClass.getFeatures().size());
        Assertions.assertEquals(2, testFullClass.getFeatures().get(0).getFeatures().size());
        Assertions.assertEquals(1, testFullClass.getFeatures().get(1).getFeatures().size());
        Assertions.assertFalse(testFullClass.getFeatures().get(1).getFeatures().contains(testFeature2));
        Assertions.assertTrue(testFullClass.getFeatures().get(1).getFeatures().contains(testFeature1));
    }

    @Test
    void testRemoveDuplicateFeature() {
        testFullClass.addFeature(testFeature1, 2);
        testFullClass.addFeature(testFeature2, 2);
        Assertions.assertEquals(2, testFullClass.getFeatures().get(1).getFeatures().size());
        testFullClass.removeFeature("Test Feature 2");
        Assertions.assertEquals(2, testFullClass.getFeatures().size());
        Assertions.assertEquals(2, testFullClass.getFeatures().get(0).getFeatures().size());
        Assertions.assertEquals(1, testFullClass.getFeatures().get(1).getFeatures().size());
        Assertions.assertFalse(testFullClass.getFeatures().get(1).getFeatures().contains(testFeature2));
        Assertions.assertTrue(testFullClass.getFeatures().get(1).getFeatures().contains(testFeature1));
    }

    @Test
    void testGetFeaturesLevelled() {
        testEmptyClass.addFeature(testFeature1, 2);
        Assertions.assertEquals(2, testEmptyClass.getFeatures().size());
        Assertions.assertEquals(1, testEmptyClass.getAllFeaturesLevelled().size());
        Assertions.assertEquals(0, testEmptyClass.getAllFeaturesLevelled().get(0).getFeatures().size());
        testEmptyClass.levelUp();
        Assertions.assertEquals(2, testEmptyClass.getAllFeaturesLevelled().size());
        Assertions.assertTrue(testEmptyClass.getAllFeaturesLevelled().get(1).getFeatures().contains(testFeature1));
    }

    @Test
    void testAddOneSubClass() {
        testEmptyClass.addSubClass(testSubClass1);
        Assertions.assertEquals(1, testEmptyClass.getSubClasses().size());
        Assertions.assertTrue(testEmptyClass.getSubClasses().contains(testSubClass1));
    }

    @Test
    void testAddManySubClass() {
        testEmptyClass.addSubClass(testSubClass1);
        testEmptyClass.addSubClass(testSubClass2);
        Assertions.assertEquals(2, testEmptyClass.getSubClasses().size());
        Assertions.assertTrue(testEmptyClass.getSubClasses().contains(testSubClass1));
        Assertions.assertTrue(testEmptyClass.getSubClasses().contains(testSubClass2));
    }

    @Test
    void testSelectSubClass() {
        Assertions.assertFalse(testEmptyClass.selectSubclass(0));
        testEmptyClass.setLevel(3);
        Assertions.assertFalse(testEmptyClass.selectSubclass(0));
        testEmptyClass.addSubClass(testSubClass1);
        Assertions.assertTrue(testEmptyClass.selectSubclass(0));
        Assertions.assertEquals(testSubClass1, testEmptyClass.getSubClass());
    }

    @Test
    void testRemoveSubClass() {
        testFullClass.removeSubClass("Test Subclass 1");
        Assertions.assertEquals(1, testFullClass.getSubClasses().size());
        Assertions.assertTrue(testFullClass.getSubClasses().contains(testSubClass2));
        Assertions.assertFalse(testFullClass.getSubClasses().contains(testSubClass1));
    }

    @Test
    void testRemoveDuplicateNameSubClass() {
        CharClass testSubClass3 = new CharClass("Test Subclass 1", new ArrayList<>());
        testFullClass.addSubClass(testSubClass3);
        testFullClass.removeSubClass("Test Subclass 1");
        Assertions.assertEquals(2, testFullClass.getSubClasses().size());
        Assertions.assertTrue(testFullClass.getSubClasses().contains(testSubClass2));
        Assertions.assertFalse(testFullClass.getSubClasses().contains(testSubClass1));
        Assertions.assertTrue(testFullClass.getSubClasses().contains(testSubClass3));
    }

    @Test
    void testRemoveDuplicateObjectSubClass() {
        testFullClass.addSubClass(testSubClass1);
        testFullClass.removeSubClass("Test Subclass 1");
        Assertions.assertEquals(2, testFullClass.getSubClasses().size());
        Assertions.assertTrue(testFullClass.getSubClasses().contains(testSubClass1));
        Assertions.assertTrue(testFullClass.getSubClasses().contains(testSubClass2));
        Assertions.assertFalse(testFullClass.getSubClasses().get(0).equals(testSubClass1));
        Assertions.assertTrue(testFullClass.getSubClasses().get(1).equals(testSubClass1));
    }

    @Test
    void testRemoveSelectedSubClass() {
        testFullClass.selectSubclass(1);
        testFullClass.removeSubClass("Test Subclass 2");
        Assertions.assertNull(testFullClass.getSubClass());
    }

    @Test
    void testGetAllFeatureScoreMods() {
        HashMap<ScoreType, Modifier> scoreModMap1 = new HashMap<>();
        scoreModMap1.put(ScoreType.CON_SAVE, new Modifier(ModifierType.BASE, BigDecimal.ZERO));
        HashMap<ScoreType, Modifier> scoreModMap2 = new HashMap<>();
        scoreModMap2.put(ScoreType.DEX_SAVE, new Modifier(ModifierType.BASE, BigDecimal.ONE));
        testEmptyClass.addFeature(new Feature("Test Score Feature 1", ScoreType.CON_SAVE,
                new Modifier(ModifierType.BASE, BigDecimal.ZERO)), 2);
        testSubClass1.addFeature(new Feature("Test Score Feature 2", ScoreType.DEX_SAVE,
                new Modifier(ModifierType.BASE, BigDecimal.ONE)));
        testEmptyClass.addSubClass(testSubClass1);

        Assertions.assertTrue(testEmptyClass.getAllFeatureScoreMods().isEmpty());

        testEmptyClass.levelUp();
        Assertions.assertEquals(1, testEmptyClass.getAllFeatureScoreMods().size());
        Assertions.assertTrue(testEmptyClass.getAllFeatureScoreMods().contains(scoreModMap1));

        testEmptyClass.setLevel(testEmptyClass.getSubClassLevel());
        Assertions.assertEquals(1, testEmptyClass.getAllFeatureScoreMods().size());
        Assertions.assertTrue(testEmptyClass.getAllFeatureScoreMods().contains(scoreModMap1));

        testEmptyClass.selectSubclass(0);
        Assertions.assertEquals(2, testEmptyClass.getAllFeatureScoreMods().size());
        Assertions.assertTrue(testEmptyClass.getAllFeatureScoreMods().contains(scoreModMap2));
    }

    @Test
    void testGetAllProficiencyScoreMods() {
        Proficiency testProficiency4 = new Proficiency(ScoreType.CON_SAVE, new BigDecimal("0.5"));
        testEmptyClass.addFeature(new Feature("Test Proficiency Feature 4", testProficiency4), 2);
        testSubClass1.addBaseProficiency(testProficiency1);
        testEmptyClass.addSubClass(testSubClass1);
        testEmptyClass.addBaseProficiency(testProficiency2);

        Assertions.assertEquals(1, testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).size());
        Assertions.assertTrue(testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency2.generateScoreMap(BigDecimal.ONE)));

        Assertions.assertTrue(testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE, false).isEmpty());

        testEmptyClass.levelUp();
        Assertions.assertEquals(2, testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).size());
        Assertions.assertTrue(testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency4.generateScoreMap(BigDecimal.ONE)));
        Assertions.assertTrue(testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency2.generateScoreMap(BigDecimal.ONE)));

        testEmptyClass.setLevel(testEmptyClass.getSubClassLevel());
        Assertions.assertEquals(2, testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).size());
        Assertions.assertTrue(testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency4.generateScoreMap(BigDecimal.ONE)));
        Assertions.assertTrue(testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency2.generateScoreMap(BigDecimal.ONE)));

        testEmptyClass.selectSubclass(0);
        Assertions.assertEquals(3, testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).size());
        Assertions.assertTrue(testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency1.generateScoreMap(BigDecimal.ONE)));
        Assertions.assertTrue(testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency4.generateScoreMap(BigDecimal.ONE)));
        Assertions.assertTrue(testEmptyClass.getAllProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency2.generateScoreMap(BigDecimal.ONE)));
    }

    @Test
    void testRollHitPoints() {
        testEmptyClass.rollHitPoints();
        Assertions.assertEquals(1, testEmptyClass.getRolledHitPoints().size());
        testEmptyClass.levelUp();
        testEmptyClass.rollHitPoints();
        Assertions.assertEquals(2, testEmptyClass.getRolledHitPoints().size());
    }

    @Test
    void testUndoHitPointRoll() {
        testEmptyClass.levelUp();
        testEmptyClass.rollHitPoints();
        Assertions.assertEquals(2, testEmptyClass.getRolledHitPoints().size());
        testEmptyClass.undoHitPointRoll();
        Assertions.assertEquals(1, testEmptyClass.getRolledHitPoints().size());
    }
}
