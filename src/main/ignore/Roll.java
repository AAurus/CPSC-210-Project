package ignore;

import enums.RollType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Random;

public class Roll {
    ///Represents a certain dice roll with modifiers.
    private RollType type;
    private BigDecimal value;
    private int diceCount;
    private int diceSides;
    private Roll firstRoll;
    private Roll secondRoll;
    private String mathMethod;
    private Random rollRandom;

    public Roll(BigDecimal value) {
        type = RollType.NUMBER;
        this.value = value;
    }

    public Roll(String diceString) {

    }

    public Roll(String method, String diceString) {

    }

    public Roll(String method, String firstDiceString, String secondDiceString) {

    }

    //EFFECTS: simulates a roll of dice, and outputs the result as a BigDecimal.
    public BigDecimal roll() {
        BigDecimal result = BigDecimal.ZERO;
        switch (type) {
            case NUMBER:
                result = value;
                break;
            case DIE:
                for (int i = 0; i < diceCount; i++) {
                    result = result.add(new BigDecimal(rollRandom.nextInt() + 1));
                }
                break;
            case MATH_EXPRESSION:
                BigDecimal[] params = new BigDecimal[]{firstRoll.roll(),secondRoll.roll()};
                invokeMath(mathMethod, params);
                break;
            case MATH_FUNCTION:
                params = new BigDecimal[]{firstRoll.roll()};
                invokeMath(mathMethod, params);
        }
        return result;
    }

    //REQUIRES: function should be an existing method in Math class
    //EFFECTS: invokes a function from the Math class taking BigDecimals as input, returning the output
    private BigDecimal invokeMath(String function, BigDecimal[] params) {
        BigDecimal result;
        Class<Math> mathClass = Math.class;
        try {
            Method method = mathClass.getDeclaredMethod(function);
            result = (BigDecimal) method.invoke(this, params);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
