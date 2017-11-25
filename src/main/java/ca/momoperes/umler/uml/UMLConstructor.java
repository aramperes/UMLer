package ca.momoperes.umler.uml;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class UMLConstructor {
    private final int modifier;
    private final String name;
    private final List<UMLParameter> parameters;

    public UMLConstructor(int modifier, String name, List<UMLParameter> parameters) {
        this.modifier = modifier;
        this.name = name;
        this.parameters = parameters;
    }

    public int getModifier() {
        return modifier;
    }

    public String getName() {
        return name;
    }

    public List<UMLParameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        String parameters = getParameters().stream().map(UMLParameter::toString).collect(Collectors.joining(", "));
        return (modifier == Modifier.PUBLIC ? "+" : "-") + name + "(" + parameters + ")";
    }
}
