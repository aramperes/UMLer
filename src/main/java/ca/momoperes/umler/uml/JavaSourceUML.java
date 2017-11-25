package ca.momoperes.umler.uml;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class JavaSourceUML implements UML {
    private String className;
    private Collection<UMLField> fields = new ArrayList<>();
    private Collection<UMLConstructor> constructors = new ArrayList<>();
    private Collection<UMLMethod> methods = new ArrayList<>();

    @Override
    public void load(File file) throws Exception {
        String plainText = Files.readAllLines(file.toPath()).stream().collect(Collectors.joining("\n"));
        CompilationUnit unit;
        try {
            unit = JavaParser.parse(plainText);
        } catch (Exception e) {
            throw new UMLException("There is a syntax error in that Java file.");
        }
        this.className = null;
        this.fields.clear();
        this.constructors.clear();
        this.methods.clear();

        // Find the class
        for (TypeDeclaration<?> typeDeclaration : unit.getTypes()) {
            if (typeDeclaration.isTopLevelType() && typeDeclaration.getModifiers().contains(Modifier.PUBLIC)) {
                this.className = typeDeclaration.getName().getIdentifier();
            }
        }
        if (this.className == null) {
            throw new UMLException("Could not find a public class in the given file.");
        }
        System.out.println(this.className);
        ClassOrInterfaceDeclaration clazz = unit.getClassByName(this.className).get();

        // Find the fields
        fields = new ArrayList<>();
        for (FieldDeclaration fieldDeclaration : clazz.getFields()) {
            EnumSet<Modifier> modifiers = fieldDeclaration.getModifiers();
            int accessModifier = modifiers.contains(Modifier.PUBLIC) ? java.lang.reflect.Modifier.PUBLIC : java.lang.reflect.Modifier.PRIVATE;
            boolean isStatic = modifiers.contains(Modifier.STATIC);
            for (VariableDeclarator variableDeclarator : fieldDeclaration.getVariables()) {
                UMLField field = new UMLField(accessModifier, variableDeclarator.getNameAsString(), variableDeclarator.getType().getElementType().asString() + (variableDeclarator.getType().isArrayType() ? "[]" : ""), isStatic);
                System.out.println(field);
                fields.add(field);
            }
        }

        // Find the constructors
        constructors = new ArrayList<>();
        for (ConstructorDeclaration constructorDeclaration : clazz.getConstructors()) {
            EnumSet<Modifier> modifiers = constructorDeclaration.getModifiers();
            int accessModifier = modifiers.contains(Modifier.PUBLIC) ? java.lang.reflect.Modifier.PUBLIC : java.lang.reflect.Modifier.PRIVATE;
            List<UMLParameter> parameters = new ArrayList<>();
            for (Parameter parameterDef : constructorDeclaration.getParameters()) {
                UMLParameter parameter = new UMLParameter(parameterDef.getNameAsString(), parameterDef.getType().getElementType().asString() + (parameterDef.getType().isArrayType() ? "[]" : ""));
                parameters.add(parameter);
            }
            UMLConstructor constructor = new UMLConstructor(accessModifier, constructorDeclaration.getNameAsString(), parameters);
            constructors.add(constructor);
            System.out.println(constructor);
        }
        if (constructors.size() == 0) {
            // Default constructor
            constructors.add(new UMLConstructor(java.lang.reflect.Modifier.PUBLIC, className, Collections.emptyList()));
        }

        // Find the methods
        methods = new ArrayList<>();
        for (MethodDeclaration methodDeclaration : clazz.getMethods()) {
            EnumSet<Modifier> modifiers = methodDeclaration.getModifiers();
            int accessModifier = modifiers.contains(Modifier.PUBLIC) ? java.lang.reflect.Modifier.PUBLIC : java.lang.reflect.Modifier.PRIVATE;
            boolean isStatic = modifiers.contains(Modifier.STATIC);
            List<UMLParameter> parameters = new ArrayList<>();
            for (Parameter parameterDef : methodDeclaration.getParameters()) {
                UMLParameter parameter = new UMLParameter(parameterDef.getNameAsString(), parameterDef.getType().getElementType().asString() + (parameterDef.getType().isArrayType() ? "[]" : ""));
                parameters.add(parameter);
            }
            UMLMethod method = new UMLMethod(accessModifier, methodDeclaration.getNameAsString(), parameters, methodDeclaration.getType().getElementType().asString() + (methodDeclaration.getType().isArrayType() ? "[]" : ""), isStatic);
            methods.add(method);
            System.out.println(method);
        }
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public Collection<UMLField> getFields() {
        return fields;
    }

    @Override
    public Collection<UMLConstructor> getConstructors() {
        return constructors;
    }

    @Override
    public Collection<UMLMethod> getMethods() {
        return methods;
    }
}
