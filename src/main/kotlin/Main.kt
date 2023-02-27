fun main() {
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
