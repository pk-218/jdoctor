package tech.pankajkk218.jdoctor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("src/main/resources/input/JpaExample.java");
        final var parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
        JavaParser javaParser = new JavaParser(parserConfiguration);

        List<VariableDto> state = new ArrayList<>();
        ModifierVisitor<List<VariableDto>> multiLineStringModifier = new MultiLineStringModifier();

        Optional<CompilationUnit> cu = javaParser.parse(file).getResult();

        cu.ifPresent(x -> multiLineStringModifier.visit(cu.get(), state));
        System.out.println(cu);
    }
}