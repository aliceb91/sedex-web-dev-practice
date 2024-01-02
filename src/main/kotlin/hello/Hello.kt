package hello

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

val app: HttpHandler = routes(
    "/{lang}/hello" bind GET to { req: Request ->
        val languages: Map<String, out String> = mapOf(
            "en-US" to "Hello",
            "fr-FR" to "Bonjour",
            "en-AU" to "G'day",
            "it-IT" to "Salve",
            "en-GB" to "Alright?"
        )
        if (req.query("name") != null && req.path("lang") == "en-GB") {
            Response(OK).body("${languages["en-GB"].toString().dropLast(1)}, ${req.query("name")}?")
        } else if (req.query("name") != null) {
            Response(OK).body("${languages[req.path("lang")]} ${req.query("name")}")
        } else {
            Response(OK).body(languages[req.path("lang")].toString())
        }
    }
)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())
}