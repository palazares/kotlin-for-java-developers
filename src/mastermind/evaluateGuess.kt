package mastermind

import kotlin.math.min

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    fun wrongPositionIndexes(s: String, g: String) = s.withIndex().filter { (i, x) -> x != g[i] }.map { (i, _) -> i }
    fun getStringMap(s: List<Char>) = s.groupBy { c -> c }.map { (c, l) -> c to l.count() }.toMap()
    fun wrongPositionCount(s: Map<Char, Int>, g: Map<Char, Int>) =
        s.map { c -> min(c.value, g.getOrDefault(c.key, 0)) }.sum()

    val wrongPositionIndexes = wrongPositionIndexes(secret, guess);
    val secretUnmatchedSymbolsMap = getStringMap(wrongPositionIndexes.map { i -> secret[i] })
    val guessUnmatchedSymbolsMap = getStringMap(wrongPositionIndexes.map { i -> guess[i] })

    return Evaluation(
        secret.length - wrongPositionIndexes.count(),
        wrongPositionCount(secretUnmatchedSymbolsMap, guessUnmatchedSymbolsMap)
    )
}

fun evaluateGuessFunc(secret: String, guess: String): Evaluation {
    val rightPositions = secret.zip(guess).count { (s, g) -> s == g }
    val commonLetters = "ABCDEF".sumBy { ch ->

        min(secret.count { it == ch }, guess.count { it == ch })
    }
    return Evaluation(rightPositions, commonLetters - rightPositions)
}
