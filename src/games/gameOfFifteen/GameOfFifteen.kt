package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    private lateinit var emptyCell: Cell

    override fun initialize() {
        board.getAllCells()
                .zip(initializer.initialPermutation)
                .forEach { (cell, value) -> board[cell] = value }
        emptyCell = Cell(4, 4)
    }

    override fun canMove() = true

    override fun hasWon() = board.getAllCells().take(15).withIndex().all { (i, e) -> i + 1 == board[e] }

    override fun processMove(direction: Direction) {
        emptyCell = board.move(direction, emptyCell)
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

fun GameBoard<Int?>.move(direction: Direction, emptyCell: Cell): Cell {
    return when (direction) {
        Direction.UP -> if (emptyCell.i < width) Cell(emptyCell.i + 1, emptyCell.j).also { swapCells(it, emptyCell) } else emptyCell
        Direction.DOWN -> if (emptyCell.i > 1) Cell(emptyCell.i - 1, emptyCell.j).also { swapCells(it, emptyCell) } else emptyCell
        Direction.LEFT -> if (emptyCell.j < width) Cell(emptyCell.i, emptyCell.j + 1).also { swapCells(it, emptyCell) } else emptyCell
        Direction.RIGHT -> if (emptyCell.j > 1) Cell(emptyCell.i, emptyCell.j - 1).also { swapCells(it, emptyCell) } else emptyCell
    }
}

fun GameBoard<Int?>.swapCells(cell1: Cell, cell2: Cell) {
    val tmp = this[cell1]
    this[cell1] = this[cell2]
    this[cell2] = tmp
}
