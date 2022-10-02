package model;

import java.util.*;

public interface CharacterModifier {
    ///An interface to describe certain preset options (Races, Classes, Backgrounds, Feats, etc.) that modify the
    /// character in more than one way.

    public int[] getScores();

    public ArrayList<Feature> getFeatures();
}
