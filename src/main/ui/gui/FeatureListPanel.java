package ui.gui;

import model.Feature;

import javax.swing.*;
import java.util.ArrayList;

public class FeatureListPanel extends JPanel {
    /// A panel specifically to display a list of features.

    private ArrayList<FeaturePanel> features = new ArrayList<>();

    public FeatureListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        reload();
    }

    public FeatureListPanel(ArrayList<Feature> features) {
        for (Feature f : features) {
            this.features.add(new FeaturePanel(f));
        }
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        reload();
    }

    // MODIFIES: this
    // EFFECTS: reloads all assets
    public void reload() {
        removeAll();
        for (FeaturePanel p : features) {
            add(p);
        }
        revalidate();
    }

    public ArrayList<FeaturePanel> getFeatures() {
        return features;
    }
}
