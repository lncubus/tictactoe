import kotlin.math.abs

const val N = 3
const val M = 3
const val MAX_RADIX = 36

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

enum class Figure(val symbol: Char) {
    Cross('X'),
    Zero('O');

    override fun toString(): String {
        return "$symbol"
    }

    companion object {
        fun parse(c: Char) =
            Figure.values().single { it.symbol == c }
    }
}

data class Position(val row: Int, val col: Int) {
    override fun toString() = toString(Act.Move)

    fun toString(format: Act) =
        if (format == Act.Move )
            "${'a' + col}${(row + 1).toString(MAX_RADIX)}"
        else
            "(${row},${col})"

    operator fun plus(other: Position) =
        Position(this.row + other.row, this.col + other.col)
    operator fun minus(other: Position) =
        Position(this.row - other.row, this.col - other.col)
    operator fun times(v: Int) =
        Position(v * this.row, v * this.col)
    fun abs() =
        Position(abs(row), abs(col))

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
                (place[1] !in '1'..'9' && place[1] !in 'a'..topRow))
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

data class Board(val grid: Map<Position, Figure>, val n: Int = N) {
    constructor(n: Int = N) : this(emptyMap<Position, Figure>(), n)

    fun drawBoard(): StringBuilder {
        val answer = StringBuilder()
        for (row in n - 1 downTo 0) {
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
        return Board(other)
    }

    fun shift(row: Int, col: Int): Board? {
        if (abs(row) + abs(col) != 1) return null
        val other = mutableMapOf<Position, Figure>()
        for (pair in grid) {
            val r = (n + row + pair.key.row) % n
            val c = (n + col + pair.key.col) % n
            val p = Position(r, c)
            other[p] = pair.value
        }
        return Board(other)
    }

    override fun toString() = drawBoard().toString()

    companion object {
        fun parse(board: CharSequence, n: Int = N): Board {
            if (board.length != n * n)
                throw IndexOutOfBoundsException()
            board.forEach {
                if (it != ' ' && it != 'X' && it != 'O')
                    throw IllegalArgumentException("Invalid board string")
            }
            val other = mutableMapOf<Position, Figure>()
            for (i in board.indices) {
                val ch = board[i]
                if (ch != ' ') {
                    val f = Figure.parse(ch)
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
                    Position.Up -> "U"
                    Position.Down -> "D"
                    Position.Left -> "L"
                    Position.Right -> "R"
                    else -> point.toString(Act.Shift)
                }
            )
        }
        return answer
    }
}

class Victory(private val grid: Map<Position, Figure>) {
    val gameResult: GameResult = calculateResult()
    var winner: Figure? = null

    override fun toString(): String =
        if (winner == null)
            gameResult.toString()
        else
            listOf(winner, gameResult).joinToString(" ")


    private fun calculateResult(): GameResult {
        val victories = mutableMapOf<Figure, Int>()
        val directions = listOf(
            Position.Down,
            Position.Right,
            Position.Down + Position.Right,
            Position.Down - Position.Right
        )
        for (row in N - 1 downTo 0) {
            for (col in 0 until N) {
                val position = Position(row, col)
                val player = grid.getOrDefault(position, null) ?: continue
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

data class TicTacToe(val board: Board,
    val player: Figure = Figure.Cross,
    val log: List<Turn> = listOf()) {
    val victory: Victory = Victory(board.grid)
    val result: GameResult = victory.gameResult
    val winner: Figure? = victory.winner

    override fun toString(): String = buildString {
        append(player)
        append(" ")
        append(victory)
        append("\n")
        append(board)
    }
}