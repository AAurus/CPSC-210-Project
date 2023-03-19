package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utility.Utility;

public class UtilityTest {
    /// A series of tests for the Utility class.

    @Test
    void logEventTest() {
        EventLog.getInstance().clear();

        Utility.logEvent("Test Event");

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("Test Event");
        }

        Assertions.assertTrue(containsEvent);

        EventLog.getInstance().clear();
    }

    @Test
    void isBlankTest() {
        String a = "Test";
        String b = "   test    ";
        String c = "      \n    ";
        String d = "";
        Assertions.assertFalse(Utility.isBlank(a));
        Assertions.assertFalse(Utility.isBlank(b));
        Assertions.assertTrue(Utility.isBlank(c));
        Assertions.assertTrue(Utility.isBlank(d));
    }
}
