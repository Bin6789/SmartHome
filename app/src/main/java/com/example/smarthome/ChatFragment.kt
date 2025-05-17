package com.example.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()
    private val database = FirebaseDatabase.getInstance("https://smarthome-4e367-default-rtdb.firebaseio.com/")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.chat_messages)
        recyclerView.layoutManager = LinearLayoutManager(context)
        messageAdapter = MessageAdapter(messages)
        recyclerView.adapter = messageAdapter

        database.reference.child("/chats/CHAT001/messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (child in snapshot.children) {
                        val message = child.getValue(Message::class.java)
                        message?.let { messages.add(it) }
                    }
                    messageAdapter.updateMessages(messages)
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        val messageInput = view.findViewById<EditText>(R.id.message_input)
        val sendButton = view.findViewById<Button>(R.id.send_button)

        sendButton.setOnClickListener {
            val text = messageInput.text.toString()
            if (text.isNotEmpty() && auth.currentUser != null) {
                val message = Message(
                    sender = auth.currentUser!!.uid,
                    text = text,
                    timestamp = System.currentTimeMillis().toString()
                )
                database.reference.child("/chats/CHAT001/messages")
                    .push().setValue(message)
                messageInput.text.clear()
            }
        }

        return view
    }
}