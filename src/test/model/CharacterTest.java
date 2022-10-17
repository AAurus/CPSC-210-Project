package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CharacterTest {
    /// A series of tests for the Character class.

    @Test
    void testConstructor() {
        Character testCharacter = new Character("Test Character", 10,11,12,13,14,15);
        Assertions.assertNull(testCharacter.getClasses());
        Assertions.assertNull(testCharacter.getRace());
        Assertions.assertNull(testCharacter.getBackground());
        Assertions.assertTrue(testCharacter.getEquippedItems().isEmpty());
        Assertions.assertTrue(testCharacter.getCarriedItems().isEmpty());
        Assertions.assertTrue(testCharacter.getInventoryItems().isEmpty());
    }
}