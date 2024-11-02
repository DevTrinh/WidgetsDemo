package com.github.sigute.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

class DemoWidgetProvider : AppWidgetProvider() {
    companion object {
        fun updateWidgetUI(context: Context, appWidgetManager: AppWidgetManager, widgetPreferences: WidgetPreferences, widgetId: Int) {
            val widgetName = widgetPreferences.getWidgetName(widgetId)
            Log.d("WidgetDebug", "Updating widget UI - ID: $widgetId, Name: $widgetName")

            if (widgetName != null) {
                val remoteViews = RemoteViews(context.packageName, R.layout.widget)
                remoteViews.setTextViewText(R.id.name, widgetName)
                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val widgetPreferences = WidgetPreferences(context)

        Log.d("WidgetDebug", "onUpdate called for widget IDs: ${appWidgetIds.joinToString()}")

        appWidgetIds.forEach { widgetId ->
            updateWidgetUI(context, appWidgetManager, widgetPreferences, widgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        val widgetPreferences = WidgetPreferences(context)
        appWidgetIds.forEach { widgetId ->
            widgetPreferences.removeWidget(widgetId)
            Log.d("WidgetDebug", "Widget deleted - ID: $widgetId")
        }
    }
}
