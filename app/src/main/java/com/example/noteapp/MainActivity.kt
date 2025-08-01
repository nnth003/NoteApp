package com.example.noteapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.fragment.AddNoteFragment
import com.example.noteapp.fragment.UpdateNoteFragment
import com.example.noteapp.sql.NoteDatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), UpdateNoteFragment.OnNoteUpdatedListener,  AddNoteFragment.OnNoteAddedListener  {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NoteDatabaseHelper
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showFab(true)
        db = NoteDatabaseHelper(this)
        noteAdapter = NoteAdapter(db.getAllNotes(), supportFragmentManager, this)
        binding.noteRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.noteRecyclerView.adapter = noteAdapter

        binding.btnAdd.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_in,
                    R.anim.slide_in_right,
                    R.anim.slide_out_right
                )
                .replace(R.id.frame_layout, AddNoteFragment(), "AddNoteFragment")
                .addToBackStack("AddNoteFragment").commit()
            showFab(false)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
            if (currentFragment is AddNoteFragment || currentFragment is UpdateNoteFragment) {
                showFab(false)
            } else {
                showFab(true)
            }
        }

    }

    fun showFab(show: Boolean) {
        val fab = findViewById<FloatingActionButton>(R.id.btnAdd)
        if (show) fab.show() else fab.hide()
    }

    override fun onResume() {
        super.onResume()
        noteAdapter.refreshData(db.getAllNotes())
    }

    override fun onNoteUpdated() {
        noteAdapter.refreshData(db.getAllNotes())
    }

    override fun onNoteAdded() {
        noteAdapter.refreshData(db.getAllNotes())
        binding.noteRecyclerView.scrollToPosition(0)
    }

}