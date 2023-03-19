package model;

import enums.ModifierType;
import enums.ProficiencyType;
import enums.ScoreType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

public class ProficiencyTest {
    /// A series of tests for the Proficiency class.
    private Proficiency testScoreProficiency;
    private Proficiency testItemProficiency;

    @BeforeEach
    void setUp() {
        testScoreProficiency = new Proficiency(ScoreType.STRENGTH, new BigDecimal(2));
        testItemProficiency = new Proficiency("Sword", new BigDecimal("0.5"));
    }

    @Test
    void testConstructor() {
        Assertions.assertEquals(ProficiencyType.SCORE, testScoreProficiency.getType());
        Assertions.assertEquals(ScoreType.STRENGTH, testScoreProficiency.getScore());
        Assertions.assertEquals(new BigDecimal(2), testScoreProficiency.getMultiplier());
        Assertions.assertNull(testScoreProficiency.getItem());

        Assertions.assertEquals(ProficiencyType.ITEM, testItemProficiency.getType());
        Assertions.assertEquals("Sword", testItemProficiency.getItem());
        Assertions.assertEquals(new BigDecimal("0.5"), testItemProficiency.getMultiplier());
        Assertions.assertNull(testItemProficiency.getScore());
    }

    @Test
    void testGenerateModifier() {
        BigDecimal testProfBonus = new BigDecimal(2);
        Modifier testScoreModifier = testScoreProficiency.generateModifier(testProfBonus);
        Modifier testItemModifier = testItemProficiency.generateModifier(testProfBonus);

        Assertions.assertEquals(testProfBonus.multiply(testScoreProficiency.getMultiplier()),
                                testScoreModifier.getValue());
        Assertions.assertEquals(testProfBonus.multiply(testItemProficiency.getMultiplier()),
                                testItemModifier.getValue());

        Assertions.assertEquals(ModifierType.ADD, testScoreModifier.getType());
        Assertions.assertEquals(ModifierType.ADD, testItemModifier.getType());
    }

    @Test
    void testGenerateScoreMap() {
        BigDecimal testProfBonus = new BigDecimal(2);
        HashMap<ScoreType, Modifier> testScoreMap = testScoreProficiency.generateScoreMap(testProfBonus);
        HashMap<ScoreType, Modifier> testItemMap = testItemProficiency.generateScoreMap(testProfBonus);

        Assertions.assertTrue(testScoreMap.containsKey(ScoreType.STRENGTH));
        Assertions.assertEquals(new Modifier(ModifierType.ADD,
                                             new BigDecimal(2).multiply(testScoreProficiency.getMultiplier())),
                                testScoreMap.get(ScoreType.STRENGTH));

        Assertions.assertTrue(testItemMap.isEmpty());
    }

    @Test
    void testGetProficiencyName() {
        Assertions.assertEquals(ScoreType.STRENGTH.name(), testScoreProficiency.getProficiencyName());
        Assertions.assertEquals(testItemProficiency.getItem(), testItemProficiency.getProficiencyName());
    }

    @Test
    void testHashCode() {
        Proficiency testHashProficiency1 = new Proficiency("Sword", new BigDecimal(0));
        Proficiency testHashProficiency2 = new Proficiency("Sword", new BigDecimal(1));
        Proficiency testHashProficiency3 = new Proficiency(ScoreType.STRENGTH, new BigDecimal(0));
        Proficiency testHashProficiency4 = new Proficiency("Sword", new BigDecimal(0));
        Assertions.assertNotEquals(testHashProficiency1.hashCode(), testHashProficiency2.hashCode());
        Assertions.assertNotEquals(testHashProficiency2.hashCode(), testHashProficiency3.hashCode());
        Assertions.assertNotEquals(testHashProficiency1.hashCode(), testHashProficiency3.hashCode());
        Assertions.assertEquals(testHashProficiency1.hashCode(), testHashProficiency4.hashCode());
    }
    
    @Test
    void testEquals() {
        Proficiency testEqualsProficiency1 = new Proficiency("Sword", new BigDecimal(2));
        Proficiency testEqualsProficiency2 = new Proficiency("Sword", new BigDecimal(2));
        Proficiency testEqualsProficiency3 = new Proficiency(ScoreType.STRENGTH, new BigDecimal("0.5"));

        Assertions.assertFalse(testEqualsProficiency1.equals(null));
        Assertions.assertFalse(testEqualsProficiency1.equals(new BigDecimal(2)));
        Assertions.assertFalse(testEqualsProficiency1.equals(testEqualsProficiency3));
        Assertions.assertFalse(testEqualsProficiency1.equals(testScoreProficiency));
        Assertions.assertFalse(testEqualsProficiency1.equals(testItemProficiency));
        Assertions.assertTrue(testEqualsProficiency1.equals(testEqualsProficiency2));
    }
}
