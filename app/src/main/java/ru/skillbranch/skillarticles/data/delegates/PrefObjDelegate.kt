package ru.skillbranch.skillarticles.data.delegates

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.skillbranch.skillarticles.data.PrefManager
import ru.skillbranch.skillarticles.data.adapters.JsonAdapter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefObjDelegate<T>(private val adapter: JsonAdapter<T>, private val customKey: String? = null) :
    ReadWriteProperty<PrefManager, T?> {
    private var _storedValue: T? = null

    override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
        val key = stringPreferencesKey(customKey ?: property.name)
        _storedValue = value
        //set non blocking on coroutine
        thisRef.scope.launch {
            thisRef.dataStore.edit { prefs ->
                prefs[key] = adapter.toJson(value)
            }
        }
    }

    override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
        if (_storedValue == null) {
            val key = stringPreferencesKey(customKey ?: property.name)
            //async flow
            val flowValue = thisRef.dataStore.data
                .map { prefs ->
                    prefs[key] ?: ""
                }
            //sync read (on IO Dispatcher and return result on call thread)
            _storedValue = runBlocking(Dispatchers.IO) { flowValue.first().let { adapter.fromJson(it) } }
        }
        return _storedValue
    }
}