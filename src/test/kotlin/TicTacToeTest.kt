import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TicTacToeTest {
    @Test
    fun crossWins() {
        val ttt = TicTacToe(Board.parse("XXXOO  O "))
        assertEquals(Figure.Cross, ttt.winner)
        assertEquals(GameResult.PlayerWins, ttt.result)
    }

    @Test
    fun crossFullWins() {
        val ttt = TicTacToe(Board.parse("XOXOXOXXO"))
        assertEquals(Figure.Cross, ttt.winner)
        assertEquals(GameResult.PlayerWins, ttt.result)
    }

    @Test
    fun zerosFullWins() {
        val ttt = TicTacToe(Board.parse("XOOOXOXXO"))
        assertEquals(Figure.Zero, ttt.winner)
        assertEquals(GameResult.PlayerWins, ttt.result)
    }

    @Test
    fun draw() {
        val ttt = TicTacToe(Board.parse("XOXOOXXXO"))
        assertEquals(null, ttt.winner)
        assertEquals(GameResult.Draw, ttt.result)
    }

    @Test
    fun unfinished1() {
        val ttt = TicTacToe(Board.parse("XO OOX X "))
        assertEquals(null, ttt.winner)
        assertEquals(GameResult.GameNotFinished, ttt.result)
    }

    @Test
    fun unfinished2() {
        val ttt = TicTacToe(Board.parse(" O X  X X"))
        assertEquals(null, ttt.winner)
        assertEquals(GameResult.GameNotFinished, ttt.result)
    }

    @Test
    fun impossible() {
        val ttt = TicTacToe(Board.parse("XO XO XOX"))
        assertEquals(null, ttt.winner)
        assertEquals(GameResult.Impossible, ttt.result)
    }

    @Test
    fun possibleWithShifts() {
        val ttt = TicTacToe(Board.parse(" OOOO X X"))
        assertEquals(null, ttt.winner)
        assertEquals(GameResult.GameNotFinished, ttt.result)
    }

    @Test
    fun stringRepresentation1() {
        val ttt = TicTacToe(Board.parse("XOXOOXXXO"))
        val img = "X Draw\n3 X O X\n2 O O X\n1 X X O\n  a b c\n"
        assertEquals(img, ttt.toString())
    }

    @Test
    fun stringRepresentation2() {
        val ttt = TicTacToe(Board.parse("XO   XXXO"))
        val img = "X Game not finished\n3 X O  \n2     X\n1 X X O\n  a b c\n"
        assertEquals(img, ttt.toString())
    }
}