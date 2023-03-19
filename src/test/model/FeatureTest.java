package model;

import enums.FeatureType;
import enums.ModifierType;
import enums.ScoreType;
import enums.StatType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeatureTest {
    /// A series of tests for the Feature class.
    //private Feature testFeature;
    private Feature testScoreFeature, testStatFeature, testProficiencyFeature, testMultiFeature,
            testChoiceFeature, testMultiChoiceFeature, testDescFeature, testMultiLevelFeature,
            testBranchChoiceFeature, testOmniFeature;

    private Modifier testMod = new Modifier(ModifierType.BASE, BigDecimal.ZERO);
    private HashMap<ScoreType, Modifier> testScoreMap = new HashMap<>();
    private HashMap<StatType, Modifier> testStatMap = new HashMap<>();
    private Proficiency testProficiency = new Proficiency(ScoreType.CON_SAVE, BigDecimal.ZERO);
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

        testFeatureList1.add(testSubFeature1);
        testFeatureList1.add(testSubFeature2);

        testMultiFeature = new Feature("Test Multi Feature", testFeatureList1);
        testChoiceFeature = new Feature("Test Choice Feature", testFeatureList1, true);
        testMultiChoiceFeature = new Feature("Test Multi-Choice Feature", testFeatureList1, true, 2);

        testDescFeature = new Feature("Test Description Feature", "Test Description");

        testMultiLevelFeature = new Feature("Test Multi Level Branch Feature",
                                            new ArrayList<>(Utility.listOf(testDescFeature, testMultiFeature, testChoiceFeature)));

        testBranchChoiceFeature = new Feature("Test Choice Feature With Branches",
                                              new ArrayList<>(Utility.listOf(testChoiceFeature, testMultiChoiceFeature)), true);

        testOmniFeature = new Feature("Test Feature With Everything",
                                      new ArrayList<>(Utility.listOf(testScoreFeature, testStatFeature, testProficiencyFeature,
                                                                     testMultiFeature, testDescFeature)));
    }

    @AfterEach
    void clearLog() {
        EventLog.getInstance().clear();
    }

    @Test
    void testFeatureConstructorLogs() {
        boolean containsScore = false;
        boolean containsStat = false;
        boolean containsProf = false;
        boolean containsMulti = false;
        boolean containsChoice = false;
        boolean containsMChoice = false;
        boolean containsDesc = false;

        for (Event e : EventLog.getInstance()) {
            switch (e.getDescription()) {
                case "New Score feature created: Test Score Feature":
                    containsScore = true;
                    break;
                case "New Stat feature created: Test Stat Feature":
                    containsStat = true;
                    break;
                case "New Proficiency feature created: Test Proficiency Feature":
                    containsProf = true;
                    break;
                case "New Multi-Feature feature created: Test Multi Feature":
                    containsMulti = true;
                    break;
                case "New Single-Choice feature created: Test Choice Feature":
                    containsChoice = true;
                    break;
                case "New 2-Choice feature created: Test Multi-Choice Feature":
                    containsMChoice = true;
                    break;
                case "New description-only feature created: Test Description Feature":
                    containsDesc = true;
                    break;
            }
        }

        Assertions.assertTrue(containsScore);
        Assertions.assertTrue(containsStat);
        Assertions.assertTrue(containsProf);
        Assertions.assertTrue(containsMulti);
        Assertions.assertTrue(containsChoice);
        Assertions.assertTrue(containsMChoice);
        Assertions.assertTrue(containsDesc);
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
        Assertions.assertEquals("", testProficiencyFeature.getDescription());
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

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("Feature " + testSubFeature2.getName()
                                                                       + " chosen from feature "
                                                                       + testChoiceFeature.getName());
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testChooseOverrideFeature() {
        testChoiceFeature.chooseFeature(1);
        Assertions.assertEquals(1, testChoiceFeature.getChoices().size());
        Assertions.assertTrue(testChoiceFeature.getChoices().contains(1));
        Assertions.assertEquals(1, testChoiceFeature.getChosenFeatures().size());
        Assertions.assertTrue(testChoiceFeature.getChosenFeatures().contains(testSubFeature2));

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("Feature " + testSubFeature2.getName()
                                                                       + " chosen from feature "
                                                                       + testChoiceFeature.getName());
        }
        Assertions.assertTrue(containsEvent);

        testChoiceFeature.chooseFeature(0);
        Assertions.assertEquals(1, testChoiceFeature.getChoices().size());
        Assertions.assertTrue(testChoiceFeature.getChoices().contains(0));
        Assertions.assertEquals(1, testChoiceFeature.getChosenFeatures().size());
        Assertions.assertTrue(testChoiceFeature.getChosenFeatures().contains(testSubFeature1));

        containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("Feature " + testSubFeature1.getName()
                                                                       + " chosen from feature "
                                                                       + testChoiceFeature.getName());
        }
        Assertions.assertTrue(containsEvent);
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

        boolean containsEvent1 = false;
        boolean containsEvent2 = false;
        for (Event e : EventLog.getInstance()) {
            switch (e.getDescription()) {
                case "Feature Test SubFeature 1 chosen from feature Test Multi-Choice Feature":
                    containsEvent1 = true;
                    break;
                case "Feature Test SubFeature 2 chosen from feature Test Multi-Choice Feature":
                    containsEvent2 = true;
                    break;
            }
        }

        Assertions.assertTrue(containsEvent1);
        Assertions.assertTrue(containsEvent2);
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
        List<Feature> result = Feature.getAllFeaturesOfType(Utility.listOf(testMultiLevelFeature),
                                                            FeatureType.ETC);
        Assertions.assertEquals(5, result.size());
        Assertions.assertTrue(result.contains(testDescFeature));
        Assertions.assertTrue(result.contains(testSubFeature1));
        Assertions.assertTrue(result.contains(testSubFeature2));
    }

    @Test
    void testGetAllReachableFeaturesOfType() {
        List<Feature> result = Feature.getAllReachableFeaturesOfType(Utility.listOf(testMultiLevelFeature),
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
                                                     FeatureType.MULTI);
        Assertions.assertEquals(1, result.size());
        result = Feature.getAllSurfaceFeaturesOfType(testOmniFeature.getFeatures(),
                                                     FeatureType.ETC);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testGetAllSurfaceFeaturesWithChoice() {
        List<Feature> result = Feature.getAllSurfaceFeaturesWithChoice(Utility.listOf(testBranchChoiceFeature));
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(testBranchChoiceFeature));
    }

    @Test
    void testGetAllFeaturesWithChoice() {
        List<Feature> result = Feature.getAllFeaturesWithChoice(Utility.listOf(testBranchChoiceFeature));
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(testBranchChoiceFeature));
        Assertions.assertTrue(result.contains(testChoiceFeature));
        Assertions.assertTrue(result.contains(testMultiChoiceFeature));
    }

    @Test
    void testGetAllReachableFeaturesWithChoice() {
        testBranchChoiceFeature.chooseFeature(0);
        List<Feature> result = Feature.getAllReachableFeaturesWithChoice(Utility.listOf(testBranchChoiceFeature));
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(testBranchChoiceFeature));
        Assertions.assertTrue(result.contains(testChoiceFeature));
    }

    @Test
    void testGetAllScoreModifiers() {
        Feature testChoiceFeatureWithScore = new Feature("Test Choice Score Feature",
                                                         Utility.listOf(testScoreFeature, testOmniFeature), true);
        testChoiceFeatureWithScore.chooseFeature(0);
        Feature multiLevelScoreFeature = new Feature("Test Multi-Level Score Feature",
                                                     Utility.listOf(testScoreFeature, testChoiceFeatureWithScore, testOmniFeature));
        List<HashMap<ScoreType, Modifier>> result = multiLevelScoreFeature.getAllScoreModifiers();
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(testScoreMap));
    }

    @Test
    void testGetAllStatModifiers() {
        Feature testChoiceFeatureWithStat = new Feature("Test Choice Stat Feature",
                                                        Utility.listOf(testStatFeature, testOmniFeature), true);
        testChoiceFeatureWithStat.chooseFeature(0);
        Feature multiLevelStatFeature = new Feature("Test Multi-Level Stat Feature",
                                                    Utility.listOf(testStatFeature, testChoiceFeatureWithStat, testOmniFeature));
        List<HashMap<StatType, Modifier>> result = multiLevelStatFeature.getAllStatModifiers();
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(testStatMap));
    }

    @Test
    void testGetAllProficiencyModifiers() {
        Feature testChoiceFeatureWithProf = new Feature("Test Choice Proficiency Feature",
                                                        Utility.listOf(testProficiencyFeature, testOmniFeature), true);
        testChoiceFeatureWithProf.chooseFeature(0);
        Feature multiLevelStatFeature = new Feature("Test Multi-Level Proficiency Feature",
                                                    Utility.listOf(testProficiencyFeature, testChoiceFeatureWithProf, testOmniFeature));
        List<HashMap<ScoreType, Modifier>> result = multiLevelStatFeature.getAllProficiencyModifiers(BigDecimal.ONE);
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains(testProficiency.generateScoreMap(BigDecimal.ONE)));
    }

    @Test
    void testDuplicateConstructorScore() {
        Feature testDuplicationFeature = new Feature(testScoreFeature);
        Assertions.assertEquals(testScoreFeature.getType(), testDuplicationFeature.getType());
        Assertions.assertEquals(testScoreFeature.getScoreMod(), testDuplicationFeature.getScoreMod());
        Assertions.assertEquals(testScoreFeature.isChoice(), testDuplicationFeature.isChoice());
        Assertions.assertEquals(testScoreFeature.getChoiceCount(), testDuplicationFeature.getChoiceCount());
        Assertions.assertNull(testDuplicationFeature.getChoices());
        Assertions.assertEquals(testScoreFeature.getStatMod(), testDuplicationFeature.getStatMod());
        Assertions.assertEquals(testScoreFeature.getProficiency(), testDuplicationFeature.getProficiency());
        Assertions.assertEquals(testScoreFeature.getFeatures(), testDuplicationFeature.getFeatures());
        Assertions.assertEquals(testScoreFeature.getDescription(), testDuplicationFeature.getDescription());
        Assertions.assertFalse(testDuplicationFeature == testScoreFeature);
    }

    @Test
    void testDuplicateConstructorStat() {
        Feature testDuplicationFeature = new Feature(testStatFeature);
        Assertions.assertEquals(testStatFeature.getType(), testDuplicationFeature.getType());
        Assertions.assertEquals(testStatFeature.getScoreMod(), testDuplicationFeature.getScoreMod());
        Assertions.assertEquals(testStatFeature.isChoice(), testDuplicationFeature.isChoice());
        Assertions.assertEquals(testStatFeature.getChoiceCount(), testDuplicationFeature.getChoiceCount());
        Assertions.assertNull(testDuplicationFeature.getChoices());
        Assertions.assertEquals(testStatFeature.getStatMod(), testDuplicationFeature.getStatMod());
        Assertions.assertEquals(testStatFeature.getProficiency(), testDuplicationFeature.getProficiency());
        Assertions.assertEquals(testStatFeature.getFeatures(), testDuplicationFeature.getFeatures());
        Assertions.assertEquals(testStatFeature.getDescription(), testDuplicationFeature.getDescription());
        Assertions.assertFalse(testDuplicationFeature == testStatFeature);
    }

    @Test
    void testDuplicateConstructorProficiency() {
        Feature testDuplicationFeature = new Feature(testProficiencyFeature);
        Assertions.assertEquals(testProficiencyFeature.getType(), testDuplicationFeature.getType());
        Assertions.assertEquals(testProficiencyFeature.getScoreMod(), testDuplicationFeature.getScoreMod());
        Assertions.assertEquals(testProficiencyFeature.isChoice(), testDuplicationFeature.isChoice());
        Assertions.assertEquals(testProficiencyFeature.getChoiceCount(), testDuplicationFeature.getChoiceCount());
        Assertions.assertNull(testDuplicationFeature.getChoices());
        Assertions.assertEquals(testProficiencyFeature.getStatMod(), testDuplicationFeature.getStatMod());
        Assertions.assertEquals(testProficiencyFeature.getProficiency(), testDuplicationFeature.getProficiency());
        Assertions.assertEquals(testProficiencyFeature.getFeatures(), testDuplicationFeature.getFeatures());
        Assertions.assertEquals(testProficiencyFeature.getDescription(), testDuplicationFeature.getDescription());
        Assertions.assertFalse(testDuplicationFeature == testProficiencyFeature);
    }

    @Test
    void testDuplicateConstructorMulti() {
        Feature testDuplicationFeature = new Feature(testMultiFeature);
        Assertions.assertEquals(testMultiFeature.getType(), testDuplicationFeature.getType());
        Assertions.assertEquals(testMultiFeature.getScoreMod(), testDuplicationFeature.getScoreMod());
        Assertions.assertEquals(testMultiFeature.isChoice(), testDuplicationFeature.isChoice());
        Assertions.assertEquals(testMultiFeature.getChoiceCount(), testDuplicationFeature.getChoiceCount());
        Assertions.assertNull(testDuplicationFeature.getChoices());
        Assertions.assertEquals(testMultiFeature.getStatMod(), testDuplicationFeature.getStatMod());
        Assertions.assertEquals(testMultiFeature.getProficiency(), testDuplicationFeature.getProficiency());
        Assertions.assertEquals(testMultiFeature.getFeatures(), testDuplicationFeature.getFeatures());
        Assertions.assertEquals(testMultiFeature.getDescription(), testDuplicationFeature.getDescription());
        Assertions.assertFalse(testDuplicationFeature == testMultiFeature);
    }

    @Test
    void testDuplicateConstructorChoice() {
        Feature testDuplicationFeature = new Feature(testChoiceFeature);
        Assertions.assertEquals(testChoiceFeature.getType(), testDuplicationFeature.getType());
        Assertions.assertEquals(testChoiceFeature.getScoreMod(), testDuplicationFeature.getScoreMod());
        Assertions.assertEquals(testChoiceFeature.isChoice(), testDuplicationFeature.isChoice());
        Assertions.assertEquals(testChoiceFeature.getChoiceCount(), testDuplicationFeature.getChoiceCount());
        Assertions.assertEquals(new ArrayList<>(), testDuplicationFeature.getChoices());
        Assertions.assertEquals(testChoiceFeature.getStatMod(), testDuplicationFeature.getStatMod());
        Assertions.assertEquals(testChoiceFeature.getProficiency(), testDuplicationFeature.getProficiency());
        Assertions.assertEquals(testChoiceFeature.getFeatures(), testDuplicationFeature.getFeatures());
        Assertions.assertEquals(testChoiceFeature.getDescription(), testDuplicationFeature.getDescription());
        Assertions.assertFalse(testDuplicationFeature == testChoiceFeature);
    }

    @Test
    void testDuplicateConstructorDesc() {
        Feature testDuplicationFeature = new Feature(testDescFeature);
        Assertions.assertEquals(testDescFeature.getType(), testDuplicationFeature.getType());
        Assertions.assertEquals(testDescFeature.getScoreMod(), testDuplicationFeature.getScoreMod());
        Assertions.assertEquals(testDescFeature.isChoice(), testDuplicationFeature.isChoice());
        Assertions.assertEquals(testDescFeature.getChoiceCount(), testDuplicationFeature.getChoiceCount());
        Assertions.assertNull(testDuplicationFeature.getChoices());
        Assertions.assertEquals(testDescFeature.getStatMod(), testDuplicationFeature.getStatMod());
        Assertions.assertEquals(testDescFeature.getProficiency(), testDuplicationFeature.getProficiency());
        Assertions.assertEquals(testDescFeature.getFeatures(), testDuplicationFeature.getFeatures());
        Assertions.assertEquals(testDescFeature.getDescription(), testDuplicationFeature.getDescription());
        Assertions.assertFalse(testDuplicationFeature == testDescFeature);
    }
}
