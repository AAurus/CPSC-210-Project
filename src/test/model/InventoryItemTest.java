package model;

import enums.ScoreType;
import exceptions.InventoryItemNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class InventoryItemTest {
    /// A series of tests for the InventoryItem class.
    private InventoryItem testInventoryItem;

    @BeforeEach
    void setUp() {
        testInventoryItem = new InventoryItem("Test Item");
    }

    @AfterEach
    void clearLog() {
        EventLog.getInstance().clear();
    }

    @Test
    void testConstructor() {
        Assertions.assertNotNull(testInventoryItem.getName());
        Assertions.assertEquals("Test Item", testInventoryItem.getName());
        Assertions.assertNull(testInventoryItem.getFeature());
        Assertions.assertEquals(new BigDecimal(0), testInventoryItem.getWeight());
        Assertions.assertEquals(new BigDecimal(0), testInventoryItem.getPrice());
        Assertions.assertEquals("", testInventoryItem.getDescription());

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("New item created: Test Item");
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testConstructorWithDescription() {
        testInventoryItem = new InventoryItem("Test Item", "Test Description");
        Assertions.assertEquals("Test Item", testInventoryItem.getName());
        Assertions.assertEquals("Test Description", testInventoryItem.getDescription());
        Assertions.assertNull(testInventoryItem.getFeature());
        Assertions.assertEquals(new BigDecimal(0), testInventoryItem.getWeight());
        Assertions.assertEquals(new BigDecimal(0), testInventoryItem.getPrice());

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("New item created: Test Item");
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testSetName() {
        testInventoryItem.setName("New Name");
        Assertions.assertEquals("New Name", testInventoryItem.getName());

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("Item Test Item changed name to New Name");
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testSetWeight() {
        testInventoryItem.setWeight(new BigDecimal(1));
        Assertions.assertEquals(new BigDecimal(1), testInventoryItem.getWeight());

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("Item Test Item weight set to 1");
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testSetPrice() {
        testInventoryItem.setPrice(new BigDecimal(1));
        Assertions.assertEquals(new BigDecimal(1), testInventoryItem.getPrice());

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("Item Test Item price set to 1");
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testSetFeature() {
        testInventoryItem.setFeature(new Feature("Test Feature", ScoreType.STRENGTH, new Modifier(1)));
        Assertions.assertEquals(new Feature("Test Feature", ScoreType.STRENGTH, new Modifier(1)),
                                testInventoryItem.getFeature());

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("Item Test Item feature set to Test Feature");
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testSetDescription() {
        testInventoryItem.setDescription("New Description");
        Assertions.assertEquals("New Description", testInventoryItem.getDescription());

        boolean containsEvent = false;
        for (Event e : EventLog.getInstance()) {
            containsEvent = containsEvent || e.getDescription().equals("Item Test Item description set to: "
                                                                       + "'New Description'");
        }
        Assertions.assertTrue(containsEvent);
    }

    @Test
    void testMoveItemIndex() {
        ArrayList<InventoryItem> testInventory1 = new ArrayList<>();
        ArrayList<InventoryItem> testInventory2 = new ArrayList<>();
        testInventory1.add(testInventoryItem);
        Assertions.assertTrue(testInventory1.contains(testInventoryItem));
        Assertions.assertFalse(testInventory2.contains(testInventoryItem));
        Assertions.assertEquals(1, testInventory1.size());
        Assertions.assertEquals(0, testInventory2.size());

        InventoryItem.moveItem(testInventory1, testInventory2, 0);

        Assertions.assertFalse(testInventory1.contains(testInventoryItem));
        Assertions.assertTrue(testInventory2.contains(testInventoryItem));
        Assertions.assertEquals(0, testInventory1.size());
        Assertions.assertEquals(1, testInventory2.size());
    }

    @Test
    void testMoveItemName() {
        ArrayList<InventoryItem> testInventory1 = new ArrayList<>();
        ArrayList<InventoryItem> testInventory2 = new ArrayList<>();
        testInventory1.add(testInventoryItem);
        Assertions.assertTrue(testInventory1.contains(testInventoryItem));
        Assertions.assertFalse(testInventory2.contains(testInventoryItem));
        Assertions.assertEquals(1, testInventory1.size());
        Assertions.assertEquals(0, testInventory2.size());

        try {
            InventoryItem.moveItem(testInventory1, testInventory2, "Test Item");

            Assertions.assertFalse(testInventory1.contains(testInventoryItem));
            Assertions.assertTrue(testInventory2.contains(testInventoryItem));
            Assertions.assertEquals(0, testInventory1.size());
            Assertions.assertEquals(1, testInventory2.size());
        } catch (InventoryItemNotFoundException ie) {
            Assertions.fail("Unexpected InventoryItemNotFound Exception");
        }
    }

    @Test
    void testMoveItemNameDuplicates() {
        ArrayList<InventoryItem> testInventory1 = new ArrayList<>();
        ArrayList<InventoryItem> testInventory2 = new ArrayList<>();
        InventoryItem testInventoryItem2 = new InventoryItem("Test Item", "Item 2");
        testInventory1.add(testInventoryItem);
        testInventory1.add(testInventoryItem2);
        Assertions.assertTrue(testInventory1.contains(testInventoryItem));
        Assertions.assertFalse(testInventory2.contains(testInventoryItem));
        Assertions.assertEquals(2, testInventory1.size());
        Assertions.assertEquals(0, testInventory2.size());

        try {
            InventoryItem.moveItem(testInventory1, testInventory2, "Test Item");

            Assertions.assertFalse(testInventory1.contains(testInventoryItem));
            Assertions.assertTrue(testInventory1.contains(testInventoryItem2));
            Assertions.assertTrue(testInventory2.contains(testInventoryItem));
            Assertions.assertFalse(testInventory2.contains(testInventoryItem2));
            Assertions.assertEquals(1, testInventory1.size());
            Assertions.assertEquals(1, testInventory2.size());
        } catch (InventoryItemNotFoundException ie) {
            Assertions.fail("Unexpected InventoryItemNotFound Exception");
        }
    }

    @Test
    void testMoveItemNameException() {
        ArrayList<InventoryItem> testInventory1 = new ArrayList<>();
        ArrayList<InventoryItem> testInventory2 = new ArrayList<>();
        testInventory1.add(testInventoryItem);
        Assertions.assertTrue(testInventory1.contains(testInventoryItem));
        Assertions.assertFalse(testInventory2.contains(testInventoryItem));
        Assertions.assertEquals(1, testInventory1.size());
        Assertions.assertEquals(0, testInventory2.size());

        try {
            InventoryItem.moveItem(testInventory1, testInventory2, "Test Item 2");
            Assertions.fail("Unexpected Success");
        } catch (InventoryItemNotFoundException ie) {
            Assertions.assertTrue(testInventory1.contains(testInventoryItem));
            Assertions.assertFalse(testInventory2.contains(testInventoryItem));
            Assertions.assertEquals(1, testInventory1.size());
            Assertions.assertEquals(0, testInventory2.size());
        }
    }

    @Test
    void testGetTotalWeight() {
        testInventoryItem.setWeight(new BigDecimal(1));
        InventoryItem testInventoryItem2 = new InventoryItem("Heavy Item");
        testInventoryItem2.setWeight(new BigDecimal("3.5"));

        ArrayList<InventoryItem> testInventory = new ArrayList<>();
        Assertions.assertEquals(new BigDecimal(0), InventoryItem.getTotalWeight(testInventory));

        testInventory.add(testInventoryItem);
        Assertions.assertEquals(testInventoryItem.getWeight(), InventoryItem.getTotalWeight(testInventory));

        testInventory.add(testInventoryItem);
        Assertions.assertEquals(testInventoryItem.getWeight().add(testInventoryItem.getWeight()),
                                InventoryItem.getTotalWeight(testInventory));

        testInventory.remove(testInventoryItem);
        Assertions.assertEquals(testInventoryItem.getWeight(), InventoryItem.getTotalWeight(testInventory));

        testInventory.add(testInventoryItem2);
        Assertions.assertEquals(testInventoryItem.getWeight().add(testInventoryItem2.getWeight()),
                                InventoryItem.getTotalWeight(testInventory));
    }
}
