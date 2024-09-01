.PHONY: lox generate_ast build build_tool clean

lox: build
	@java -cp bin/lox com.craftinginterpreters.lox.Lox "$(file)"

generate_ast: build_tool
	@java -cp bin/lox com.craftinginterpreters.tool.GenerateAst src/main/java/com/craftinginterpreters/lox

build: bin generate_ast
	@javac -d bin/lox src/main/java/com/craftinginterpreters/lox/*.java

build_tool: bin
	@javac -d bin/lox src/main/java/com/craftinginterpreters/tool/*.java

clean:
	@rm -rf bin

bin:
	@mkdir -p bin/lox