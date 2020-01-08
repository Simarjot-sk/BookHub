package com.simarjot.bookwala.cat

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.simarjot.bookwala.R
import com.simarjot.bookwala.helpers.Helper
import com.skydoves.powerspinner.createPowerSpinnerView
import kotlinx.android.synthetic.main.activity_school_cat.*

class SchoolCatActivity : AppCompatActivity() {
    var doneClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_cat)
        val prefs = getSharedPreferences(Helper.BOOK_IMAGE_PREFS, Context.MODE_PRIVATE)
        val coverImageUri = prefs.getString(Helper.COVER_IMAGE, null)
        cover_image.setImageURI(Uri.parse(coverImageUri))

        title_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(doneClicked){
                    if(text.isNullOrBlank()){
                        setError(title_et, "Title is mandatory")
                    }else{
                        removeError(title_et, "Book Title")
                    }
                }
            }
        })

        subject_et.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if(doneClicked){
                    if(text.isNullOrBlank()){
                        setError(subject_et, "Subject is mandatory")
                    }else{
                        removeError(subject_et, "Subject")
                    }
                }
            }
        })

        done_btn.setOnClickListener{
            doneClicked = true
            if(title_et.text.isNullOrBlank()){
                setError(title_et, "Title is mandatory")
            }

            if(subject_et.text.isNullOrBlank()){
                setError(subject_et, "Subject is mandatory")
            }

        }
    }

    private fun setError(view: EditText, msg: String){
        view.setBackgroundResource(R.drawable.background_error)
        view.hint = msg
    }
    private fun removeError(view: EditText, msg: String){
        view.setBackgroundResource(R.drawable.rounded_edit_text_white)
        view.hint = msg
    }
}
