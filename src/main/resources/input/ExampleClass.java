public class ExampleClass {

    public static void main(String[] args) {
        String multiLineString = """
                This is a multi-line string.
                It spans multiple lines and uses concatenation.
                You can add more lines as needed.\
                """;

        String longString = """
                This is a long string that goes on and on. \
                It can be quite useful when you need to concatenate \
                multiple strings together to form a single long string. \
                You can keep adding more text as necessary to make it even longer.\
                """;

        System.out.println(multiLineString);
        System.out.println(longString);
    }
}