package com.example.noteapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noteapp.MainActivity
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentUpdateNoteBinding
import com.example.noteapp.sql.NoteDatabaseHelper

class UpdateNoteFragment : Fragment() {

    private lateinit var binding: FragmentUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1
    private var updateListener: OnNoteUpdatedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNoteUpdatedListener) {
            updateListener = context
        }
    }

    interface OnNoteUpdatedListener {
        fun onNoteUpdated()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showFab(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        db = NoteDatabaseHelper(requireContext())

        noteId = arguments?.getInt("note_id", -1) ?: -1
        if (noteId == -1) {
            parentFragmentManager.popBackStack()
            return binding.root
        }
        val note = db.getNoteById(noteId)
        binding.etUpdateTitle.setText(note.title)
        binding.etUpdateContent.setText(note.content)

        binding.btnUpdateSave.setOnClickListener {
            val newTitle = binding.etUpdateTitle.text.toString()
            val newContent = binding.etUpdateContent.text.toString()
            val updatedNote = Note(noteId, newTitle, newContent)
            db.updateNote(updatedNote)
            Toast.makeText(context, "Note Updated", Toast.LENGTH_SHORT).show()
            updateListener?.onNoteUpdated()
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

}