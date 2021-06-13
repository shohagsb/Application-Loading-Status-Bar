package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

private const val NOTIFICATION_ID = 0

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var downloadManager: DownloadManager

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        createChannel(
            getString(R.string.download_channel_name)
        )

        custom_button.setOnClickListener {
            if (URL != null) {
                custom_button.buttonState = ButtonState.Loading
                download()
            } else {
                Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            notificationManager.cancelAll()
            custom_button.buttonState = ButtonState.Completed
            val query = DownloadManager.Query()
            query.setFilterById(id!!)
            val cursor = downloadManager.query(query)
            var downloadStatus: String? = null
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
                if (DownloadManager.STATUS_SUCCESSFUL == status) {
                    downloadStatus = "Success"
                } else if (DownloadManager.STATUS_FAILED == status) {
                    downloadStatus = "Failed"
                }
            }
            downloadStatus?.let { status ->
                notificationManager.sendNotification(
                    applicationContext.getString(R.string.notification_description),
                    applicationContext,
                    status
                )
            }

        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(fileName)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private var URL: String? = null
        private var fileName: String? = null
        private const val CHANNEL_ID = "channelId"
    }

    // Create notification Chanel
    private fun createChannel(channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    //    Send notification extension function
    fun NotificationManager.sendNotification(
        messageBody: String,
        applicationContext: Context,
        status: String
    ) {
        val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        contentIntent.putExtra("file_name", fileName)
        contentIntent.putExtra("status", status)
        pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Build the notification
        val builder = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        ).setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(
                applicationContext
                    .getString(R.string.notification_title)
            )
            .setContentText(messageBody)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                applicationContext.getString(R.string.notification_button),
                pendingIntent
            )

        notify(NOTIFICATION_ID, builder.build())
    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_glide ->
                    if (checked) {
                        URL =
                            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
                        fileName = getString(R.string.glide_text)
                    }
                R.id.radio_load_app ->
                    if (checked) {
                        URL =
                            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                        fileName = getString(R.string.load_app_text)
                    }
                R.id.radio_retrofit ->
                    if (checked) {
                        URL =
                            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
                        fileName = getString(R.string.retrofit_text)
                    }
            }
        }
    }


}
