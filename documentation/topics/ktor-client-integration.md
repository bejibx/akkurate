# Ktor Client

Ktor Client is a framework for building asynchronous client-side applications. %product% provides an integration to
automatically validate downloaded data.

![A code example of the Ktor Client integration, used to showcase %product% on social networks.](social-ktor-client.png)
{width="700" border-effect="rounded"}

## Installation

Before using %product% with Ktor, you need to add the following dependency to your Gradle script:

<procedure title="Add the %product% plugin for Ktor" id="install-akkurate">

<code-block lang="kotlin">
implementation("dev.nesk.akkurate:akkurate-ktor-client:%version%")
</code-block>

</procedure>

> The %product% plugin requires the [Content Negotiation](https://ktor.io/docs/client-serialization.html)
> plugin, see its official documentation page for installation.

{style="note"}

## Validate on deserialization

Let's take [The One API](https://the-one-api.dev/), an API dedicated to The Lord of the Rings. We can write the
following code to retrieve a book list:

<compare type="top-bottom" first-title="Code" second-title="Output">

```kotlin
@Serializable
data class ListBooksResponse(val docs: List<Book>)

@Serializable
data class Book(@SerialName("_id") val id: String, val name: String)

suspend fun main() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val apiUrl = "https://the-one-api.dev/v2/book"
    val response: HttpResponse = client.get(apiUrl)
    val data = response.body<ListBooksResponse>()
    println(data)
}
```

```text
BookPayload(docs=[
    Book(id=5cf5805fb53e011a64671582, name=The Fellowship Of The Ring),
    Book(id=5cf58077b53e011a64671583, name=The Two Towers),
    Book(id=5cf58080b53e011a64671584, name=The Return Of The King)
])
```

</compare>


Although this API is probably well maintained, we fear potential issues on the API side, like bugs or breaking changes.
To protect ourselves, we want to validate the response body:

```kotlin
val validateBookList = Validator<ListBooksResponse> {
    docs.each {
        id.isNotEmpty()
        name.isNotEmpty()
    }
}
```

And we register this validator in our client:

```kotlin
val client = HttpClient(CIO) {
    /* ... */
    install(Akkurate) {
        registerValidator(validateBookList)
    }
}
```

Now, when deserializing the response body, the `ListBooksResponse` instance is automatically validated and throws when
invalid:

```kotlin
try {
    val data = response.body<ListBookResponse>()
    println(data)
} catch (exception: ValidationResult.Exception) {
    System.err.println("Invalid response: ${exception.violations}")
}
```

<seealso style="cards">
  <category ref="related">
    <a href="ktor-server-integration.md" />
    <a href="ktor-validation-tutorial.md" />
  </category>
</seealso>
