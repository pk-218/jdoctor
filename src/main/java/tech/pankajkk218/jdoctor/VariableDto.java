package tech.pankajkk218.jdoctor;

import com.github.javaparser.ast.type.Type;

public record VariableDto(int line, int column, Type type, String name, String value) {

    @Override
    public String toString() {
        return String.format(
                """
                        (%s, %s) - %s, %s, %s
                        """, this.line, this.column, this.type, this.name, this.value
        );
    }
}
