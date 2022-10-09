package model;

import enums.ModifierType;
import enums.ScoreType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class CharacterModifierTest {
    /// A series of tests for all classes implementing the CharacterModifier interface.

    private CharacterModifier testClass;
    private CharacterModifier testRace;
    private CharacterModifier testBackground;

    private Proficiency testProficiency;
    private Modifier testModifier;
    private HashMap<ScoreType, Modifier> testScoreMap;
    private Feature testFeature;
    private ArrayList<Feature> testFeatureList;
    private ArrayList<HashMap<ScoreType, Modifier>> testScoreMapList;

    @BeforeEach
    void runBefore() {
        testProficiency = new Proficiency(ScoreType.CON_SAVE, BigDecimal.ZERO);
        testModifier = new Modifier(ModifierType.BASE, BigDecimal.ZERO);
        testScoreMap = new HashMap<>();
        testScoreMap.put(ScoreType.CON_SAVE, testModifier);
        testFeature = new Feature("Test Feature 1", ScoreType.CON_SAVE, testModifier);
        testFeatureList = new ArrayList<>();
        testFeatureList.add(testFeature);
        testFeatureList.add(new Feature("Test Feature 2", ScoreType.DEX_SAVE, testModifier));
        testScoreMapList = new ArrayList<>();
        testScoreMapList.add(testScoreMap);

        testClass = new CharClass("Test Class", new ArrayList<>(), testFeatureList,
                                                      new ArrayList<>(), new ArrayList<>(),
                                                      new ArrayList<>(), 3);
        testRace = new CharRace("Test Race", testScoreMap,
                                new ArrayList<>(), testFeatureList);
        testBackground = new CharBackground("Test Background", testScoreMap,
                                                new ArrayList<>(), testFeatureList);
    }

    @Test
    void testClassConstruction() {
        Assertions.assertEquals("Test Class", testClass.getName());
        Assertions.assertNull(testClass.getScores());
        Assertions.assertEquals(new ArrayList<>(), testClass.getProficiencies());
        Assertions.assertEquals(testFeatureList, testClass.getFeatures());
    }

    @Test
    void testRaceConstruction() {
        Assertions.assertEquals("Test Race", testRace.getName());
        Assertions.assertEquals(testScoreMap, testRace.getScores());
        Assertions.assertEquals(new ArrayList<>(), testRace.getProficiencies());
        Assertions.assertEquals(testFeatureList, testRace.getFeatures());
    }

    @Test
    void testBackgroundConstruction() {
        Assertions.assertEquals("Test Background", testBackground.getName());
        Assertions.assertEquals(testScoreMap, testBackground.getScores());
        Assertions.assertEquals(new ArrayList<>(), testBackground.getProficiencies());
        Assertions.assertEquals(testFeatureList, testBackground.getFeatures());
    }
}
