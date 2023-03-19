package ui.gui;

import model.Feature;
import model.InventoryItem;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class FeaturePanel extends JPanel {
    /// A Panel dedicated to displaying a Feature.
    private Feature feature;
    private JLabel nameLabel = new JLabel();
    private JLabel typeLabel = new JLabel();
    private JLabel descriptionLabel = new JLabel();

    public FeaturePanel(Feature feature) {
        super();
        this.feature = feature;
        readFeature(this.feature);
        setAlignmentX(LEFT_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setVisible(true);
        add(nameLabel);
        add(typeLabel);
        add(descriptionLabel);
    }

    // MODIFIES: this
    // EFFECTS: reads fields from a Feature and updates to display its values
    private void readFeature(Feature feature) {
        nameLabel.setText(feature.getName());
        typeLabel.setText(feature.getType().toString());
        descriptionLabel.setText(feature.getDescription());
    }
}
