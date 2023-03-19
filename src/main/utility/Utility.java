package utility;

import enums.*;
import model.*;

import java.lang.Character;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public abstract class Utility {
    ///A utility class for listOf() functionality replication and hashCode helpers.

    private Utility() {
        //never intended to be called; added here so that JaCoCo doesn't pester me about it.
    }

    //EFFECTS: returns a List containing the elements passed in
    public static List<ScoreType> listOf(ScoreType... elements) {
        return Arrays.asList(elements);
    }

    //EFFECTS: returns a List containing the elements passed in
    public static List<StatType> listOf(StatType... elements) {
        return Arrays.asList(elements);
    }

    //EFFECTS: returns a List containing the elements passed in
    public static List<Feature> listOf(Feature... elements) {
        return Arrays.asList(elements);
    }

    //EFFECTS: hashes an object if it is able to, returning 0 on a null value
    public static int hashIfAble(Object object) {
        if (object != null) {
            try {
                Method hashFunction = object.getClass().getDeclaredMethod("hashCode");
                return (int) hashFunction.invoke(object);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                return object.hashCode();
            }
        }
        return 0;
    }

    //EFFECTS: helper for hashCode overrides, compiles a bunch of integer values (component hashCodes) into one hash
    public static int hashCodeHelper(List<Integer> hashComponents, int hashMultiplier) {
        int result = 0;
        for (int i : hashComponents) {
            result *= hashMultiplier;
            result += i;
        }
        return result;
    }

    // EFFECTS: replicates JDK11 String.isBlank() method
    public static boolean isBlank(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: logs an event, given only the description
    public static void logEvent(String description) {
        EventLog.getInstance().logEvent(new Event(description));
    }
}
