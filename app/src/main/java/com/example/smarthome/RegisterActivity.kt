package com.example.smarthome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailInput = findViewById<EditText>(R.id.register_email_input)
        val passwordInput = findViewById<EditText>(R.id.register_password_input)
        val confirmPasswordInput = findViewById<EditText>(R.id.register_confirm_password_input)
        val registerButton = findViewById<Button>(R.id.register_confirm_button)
        val backToLoginButton = findViewById<Button>(R.id.back_to_login_button)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("RegisterActivity", "Đăng ký thành công")
                        Toast.makeText(this, "Đăng ký thành công, vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
                        // Quay lại màn hình đăng nhập
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Log.e("RegisterActivity", "Đăng ký thất bại: ${task.exception?.message}")
                        Toast.makeText(this, "Đăng ký thất bại: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        backToLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}