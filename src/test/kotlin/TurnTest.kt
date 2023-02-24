import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class TurnTest {

    @Test
    fun drawAction() {
        val crossTurn = Turn(Figure.Cross, Act.Move, Position(0, 1))
        val zeroTurn = Turn(Figure.Zero, Act.Shift, Position.Right)
        assertEquals("Xb1", crossTurn.toString())
        assertEquals("OR", zeroTurn.toString())
    }
}