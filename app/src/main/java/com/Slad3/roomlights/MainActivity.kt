package com.Slad3.roomlights

import android.R.id.input
import android.R.id.message
import android.annotation.SuppressLint
import android.app.Notification
import android.app.Notification.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.net.URL


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var ipAddress: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val ipText: EditText = findViewById(R.id.ipText)
        val submitButton: Button = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {

            println(ipText.text.toString())
            ipAddress = ipText.text.toString()
        }

        createNotificationChannel()
        notificationSend(this.applicationContext)

    }

    @SuppressLint("WrongConstant")
    private fun notificationSend(context: Context){

        val broadcastIntentOn1 = Intent(this, MyBroadcastReceiver::class.java).apply{
            putExtra("full", "on1")
        }
        val actionIntentOn1 = PendingIntent.getBroadcast(this,0, broadcastIntentOn1, 0)

       val broadcastIntentOff1 = Intent(this, MyBroadcastReceiver::class.java).apply{
           putExtra("full", "off1")
       }
        val actionIntentOff1 = PendingIntent.getBroadcast(this,1, broadcastIntentOff1, 0)

//       val broadcastIntentOn2 = Intent(this, MyBroadcastReceiver::class.java).apply{
//           putExtra("full", "on2")
//       }
//        val actionIntentOn2 = PendingIntent.getBroadcast(this,0, broadcastIntentOn2, 0)
//
//
//
//       val broadcastIntentOff2 = Intent(this, MyBroadcastReceiver::class.java).apply{
//           putExtra("full", "off2")
//       }
//        val actionIntentOff2 = PendingIntent.getBroadcast(this, 0, broadcastIntentOff2, 0)
//



        val notificationBuilder = NotificationCompat.Builder(context, getString(R.string.primary_notification_channel_id))
                .setSmallIcon(R.drawable.light)
                .setContentTitle("Lights")
//                .setContentText("Description")
                .addAction(R.drawable.light, "On 1", actionIntentOn1)
                .addAction(R.drawable.light, "Off 1", actionIntentOff1)

                .setOnlyAlertOnce(true)
                .setAutoCancel(false)
                .setOngoing(true)
                .setVisibility(-1)

                .setStyle(NotificationCompat.BigTextStyle().bigText("Big Text"))
//                .setStyle(
//                    androidx.media.app.NotificationCompat.MediaStyle()
//                        .setShowActionsInCompactView(0, 1)
//                        .setMediaSession(null)
//
//                )

                with(NotificationManagerCompat.from(this)) {
                    notify(0, notificationBuilder.build())
                }
    }

    private fun createNotificationChannel() {
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
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    public fun request(extension: String){
        val tempHttp = RetrieveHttpTask(extension).execute()
    }

    public fun getIPAddress(): String { return ipAddress}

    class MyBroadcastReceiver : BroadcastReceiver() {

        fun request(extension: String){
            val tempHttp = RetrieveHttpTask( extension).execute()
        }

        override fun onReceive(
            context: Context,
            intent: Intent
        ) {


            val action: String? = intent.getStringExtra("action")
            val port: String? = intent.getStringExtra("port")
            println("Action:\t" + action + "\t" + port)
            println(intent.getStringExtra("full"))
            this.request(intent.getStringExtra("full"))


            //This is used to close the notification tray
//            val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
//            context.sendBroadcast(it)
        }

    }


    internal class RetrieveHttpTask(input: String) :
            AsyncTask<String?, Void?, Void?>() {
        private var exception: Exception? = null

        var task: String = input

        override fun doInBackground(vararg p0: String?): Void? {
            val endUrl = MainActivity.ipAddress + task
            println(endUrl)
            val url = URL(endUrl)
            url.readText()
            return null
        }

    }
}

