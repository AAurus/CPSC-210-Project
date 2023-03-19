package ui.gui;

import model.Character;
import model.Feature;
import model.InventoryItem;
import persistence.DataHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

public class CharacterViewPanel extends JPanel implements ItemListener, ActionListener {
    /// A panel to manage a more detailed view of a selected item; currently, this is only of type Character.
    private DataHandlerWrapper data;

    private HashMap<String, Character> characterMap = new HashMap<>();
    private HashMap<String, InventoryItem> itemMap = new HashMap<>();
    private HashMap<String, Feature> featureMap = new HashMap<>();

    private JComboBox<String> characterSelectBox = new JComboBox<>();
    private JComboBox<String> itemSelectBox = new JComboBox<>();
    private JComboBox<String> featureSelectBox = new JComboBox<>();

    private JButton addItemButton = new JButton("Add Item");
    private JButton addFeatureButton = new JButton("Add Feature");

    private JPanel characterPanel = new JPanel();
    private JPanel characterInventoriesPanel = new JPanel();
    private JPanel characterFeaturesPanel = new JPanel();

    private Character selectedCharacter;
    private CharacterPanel characterDetailsPanel;
    private InventoryItemListPanel characterInventoryListPanel;
    private FeatureListPanel characterFeatureListPanel;

    public CharacterViewPanel(DataHandlerWrapper data) {
        this.data = data;
        data.addActionListener(this);
        setLayout(new BorderLayout());
        loadDataMaps();
        loadComboBoxes();
        add(characterSelectBox, BorderLayout.NORTH);
        characterInventoriesPanel.add(itemSelectBox);
        characterFeaturesPanel.add(featureSelectBox);
        if (selectedCharacter != null) {
            initCharacterPanel();
        }
    }

    // MODIFIES: this
    // EFFECTS: loads values in data into hashMaps for comboBoxes
    private void loadDataMaps() {
        for (Character c : data.getCharacters()) {
            String lookupText = "[" + data.getCharacters().indexOf(c) + "] " + c.getName();
            characterMap.put(lookupText, c);
            selectedCharacter = c;
        }
        for (InventoryItem i : data.getItems()) {
            String lookupText = "[" + data.getItems().indexOf(i) + "] " + i.getName();
            itemMap.put(lookupText, i);
        }
        for (Feature f : data.getFeatures()) {
            String lookupText = "[" + data.getFeatures().indexOf(f) + "] " + f.getName();
            featureMap.put(lookupText, f);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads hashMap keys into comboBoxes
    private void loadComboBoxes() {
        for (String s : characterMap.keySet()) {
            characterSelectBox.addItem(s);
        }
        for (String s : itemMap.keySet()) {
            itemSelectBox.addItem(s);
        }
        for (String s : featureMap.keySet()) {
            featureSelectBox.addItem(s);
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes character display and modification panel
    public void initCharacterPanel() {
        characterPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        characterDetailsPanel = new CharacterPanel(selectedCharacter);
        characterPanel.add(characterDetailsPanel, constraints);

        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        characterPanel.add(characterInventoriesPanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        characterPanel.add(characterFeaturesPanel, constraints);

        initCharacterSelectables();
        add(characterPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: initializes characterInventoriesPanel and characterFeaturesPanel
    public void initCharacterSelectables() {
        characterInventoriesPanel.setLayout(new BoxLayout(characterInventoriesPanel, BoxLayout.PAGE_AXIS));
        characterFeaturesPanel.setLayout(new BoxLayout(characterFeaturesPanel, BoxLayout.PAGE_AXIS));

        addItemButton.addActionListener(this);
        characterInventoriesPanel.add(addItemButton);
        characterInventoriesPanel.add(itemSelectBox);

        addFeatureButton.addActionListener(this);
        characterFeaturesPanel.add(addFeatureButton);
        characterFeaturesPanel.add(featureSelectBox);

        characterInventoryListPanel = new InventoryItemListPanel(selectedCharacter.getEquippedItems());
        characterInventoriesPanel.add(characterInventoryListPanel);

        characterFeatureListPanel = new FeatureListPanel(selectedCharacter.getFeatures());
        characterFeaturesPanel.add(characterFeatureListPanel);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(characterSelectBox)) {
            selectedCharacter = characterMap.get(characterSelectBox.getSelectedItem());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(addItemButton)) {
            selectedCharacter.addEquippedItem(
                    itemMap.get(itemSelectBox.getSelectedItem().toString()));
            reloadCharacterInventory();
        }
        if (e.getSource().equals(addFeatureButton)) {
            selectedCharacter.addFeature(featureMap.get(featureSelectBox.getSelectedItem().toString()));
            reloadCharacterFeatures();
        }
        if (e.getSource().equals(data)) {
            characterMap.clear();
            itemMap.clear();
            featureMap.clear();
            characterSelectBox.removeAllItems();
            itemSelectBox.removeAllItems();
            featureSelectBox.removeAllItems();
            loadDataMaps();
            loadComboBoxes();
        }
    }

    // MODIFIES: this
    // EFFECTS: helper; reloads character inventory
    private void reloadCharacterInventory() {
        characterInventoriesPanel.remove(characterInventoryListPanel);
        characterInventoryListPanel = new InventoryItemListPanel(selectedCharacter.getEquippedItems());
        characterInventoriesPanel.add(characterInventoryListPanel);
        revalidate();
    }

    // MODIFIES: this
    // EFFECTS: helper; reloads character features
    private void reloadCharacterFeatures() {
        characterFeaturesPanel.remove(characterFeatureListPanel);
        characterFeatureListPanel = new FeatureListPanel(selectedCharacter.getFeatures());
        characterFeaturesPanel.add(characterFeatureListPanel);
        revalidate();
    }
}
