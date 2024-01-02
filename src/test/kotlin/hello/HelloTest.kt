package hello

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
        val result = app(Request(GET, "/hello")).status
        assertEquals(expected, result)
    }

    @Test
    fun `it returns the text Hello when called`() {
        val expected: String = "Hello"
        val result: String = app(Request(GET, "/hello")).body.toString()
        assertEquals(expected, result)
    }

    @Test
    fun `it returns Hello name when given name parameters with OK status codes`() {
        val expected1: Response = Response(OK).body("Hello Alice")
        val expected2: Response = Response(OK).body("Hello Jess")
        val result1: Response= app(Request(GET, "/hello?name=Alice"))
        val result2: Response = app(Request(GET, "/hello?name=Jess"))
        assertEquals(expected1, result1)
        assertEquals(expected2, result2)
    }
}