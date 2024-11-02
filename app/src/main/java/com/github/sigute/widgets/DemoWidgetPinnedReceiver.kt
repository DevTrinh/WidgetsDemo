package com.github.sigute.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews

class DemoWidgetPinnedReceiver : BroadcastReceiver() {
    companion object {
        const val WIDGET_NAME = "WIDGET_NAME"
        const val BROADCAST_ID = 123456

        fun getPendingIntent(context: Context, widgetName: String): PendingIntent {
            val callbackIntent = Intent(context, DemoWidgetPinnedReceiver::class.java)
            callbackIntent.putExtra(WIDGET_NAME, widgetName)
            // Thêm flag để mỗi PendingIntent là unique
            return PendingIntent.getBroadcast(
                context,
                System.currentTimeMillis().toInt(), // Sử dụng timestamp để tạo unique request code
                callbackIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            Log.e("WidgetDebug", "Context or Intent is null")
            return
        }

        val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        val widgetName = intent.getStringExtra(WIDGET_NAME)

        Log.d("WidgetDebug", "DemoWidgetPinnedReceiver - Received widget ID: $widgetId, name: $widgetName")

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Log.e("WidgetDebug", "Invalid widget ID")
            return
        }

        if (widgetName == null) {
            Log.e("WidgetDebug", "Widget name is null")
            return
        }

        val widgetPreferences = WidgetPreferences(context)
        widgetPreferences.setWidgetValues(widgetId, widgetName)

        // Update chỉ widget cụ thể này thay vì update tất cả
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val remoteViews = RemoteViews(context.packageName, R.layout.widget)
        remoteViews.setTextViewText(R.id.name, widgetName)
        appWidgetManager.updateAppWidget(widgetId, remoteViews)

        Log.d("WidgetDebug", "Widget updated - ID: $widgetId, Name: $widgetName")
    }
}
