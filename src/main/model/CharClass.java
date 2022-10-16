package model;

import enums.FeatureType;
import enums.ProficiencyType;
import enums.ScoreType;
import utility.ListOfHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CharClass extends HasFeatures {
    /// A representation of a D&D Class.
    private String name;
    private ArrayList<Proficiency> baseProficiencies;
    private ArrayList<Proficiency> multiClassProficiencies;
    private ArrayList<CharClass> subClasses;
    private ArrayList<InventoryItem> equipment;
    private String description;

    private int subClassSelect;
    private int subClassLevel;

    private int hitDie;
    private ArrayList<Integer> rolledHitPoints = new ArrayList<>();
    private int level;

    //Main Character Class Constructor
    public CharClass(String name, ArrayList<Proficiency> baseProficiencies,
                     ArrayList<Proficiency> multiClassProficiencies, ArrayList<Feature> features,
                     ArrayList<InventoryItem> equipment, ArrayList<CharClass> subClasses,
                     int subClassLevel, int hitDie) {
        this.name = name;
        this.baseProficiencies = baseProficiencies;
        this.multiClassProficiencies = multiClassProficiencies;
        this.features = new ArrayList<>();
        for (Feature f : features) {
            if (f.getName().matches("Level\\s(\\d*)\\sFeatures") && f.getType().equals(FeatureType.MULTI)) {
                int featureIndex = Integer.parseInt(f.getName().replaceAll("[^0-9]", "")) - 1;
                initFeatures(featureIndex);
                features.set(featureIndex, f);
            } else {
                addFeature(f, 1);
            }
        }
        this.equipment = equipment;
        this.subClasses = subClasses;
        this.subClassLevel = subClassLevel;
        this.hitDie = hitDie;
        this.rolledHitPoints.add(hitDie);
        this.description = "";

        this.subClassSelect = -1;
        this.level = 1;
    }

    //Character Subclass Constructor
    public CharClass(String name, ArrayList<Feature> features) {
        this.name = name;
        this.baseProficiencies = new ArrayList<>();
        this.multiClassProficiencies = new ArrayList<>();
        this.features = features;
        this.equipment = new ArrayList<>();
        this.subClasses = new ArrayList<>();
        this.subClassLevel = -1;
        this.hitDie = 0;
        this.description = "";

        this.subClassSelect = -1;
        this.level = 0;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Proficiency> getBaseProficiencies() {
        return baseProficiencies;
    }

    public ArrayList<Proficiency> getMultiClassProficiencies() {
        return multiClassProficiencies;
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

    public List<CharClass> getSubClasses() {
        return subClasses;
    }

    public ArrayList<InventoryItem> getEquipment() {
        return equipment;
    }

    public int getHitDie() {
        return hitDie;
    }

    public ArrayList<Integer> getRolledHitPoints() {
        return rolledHitPoints;
    }

    public String getDescription() {
        return description;
    }



    public void addBaseProficiency(Proficiency proficiency) {
        baseProficiencies.add(proficiency);
    }

    public void addMultiClassProficiency(Proficiency proficiency) {
        multiClassProficiencies.add(proficiency);
    }

    @Override
    public void addFeature(Feature feature) {
        addFeature(feature, 1);
    }

    //EFFECTS: adds a feature to a certain level slot
    public void addFeature(Feature feature, int level) {
        int levelSlot = level - 1;
        if (levelSlot >= 0 && levelSlot < features.size() && features.get(levelSlot) != null) {
            Feature levelFeature = features.get(levelSlot);
            ArrayList<Feature> featureList = new ArrayList<>(levelFeature.getFeatures());
            featureList.add(feature);
            features.set(levelSlot, new Feature(levelFeature.getName(), featureList,
                    levelFeature.isChoice(), levelFeature.getDescription()));
        } else {
            initFeatures(levelSlot);
            features.set(levelSlot, new Feature("Level " + level + " Features", ListOfHelper.listOf(feature)));
        }
    }

    //MODIFIES: this
    //EFFECTS: adds a certain number of empty Features of type MULTI to features, to prepare them for
    //         feature input; does NOT override existing features
    public void initFeatures(int upToSlot) {
        for (int i = 0; i <= upToSlot; i++) {
            if ((features.size() - 1) < i) {
                features.add(new Feature("Level " + Integer.toString(i + 1) + " Features", new ArrayList<>()));
            }
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEquipment(InventoryItem item) {
        equipment.add(item);
    }

    public void addSubClass(CharClass subClass) {
        subClasses.add(subClass);
    }

    //MODIFIES: this
    //EFFECTS: removes the first proficiency that matches
    public void removeBaseProficiency(String profName) {
        for (Proficiency p : baseProficiencies) {
            if (p.getProficiencyName().equals(profName)) {
                baseProficiencies.remove(p);
                break;
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: removes the first proficiency that matches
    public void removeMultiClassProficiency(String profName) {
        for (Proficiency p : multiClassProficiencies) {
            if (p.getProficiencyName().equals(profName)) {
                multiClassProficiencies.remove(p);
                break;
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: removes the first item that matches
    public void removeEquipment(String itemName) {
        for (InventoryItem ii : equipment) {
            if (ii.getName().equals(itemName)) {
                equipment.remove(ii);
                break;
            }
        }
    }

    //EFFECTS: removes the first features that matches, from highest level to lowest
    @Override
    public void removeFeature(String featureName) {
        for (int i = features.size(); i > 0; i++) {
            if (removeFeature(featureName, i)) {
                break;
            }
        }
    }

    //REQUIRES: level is within range of features
    //MODIFIES: this
    //EFFECTS: removes the first feature that matches in the level and returns true on a successful removal, false on
    //         failure
    public boolean removeFeature(String featureName, int level) {
        int levelSlot = level - 1;
        if (levelSlot >= 0 && levelSlot < features.size() && features.get(levelSlot) != null) {
            Feature levelFeature = features.get(levelSlot);
            List<Feature> featureList = levelFeature.getFeatures();
            for (Feature f : featureList) {
                if (f.getName().equals(featureName)) {
                    featureList.remove(f);
                    features.set(levelSlot, new Feature(levelFeature.getName(), featureList,
                                                        levelFeature.isChoice(), levelFeature.getDescription()));
                    return true;
                }
            }
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: removes the first subclass that matches
    public void removeSubClass(String subClassName) {
        CharClass chosenClass = null;
        if (subClassSelect >= 0) {
            chosenClass = subClasses.get(subClassSelect);
        }
        for (CharClass cc : subClasses) {
            if (cc.getName().equals(subClassName)) {
                if (subClasses.indexOf(cc) == subClassSelect) {
                    resetSubClassSelect();
                }
                subClasses.remove(cc);
                break;
            }
        }
        if (chosenClass != null) {
            subClassSelect = subClasses.indexOf(chosenClass);
        }
    }

    public void resetSubClassSelect() {
        subClassSelect = -1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    //EFFECTS: increments level by 1
    public void levelUp() {
        level++;
    }



    //EFFECTS: returns ArrayList of features that can be accessed at current level
    public ArrayList<Feature> getSurfaceFeaturesLevelled() {
        ArrayList<Feature> result = new ArrayList<>();
        for (int i = 0; i < level; i++) {
            result.add(features.get(i));
        }
        return result;
    }

    //EFFECTS: returns ArrayList of features that can be accessed at current level including subclasses
    public ArrayList<Feature> getAllFeaturesLevelled() {
        ArrayList<Feature> result = new ArrayList<>();
        for (int i = 0; i < level; i++) {
            if (i < features.size()) {
                result.add(features.get(i));
            }
            if (subClassSelect >= 0) {
                if (i < getSubClass().getFeatures().size()) {
                    result.add(getSubClass().getFeatures().get(i));
                }
            }
        }
        return result;
    }

    //REQUIRES: select must be within range for subClasses ArrayList
    public boolean selectSubclass(int select) {
        if (level >= subClassLevel && select >= 0 && select < subClasses.size()) {
            subClassSelect = select;
            return true;
        }
        return false;
    }

    //EFFECTS: returns number of hit points left unrolled
    public int getHitPointRollsAvailable() {
        return level - rolledHitPoints.size();
    }

    //MODIFIES: this
    //EFFECTS: rolls hit points from level up
    public void rollHitPoints() {
        if (getHitPointRollsAvailable() > 0) {
            Random dieRoller = new Random();
            rolledHitPoints.add(dieRoller.nextInt(hitDie) + 1);
        }
    }

    //MODIFIES: this
    //EFFECTS: removes most recent value in rolled hit points
    public void undoHitPointRoll() {
        if (!rolledHitPoints.isEmpty()) {
            rolledHitPoints.remove(rolledHitPoints.size() - 1);
        }
    }

    @Override
    public ArrayList<HashMap<ScoreType, Modifier>> getAllFeatureScoreMods() {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Feature f : getAllFeaturesLevelled()) {
            result.addAll(f.getAllScoreModifiers());
        }
        return result;
    }

    public ArrayList<HashMap<ScoreType, Modifier>> getAllProficienciesApplied(BigDecimal profBonus, boolean baseClass) {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        if (baseClass) {
            for (Proficiency p : baseProficiencies) {
                if (p.getType().equals(ProficiencyType.SCORE)) {
                    result.add(p.generateScoreMap(profBonus));
                }
            }
        } else {
            for (Proficiency p : multiClassProficiencies) {
                if (p.getType().equals(ProficiencyType.SCORE)) {
                    result.add(p.generateScoreMap(profBonus));
                }
            }
        }
        for (Feature f : getAllFeaturesLevelled()) {
            result.addAll(f.getAllProficiencyModifiers(profBonus));
        }
        if (subClassSelect >= 0) {
            result.addAll(getSubClass().getAllProficienciesApplied(profBonus));
        }
        return result;
    }

    public ArrayList<HashMap<ScoreType, Modifier>> getAllProficienciesApplied(BigDecimal profBonus) {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Proficiency p : baseProficiencies) {
            if (p.getType().equals(ProficiencyType.SCORE)) {
                result.add(p.generateScoreMap(profBonus));
            }
        }
        for (Feature f : getAllFeaturesLevelled()) {
            result.addAll(f.getAllProficiencyModifiers(profBonus));
        }
        if (subClassSelect >= 0) {
            result.addAll(getSubClass().getAllProficienciesApplied(profBonus));
        }
        return result;
    }
}
