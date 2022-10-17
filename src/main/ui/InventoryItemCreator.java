package ui;

import model.Feature;
import model.InventoryItem;

import java.math.BigDecimal;
import java.util.List;

public class InventoryItemCreator extends AcceptsInput {
    /// A helper for main that handles creating InventoryItems.

    //EFFECTS: creates an inventoryItem from user input
    public InventoryItem createInventoryItem(List<Feature> otherFeatures) {
        String name = input.scanName("Item");
        if (name == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add extra information about this item?")) {
            return createInventoryItemExtended(new InventoryItem(name), otherFeatures);
        }
        return new InventoryItem(name);
    }

    //EFFECTS: adds extra info about the created inventoryItem before returning
    private InventoryItem createInventoryItemExtended(InventoryItem baseItem, List<Feature> otherFeatures) {
        return addItemDescription(addItemFeature(addItemWeight(addItemPrice(baseItem)), otherFeatures));
    }

    //EFFECTS: adds price to item if user wishes, and returns it
    private InventoryItem addItemPrice(InventoryItem baseItem) {
        if (baseItem == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to indicate this item's worth in gold pieces?")) {
            while (true) {
                String priceInput = input.scanWithExit("Please enter this item's worth:");
                if (priceInput == null) {
                    return null;
                }
                if (priceInput.matches("\\d*\\.?\\d*")) {
                    baseItem.setPrice(new BigDecimal(priceInput));
                }
            }
        }
        return baseItem;
    }

    //EFFECTS: adds weight to item if user wishes, and returns it
    private InventoryItem addItemWeight(InventoryItem baseItem) {
        if (baseItem == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to indicate this item's weight in pounds?")) {
            while (true) {
                String priceInput = input.scanWithExit("Please enter this item's worth:");
                if (priceInput == null) {
                    return null;
                }
                if (priceInput.matches("\\d*\\.?\\d*")) {
                    baseItem.setWeight(new BigDecimal(priceInput));
                }
            }
        }
        return baseItem;
    }

    //EFFECTS: adds feature to item if user wishes, and returns it
    private InventoryItem addItemFeature(InventoryItem baseItem, List<Feature> otherFeatures) {
        if (baseItem == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add a feature to this item?")) {
            if (otherFeatures.isEmpty()) {
                System.out.println("Sorry, this functionality isn't available yet. Make some features first!");
            } else {
                System.out.println("The features you can choose to add to this feature are:");
                for (int i = 0; i < otherFeatures.size(); i++) {
                    System.out.println("[" + i + "] " + otherFeatures.get(i).getName());
                }
                while (true) {
                    int index = input.scanPositiveIntWithExit("Please enter the index of your selected feature");
                    if (index < 0) {
                        return null;
                    } else if (index < otherFeatures.size()) {
                        baseItem.setFeature(otherFeatures.get(index));
                        break;
                    }
                }
            }
        }
        return baseItem;
    }

    //EFFECTS: adds description to item if user wishes, and returns it
    private InventoryItem addItemDescription(InventoryItem baseItem) {
        if (baseItem == null) {
            return null;
        }
        if (input.scanYesNo("Would you like to add a description to this item?")) {
            String description = input.scanWithExit("Please enter a description of your item here");
            if (description == null) {
                return null;
            }
            baseItem.setDescription(description);
        }
        return baseItem;
    }
}
