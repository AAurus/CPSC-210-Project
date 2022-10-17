package model;

import enums.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Character {
    /// A representation of a DnD Character.

    private String name;
    private HashMap<ScoreType, Integer> rolledAbilityScores;
    private HashMap<ScoreType, Integer> abilityScores;
    private HashMap<ScoreType, Integer> skillThrowBonuses;
    private HashMap<StatType, Integer> stats;
    private HashMap<Integer, Integer> hitDice;
    private CharRace race;
    private CharBackground background;
    private ArrayList<CharClass> classes;
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
        equippedItems = new ArrayList<>();
        carriedItems = new ArrayList<>();
        inventoryItems = new ArrayList<>();
        classes = new ArrayList<>();
        loadBaseScores(strength, dexterity, constitution, intelligence, wisdom, charisma);
        updateScores();
        updateStats();
    }

    //MODIFIES: this
    //EFFECTS: initializes all stats and scores on character from race, background, classes, equipment
    public void reinitializeCharacter() {
        updateScores();
        updateStats();
    }

    //MODIFIES: this
    //EFFECTS: initializes all values on character from race, background, classes, equipment, WIPING INVENTORY
    public void initializeCharacter() {
        reinitializeCharacter();
        equippedItems = new ArrayList<>();
        carriedItems = new ArrayList<>();
        inventoryItems = new ArrayList<>();
        initStarterEquipment();
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
        ScoreType.applyAllScoresToList(baseAbilityScores, allScoreMods, ScoreType.BASE_SCORES);
        ScoreType.applyAllScoresToList(baseAbilityScores, allProfMods, ScoreType.BASE_SCORES);
        abilityScores = ScoreType.finalizeScoreMods(baseAbilityScores);

        HashMap<ScoreType, Modifier> checkScores = ScoreType.initCheckScoreMods(abilityScores);
        ScoreType.applyAllScoresToList(checkScores, allScoreMods, ScoreType.CHECK_SCORES);
        ScoreType.applyAllScoresToList(checkScores, allProfMods, ScoreType.CHECK_SCORES);
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
                conPerLevel.apply(mods.get(StatType.HIT_POINT_CON_PER_LEVEL));
            }
            if (mods.keySet().contains(StatType.WALK_SPEED)) {
                walkSpeed.apply(mods.get(StatType.WALK_SPEED));
            }
        }
        HashMap<StatType, Modifier> charDeriveMap = new HashMap<>();
        charDeriveMap.put(StatType.MAX_HIT_POINTS, new Modifier(ModifierType.BASE, conPerLevel.getValue().multiply(
                new BigDecimal(skillThrowBonuses.get(ScoreType.CON_CHECK) * getCharacterLevel()
                        + getClassesHitPoints()))));
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

        if (race != null) {
            result.addAll(race.getAllScores());
            result.addAll(race.getAllFeatureScoreMods());
        }
        if (background != null) {
            result.addAll(background.getAllFeatureScoreMods());
        }
        for (CharClass c : classes) {
            result.addAll(c.getAllFeatureScoreMods());
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
        if (race != null) {
            result.addAll(race.getAllScoreProficienciesApplied(profBonus));
        }
        if (background != null) {
            result.addAll(background.getAllScoreProficienciesApplied(profBonus));
        }
        if (!classes.isEmpty()) {
            result.addAll(classes.get(0).getAllProficienciesApplied(profBonus, true));
            for (int i = 1; i < classes.size(); i++) {
                result.addAll(classes.get(i).getAllProficienciesApplied(profBonus, false));
            }
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
        ArrayList<Feature> features = new ArrayList<>();
        if (race != null) {
            features.addAll(race.getFeatures());
        }
        if (background != null) {
            features.addAll(background.getFeatures());
        }
        for (CharClass c : classes) {
            features.addAll(c.getAllFeaturesLevelled());
        }
        for (InventoryItem i : equippedItems) {
            if (i.getFeature() != null) {
                features.add(i.getFeature());
            }
        }
        List<Feature> statFeatures = Feature.getAllReachableFeaturesOfType(features, FeatureType.STAT);
        for (Feature f : statFeatures) {
            result.add(f.getStatMod());
        }
        return result;
    }

    //MODIFIES: this
    //EFFECTS: adds all background and class starter equipment to inventory
    private void initStarterEquipment() {
        if (background != null) {
            inventoryItems.addAll(background.getEquipment());
        }
        if (!classes.isEmpty()) {
            inventoryItems.addAll(classes.get(0).getEquipment());
        }
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

    public CharRace getRace() {
        return race;
    }

    public CharBackground getBackground() {
        return background;
    }

    public ArrayList<CharClass> getClasses() {
        return classes;
    }

    public int getCharacterLevel() {
        int result = 0;
        for (CharClass c : classes) {
            result += c.getLevel();
        }
        return result;
    }

    public int calculateProficiencyBonus() {
        BigDecimal undividedValue = new BigDecimal(getCharacterLevel()).add(new BigDecimal(1));
        BigDecimal unroundedValue = undividedValue.divide(new BigDecimal(4));
        return unroundedValue.setScale(0, RoundingMode.FLOOR).intValue();
    }

    public int getClassesHitPoints() {
        int result = 0;
        for (CharClass c : classes) {
            for (int i : c.getRolledHitPoints()) {
                result += i;
            }
        }
        return result;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setRace(CharRace race) {
        this.race = race;
        reinitializeCharacter();
    }

    public void setBackground(CharBackground background) {
        this.background = background;
        reinitializeCharacter();
    }

    public void addEquippedItem(InventoryItem item) {
        equippedItems.add(item);
        reinitializeCharacter();
    }

    public void addCarriedItem(InventoryItem item) {
        carriedItems.add(item);
    }

    public void addInventoryItem(InventoryItem item) {
        inventoryItems.add(item);
    }

    //MODIFIES: this
    //EFFECTS: removes first item from equipped items that matches name
    public void removeEquippedItem(String itemName) {
        for (InventoryItem i : equippedItems) {
            if (i.getName().equals(itemName)) {
                equippedItems.remove(i);
                updateScores();
                break;
            }
        }
        reinitializeCharacter();
    }

    //MODIFIES: this
    //EFFECTS: removes first item from carried items that matches name
    public void removeCarriedItem(String itemName) {
        for (InventoryItem i : carriedItems) {
            if (i.getName().equals(itemName)) {
                carriedItems.remove(i);
                break;
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: removes first item from inventory that matches name
    public void removeInventoryItem(String itemName) {
        for (InventoryItem i : inventoryItems) {
            if (i.getName().equals(itemName)) {
                inventoryItems.remove(i);
                break;
            }
        }
    }

    public void addClass(CharClass charClass) {
        classes.add(charClass);
        reinitializeCharacter();
    }

    public void removeClass(String className) {
        for (CharClass c : classes) {
            if (className.trim().equals(c.getName())) {
                classes.remove(c);
                break;
            }
        }
    }

    public void removeClass(int index) {
        classes.remove(index);
        reinitializeCharacter();
    }

    //REQUIRES: index must be in range for classes
    //MODIFIES: this
    //EFFECTS: levels up on a certain class
    public void levelUp(int index) {
        if (index >= 0 && index < classes.size()) {
            classes.get(index).levelUp();
            reinitializeCharacter();
        }
    }

    //REQUIRES: index must be in range for classes
    //MODIFIES: this
    //EFFECTS: rolls bonus hit points for a certain class, if possible
    public void rollClassHitPoints(int index) {
        if (index >= 0 && index < classes.size()) {
            classes.get(index).rollHitPoints();
            reinitializeCharacter();
        }
    }
}
