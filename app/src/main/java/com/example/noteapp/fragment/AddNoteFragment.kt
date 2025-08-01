package com.example.noteapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentAddNoteBinding
import com.example.noteapp.sql.NoteDatabaseHelper

class AddNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var db: NoteDatabaseHelper

    private var addListener: OnNoteAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNoteAddedListener) {
            addListener = context
        }
    }

    interface OnNoteAddedListener {
        fun onNoteAdded()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as? MainActivity)?.showFab(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_add_note, container, false)

        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        db = NoteDatabaseHelper(requireContext())

        binding.saveButton.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val note = Note(0, title, content)
            db.insertNote(note)
            Toast.makeText(context, "Note Added", Toast.LENGTH_SHORT).show()
            addListener?.onNoteAdded()
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

}