package model;

import enums.StatType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class StatTypeTest {
    /// A series of tests for the static functions found in the StatType class.

    @Test
    void testInitBasicStats() {
        HashMap<StatType, Integer> testInitStatMap = StatType.initBasicStats();
        for (StatType s : testInitStatMap.keySet()) {
            if (s.equals(StatType.HIT_POINT_CON_PER_LEVEL)) {
                Assertions.assertEquals(1, testInitStatMap.get(s));
            } else {
                Assertions.assertEquals(0, testInitStatMap.get(s));
            }
        }
    }
}
