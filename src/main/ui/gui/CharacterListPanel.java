package ui.gui;

import javax.swing.*;
import java.util.ArrayList;

public class CharacterListPanel extends JPanel {
    /// A JPanel dedicated to managing character lists.
    private ArrayList<CharacterPanel> characters = new ArrayList<>();

    public CharacterListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        reload();
    }

    // MODIFIES: this
    // EFFECTS: reloads all assets
    public void reload() {
        removeAll();
        for (CharacterPanel p : characters) {
            add(p);
        }
        revalidate();
    }

    public ArrayList<CharacterPanel> getCharacters() {
        return characters;
    }
}
