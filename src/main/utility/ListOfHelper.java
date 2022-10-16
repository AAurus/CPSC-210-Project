package utility;

import enums.*;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class ListOfHelper {
    ///A utility class SOLELY CREATED to replicate the ListOfHelper.listOf(E... elements) method.

    //EFFECTS: returns a List containing the elements passed in
    public static final List<ScoreType> listOf(ScoreType... elements) {
        List<ScoreType> result = new ArrayList<>();
        for (ScoreType element : elements) {
            result.add(element);
        }
        return result;
    }

    //EFFECTS: returns a List containing the elements passed in
    public static final List<StatType> listOf(StatType... elements) {
        List<StatType> result = new ArrayList<>();
        for (StatType element : elements) {
            result.add(element);
        }
        return result;
    }

    //EFFECTS: returns a List containing the elements passed in
    public static final List<ProficiencyType> listOf(ProficiencyType... elements) {
        List<ProficiencyType> result = new ArrayList<>();
        for (ProficiencyType e : elements) {
            result.add(e);
        }
        return result;
    }

    //EFFECTS: returns a List containing the elements passed in
    public static final List<ModifierType> listOf(ModifierType... elements) {
        List<ModifierType> result = new ArrayList<>();
        for (ModifierType e : elements) {
            result.add(e);
        }
        return result;
    }

    //EFFECTS: returns a List containing the elements passed in
    public static final List<Feature> listOf(Feature... elements) {
        List<Feature> result = new ArrayList<>();
        for (Feature e : elements) {
            result.add(e);
        }
        return result;
    }

    //EFFECTS: returns a List containing the elements passed in
    public static final List<Proficiency> listOf(Proficiency... elements) {
        List<Proficiency> result = new ArrayList<>();
        for (Proficiency e : elements) {
            result.add(e);
        }
        return result;
    }

    //EFFECTS: returns a List containing the elements passed in
    public static final List<InventoryItem> listOf(InventoryItem... elements) {
        List<InventoryItem> result = new ArrayList<>();
        for (InventoryItem e : elements) {
            result.add(e);
        }
        return result;
    }

    //EFFECTS: returns a List containing the elements passed in
    public static final List<String> listOf(String... elements) {
        List<String> result = new ArrayList<>();
        for (String e : elements) {
            result.add(e);
        }
        return result;
    }

    //EFFECTS: returns a List containing the elements passed in
    public static final List<CharClass> listOf(CharClass... elements) {
        List<CharClass> result = new ArrayList<>();
        for (CharClass e : elements) {
            result.add(e);
        }
        return result;
    }

    //EFFECTS: returns a List containing the elements passed in
    public static final List<CharRace> listOf(CharRace... elements) {
        List<CharRace> result = new ArrayList<>();
        for (CharRace e : elements) {
            result.add(e);
        }
        return result;
    }
}
