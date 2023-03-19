package ui.gui;

import enums.ScoreType;
import model.Character;
import model.InventoryItem;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;

public class InventoryItemPanel extends JPanel {
    /// A JPanel to give a visual representation of an InventoryItem.
    private InventoryItem inventoryItem;
    private JLabel nameLabel = new JLabel();
    private JLabel worthWeightLabel = new JLabel();
    private JLabel descriptionLabel = new JLabel();

    public InventoryItemPanel(InventoryItem item) {
        super();
        this.inventoryItem = item;
        readItem(this.inventoryItem);
        setAlignmentX(LEFT_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setVisible(true);
        add(nameLabel);
        add(worthWeightLabel);
        add(descriptionLabel);
    }

    // MODIFIES: this
    // EFFECTS: reads fields from an InventoryItem and displays its values
    private void readItem(InventoryItem item) {
        nameLabel.setText(item.getName());
        worthWeightLabel.setText(item.getPrice().toString() + "gp | " + item.getWeight().toString() + " lb");
        descriptionLabel.setText(item.getDescription());
    }
}
