# Ktor Server

Ktor Server is a framework for building asynchronous server-side applications. %product% provides an integration to
automatically validate incoming data.

> If you're searching for a more step-by-step guide to server-side
> validation, [check out this tutorial](ktor-validation-tutorial.md) to build an HTTP
> API with Ktor and %product%.

![A code example of the Ktor Server integration, used to showcase %product% on social networks.](social-ktor-server.png)
{width="700" border-effect="rounded"}

## Installation

> You can build a new Ktor project preconfigured with
> %product% [via start.ktor.io](https://start.ktor.io/p/akkurate)

{style="tip"}

Before using %product% with Ktor, you need to add the following dependency to your Gradle script:

<procedure title="Add the %product% plugin for Ktor" id="install-akkurate">

<code-block lang="kotlin">
implementation("dev.nesk.akkurate:akkurate-ktor-server:%version%")
</code-block>

</procedure>

> The %product% plugin requires the [Content Negotiation](https://ktor.io/docs/server-serialization.html)
> and [Request Validation](https://ktor.io/docs/server-request-validation.html) plugins, see their official
> documentation pages for installation.

{style="note"}

## Validate on deserialization

Take the following route that deserializes the body to a `Book` instance:

```kotlin
@Validate
@Serializable
data class Book(val title: String)

fun Application.configureRouting() {
    routing {
        post("/books") {
            val book = call.receive<Book>()

            // Do something with the book...
        }
    }
}
```

We want to make sure the book title is always filled, so we create a validator to enforce this:

```kotlin
val validateBook = Validator<Book> {
    title.isNotEmpty()
}
```

Then we register this validator in our Ktor configuration:

```kotlin
fun Application.configureSerialization() {
    install(ContentNegotiation) { json() }

    install(Akkurate)
    install(RequestValidation) {
        registerValidator(validateBook)
    }
}
```

Now, when deserializing the request body, the `Book` instance is automatically validated:

<compare type="top-bottom" first-title="cURL request" second-title="Response">

```shell
curl 127.0.0.1:8080/books \
    --header 'Content-Type: application/json' \
    --data '{"title": ""}'
```

```json5
// HTTP/1.1 422 Unprocessable Entity
// Content-Type: application/problem+json

{
  "status": 422,
  "fields": [
    {
      "message": "Must not be empty",
      "path": "title"
    }
  ],
  "type": "https://akkurate.dev/validation-error",
  "title": "The payload is invalid",
  "detail": "The payload has been successfully parsed, but the server is unable to accept it due to validation errors."
}
```

</compare>

> You can register as many validators as you need.

## Customize the response

A default response,
based [on the RFC 9457 (Problem Details for HTTP APIs)](https://www.rfc-editor.org/rfc/rfc9457.html), is returned when
the validation fails.

You can customize its status and `Content-Type` header:

```kotlin
install(Akkurate) {
    status = HttpStatusCode.BadRequest
    contentType = ContentType.Application.Json
}
```

If you need a different payload, you can override the whole response builder:

```kotlin
install(Akkurate) {
    buildResponse { call, violations ->
        call.respond(
            HttpStatusCode.BadRequest,
            violations.byPath.toString()
        )
    }
}
```

<seealso style="cards">
  <category ref="related">
    <a href="ktor-validation-tutorial.md" />
    <a href="ktor-client-integration.md" />
  </category>
</seealso>
