# Syntax

<!-- TOC -->
* [Syntax](#syntax)
  * [Type checking](#type-checking)
  * [Generated functions](#generated-functions)
    * [`toString`](#tostring)
    * [`equals`](#equals)
    * [Arguments](#arguments)
  * [Keywords](#keywords)
    * [Control flow](#control-flow)
    * [Types](#types)
    * [Type coherence](#type-coherence)
    * [Type definitions](#type-definitions)
    * [Variables](#variables)
    * [Functions](#functions)
    * [Modules](#modules)
    * [Visibility and access modifiers](#visibility-and-access-modifiers)
    * [Other](#other)
    * [Operators](#operators)
<!-- TOC -->

## Type checking

You can check if a value is of a certain type using the `is` keyword.

If the value is generic then the type will be checked during compile time.

If the type is generic then the value will be checked during runtime.

Syntax: `value is type`

## Generated functions

The following functions are generated for each type if they are not defined by the user:

### `toString`

This function is used to convert the value to a string.

### `equals`

This function is used to compare two values.

### Arguments

* `other: any` - The value to compare to. This value can be of any type.

## Keywords

### Control flow

* `if`
* `else`
* `while`
* `break`
* `continue`
* `return`

### Types

* `int`
* `double`
* `bool`
* `str`
* `void`
* `any`

### Type coherence

* `as` - W.I.P.

### Type definitions

* `type`
* `nativetype`

### Variables

* `var`
* `const`

### Functions

* `fnc`
* `constructor`
* `unop`
* `binop`

### Modules

* `import`
* `export`

### Visibility and access modifiers

* `pub`
* `prv`
* `shared`

### Other

* `true`
* `false`
* `new`
* `this`

### Operators

* `+`
* `-`
* `*`
* `/`
* `%`
* `==`
* `!=`
* `>`
* `<`
* `>=`
* `<=`
* `&&`
* `||`
* `!`
* `^`
* `&`
* `|`
* `<<`
* `>>`
* `~`
* `=`

* `is`