package ui.gui;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public abstract class GuiUtility {
    /// A utility class to hold methods that will assist in creating GUI elements.

    // REQUIRES:
    // EFFECTS: adds all JComponents from list to another JComponent using BoxLayout with spacers.
    public static void addAllWithSpacing(JPanel panel, List<JComponent> components, int size) {
        for (JComponent c : components) {
            panel.add(Box.createRigidArea(new Dimension(size, size)));
            panel.add(c);
        }
        panel.add(Box.createRigidArea(new Dimension(size, size)));
    }
}
