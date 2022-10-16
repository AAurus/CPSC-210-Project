package model;

import enums.ModifierType;
import enums.ScoreType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.ListOfHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharRaceTest {
    /// A series of tests for the CharRace Race.
    private CharRace testEmptyRace;
    private CharRace testFullRace;

    private HashMap<ScoreType, Modifier> testScoreMap = new HashMap<>();

    private Feature testFeature1 = new Feature("Test Feature 1", "Test 1");
    private Feature testFeature2 = new Feature("Test Feature 2", "Test 2");

    private Proficiency testProficiency1 = new Proficiency(ScoreType.CON_SAVE, new BigDecimal("0.2"));
    private Proficiency testProficiency2 = new Proficiency(ScoreType.DEX_SAVE, new BigDecimal("4.5"));
    private Proficiency testProficiency3 = new Proficiency("Short Swords", new BigDecimal("2"));

    private CharRace testSubRace1 = new CharRace("Test SubRace 1");
    private CharRace testSubRace2 = new CharRace("Test SubRace 2");

    private ArrayList<Proficiency> proficiencyList = new ArrayList<>(ListOfHelper.listOf(testProficiency1,
                                                                             testProficiency2,
                                                                             testProficiency3));
    private ArrayList<String> languageList = new ArrayList<>(ListOfHelper.listOf("Common", "TestLang"));
    private ArrayList<Feature> featureList = new ArrayList<>(ListOfHelper.listOf(testFeature1, testFeature2));
    @BeforeEach
    void runBefore() {
        testScoreMap.put(ScoreType.INTELLIGENCE, new Modifier(2));
        testScoreMap.put(ScoreType.STRENGTH, new Modifier(1));

        testEmptyRace = new CharRace("Test Empty Race");
        testFullRace = new CharRace("Test Full Race", testScoreMap, proficiencyList, languageList, featureList,
                new ArrayList<>(ListOfHelper.listOf(testSubRace1, testSubRace2)), "Test 2");
    }

    @Test
    void testConstructor() {
        Assertions.assertEquals("Test Empty Race", testEmptyRace.getName());
        Assertions.assertEquals("", testEmptyRace.getDescription());
        Assertions.assertTrue(testEmptyRace.getFeatures().isEmpty());
        Assertions.assertTrue(testEmptyRace.getProficiencies().isEmpty());
        Assertions.assertTrue(testEmptyRace.getLanguages().isEmpty());
    }

    @Test
    void testFullConstructor() {
        Assertions.assertEquals("Test Full Race", testFullRace.getName());
        Assertions.assertEquals("Test 2", testFullRace.getDescription());
        Assertions.assertEquals(proficiencyList, testFullRace.getProficiencies());
        Assertions.assertEquals(languageList, testFullRace.getLanguages());
        Assertions.assertEquals(featureList, testFullRace.getFeatures());
    }

    @Test
    void setOneScore() {
        testEmptyRace.setScoreMod(ScoreType.CON_SAVE, new Modifier(2));
        Assertions.assertEquals(new Modifier(2), testEmptyRace.getScores().get(ScoreType.CON_SAVE));
    }

    @Test
    void setManyScores() {
        testEmptyRace.setScoreMod(ScoreType.CON_SAVE, new Modifier(2));
        testEmptyRace.setScoreMod(ScoreType.DEX_SAVE, new Modifier(1));
        Assertions.assertEquals(new Modifier(2), testEmptyRace.getScores().get(ScoreType.CON_SAVE));
        Assertions.assertEquals(new Modifier(1), testEmptyRace.getScores().get(ScoreType.DEX_SAVE));
    }

    @Test
    void resetScores() {
        testFullRace.resetScoreMod(ScoreType.STRENGTH);
        Assertions.assertNull(testEmptyRace.getScores().get(ScoreType.STRENGTH));
    }

    @Test
    void testAddOneProficiency() {
        testEmptyRace.addProficiency(testProficiency1);
        Assertions.assertTrue(testEmptyRace.getProficiencies().contains(testProficiency1));
    }

    @Test
    void testAddManyProficiency() {
        testEmptyRace.addProficiency(testProficiency1);
        testEmptyRace.addProficiency(testProficiency2);
        Assertions.assertTrue(testEmptyRace.getProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testEmptyRace.getProficiencies().contains(testProficiency2));
    }

    @Test
    void testRemoveScoreProficiency() {
        testFullRace.removeProficiency("CON_SAVE");
        Assertions.assertFalse(testFullRace.getProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testFullRace.getProficiencies().contains(testProficiency2));
        Assertions.assertTrue(testFullRace.getProficiencies().contains(testProficiency3));
    }

    @Test
    void testRemoveStringProficiency() {
        testFullRace.removeProficiency("Short Swords");
        Assertions.assertFalse(testFullRace.getProficiencies().contains(testProficiency3));
        Assertions.assertTrue(testFullRace.getProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testFullRace.getProficiencies().contains(testProficiency2));
    }

    @Test
    void testRemoveDuplicateScoreProficiency() {
        Proficiency proficiencyTest4 = new Proficiency(ScoreType.CON_SAVE, new BigDecimal("4"));
        testFullRace.addProficiency(proficiencyTest4);
        testFullRace.removeProficiency("CON_SAVE");
        Assertions.assertFalse(testFullRace.getProficiencies().contains(testProficiency1));
        Assertions.assertTrue(testFullRace.getProficiencies().contains(testProficiency2));
        Assertions.assertTrue(testFullRace.getProficiencies().contains(proficiencyTest4));
    }

    @Test
    void testRemoveDuplicateObjectProficiency() {
        testFullRace.addProficiency(testProficiency1);
        testFullRace.removeProficiency("CON_SAVE");
        Assertions.assertTrue(testFullRace.getProficiencies().contains(testProficiency1));
        Assertions.assertNotEquals(testProficiency1, testFullRace.getProficiencies().get(0));
    }

    @Test
    void testAddOneLanguage() {
        testEmptyRace.addLanguage("Elvish");
        Assertions.assertTrue(testEmptyRace.getLanguages().contains("Elvish"));
    }

    @Test
    void testAddManyLanguage() {
        testEmptyRace.addLanguage("Elvish");
        testEmptyRace.addLanguage("Java");
        Assertions.assertTrue(testEmptyRace.getLanguages().contains("Elvish"));
        Assertions.assertTrue(testEmptyRace.getLanguages().contains("Java"));
    }

    @Test
    void testRemoveLanguage() {
        testFullRace.removeLanguage("Common");
        Assertions.assertFalse(testFullRace.getLanguages().contains("Common"));
        Assertions.assertTrue(testFullRace.getLanguages().contains("TestLang"));
    }

    @Test
    void testRemoveDuplicateLanguage() {
        testFullRace.addLanguage("Common");
        Assertions.assertEquals("Common", testFullRace.getLanguages().get(0));
        testFullRace.removeLanguage("Common");
        Assertions.assertTrue(testFullRace.getLanguages().contains("Common"));
        Assertions.assertNotEquals("Common", testFullRace.getLanguages().get(0));
    }

    @Test
    void testGetAllProficienciesApplied() {
        ArrayList<HashMap<ScoreType, Modifier>> testApplyList =
                testFullRace.getAllScoreProficienciesApplied(new BigDecimal("1"));
        Assertions.assertEquals(testProficiency1.generateScoreMap(new BigDecimal("1")),
                testApplyList.get(0));
        Assertions.assertEquals(testProficiency2.generateScoreMap(new BigDecimal("1")),
                testApplyList.get(1));
    }

    @Test
    void testSetSubRaceByIndex() {
        Assertions.assertNull(testFullRace.getSubRace());
        testFullRace.selectSubRace(0);
        Assertions.assertEquals(testSubRace1, testFullRace.getSubRace());
    }

    @Test
    void testSetSubRaceByName() {
        Assertions.assertNull(testFullRace.getSubRace());
        testFullRace.selectSubRace("Test SubRace 2");
        Assertions.assertEquals(testSubRace2, testFullRace.getSubRace());
    }

    @Test
    void testResetSubRace() {
        testFullRace.selectSubRace(0);
        Assertions.assertEquals(testSubRace1, testFullRace.getSubRace());
        testFullRace.resetSubRaceSelect();
        Assertions.assertNull(testFullRace.getSubRace());
    }

    @Test
    void testAddOneSubRace() {
        testEmptyRace.addSubRace(testSubRace1);
        Assertions.assertEquals(1, testEmptyRace.getSubRaces().size());
        Assertions.assertTrue(testEmptyRace.getSubRaces().contains(testSubRace1));
    }

    @Test
    void testAddManySubRace() {
        testEmptyRace.addSubRace(testSubRace1);
        testEmptyRace.addSubRace(testSubRace2);
        Assertions.assertEquals(2, testEmptyRace.getSubRaces().size());
        Assertions.assertTrue(testEmptyRace.getSubRaces().contains(testSubRace1));
        Assertions.assertTrue(testEmptyRace.getSubRaces().contains(testSubRace2));
    }

    @Test
    void testSelectSubRace() {
        Assertions.assertFalse(testEmptyRace.selectSubRace(0));
        testEmptyRace.addSubRace(testSubRace1);
        Assertions.assertTrue(testEmptyRace.selectSubRace(0));
        Assertions.assertEquals(testSubRace1, testEmptyRace.getSubRace());
    }

    @Test
    void testRemoveSubRace() {
        testFullRace.removeSubRace("Test SubRace 1");
        Assertions.assertEquals(1, testFullRace.getSubRaces().size());
        Assertions.assertTrue(testFullRace.getSubRaces().contains(testSubRace2));
        Assertions.assertFalse(testFullRace.getSubRaces().contains(testSubRace1));
    }

    @Test
    void testRemoveDuplicateNameSubRace() {
        CharRace testSubRace3 = new CharRace("Test SubRace 1");
        testFullRace.addSubRace(testSubRace3);
        testFullRace.removeSubRace("Test SubRace 1");
        Assertions.assertEquals(2, testFullRace.getSubRaces().size());
        Assertions.assertTrue(testFullRace.getSubRaces().contains(testSubRace2));
        Assertions.assertFalse(testFullRace.getSubRaces().contains(testSubRace1));
        Assertions.assertTrue(testFullRace.getSubRaces().contains(testSubRace3));
    }

    @Test
    void testRemoveDuplicateObjectSubRace() {
        testFullRace.addSubRace(testSubRace1);
        testFullRace.removeSubRace("Test SubRace 1");
        Assertions.assertEquals(2, testFullRace.getSubRaces().size());
        Assertions.assertTrue(testFullRace.getSubRaces().contains(testSubRace1));
        Assertions.assertTrue(testFullRace.getSubRaces().contains(testSubRace2));
        Assertions.assertFalse(testFullRace.getSubRaces().get(0).equals(testSubRace1));
        Assertions.assertTrue(testFullRace.getSubRaces().get(1).equals(testSubRace1));
    }

    @Test
    void testRemoveSelectedSubRace() {
        testFullRace.selectSubRace(1);
        testFullRace.removeSubRace("Test SubRace 2");
        Assertions.assertNull(testFullRace.getSubRace());
    }

    @Test
    void testGetAllFeatureScoreMods() {
        HashMap<ScoreType, Modifier> scoreModMap1 = new HashMap<>();
        scoreModMap1.put(ScoreType.CON_SAVE, new Modifier(0));
        HashMap<ScoreType, Modifier> scoreModMap2 = new HashMap<>();
        scoreModMap2.put(ScoreType.DEX_SAVE, new Modifier(ModifierType.BASE, BigDecimal.ONE));
        testSubRace1.addFeature(new Feature("Test Score Feature 2", ScoreType.DEX_SAVE,
                new Modifier(ModifierType.BASE, BigDecimal.ONE)));
        testEmptyRace.addSubRace(testSubRace1);
        testEmptyRace.addFeature(new Feature("Test Score Feature 1", ScoreType.CON_SAVE, new Modifier(0)));

        Assertions.assertEquals(1, testEmptyRace.getAllFeatureScoreMods().size());
        Assertions.assertTrue(testEmptyRace.getAllFeatureScoreMods().contains(scoreModMap1));

        testEmptyRace.selectSubRace(0);
        Assertions.assertEquals(2, testEmptyRace.getAllFeatureScoreMods().size());
        Assertions.assertTrue(testEmptyRace.getAllFeatureScoreMods().contains(scoreModMap2));
    }

    @Test
    void testGetAllProficiencyScoreMods() {
        Proficiency testProficiency4 = new Proficiency(ScoreType.CON_SAVE, new BigDecimal("0.5"));
        testEmptyRace.addFeature(new Feature("Test Proficiency Feature 4", testProficiency4));
        testSubRace1.addProficiency(testProficiency2);
        testEmptyRace.addSubRace(testSubRace1);
        testEmptyRace.addProficiency(testProficiency1);

        Assertions.assertEquals(2, testEmptyRace.getAllScoreProficienciesApplied(BigDecimal.ONE).size());
        Assertions.assertTrue(testEmptyRace.getAllScoreProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency4.generateScoreMap(BigDecimal.ONE)));
        Assertions.assertTrue(testEmptyRace.getAllScoreProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency1.generateScoreMap(BigDecimal.ONE)));

        testEmptyRace.selectSubRace(0);
        Assertions.assertEquals(3, testEmptyRace.getAllScoreProficienciesApplied(BigDecimal.ONE).size());
        Assertions.assertTrue(testEmptyRace.getAllScoreProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency1.generateScoreMap(BigDecimal.ONE)));
        Assertions.assertTrue(testEmptyRace.getAllScoreProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency4.generateScoreMap(BigDecimal.ONE)));
        Assertions.assertTrue(testEmptyRace.getAllScoreProficienciesApplied(BigDecimal.ONE).contains(
                testProficiency2.generateScoreMap(BigDecimal.ONE)));
    }
}
