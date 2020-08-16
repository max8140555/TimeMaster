package com.max.timemaster.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.max.timemaster.MainActivity
import com.max.timemaster.MainViewModel
import com.max.timemaster.R
import com.max.timemaster.databinding.ActivityLoginBinding
import com.max.timemaster.databinding.ActivityMainBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.UserManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val viewModel by viewModels<LoginViewModel> { getVmFactory() }
    private var auth: FirebaseAuth? = null
    private var callbackManager: CallbackManager? = null
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.btnLogin.setOnClickListener {
            facebookLogin()
        }
        auth = FirebaseAuth.getInstance()
        //printHashKey()
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    private fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("public_profile", "email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    //Second step
                    viewModel.handleFacebookAccessTokenResult(result?.accessToken)
                    viewModel.user.observe(this@LoginActivity, Observer {
                        it?.let {
                            moveMainPage(viewModel.user.value)
                        }
                    })
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                    Log.e("Max", error.toString())
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

