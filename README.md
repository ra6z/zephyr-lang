# Zephyr

## Table of Contents

<!-- TOC -->
* [Zephyr](#zephyr)
  * [Table of Contents](#table-of-contents)
  * [Description](#description)
  * [TODO](#todo)
  * [Use cases](#use-cases)
  * [Language Server Protocol](#language-server-protocol)
  * [Syntax](#syntax)
  * [Standard library](#standard-library)
  * [License](#license)
<!-- TOC -->

## Description

## TODO

- [ ] Better error reporting
  - [ ] Severity levels (e.g. `error`, `warning`, `info`)
  - [ ] Error codes (e.g. `E0001`)
  - [ ] Error messages (e.g. "expected `;`")
  - [ ] Error locations (e.g. line numbers)
  - [ ] Error ranges (e.g. for syntax highlighting)
  - [ ] Error suggestions (e.g. "did you mean `=`?")
  - [ ] Error fixes (e.g. "add `;`")
- [ ] Additional syntax
    - [ ] `for` loops 
    - [ ] `do while` loops 
    - [ ] `switch` statements 
      - [ ] `case` statements
      - [ ] `default` statements
    - [ ] `try`/`catch`/`finally` statements
    - [ ] `throw` statements
- [ ] Comments
    - [ ] `//` single line comments
    - [ ] `/* */` multi line comments
    - [ ] `///` doc comments
- [x] `import` statements
    - [ ] `as` keyword for renaming imports
- [x] `export` statements
- [ ] Array Types
    - [ ] Array creation syntax (e.g. `{ 1, 2, 3 }`)
    - [ ] Array indexing syntax (e.g. `array[0]`)
## Use cases

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

## Syntax

## Standard library

* [std:io](STD_IO.md)
* [std:math](STD_MATH.md)
* [std:ui](STD_UI.md)
* [std:data/json](STD_DATA_JSON.md)
* [std:data/xml](STD_DATA_XML.md)
* [std:net](STD_WEB.md)
* [std:net/http](STD_NET_HTTP.md)
* [std:net/irc](STD_NET_IRC.md)
* [std:threading](STD_THREADING.md)
* [std:time](STD_TIME.md)
* [std:config](STD_CONFIG.md)
* [std:crypto](STD_CRYPTO.md)
* [std:debug](STD_DEBUG.md)

## License

Zephyr is licensed under the MIT license. See [LICENSE](LICENSE) for more information.