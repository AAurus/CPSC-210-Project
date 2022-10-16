package model;

import enums.ModifierType;
import enums.ScoreType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Modifier {
    /// A representation of the ways you can modify a numerical value.
    public static final ModifierType[] OPERATIONS_ORDER = {
            ModifierType.BASE,
            ModifierType.ADD,
            ModifierType.MULTIPLY,
            ModifierType.MAX,
            ModifierType.MIN
    };

    private final ModifierType type;
    private final BigDecimal value;

    public Modifier(int value) {
        this.type = ModifierType.BASE;
        this.value = new BigDecimal(value);
    }

    public Modifier(ModifierType type, BigDecimal value) {
        this.type = type;
        this.value = value;
    }

    //REQUIRES: applyTo should be either same type, or base
    //EFFECTS: applies this to another Modifier according to its type and returns the result
    public Modifier apply(Modifier applyTo) {
        Modifier result = new Modifier(ModifierType.BASE,BigDecimal.ZERO);
        switch (type) {
            case ADD:
                result = new Modifier(applyTo.getType(), applyTo.getValue().add(value));
                break;
            case MULTIPLY:
                result = new Modifier(applyTo.getType(), applyTo.getValue().multiply(value));
                break;
            case MIN:
                result = new Modifier(applyTo.getType(), applyTo.getValue().min(value));
                break;
            case MAX:
            case BASE:
                result = new Modifier(applyTo.getType(), applyTo.getValue().max(value));
                break;
        }
        return result;
    }

    public ModifierType getType() {
        return type;
    }

    public BigDecimal getValue() {
        return value;
    }

    //EFFECTS: returns true if this has same values as other
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Modifier other = (Modifier) o;
        return (this.getValue().equals(other.getValue())
                && this.getType().equals(other.getType()));
    }
}
