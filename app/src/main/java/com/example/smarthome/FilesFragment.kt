package com.example.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FilesFragment : Fragment() {

    private lateinit var fileAdapter: FileAdapter
    private val files = mutableListOf<FileItem>()
    private val storage = FirebaseStorage.getInstance() // Sử dụng bucket mặc định
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_files, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.files_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        fileAdapter = FileAdapter(files) { file ->
            // Logic tải xuống file
            downloadFile(file)
        }
        recyclerView.adapter = fileAdapter

        val uploadButton = view.findViewById<Button>(R.id.upload_button)
        uploadButton.setOnClickListener {
            // Logic tải lên file (ví dụ: mở file picker)
            Toast.makeText(context, "Tính năng tải lên chưa được triển khai", Toast.LENGTH_SHORT).show()
            // Bạn có thể triển khai logic chọn file và tải lên ở đây
        }

        // Kiểm tra người dùng đã đăng nhập chưa
        val currentUser = auth.currentUser
        if (currentUser != null) {
            loadFiles(currentUser.uid)
        } else {
            Toast.makeText(context, "Vui lòng đăng nhập để xem file", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadFiles(userId: String) {
        val storageRef = storage.reference.child("SmartHomeApp/files/$userId")
        storageRef.listAll()
            .addOnSuccessListener { result ->
                // Xóa danh sách cũ
                files.clear()
                // Xử lý từng file
                result.items.forEach { storageReference ->
                    storageReference.downloadUrl
                        .addOnSuccessListener { uri ->
                            val fileItem = FileItem(storageReference.name, uri.toString())
                            files.add(fileItem)
                            fileAdapter.updateFiles(files)
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context, "Lỗi khi lấy URL: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Lỗi khi tải danh sách file: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun downloadFile(file: FileItem) {
        // Logic tải file (ví dụ: mở URL trong trình duyệt hoặc tải xuống thiết bị)
        Toast.makeText(context, "Đang tải xuống: ${file.name}", Toast.LENGTH_SHORT).show()
        // Bạn có thể triển khai logic tải file thực tế ở đây
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Xóa danh sách file để tránh rò rỉ bộ nhớ
        files.clear()
    }
}