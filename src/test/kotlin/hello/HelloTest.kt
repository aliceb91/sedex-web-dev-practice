package hello

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HelloTest {

    @Test
    fun `it returns a 200 OK status code`() {
        val expected = OK
        val result = app(Request(GET, "/hello")).status
        assertEquals(expected, result)
    }

    @Test
    fun `it returns the text Hello when called`() {
        val expected: String = "Hello"
        val result: String = app(Request(GET, "/hello")).body.toString()
        assertEquals(expected, result)
    }
}