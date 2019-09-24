package com.example.foodey.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.foodey.R
import com.example.foodey.util.obtainViewModel
import com.example.foodey.viewmodel.SignupVM
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpVM: SignupVM

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
        signUpVM.name.observe(this, Observer { name ->
            // Update the view
            evName.setText(name)
        })
        signUpVM.mobile.observe(this, Observer { mobile ->
            // Update the view
            evMobile.setText(mobile)
        })
        signUpVM.password.observe(this, Observer { pw ->
            // Update the view
            evPassword.setText(pw)
        })
        signUpVM.isDataLoading.observe(this, Observer { toShowProgressBar ->
            // Update the view
            if (toShowProgressBar) {

            } else {

            }
        })
    }

    private fun handleButton() {
        btnRegister.setOnClickListener {
            signUpVM.register()
        }
    }

    private fun initVar() {
        signUpVM = obtainViewModel(SignupVM::class.java)
    }

}
