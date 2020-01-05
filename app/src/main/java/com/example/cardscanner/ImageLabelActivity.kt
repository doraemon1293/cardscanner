package com.example.cardscanner

import android.graphics.Bitmap
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.example.cardscanner.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_image_label.*

class ImageLabelActivity : BaseCameraActivity() {

    private var itemsList: ArrayList<Any> = ArrayList()
    private lateinit var itemAdapter: ImageLabelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet(R.layout.layout_image_label)
        rvLabel.layoutManager = LinearLayoutManager(this)
    }

//    private fun getLabelsFromDevice(bitmap: Bitmap) {
//        val image = FirebaseVisionImage.fromBitmap(bitmap)
//        val detector = FirebaseVision.getInstance().visionLabelDetector
//        itemsList.clear()
//        detector.detectInImage(image)
//                .addOnSuccessListener {
//                    // Task completed successfully
//                    fabProgressCircle.hide()
//                    itemsList.addAll(it)
//                    itemAdapter = ImageLabelAdapter(itemsList, false)
//                    rvLabel.adapter = itemAdapter
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
//                }
//                .addOnFailureListener {
//                    // Task failed with an exception
//                    fabProgressCircle.hide()
//                    Toast.makeText(baseContext,"Sorry, something went wrong!",Toast.LENGTH_SHORT).show()
//                }
//    }

    private fun getLabelsFromClod(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
                .visionCloudLabelDetector
        itemsList.clear()
        detector.detectInImage(image)
                .addOnSuccessListener {
                    fabProgressCircle.hide()
                    itemsList.addAll(it)
                    itemAdapter = ImageLabelAdapter(itemsList, true)
                    rvLabel.adapter = itemAdapter
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                }
                .addOnFailureListener {
                    fabProgressCircle.hide()
                    it.printStackTrace()
                    Toast.makeText(baseContext,"Sorry, something went wrong!",Toast.LENGTH_SHORT).show()
                }
    }

    override fun onClick(v: View?) {
        fabProgressCircle.show()
        cameraView.captureImage { cameraKitImage ->
            // Get the Bitmap from the captured shot
            getLabelsFromClod(cameraKitImage.bitmap)
            runOnUiThread {
                showPreview()
                imagePreview.setImageBitmap(cameraKitImage.bitmap)
            }
        }
    }

}
