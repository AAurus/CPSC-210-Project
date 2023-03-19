package model;

import enums.*;
import exceptions.ClassNotFoundException;
import exceptions.FeatureNotFoundException;
import exceptions.InventoryItemNotFoundException;
import model.*;
import utility.Utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Character {
    /// A representation of a DnD Character.
    public static final int HASH_MULTIPLIER = 31;

    private String name;
    private HashMap<ScoreType, Integer> rolledAbilityScores;
    private HashMap<ScoreType, Integer> abilityScores;
    private HashMap<ScoreType, Integer> skillThrowBonuses;
    private HashMap<StatType, Integer> stats;
    private HashMap<Integer, Integer> hitDice;
    private String race;
    private String background;
    private ArrayList<String> classes;
    private HashMap<String, Integer> classLevels;
    private ArrayList<Feature> features;
    private ArrayList<InventoryItem> equippedItems;
    private ArrayList<InventoryItem> carriedItems;
    private ArrayList<InventoryItem> inventoryItems;

    public Character(String name, int strength, int dexterity, int constitution,
                     int intelligence, int wisdom, int charisma) {
        this.name = name;
        rolledAbilityScores = new HashMap<>();
        abilityScores = new HashMap<>();
        skillThrowBonuses = new HashMap<>();
        stats = new HashMap<>();
        hitDice = new HashMap<>();
        features = new ArrayList<>();
        equippedItems = new ArrayList<>();
        carriedItems = new ArrayList<>();
        inventoryItems = new ArrayList<>();
        classes = new ArrayList<>();
        classLevels = new HashMap<>();
        loadBaseScores(strength, dexterity, constitution, intelligence, wisdom, charisma);
        updateScores();
        updateStats();

        Utility.logEvent("New character created: " + name);
    }

    //MODIFIES: this
    //EFFECTS: initializes all stats and scores on character from race, background, classes, equipment
    public void reinitializeCharacter() {
        updateScores();
        updateStats();
    }

    //MODIFIES: this
    //EFFECTS: loads base scores into this
    private void loadBaseScores(int strength, int dexterity, int constitution,
                                int intelligence, int wisdom, int charisma) {
        rolledAbilityScores.put(ScoreType.STRENGTH, strength);
        rolledAbilityScores.put(ScoreType.DEXTERITY, dexterity);
        rolledAbilityScores.put(ScoreType.CONSTITUTION, constitution);
        rolledAbilityScores.put(ScoreType.INTELLIGENCE, intelligence);
        rolledAbilityScores.put(ScoreType.WISDOM, wisdom);
        rolledAbilityScores.put(ScoreType.CHARISMA, charisma);
    }

    //MODIFIES: this
    //EFFECTS: updates ability scores from race, background, classes, base scores
    private void updateScores() {
        ArrayList<HashMap<ScoreType, Modifier>> allScoreMods = getAllScoreMods();
        ArrayList<HashMap<ScoreType, Modifier>> allProfMods = getAllScoreProficiencyMods();

        HashMap<ScoreType, Modifier> baseAbilityScores = ScoreType.initBaseScoreMods(rolledAbilityScores);
        baseAbilityScores = ScoreType.applyAllScoresToList(baseAbilityScores, allScoreMods, ScoreType.BASE_SCORES);
        baseAbilityScores = ScoreType.applyAllScoresToList(baseAbilityScores, allProfMods, ScoreType.BASE_SCORES);
        abilityScores = ScoreType.finalizeScoreMods(baseAbilityScores);

        HashMap<ScoreType, Modifier> checkScores = ScoreType.initCheckScoreMods(abilityScores);
        checkScores = ScoreType.applyAllScoresToList(checkScores, allScoreMods, ScoreType.CHECK_SCORES);
        checkScores = ScoreType.applyAllScoresToList(checkScores, allProfMods, ScoreType.CHECK_SCORES);
        skillThrowBonuses = ScoreType.finalizeScoreMods(checkScores);
    }

    //MODIFIES: this
    //EFFECTS: updates stats according to ability scores
    private void updateStats() {
        ArrayList<HashMap<StatType, Modifier>> allStatMods = getAllStatMods();
        Modifier conPerLevel = new Modifier(1);
        Modifier walkSpeed = new Modifier(0);
        for (HashMap<StatType, Modifier> mods : allStatMods) {
            if (mods.keySet().contains(StatType.HIT_POINT_CON_PER_LEVEL)) {
                conPerLevel = mods.get(StatType.HIT_POINT_CON_PER_LEVEL).apply(conPerLevel);
            }
            if (mods.keySet().contains(StatType.WALK_SPEED)) {
                walkSpeed = mods.get(StatType.WALK_SPEED).apply(walkSpeed);
            }
        }
        HashMap<StatType, Modifier> charDeriveMap = new HashMap<>();
        charDeriveMap.put(StatType.HIT_POINT_CON_PER_LEVEL, conPerLevel);
        charDeriveMap.put(StatType.MAX_HIT_POINTS, new Modifier(ModifierType.BASE, conPerLevel.getValue().multiply(
                new BigDecimal(skillThrowBonuses.get(ScoreType.CON_CHECK) * getCharacterLevel()))));
        charDeriveMap.put(StatType.INITIATIVE_BONUS, new Modifier(skillThrowBonuses.get(ScoreType.DEX_CHECK)));
        charDeriveMap.put(StatType.DEXTERITY_ARMOR_BONUS, new Modifier(skillThrowBonuses.get(ScoreType.DEX_CHECK)));
        charDeriveMap.put(StatType.CARRY_CAPACITY, new Modifier(abilityScores.get(ScoreType.STRENGTH) * 15));
        charDeriveMap.put(StatType.PROFICIENCY_BONUS, new Modifier(calculateProficiencyBonus()));
        charDeriveMap.put(StatType.WALK_SPEED, walkSpeed);
        HashMap<StatType, Modifier> finalApplyMap = StatType.deriveStats(charDeriveMap);
        stats = StatType.finalizeStats(StatType.applyAllStatsToList(finalApplyMap, allStatMods,
                StatType.getNonDerivingStats()));
    }

    //EFFECTS: returns all score modifiers from applied to this
    private ArrayList<HashMap<ScoreType, Modifier>> getAllScoreMods() {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Feature f : features) {
            result.addAll(f.getAllScoreModifiers());
        }
        for (InventoryItem i : equippedItems) {
            if (i.getFeature() != null) {
                result.addAll(i.getFeature().getAllScoreModifiers());
            }
        }

        return result;
    }

    //EFFECTS: returns list of all proficiency-based score changes applied to this
    private ArrayList<HashMap<ScoreType, Modifier>> getAllScoreProficiencyMods() {
        BigDecimal profBonus = new BigDecimal(calculateProficiencyBonus());
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Feature f : features) {
            result.addAll(f.getAllProficiencyModifiers(profBonus));
        }
        for (InventoryItem i : equippedItems) {
            if (i.getFeature() != null) {
                result.addAll(i.getFeature().getAllProficiencyModifiers(profBonus));
            }
        }

        return result;
    }

    //EFFECTS: returns list of all stat changes applied to this
    private ArrayList<HashMap<StatType, Modifier>> getAllStatMods() {
        ArrayList<HashMap<StatType, Modifier>> result = new ArrayList<>();
        for (Feature f : features) {
            result.addAll(f.getAllStatModifiers());
        }
        for (InventoryItem i : equippedItems) {
            if (i.getFeature() != null) {
                features.add(i.getFeature());
            }
        }
        return result;
    }

    public HashMap<ScoreType, Integer> getRolledAbilityScores() {
        return rolledAbilityScores;
    }

    public HashMap<ScoreType, Integer> getAbilityScores() {
        return abilityScores;
    }

    public HashMap<ScoreType, Integer> getSkillThrowBonuses() {
        return skillThrowBonuses;
    }

    public HashMap<StatType, Integer> getStats() {
        return stats;
    }

    public String getRace() {
        return race;
    }

    public String getBackground() {
        return background;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public HashMap<String, Integer> getClassLevels() {
        return classLevels;
    }

    public int calculateProficiencyBonus() {
        BigDecimal undividedValueNoBase = new BigDecimal(getCharacterLevel()).subtract(new BigDecimal(1));
        BigDecimal unroundedValueNoBase = undividedValueNoBase.divide(new BigDecimal(4));
        int roundedValueNoBase = unroundedValueNoBase.setScale(0, RoundingMode.FLOOR).intValue();
        return roundedValueNoBase + 2;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public ArrayList<InventoryItem> getEquippedItems() {
        return equippedItems;
    }

    public ArrayList<InventoryItem> getCarriedItems() {
        return carriedItems;
    }

    public ArrayList<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public String getName() {
        return name;
    }

    public int getCharacterLevel() {
        int result = 0;
        for (int i = 0; i < classes.size(); i++) {
            result += classLevels.get(classes.get(i) + i);
        }
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setBackground(String background) {
        this.background = background;
    }
    
    public void addEquippedItem(InventoryItem item) {
        equippedItems.add(item);
        reinitializeCharacter();

        Utility.logEvent("Item " + item.getName() + " added to character "
                                                  + getName() + "'s equipment");
    }

    public void addCarriedItem(InventoryItem item) {
        carriedItems.add(item);
    }

    public void addInventoryItem(InventoryItem item) {
        inventoryItems.add(item);
    }

    public void addFeature(Feature feature) {
        features.add(feature);
        reinitializeCharacter();

        Utility.logEvent("Feature " + feature.getName() + " added to character "
                                                  + getName());
    }

    //MODIFIES: this
    //EFFECTS: removes the feature that matches the index passed in
    public void removeFeature(int index) throws IndexOutOfBoundsException {
        features.remove(index);
        reinitializeCharacter();
    }

    //MODIFIES: this
    //EFFECTS: removes first item from inventory that matches name
    public void removeFeature(String featureName) throws FeatureNotFoundException {
        for (Feature f : features) {
            if (f.getName().equals(featureName)) {
                features.remove(f);
                reinitializeCharacter();
                return;
            }
        }
        throw new FeatureNotFoundException();
    }

    public void addClass(String className) {
        classes.add(className);
        classLevels.put(className + classes.indexOf(className), 1);
        reinitializeCharacter();
    }

    //MODIFIES: this
    //EFFECTS: removes the first class that matches the name passed in
    public void removeClass(String className) throws ClassNotFoundException {
        if (classes.contains(className)) {
            classLevels.remove(className + classes.indexOf(className));
            classes.remove(className);
            reinitializeCharacter();
        } else {
            throw new ClassNotFoundException();
        }
    }

    //MODIFIES: this
    //EFFECTS: removes the class that matches the index passed in
    public void removeClass(int index) throws IndexOutOfBoundsException {
        classLevels.remove(classes.get(index) + index);
        classes.remove(index);
        reinitializeCharacter();
    }

    //MODIFIES: this
    //EFFECTS: increases the level of the first class that matches the name passed in by 1
    public void levelUp(String className) throws ClassNotFoundException {
        if (classes.contains(className)) {
            String classKey = className + classes.indexOf(className);
            classLevels.put(classKey, classLevels.get(classKey) + 1);
            reinitializeCharacter();
        } else {
            throw new ClassNotFoundException();
        }
    }

    //MODIFIES: this
    //EFFECTS: increases the level of the class that matches the index passed in by 1
    public void levelUp(int index) throws IndexOutOfBoundsException {
        String classKey = classes.get(index) + index;
        classLevels.put(classKey, classLevels.get(classKey) + 1);
        reinitializeCharacter();
    }

    //MODIFIES: this
    //EFFECTS: sets the level of the first class that matches the name passed in
    public void setLevel(String className, int value) throws ClassNotFoundException {
        if (classes.contains(className)) {
            String classKey = className + classes.indexOf(className);
            classLevels.put(classKey, value);
            reinitializeCharacter();
        } else {
            throw new ClassNotFoundException();
        }
    }

    //MODIFIES: this
    //EFFECTS: sets the level of the class that matches the index passed in
    public void setLevel(int index, int value) throws IndexOutOfBoundsException {
        String classKey = classes.get(index) + index;
        classLevels.put(classKey, value);
        reinitializeCharacter();
    }

    @Override
    public int hashCode() {
        ArrayList<Integer> hashComponents = new ArrayList<>();
        hashComponents.add(getName().hashCode());
        hashComponents.add(Utility.hashIfAble(race));
        hashComponents.add(Utility.hashIfAble(background));
        hashComponents.add(classes.hashCode());
        hashComponents.add(classLevels.hashCode());
        hashComponents.add(rolledAbilityScores.hashCode());
        hashComponents.add(features.hashCode());
        hashComponents.add(equippedItems.hashCode());
        hashComponents.add(carriedItems.hashCode());
        hashComponents.add(inventoryItems.hashCode());
        return Utility.hashCodeHelper(hashComponents, HASH_MULTIPLIER);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Character other = (Character) obj;
        return (this.hashCode() == other.hashCode());
    }
}
