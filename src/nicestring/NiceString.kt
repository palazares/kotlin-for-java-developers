package nicestring

fun String.isNice(): Boolean {
    val containsb = setOf("ba", "bu", "be").none { this.contains(it) }
    val containsVowel = this.count { it in "aeiou" } >= 3
    val containsDouble = this.zipWithNext().any { it.first == it.second }

    return listOf(containsb, containsVowel, containsDouble).count { it } >= 2
}