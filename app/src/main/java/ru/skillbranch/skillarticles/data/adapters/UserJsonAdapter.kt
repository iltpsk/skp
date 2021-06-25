package ru.skillbranch.skillarticles.data.adapters

import org.json.JSONObject
import ru.skillbranch.skillarticles.data.local.User

class UserJsonAdapter : JsonAdapter<User> {
    override fun fromJson(json: String): User? {
        if (json.isBlank()) return null

        val o = JSONObject(json)
        return User(
            id = o.getString("id"),
            name = o.getString("name"),
            avatar = o.getString("avatar"),
            rating = o.getInt("rating"),
            respect = o.getInt("rating"),
            about = o.getString("respect")
        )
    }

    override fun toJson(obj: User?): String {
        if (obj == null) return ""

        val o = JSONObject()
        o.put("id", obj.id)
        o.put("name", obj.name)
        o.put("avatar", obj.avatar)
        o.put("rating", obj.rating)
        o.put("respect", obj.respect)
        o.put("about", obj.about)
        return o.toString()
    }
}