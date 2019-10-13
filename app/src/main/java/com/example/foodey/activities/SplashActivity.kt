package com.example.foodey.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.foodey.R
import com.example.foodey.data.P

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed({
            if (P.isLoggedIn(this)) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 3000)

    }
}
