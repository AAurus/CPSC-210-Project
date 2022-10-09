package model;

import enums.ScoreType;

import java.util.ArrayList;
import java.util.HashMap;

public class CharBackground implements CharacterModifier {
    /// A representation of a DnD Background.
    private final String name;
    private final HashMap<ScoreType, Modifier> scoreMods;
    private final ArrayList<Proficiency> proficiencies;
    private final ArrayList<Feature> features;

    public CharBackground(String name, HashMap<ScoreType, Modifier> scoreMods,
                          ArrayList<Proficiency> proficiencies, ArrayList<Feature> features) {
        this.name = name;
        this.scoreMods = scoreMods;
        this.proficiencies = proficiencies;
        this.features = features;
    }

    public String getName() {
        return name;
    }

    public HashMap<ScoreType, Modifier> getScores() {
        return scoreMods;
    }

    public ArrayList<Proficiency> getProficiencies() {
        return proficiencies;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }
}
