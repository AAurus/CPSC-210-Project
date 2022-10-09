package model;

import enums.ScoreType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class CharClass implements CharacterModifier {
    /// A representation of a D&D Class.
    private final String name;
    private final ArrayList<Proficiency> baseProficiencies;
    private final ArrayList<Proficiency> multiClassProficiencies;
    private final ArrayList<Feature> baseFeatures;
    private final ArrayList<Feature> levelFeatures; //one feature of "MULTI" type for each level
    private final ArrayList<CharClass> subClasses;
    private int subClassSelect;
    private int subClassLevel;
    private int level;

    public CharClass(String name, ArrayList<Proficiency> baseProficiencies, ArrayList<Feature> baseFeatures,
                     ArrayList<Proficiency> multiClassProficiencies, ArrayList<Feature> levelFeatures,
                     ArrayList<CharClass> subClasses, int subClassLevel) {
        this.name = name;
        this.baseProficiencies = baseProficiencies;
        this.baseFeatures = baseFeatures;
        this.multiClassProficiencies = multiClassProficiencies;
        this.levelFeatures = levelFeatures;
        this.subClasses = subClasses;
        this.subClassLevel = subClassLevel;

        subClassSelect = -1;
        level = 1;
    }

    public String getName() {
        return name;
    }

    public HashMap<ScoreType, Modifier> getScores() {
        return null;
    }

    public ArrayList<Proficiency> getProficiencies() {
        return baseProficiencies;
    }

    public ArrayList<Proficiency> getMultiClassProficiencies() {
        return multiClassProficiencies;
    }

    public ArrayList<Feature> getFeatures() {
        ArrayList<Feature> result = new ArrayList<>();
        result.addAll(baseFeatures);
        result.addAll(levelFeatures);
        return result;
    }

    public ArrayList<CharClass> getAllSubClasses() {
        return subClasses;
    }

    public CharClass getSubClass() {
        if (subClassSelect >= 0 && subClassSelect < subClasses.size()) {
            return subClasses.get(subClassSelect);
        }
        return null;
    }

    public int getLevel() {
        return level;
    }

    public int getSubClassLevel() {
        return subClassLevel;
    }



    public void setLevel(int level) {
        this.level = level;
    }

    //EFFECTS: increments level by 1
    public void levelUp() {
        level++;
    }



    //EFFECTS: returns ArrayList of features that can be accessed at current level
    public ArrayList<Feature> getFeaturesLevelled() {
        ArrayList<Feature> result = new ArrayList<>();
        for (int i = 0; i < level; i++) {
            result.add(levelFeatures.get(i));
        }
        return result;
    }


    //REQUIRES: select must be within range for subClasses ArrayList
    public boolean selectSubclass(int select) {
        if (level >= subClassLevel) {
            subClassSelect = select;
            return true;
        }
        return false;
    }
}
