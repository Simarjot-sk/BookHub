package com.simarjot.bookwala

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.simarjot.bookwala.cat.CollegeCatActivity
import com.simarjot.bookwala.cat.SchoolCatActivity
import com.simarjot.bookwala.helpers.Helper
import kotlinx.android.synthetic.main.activity_categories2.*

class Categories : AppCompatActivity() {
    var category: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories2)

        back_button.setOnClickListener {
            finish()
        }

        hideAllTicks()

        college.setOnClickListener {
            hideAllTicks()
            college_tick.visibility = View.VISIBLE
            category = "college"
        }
        school.setOnClickListener {
            hideAllTicks()
            school_tick.visibility = View.VISIBLE
            category = "school"
        }
        competitive.setOnClickListener {
            hideAllTicks()
            competitive_tick.visibility = View.VISIBLE
            category = "competitive"
        }
        other.setOnClickListener {
            hideAllTicks()
            others_tick.visibility = View.VISIBLE
            category = "other"
        }

        done_btn.setOnClickListener{
            Toast.makeText(this, category, Toast.LENGTH_SHORT).show()
            val editor = getSharedPreferences(Helper.BOOK_IMAGE_PREFS, Context.MODE_PRIVATE).edit()
            editor.putString(Helper.BOOK_CATEGORY, category)
            editor.apply()

            if (category.equals("school")){
                val intent = Intent(this@Categories, SchoolCatActivity::class.java)
                startActivity(intent)
            }else if (category.equals("college")){
                val intent = Intent(this@Categories, CollegeCatActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun hideAllTicks(){
        college_tick.visibility = View.GONE
        school_tick.visibility = View.GONE
        competitive_tick.visibility = View.GONE
        others_tick.visibility = View.GONE
    }
}
