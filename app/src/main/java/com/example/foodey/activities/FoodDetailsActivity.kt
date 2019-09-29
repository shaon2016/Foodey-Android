
package com.example.foodey.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodey.R
import com.example.foodey.models.Food

class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var f : Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        f = intent?.extras?.getSerializable("food") as Food

    }
}
