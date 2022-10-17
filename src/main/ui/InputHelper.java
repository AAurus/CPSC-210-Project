package ui;

import enums.*;
import model.Modifier;
import model.Proficiency;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputHelper {
    ///A helper class for Main, to handle input requests.
    private static final String EXIT_KEYWORD = "EXIT";

    private Scanner scanner;

    public InputHelper() {
        scanner = new Scanner(System.in);
    }

    //EFFECTS: asks for a name to create object, using label to describe its class, and returns input text
    public String scanName(String label) {
        System.out.println("CREATING " + label.toUpperCase());
        return scanWithExit("Please enter your new " + label.toLowerCase() + "'s name:");
    }

    //EFFECTS: asks for a feature type and returns FeatureType selected
    public FeatureType scanFeatureType() {
        System.out.println("Please select a feature type:");
        for (FeatureType f : FeatureType.values()) {
            System.out.print("[" + f.name() + "] ");
        }
        System.out.println("");
        while (true) {
            String input = scanWithExit("");
            if (input == null) {
                break;
            } else {
                String parsedInput = input.trim().toUpperCase();
                for (FeatureType f : FeatureType.values()) {
                    if (parsedInput.equals(f.name())) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    //EFFECTS: asks for a score type and returns ScoreType selected
    public ScoreType scanScoreType() {
        System.out.println("Please select a score type:");
        for (int i = 0; i < ScoreType.values().length; i++) {
            ScoreType s = ScoreType.values()[i];
            System.out.print("[" + s.name() + "] ");
            if (i % 6 == 0 && i != 0) {
                System.out.println("");
            }
        }
        System.out.println("");
        while (true) {
            String input = scanWithExit("");
            if (input == null) {
                break;
            } else {
                String parsedInput = input.trim().toUpperCase();
                for (ScoreType s : ScoreType.values()) {
                    if (parsedInput.equals(s.name())) {
                        return s;
                    }
                }
            }
        }
        return null;
    }

    //EFFECTS: asks for a stat type and returns StatType selected
    public StatType scanStatType() {
        System.out.println("Please select a stat type:");
        for (int i = 0; i < StatType.values().length; i++) {
            StatType s = StatType.values()[i];
            System.out.print("[" + s.name() + "] ");
            if (i % 6 == 0 && i != 0) {
                System.out.println("");
            }
        }
        System.out.println("");
        while (true) {
            String input = scanWithExit("");
            if (input == null) {
                break;
            } else {
                String parsedInput = input.trim().toUpperCase();
                for (StatType s : StatType.values()) {
                    if (parsedInput.equals(s.name())) {
                        return s;
                    }
                }
            }
        }
        return null;
    }

    //EFFECTS: asks for a Modifier type and returns ModifierType selected
    public ModifierType scanModifierType() {
        System.out.println("Please select a modifier type:");
        for (int i = 0; i < ModifierType.values().length; i++) {
            ModifierType m = ModifierType.values()[i];
            System.out.print("[" + m.name() + "] ");
            if (i % 6 == 0 && i != 0) {
                System.out.println("");
            }
        }
        System.out.println("");
        while (true) {
            String input = scanWithExit("");
            if (input == null) {
                break;
            } else {
                String parsedInput = input.trim().toUpperCase();
                for (ModifierType m : ModifierType.values()) {
                    if (parsedInput.equals(m.name())) {
                        return m;
                    }
                }
            }
        }
        return null;
    }

    //EFFECTS: asks for console inputs for modifierType and value, and returns modifier based on input
    public Modifier scanModifier() {
        ModifierType type = scanModifierType();
        if (type == null) {
            return null;
        }
        while (true) {
            String input = scanWithExit("Please enter the numerical value for this modifier:");
            if (input == null) {
                return null;
            } else if (!input.trim().replaceAll("[0-9]", "").isEmpty()) {
                continue;
            } else {
                return new Modifier(type, new BigDecimal(input.trim()));
            }
        }
    }

    //EFFECTS: asks for console inputs for ProficiencyType and modifier, and returns proficiency based on input
    public Proficiency scanProficiency() {
        System.out.println("Proficiencies can be applied to scores and checks, or applied to mundane things like"
                + "item groups. Here are a list of score keywords that a proficiency can be applied to:");
        printScoreTypes(6);
        String profApplyInput = scanWithExit("Please enter your desired proficiency type");
        if (profApplyInput == null) {
            return null;
        }
        while (true) {
            String input = scanWithExit("Please enter the numerical multiplier for this proficiency:");
            if (input == null) {
                return null;
            } else if (!input.trim().replace("([0-9]+\\.[0-9]*)|([0.-9]*\\.[0-9]+)", "").isEmpty()) {
                continue;
            } else {
                for (ScoreType s : ScoreType.values()) {
                    if (s.name().equals(profApplyInput.trim().toUpperCase())) {
                        return new Proficiency(s, new BigDecimal(input.trim()));
                    }
                }
                return new Proficiency(profApplyInput, new BigDecimal(input.trim()));
            }
        }
    }

    //EFFECTS: prints all ScoreTypes, adding a line break every length types
    private void printScoreTypes(int length) {
        for (int i = 0; i < ScoreType.values().length; i++) {
            System.out.print(ScoreType.values()[i]);
            if (i % length == 0 && i != 0) {
                System.out.println();
            }
        }
    }

    //EFFECTS: asks for a console input, using label to describe the input asked for, and returns input text or
    //         null if exit keyword is inputted
    public String scanWithExit(String label) {
        while (true) {
            System.out.println(label);
            System.out.println("alternatively, enter " + EXIT_KEYWORD + " to exit this process");
            String input = scanner.nextLine();
            if (input.trim().equals(EXIT_KEYWORD)) {
                if (scanYesNo("Are you sure you want to exit?")) {
                    return null;
                } else {
                    continue;
                }
            } else {
                return input;
            }
        }
    }

    //EFFECTS: asks a y/n question, using label to describe the question, and returns boolean once valid answer is given
    public boolean scanYesNo(String label) {
        System.out.println(label + " Y/N");
        while (true) {
            String parsedInput = scanner.nextLine().trim().toLowerCase();
            if (parsedInput.matches("y(es)?")) {
                return true;
            } else if (parsedInput.matches("n(o)?")) {
                return false;
            }
        }
    }

    //EFFECTS: asks for a console positive integer input, using label to describe the question, and returns int once
    //         answer is given
    public int scanPositiveIntWithExit(String label) {
        while (true) {
            String parsedInput = scanWithExit(label);
            if (parsedInput == null) {
                return -1;
            }
            if (parsedInput.trim().matches("\\d+")) {
                return Integer.parseInt(parsedInput);
            }
        }
    }

    public Scanner getScanner() {
        return scanner;
    }
}
