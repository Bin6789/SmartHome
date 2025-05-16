package com.example.smarthome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FileAdapter(
    private val files: MutableList<FileItem>,
    private val onDownloadClick: (FileItem) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileNameTextView: TextView = itemView.findViewById(R.id.file_name)
        val downloadButton: Button = itemView.findViewById(R.id.download_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.fileNameTextView.text = file.name
        holder.downloadButton.setOnClickListener {
            onDownloadClick(file)
        }
    }

    override fun getItemCount(): Int = files.size

    fun updateFiles(newFiles: List<FileItem>) {
        files.clear()
        files.addAll(newFiles)
        notifyDataSetChanged()
    }
}