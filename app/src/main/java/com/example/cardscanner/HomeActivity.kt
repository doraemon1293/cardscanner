package com.example.cardscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val apiList by lazy {
        ArrayList<EntryModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        with(apiList) {
            add(EntryModel(R.drawable.image_labelling, getString(R.string.title_labelling), getString(R.string.desc_labelling), 0))
            add(EntryModel(R.drawable.text_recognition, getString(R.string.title_text), getString(R.string.desc_text), 1))
//            add(PojoApi(R.drawable.barcode_scanning, getString(R.string.title_barcode), getString(R.string.desc_barcode), 2))
            add(EntryModel(R.drawable.landmark_identification, getString(R.string.title_landmark), getString(R.string.desc_landmark), 3))
            add(EntryModel(R.drawable.face_detection, getString(R.string.title_face), getString(R.string.desc_face), 4))
        }

        rvHome.layoutManager = LinearLayoutManager(this)
        rvHome.adapter = HomeAdapter(apiList)
    }
}
