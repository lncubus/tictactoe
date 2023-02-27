import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.test.assertFailsWith

class BoardTest {

    @Test
    fun drawEmpty() {
        val board = Board()
        assertEquals("3      \n2      \n1      \n  a b c\n", board.toString())
    }

    @Test
    fun drawDiagonal() {
        val map = hashMapOf(
            Position(0,2) to Figure.Cross,
            Position(1,1) to Figure.Cross,
            Position(2,0) to Figure.Cross
        )
        val board = Board(map)
        assertEquals("3 X    \n2   X  \n1     X\n  a b c\n", board.toString())
    }

    @Test
    fun move() {
        val map = hashMapOf(
            Position(0,2) to Figure.Cross,
            Position(1,1) to Figure.Cross,
            Position(2,0) to Figure.Cross
        )
        val startingBoard = Board(map)
        val invalidBoard = startingBoard.move(Figure.Zero, 2, 0)
        val board = startingBoard.move(Figure.Zero, 0, 0)
        assertEquals(null, invalidBoard)
        assertEquals("3 X    \n2   X  \n1 O   X\n  a b c\n", board?.toString())
    }

    @Test
    fun shift() {
        val map = hashMapOf(
            Position(0,2) to Figure.Cross,
            Position(1,1) to Figure.Zero,
            Position(2,0) to Figure.Cross
        )
        val startingBoard = Board(map)
        val invalidBoard = startingBoard.shift(2, 0)
        val boardLeft = startingBoard.shift(0, -1)
        val boardUp = startingBoard.shift(1, 0)
        val gridLeft = hashMapOf(
            Position(0,1) to Figure.Cross,
            Position(1,0) to Figure.Zero,
            Position(2,2) to Figure.Cross
        )
        val gridUp = hashMapOf(
            Position(1,2) to Figure.Cross,
            Position(2,1) to Figure.Zero,
            Position(0,0) to Figure.Cross
        )
        assertEquals(null, invalidBoard)
        assertEquals(gridLeft, boardLeft?.grid)
        assertEquals(gridUp, boardUp?.grid)
    }

    @Test
    fun parse() {
        val board = Board.parse("X  " + "XOX" + "  X")
        val map = hashMapOf(
            Position(2,0) to Figure.Cross,
            Position(1,0) to Figure.Cross,
            Position(1,1) to Figure.Zero,
            Position(1,2) to Figure.Cross,
            Position(0,2) to Figure.Cross
        )
        assertEquals(map, board.grid)
    }

    @Test
    fun parseWringChars() {
        assertFailsWith<IllegalArgumentException> {
            val board = Board.parse("X..XOX..X")
        }
    }

    @Test
    fun parseInvalidLength() {
        assertFailsWith<IndexOutOfBoundsException> {
            val board = Board.parse("X  XX  X")
        }
    }
}