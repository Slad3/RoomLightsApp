package com.Slad3.roomlights

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        request("")

        createNotificationChannel()
        notificationSend(this.applicationContext)

    }

    private fun notificationSend(context: Context){
//        val tempIntent: Intent = Intent(this, this.javaClass).apply {
//            action = Intent.ACTION_SEND
//
//        }
        val intentAction = Intent(context, ActionReceiver::class.java)

        intentAction.putExtra("action", "actionName")


        val replyActionPendingIntent = PendingIntent.getBroadcast(context, 0, intentAction, 0);


        val notificationBuilder =
//            NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.iyiuy71z)
//                .setContentTitle("Lights")
//                .setContentText("Description")
////                .setStyle(NotificationCompat.BigTextStyle().bigText("Big Text"))
////                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .addAction(R.drawable.iyiuy71z, "Previous", replyActionPendingIntent)
//                .setOngoing(true)
////                .setStyle(
////                    androidx.media.app.NotificationCompat.MediaStyle()
//////                        .setShowActionsInCompactView(0)
////                )
            NotificationCompat.Builder(context, getString(R.string.primary_notification_channel_id))
                .setSmallIcon(R.drawable.iyiuy71z)
                .setContentTitle("Lights")
                .setContentText("Description")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Big Text"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(replyActionPendingIntent)
                .setAutoCancel(false)
                .addAction(R.drawable.iyiuy71z, "Previous", replyActionPendingIntent)
                .setOngoing(true)
//                .setStyle(
//                    androidx.media.app.NotificationCompat.MediaStyle()
////                        .setShowActionsInCompactView(0)
//                )


                with(NotificationManagerCompat.from(this)) {
                    notify(0, notificationBuilder!!.build())
                }
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.primary_notification_channel_name)
            val descriptionText = getString(R.string.primary_notification_channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                    getString(R.string.primary_notification_channel_id),
                    name,
                importance
            ).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun request(extension: String){
        val url = URL("https://benbarcaskey.com/")

        url.readText()

//        val conn = url.openConnection() as HttpURLConnection
//        conn.requestMethod = "GET"
//
//        BufferedReader(
//            InputStreamReader(conn.inputStream)
//        ).use { reader ->
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                println(line)
//            }
//        }


    }

    class ActionReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {

            println("here")
            Toast.makeText(context,"recieved", Toast.LENGTH_LONG).show();
            val action = intent.getStringExtra("action")
            if (action == "action1") {
                performAction1()
            } else if (action == "action2") {
                performAction2()
            }
            else{
                performDefaultAction()
            }
            //This is used to close the notification tray
            val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            context.sendBroadcast(it)
        }

        fun performAction1() {

            println("action 1")
        }
        fun performAction2() {
            println("action 2")
        }
        fun performDefaultAction() {
            println("default action")
        }
    }

}

