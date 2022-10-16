package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CharacterTest {
    /// A series of tests for the Character class.

    @Test
    void testConstructor() {
        Character testCharacter = new Character(10,11,12,13,14,15);
        Assertions.assertNull(testCharacter.getClasses());

    }
}