import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class VictoryTest {
    @Test
    fun crossVictory() {
        val map = hashMapOf(
            Position(0, 2) to Figure.Cross,
            Position(1, 1) to Figure.Cross,
            Position(2, 0) to Figure.Cross
        )
        val crossVictory = Victory(map)
        assertEquals(GameResult.PlayerWins, crossVictory.gameResult)
        assertEquals(Figure.Cross, crossVictory.winner)
        assertEquals("X Player wins", crossVictory.toString())
    }

    @Test
    fun zerosVictory() {
        val map = hashMapOf(
            Position(0, 2) to Figure.Cross,
            Position(0, 1) to Figure.Cross,
            Position(0, 0) to Figure.Zero,
            Position(1, 2) to Figure.Cross,
            Position(1, 1) to Figure.Zero,
            Position(1, 0) to Figure.Cross,
            Position(2, 2) to Figure.Zero,
            Position(2, 1) to Figure.Cross,
            Position(2, 0) to Figure.Zero
        )
        val zeroVictory = Victory(map)
        assertEquals(GameResult.PlayerWins, zeroVictory.gameResult)
        assertEquals(Figure.Zero, zeroVictory.winner)
        assertEquals("O Player wins", zeroVictory.toString())
    }

    @Test
    fun impossible() {
        val map = hashMapOf(
            Position(0, 2) to Figure.Cross,
            Position(0, 1) to Figure.Cross,
            Position(0, 0) to Figure.Cross,
            Position(1, 2) to Figure.Zero,
            Position(1, 1) to Figure.Zero,
            Position(1, 0) to Figure.Zero
        )
        val impossible = Victory(map)
        assertEquals(null, impossible.winner)
        assertEquals(GameResult.Impossible, impossible.gameResult)
        assertEquals("Impossible", impossible.toString())
    }

    @Test
    fun draw() {
        val map = hashMapOf(
            Position(0, 2) to Figure.Cross,
            Position(0, 1) to Figure.Zero,
            Position(0, 0) to Figure.Cross,
            Position(1, 2) to Figure.Cross,
            Position(1, 1) to Figure.Zero,
            Position(1, 0) to Figure.Cross,
            Position(2, 2) to Figure.Zero,
            Position(2, 1) to Figure.Cross,
            Position(2, 0) to Figure.Zero
        )
        val draw = Victory(map)
        assertEquals(null, draw.winner)
        assertEquals(GameResult.Draw, draw.gameResult)
        assertEquals("Draw", draw.toString())
    }

    @Test
    fun nobodyWins() {
        val map = hashMapOf(
            Position(0, 2) to Figure.Cross,
            Position(0, 1) to Figure.Cross,
            Position(0, 0) to Figure.Cross,
            Position(1, 2) to Figure.Cross,
            Position(1, 1) to Figure.Cross,
            Position(1, 0) to Figure.Cross,
            Position(2, 2) to Figure.Cross,
            Position(2, 1) to Figure.Cross,
            Position(2, 0) to Figure.Cross
        )
        val draw = Victory(map, 3, 5)
        assertEquals(null, draw.winner)
        assertEquals(GameResult.Draw, draw.gameResult)
        assertEquals("Draw", draw.toString())
    }

}