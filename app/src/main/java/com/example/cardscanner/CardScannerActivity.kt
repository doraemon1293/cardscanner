package com.example.cardscanner


import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_card_scanner.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CardScannerActivity : BaseCameraActivity() {
    val Tag="CardScannerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet(R.layout.layout_card_scanner)
    }

    override fun onClick(v: View?) {
        fabProgressCircle.show()
        cameraView.captureImage { cameraKitImage ->
            // Get the Bitmap from the captured shot
            getCardDetailsFromCloud(cameraKitImage.bitmap)
            runOnUiThread {
                showPreview()
                imagePreview.setImageBitmap(cameraKitImage.bitmap)
            }
        }
    }

    private fun getCardDetailsFromCloud(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val firebaseVisionTextDetector = FirebaseVision.getInstance().cloudTextRecognizer

        firebaseVisionTextDetector.processImage(image)
                .addOnSuccessListener {
                    val words = it.text.split("\n")
                    var cardNumber="Unknown"
                    var expireDate="Unknown"
                    var expireDateInt=0
                    for (word in words) {
                        Log.d(Tag, word)
                        val new_word=word.replace('s','5',true).replace('j','1',true)
                                .replace('B','8',true).replace(')','1',true)

                        val re = Regex("[^0-9/]")
                        val wordArray= re.replace(new_word, "").toCharArray()

                        Log.d(Tag, "new_word ${new_word}")
                        Log.d(Tag, "wordArray ${wordArray.joinToString("")}")
                        if ('/' in wordArray) {
                            for ((i, ch) in wordArray.withIndex()) {
                                if ((ch == '/') && (i >= 2) && (i < wordArray.size - 2) && wordArray[i - 2].isDigit() && wordArray[i - 1].isDigit() &&
                                        wordArray[i + 1].isDigit() && wordArray[i + 2].isDigit()) {
                                    val s = "" + wordArray[i - 2] + wordArray[i - 1] + '/' + wordArray[i + 1] + wordArray[i + 2]
                                    if ((s.substring(3, 5) + s.substring(0, 2)).toInt() > expireDateInt) {
                                        expireDateInt = (s.substring(3, 5) + s.substring(0, 2)).toInt()
                                        expireDate = s
                                    }
                                }

                            }
                        }
                        else if (wordArray.size==16){
                            cardNumber=wordArray.joinToString("")

                        }
                    }

                    if (cardNumber!="Unknown"&&expireDate!="Unknown") {
                        val card: MutableMap<String, String?> = HashMap()
                        card["cardNumber"] = cardNumber
                        card["expireDate"] = expireDate
                        val token = SharedUtil.getIntance(this).readShared("token", "")
                        card["token"] = token
                        card["notified"]="F"
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                        val formatted = current.format(formatter)
                        card["Added"] = formatted
                        // Add a new document with a generated ID
                        val db = FirebaseFirestore.getInstance()
                        db.collection("cards")
                                .add(card)
                                .addOnSuccessListener { documentReference -> Log.d("card_list_activity", "DocumentSnapshot added with ID: " + documentReference.id) }
                                .addOnFailureListener { e -> Log.w("card_list_activity", "Error adding document", e) }
                    }

                    tvCardNumber.text=cardNumber
                    tvCardExpiry.text=expireDate
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    Toast.makeText(baseContext, "Sorry, something went wrong!", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    fabProgressCircle.hide()
                }
    }
}