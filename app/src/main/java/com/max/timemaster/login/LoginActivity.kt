package com.max.timemaster.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.max.timemaster.R
import com.max.timemaster.util.UserManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        btn_login.setOnClickListener {
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
                    handleFacebookAccessToken(result?.accessToken)

                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                    Log.e("Max", error.toString())

                }

            })
    }

    fun handleFacebookAccessToken(token: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    //Third step
                    //Login
                    val userId = task.result?.additionalUserInfo?.profile?.getValue("id")
                    UserManager.user.image = "https://graph.facebook.com/$userId/picture?height=500"
                    moveMainPage(task.result?.user)
                    UserManager.userEmail = task.result?.user?.email.toString()
                    Log.e(
                        "Max",
                        "${UserManager.userEmail} \n ${task.result?.user?.email.toString()}\n" +
                                " ${task.result?.user}\n" +
                                " ${task.result}\n" +
                                " ${task}"
                    )
                    UserManager.user.email = task.result?.user?.email.toString()
                    UserManager.user.name = task.result?.user?.displayName.toString()

                } else {
                    //Show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
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

