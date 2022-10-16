package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InventoryItem {
    /// A representation of a DnD item.
    private String name;
    private BigDecimal price;
    private BigDecimal weight;
    private String description;
    private Feature feature;

    public InventoryItem(String name) {
        this.name = name;
        this.price = BigDecimal.ZERO;
        this.weight = BigDecimal.ZERO;
        this.description = "";
        this.feature = null;
    }

    public InventoryItem(String name, String description) {
        this(name);
        this.description = description;
    }



    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public Feature getFeature() {
        return feature;
    }

    //MODIFIES: from, to
    //EFFECTS: moves item from one inventory to another
    public static void moveItem(ArrayList<InventoryItem> from, ArrayList<InventoryItem> to, String itemName) {
        for (InventoryItem ii : from) {
            if (ii.getName().equals(itemName)) {
                to.add(ii);
                from.remove(ii);
            }
        }
    }

    //EFFECTS: returns total weight of all items in list
    public static BigDecimal getTotalWeight(List<InventoryItem> items) {
        BigDecimal result = BigDecimal.ZERO;
        for (InventoryItem ii : items) {
            result.add(ii.getWeight());
        }
        return result;
    }
}