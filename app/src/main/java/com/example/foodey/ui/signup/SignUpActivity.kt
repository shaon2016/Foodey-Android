package com.example.foodey.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodey.R
import com.example.foodey.ui.MainActivity
import com.example.foodey.util.obtainViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_up.*
import javax.inject.Inject

class SignUpActivity : AppCompatActivity() {

    @Inject lateinit var viewmodelFactory: ViewModelProvider.Factory

    private val signUpVM by lazy {
        ViewModelProvider(this, viewmodelFactory).get(SignUpVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

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
            startActivity(Intent(this, MainActivity::class.java))
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


}
