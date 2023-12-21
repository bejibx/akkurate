# Akkurate

[**Get started by reading the documentation**](https://akkurate.dev)

Akkurate is a validation library taking advantage of the expressive power of Kotlin. No need \
for 30+ annotations or complicated custom constraints; write your validation code in Kotlin with a beautiful declarative
API.

Designed from scratch to handle complex business logic, its role is to help you write qualitative and maintainable
validation code.

<img src="documentation/images/social.png" witdh=700 height=350 alt="A code example of Akkurate used to showcase the library on social networks." />

> [!WARNING]
> Akkurate is under development and, despite being heavily tested, its API isn't yet stabilized; _breaking changes
> might happen on minor releases._ However, we will always provide migration guides.
>
> Report any issue or bug <a href="/issues">in the GitHub repository.</a>

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

- [**Beautiful DSL and API**](https://akkurate.dev/docs/harness-the-dsl.html) \
  Write crystal clear validation code and keep it <tooltip term="DRY">DRY</tooltip>. Use loops and conditions when
  needed; forget about annotation hell.

- [**Bundled with all the essential constraints**](https://akkurate.dev/docs/apply-constraints.html) \
  Write custom constraints for your business logic and nothing more. The rest? It's on us!

- [**Easily and highly extendable**](https://akkurate.dev/docs/extend.html) \
  No need to write verbose code to create custom constraints, within Akkurate they're as simple as a lambda with
  parameters.

- [**Contextual and asynchronous**](https://akkurate.dev/docs/use-external-sources.html) \
  Query sync/async data sources whenever you need to, like a database, or a REST API. Everything can happen inside
  validation.

- [**Integrated with your favorite tools**](https://akkurate.dev/docs/integrations.html) \
  Validate your data within any popular framework, we take care of the integrations for you.

- ⏱ **Code once, deploy everywhere** \
  Take advantage of Kotlin Multiplatform; write your validation code once, use it both for front-end and back-end
  usages.

- ⏱ **Testable out of the box** \
  Finding how to test your validation code shouldn't be one of your tasks. You will find all the tools you need to write
  good tests.

> [!NOTE]
> Features marked with ⏱ are on the roadmap.

