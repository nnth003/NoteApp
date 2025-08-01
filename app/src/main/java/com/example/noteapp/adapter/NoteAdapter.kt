package com.example.noteapp.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.fragment.UpdateNoteFragment
import com.example.noteapp.sql.NoteDatabaseHelper

class NoteAdapter(
    private var note: List<Note>,
    private val fragmentManager: FragmentManager,
    context: Context
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val db : NoteDatabaseHelper = NoteDatabaseHelper(context)

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitleItem)
        val tvContent: TextView = itemView.findViewById(R.id.tvContentItem)
        val btnUpdate: ImageView = itemView.findViewById(R.id.btnUpdate)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return note.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = note[position]
        holder.tvTitle.text = note.title
        holder.tvContent.text = note.content

        holder.btnUpdate.setOnClickListener {
            val bundle = Bundle()
            // Send the note ID to the UpdateNoteFragment
            bundle.putInt("note_id", note.id)

            val fragment = UpdateNoteFragment()
            fragment.arguments = bundle

            fragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_up_in,
                    R.anim.slide_out_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.frame_layout, fragment, "UpdateNoteFragment")
                .addToBackStack("UpdateNoteFragment")
                .commit()
        }

        holder.btnDelete.setOnClickListener {
            db.deleteNote(note.id)
            refreshData(db.getAllNotes())
        }
    }

    fun refreshData(newNote: List<Note>) {
        note = newNote
        notifyDataSetChanged()
    }
}