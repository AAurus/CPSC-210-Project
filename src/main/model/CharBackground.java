package model;

import enums.ProficiencyType;
import enums.ScoreType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharBackground extends HasFeatures {
    /// A representation of a DnD Background.
    private String name;
    private ArrayList<Proficiency> proficiencies;
    private ArrayList<String> languages;
    private ArrayList<InventoryItem> equipment;
    private String description;

    public CharBackground(String name) {
        this.name = name;
        this.proficiencies = new ArrayList<>();
        this.languages = new ArrayList<>();
        this.features = new ArrayList<>();
        this.equipment = new ArrayList<>();
        this.description = "";
    }

    public CharBackground(String name, ArrayList<Proficiency> proficiencies,
                          ArrayList<String> languages, ArrayList<Feature> features,
                          ArrayList<InventoryItem> equipment, String description) {
        this.name = name;
        this.proficiencies = proficiencies;
        this.languages = languages;
        this.features = features;
        this.equipment = equipment;
        this.description = description;
    }



    public void addProficiency(Proficiency proficiency) {
        proficiencies.add(proficiency);
    }

    public void addLanguage(String langName) {
        languages.add(langName);
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



    //EFFECTS: removes the first proficiency that matches
    public void removeProficiency(String profName) {
        for (Proficiency p : proficiencies) {
            if (p.getProficiencyName().equals(profName)) {
                proficiencies.remove(p);
                break;
            }
        }
    }

    public void removeLanguage(String langName) {
        languages.remove(langName);
    }

    public void removeEquipment(String itemName) {
        for (InventoryItem i : equipment) {
            if (i.getName().equals(itemName)) {
                equipment.remove(i);
                break;
            }
        }
    }


    public String getName() {
        return name;
    }

    public ArrayList<Proficiency> getProficiencies() {
        return proficiencies;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public ArrayList<InventoryItem> getEquipment() {
        return equipment;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<HashMap<ScoreType, Modifier>> getAllScoreProficienciesApplied(BigDecimal profBonus) {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Proficiency p : proficiencies) {
            if (p.getType().equals(ProficiencyType.SCORE)) {
                result.add(p.generateScoreMap(profBonus));
            }
        }
        for (Feature f : features) {
            f.getAllProficiencyModifiers(profBonus);
        }
        return result;
    }
}
