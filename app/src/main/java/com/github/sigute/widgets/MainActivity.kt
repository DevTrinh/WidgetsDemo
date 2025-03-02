package com.github.sigute.widgets

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sorry: TextView
    private lateinit var widgetName: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sorry = findViewById(R.id.sorry)
        widgetName = findViewById(R.id.widget_name_edit_text)
        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                save()
            } else {
                Toast.makeText(this, getString(R.string.error_widget_pinning_not_available_sdk_version), Toast.LENGTH_SHORT).show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sorry.visibility = View.GONE
            widgetName.visibility = View.VISIBLE
            saveButton.visibility = View.VISIBLE
        } else {
            sorry.visibility = View.VISIBLE
            widgetName.visibility = View.GONE
            saveButton.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun save() {
        val widgetNameString = widgetName.text.toString()

        if (widgetNameString.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_widget_name_not_entered), Toast.LENGTH_SHORT).show()
            return
        }

        val appWidgetManager = AppWidgetManager.getInstance(this)
        val provider = ComponentName(this, DemoWidgetProvider::class.java)

        if (!appWidgetManager.isRequestPinAppWidgetSupported) {
            Toast.makeText(this, getString(R.string.error_widget_pinning_not_supported_by_launcher), Toast.LENGTH_SHORT).show()
            return
        }

        // Tạo preview
        val remoteViews = RemoteViews(packageName, R.layout.widget)
        remoteViews.setTextViewText(R.id.name, widgetNameString)

        // Tạo PendingIntent với timestamp để đảm bảo uniqueness
        val successCallback = DemoWidgetPinnedReceiver.getPendingIntent(this, widgetNameString)

        // Request pin widget
        val bundle = Bundle()
        bundle.putParcelable(AppWidgetManager.EXTRA_APPWIDGET_PREVIEW, remoteViews)
        appWidgetManager.requestPinAppWidget(provider, bundle, successCallback)

        Log.d("WidgetDebug", "Requested pin widget with name: $widgetNameString")
    }
}
