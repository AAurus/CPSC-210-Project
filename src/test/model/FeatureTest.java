package model;

import enums.FeatureType;
import enums.ModifierType;
import enums.ScoreType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FeatureTest {
    /// A series of tests for the Feature class.

    private Feature testFeature;

    @Test
    void testScoreFeature() {
        Modifier testMod = new Modifier(ModifierType.BASE, BigDecimal.ZERO);
        testFeature = new Feature("Test Feature", ScoreType.CON, testMod);

        Assertions.assertEquals(FeatureType.SCORE, testFeature.getType());
        Assertions.assertEquals(testMod, testFeature.getScoreMod());
        Assertions.assertEquals(ScoreType.CON, testFeature.getScoreSlot());

        Assertions.assertNull(testFeature.getFeatures());
        Assertions.assertNull(testFeature.getAttack());
        Assertions.assertEquals("", testFeature.getDescription());
    }

    @Test
    void testAttackFeature() {
        Attack testAttack = new Attack();
        testFeature = new Feature("Test Feature", testAttack);

        Assertions.assertEquals(FeatureType.ATTACK, testFeature.getType());
        Assertions.assertEquals(testAttack, testFeature.getAttack());

        Assertions.assertNull(testFeature.getScoreMod());
        Assertions.assertNull(testFeature.getScoreSlot());
        Assertions.assertNull(testFeature.getFeatures());
        Assertions.assertEquals("", testFeature.getDescription());
    }

    @Test
    void testMultiFeature() {
        Feature testSubFeature1 = new Feature("Test SubFeature 1", "description");
        Feature testSubFeature2 = new Feature("Test SubFeature 2", "lorem ipsum");
        ArrayList<Feature> testFeatureList = new ArrayList<>();
        testFeatureList.add(testSubFeature1);
        testFeatureList.add(testSubFeature2);
        testFeature = new Feature("Test Feature", testFeatureList);

        Assertions.assertEquals(FeatureType.MULTI, testFeature.getType());
        Assertions.assertEquals(testFeatureList, testFeature.getFeatures());

        Assertions.assertNull(testFeature.getScoreMod());
        Assertions.assertNull(testFeature.getScoreSlot());
        Assertions.assertNull(testFeature.getAttack());
        Assertions.assertEquals("", testFeature.getDescription());
    }

    @Test
    void testDescriptionName() {
        testFeature = new Feature("Test Feature", "Test Description");

        Assertions.assertEquals("Test Feature", testFeature.getName());
        Assertions.assertEquals("Test Description", testFeature.getDescription());
    }
}
