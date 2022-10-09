package model;

import enums.ScoreType;

import java.math.BigDecimal;
import java.util.*;

public interface CharacterModifier {
    ///A superclass to describe certain preset options (Races, Classes, Backgrounds, Feats, etc.) that modify the
    /// character in more than one way.

    public String getName();

    public HashMap<ScoreType, Modifier> getScores();

    public ArrayList<Proficiency> getProficiencies();

    public ArrayList<Feature> getFeatures();

    public static ArrayList<HashMap<ScoreType, Modifier>> getAllScores(CharacterModifier charMod) {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        result.add(charMod.getScores());
        for (Feature f : charMod.getFeatures()) {
            result.addAll(f.getAllModifiersWithSlots());
        }
        return result;
    }
}
