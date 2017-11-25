package ca.momoperes.umler.uml;

import java.lang.reflect.Modifier;

public class UMLField {
    private final int modifier;
    private final String name;
    private final String type;
    private final boolean isStatic;

    public UMLField(int modifier, String name, String type, boolean isStatic) {
        this.modifier = modifier;
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
    }

    public int getModifier() {
        return modifier;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public String toString() {
        return (modifier == Modifier.PUBLIC ? "+" : "-") + name + ": " + type;
    }
}
