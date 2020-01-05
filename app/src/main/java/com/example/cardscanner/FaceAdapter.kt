package com.example.cardscanner

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import kotlinx.android.synthetic.main.face_row.view.*

class FaceAdapter(private val faces: List<FirebaseVisionFace>) : RecyclerView.Adapter<FaceAdapter.FaceHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaceHolder {
        return FaceHolder(LayoutInflater.from(parent.context).inflate(R.layout.face_row, parent, false))
    }

    override fun getItemCount() = faces.size

    override fun onBindViewHolder(faceholder: FaceHolder, position: Int) {
        val face = faces[position]

        faceholder.itemView.smilingPro.text = face.smilingProbability.toString()
        faceholder.itemView.leftEyeClose.text = face.leftEyeOpenProbability.toString()
        faceholder.itemView.rightEyeClosed.text = face.rightEyeOpenProbability.toString()


    }

    class FaceHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}