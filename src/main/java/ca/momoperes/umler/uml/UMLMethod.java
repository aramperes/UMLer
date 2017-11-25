package ca.momoperes.umler.uml;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class UMLMethod {
    private final int modifier;
    private final String name;
    private final List<UMLParameter> parameters;
    private final String returnType;
    private final boolean isStatic;

    public UMLMethod(int modifier, String name, List<UMLParameter> parameters, String returnType, boolean isStatic) {
        this.modifier = modifier;
        this.name = name;
        this.parameters = parameters;
        this.returnType = returnType;
        this.isStatic = isStatic;
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

    public String getReturnType() {
        return returnType;
    }

    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public String toString() {
        String parameters = getParameters().stream().map(UMLParameter::toString).collect(Collectors.joining(", "));
        return (modifier == Modifier.PUBLIC ? "+" : "-") + name + "(" + parameters + "): " + returnType;
    }
}
