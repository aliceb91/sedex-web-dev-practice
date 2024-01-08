package hello

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.filter.DebuggingFilters.PrintResponse

class HelloClient {

    val client: HttpHandler = JavaHttpClient()

    fun hello(lang: String = "en-US", name: String = ""): String {
        var requestString: String = "http://localhost:9000/hello"
        if (name != "") {
            requestString += "?name=$name"
        }
        val response: Response = client(Request(GET, requestString).header("Accept-Language", lang))
        return response.bodyString()
    }

    fun echoHeaders(prefix: String = ""): Response {
        return client(Request(GET, "http://localhost:9000/echo_headers"))
    }

    fun echoHeadersJson(): String {
        return client(Request(GET, "http://localhost:9000/echo_headers")
            .header("Content-type", "application/json"))
            .body.toString()
    }

    fun echoHeadersPrefix(prefix: String): Response {
        return client(Request(GET, "http://localhost:9000/echo_headers?as_response_headers_with_prefix=$prefix"))
    }
}