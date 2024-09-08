package tech.pankajkk218.jdoctor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Main {

    public static void listClasses(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            try {
                new VoidVisitorAdapter<>() {
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(n.getFields());
                        System.out.println(n.getAnnotationByName("Query"));
                        System.out.println(n.getName());
                    }
                }
//                new ModifierVisitor<List<String>>() {
//                    @Override
//                    public VariableDeclarator visit(final VariableDeclarator v, List<String> list) {
//                        super.visit(v, list);
//                        state.add("");
//                        state.add(v.getNameAsString());
//                        return null;
//                    }
//                }
                        .visit(StaticJavaParser.parse(file), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).explore(projectDir);
    }


    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("src/main/resources/input/JpaExample.java");
        final var parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
        JavaParser javaParser = new JavaParser(parserConfiguration);

        List<VariableDto> state = new ArrayList<>();
        ModifierVisitor<List<VariableDto>> multiLineStringModifier = new MultiLineStringModifier();

        Optional<CompilationUnit> cu = javaParser.parse(file).getResult();

//        listClasses(directory);

        cu.ifPresent(x -> {
            multiLineStringModifier.visit(cu.get(), state);
        });
        System.out.println(cu);
    }
}