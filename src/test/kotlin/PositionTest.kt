import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PositionTest {

    @Test
    fun testToString() {
        assertEquals("a1", Position(0, 0).toString())
        assertEquals("a3", Position(2, 0).toString())
        assertEquals("b2", Position(1, 1).toString())
        assertEquals("c3", Position(2, 2).toString())
        assertEquals("zo", Position(23, 25).toString())
    }

    @Test
    fun testParse() {
        assertEquals(Position(0, 0), Position.parse("a1"))
        assertEquals(Position(1, 1), Position.parse("b2"))
        assertEquals(Position(2, 2), Position.parse("c3"))
        assertEquals(Position(2, 0), Position.parse("a3"))
        assertEquals(null, Position.parse("zo"))
        assertEquals(null, Position.parse("a"))
        val p = Position.parse("zz", 35)
        assertEquals(Position(35, 25), p)
    }

    @Test
    fun testPlus() {
        assertEquals(Position(3, 3), Position(1, 1) + Position(2, 2))
        assertEquals(Position(0, 1), Position(1, 1) + Position(-1, 0))
    }

    @Test
    fun testMinus() {
        assertEquals(Position(-1, -1), Position(1, 1) - Position(2, 2))
        assertEquals(Position(2, 1), Position(1, 1) - Position(-1, 0))
    }

    @Test
    fun testTimes() {
        assertEquals(Position(2, 2), Position(1, 1) * 2)
        assertEquals(Position(-3, 3), Position(-1, 1) * 3)
        assertEquals(Position(0, 0), Position(-1, 1) * 0)
    }

    @Test
    fun abs() {
        assertEquals(Position(2, 2), Position(-2, -2).abs())
        assertEquals(Position(1, 0), Position(-1, 0).abs())
        assertEquals(Position(0, 0), Position(0, 0).abs())
    }
}