import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class FigureTest {

    @Test
    fun testToString() {
        assertEquals("X", Figure.Cross.toString())
        assertEquals("O", Figure.Zero.toString())
    }

    @Test
    fun testParse() {
        assertEquals(Figure.Cross, Figure.Parse('X'))
        assertEquals(Figure.Zero, Figure.Parse('O'))
    }

}