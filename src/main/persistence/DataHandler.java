package persistence;

import model.Character;
import model.Feature;
import model.InventoryItem;
import persistence.FileIOHandler;
import utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DataHandler {
    /// What used to be the main UI class of TinySRVNT. Has since been cannibalized for the GUI.
    private static final int HASH_MULTIPLIER = 43;

    private ArrayList<Character> characters;
    private ArrayList<InventoryItem> items;
    private ArrayList<Feature> features;

    public DataHandler() {
        characters = new ArrayList<>();
        items = new ArrayList<>();
        features = new ArrayList<>();
    }

    public DataHandler(ArrayList<Character> characters, ArrayList<InventoryItem> items, ArrayList<Feature> features) {
        this.characters = characters;
        this.items = items;
        this.features = features;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public ArrayList<InventoryItem> getItems() {
        return items;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataHandler that = (DataHandler) o;
        return that.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Utility.hashCodeHelper(Arrays.asList(getCharacters().hashCode(),
                                                    getItems().hashCode(),
                                                    getFeatures().hashCode()),
                                      HASH_MULTIPLIER);
    }
}
