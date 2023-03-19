package ui.gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;

import enums.ScoreType;
import model.Character;

public class CharacterPanel extends JPanel {
    /// A JPanel to give a visual representation of a Character.
    private Character character;
    private JLabel nameLabel = new JLabel();
    private JLabel raceBackgroundLabel = new JLabel();
    private ArrayList<JLabel> classesLabels;
    private ArrayList<JLabel> scoresLabels;

    public CharacterPanel(Character character) {
        super();
        this.character = character;
        readCharacter(this.character);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setVisible(true);
        add(nameLabel);
        add(raceBackgroundLabel);
    }

    // MODIFIES: this
    // EFFECTS: reads values from an associated character and updates gui elements to match
    private void readCharacter(Character character) {
        nameLabel.setText(character.getName());
        raceBackgroundLabel.setText(character.getRace() + " " + character.getBackground());
        classesLabels = new ArrayList<>();
        for (String s : character.getClasses()) {
            classesLabels.add(new JLabel("Level " + character.getClassLevels().get(s) + " " + s));
        }
        scoresLabels = new ArrayList<>();
        for (ScoreType t : ScoreType.values()) {
            scoresLabels.add(new JLabel(String.valueOf(character.getSkillThrowBonuses().get(t))));
        }
    }
}
