package com.example.foodey.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.foodey.R
import com.example.foodey.activities.SignUpActivity
import com.example.foodey.ui.MainActivity
import com.example.foodey.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginVM: LoginVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        initVar()

        initView()

    }


    private fun initVar() {
        loginVM = obtainViewModel(LoginVM::class.java)
    }

    private fun initView() {
        loginVM.toastMsg.observe(this, Observer { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        })

        loginVM.redirectToHomePage.observe(this, Observer {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })

        loginVM.isDataLoading.observe(this, Observer {isLoading->
            if(isLoading) pbLogin.visibility = View.VISIBLE else View.GONE
        })

        handleButton()
    }

    private fun handleButton() {
        btnLogin.setOnClickListener {
            loginVM.setMobileValue(evMobile.text.toString())
            loginVM.setPasswordValue(evPassword.text.toString())
            loginVM.login()
        }

        btnRegisterHere.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
