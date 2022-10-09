package model;

import enums.ModifierType;

import java.math.BigDecimal;

public class Modifier {
    private final ModifierType type;
    private final BigDecimal value;

    public Modifier(ModifierType type, BigDecimal value) {
        this.type = type;
        this.value = value;
    }

    public Modifier apply(Modifier applyTo) {
        Modifier result = new Modifier(ModifierType.BASE,BigDecimal.ZERO);
        switch (type) {
            case BASE:
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
}
