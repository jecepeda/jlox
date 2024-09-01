package com.craftinginterpreters.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Assign   : Token name, Expr value",
                "Binary   : Expr left, Token operator, Expr right",
                "Call     : Expr callee, Token paren, List<Expr> arguments",
                "Get      : Expr object, Token name",
                "Set      : Expr object, Token name, Expr value",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Logical  : Expr left, Token operator, Expr right",
                "Unary    : Token operator, Expr right",
                "This     : Token keyword",
                "Super    : Token keyword, Token method",
                "Variable : Token name"
        ));
        defineAst(outputDir, "Stmt", Arrays.asList(
                "Block      : List<Stmt> statements",
                "Class      : Token name, Expr.Variable superclass, List<Stmt.Function> methods",
                "Break      : ",
                "Expression : Expr expression",
                "Function   : Token name, List<Token> params, List<Stmt> body",
                "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
                "Print      : Expr expression",
                "Return     : Token keyword, Expr value",
                "Var        : Token name, Expr initializer",
                "While      : Expr condition, Stmt body"
        ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

        writer.println("package com.craftinginterpreters.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("");
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");
        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println();
        writer.println(spaces(1) + "interface Visitor<R> {");
        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println(spaces(2) + "R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
            writer.println();
        }

        writer.println(spaces(1) + "}");
        writer.println();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println(spaces(1) + "static public class " + className + " extends " + baseName + " {");
        String[] fields;
        if (fieldList.isEmpty()) {
            fields = new String[0];
        } else {
            fields = fieldList.split(", ");
        }

        // Fields.
        for (String field : fields) {
            writer.println(spaces(2) + "final " + field + ";");
        }
        writer.println();

        // constructor
        writer.println(spaces(2) + className + "(" + fieldList + ") {");
        // Store parameters in fields.
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println(spaces(3) + "this." + name + " = " + name + ";");
        }

        writer.println(spaces(2) + "}");

        // visitor pattern
        writer.println();
        writer.println(spaces(2) + "@Override");
        writer.println(spaces(2) + "<R> R accept(Visitor<R> visitor) {");
        writer.println(spaces(3) + "return visitor.visit" +
                className + baseName + "(this);");
        writer.println(spaces(2) + "}");


        // equals


        writer.println(spaces(1) + "}");
        writer.println();
    }

    private static String spaces(int i) {
        return "    ".repeat(i);
    }


}
