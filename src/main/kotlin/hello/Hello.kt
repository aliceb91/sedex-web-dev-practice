package hello

import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.routing.path
import org.http4k.core.Body
import org.http4k.core.with
import org.http4k.filter.DebuggingFilters
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import org.http4k.format.Jackson.json
import org.http4k.server.Http4kServer

val app: HttpHandler = routes(
    "/hello" bind GET to { req: Request ->
        val languages: Map<String, out String> = mapOf(
            "en-US" to "Hello",
            "fr-FR" to "Bonjour",
            "en-AU" to "G'day",
            "it-IT" to "Salve",
            "en-GB" to "Alright?"
        )
        val currentLang: String = languages[req.header("Accept-Language")] ?: "Hello"
        if (req.query("name") != null && req.header("Accept-Language") == "en-GB") {
            Response(OK).body("${currentLang.dropLast(1)}, ${req.query("name")}?")
        } else if (req.query("name") != null) {
            Response(OK).body("$currentLang ${req.query("name")}")
        } else {
            Response(OK).body(currentLang)
        }
    },

    "/echo_headers" bind GET to { req: Request ->
        if (req.query("as_response_headers_with_prefix") != null) {
            Response(OK).headers(
                req.headers.map {
                    Pair("${req.query("as_response_headers_with_prefix")}${it.first}", it.second)
                }
            )
        } else if (req.header("Content-Type") == "application/json") {
            Response(OK).with(
                Body.json().toLens() of req.headers.map {
                        it.first to it.second.asJsonValue()
                    }.asJsonObject()
            )
        } else {
            Response(OK).body(req.headers.map {
                "${it.first}: ${it.second}"
            }.joinToString("\n"))
        }
    }
)

fun helloServer(): Http4kServer {

    val server = app.asServer(SunHttp(9000))

    println("Server started on " + server.port())

    return server
}

fun main() {

    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())
}