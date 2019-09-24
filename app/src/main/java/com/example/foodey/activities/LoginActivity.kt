package com.example.foodey.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.foodey.R
import com.example.foodey.util.obtainViewModel
import com.example.foodey.viewmodel.LoginVM
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.log

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
        loginVM.toastMsg.observe(this, Observer {msg->
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        })

        handleButton()
    }

    private fun handleButton() {
        btnLogin.setOnClickListener {
            loginVM.login()
        }
    }
}
