package hello

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

    fun echoHeaders(asJson: Boolean = false, prefix: String = ""): Response {
        var requestString: String = "http://localhost:9000/echo_headers"
        if (prefix != "") {
            requestString += "?as_response_headers_with_prefix=$prefix"
            return  client(Request(GET, requestString))
        } else if (asJson) {
            return client(Request(GET, requestString).header("Content-Type", "application/json"))
        } else {
            return client(Request(GET, requestString))
        }
    }
}