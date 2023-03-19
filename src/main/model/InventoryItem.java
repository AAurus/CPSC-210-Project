package model;

import exceptions.InventoryItemNotFoundException;
import utility.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InventoryItem {
    /// A representation of a DnD item.
    public static final int HASH_MULTIPLIER = 149;

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

        Utility.logEvent("New item created: " + name);
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

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;

        Utility.logEvent("Item " + oldName + " changed name to " + name);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;

        Utility.logEvent("Item " + name + " price set to " + price);
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;

        Utility.logEvent("Item " + name + " weight set to " + weight);
    }

    public void setDescription(String description) {
        this.description = description;

        Utility.logEvent("Item " + name + " description set to: '" + description + "'");
    }

    public void setFeature(Feature feature) {
        this.feature = feature;

        Utility.logEvent("Item " + name + " feature set to " + feature.getName());
    }

    //MODIFIES: from, to
    //EFFECTS: moves item from one inventory to another
    public static void moveItem(ArrayList<InventoryItem> from, ArrayList<InventoryItem> to, String itemName)
            throws InventoryItemNotFoundException {
        for (InventoryItem i : from) {
            if (i.getName().equals(itemName)) {
                to.add(i);
                from.remove(i);
                return;
            }
        }
        throw new InventoryItemNotFoundException();
    }

    //MODIFIES: from, to
    //EFFECTS: moves item from one inventory to another
    public static void moveItem(ArrayList<InventoryItem> from, ArrayList<InventoryItem> to, int index)
            throws IndexOutOfBoundsException {
        InventoryItem item = from.get(index);
        to.add(item);
        from.remove(item);
    }

    //EFFECTS: returns total weight of all items in list
    public static BigDecimal getTotalWeight(List<InventoryItem> items) {
        BigDecimal result = new BigDecimal(0);
        for (InventoryItem i : items) {
            result = result.add(i.getWeight());
        }
        return result;
    }

    @Override
    public int hashCode() {
        ArrayList<Integer> hashComponents = new ArrayList<>();
        hashComponents.add(name.hashCode());
        hashComponents.add(description.hashCode());
        hashComponents.add(weight.hashCode());
        hashComponents.add(price.hashCode());
        hashComponents.add(Utility.hashIfAble(feature));
        return Utility.hashCodeHelper(hashComponents, HASH_MULTIPLIER);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InventoryItem other = (InventoryItem) obj;
        return (this.hashCode() == other.hashCode());
    }
}
