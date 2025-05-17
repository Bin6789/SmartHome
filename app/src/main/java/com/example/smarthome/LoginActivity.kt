package com.example.smarthome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Kiểm tra nếu người dùng đã đăng nhập, chuyif (auth.currentUser != null) {
        ////            startActivity(Intent(this, MainActivity::class.java))
        ////            finish()
        ////            return
        ////        }ển thẳng sang MainActivity
//

        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<Button>(R.id.register_button)
        val googleSignInButton = findViewById<Button>(R.id.google_sign_in_button)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginActivity", "Đăng nhập thành công")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Log.e("LoginActivity", "Đăng nhập thất bại: ${task.exception?.message}")
                        Toast.makeText(
                            this,
                            "Đăng nhập thất bại: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginActivity", "Đăng ký thành công")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Log.e("LoginActivity", "Đăng ký thất bại: ${task.exception?.message}")
                        Toast.makeText(
                            this,
                            "Đăng ký thất bại: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        googleSignInButton.setOnClickListener {
            Toast.makeText(this, "Đăng nhập bằng Google chưa được triển khai", Toast.LENGTH_SHORT).show()
        }
    }
}