package model;

import enums.ModifierType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ModifierTest {
    ///A series of tests for the Modifier class.
    private static final int BASE_VALUE = 1;
    private static final int ADD_VALUE = 2;
    private static final int MULT_VALUE = 3;
    private static final int MAX_VALUE = 4;
    private static final int MIN_VALUE = 5;
    
    private Modifier testBaseModifier;
    private Modifier testAddModifier;
    private Modifier testMultiplyModifier;
    private Modifier testMaxModifier;
    private Modifier testMinModifier;

    @BeforeEach
    void setUp() {
        testBaseModifier = new Modifier(ModifierType.BASE, new BigDecimal(BASE_VALUE));
        testAddModifier = new Modifier(ModifierType.ADD, new BigDecimal(ADD_VALUE));
        testMultiplyModifier = new Modifier(ModifierType.MULTIPLY, new BigDecimal(MULT_VALUE));
        testMaxModifier = new Modifier(ModifierType.MAX, new BigDecimal(MAX_VALUE));
        testMinModifier = new Modifier(ModifierType.MIN, new BigDecimal(MIN_VALUE));
    }
    
    @Test
    void testConstructors() {
        Assertions.assertEquals(ModifierType.BASE, testBaseModifier.getType());
        Assertions.assertEquals(ModifierType.ADD, testAddModifier.getType());
        Assertions.assertEquals(ModifierType.MULTIPLY, testMultiplyModifier.getType());
        Assertions.assertEquals(ModifierType.MAX, testMaxModifier.getType());
        Assertions.assertEquals(ModifierType.MIN, testMinModifier.getType());
        
        Assertions.assertEquals(new BigDecimal(BASE_VALUE), testBaseModifier.getValue());
        Assertions.assertEquals(new BigDecimal(ADD_VALUE), testAddModifier.getValue());
        Assertions.assertEquals(new BigDecimal(MULT_VALUE), testMultiplyModifier.getValue());
        Assertions.assertEquals(new BigDecimal(MAX_VALUE), testMaxModifier.getValue());
        Assertions.assertEquals(new BigDecimal(MIN_VALUE), testMinModifier.getValue());
    }

    @Test
    void testIntConstructor() {
        Modifier testIntModifier = new Modifier(6);
        Assertions.assertEquals(ModifierType.BASE, testIntModifier.getType());
        Assertions.assertEquals(new BigDecimal(6), testIntModifier.getValue());
    }

    @Test
    void testEquals() {
        Modifier testEqualsModifier1 = new Modifier(5);
        Modifier testEqualsModifier2 = new Modifier(ModifierType.BASE, new BigDecimal(5));
        Assertions.assertFalse(testEqualsModifier1.equals(null));
        Assertions.assertFalse(testEqualsModifier1.equals(new BigDecimal(6)));
        Assertions.assertFalse(testEqualsModifier1.equals(testAddModifier));
        Assertions.assertFalse(testEqualsModifier1.equals(testBaseModifier));
        Assertions.assertFalse(testEqualsModifier1.equals(testMinModifier));
        Assertions.assertTrue(testEqualsModifier1.equals(testEqualsModifier2));
    }

    @Test
    void testHashCode() {
        Modifier testHashModifier1 = new Modifier(0);
        Modifier testHashModifier2 = new Modifier(1);
        Modifier testHashModifier3 = new Modifier(ModifierType.ADD, new BigDecimal(0));
        Modifier testHashModifier4 = new Modifier(ModifierType.BASE, new BigDecimal(0));
        Assertions.assertNotEquals(testHashModifier1.hashCode(), testHashModifier2.hashCode());
        Assertions.assertNotEquals(testHashModifier2.hashCode(), testHashModifier3.hashCode());
        Assertions.assertNotEquals(testHashModifier1.hashCode(), testHashModifier3.hashCode());
        Assertions.assertEquals(testHashModifier1.hashCode(), testHashModifier4.hashCode());
    }

    @Test
    void testClone() {
        Modifier testCloneBaseModifier = testBaseModifier.clone();
        Assertions.assertEquals(testCloneBaseModifier.getType(), testBaseModifier.getType());
        Assertions.assertEquals(testCloneBaseModifier.getValue(), testBaseModifier.getValue());
        Assertions.assertFalse(testCloneBaseModifier.getValue()==testBaseModifier.getValue());
        Assertions.assertFalse(testCloneBaseModifier==testBaseModifier);
    }

    @Test
    void testApplyBaseToModifier() {
        Modifier resultModifier = new Modifier(BASE_VALUE + 1).apply(testBaseModifier);
        Assertions.assertEquals(testBaseModifier.getType(), resultModifier.getType());
        Assertions.assertEquals(new BigDecimal(BASE_VALUE + 1), resultModifier.getValue());

        resultModifier = new Modifier(BASE_VALUE).apply(testBaseModifier);
        Assertions.assertEquals(testBaseModifier.getType(), resultModifier.getType());
        Assertions.assertEquals(testBaseModifier.getValue(), resultModifier.getValue());

        resultModifier = new Modifier(BASE_VALUE - 1).apply(testBaseModifier);
        Assertions.assertEquals(testBaseModifier.getType(), resultModifier.getType());
        Assertions.assertEquals(testBaseModifier.getValue(), resultModifier.getValue());
    }

    @Test
    void testApplyAddToModifier() {
        Modifier resultModifier = testAddModifier.apply(testBaseModifier);
        Assertions.assertEquals(testBaseModifier.getType(), resultModifier.getType());
        Assertions.assertEquals(testBaseModifier.getValue().add(testAddModifier.getValue()), resultModifier.getValue());
    }

    @Test
    void testApplyMultiplyToModifier() {
        Modifier resultModifier = testMultiplyModifier.apply(testBaseModifier);
        Assertions.assertEquals(testBaseModifier.getType(), resultModifier.getType());
        Assertions.assertEquals(testBaseModifier.getValue().multiply(testMultiplyModifier.getValue()),
                                resultModifier.getValue());

        resultModifier = testMultiplyModifier.apply(new Modifier(2));
        Assertions.assertEquals(ModifierType.BASE, resultModifier.getType());
        Assertions.assertEquals(new BigDecimal(2).multiply(testMultiplyModifier.getValue()),
                                resultModifier.getValue());
    }

    @Test
    void testApplyMaxToModifier() {
        Modifier resultModifier = testMaxModifier.apply(testBaseModifier);
        Assertions.assertEquals(testBaseModifier.getType(), resultModifier.getType());
        Assertions.assertEquals(testBaseModifier.getValue(), resultModifier.getValue());

        resultModifier = testMaxModifier.apply(new Modifier(MAX_VALUE - 1));
        Assertions.assertEquals(ModifierType.BASE, resultModifier.getType());
        Assertions.assertEquals(new BigDecimal(MAX_VALUE - 1), resultModifier.getValue());

        resultModifier = testMaxModifier.apply(new Modifier(MAX_VALUE));
        Assertions.assertEquals(ModifierType.BASE, resultModifier.getType());
        Assertions.assertEquals(new BigDecimal(MAX_VALUE), resultModifier.getValue());

        resultModifier = testMaxModifier.apply(new Modifier(MAX_VALUE + 1));
        Assertions.assertEquals(ModifierType.BASE, resultModifier.getType());
        Assertions.assertEquals(testMaxModifier.getValue(), resultModifier.getValue());
    }

    @Test
    void testApplyMinToModifier() {
        Modifier resultModifier = testMinModifier.apply(testBaseModifier);
        Assertions.assertEquals(testBaseModifier.getType(), resultModifier.getType());
        Assertions.assertEquals(testMinModifier.getValue(), resultModifier.getValue());

        resultModifier = testMinModifier.apply(new Modifier(MIN_VALUE - 1));
        Assertions.assertEquals(ModifierType.BASE, resultModifier.getType());
        Assertions.assertEquals(testMinModifier.getValue(), resultModifier.getValue());

        resultModifier = testMinModifier.apply(new Modifier(MIN_VALUE));
        Assertions.assertEquals(ModifierType.BASE, resultModifier.getType());
        Assertions.assertEquals(new BigDecimal(MIN_VALUE), resultModifier.getValue());

        resultModifier = testMinModifier.apply(new Modifier(MIN_VALUE + 1));
        Assertions.assertEquals(ModifierType.BASE, resultModifier.getType());
        Assertions.assertEquals(new BigDecimal(MIN_VALUE + 1), resultModifier.getValue());
    }

    @Test
    void testApplyInheritType() {
        Modifier resultModifier = testBaseModifier.apply(testBaseModifier);
        Assertions.assertEquals(testBaseModifier.getType(), resultModifier.getType());

        resultModifier = testBaseModifier.apply(testAddModifier);
        Assertions.assertEquals(testAddModifier.getType(), resultModifier.getType());

        resultModifier = testBaseModifier.apply(testMultiplyModifier);
        Assertions.assertEquals(testMultiplyModifier.getType(), resultModifier.getType());

        resultModifier = testBaseModifier.apply(testMaxModifier);
        Assertions.assertEquals(testMaxModifier.getType(), resultModifier.getType());

        resultModifier = testBaseModifier.apply(testMinModifier);
        Assertions.assertEquals(testMinModifier.getType(), resultModifier.getType());
    }
}
