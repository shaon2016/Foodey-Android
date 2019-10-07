package com.example.foodey.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.foodey.R
import com.example.foodey.util.obtainViewModel
import com.example.foodey.viewmodel.SignUpVM
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpVM: SignUpVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initVar()
        initView()
    }

    private fun initView() {
        observeAndUpdateView()
        handleButton()
    }

    private fun observeAndUpdateView() {
        signUpVM.isDataLoading.observe(this, Observer { isLoading ->
            if (isLoading) pbSignup.visibility = View.VISIBLE else View.GONE
        })

        signUpVM.toastMsg.observe(this, Observer { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        })

        signUpVM.redirectToHomePage.observe(this, Observer {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        })

    }

    private fun handleButton() {
        btnRegister.setOnClickListener {
            signUpVM.setMobileValue(evMobile.text.toString())
            signUpVM.setPasswordValue(evPassword.text.toString())
            signUpVM.setNameValue(evName.text.toString())
            signUpVM.register()
        }
    }

    private fun initVar() {
        signUpVM = obtainViewModel(SignUpVM::class.java)
    }

}
