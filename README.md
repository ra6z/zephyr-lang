# Zephyr
![GitHub](https://img.shields.io/github/license/ra6z/zephyr)
![GitHub forks](https://img.shields.io/github/forks/ra6z/zephyr)
![GitHub Repo stars](https://img.shields.io/github/stars/ra6z/zephyr)

## Table of Contents

<!-- TOC -->
* [Zephyr](#zephyr)
  * [Table of Contents](#table-of-contents)
  * [Description](#description)
  * [Examples](#examples)
    * [Hello World](#hello-world)
  * [Roadmap](#roadmap)
  * [TODO](#todo)
  * [Language Server Protocol](#language-server-protocol)
  * [VSCode extension](#vscode-extension)
  * [Syntax](#syntax)
  * [Standard library](#standard-library)
  * [License](#license)
<!-- TOC -->

## Description

Zephyr is a programming language that is currently interpreted. It is designed to be a general purpose language that is easy to learn and use. It is also designed to be a language that is easy to use for beginners, but also powerful enough for advanced users.

Its design is inspired by Object-Oriented Programming, Functional Programming, and Imperative Programming. It is also inspired by other languages such as C#, Java, JavaScript, Python, and Rust.

Every type in Zephyr is an object. This means that every type has functions and fields. This also means that every type can be used as a value.

Zephyr is a statically typed language. This means that the type of every value is known at compile time. This also means that the type of every value is known at runtime.

The entry file of a program needs to export a type that has a **public shared** function called `main`. The `main` function is the entry point of the program. The `main` function takes two arguments: `argc` and `argv`. The `argc` argument is an integer that represents the number of arguments passed to the program. The `argv` argument is an array of strings that represents the arguments passed to the program.
See: `pub shared fnc main(argc: int, argv: str[]): int`

## Examples

### Hello World

```
import "std:console";

type Program {
  pub shared fn main(argc: int, argv: str[]): int {
    Console.log("Hello, World!");
    return 0;
  }
}

export Program;
```

The code above is a simple "Hello, World!" program written in Zephyr. It is a single file program that prints "Hello, World!" to the console.

The `import` statement imports the `std:console` module. The `std:console` module contains a type called `Console` that has a function called `printString`. The `printString` function prints a string to the console.

The type `Program` is a type that contains a function called `main`. The `main` function is the entry point of the program. The `main` function takes two arguments: `argc` and `argv`. The `argc` argument is an integer that represents the number of arguments passed to the program. The `argv` argument is an array of strings that represents the arguments passed to the program.

The `export` statement exports the `Program` type. The `Program` type is exported.

## Roadmap

- [x] Lexer
- [x] Parser
- [x] AST
- [x] Type checking
- [x] Interpreter
- [ ] Control flow analysis
  - [ ] Control flow graph
  - [ ] Dead code elimination
- [ ] Code generation
  - [ ] WASM
  - [ ] JIT
- [ ] Compiler
- [ ] Self-hosting
- [ ] Standard library
  - [ ] `std:io`
  - [ ] `std:math`
  - [ ] `std:ui`
  - [ ] `std:data/json`
  - [ ] `std:data/xml`
  - [ ] `std:net`
  - [ ] `std:net/http`
  - [ ] `std:net/irc`
  - [ ] `std:threading`
  - [ ] `std:time`
  - [ ] `std:config`
  - [ ] `std:crypto`
  - [ ] `std:debug`
  - [ ] `std:testing`
- [ ] Language Server Protocol
  - [ ] Code diagnostics
  - [ ] Code completion
  - [ ] Code formatting
  - [ ] Code folding
  - [ ] Code highlighting
  - [ ] Code navigation
  - [ ] Code refactoring
  - [ ] Code validation
  - [ ] Code snippets
  - [ ] Code fixes
  - [ ] Code documentation

## TODO

- [x] `import` statements
  - [ ] `as` keyword for renaming imports
- [x] `export` statements
- [x] Array Types
  - [x] Array creation syntax (e.g. `[0, 1, 2, 3]`)
  - [x] Array indexing syntax (e.g. `array[0]`)
  - [x] Array fields
    - [x] `array.length`
  - [x] Array functions
    - [x] `array.copy()`
- [x] Generic types (e.g. `type MyType<T> { ... }`)
- [ ] Type aliases (e.g. `type MyType = int;`)
- [ ] Additional syntax
  - [ ] `for` loops
  - [ ] `do while` loops
  - [ ] `switch` statements
    - [ ] `case` statements
    - [ ] `default` statements
  - [ ] `try`/`catch`/`finally` statements
  - [ ] `throw` statements
- [ ] Trivia
    - [ ] Whitespace
    - [ ] Comments
        - [ ] `//` single line comments
        - [ ] `/* */` multi line comments
        - [ ] `///` doc comments
        - [ ] `//!` doc comments
    - [ ] Newlines
- [ ] Interfaces (e.g. `interface MyInterface { ... }`)
  - [ ] Types that implement interfaces (e.g. `type MyType implements MyInterface { ... }`)
- [ ] Enums (e.g. `enum MyEnum { ... }`)
- [ ] Asynchronous functions (e.g. `async fnc myFunction() { ... }`)
  - [ ] `await` keyword (e.g. `await myFunction()`)
  - [ ] `async` keyword (e.g. `async myFunction()`)
- [ ] Better diagnostics 
  - [ ] Severity levels (e.g. `error`, `warning`, `info`)
  - [ ] Error codes (e.g. `E0001`)
  - [ ] Error messages (e.g. "expected `;`")
  - [ ] Error locations (e.g. line numbers)
  - [ ] Error ranges (e.g. for syntax highlighting)
  - [ ] Error suggestions (e.g. "did you mean `=`?")
  - [ ] Error fixes (e.g. "add `;`")
- [ ] Importing of other file types and parsing them to a Zephyr module
  - [ ] JSON (e.g. import "file.json");

## Language Server Protocol

- [ ] Error reporting
- [ ] Code completion
- [ ] Code formatting
- [ ] Code folding
- [ ] Code highlighting
- [ ] Code navigation
- [ ] Code refactoring
- [ ] Code validation
- [ ] Code snippets
- [ ] Code fixes

## VSCode extension

- [ ] Error reporting
- [ ] Code completion
- [ ] Code formatting
- [ ] Code documentation
- [ ] Code highlighting
  - [x] Syntax highlighting
    - [x] Keywords
    - [x] Identifiers
    - [x] Literals
    - [x] Operators
    - [x] Punctuation
  - [ ] Error highlighting
  - [ ] Warning highlighting
  - [ ] Info highlighting

## Syntax

## Standard library

The documentation for the standard library can be found [here](docs/stdlib.md)

## License

Zephyr is licensed under the MIT license. See [LICENSE](LICENSE) for more information.
