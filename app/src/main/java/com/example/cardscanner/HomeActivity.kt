package com.example.cardscanner

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    val TAG = "HomeActivity"
    private val apiList by lazy {
        ArrayList<EntryModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW))
        }

        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!![key]
                Log.d(TAG, "Key: $key Value: $value")
            }
        }


        // Get token
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    // Get new Instance ID token
                    val token = task.result!!.token
                    Log.d(TAG, "token: " + token)
                    SharedUtil.getIntance(this).writeShared("token", token)


                })


        with(apiList) {
            add(EntryModel(R.drawable.image_labelling, getString(R.string.title_labelling), getString(R.string.desc_labelling), 0))
            add(EntryModel(R.drawable.text_recognition, getString(R.string.title_text), getString(R.string.desc_text), 1))
//            add(EntryModel(R.drawable.barcode_scanning, getString(R.string.title_barcode), getString(R.string.desc_barcode), 2))
            add(EntryModel(R.drawable.landmark_identification, getString(R.string.title_landmark), getString(R.string.desc_landmark), 3))
            add(EntryModel(R.drawable.face_detection, getString(R.string.title_face), getString(R.string.desc_face), 4))
            add(EntryModel(R.drawable.cards_list, getString(R.string.title_cards_list), getString(R.string.desc_cards_list), 2))

        }

        rvHome.layoutManager = LinearLayoutManager(this)
        rvHome.adapter = HomeAdapter(apiList)


    }
}
