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

    fun echoHeaders(prefix: String ="", json: Boolean = false): Map<String, String> {
        if (prefix != "") {
            return echoHeadersPrefix(prefix)
        } else if (json == true) {
            return echoHeadersJson()
        } else {
            return echoHeadersBase()
        }
    }

    fun echoHeadersBase(): Map<String, String> {
        return client(Request(GET, "$baseUrl/echo_headers"))
            .body
            .toString()
            .split("\n")
            .map {
                val splitList = it.split(": ")
                Pair(splitList[0], splitList[1])
            }
            .toMap()
    }

    fun echoHeadersJson(): Map<String, String> {
        return client(Request(GET, "$baseUrl/echo_headers")
            .header("Content-type", "application/json"))
            .body
            .toString()
            .substring(1)
            .dropLast(1)
            .split(",")
            .map {
                val splitList = it.split(":", limit = 2)
                Pair(
                    splitList[0].substring(1).dropLast(1),
                    splitList[1].substring(1).dropLast(1)
                )
            }
            .toMap()
    }

    fun echoHeadersPrefix(prefix: String): Map<String, String> {
        return client(Request(GET, "$baseUrl/echo_headers?as_response_headers_with_prefix=$prefix"))
            .headers
            .map {
                Pair(it.first.lowercase(), it.second.toString().lowercase())
            }
            .toMap()
    }
}