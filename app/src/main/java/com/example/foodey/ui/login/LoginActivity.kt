package com.example.foodey.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodey.R
import com.example.foodey.ui.signup.SignUpActivity
import com.example.foodey.ui.MainActivity
import com.example.foodey.util.obtainViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject lateinit var viewmodelFactory : ViewModelProvider.Factory

    private val  loginVM by lazy {
        obtainViewModel(LoginVM::class.java, viewmodelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        initView()

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
