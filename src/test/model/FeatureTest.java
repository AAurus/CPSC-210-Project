package model;

import enums.FeatureType;
import enums.ModifierType;
import enums.ScoreType;
import enums.StatType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.ListOfHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeatureTest {
    /// A series of tests for the Feature class.
    //private Feature testFeature;
    private Feature testScoreFeature, testStatFeature, testProficiencyFeature, testAttackFeature, testMultiFeature,
            testChoiceFeature, testMultiChoiceFeature, testDescFeature, testMultiLevelFeature,
            testBranchChoiceFeature, testOmniFeature;

    private Modifier testMod = new Modifier(ModifierType.BASE, BigDecimal.ZERO);
    private HashMap<ScoreType, Modifier> testScoreMap = new HashMap<>();
    private HashMap<StatType, Modifier> testStatMap = new HashMap<>();
    private Proficiency testProficiency = new Proficiency(ScoreType.CON_SAVE, BigDecimal.ZERO);
    private Attack testAttack = new Attack();
    private Feature testSubFeature1 = new Feature("Test SubFeature 1", "description");
    private Feature testSubFeature2 = new Feature("Test SubFeature 2", "lorem ipsum");
    private ArrayList<Feature> testFeatureList1 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        testScoreFeature = new Feature("Test Score Feature", ScoreType.CON_CHECK, testMod);
        testScoreMap.put(ScoreType.CON_CHECK, testMod);

        testStatFeature = new Feature("Test Stat Feature", StatType.MAX_HIT_POINTS, testMod);
        testStatMap.put(StatType.MAX_HIT_POINTS, testMod);

        testProficiencyFeature = new Feature("Test Proficiency Feature", testProficiency);

        testAttackFeature = new Feature("Test Attack Feature", testAttack);
        testFeatureList1.add(testSubFeature1);
        testFeatureList1.add(testSubFeature2);

        testMultiFeature = new Feature("Test Multi Feature", testFeatureList1);
        testChoiceFeature = new Feature("Test Choice Feature", testFeatureList1, true);
        testMultiChoiceFeature = new Feature("Test Multi-Choice Feature", testFeatureList1, true, 2);

        testDescFeature = new Feature("Test Description Feature", "Test Description");

        testMultiLevelFeature = new Feature("Test Multi Level Branch Feature",
                new ArrayList<>(ListOfHelper.listOf(testDescFeature, testMultiFeature, testChoiceFeature)));

        testBranchChoiceFeature = new Feature("Test Choice Feature With Branches",
                new ArrayList<>(ListOfHelper.listOf(testChoiceFeature, testMultiChoiceFeature)), true);

        testOmniFeature = new Feature("Test Feature With Everything",
                new ArrayList<>(ListOfHelper.listOf(testScoreFeature, testStatFeature, testProficiencyFeature, testAttackFeature,
                        testMultiFeature, testDescFeature)));
    }

    @Test
    void testScoreFeatureConstruct() {
        Assertions.assertEquals(FeatureType.SCORE, testScoreFeature.getType());
        Assertions.assertEquals(testScoreMap, testScoreFeature.getScoreMod());

        Assertions.assertFalse(testScoreFeature.isChoice());
        Assertions.assertEquals(0, testScoreFeature.getChoiceCount());
        Assertions.assertNull(testScoreFeature.getChoices());
        Assertions.assertNull(testScoreFeature.getStatMod());
        Assertions.assertNull(testScoreFeature.getProficiency());
        Assertions.assertNull(testScoreFeature.getFeatures());
        Assertions.assertNull(testScoreFeature.getAttack());
        Assertions.assertEquals("", testScoreFeature.getDescription());
    }

    @Test
    void testStatFeatureConstruct() {
        Assertions.assertEquals(FeatureType.STAT, testStatFeature.getType());
        Assertions.assertEquals(testStatMap, testStatFeature.getStatMod());

        Assertions.assertFalse(testStatFeature.isChoice());
        Assertions.assertEquals(0, testStatFeature.getChoiceCount());
        Assertions.assertNull(testStatFeature.getChoices());
        Assertions.assertNull(testStatFeature.getScoreMod());
        Assertions.assertNull(testStatFeature.getProficiency());
        Assertions.assertNull(testStatFeature.getFeatures());
        Assertions.assertNull(testStatFeature.getAttack());
        Assertions.assertEquals("", testStatFeature.getDescription());
    }

    @Test
    void testProficiencyFeatureConstruct() {
        Assertions.assertEquals(FeatureType.PROFICIENCY, testProficiencyFeature.getType());
        Assertions.assertEquals(testProficiency, testProficiencyFeature.getProficiency());

        Assertions.assertFalse(testProficiencyFeature.isChoice());
        Assertions.assertEquals(0, testProficiencyFeature.getChoiceCount());
        Assertions.assertNull(testProficiencyFeature.getChoices());
        Assertions.assertNull(testProficiencyFeature.getScoreMod());
        Assertions.assertNull(testProficiencyFeature.getStatMod());
        Assertions.assertNull(testProficiencyFeature.getFeatures());
        Assertions.assertNull(testProficiencyFeature.getAttack());
        Assertions.assertEquals("", testProficiencyFeature.getDescription());
    }

    @Test
    void testAttackFeatureConstruct() {
        Assertions.assertEquals(FeatureType.ATTACK, testAttackFeature.getType());
        Assertions.assertEquals(testAttack, testAttackFeature.getAttack());

        Assertions.assertFalse(testAttackFeature.isChoice());
        Assertions.assertEquals(0, testAttackFeature.getChoiceCount());
        Assertions.assertNull(testAttackFeature.getChoices());
        Assertions.assertNull(testAttackFeature.getScoreMod());
        Assertions.assertNull(testAttackFeature.getStatMod());
        Assertions.assertNull(testAttackFeature.getProficiency());
        Assertions.assertNull(testAttackFeature.getFeatures());
        Assertions.assertEquals("", testAttackFeature.getDescription());
    }

    @Test
    void testMultiFeatureConstruct() {
        Assertions.assertEquals(FeatureType.MULTI, testMultiFeature.getType());
        Assertions.assertEquals(testFeatureList1, testMultiFeature.getFeatures());

        Assertions.assertFalse(testMultiFeature.isChoice());
        Assertions.assertEquals(0, testMultiFeature.getChoiceCount());
        Assertions.assertNull(testMultiFeature.getChoices());
        Assertions.assertNull(testMultiFeature.getScoreMod());
        Assertions.assertNull(testMultiFeature.getStatMod());
        Assertions.assertNull(testMultiFeature.getProficiency());
        Assertions.assertNull(testMultiFeature.getAttack());
        Assertions.assertEquals("", testMultiFeature.getDescription());
    }

    @Test
    void testChooseFeatureConstruct() {
        Assertions.assertEquals(FeatureType.MULTI, testChoiceFeature.getType());
        Assertions.assertEquals(testFeatureList1, testChoiceFeature.getFeatures());

        Assertions.assertTrue(testChoiceFeature.isChoice());
        Assertions.assertEquals(Feature.DEFAULT_MAXIMUM_CHOICES, testChoiceFeature.getChoiceCount());
        Assertions.assertTrue(testChoiceFeature.getChoices().isEmpty());

        Assertions.assertNull(testChoiceFeature.getScoreMod());
        Assertions.assertNull(testChoiceFeature.getStatMod());
        Assertions.assertNull(testChoiceFeature.getProficiency());
        Assertions.assertNull(testChoiceFeature.getAttack());
        Assertions.assertEquals("", testChoiceFeature.getDescription());
    }

    @Test
    void testMultiChooseFeatureConstruct() {
        Assertions.assertEquals(FeatureType.MULTI, testMultiChoiceFeature.getType());
        Assertions.assertEquals(testFeatureList1, testMultiChoiceFeature.getFeatures());

        Assertions.assertTrue(testMultiChoiceFeature.isChoice());
        Assertions.assertEquals(2, testMultiChoiceFeature.getChoiceCount());
        Assertions.assertTrue(testMultiChoiceFeature.getChoices().isEmpty());

        Assertions.assertNull(testMultiChoiceFeature.getScoreMod());
        Assertions.assertNull(testMultiChoiceFeature.getStatMod());
        Assertions.assertNull(testMultiChoiceFeature.getProficiency());
        Assertions.assertNull(testMultiChoiceFeature.getAttack());
        Assertions.assertEquals("", testMultiChoiceFeature.getDescription());
    }

    @Test
    void testDescriptionName() {
        Assertions.assertEquals("Test Description Feature", testDescFeature.getName());
        Assertions.assertEquals("Test Description", testDescFeature.getDescription());
    }

    @Test
    void testChooseOneFeature() {
        testChoiceFeature.chooseFeature(1);
        Assertions.assertEquals(1, testChoiceFeature.getChoices().size());
        Assertions.assertTrue(testChoiceFeature.getChoices().contains(1));
        Assertions.assertEquals(1, testChoiceFeature.getChosenFeatures().size());
        Assertions.assertTrue(testChoiceFeature.getChosenFeatures().contains(testSubFeature2));
    }

    @Test
    void testChooseOverrideFeature() {
        testChoiceFeature.chooseFeature(1);
        Assertions.assertEquals(1, testChoiceFeature.getChoices().size());
        Assertions.assertTrue(testChoiceFeature.getChoices().contains(1));
        Assertions.assertEquals(1, testChoiceFeature.getChosenFeatures().size());
        Assertions.assertTrue(testChoiceFeature.getChosenFeatures().contains(testSubFeature2));

        testChoiceFeature.chooseFeature(0);
        Assertions.assertEquals(1, testChoiceFeature.getChoices().size());
        Assertions.assertTrue(testChoiceFeature.getChoices().contains(0));
        Assertions.assertEquals(1, testChoiceFeature.getChosenFeatures().size());
        Assertions.assertTrue(testChoiceFeature.getChosenFeatures().contains(testSubFeature1));
    }

    @Test
    void testChooseManyFeature() {
        testMultiChoiceFeature.chooseFeature(0);
        testMultiChoiceFeature.chooseFeature(1);
        Assertions.assertEquals(2, testMultiChoiceFeature.getChoices().size());
        Assertions.assertTrue(testMultiChoiceFeature.getChoices().contains(0));
        Assertions.assertTrue(testMultiChoiceFeature.getChoices().contains(1));
        Assertions.assertEquals(2, testMultiChoiceFeature.getChosenFeatures().size());
        Assertions.assertTrue(testMultiChoiceFeature.getChosenFeatures().contains(testSubFeature1));
        Assertions.assertTrue(testMultiChoiceFeature.getChosenFeatures().contains(testSubFeature2));
    }

    @Test
    void testGetAllSurfaceFeaturesOfType() {
        List<Feature> result = Feature.getAllSurfaceFeaturesOfType(testMultiLevelFeature.getFeatures(),
                FeatureType.ETC);
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(testDescFeature));
    }

    @Test
    void testGetAllFeaturesOfType() {
        List<Feature> result = Feature.getAllFeaturesOfType(ListOfHelper.listOf(testMultiLevelFeature),
                FeatureType.ETC);
        Assertions.assertEquals(5, result.size());
        Assertions.assertTrue(result.contains(testDescFeature));
        Assertions.assertTrue(result.contains(testSubFeature1));
        Assertions.assertTrue(result.contains(testSubFeature2));
    }

    @Test
    void testGetAllReachableFeaturesOfType() {
        List<Feature> result = Feature.getAllReachableFeaturesOfType(ListOfHelper.listOf(testMultiLevelFeature),
                FeatureType.ETC);
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(testDescFeature));
        Assertions.assertTrue(result.contains(testSubFeature1));
        Assertions.assertTrue(result.contains(testSubFeature2));
    }

    @Test
    void testGetAllFeaturesTypeValidity() {
        List<Feature> result = Feature.getAllSurfaceFeaturesOfType(testOmniFeature.getFeatures(),
                FeatureType.SCORE);
        Assertions.assertEquals(1, result.size());
        result = Feature.getAllSurfaceFeaturesOfType(testOmniFeature.getFeatures(),
                FeatureType.STAT);
        Assertions.assertEquals(1, result.size());
        result = Feature.getAllSurfaceFeaturesOfType(testOmniFeature.getFeatures(),
                FeatureType.PROFICIENCY);
        Assertions.assertEquals(1, result.size());
        result = Feature.getAllSurfaceFeaturesOfType(testOmniFeature.getFeatures(),
                FeatureType.ATTACK);
        Assertions.assertEquals(1, result.size());
        result = Feature.getAllSurfaceFeaturesOfType(testOmniFeature.getFeatures(),
                FeatureType.MULTI);
        Assertions.assertEquals(1, result.size());
        result = Feature.getAllSurfaceFeaturesOfType(testOmniFeature.getFeatures(),
                FeatureType.ETC);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testGetAllSurfaceFeaturesWithChoice() {
        List<Feature> result = Feature.getAllSurfaceFeaturesWithChoice(ListOfHelper.listOf(testBranchChoiceFeature));
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(testBranchChoiceFeature));
    }

    @Test
    void testGetAllFeaturesWithChoice() {
        List<Feature> result = Feature.getAllFeaturesWithChoice(ListOfHelper.listOf(testBranchChoiceFeature));
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(testBranchChoiceFeature));
        Assertions.assertTrue(result.contains(testChoiceFeature));
        Assertions.assertTrue(result.contains(testMultiChoiceFeature));
    }

    @Test
    void testGetAllReachableFeaturesWithChoice() {
        testBranchChoiceFeature.chooseFeature(0);
        List<Feature> result = Feature.getAllReachableFeaturesWithChoice(ListOfHelper.listOf(testBranchChoiceFeature));
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(testBranchChoiceFeature));
        Assertions.assertTrue(result.contains(testChoiceFeature));
    }

    @Test
    void testGetAllScoreModifiers() {
        Feature testChoiceFeatureWithScore = new Feature("Test Choice Score Feature",
                ListOfHelper.listOf(testScoreFeature, testOmniFeature), true);
        testChoiceFeatureWithScore.chooseFeature(0);
        Feature multiLevelScoreFeature = new Feature("Test Multi-Level Score Feature",
                ListOfHelper.listOf(testScoreFeature, testChoiceFeatureWithScore, testOmniFeature));
        List<HashMap<ScoreType, Modifier>> result = multiLevelScoreFeature.getAllScoreModifiers();
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(testScoreMap));
    }

    @Test
    void testGetAllStatModifiers() {
        Feature testChoiceFeatureWithStat = new Feature("Test Choice Stat Feature",
                ListOfHelper.listOf(testStatFeature, testOmniFeature), true);
        testChoiceFeatureWithStat.chooseFeature(0);
        Feature multiLevelStatFeature = new Feature("Test Multi-Level Stat Feature",
                ListOfHelper.listOf(testStatFeature, testChoiceFeatureWithStat, testOmniFeature));
        List<HashMap<StatType, Modifier>> result = multiLevelStatFeature.getAllStatModifiers();
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(testStatMap));
    }

    @Test
    void testGetAllProficiencyModifiers() {
        Feature testChoiceFeatureWithProf = new Feature("Test Choice Proficiency Feature",
                ListOfHelper.listOf(testProficiencyFeature, testOmniFeature), true);
        testChoiceFeatureWithProf.chooseFeature(0);
        Feature multiLevelStatFeature = new Feature("Test Multi-Level Proficiency Feature",
                ListOfHelper.listOf(testProficiencyFeature, testChoiceFeatureWithProf, testOmniFeature));
        List<HashMap<ScoreType, Modifier>> result = multiLevelStatFeature.getAllProficiencyModifiers(BigDecimal.ONE);
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(testProficiency.generateScoreMap(BigDecimal.ONE)));
    }
}
