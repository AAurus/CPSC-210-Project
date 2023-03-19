package ui.gui;

import model.Character;
import model.Feature;
import model.InventoryItem;
import model.Proficiency;
import persistence.DataHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DataHandlerWrapper {
    /// A class to wrap DataHandler with an action-handling system.
    private DataHandler data;
    private ArrayList<ActionListener> listenerList = new ArrayList<ActionListener>();

    public DataHandlerWrapper(DataHandler data) {
        this.data = data;
    }

    public void addItem(InventoryItem item) {
        data.getItems().add(item);
        notifyListeners(new ActionEvent(this, 0, "Add Item"));
    }

    public void addCharacter(Character character) {
        data.getCharacters().add(character);
        notifyListeners(new ActionEvent(this, 1, "Add Character"));
    }

    public void addFeature(Feature feature) {
        data.getFeatures().add(feature);
        notifyListeners(new ActionEvent(this, 2, "Add Feature"));
    }

    public List<InventoryItem> getItems() {
        return data.getItems();
    }

    public List<Character> getCharacters() {
        return data.getCharacters();
    }

    public List<Feature> getFeatures() {
        return data.getFeatures();
    }

    public DataHandler getData() {
        return data;
    }

    // MODIFIES: this
    // EFFECTS: replicates functionality of other JComponents with actionEvents; adds an action listener to this
    public void addActionListener(ActionListener al) {
        listenerList.add(al);
    }

    // MODIFIES: this
    // EFFECTS: replicates functionality of other JComponents with actionEvents; removes a listener from this
    public void removeActionListener(ActionListener al) {
        listenerList.remove(al);
    }

    // EFFECTS: notifies all listeners that an action has been performed
    private void notifyListeners(ActionEvent event) {
        for (ActionListener action : listenerList) {
            action.actionPerformed(event);
        }
    }
}
