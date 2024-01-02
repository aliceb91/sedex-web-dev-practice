package hello

import org.http4k.core.Headers
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HelloTest {

    @Test
    fun `it returns a 200 OK status code`() {
        val expected: Status = OK
        val result = app(Request(GET, "/en-US/hello")).status
        assertEquals(expected, result)
    }

    @Test
    fun `it returns the text Hello when called`() {
        val expected: String = "Hello"
        val result: String = app(Request(GET, "/en-US/hello")).body.toString()
        assertEquals(expected, result)
    }

    @Test
    fun `it returns Hello name when given name parameters with OK status codes`() {
        val expected1: Response = Response(OK).body("Hello Alice")
        val expected2: Response = Response(OK).body("Hello Jess")
        val result1: Response= app(Request(GET, "/en-US/hello?name=Alice"))
        val result2: Response = app(Request(GET, "/en-US/hello?name=Jess"))
        assertEquals(expected1, result1)
        assertEquals(expected2, result2)
    }

    @Test
    fun `it accounts for language modifiers`() {
        val expected1: Response = Response(OK).body("Hello Alice")
        val expected2: Response = Response(OK).body("Bonjour Alice")
        val expected3: Response = Response(OK).body("G'day Alice")
        val expected4: Response = Response(OK).body("Salve Alice")
        val expected5: Response = Response(OK).body("Alright, Alice?")
        val result1: Response = app(Request(GET, "/en-US/hello?name=Alice"))
        val result2: Response = app(Request(GET, "/fr-FR/hello?name=Alice"))
        val result3: Response = app(Request(GET, "/en-AU/hello?name=Alice"))
        val result4: Response = app(Request(GET, "/it-IT/hello?name=Alice"))
        val result5: Response = app(Request(GET, "/en-GB/hello?name=Alice"))
        assertEquals(expected1, result1)
        assertEquals(expected2, result2)
        assertEquals(expected3, result3)
        assertEquals(expected4, result4)
        assertEquals(expected5, result5)
    }

    @Test
    fun `it returns the correct greeting when no name is given`() {
        val expected1: Response = Response(OK).body("Hello")
        val expected2: Response = Response(OK).body("Bonjour")
        val expected3: Response = Response(OK).body("G'day")
        val expected4: Response = Response(OK).body("Salve")
        val expected5: Response = Response(OK).body("Alright?")
        val result1: Response = app(Request(GET, "/en-US/hello"))
        val result2: Response = app(Request(GET, "/fr-FR/hello"))
        val result3: Response = app(Request(GET, "/en-AU/hello"))
        val result4: Response = app(Request(GET, "/it-IT/hello"))
        val result5: Response = app(Request(GET, "/en-GB/hello"))
        assertEquals(expected1, result1)
        assertEquals(expected2, result2)
        assertEquals(expected3, result3)
        assertEquals(expected4, result4)
        assertEquals(expected5, result5)
    }

    @Test
    fun `echo_headers returns all request headers`() {
        val expected: Response = Response(OK).body("Test header: Test value")
        val result: Response = app(Request(GET, "/echo_headers").header("Test header", "Test value"))
        assertEquals(expected, result)
    }

    @Test
    fun `echo_headers returns multiple headers`() {
        val expected: Response = Response(OK).body("""
            Test header 1: Test value 1
            Test header 2: Test value 2
        """.trimIndent())
        val result: Response = app(Request(GET, "/echo_headers")
            .header("Test header 1", "Test value 1")
            .header("Test header 2", "Test value 2")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `echo_headers handles the example response`() {
        val expected: Response = Response(OK).body("""
            Accept: text/html
            Accept-Encoding: gzip
            Connection: keep-alive
            Accept-Language: en-GB
        """.trimIndent())
        val result: Response = app(Request(GET, "echo_headers")
            .header("Accept", "text/html")
            .header("Accept-Encoding", "gzip")
            .header("Connection", "keep-alive")
            .header("Accept-Language", "en-GB")
        )
        assertEquals(expected, result)
    }
}