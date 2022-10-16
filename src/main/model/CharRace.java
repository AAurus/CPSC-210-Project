package model;

import enums.FeatureType;
import enums.ProficiencyType;
import enums.ScoreType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class CharRace extends HasFeatures {
    /// A representation of a DnD Race.
    private String name;
    private HashMap<ScoreType, Modifier> scores;
    private ArrayList<Proficiency> proficiencies;
    private ArrayList<String> languages;
    private ArrayList<CharRace> subRaces;
    private int subRaceSelect;
    private String description;

    public CharRace(String name) {
        this.name = name;
        this.scores = new HashMap<>();
        this.proficiencies = new ArrayList<>();
        this.languages = new ArrayList<>();
        this.features = new ArrayList<>();
        this.subRaces = new ArrayList<>();
        this.subRaceSelect = -1;
        this.description = "";
    }

    public CharRace(String name, HashMap<ScoreType, Modifier> scores, ArrayList<Proficiency> proficiencies,
                    ArrayList<String> languages, ArrayList<Feature> features,
                    ArrayList<CharRace> subRaces, String description) {
        this.name = name;
        this.scores = scores;
        this.proficiencies = proficiencies;
        this.languages = languages;
        this.features = features;
        this.subRaces = subRaces;
        this.subRaceSelect = -1;
        this.description = description;
    }



    public void setScoreMod(ScoreType slot, Modifier mod) {
        scores.put(slot, mod);
    }

    public void addProficiency(Proficiency proficiency) {
        proficiencies.add(proficiency);
    }

    public void addLanguage(String langName) {
        languages.add(langName);
    }

    public void addSubRace(CharRace subRace) {
        subRaces.add(subRace);
    }

    //MODIFIES: this
    //EFFECTS: returns true if subrace was selected successfully, false otherwise
    public boolean selectSubRace(int choiceIndex) {
        if (choiceIndex >= 0 && choiceIndex < getSubRaces().size()) {
            subRaceSelect = choiceIndex;
            return true;
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: returns true if subrace was selected successfully, false otherwise
    public boolean selectSubRace(String choiceName) {
        for (CharRace r : subRaces) {
            if (r.getName().equals(choiceName)) {
                subRaceSelect = subRaces.indexOf(r);
                return true;
            }
        }
        return false;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void resetScoreMod(ScoreType slot) {
        scores.remove(slot);
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

    //EFFECTS: removes the first subrace that matches
    public void removeSubRace(String subRaceName) {
        CharRace chosenRace = null;
        if (subRaceSelect >= 0) {
            chosenRace = subRaces.get(subRaceSelect);
        }
        for (CharRace r : subRaces) {
            if (r.getName().equals(subRaceName)) {
                if (subRaces.indexOf(r) == subRaceSelect) {
                    resetSubRaceSelect();
                }
                subRaces.remove(r);
                break;
            }
        }
        if (chosenRace != null) {
            subRaceSelect = subRaces.indexOf(chosenRace);
        }
    }

    public void resetSubRaceSelect() {
        subRaceSelect = -1;
    }

    public String getName() {
        return name;
    }

    public ArrayList<HashMap<ScoreType, Modifier>> getAllScores() {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        result.add(getScores());
        if (subRaceSelect >= 0) {
            result.add(subRaces.get(subRaceSelect).getScores());
        }
        return result;
    }

    public HashMap<ScoreType, Modifier> getScores() {
        return scores;
    }

    public ArrayList<Proficiency> getProficiencies() {
        return proficiencies;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public ArrayList<CharRace> getSubRaces() {
        return subRaces;
    }

    public CharRace getSubRace() {
        if (subRaceSelect >= 0) {
            return subRaces.get(subRaceSelect);
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    //EFFECTS: returns list of all proficiency-based score modifier maps
    public ArrayList<HashMap<ScoreType, Modifier>> getAllScoreProficienciesApplied(BigDecimal profBonus) {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Proficiency p : proficiencies) {
            if (p.getType().equals(ProficiencyType.SCORE)) {
                result.add(p.generateScoreMap(profBonus));
            }
        }
        for (Feature f : features) {
            result.addAll(f.getAllProficiencyModifiers(profBonus));
        }
        if (subRaceSelect >= 0) {
            result.addAll(getSubRace().getAllScoreProficienciesApplied(profBonus));
        }
        return result;
    }

    //EFFECTS: returns list of all score-based score modifier maps
    public ArrayList<HashMap<ScoreType, Modifier>> getAllScoresApplied() {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        result.add(getScores());
        for (Feature f : features) {
            result.addAll(f.getAllScoreModifiers());
        }
        if (subRaceSelect >= 0) {
            result.addAll(getSubRace().getAllScoresApplied());
        }
        return result;
    }

    //EFFECTS: Overridden to go through subclasses
    @Override
    public ArrayList<HashMap<ScoreType, Modifier>> getAllFeatureScoreMods() {
        ArrayList<HashMap<ScoreType, Modifier>> result = new ArrayList<>();
        for (Feature f : Feature.getAllReachableFeaturesOfType(features, FeatureType.SCORE)) {
            result.add(f.getScoreMod());
        }
        if (getSubRace() != null) {
            result.addAll(getSubRace().getAllFeatureScoreMods());
        }
        return result;
    }
}
