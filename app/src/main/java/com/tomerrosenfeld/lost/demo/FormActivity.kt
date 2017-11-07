package com.tomerrosenfeld.lost.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.lost_form.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Toast
import java.io.FileNotFoundException
import android.graphics.Bitmap.CompressFormat
import com.parse.*
import java.io.ByteArrayOutputStream


/**
 * Created by tomer on 11/5/17.
 */

class FormActivity : AppCompatActivity() {
    private val PICK_IMAGE_CODE = 211
    var image: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lost_form)
        Parse.initialize(this)
        ParseInstallation.getCurrentInstallation().saveInBackground()
        profile_image.setOnClickListener({
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_CODE)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_CODE && data != null) {
            try {
                val imageUri = data.data
                val imageStream = contentResolver.openInputStream(imageUri)
                image = BitmapFactory.decodeStream(imageStream)
                profile_image.setImageBitmap(image)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
    }

    public fun submit(v: View) {
        if (name.text.isBlank()) {
            name.error = "Required"
            return
        }
        name.error = null
        if (lkl.text.isBlank()) {
            lkl.error = "Required"
            return
        }
        if (phone_number.text.isBlank()) {
            phone_number.error = "Required"
            return
        }
        if (time.text.isBlank()) {
            time.error = "Required"
            return
        }
        lkl.error = null
        if (image == null) {
            Toast.makeText(this, "Please upload a picture", Toast.LENGTH_LONG).show()
            return
        }
        val person = ParseObject("People")
        person.put("name", name.text.toString())
        person.put("last_known_location", lkl.text.toString())
        person.put("phone", phone_number.text.toString())
        person.put("time", time.text.toString())
        v.alpha = 0.5f
        v.isEnabled = false
        image?.let {
            val stream = ByteArrayOutputStream()
            image?.compress(CompressFormat.PNG, 100, stream)
            val file = ParseFile("DocImage.jpg", stream.toByteArray())
            try {
                file.save()
                person.put("image", file)
                person.saveEventually({
                    finish()
                    Toast.makeText(this, "Published", Toast.LENGTH_LONG).show()
                })
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }
}
