# Overview

%product% is a validation library taking advantage of the expressive power of Kotlin. No need \
for 30+ annotations or complicated custom constraints; write your validation code in Kotlin with a beautiful declarative
API.

Designed from scratch to handle complex business logic, its role is to help you write qualitative and maintainable
validation code.

> %product% is under development and, despite being heavily tested, its API isn't yet stabilized; _breaking changes
> might happen on minor releases._ However, we will always provide migration guides.
>
> Report any issue or bug <a href="%github_product_url%/issues">in the GitHub repository.</a>

{style="warning"}

## Showcase

Here's an example showcasing how you can constrain a book and its list of authors.

```kotlin
// Define your classes

@Validate
data class Book(
    val title: String,
    val releaseDate: LocalDateTime,
    val authors: List<Author>,
)

@Validate
data class Author(val firstName: String, val lastName: String)

// Write your validation rules

val validateBook = Validator<Book> {
    // First the property, then the constraint, finally the message.
    title.isNotEmpty() otherwise { "Missing title" }

    releaseDate.isInPast() otherwise { "Release date must be in past" }

    authors.hasSizeBetween(1..10) otherwise { "Wrong author count" }

    authors.each { // Apply constraints to each author
        (firstName and lastName) {
            // Apply the same constraint to both properties
            isNotEmpty() otherwise { "Missing name" }
        }
    }
}

// Validate your data

when (val result = validateBook(someBook)) {
    is Success -> println("Success: ${result.value}")
    is Failure -> {
        val list = result.violations
            .joinToString("\n") { "${it.path}: ${it.message}" }
        println("Failures:\n$list")
    }
}
```

Notice how each constraint applied to a property can be read like a sentence. This code:

```kotlin
title.isNotEmpty() otherwise { "Missing title" }
```

can be read:

```text
Check if 'title' is not empty otherwise write "Missing title".
```

## Features

- **Beautiful DSL and API.** Write crystal clear validation code and keep it <tooltip term="DRY">DRY</tooltip>. Use
  loops and conditions when needed; forget about annotation hell.
- **Bundled with all the essential constraints.** Write custom constraints for your business logic and nothing more. The
  rest? It's on us!
- **Easily and highly extendable.** No need to write verbose code to create custom constraints, within %product% they're
  as simple as a lambda with parameters.
- **Contextual and asynchronous.** Query sync/async data sources whenever you need to, like a database, or a REST API.
  Everything can happen inside validation.
- ![Coming soon](clock.png){width="16"} **Testable out of the box.** Finding how to test your validation code shouldn't
  be one of your tasks. You will find all the tools you need to write good tests.
- ![Coming soon](clock.png){width="16"} **Integrated with your favorite tools.** Validate your data within any popular
  framework, we take care of the integrations for you.
- ![Coming soon](clock.png){width="16"} **Code once, deploy everywhere.** Take advantage of Kotlin Multiplatform; write
  your validation code once, use it both for front-end and back-end usages.

> Features marked with ![Coming soon](clock.png){width="16"} are on the roadmap.

<seealso style="cards">
  <category ref="external">
    <a href="%github_product_url%">GitHub repository</a>
    <a href="%github_product_url%/discussions">Community</a>
    <a href="%github_product_url%/discussions">Roadmap</a>
  </category>
</seealso>
