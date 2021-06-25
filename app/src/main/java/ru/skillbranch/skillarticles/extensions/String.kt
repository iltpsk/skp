package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf(
    substr: String,
    ignoreCase: Boolean = true
): List<Int> {
    val matches: MutableList<Int> = mutableListOf()

    if (this.isNullOrEmpty() || substr.isEmpty()) {
        return matches
    }

    var startIndex = 0
    while (startIndex < length) {
        val res = indexOf(substr, startIndex, ignoreCase)

        if (res == -1) break
        matches.add(res)
        startIndex = res + substr.length
    }
    return matches
}