package hello

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.filter.DebuggingFilters.PrintResponse

class HelloClient(baseUrl: String) {

    val client: HttpHandler = JavaHttpClient()
    val baseUrl = baseUrl

    fun hello(lang: String = "en-US", name: String = ""): String {
        if (name != "") {
            return client(Request(GET, "$baseUrl/hello?name=$name")
                .header("Accept-Language", lang))
                .bodyString()
        } else {
            return client(Request(GET, "$baseUrl/hello")
                .header("Accept-Language", lang))
                .bodyString()
        }
    }

    fun echoHeaders(prefix: String = ""): String {
        return client(Request(GET, "$baseUrl/echo_headers"))
            .body.toString()
    }

    fun echoHeadersJson(): String {
        return client(Request(GET, "$baseUrl/echo_headers")
            .header("Content-type", "application/json"))
            .body.toString()
    }

    fun echoHeadersPrefix(prefix: String): Response {
        return client(Request(GET, "$baseUrl/echo_headers?as_response_headers_with_prefix=$prefix"))
    }
}