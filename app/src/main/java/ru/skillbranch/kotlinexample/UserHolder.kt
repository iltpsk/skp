package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User = registerByEmailOrPhone(fullName, email = email, password = password)

    fun registerUserByPhone(fullName: String, rawPhone: String): User =
        registerByEmailOrPhone(fullName, phone = rawPhone)

    private fun registerByEmailOrPhone(
        fullName: String,
        email: String? = null,
        password: String? = null,
        phone: String? = null
    ): User = User.makeUser(fullName = fullName, email = email, password = password, phone = phone)
        .also { user ->
            if (map[user.login] == null) {
                map[user.login] = user
            } else {
                throw IllegalArgumentException("A user with this ${if (email.isNullOrBlank()) "email" else "phone"} already exists")
            }
        }

    fun loginUser(login: String, password: String): String? =
        (map[login.trim()] ?: map[login.normalizePhone()])?.let {
            if (it.checkPassword(password)) it.userInfo else null
        }


    fun requestAccessCode(login: String) {
        map[login.normalizePhone()]?.apply {
            accessCode?.let { changePassword(it, generateAccessCode()) }
        }
    }

    fun importUsers(list: List<String>): List<User> {
        return list.map { str ->
            str.split(";")
                .also {
                    if (it.size != 5) {
                        throw IllegalStateException("Line \"$str\" has another fields count: ${it.size - 1}, expected: 4")
                    }
                }
        }.map { row ->
            val fullName = row[0].trim()
            val email = if (row[1].isBlank()) null else row[1].trim()
            val (salt, passwordHash) = row[2].split(":")
                .filter { str -> str.isNotBlank() }
                .run {
                    println("Pair salt:passwordHash size: $size")
                    if (size != 2) {
                        throw IllegalStateException("Pair salt:passwordHash is invalid $this")
                    } else {
                        first() to last()
                    }
                }

            val phone = if (row[3].isBlank()) null else row[3].normalizePhone()
            User.makeUser(fullName, email, salt, passwordHash = passwordHash, phone = phone)
                .also { user -> map[user.login] = user }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}