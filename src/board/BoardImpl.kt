package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(createSquareBoard(width))

class SquareBoardImpl(private val size: Int) : SquareBoard {
    override val width: Int
        get() = size

    private val cells: Array<Array<Cell>> = Array(width) { i -> Array(width) { j -> Cell(i + 1, j + 1) } }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if (i in 1..width && j in 1..width) cells[i - 1][j - 1] else null
    }

    override fun getCell(i: Int, j: Int): Cell {
        require(i in 1..width)
        require(j in 1..width)
        return cells[i - 1][j - 1]
    }

    override fun getAllCells(): Collection<Cell> {
        return cells.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return if (i in 1..width)
            jRange.filter { it in 1..width }.map { cells[i - 1][it - 1] }
        else
            listOf()
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return if (j in 1..width)
            iRange.filter { it in 1..width }.map { cells[it - 1][j - 1] }
        else
            listOf()
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? =
            when (direction) {
                UP -> if (i > 1) cells[i - 2][j - 1] else null
                DOWN -> if (i < width) cells[i][j - 1] else null
                RIGHT -> if (j < width) cells[i - 1][j] else null
                LEFT -> if (j > 1) cells[i - 1][j - 2] else null
            }
}

class GameBoardImpl<T>(squareBoard: SquareBoard) : SquareBoard by squareBoard, GameBoard<T> {
    private val map: MutableMap<Cell, T?> = squareBoard.getAllCells().associateWith { null }.toMutableMap()

    override fun get(cell: Cell): T? = map[cell]

    override fun set(cell: Cell, value: T?) {
        if (cell in map) map[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = map.filter { predicate(it.value) }.keys.toList()


    override fun find(predicate: (T?) -> Boolean): Cell? = map.entries.find { predicate(it.value) }?.key


    override fun any(predicate: (T?) -> Boolean): Boolean = map.values.any(predicate)


    override fun all(predicate: (T?) -> Boolean): Boolean = map.values.all(predicate)
}