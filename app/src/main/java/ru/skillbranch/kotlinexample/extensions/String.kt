package ru.skillbranch.kotlinexample.extensions

fun String.normalizePhone(): String = this.replace("""[^+\d]""".toRegex(), "")
