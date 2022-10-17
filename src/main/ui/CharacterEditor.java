package ui;

import enums.ScoreType;
import model.*;
import model.Character;

import java.util.ArrayList;
import java.util.List;

public class CharacterEditor extends AcceptsInput {
    /// A helper class for Main. Manages all character editing.

    public Character edit(Character baseCharacter, List<CharClass> classes,
                          List<CharRace> races, List<CharBackground> backgrounds,
                          List<InventoryItem> items) {
        while (true) {
            render(baseCharacter);
            System.out.println("Please select an option:\n"
                               + "[1] Change Name\n"
                               + "[2] Change Race\n"
                               + "[3] Change Background\n"
                               + "[4] Add Class\n"
                               + "[5] Remove Class\n"
                               + "[6] Add Item\n"
                               + "[7] Remove Item\n"
                               + "[8] Move Item\n"
                               + "[9] Finish");
            int menuSelect = input.scanPositiveIntWithExit("Please select an option using its index.");
            if (menuSelect < 0 || menuSelect == 9) {
                break;
            }
            baseCharacter = menuEdit(menuSelect, baseCharacter, classes, races, backgrounds, items);
        }
        return baseCharacter;
    }

    @SuppressWarnings("methodlength")
    //EFFECTS: helper method for edit; manages switch case menuing
    public Character menuEdit(int menuSelect, Character baseCharacter, List<CharClass> classes,
                              List<CharRace> races, List<CharBackground> backgrounds,
                              List<InventoryItem> items) {
        switch (menuSelect) {
            case 1:
                changeName(baseCharacter);
                break;
            case 2:
                changeRace(baseCharacter, races);
                break;
            case 3:
                changeBackground(baseCharacter, backgrounds);
                break;
            case 4:
                addClass(baseCharacter, classes);
                break;
            case 5:
                removeClass(baseCharacter);
                break;
            case 6:
                addItem(baseCharacter, items);
                break;
            case 7:
                removeItem(baseCharacter);
                break;
            case 8:
                moveItem(baseCharacter);
        }
        return baseCharacter;
    }

    private void changeName(Character baseCharacter) {
        baseCharacter.setName(input.scanName("Please enter a new name:"));
    }

    private void changeRace(Character baseCharacter, List<CharRace> races) {
        System.out.println("Races Available:");
        for (int i = 0; i < races.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + races.get(i).getName());
        }
        while (true) {
            int raceIndex = input.scanPositiveIntWithExit("Please choose a new race by its index:");
            if (raceIndex < 0) {
                return;
            }
            if (raceIndex > 0 && raceIndex <= races.size()) {
                baseCharacter.setRace(races.get(raceIndex - 1));
                if (!races.get(raceIndex - 1).getSubRaces().isEmpty()) {
                    if (input.scanYesNo("Would you also like to select a subrace?")) {
                        selectSubRace(baseCharacter);
                    }
                }
                return;
            }
        }
    }

    //EFFECTS: helper method; selects a subrace for the character
    private void selectSubRace(Character baseCharacter) {
        ArrayList<CharRace> races = baseCharacter.getRace().getSubRaces();
        System.out.println("Subraces Available:");
        for (int i = 0; i < races.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + races.get(i).getName());
        }
        while (true) {
            int raceIndex = input.scanPositiveIntWithExit("Please choose a new subrace by its index:");
            if (raceIndex < 0) {
                return;
            }
            if (raceIndex > 0 && raceIndex <= races.size()) {
                baseCharacter.getRace().selectSubRace(raceIndex - 1);
                return;
            }
        }
    }

    private void changeBackground(Character baseCharacter, List<CharBackground> backgrounds) {
        System.out.println("Backgrounds Available:");
        for (int i = 0; i < backgrounds.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + backgrounds.get(i).getName());
        }
        while (true) {
            int backgroundIndex = input.scanPositiveIntWithExit("Please choose a new background by its index:");
            if (backgroundIndex < 0) {
                return;
            }
            if (backgroundIndex > 0 && backgroundIndex <= backgrounds.size()) {
                baseCharacter.setBackground(backgrounds.get(backgroundIndex - 1));
                return;
            }
        }
    }

    private void addClass(Character baseCharacter, List<CharClass> classes) {
        System.out.println("Classes Available:");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + classes.get(i).getName());
        }
        while (true) {
            int classIndex = input.scanPositiveIntWithExit("Please choose a class by its index:");
            if (classIndex < 0) {
                return;
            }
            if (classIndex > 0 && classIndex <= classes.size()) {
                baseCharacter.addClass(classes.get(classIndex - 1));
                return;
            }
        }
    }

    private void removeClass(Character baseCharacter) {
        ArrayList<CharClass> classes = baseCharacter.getClasses();
        System.out.println("Classes Available:");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + classes.get(i).getName());
        }
        while (true) {
            int classIndex = input.scanPositiveIntWithExit("Please choose a class by its index:");
            if (classIndex < 0) {
                return;
            }
            if (classIndex > 0 && classIndex <= classes.size()) {
                baseCharacter.removeClass(classIndex - 1);
                return;
            }
        }
    }

    private void addItem(Character baseCharacter, List<InventoryItem> items) {
        System.out.println("Items Available:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + items.get(i).getName());
        }
        while (true) {
            int itemIndex = input.scanPositiveIntWithExit("Please choose an item by its index:");
            if (itemIndex < 0) {
                return;
            }
            if (itemIndex > 0 && itemIndex <= items.size()) {
                switch (selectInventoryIndex("Please select an inventory to add to")) {
                    case 1:
                        baseCharacter.addEquippedItem(items.get(itemIndex - 1));
                        break;
                    case 2:
                        baseCharacter.addCarriedItem(items.get(itemIndex - 1));
                        break;
                    case 3:
                        baseCharacter.addInventoryItem(items.get(itemIndex - 1));
                }
                return;
            }
        }
    }

    private void removeItem(Character baseCharacter) {
        ArrayList<InventoryItem> items = selectInventory(baseCharacter,
                                                         "Please choose an inventory to remove from");
        if (items == null) {
            return;
        }
        System.out.println("Items Available:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + items.get(i).getName());
        }
        while (true) {
            int itemIndex = input.scanPositiveIntWithExit("Please choose an item by its index:");
            if (itemIndex < 0) {
                return;
            }
            if (itemIndex > 0 && itemIndex <= items.size()) {
                items.remove(items.get(itemIndex - 1));
                return;
            }
        }
    }

    private void moveItem(Character baseCharacter) {
        ArrayList<InventoryItem> itemsFrom = selectInventory(baseCharacter,"Please choose an inventory to move from");
        if (itemsFrom == null) {
            return;
        }
        System.out.println("Items Available:");
        for (int i = 0; i < itemsFrom.size(); i++) {
            System.out.println("[" + Integer.toString(i + 1) + "] " + itemsFrom.get(i).getName());
        }
        while (true) {
            int itemIndex = input.scanPositiveIntWithExit("Please choose an item by its index:");
            if (itemIndex < 0) {
                return;
            }
            if (itemIndex > 0 && itemIndex <= itemsFrom.size()) {
                ArrayList<InventoryItem> itemsTo = selectInventory(baseCharacter,
                                                                   "Please choose an inventory to move to");
                if (itemsTo == null) {
                    return;
                }
                InventoryItem.moveItem(itemsFrom, itemsTo, itemIndex - 1);
                return;
            }
        }
    }

    //EFFECTS: helper method; selects an index corresponding to one of the three inventories that a character has
    private int selectInventoryIndex(String label) {
        int inventorySelect = 0;
        System.out.println(label + "\n"
                           + "[1] Equipped Items\n"
                           + "[2] Carried Items\n"
                           + "[3] Inventory Items");
        while (true) {
            int invSelectInput = input.scanPositiveIntWithExit("Please select an inventory by index:");
            if (invSelectInput < 0) {
                return -1;
            }
            if (invSelectInput > 0 && invSelectInput <= 3) {
                return invSelectInput;
            }
        }
    }

    //EFFECTS: helper method; selects one of the three actual ArrayList inventories that a character has
    private ArrayList<InventoryItem> selectInventory(Character baseCharacter, String label) {
        switch (selectInventoryIndex(label)) {
            case -1:
                return null;
            case 1:
                return baseCharacter.getEquippedItems();
            case 2:
                return baseCharacter.getCarriedItems();
            case 3:
                return baseCharacter.getInventoryItems();
        }
        return null;
    }

    //EFFECTS: takes values from a character and prints a representation
    public void render(Character baseCharacter) {
        String name = baseCharacter.getName();
        System.out.println(name);
        System.out.println(renderRaceBackground(baseCharacter));
        System.out.println(renderClasses(baseCharacter));
        System.out.println(renderScores(baseCharacter));
        System.out.println(renderInventory(baseCharacter));
    }

    //EFFECTS: takes a character's race and background and renders them in text
    private String renderRaceBackground(Character baseCharacter) {
        String result = "";
        if (baseCharacter.getRace() != null) {
            result = result.concat(baseCharacter.getRace().getName() + " ");
        } else {
            result = result.concat("Unknown ");
        }
        if (baseCharacter.getBackground() != null) {
            result = result.concat(baseCharacter.getBackground().getName());
        } else {
            result = result.concat("Unknown ");
        }
        return result;
    }

    //EFFECTS: takes a character's classes and renders them in text
    private String renderClasses(Character baseCharacter) {
        String result = "--Classes:";
        for (CharClass c : baseCharacter.getClasses()) {
            String classNameRender = c.getName();
            if (c.getSubClass() != null) {
                classNameRender = c.getSubClass().getName().concat(classNameRender);
            }
            result = result.concat("\n---" + classNameRender + " " + c.getLevel());
        }
        return result;
    }

    //EFFECTS: takes a character's scores and renders them in text
    private String renderScores(Character baseCharacter) {
        String result = "--Scores:";
        String[] topScores = new String[6];
        for (int i = 0; i < 6; i++) {
            topScores[i] = baseCharacter.getAbilityScores().get(ScoreType.values()[i]).toString();
            topScores[i] = topScores[i].concat(" ("
                                               + baseCharacter.getSkillThrowBonuses().get(
                                                       ScoreType.values()[i + 6]).toString()
                                               + ")");
        }
        result = result.concat("\nSTR: " + topScores[0] + " | DEX: " + topScores[1] + " | CON: " + topScores[2]
                               + "\nINT: " + topScores[3] + " | WIS: " + topScores[4] + " | CHR: " + topScores[5]);
        return result;
    }

    //EFFECTS: takes a character's inventory and renders them in text
    private String renderInventory(Character baseCharacter) {
        ArrayList<String> equippedList = new ArrayList<>();
        ArrayList<String> carriedList = new ArrayList<>();
        ArrayList<String> inventoryList = new ArrayList<>();
        equippedList.add("Equipped Items:    ");
        carriedList.add(" | Carried Items:    ");
        inventoryList.add(" | Inventory Items:    ");
        for (InventoryItem i : baseCharacter.getEquippedItems()) {
            equippedList.add(i.getName());
        }
        for (InventoryItem i : baseCharacter.getCarriedItems()) {
            carriedList.add(" | " + i.getName());
        }
        for (InventoryItem i : baseCharacter.getInventoryItems()) {
            inventoryList.add(" | " + i.getName());
        }
        return renderVertical(addHorizontal(addHorizontal(equippedList, carriedList), inventoryList));
    }

    //EFFECTS: helper method; used to merge two vertical lists of strings together while keeping them spaced properly
    //         on screen
    private List<String> addHorizontal(List<String> listA, List<String> listB) {
        int longestLine = 0;
        for (String s : listA) {
            if (s.length() > longestLine) {
                longestLine = s.length();
            }
        }
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < Math.max(listA.size(), listB.size()); i++) {
            String first = "";
            String second = "";
            if (i < listA.size()) {
                first = first.concat(listA.get(i));
            }
            if (i < listB.size()) {
                second = second.concat(listB.get(i));
            }
            while (first.length() < longestLine) {
                first = first.concat(" ");
            }
            result.add(first.concat(second));
        }
        return result;
    }

    //EFFECTS: helper method; used to turn a list of strings into one big column string
    public String renderVertical(List<String> list) {
        String result = "";
        for (String s : list) {
            result = result.concat(s + "\n");
        }
        return result;
    }
}
