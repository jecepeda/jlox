package com.craftinginterpreters.lox;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    @Test
    void parseExpression() {
        var scanner = new Scanner("3 == 4; 5 == 6; 7 == 8; print 3+2; 5 / 6;" +
                "4-5;-4.40;");

        List<Token> tokens = scanner.scanTokens();

        Parser p = new Parser(tokens);
        List<Stmt> got = p.parse();
        List<Stmt> expected = Arrays.asList(
                new Stmt.Expression(new Expr.Binary(
                        new Expr.Literal((double) (3)),
                        new Token(TokenType.EQUAL_EQUAL, "==", null, 1),
                        new Expr.Literal((double) 4)
                )),
                new Stmt.Expression(new Expr.Binary(
                        new Expr.Literal((double) (5)),
                        new Token(TokenType.EQUAL_EQUAL, "==", null, 1),
                        new Expr.Literal((double) 6)
                )),
                new Stmt.Expression(
                        new Expr.Binary(
                                new Expr.Literal((double) (7)),
                                new Token(TokenType.EQUAL_EQUAL, "==", null, 1),
                                new Expr.Literal((double) 8)
                        )
                ),
                new Stmt.Print(
                        new Expr.Binary(
                                new Expr.Literal((double) (3)),
                                new Token(TokenType.PLUS, "+", null, 1),
                                new Expr.Literal((double) 2)
                        )
                ),
                new Stmt.Expression(
                        new Expr.Binary(
                                new Expr.Literal((double) (5)),
                                new Token(TokenType.SLASH, "/", null, 1),
                                new Expr.Literal((double) 6)
                        )
                ),
                new Stmt.Expression(
                        new Expr.Binary(
                                new Expr.Literal((double) (4)),
                                new Token(TokenType.MINUS, "-", null, 1),
                                new Expr.Literal((double) 5)
                        )
                ),
                new Stmt.Expression(
                        new Expr.Unary(
                                new Token(TokenType.MINUS, "-", null, 1),
                                new Expr.Literal((double) (4.40))
                        )
                )
        );
        assertEquals(expected, got);
    }
}