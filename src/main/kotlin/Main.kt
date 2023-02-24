import kotlin.math.abs

const val N = 3
const val M = 3

fun main() {
    // write your code here
    test()
}

enum class GameResult(val displayName: String) {
    GameNotFinished("Game not finished"),
    Draw("Draw"),
    PlayerWins("Player wins"),
    Impossible("Impossible");
    override fun toString(): String = displayName
}

enum class Act {
    Move,
    Shift;
}

enum class Figure (val symbol: Char) {
    Cross('X'),
    Zero('O');

    override fun toString(): String {
        return "$symbol"
    }

    companion object {
        fun Parse(c: Char) = Figure.values().single { it.symbol == c }
    }
}

const val MAX_RADIX = 36

data class Position(val row: Int, val col: Int) {
    override fun toString() = "${'a' + col}${(row + 1).toString(MAX_RADIX)}"

    operator fun plus(other: Position) = Position(this.row + other.row, this.col + other.col)
    operator fun minus(other: Position) = Position(this.row - other.row, this.col - other.col)
    operator fun times(v: Int) = Position(v * this.row, v * this.col)

    fun abs() = Position(abs(row), abs(col))

    companion object {
        val Up = Position(1, 0)
        val Down = Position(-1, 0)
        val Left = Position(0, -1)
        val Right = Position(0, 1)

        fun parse(place: CharSequence, n: Int = N): Position? {
            if (n >= MAX_RADIX)
                throw IllegalArgumentException("n must be less than $MAX_RADIX")
            val topRow = n.toString(MAX_RADIX).single()
            val rightColumn = 'a' + (n - 1)
            if (place.length != 2 ||
                place[0] !in 'a'..rightColumn ||
                (place[1] !in '1'..'9' && place[1] !in 'a'..topRow)
            )
                return null
            else {
                val row = when {
                    place[1].isDigit() -> place[1].code - '1'.code
                    place[1].isLetter() -> place[1].code - 'a'.code + 0xA
                    else -> throw IndexOutOfBoundsException()
                }
                val col = place[0].code - 'a'.code
                return Position(row, col)
            }
        }
    }
}

data class Board (val grid: Map<Position, Figure>, val n: Int = N) {
    constructor(n: Int = N): this(emptyMap<Position, Figure>(), n)

    fun drawBoard(): StringBuilder {
        val answer = StringBuilder()
        for(row in n - 1 downTo 0) {
            answer.append((row + 1).toString(MAX_RADIX))
            for (col in 0 until n) {
                val p = Position(row, col)
                val c = grid.getOrDefault(p, null)?.symbol ?: ' '
                answer.append(' ')
                answer.append(c)
            }
            answer.appendLine()
        }
        answer.append(' ')
        for (col in 0 until n) {
            answer.append(' ')
            answer.append('a' + col)
        }
        answer.appendLine()
        return answer
    }

    fun move(player: Figure, row: Int, col: Int): Board? {
        val p = Position(row, col)
        if (grid.getOrDefault(p, null) != null)
            return null
        val other = grid.toMutableMap()
        other[p] = player
        return Board(other.toMap())
    }

    fun shift(row: Int, col: Int): Board? {
        if (abs(row) + abs(col) != 1)
            return null
        val other = mutableMapOf<Position, Figure>()
        for (pair in grid) {
            val r = (n + row + pair.key.row) % n
            val c = (n + col + pair.key.col) % n
            val p = Position(r, c)
            other[p] = pair.value
        }
        return Board(other.toMap())
    }

    override fun toString() = drawBoard().toString()

    companion object {
        fun parse(board: CharSequence, n: Int = N): Board {
            board.forEach {
                if (it != ' ' && it != 'X' && it != 'O')
                    throw IllegalArgumentException("Invalid board string")
            }
            if (board.length != n * n)
                throw IndexOutOfBoundsException()
            val other = mutableMapOf<Position, Figure>()
            for (i in board.indices) {
                val ch = board[i]
                if (ch != ' ') {
                    val f = Figure.Parse(ch)
                    val r = (n - 1) - i / n
                    val c = i % n
                    val p = Position(r, c)
                    other[p] = f
                }
            }
            return Board(other, n)
        }
    }
}

data class Turn(val player: Figure, val act: Act, val point: Position) {
    override fun toString(): String = drawTurn().toString()

    fun drawTurn(): StringBuilder {
        val answer = StringBuilder()
        answer.append(player)
        when (act) {
            Act.Move -> answer.append(point)
            Act.Shift -> answer.append(
                when (point) {
                    Position.Up -> 'U'
                    Position.Down -> 'D'
                    Position.Left -> 'L'
                    Position.Right -> 'R'
                    else -> throw IndexOutOfBoundsException()
                }
            )
        }
        return answer
    }
}

class Victory (private val grid: Map<Position, Figure>) {
    val gameResult: GameResult = calculateResult()
    var winner: Figure? = null

    override fun toString(): String =
        if (winner != null)
            listOf(winner, gameResult).joinToString(" ")
        else
            gameResult.toString()

    private fun calculateResult(): GameResult {
        val victories = mutableMapOf<Figure, Int>()
        val directions = listOf(
            Position.Down, Position.Right,
            Position.Down + Position.Right, Position.Down - Position.Right
        )
        for (row in N - 1 downTo 0) {
            for (col in 0 until N) {
                val position = Position(row, col)
                val player = grid.getOrDefault(position, null)
                    ?: continue
                for (delta in directions) {
                    // visited cells
                    if (grid.getOrDefault(position - delta, null) == player)
                        continue
                    var victory = true
                    // check if there is a victory
                    for (m in M - 1 downTo 1) {
                        val p = position + delta * m
                        if (grid.getOrDefault(p, null) != player) {
                            victory = false
                            break
                        }
                    }
                    if (victory)
                        victories[player] = victories.getOrDefault(player, 0) + 1
                }
            }
        }
        val crosses = victories.getOrDefault(Figure.Cross, 0)
        val zeros = victories.getOrDefault(Figure.Zero, 0)
        val empty = N * N - grid.size
        return when {
            crosses > 0 && zeros > 0 -> GameResult.Impossible
            crosses > 0 -> {
                winner = Figure.Cross
                GameResult.PlayerWins
            }
            zeros > 0 -> {
                winner = Figure.Zero
                GameResult.PlayerWins
            }
            empty > 0 -> GameResult.GameNotFinished
            else -> GameResult.Draw
        }
    }
}

private data class TicTacToe (val board: Board,
    val player: Figure = Figure.Cross, val log: List<Turn> = listOf()) {
    val victory: Victory = Victory(board.grid)
    val result: GameResult = victory.gameResult
    val winner: Figure? = victory.winner
    override fun toString(): String =
        buildString {
            append(player)
            append(" ")
            append(victory)
            append("\n")
            append(board)
        }
}

private fun check(input: String, result: GameResult, winner: Figure? = null ) {
    val board = Board.parse(input)
    val ttt = TicTacToe(board)
    if (ttt.result != result)
        throw AssertionError("Expected $result, got ${ttt.result}")
    if (result == GameResult.PlayerWins && ttt.winner != winner)
        throw AssertionError("Expected $winner, got ${ttt.winner}")
    println(ttt.toString())
}

private fun test() {
    check("XXXOO  O ", GameResult.PlayerWins, Figure.Cross)
    check("XOXOXOXXO", GameResult.PlayerWins, Figure.Cross)
    check("XOOOXOXXO", GameResult.PlayerWins, Figure.Zero)
    check("XOXOOXXXO", GameResult.Draw)
    check("XO OOX X ", GameResult.GameNotFinished)
    check("XO XO XOX", GameResult.Impossible)
    check(" O X  X X", GameResult.GameNotFinished)
    check(" OOOO X X", GameResult.GameNotFinished)
    check("XOXOXXOOX", GameResult.PlayerWins, Figure.Cross)
}

//    val board = Board()
//    val ticTacToe = TicTacToe(board)
//    var player = 0
//    //var result = Result.GameNotFinished
//    while (ticTacToe.result == GameResult.GameNotFinished) {
//        println(ticTacToe.board)
//        var okay = false
//        while (!okay) {
//            val move = readln().split(' ', limit = 2)
//            if (move.size != 2 || move.any { it.any { !it.isDigit() } }) {
//                println("You should enter numbers!")
//                continue
//            }
//            val coordinates = move.map { it.toInt() - 1 }
//            if (coordinates.any { it !in 0 until N }) {
//                println("Coordinates should be from 1 to ${N}!")
//                continue
//            }
//            val (row, column) = coordinates
//            val c = ticTacToe.grid[row][column]
//            if (c != ' ') {
//                println("This cell is occupied! Choose another one!")
//                continue
//            }
//            ticTacToe.grid[row][column] = players[player]
//            player = (player + 1) % players.size
//            okay = true
//            result = ticTacToe.getResult()
//        }
//    }
//    println(ticTacToe.drawBoard())
//    if (result == Result.PlayerWins)
//        println("${ticTacToe.getWinner()} wins")
//    else
//        println(result)

//
//    private fun checkVictory(r: Int, c: Int, dr: Int, dc: Int): Figure? {
//        val position = Position(r, c)
//        val player = grid.getOrDefault(position, null)
//            ?: return null
//        var down = 0
//        var up = 0
//
//        while (up - down + 1 < M) {
//            val d = Position
//
//        }
//
//        for (i in 1 until M) {
//            val p = Position(r + dr * i, c + dc * i)
//            val mark = grid.getOrDefault(p, null)
//            if (mark != player)
//                return null
//        }
//        return player
//    }

//    private val victories: MutableMap<Figure, Int> = mutableMapOf<Figure, Int>()
//
//    private fun countVictory(r: Int, c: Int, dr: Int, dc: Int) {
//        val winner = checkVictory(r, c, dr, dc)
//        if (winner != null) {
//            victories[winner] = victories.getOrDefault(winner, 0) + 1
//        }
//    }
//
/*
        val count = mutableMapOf<Figure, Int>()
        victories.clear()
        for (pair in grid) {
            val position = pair.key
            countVictory(position.row, position.col, 1, 0)
            // FUUUUUUUUUUUUUUUUUUUU!!!!!!!!!!
        }


        grid.forEach {
            count[it.value] =

            if (players.contains(it)) {
                count[it] = count.getOrDefault(it, 0) + 1
            }
        }}

        val minimum = count.values.minOfOrNull { it } ?: 0
        val maximum = count.values.maxOfOrNull { it } ?: 0
        if (abs(minimum - maximum) > 1)
            return Result.Impossible
        countVictory(0, 0, 1, 1)
        countVictory(0, N - 1, 1, -1)
        for(i in 0 until N) {
            countVictory(i, 0, 0, 1)
            countVictory(0, i, 1, 0)
        }
        val totalVictories = victories.keys.size
        return when {
            totalVictories > 1 -> return Result.Impossible
            totalVictories == 1 -> return Result.PlayerWins
            grid.any { it.any { it == ' ' }} -> Result.GameNotFinished
            else -> Result.Draw
        }
    }


    fun getWinner() = victories.keys.singleOrNull()






}
*/
