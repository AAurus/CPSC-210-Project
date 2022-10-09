package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class CharClassTest {
    /// A series of tests for the CharClass class.
    private CharClass testClass;

    @BeforeEach
    void runBefore() {
        testClass = new CharClass("Test Class", new ArrayList<>(), new ArrayList<>(),
                                                      new ArrayList<>(), new ArrayList<>(),
                                                      new ArrayList<>(), 3);
    }

    @Test
    void testConstructor() {
        Assertions.assertNotNull(testClass.getName());
        Assertions.assertNotNull(testClass.getFeatures());
        Assertions.assertNotNull(testClass.getProficiencies());
        Assertions.assertEquals(1, testClass.getLevel());
        Assertions.assertNull(testClass.getScores());
        Assertions.assertEquals(3, testClass.getSubClassLevel());
    }

    @Test
    void testLevelUpOnce() {
        testClass.levelUp();
        Assertions.assertEquals(2, testClass.getLevel());
    }

    @Test
    void testLevelUpMultiple() {
        testClass.levelUp();
        testClass.levelUp();
        Assertions.assertEquals(3, testClass.getLevel());
    }

    @Test
    void testSelectSubClass() {
        Assertions.assertFalse(testClass.selectSubclass(0));
        testClass.setLevel(3);
        Assertions.assertTrue(testClass.selectSubclass(0));
    }
}
