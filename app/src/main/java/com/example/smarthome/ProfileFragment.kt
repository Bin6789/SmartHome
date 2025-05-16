package com.example.smarthome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val userNameText = view.findViewById<TextView>(R.id.user_name)
        val userEmailText = view.findViewById<TextView>(R.id.user_email)
        val lastLoginText = view.findViewById<TextView>(R.id.last_login)
        val logoutButton = view.findViewById<Button>(R.id.logout_button)

        val user = auth.currentUser
        if (user != null) {
            userEmailText.text = "Email: ${user.email}"
            database.reference.child("SmartHomeApp/users/${user.uid}")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name = snapshot.child("name").getValue(String::class.java) ?: "N/A"
                        val lastLogin = snapshot.child("lastLogin").getValue(String::class.java) ?: "N/A"
                        userNameText.text = "Name: $name"
                        lastLoginText.text = "Last Login: $lastLogin"
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        return view
    }
}