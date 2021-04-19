package ru.skillbranch.kotlinexample.extensions

inline fun <T> List<T>.dropLastUntil(predicate: (T) -> Boolean): List<T> {
    if (!isEmpty()) {
        val iterator = listIterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            if (predicate(element)) {
                return take(iterator.previousIndex())
            }
        }
    }
    return emptyList()
}