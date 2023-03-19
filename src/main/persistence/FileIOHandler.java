package persistence;

import enums.*;
import model.*;
import model.Character;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.Utility;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class FileIOHandler {
    /// A class for reading and writing all objects.
    public static final String ILLEGAL_FILE_NAME_CHARACTERS = "[#%&{}\\\\$!'\":@<>*?/\\s+`|=]+";
    public static final String SAVE_FILE_DIRECTORY_PATH = "data/saves";

    //REQUIRES: jsonObject stores values corresponding to Modifier class
    //EFFECTS: creates a Proficiency from fields stored in a suitable jsonObject
    public Modifier readModifier(JSONObject jsonObject) {
        ModifierType type = jsonObject.getEnum(ModifierType.class, "type");
        BigDecimal value = jsonObject.getBigDecimal("value");

        return new Modifier(type, value);
    }

    //REQUIRES: jsonObject stores values corresponding to Proficiency class
    //EFFECTS: creates a Proficiency from fields stored in a suitable jsonObject
    public Proficiency readProficiency(JSONObject jsonObject) {
        ProficiencyType type = jsonObject.getEnum(ProficiencyType.class, "type");
        BigDecimal multiplier = jsonObject.getBigDecimal("multiplier");

        if (type.equals(ProficiencyType.SCORE)) {
            ScoreType score = (ScoreType) jsonObject.get("score");
            return new Proficiency(score, multiplier);
        } else {
            String item = jsonObject.getString("item");
            return new Proficiency(item, multiplier);
        }
    }

    //REQUIRES: jsonObject stores values corresponding to Feature class
    //EFFECTS: creates a Feature from fields stored in a suitable jsonObject
    public Feature readFeature(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String description = "";
        if (jsonObject.has("description")) {
            description = jsonObject.getString("description");
        }
        FeatureType type = jsonObject.getEnum(FeatureType.class, "type");
        switch (type) {
            case SCORE:
                return readFeatureScore(name, description, jsonObject);
            case STAT:
                return readFeatureStat(name, description, jsonObject);
            case PROFICIENCY:
                Proficiency proficiency = readProficiency(jsonObject.getJSONObject("proficiency"));
                return new Feature(name, proficiency, description);
            case MULTI:
                return readFeatureMulti(name, description, jsonObject);
            case ETC:
            default:
                return new Feature(name, description);
        }
    }

    //EFFECTS: helper method: handles score reading for readFeature
    private Feature readFeatureScore(String name, String description, JSONObject jsonObject) {
        JSONObject scoreMapObject = jsonObject.getJSONObject("scoreMod");
        ScoreType scoreType = null;
        for (String s : scoreMapObject.keySet()) {
            scoreType = ScoreType.valueOf(s);
            break;
        }
        Modifier scoreModifier = readModifier(scoreMapObject.getJSONObject(scoreType.name()));
        return new Feature(name, scoreType, scoreModifier, description);
    }

    //EFFECTS: helper method: handles stat reading for readFeature
    private Feature readFeatureStat(String name, String description, JSONObject jsonObject) {
        JSONObject statMapObject = jsonObject.getJSONObject("statMod");
        StatType statType = null;
        for (String s : statMapObject.keySet()) {
            statType = StatType.valueOf(s);
            break;
        }
        Modifier statModifier = readModifier(statMapObject.getJSONObject(statType.name()));
        return new Feature(name, statType, statModifier, description);
    }

    //EFFECTS: helper method: handles reading a list of features, choices, etc for readFeature
    private Feature readFeatureMulti(String name, String description, JSONObject jsonObject) {
        ArrayList<Feature> features = new ArrayList<>();
        JSONArray featureArray = jsonObject.getJSONArray("features");
        for (int i = 0; i < featureArray.length(); i++) {
            features.add(readFeature(featureArray.getJSONObject(i)));
        }
        boolean choice = jsonObject.getBoolean("choice");
        int choiceCount = 0;
        ArrayList<Integer> choices = new ArrayList<>();
        if (choice) {
            choiceCount = jsonObject.getInt("choiceCount");
            JSONArray choiceArray = jsonObject.getJSONArray("choices");
            for (int i = 0; i < choiceArray.length(); i++) {
                choices.add(choiceArray.getInt(i));
            }
        }
        return new Feature(name, features, choice, choiceCount, choices, description);
    }

    //REQUIRES: jsonObject stores values corresponding to InventoryItem class
    //EFFECTS: creates an InventoryItem from fields stored in a suitable jsonObject
    public InventoryItem readInventoryItem(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        BigDecimal weight = jsonObject.getBigDecimal("weight");
        BigDecimal price = jsonObject.getBigDecimal("price");

        InventoryItem result = new InventoryItem(name, description);
        result.setWeight(weight);
        result.setPrice(price);
        if (jsonObject.has("feature")) {
            result.setFeature(readFeature(jsonObject.getJSONObject("feature")));
        }
        return result;
    }

    //REQUIRES: jsonObject stores values corresponding to Character class
    //EFFECTS: creates a character from fields stored in a suitable jsonObject
    public Character readCharacter(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        JSONObject rollMap = jsonObject.getJSONObject("rolledAbilityScores");
        int strengthRoll = rollMap.getInt("STRENGTH");
        int dexterityRoll = rollMap.getInt("DEXTERITY");
        int constitutionRoll = rollMap.getInt("CONSTITUTION");
        int intelligenceRoll = rollMap.getInt("INTELLIGENCE");
        int wisdomRoll = rollMap.getInt("WISDOM");
        int charismaRoll = rollMap.getInt("CHARISMA");

        Character result = new Character(name, strengthRoll, dexterityRoll, constitutionRoll,
                                         intelligenceRoll, wisdomRoll, charismaRoll);
        return readCharacterOptionals(result, jsonObject);
    }

    //EFFECTS: helper method: handles the race, background, and classes for readCharacter
    private Character readCharacterOptionals(Character character, JSONObject jsonObject) {
        character.setRace(jsonObject.optString("race"));
        character.setBackground(jsonObject.optString("background"));
        JSONArray classArray = jsonObject.getJSONArray("classes");
        JSONObject classLevels = jsonObject.getJSONObject("classLevels");
        for (int i = 0; i < classArray.length(); i++) {
            character.addClass(classArray.getString(i));
            character.setLevel(i, classLevels.getInt(classArray.getString(i) + i));
        }
        JSONArray featureArray = jsonObject.getJSONArray("features");
        for (int i = 0; i < featureArray.length(); i++) {
            character.addFeature(readFeature(featureArray.getJSONObject(i)));
        }
        return readCharacterInventories(character, jsonObject);
    }

    //EFFECTS: helper method: handles inventories for readCharacter
    private Character readCharacterInventories(Character character, JSONObject jsonObject) {
        JSONArray equipmentArray = jsonObject.getJSONArray("equippedItems");
        for (int i = 0; i < equipmentArray.length(); i++) {
            character.addEquippedItem(readInventoryItem(equipmentArray.getJSONObject(i)));
        }
        JSONArray carriedArray = jsonObject.getJSONArray("carriedItems");
        for (int i = 0; i < carriedArray.length(); i++) {
            character.addCarriedItem(readInventoryItem(carriedArray.getJSONObject(i)));
        }
        JSONArray inventoryArray = jsonObject.getJSONArray("inventoryItems");
        for (int i = 0; i < inventoryArray.length(); i++) {
            character.addInventoryItem(readInventoryItem(inventoryArray.getJSONObject(i)));
        }
        return character;
    }

    //EFFECTS: creates an entire MainMenu ui object from fields stored in a suitable jsonObject
    public DataHandler readMain(JSONObject jsonObject) {
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<InventoryItem> items = new ArrayList<>();
        ArrayList<Feature> features = new ArrayList<>();

        JSONArray characterReadArray = jsonObject.getJSONArray("characters");
        JSONArray itemReadArray = jsonObject.getJSONArray("items");
        JSONArray featureReadArray = jsonObject.getJSONArray("features");

        for (int i = 0; i < characterReadArray.length(); i++) {
            characters.add(readCharacter(characterReadArray.getJSONObject(i)));
        }
        for (int i = 0; i < itemReadArray.length(); i++) {
            items.add(readInventoryItem(itemReadArray.getJSONObject(i)));
        }
        for (int i = 0; i < featureReadArray.length(); i++) {
            features.add(readFeature(featureReadArray.getJSONObject(i)));
        }

        return new DataHandler(characters, items, features);
    }

    //EFFECTS: reads a jsonObject from a certain save file and creates a new MainMenu from its values
    public DataHandler loadSave(File saveFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(saveFile));
        String readerInput = "";
        while (true) {
            String read = reader.readLine();
            if (read == null) {
                break;
            } else {
                readerInput = readerInput.concat(read);
            }
        }
        reader.close();
        return readMain(new JSONObject(readerInput));
    }

    //EFFECTS: writes the state of a certain MainMenu into a file
    public void writeSave(String name, DataHandler state) throws IOException {
        String parsedName = name.replaceAll(ILLEGAL_FILE_NAME_CHARACTERS, "-");
        File saveOutput = new File(SAVE_FILE_DIRECTORY_PATH + "/" + parsedName + ".json");
        saveOutput.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(saveOutput, false));
        writer.write(new JSONObject(state).toString());
        writer.close();

    }
}
