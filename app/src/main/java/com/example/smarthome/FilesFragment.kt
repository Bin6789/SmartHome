package com.example.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class FileFragment : Fragment() {

    private lateinit var fileAdapter: FileAdapter
    private val files = mutableListOf<FileItem>()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_files, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.files_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        fileAdapter = FileAdapter(files) { file ->
            // Implement download logic here
        }
        recyclerView.adapter = fileAdapter

        val uploadButton = view.findViewById<Button>(R.id.upload_button)
        uploadButton.setOnClickListener {
            // Implement upload logic here
        }

        if (auth.currentUser != null) {
            storage.reference.child("SmartHomeApp/files/${auth.currentUser!!.uid}")
                .listAll()
                .addOnSuccessListener { result ->
                    files.clear()
                    files.addAll(result.items.map { FileItem(it.name, it.downloadUrl.toString()) })
                    fileAdapter.updateFiles(files)
                }
        }

        return view
    }
}