package com.github.sigute.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class WidgetPreferences(private val context: Context) {
    companion object {
        private const val PREF_FILE_NAME = "widget_data"
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    @SuppressLint("ApplySharedPref")
    fun setWidgetValues(widgetId: Int, name: String) {
        Log.d("WidgetDebug", "Saving widget - ID: $widgetId, Name: $name")
        preferences.edit()
            .putString(widgetId.toString(), name)
            .commit()
    }

    fun getWidgetName(widgetId: Int): String? {
        val name = preferences.getString(widgetId.toString(), null)
        Log.d("WidgetDebug", "Getting widget name - ID: $widgetId, Name: $name")
        return name
    }

    fun removeWidget(widgetId: Int) {
        preferences.edit()
            .remove(widgetId.toString())
            .apply()
    }
}