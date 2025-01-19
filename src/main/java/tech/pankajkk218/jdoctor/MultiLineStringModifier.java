package tech.pankajkk218.jdoctor;

import com.github.javaparser.Position;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class MultiLineStringModifier extends ModifierVisitor<List<VariableDto>> {

    @Override
    public VariableDeclarationExpr visit(final VariableDeclarationExpr v, List<VariableDto> state) {
        super.visit(v, state);
        Position position = v.getBegin().isPresent() ? v.getBegin().get() : new Position(0, 0);
        VariableDto variableDto = new VariableDto(
                position.line,
                position.column,
                (Type) v.getVariable(0).getChildNodes().get(0),
                v.getVariable(0).getChildNodes().get(1).toString(),
                v.getVariable(0).getInitializer().map(Objects::toString).orElse("null"));

        System.out.println("VariableDto: " + variableDto);
        state.add(variableDto);

        boolean needToModify = false;
        List<String> m = null;
        List<String> multiLiners = new ArrayList<>();
        String regex = "^\"([\\w\\s']+)\"(\\s*\\+\\s*.+)*([\\s\\w]|\")$";

        if (Pattern.matches(regex, variableDto.value())) {
            List<String> multiLineString = new ArrayList<>(Arrays.stream(variableDto.value().split("\\+")).toList());
            m = new ArrayList<>(multiLineString.stream().map(String::strip).toList());

            System.out.println("matched");
            needToModify = true;

            for (int i = 0; i < m.size(); i++) {
                System.out.println(m.get(i).strip());
                if (Integer.valueOf(m.get(i).codePointAt(0)).equals(34)) {
                    m.set(i, m.get(i).substring(1, m.get(i).length() - 1));
                }
            }
            System.out.println(String.join(" ", m));
            multiLiners.add(Arrays.toString(variableDto.value().split("\\+")).strip());
        }
        System.out.println(multiLiners.size());
        System.out.println();

        if (needToModify) {
            TextBlockLiteralExpr tb = new TextBlockLiteralExpr(String.join(" ", m));
            VariableDeclarator vd = new VariableDeclarator(variableDto.type(), variableDto.name(), tb);
            v.setVariable(0, vd);
        }
        return v;
    }
}
