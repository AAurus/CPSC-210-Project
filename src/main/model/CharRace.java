package model;

import enums.ScoreType;

import java.util.*;

public class CharRace implements CharacterModifier {
    ///A representation of the various ability score bonuses and features that come with a certain D&D race.
    private final String name;
    private final HashMap<ScoreType, Modifier> scoreMods;
    private final ArrayList<Proficiency> proficiencies;
    private final ArrayList<Feature> features;

    public CharRace(String name, HashMap<ScoreType, Modifier> scoreMods,
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
