# PHP Static Analysis: TypeResolver

A high-performance, lightweight static analysis component that resolves PHP variable types based on `@var` DocBlock annotations. This tool is designed to be integrated into larger static analysis engines or IDE plugins to improve type-inference accuracy.

---

## Features

* **Precise Tag Matching:** Correctly identifies types even when multiple `@var` tags exist in a single DocBlock by matching the variable name.
* **Union Type Support:** Automatically detects and constructs `UnionType` objects for multi-type definitions (e.g., `string|int`).
* **Context Awareness:** Distinguishes between generic tags (`@var User`) and named tags (`@var User $user`).
* **Robust Fallbacks:** Defaults to a `mixed` type safely when documentation is missing or provides mismatched metadata.

---

## How It Works

The `TypeResolver` processes `PhpVariable` objects by inspecting their associated `PhpDocBlock`. It follows a prioritized resolution flow:

1.  **Extraction:** Retrieves all `@var` tags from the DocBlock.
2.  **Validation:** * If a tag contains a name (e.g., `$admin`), it is only used if it matches the current variable's name.
    * If a tag is "anonymous" (no name provided), it is treated as a match for the current variable.
3.  **Transformation:**
    * Single types are passed to `TypeFactory::createType`.
    * Piped types (e.g., `string|null`) are split and passed to `TypeFactory::createUnionType`.

### Resolution Examples

| DocBlock Annotation | Variable | Resolved Type |
| :--- | :--- | :--- |
| `/** @var User */` | `$user` | `User` |
| `/** @var string\|int */` | `$id` | `UnionType(string, int)` |
| `/** @var Logger $log */` | `$log` | `Logger` |
| `/** @var Admin $adm */` | `$guest` | `mixed` (Mismatch ignored) |

---

## Project Structure

The project is organized following standard Gradle conventions within the `com.analysis` package:

```text
src/
├── main/
│   └── java/
│       └── com.analysis/
│           ├── PhpCore.java      # Contains core interfaces: DocTag, PhpDocBlock, 
│           │                     # PhpType, PhpVariable, UnionType
│           ├── TypeFactory.java  # Factory for creating single and union types
│           └── TypeResolver.java # Main logic for type resolution
└── test/
    └── java/
        └── com.analysis/
            └── TypeResolverTest.java # Comprehensive JUnit/TestNG test suite
```

---

## Testing

This project uses **Gradle** for build automation and testing. The test suite covers edge cases including name mismatches, malformed DocBlocks, and complex union types.

### Running Tests
To execute the test suite, run the following command in the root directory:

```bash
./gradlew test
