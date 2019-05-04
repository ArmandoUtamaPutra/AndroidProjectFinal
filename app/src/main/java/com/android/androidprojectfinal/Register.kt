package com.android.androidprojectfinal

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.androidprojectfinal.helper.UserHelper
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.register.*

class Register : AppCompatActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        btn_Register.setOnClickListener {
            if (Username.text.toString() == "" && etPassword.text.toString() == "" &&
                    Password.text.toString() == ""
            ) {
                Toast.makeText(this, "Please check your credentials again", Toast.LENGTH_SHORT).show()
            } else {
                if (tvPassword.text.toString() == etPassword.text.toString()) {
                    startActivity(Intent(this, MainActivity::class.java))
                    UserHelper(this).StatusLogin = true
                    finish()
                } else {
                    Toast.makeText(this, "Please check your credentials again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}