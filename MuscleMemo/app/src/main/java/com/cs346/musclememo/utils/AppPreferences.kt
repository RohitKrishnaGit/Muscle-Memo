package com.cs346.musclememo.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit


object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences("MuscleMemo.sharedprefs", MODE_PRIVATE)
        if (systemOfMeasurementDistance == "")
            systemOfMeasurementDistance = "km"
        if (systemOfMeasurementWeight == "")
            systemOfMeasurementWeight = "kg"
    }

    fun listen(listener: SharedPreferences. OnSharedPreferenceChangeListener){
        sharedPreferences?.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unListen(listener: SharedPreferences.OnSharedPreferenceChangeListener){
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(listener)
    }

    var accessToken: String?
        get() = Key.ACCESS_TOKEN.getString()
        set(value) = Key.ACCESS_TOKEN.setString(value)

    var refreshToken: String?
        get() = Key.REFRESH_TOKEN.getString()
        set(value) = Key.REFRESH_TOKEN.setString(value)

    var systemOfMeasurementDistance: String?
        get() = Key.SYSTEMMEASUREMENTDISTANCE.getString()
        set(value) = Key.SYSTEMMEASUREMENTDISTANCE.setString(value)

    var systemOfMeasurementWeight: String?
        get() = Key.SYSTEMMEASUREMENTWEIGHT.getString()
        set(value) = Key.SYSTEMMEASUREMENTWEIGHT.setString(value)

    var darkMode: Boolean?
        get() = Key.DARKMODE.getBoolean()
        set(value) = Key.DARKMODE.setBoolean(value)

    private enum class Key {
        ACCESS_TOKEN, REFRESH_TOKEN, SYSTEMMEASUREMENTDISTANCE, SYSTEMMEASUREMENTWEIGHT, DARKMODE;

        fun getBoolean(): Boolean? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getBoolean(name, false) else null
        fun getFloat(): Float? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getFloat(name, 0f) else null
        fun getInt(): Int? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getInt(name, 0) else null
        fun getLong(): Long? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getLong(name, 0) else null
        fun getString(): String? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(name, "") else null

        fun setBoolean(value: Boolean?) = value?.let { sharedPreferences!!.edit { putBoolean(name, value) } } ?: remove()
        fun setFloat(value: Float?) = value?.let { sharedPreferences!!.edit { putFloat(name, value) } } ?: remove()
        fun setInt(value: Int?) = value?.let { sharedPreferences!!.edit { putInt(name, value) } } ?: remove()
        fun setLong(value: Long?) = value?.let { sharedPreferences!!.edit { putLong(name, value) } } ?: remove()
        fun setString(value: String?) = value?.let { sharedPreferences!!.edit { putString(name, value) } } ?: remove()

        fun exists(): Boolean = sharedPreferences!!.contains(name)
        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}